# HbaseTest
convert json file to hbase
老师的项目
尝试将从微博处抓取的json文件解析后存储进Hbase
使用的条件是抓取微博的磁盘用CIFS（SAMBA）共享到内网处，此代码运行于第三端，写入第二端的HBase中
写入的效率取决于网络（机房网络大概1M/S）大概40分钟读取并写入1.8GB的数据（平均值）


有问题可以联系我~

希望有人能喜欢
