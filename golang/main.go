package main

import (
	"encoding/json"
	"fmt"
	p "golang/pkg" // module/package
	"strings"
	"unsafe"
)

func main() {

	// 变量与常量
	// var_const()

	// 数据类型
	// data_type()

	// 流程控制
	// logical_flow()

	// 数组
	// array()

	// 切片
	// slice()

	// 哈希表
	// hashmap()

	// 函数
	// function()

	// 异常
	// exception()

	// 指针
	// pointer()

	// 结构体
	// structure()

	// 包
	// pkg()

	// 接口
	// interf()

	// 反射

}

func interf() {

	// 在Go语言中接口（interface）是一种类型，一种抽象的类型

	// type 接口类型名 interface{
	// 	方法名1( 参数列表1 ) 返回值列表1
	//	方法名2( 参数列表2 ) 返回值列表2
	// }

	// Go语言的接口在命名时，一般会在单词后面添加er，突出该接口的类型含义

	// Go语言的 interface 是非侵入式的，不像 Java 的 interface 实现需要显示的声明
	// 一个对象只要全部实现了接口中的方法，那么就实现了这个接口

	// 接口类型变量
	// 接口类型变量能够存储所有 实现了该接口 的实例

	a := s{f1: 1}
	var _ ier = a // check，确保实例实现了接口
	a.i1()

	var b ier
	b = a // 结构体类型 和 结构体指针类型 都可以赋值接口类型变量
	b = &a
	b.i1()

	// 一个接口的方法，不一定需要由一个类型完全实现，接口的方法可以通过在类型中嵌入其他类型或者结构体来实现

	// 接口与接口间可以通过嵌套创造出新的接口

	// 空接口
	// 空接口是指没有定义任何方法的接口，空接口类型的变量可以存储任意类型的变量
	// 作用： 1. 作为函数的参数 2. 作为map的值

	// 类型断言
	// x.(T)  -> 接口类型.(具体类型)
	c, e := b.(*s)
	fmt.Println(c, e) // &{1} true

}

type ier interface {
	i1()
}

type s struct {
	f1 int
}

func (s s) i1() {
	fmt.Println(s.f1)
}

func pkg() {
	a := p.A
	b := p.B
	p.O()
	c := p.C{
		F: 1,
	}

	fmt.Println(a, b, c)
}

// 结构体
func structure() {

	// 类型定义
	type myInt int

	// 结构体
	// 声明类型
	type s struct {
		// 字段大写开头表示可公开访问，小写表示私有（仅在定义当前结构体的包中可访问）
		f1 int
		f2 int
	}

	// 实例化、初始化
	var a s = s{}
	a.f1 = 1
	a.f2 = 1
	fmt.Println(a)

	c := s{
		f1: 1,
		f2: 1,
	}
	fmt.Println(c)

	d := &s{
		f1: 1,
		f2: 1,
	}
	fmt.Println(d, *d)

	b := new(s)
	b.f1 = 1
	b.f2 = 1
	fmt.Println(b, *b)

	e := &s{
		1,
		1,
	}
	fmt.Println(e, *e)

	// 构造函数
	f := newSt(6)
	fmt.Println(f)

	// Go语言中的方法（Method）是一种作用于【特定类型变量】的函数
	// func (接收者变量 接收者类型) 方法名(参数列表) (返回参数) {}

	// 值类型接收者（副本）
	f.m1(7)
	fmt.Println(f)

	// 指针类型接收者（本体）
	f.m2(8)
	fmt.Println(f)

	// 可为自定义的任意类型添加方法
	// type myInt int
	// func (m myInt) m() {}

	// 继续效果
	// 通过 结构体嵌套 实现【继承】效果：指可以让某个类型的对象获得另一个类型的对象的属性的方法
	ch := &child{
		&parent{
			id: 1,
		},
	}
	ch.getPId()
	ch.getCId()

	// 序列化与反序列化
	type u struct {
		F1 int // json序列化是默认使用字段名作为key
		F2 int `json:"f_2"` // 通过指定tag实现json序列化该字段时的key
		f3 int // 私有不能被json包访问
	}

	v := &u{
		F1: 1,
		F2: 2,
		f3: 3,
	}

	data, _ := json.Marshal(v)
	fmt.Println(string(data))

	w := &u{}
	json.Unmarshal(data, w)
	fmt.Println(w)

}

type parent struct {
	id int
}

func (p parent) getPId() {
	fmt.Println(p.id)
}

type child struct {
	// p *parent
	*parent // 用嵌套匿名结构体，才可以省略 c.p.id 的 p
}

func (c child) getCId() {
	// fmt.Println(c.p.id)
	fmt.Println(c.id)
}

type st struct {
	f1 int
}

func newSt(f1 int) *st {
	return &st{
		f1: f1,
	}
}

func (st st) m1(f1 int) {
	st.f1 = f1
}

func (st *st) m2(f1 int) {
	st.f1 = f1 // s.f1 -> 成员变量指针
}

func pointer() {

	// 区别于C/C++中的指针，Go语言中的指针不能进行偏移和运算，是安全指针

	// 3个概念：指针类型、指针地址和指针取值
	// 指针变量
	//		【值类型】（int、float、bool、string、array、struct）都有对应的【指针类型】，如：*int、*int64、*string等
	// 指针取址 -> 返回指针变量
	//		对变量进行取地址（&）操作，可以获得这个变量的【指针变量】，指针变量的值是指针地址
	a := 10
	b := &a
	var c *int = &a
	// 指针取值
	//		对指针变量进行取值（*）操作，可以获得指针变量指向的原变量的值
	d := *b
	*b = 11
	fmt.Println(a, b, c, d) // 11 0xc00000a098 0xc00000a098 10

	// 【值类型】变量，声明不需要分配内存空间，是因为它们在声明的时候已经默认分配好了内存空间

	// 【引用类型】变量（eg 指针变量、slice、map、channel），使用的时候不仅要声明它，还要为它分配内存空间，否则我们的值就没办法存储
	// var x *int
	// *x = 10
	// var y map[int]int
	// y[100] = 100

	// 内存分配：

	// new
	// func new(Type) *Type  -> 返回指针变量
	var e *int = new(int)
	*e = 10

	// make
	// 区别于new，它只用于slice、map以及channel的内存创建
	// func make(t Type, size ...IntegerType) Type -> 返回 Type，因为 Type 本身就是引用类型
	var f map[int]int = make(map[int]int, 10)
	f[1] = 1

}

func exception() {

	// defer
	// 当 defer 所在函数即将返回时，将延迟处理的语句按 defer 定义的逆序进行执行，即：先被defer的语句最后被执行，最后被defer的语句，最先被执行。
	// 函数 return 在底层不是原子操作，它分为 给返回值赋值 和 RETURN指令 两步。defer 语句执行的时机就在 返回值赋值操作 后，RET指令 执行前。
	e1()

	// panic / recover
	// Go语言中目前（Go1.12）是没有异常机制，是使用panic/recover模式来处理错误。
	// panic可以在任何地方引发，但recover只有在defer调用的函数中有效。

	// e3()	// panic
	e4() // panic with recover

}

func e4() {
	defer func() {
		err := recover()
		if err != nil {
			fmt.Println(err)
			fmt.Println("recover e4")
		}
	}()
	panic("failed e4")
}

func e3() {
	panic("failed e3")
}

func e1() {
	x := 1
	y := 2
	// defer 注册要延迟执行的函数时该函数所有的参数都需要确定其值
	// defer 注册的是外层函数
	defer e2("AA", x, e2("A", x, y))
	x = 10
	defer e2("BB", x, e2("B", x, y))
	y = 20

}

func e2(s string, a, b int) int {
	ret := a + b
	fmt.Println(s, a, b, ret)
	return ret
}

func function() {

	// Go语言中支持函数、匿名函数和闭包，函数属于一等公民

	// 基础：可变参数 + 多返回值
	a, b := f1(1, 2, []int{1, 2}...) // ... 语法糖 1. 函数可以接受多个不确定数量的参数 2. 切片可以被打散进行传递
	fmt.Println(a, b)

	// 进阶：函数类型 (type 重命名)
	type fx func(a int, b int, c ...int) (int, int)
	var c fx
	c = f1
	a, b = c(1, 2, []int{1, 2}...)
	fmt.Println(a, b)

	// 高阶

	// 函数作为入参（lambda 作为入参）
	a, b = f2(3, 4, c)
	fmt.Println(a, b)

	// 函数作为返回值
	d := f3()
	a, b = d(3, 4, 5)
	fmt.Println(a, b)

	// 匿名函数（函数中定义函数，即 lambda）
	e := func(a, b int) int { return a + b }
	f := e(3, 4)
	fmt.Println(f)

	// 闭包（将外部变量放入函数对象，导致生命周期跟随函数对象）
	// 1.
	g := f4()
	fmt.Println(g(1))
	fmt.Println(g(2))
	// 2.
	h := func() func() {
		i := 127
		return func() { fmt.Println(i) } // 将 i 放入 lambda 内，i 生命周期跟随 lambda
	}
	h()
	// 3.
	for i := 0; i < 10; i++ {
		m := func() { fmt.Println(i) }
		fmt.Println(m) // 都是相同的地址，很奇怪跟 java 不一样，代码块局部变量？垃圾回收？堆栈？
	}

	// 一些内置的函数
	// close	主要用来关闭channel
	// len		用来求长度，比如string、array、slice、map、channel
	// new		用来分配内存，主要用来分配值类型，比如int、struct。返回的是指针
	// make		用来分配内存，主要用来分配引用类型，比如chan、map、slice
	// append	用来追加元素到数组、slice中
	// panic recover	用来做错误处理
}

func f4() func(y int) int {
	x := 0
	// 将 x 放入 lambda 内，x 生命周期跟随 lambda
	return func(y int) int {
		x += y
		return x
	}
}

func f3() func(a int, b int, c ...int) (int, int) {
	return f1
}

func f2(a, b int, fx func(a int, b int, c ...int) (int, int)) (int, int) {
	return fx(a, b)
}

func f1(a int, b int, c ...int) (int, int) {
	s := a + b
	m := a * b
	for _, v := range c {
		s += v
		m *= v
	}
	return s, m
}

// 哈希表
func hashmap() {

	// map是引用类型
	a := map[int]int{1: 1, 2: 2}
	fmt.Println(a)
	b := make(map[int]int)

	// add
	b[1] = 1
	b[2] = 2
	fmt.Println(b)

	// exists
	v, e := a[1]
	fmt.Println(v, e)

	// delete
	delete(a, 1)

	// foreach
	for v, i := range b {
		fmt.Println(v, i) // 遍历map时的元素顺序与添加键值对的顺序无关
	}

	// k: v -> slice
	c := map[int][]int{1: []int{1}, 2: []int{1, 2}}
	fmt.Println(c)

	// []map
	d := []map[int]int{map[int]int{1: 1}, map[int]int{1: 1, 2: 2}}
	fmt.Println(d)

}

// 切片
func slice() {

	// 数组的长度是固定，基础类型
	a := [3]int{}
	fmt.Println(a)

	// 切片是一个拥有相同类型元素的可变长度的序列，引用类型
	// 切片的本质就是对底层数组的封装，它包含了三个信息：底层数组的指针、切片的长度（len）和切片的容量（cap）
	d := [8]int{0, 1, 2, 3, 4, 5, 6, 7}
	e := d[2:4]         // idx [start:end)
	fmt.Println(e)      // [2 3]
	fmt.Println(len(e)) // 4 - 2 = 2
	fmt.Println(cap(e)) // 8 - 2 = 6

	m := []int{2, 3}
	fmt.Println(m)      // [2 3]
	fmt.Println(len(m)) // 2
	fmt.Println(cap(m)) // 2

	f := make([]int, 2, 10)
	fmt.Println(f)      // [0 0]
	fmt.Println(len(f)) // 2
	fmt.Println(cap(f)) // 10

	// 不能使用 == 操作符来判断两个切片是否含有全部相等元素 （数组可以）
	// 判断一个切片是否是空的，要是用 len(s) == 0 来判断，不应该使用 s == nil 来判断
	var g []int         // len(g) == 0, cap(g) == 0, g == nil
	h := []int{}        // len(h) == 0, cap(g) == 0, h != nil
	j := make([]int, 0) // len(j) == 0, cap(g) == 0, j != nil
	fmt.Println(g, h, j)

	// 切片是指针类型
	o := []int{1, 2}
	p := o
	p[1] = 3
	fmt.Println(o, p)

	// copy()
	s := []int{1, 2}
	t := []int{0}
	copy(t, s) // s -> t，得有 cap 才能填充
	fmt.Printf("%v\t%p\n", s, s)
	fmt.Printf("%v\t%p\n", t, t)

	// append()
	// 每个切片会指向一个底层数组，这个数组的容量够用就添加新增元素。
	// 当底层数组不能容纳新增的元素时，切片就会自动按照一定的策略进行扩容，此时该切片指向的底层数组就会更换。
	// 扩容操作往往发生在 append() 调用时，所以我们通常都需要用原变量接收 append() 的返回值
	r := []int{}
	for i := 0; i < 10; i++ {
		r = append(r, i, i)
		fmt.Printf("%v\tlen:%d\tcap:%d\tptr:%p\n", r, len(r), cap(r), r)
	}

	// remove
	z := []int{1, 2, 3, 4, 5}
	z = append(z[:2], z[3:]...) // [0,2) [3,...)
	fmt.Println(z)

}

// 数组
func array() {

	// 数组有固定的长度

	// 初始化
	a := [3]int{}
	b := [...]int{1, 2, 3} // ... 数组专用
	z := []int{1, 2, 3}    // 切片
	fmt.Println(a, b, z)

	c := [...][3]int{ // 列数需要明确指定
		{1, 1},
		{2, 2},
		{3, 3},
	}
	fmt.Println(c)

	// 遍历
	for i := 0; i < len(a); i++ {
	}
	for i, v := range a {
		fmt.Println(i, v)
	}

	// 数组是值类型（需要指针）

	// [n]*T 指针数组
	e, f := 1, 2
	g := [2]*int{&e, &f}
	fmt.Println(g)

	// *[]T 数组指针
	h := [3]int{1, 2, 3}
	var i *[3]int = &h
	fmt.Println(i)

	// 数组支持 ==、!= 操作符，因为会初始化
	j := [3]int{}
	k := [3]int{1}
	l := [4]int{}
	fmt.Println(j == k) // false
	fmt.Println(l)      // 数组长度不同的变量，无法比较

}

// 流程控制
func logical_flow() {

	// if
	if a := 50; a >= 100 {
	} else if a <= 0 {
	} else {
	}

	// for (可实现所有循环类型)
	for i := 0; i < 10; i++ {
	}

	var j = 0
	for j < 10 {
		j++
	}

	// for range 遍历 []、slice、string、map 及 channel
	var b = []int{1, 2, 3}
	for i := range b {
		fmt.Println(i)
	}

	// switch case
	var d = 1
	switch d {
	case 1, 2, 3:
		fmt.Println("case 1")
		fallthrough // 接着执行当前case的下一个case
	case 4:
		fmt.Println("case 2")
	default:
		fmt.Println("case default")
	}

	// break continue

	// goto
	for i := 0; i < 10; i++ {
		for j := 0; j < 10; j++ {
			goto step
			fmt.Println("loop")
		}
	}
step: // 标签
	fmt.Println("goto step")

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

	// 字符 rune
	var o uint8 = 'a' // ASCII码  0~127
	var p rune = '中'  // UTF-8字符，int32
	fmt.Printf("%d(%c)\n", o, o)
	fmt.Printf("%d(%c)\n", p, p)

	// 字符串（原生数据类型）
	var m string = "hello world"
	var n string = `hello
world`
	fmt.Println(m)
	fmt.Println(n)
	strings.Contains(m, n) // strings 工具包

	// 修改字符串，需先转成数组
	var q []rune = []rune(m)
	q[0] = 'g'
	var r string = string(q) // 重新分配内存，复制字节数组
	fmt.Println(r)

	// 类型转换（只有强制转换）  T()
	var s float64 = 1.1
	var t int = int(s)
	fmt.Println(s, t)

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
