module golang

go 1.17

require (
	// 导包
)

replace (

	// 适用于 无法导包（换个地址） / 对旧包做修改（fork增量） 等情况
	// replace old pkg => new pkg
	// require new pkg

)

// go init   初始化当前文件夹, 创建go.mod文件
// go get    下载依赖包
// go run    运行
// go build  编译生成可执行文件
