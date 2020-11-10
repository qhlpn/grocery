# Prometheus

> 作用：系统监控和告警的工具



### 核心组件

![](https://www.prometheus.wang/quickstart/static/prometheus_architecture.png)

+ **Prometheus Server**

  > 实现监控数据的获取（调用Exporters HTTP接口）、存储（时序数据库）、查询（PromQL语言）

+ **Exporter**

  > 将采集监控数据的Endpoint端点，以HTTP接口形式提供给 Server（Pull模式）

+ **PushGateway**

  > 当网络隔离时，内部网络的Exporters可将数据主动推给Gateway（Push模式）进行中转，Server以相同的Pull模式向网关获取数据

+ **AlertManager**

  > Server定义PromQL告警规则产生告警信号，后续处理流程由告警系统AlertManager进行管理



### PromQL

+ **时间序列**

  ```
  <--------------- metric ---------------------><-timestamp -><-value->
  http_request_total{status="200", method="GET"}@1434417560938 => 94355
  ```

  > Prometheus 的存储是以时间序列的形式保存在TSDB（时序数据库）中

+ **Metrics 指标**

  ```
  <--- name ---><--------- labelset ----------->
  <metric name>{<label name>=<label value>, ...}
  ```

  > 标签可以用于对样子数据的过滤、聚合，名称满足 \[a-zA-Z_]\[a-zA-Z0-9\_\]*。指标类型包括：

  + **Counter：** 只增不减的计数器
  + **Gauge：** 可增可减的仪表盘
  + **Histogram：**  区间统计直方图
  + **Summary：**  分位数计算，Summary在客户端，Histogram在服务端

  > 四个黄金指标：**请求时间、系统流量、错误请求、服务饱和度**

+ **Prome SQL 操作**

  > 条件查询、时间偏移、聚合操作、操作运算符、内置函数



### Prome Server

```yaml
# /etc/prometheus/prometheus.yaml
# 全局配置
global:
  scrape_interval:     15s # 拉取数据时间间隔
  evaluation_interval: 15s # 扫描AlertRule时间间隔

# 配置 AlertRule 文件路径
rule_files:
  - "first_rules.yml"

# 配置 Alertmanager URL
alerting:
  alertmanagers:
  - static_configs:
    - targets: ['10.190.180.240:9093']
 
# 配置 Exporter 数据源
scrape_configs:
  - job_name: prometheus
    static_configs:
      - targets: ['10.190.180.240:9090']
        labels:
          instance: 'prometheus'
  - job_name: linux
    static_configs:
      - targets: ['10.190.180.240:9100']
        labels:
          instance: 'linux'
```



### Exporter

> 提供 HTTP 接口，响应 Prometheus Server 请求，返回监控样本数据，其实例又称 Target



### Alertmanager

> Prometheus Server 周期性根据 PromQL 定义的 AlertRule 告警规则进行计算，将满足触发条件的告警信息发送给 Alertmanager 进行后续处理。
> Alertmanager 除了提供基本的告警功能以外，还提供了分组聚合、抑制重复、静默处理等功能

<img src="https://www.prometheus.wang/alert/static/prometheus-alert-artich.png" style="zoom: 40%;" />

+ **AlertRule**

  ```yaml
  # /etc/prometheus/rules/rule-example.yaml 
  groups:
  - name: example  # 组名
    rules:
    - alert: HighErrorRate  # 警告名
      expr: job:request_latency_seconds:mean5m{job="myjob"} > 0.5  # PromQL触发表达式 上下文取值$value
      for: 10m  # 触发持续10m才告警
      labels:
        severity: page   # 告警附加标签  上下文取值 $labels.<labelname>
      annotations:
        summary: High request latency on {{ $labels.instance }}   # 描述告警的概要信息
        description: current value: {{ $value }}  # 描述告警的详细信息
  ```

  ```yaml
  # /etc/prometheus/prometheus.yaml
  rule_files:
    [ - <filepath_glob> ... ] # 规则文件路径
  global:
    [ evaluation_interval: <duration> | default = 1m ] # 扫描间隔
  ```

+ **Alertmanager**

  ```yaml
  # /etc/alertmanager/alertmanager.yaml 
  global:    # 全局配置
    [ resolve_timeout: <duration> | default = 5m ]
    [ smtp_from: <tmpl_string> ] 
    [ smtp_smarthost: <string> ] 
    [ smtp_auth_username: <string> ]
    [ smtp_auth_password: <secret> ]
    [ smtp_require_tls: <bool> | default = true ]
    ...
  
  templates:   # 模板配置 html email
    [ - <filepath> ... ]
  
  route: <route>  # 告警路由，分配处理方式
  
  receivers:   # 接收人，配合告警路由使用，wechat email webhook..
    - <receiver> ...
  
  inhibit_rules:  # 抑制重复规则
    [ - <inhibit_rule> ... ]
  ```
  