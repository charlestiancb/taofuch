该包的使用方法：

由于该包中集成了所有采购项目与其它系统的远程调用配置，在使用时，注意引入自己需要的文件！
classpath下：
context/remote/remote-context.xml：这里面包含了这个模块中所有定义的远程调用，包括所有的xmlrpc远程调用，使用时注意！
context/remote/xmlrpc-config.xml：这里面包含了这个模块中所有定义的xmlrpc远程调用。
context/remote/system：这个包下面定义的分别是通过Hessian调用各个系统的定义。