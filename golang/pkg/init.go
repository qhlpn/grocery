package pkg

import "fmt"

// 如果想在一个包中引用另外一个包里的标识符（如变量、常量、类型、函数等）时，该标识符必须是对外可见的（public）
// 在Go语言中只需要将标识符的首字母大写就可以让标识符对外可见了

var a = 1
var A = 2

const b = 3
const B = 4

type c struct {
	f int
}

type C struct {
	f int
	F int
}

type d interface {
	m()
}

type D interface {
	N()
	n()
}

func o() {
	fmt.Println("call o 龙龙是笨猪")
}

func O() {
	fmt.Println("call O")
}

// init 函数在包完成初始化后自动执行，并且执行优先级比 main 函数高。
func init() {
	fmt.Println("import pkg init ...")
}
