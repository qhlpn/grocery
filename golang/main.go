package main

import (
	"fmt"
	"strings"
	"unsafe"
)

func main() {

	//var_const()
	data_type()

}

// 数据类型
func data_type() {

	// 整数
	var a int = 10
	fmt.Printf("%d %b %o %x %X \n", a, a, a, a, a)
	var b int = 0b1010
	var c int = 0o12
	var d int = 0xa
	var e int = 0xA
	fmt.Println(b, c, d, e)

	// var f int = 0xffffffffffffffff   overflows int
	var g uint = 0xffffffffffffffff
	var h uintptr = uintptr(unsafe.Pointer(&g)) // uintptr 是一个整数类型，unsafe.Pointer 是一个指针类型
	fmt.Println(g, h)

	// 浮点数
	var i float32 = 1.0
	var j float64 = 1.0
	fmt.Printf("%f %f \n", i, j)

	// 布尔
	var k, l bool = true, false
	fmt.Println(k, l)

	// 字符串（原生数据类型）
	var m string = "hello world"
	var n string = `hello
world`
	fmt.Println(m)
	fmt.Println(n)
	strings.Contains(m, n) // strings 工具包

	// 字符 rune
	var o uint8 = 'a' // ASCII码  0~127
	var p rune = '中'  // UTF-8字符，int32
	fmt.Printf("%d(%c)\n", o, o)
	fmt.Printf("%d(%c)\n", p, p)

}

// 变量与常量
func var_const() {

	// 变量
	var a int = 8
	b := 8
	x, y := 8, 8
	fmt.Println(a, b, x, y)

	// 匿名变量（无需使用时）
	var _ = 8
	_ = 8

	// 常量
	const c = 8

	// 常量计数器（iota是const语句块中的行索引）
	const (
		d = iota     // 0
		e = iota + 2 // 1 + 2 = 3
		f = iota     // 2
	)
	fmt.Println(d, e, f)
	const (
		g = iota     // 0
		h = iota + 2 // 3
		i            // 4 顺延
	)
	fmt.Println(g, h, i)

}
