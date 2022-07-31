package pkg

import (
	"fmt"
	"net"
	"net/rpc"
	"net/rpc/jsonrpc"
	"time"
)

type RPCService struct{}

// 方法只能有两个可序列化的参数。
// channel（通道）、complex（复数类型3+4i）、func（函数）均不能进行序列化
// 其中第二个参数是指针类型，并且返回一个 error 类型。
// 同时必须是公开的方法。
func (rpcService *RPCService) Run(request string, response *string) error {
	*response = "rpc res: " + request
	return nil
}

func RPCServerRun() {

	// 注册rpc服务，维护一个hash表
	// key值是服务名称，value值是服务的地址
	_ = rpc.RegisterName("helloRPC", new(RPCService))

	listen, _ := net.Listen("tcp", "127.0.0.1:8080")
	accept, _ := listen.Accept()

	// 1. read，获取服务名称和方法名，获取请求数据
	// 2. run，调用对应服务里面的方法，获取传出数据
	// 3. write，把数据返回给客户端
	rpc.ServeConn(accept)

}

func RPCClientRun() {

	// rpc.Dial
	dial, _ := rpc.Dial("tcp", "127.0.0.1:8080")
	respond := ""

	// dial.Call 调用具体方法
	// 第一个参数是 用点号链接 RPC服务名字 和 方法名字
	_ = dial.Call("helloRPC.Run", "i am request", &respond)
	fmt.Println(respond)

}

func RPCRun() {

	//go RPCServerRun()
	//time.Sleep(5 * time.Second)
	//RPCClientRun()

	go JsonRPCServerRun()
	time.Sleep(5 * time.Second)
	JsonRPCClientRun()

}

func JsonRPCServerRun() {

	_ = rpc.RegisterName("helloRPC", new(RPCService))
	listen, _ := net.Listen("tcp", "127.0.0.1:8080")
	accept, _ := listen.Accept()

	// 基于json编解码的rpc服务
	go rpc.ServeCodec(jsonrpc.NewServerCodec(accept))
}

func JsonRPCClientRun() {

	// net.Dial
	dial, _ := net.Dial("tcp", "127.0.0.1:8080")

	// 基于json编解码的rpc服务
	client := rpc.NewClientWithCodec(jsonrpc.NewClientCodec(dial))
	respond := ""
	_ = client.Call("helloRPC.Run", "i am request", &respond)
	fmt.Println(respond)

}
