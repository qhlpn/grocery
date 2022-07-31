package pkg

import (
	"fmt"
	"io/ioutil"
	"net"
	"net/http"
	"strings"
)

func SocketRun() {
	//go tcpServer()
	//time.Sleep(5 * time.Second)
	//tcpClient()

	httpClient()
}

func tcpServer() {

	listen, _ := net.Listen("tcp", "127.0.0.1:8080")
	accept, _ := listen.Accept()
	input := make([]byte, 1024)
	ilen, _ := accept.Read(input)
	fmt.Println("sever input: " + string(input[0:ilen]))
	output := strings.ToUpper(string(input[0:ilen]))
	olen, _ := accept.Write([]byte(output))
	fmt.Println("sever output: " + string(output[0:olen]))
	_ = accept.Close()

}

func tcpClient() {

	dial, _ := net.Dial("tcp", "127.0.0.1:8080")
	output := []byte("hello world")
	olen, _ := dial.Write(output)
	fmt.Println("client output: " + string(output[0:olen]))
	input := make([]byte, 1024)
	ilen, _ := dial.Read(input)
	fmt.Println("client input: " + string(input[0:ilen]))
	_ = dial.Close()

}

func httpClient() {
	client := http.Client{}
	resp, _ := client.Get("https://www.baidu.com")

	resBody, _ := ioutil.ReadAll(resp.Body)
	fmt.Println(string(resBody))

	conType := resp.Header.Get("Content-Type")
	date := resp.Header.Get("Date")
	server := resp.Header.Get("Server")
	fmt.Println(conType + " " + date + " " + server)

	url := resp.Request.URL.String()
	code := resp.StatusCode
	status := resp.Status
	fmt.Println(url + " " + string(rune(code)) + " " + status)


}
