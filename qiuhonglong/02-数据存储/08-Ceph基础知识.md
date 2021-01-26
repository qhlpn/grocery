http://docs.ceph.org.cn/man/8/ceph/

https://ceph.io/geen-categorie/documentation-for-ceph-rest-api/



一文讲清楚Ceph分布式存储  https://mp.weixin.qq.com/s/TWXPPk7hE1D4AGsHg7CIMg

干货分享: Ceph原理、架构等知识梳理总结  https://mp.weixin.qq.com/s/mMfXbUqN9-SmBgc1gXdCTg

Ceph分布式存储系列（一）：Ceph工作原理及架构浅析梳理  https://blog.csdn.net/weixin_43860781/article/details/109053152

Crush算法：https://mp.weixin.qq.com/s/bkn0MvCgAZFA0WzusFwvaw   https://mp.weixin.qq.com/s/S5pGjydu4pRCBIgiAjxgYQ

一篇文章让你理解Ceph的三种存储接口(块设备、文件系统、对象存储)  https://zhuanlan.zhihu.com/p/69997558



### 存储方案

<img src="E:\projects\grocery\qiuhonglong\02-数据存储\pictures\image-20210111200839478.png" alt="image-20210111200839478" style="zoom:80%;" />

+ **DAS(Direct-attached Storage)：**直连存储裸设备，物理接口插拔，本地操作
+ **NAS(Network Attached Storage)：** **网络**附加存储，**共享**文件系统，如NFS服务器
+ **SAN(Storage Area Network)：**存储区域**网络**，**共享**裸设备。
+ **OS(Object Storage)：** 整合任意可达磁盘，存储**静态不可编辑文件**





### Ceph特点

> Ceph可以将多台服务器组成集群，把这些机器中的磁盘资源整合到一块，形成资源池（支持PB级别 K M G T P），按需分配给客户端使用。

+ **应用层**支持三种存储：块存储、文件存储、对象存储
+ 提供**CRUSH分布式**存储，去中心，可扩展，无需单点维护元数据
+ 数据**强一致性**，写入OSD时先同步完副本再返回响应



### Ceph结构

<img src="E:\projects\grocery\qiuhonglong\02-数据存储\pictures\image-20210107153729915.png" alt="image-20210107153729915" style="zoom: 75%;" />



+ **RADOS(Reliable Autonomic Object Store)：**底层完整的对象存储系统（Ceph中所有数据都以对象形式存储），RADOS主要包括OSD和Monitor
+ **LIBRADOS：**客户端直接访问RADOS所需的基础库
+ **上层应用接口：**提供抽象层次更高的应用接口
  + **RGW：**提供对象存储接口（区分RADOS对象）
  + **RBD：**提供块存储设备接口
  + **CephFS：**提供文件系统接口



### Ceph组件

+ **osd(object storage daemon)：**ceph通过osd管理物理硬盘，每一块盘对应一个osd进程，**负责处理集群数据存储、复制、恢复、均衡等**
+ **mon(monitor)：**维护cluster map（五张表）信息，包括其它组件元信息、crush算法信息，**保证集群数据一致性**  **Paxos算法**
+ **mgr(manager)：**监控ceph集群，**采集存储利用率、系统负载等运行指标**，并对外提供ui dashboard界面
+ **mds(metadata server)：**专为**cephfs文件存储**提供的**元数据缓存服务器**，提高查询性能



### RADOS存储

>  一个文件首先按照配置的大小切分成多个对象，对象经过哈希算法映射到不同的归置组PG中，每个PG再通过C-RUSH算法将对象存储到经状态过滤的多个OSD中（第一个OSD是主节点，其它的是副本从节点）。



<img src="E:\projects\grocery\qiuhonglong\02-数据存储\pictures\image-20210108092906061.png" alt="image-20210108092906061" style="zoom: 80%;" />



+ **File：**用户需要存储或访问的文件
+ **Objects：**RADOS基本存储单元，即对象，由File进行切分（ID / Binary Data / Metadata）
+ **PG(Placement Group)：**用于放置多个Objects，映射到多个OSD上（一主多从）。是实现上的逻辑概念，物理上不存在。PG数量固定，不会随着OSD动态伸缩。
+ **PGP：**相当于PG存放的OSD副本排列组合。假设PG映射到3个osd，即osd1，osd2，osd3，副本数为2，如果pgp=1，那么pg存放的副本osd的组合就有一种，比如（osd1, osd2）
+ **Pool**：对多个PG设置相同的Namespace，定义成逻辑概念上的Pool，可以对不同的业务场景做隔离
+ **OSDs：**每一块物理硬盘对应一个osd进程
+ **Bucket：**C-RUSH算法树的中间节点（区别于rgw对象存储中的bucket）

> 上面的映射可归纳为：file → (Pool, Object) → (Pool, PG) → OSD set → OSD/Disk



### CRUSH算法

> Object在通过PG存储到实际的OSD设备上时，会通过C-RUSH算法，按照预定好的规则选择N个OSD进行存储，即CRUSH(pgid) --> (osd1,osd2 ...)。不同对象存储到OSD设备位置无必然联系，对相同对象进行重复计算，其存储位置必然相同。	

<img src="E:\projects\grocery\qiuhonglong\02-数据存储\pictures\image-20210107163431027.png" alt="image-20210107163431027" style="zoom: 67%;" />

> ceph通过cluster_map存储分布式集群的结构拓扑，集群物理结构定义为树形结构，默认10种层级，每个**中间节点**称为**bucket**，**叶子节点**一定是**osd**

> 若以host为单位，如host0，则落到dev0 dev1
>
> 若以rack为单位，如rack0，则落到dev0 ... 8个



### 用户管理

> https://is-cloud.blog.csdn.net/article/details/89679912   用户角色 + 资源列表 + 动作列表 + 操作权限

+ **User：**用户命名遵循 \<TYPE.ID> 规则，如 client.admin 。TYPE 有 client/mon/osd/mgr 类型
+ **Capabilty：**对用户进行 mon/osd/mds 某些操作进行授权 / 能力



### 集群管理

