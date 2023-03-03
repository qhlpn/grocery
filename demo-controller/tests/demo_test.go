package tests

import (
	"context"
	clientset "demo-controller/pkg/generated/clientset/versioned"
	"fmt"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/client-go/tools/clientcmd"
	"k8s.io/klog/v2"
	"testing"
)

func TestDemo(t *testing.T) {

	cfg, err := clientcmd.BuildConfigFromFlags("", "/etc/rancher/k3s/k3s.yaml")
	if err != nil {
		klog.Fatalf("Error building kubeconfig: %s", err.Error())
	}

	demoClient, err := clientset.NewForConfig(cfg)
	if err != nil {
		klog.Fatalf("Error building demo clientset: %s", err.Error())
	}
	
	demos, err := demoClient.
		DemoV1(). // 实例化资源客户端，这里标识实例化 CoreV1Client
		Demos(""). // 选择 namespace，为空则表示所有 Namespace
		List(context.TODO(), metav1.ListOptions{}) // 查询 pods 列表
	if err != nil {
		panic(err.Error())
	}

	for _, item := range demos.Items {
		fmt.Printf("namespace: %v, name: %v\n", item.Namespace, item.Name)
	}

}
