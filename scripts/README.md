
# 交通数据模拟脚本

执行trafficdatamocker.py自动生成交通数据并发送到kafka队列。

使用方法：

* 确认安装了python 2.7或以上版本
* 安装依赖库
  * pip install kafka-python
* 配置kafka broker和topic，请修改脚本中的`brokers`和`topic`
* 执行脚本  
`python trafficdatamocker.py`