package pkg

import (
	"context"
	"fmt"
	corev1 "k8s.io/api/core/v1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/labels"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/runtime/schema"
	"k8s.io/client-go/discovery"
	"k8s.io/client-go/discovery/cached/disk"
	"k8s.io/client-go/dynamic"
	"k8s.io/client-go/informers"
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/kubernetes/scheme"
	"k8s.io/client-go/rest"
	"os"
	"time"
)

func ClientGoFunc() {

	//RESTClient()
	//ClientSet()
	//DynamicClient()
	//DiscoveryClient()
	//GVRCache()
	//ListWatch()
	//ShareInformer()
	SampleController()

}

var config = &rest.Config{
	Host:        "https://istack-dev-k8s.ctcdn.cn",
	BearerToken: "eyJhbGciOiJFUzUxMiIsImtpZCI6IlB2TnVCZlBMc1RRRDZYYUNnWEJscV84OWJ1c3RMQ1M5MkwydXBMeU9VWHcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJuZXN0LXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJuZXN0LWFkbWluLXRva2VuLW1zYjVkIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6Im5lc3QtYWRtaW4iLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiIyMTY2MGM0Ny1mMjI4LTRjNGEtOTU0Ni00MzQzYjNjNTk1NzkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6bmVzdC1zeXN0ZW06bmVzdC1hZG1pbiJ9.Ac3kUrNfqV7D8vNucuxNe4HM5494OdOvDUFgSw_MhOfb9pxy0LSszcAR-gbnHgMAcd2RxHk1X19Uk5AUEKR5dJEtAUfE4K3_4UCL3Vt-6wLnI5xOCZXmLTuEpktdLBWoU9tQHwMvLWX8f7j5aXlynfbf2LyLOR4A0Zmad3iwNDUyAXbn",
}

/**
RESTClient 最基础的客户端，主要是对 HTTP 请求进行了封装，支持 Json 和 Protobuf 格式的数据。
*/
func RESTClient() {

	pwd, _ := os.Getwd()
	fmt.Println(pwd)

	// 加载配置文件，生成 config 对象 root/.kube/config
	// config, err := clientcmd.BuildConfigFromFlags("", "kube_conf.yaml")
	// if err != nil {
	//	 panic(err.Error())
	// }

	// 配置 API 路径
	config.APIPath = "api"
	// 配置分组版本
	config.GroupVersion = &corev1.SchemeGroupVersion
	// 配置数据的编解码器
	config.NegotiatedSerializer = scheme.Codecs

	// 实例化 RESTClient
	restClient, err := rest.RESTClientFor(config)
	if err != nil {
		panic(err.Error())
	}

	// 定义返回接收值
	result := &corev1.PodList{}
	err = restClient.Get().
		Namespace("default").                                                    // 查询的 Namespace
		Resource("pods").                                                        // 查询的资源类型
		VersionedParams(&metav1.ListOptions{Limit: 100}, scheme.ParameterCodec). // 参数及序列化工具
		Do(context.TODO()).                                                      // 发送请求
		Into(result)                                                             // 写入返回值
	if err != nil {
		panic(err.Error())
	}
	// 输出返回结果
	for _, d := range result.Items {
		fmt.Printf("namespace: %v, name: %v, status: %v\n", d.Namespace, d.Name, d.Status.Phase)
	}

}

/**
ClientSet 负责操作 Kubernetes 内置的资源对象，例如：Pod、Service等
*/
func ClientSet() {

	// 实例化 ClientSet
	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	// 查询 default 下的 pods 部门资源信息
	pods, err := clientset.
		CoreV1().                                  // 实例化资源客户端，这里标识实例化 CoreV1Client
		Pods("default").                           // 选择 namespace，为空则表示所有 Namespace
		List(context.TODO(), metav1.ListOptions{}) // 查询 pods 列表
	if err != nil {
		panic(err.Error())
	}

	// 输出 Pods 资源信息
	for _, item := range pods.Items {
		fmt.Printf("namespace: %v, name: %v\n", item.Namespace, item.Name)
	}
}

/**
DynamicClient 动态客户端，可以对任意的 Kubernetes 资源对象进行通用操作，包括 CRD
*/
func DynamicClient() {

	// 实例化 DynamicClient
	dynamicClient, err := dynamic.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	// 设置要请求的 GVR
	gvr := schema.GroupVersionResource{
		Group:    "",
		Version:  "v1",
		Resource: "pods",
	}

	// 发送请求，并得到返回结果
	unStructData, err := dynamicClient.Resource(gvr).Namespace("default").List(context.TODO(), metav1.ListOptions{})
	if err != nil {
		panic(err.Error())
	}

	// 使用反射将 unStructData 的数据转成对应的结构体类型，例如这是是转成 v1.PodList 类型
	podList := &corev1.PodList{}
	err = runtime.DefaultUnstructuredConverter.FromUnstructured(
		unStructData.UnstructuredContent(),
		podList,
	)
	if err != nil {
		panic(err.Error())
	}

	// 输出 Pods 资源信息
	for _, item := range podList.Items {
		fmt.Printf("namespace: %v, name: %v\n", item.Namespace, item.Name)
	}
}

/**
DiscoveryClient 发现客户端，负责发现 APIServer 支持的资源组、资源版本和资源信息的。
*/
func DiscoveryClient() {

	// 实例化 DiscoveryClient
	discoveryClient, err := discovery.NewDiscoveryClientForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	_, apiResources, err := discoveryClient.ServerGroupsAndResources()
	if err != nil {
		panic(err.Error())
	}

	for _, list := range apiResources {
		gv, err := schema.ParseGroupVersion(list.GroupVersion)
		if err != nil {
			panic(err.Error())
		}
		for _, resource := range list.APIResources {
			fmt.Printf("name: %v, group: %v, version: %v\n", resource.Name, gv.Group, gv.Version)
		}
	}
}

/**
GVR数据其实很少变动，因此我们可以缓存GVR数据，来减少Client与APIServer交互
缓存到本地：CachedDiscoveryClient
缓存到内存：memCacheClient
在运行K8s机器上，缓存信息默认存储于/.kube/cache和/.kube/http-cache
*/
func GVRCache() {

	// 实例化客户端，本客户端负责将GVR数据缓存到本地文件中
	cachedDiscoveryClient, err := disk.NewCachedDiscoveryClientForConfig(config, "./cache/discovery", "./cache/http", time.Minute*60)
	if err != nil {
		panic(err.Error())
	}

	/*
		1) 先从缓存文件中找GVR数据，有则直接返回，没有则调用APIServer
		2) 调用APIServer获取GVR数据
		3) 将获取到的GVR数据缓存到本地，然后返回给客户端
	*/
	_, apiResourcesList, err := cachedDiscoveryClient.ServerGroupsAndResources()
	if err != nil {
		panic(err.Error())
	}

	for _, list := range apiResourcesList {
		gv, err := schema.ParseGroupVersion(list.GroupVersion)
		if err != nil {
			panic(err.Error())
		}

		for _, resource := range list.APIResources {
			fmt.Printf("name: %v, group: %v, version: %v\n", resource.Name, gv.Group, gv.Version)
		}
	}
}

/**
List-Watch它分为两个部分：
list负责调用资源对应的Kubernetes APIServer的restful API获取全局数据列表，并同步到本地缓存中。
watch负责监听资源变化，并调用相应的事件处理函数进行处理，同时更新本地缓存，使本地缓存与Etcd中数据保持一致。
list是基于HTTP中的短链接实现。watch则是基于HTTP长链接实现
*/
func ListWatch() {

	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	// 调用监听的方法
	watch, err := clientset.AppsV1().Deployments("kube-system").Watch(context.TODO(), metav1.ListOptions{}) //返回一个通道
	if err != nil {
		panic(err.Error())
	}
	for {
		select {
		//多路复用
		case e, _ := <-watch.ResultChan(): //监听通道，实时拿到最新消息
			fmt.Println(e.Type, e.Object)
			//e.Type表示事件变化的类型， Added ， Delete等
			//e.Object 表示变化后的数据
		}
	}
}

/**
Informer会将资源缓存在本地以供自己后续使用
但K8s中运行了很多控制器，有很多资源需要管理，难免会出现一个资源受到多个控制器管理
为了应对这种常见，可以通过ShareInformer来创建一份供多个控制器共享的缓存，减少系统内存开销，和Watch连接数量。
*/
func ShareInformer() {

	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}

	// 初始化 sharedInformerFactory
	sharedInformerFactory := informers.NewSharedInformerFactory(clientset, 0)

	// 生成 podInformer
	podInformer := sharedInformerFactory.Core().V1().Pods()

	// 生成 indexer 用于查询数据
	indexer := podInformer.Lister()

	// 启动 sharedInformerFactory
	sharedInformerFactory.Start(nil)

	// 等待数据同步完成
	sharedInformerFactory.WaitForCacheSync(nil)

	// 利用 indexer 获取数据
	pods, err := indexer.List(labels.Everything())
	if err != nil {
		panic(err.Error())
	}
	for _, pod := range pods {
		fmt.Printf("namespace: %v, name: %v\n", pod.Namespace, pod.Name)
	}
}

func SampleController() {
	// https://github.com/kubernetes/sample-controller
	// + https://andblog.cn/3196
	// + https://blog.51cto.com/daixuan/5175780
}
