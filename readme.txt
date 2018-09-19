生成知识图谱： 运行里面的main.java
运行之后在output文件夹里面会找到所有生成的txt文件
生成的txt文件中第一行为header, 类似于数据库中表中的每一个字段。

目前总共有五个表，分别是Person(申请人表），Apply(进件表），Phone(通话记录表），Email(邮箱列表），Black(黑名单表）。


改变参数：在NetworkGenerator class里定义了一些变量，比如NUM_PHONES, NUM_PERONS这些是定义知识图谱中有多少个电话，有多少个申请人等等，可以相应地去改变。

为了测试期间建议不要用很大的值，比如NUM_PHONES赋予比较大的值，生成图的过程会很慢，测试期间建议在10万个以内。其他的变量对运行速度影响不大。

