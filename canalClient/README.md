# 简介
canal，译意为水道/管道/沟渠，主要用途是基于 MySQL 数据库增量日志解析，提供增量数据订阅和消费——canal官网

这句介绍有几个关键字：增量日志，增量数据订阅和消费。我们可以简单地把canal理解为一个用来同步增量数据的一个工具。

接下来我们看一张官网提供的示意图：

![canal](https://github.com/bigliu66/coding/blob/master/canalClient/src/main/resources/picture/canal.png)

canal的工作原理就是把自己伪装成MySQL slave，模拟MySQL slave的交互协议向MySQL Mater发送 dump协议，MySQL mater收到canal发送过来的dump请求，开始推送binary log给canal，然后canal解析binary log，再发送到存储目的地，比如MySQL，Kafka，Elastic Search等等。
# 版本
canal 1.1.5
# 依赖
```$xslt
<dependency>
			<groupId>com.alibaba.otter</groupId>
			<artifactId>canal.client</artifactId>
			<version>1.1.5</version>
		</dependency>

		<dependency>
			<groupId>com.alibaba.otter</groupId>
			<artifactId>canal.protocol</artifactId>
			<version>1.1.5</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-amqp</artifactId>
		</dependency>
```
# 搭建canal
## 1.首先有一个MySQL服务器
当前的 canal 支持源端 MySQL 版本包括 5.1.x , 5.5.x , 5.6.x , 5.7.x , 8.0.x

我安装的MySQL服务器是5.7版本。MySQL的安装这里就不演示了，比较简单，网上也有很多教程。

然后在MySQL中需要创建一个用户，并授权：

```$xslt
-- 使用命令登录：mysql -u root -p
-- 创建用户 用户名：canal 密码：Canal@123456
create user 'canal'@'%' identified by 'Canal@123456';
-- 授权 *.*表示所有库
grant SELECT, REPLICATION SLAVE, REPLICATION CLIENT on *.* to 'canal'@'%' identified by 'Canal@123456';
```
下一步在MySQL配置文件my.cnf设置如下信息：
```$xslt
[mysqld]
# 打开binlog
log-bin=mysql-bin
# 选择ROW(行)模式
binlog-format=ROW
# 配置MySQL replaction需要定义，不要和canal的slaveId重复
server_id=1
```
改了配置文件之后，重启MySQL，使用命令查看是否打开binlog模式：
```$xslt
show VARIABLES LIKE 'log_bin'
```
查看binlog日志文件列表：
```$xslt
show binary logs
```
查看当前正在写入的binlog文件：
```$xslt
show master status
```
## 2.安装canal服务端

去官网下载页面进行下载：https://github.com/alibaba/canal/releases

我这里下载的是1.1.5的版本（1.1.5新增了rabbitmq的支持）。

解压canal.deployer-1.1.4.tar.gz，接着打开配置文件conf/example/instance.properties，配置信息如下：
```$xslt
#################################################
## mysql serverId , v1.0.26+ will autoGen
# canal.instance.mysql.slaveId=0

# enable gtid use true/false
canal.instance.gtidon=false

# position info
canal.instance.master.address=127.0.0.1:3306
canal.instance.master.journal.name=binlog.000023
canal.instance.master.position=179
canal.instance.master.timestamp=
canal.instance.master.gtid=

# rds oss binlog
canal.instance.rds.accesskey=
canal.instance.rds.secretkey=
canal.instance.rds.instanceId=

# table meta tsdb info
canal.instance.tsdb.enable=true
#canal.instance.tsdb.url=jdbc:mysql://127.0.0.1:3306/canal_tsdb
#canal.instance.tsdb.dbUsername=canal
#canal.instance.tsdb.dbPassword=canal

#canal.instance.standby.address =
#canal.instance.standby.journal.name =
#canal.instance.standby.position =
#canal.instance.standby.timestamp =
#canal.instance.standby.gtid=

# username/password
canal.instance.dbUsername=root
canal.instance.dbPassword=root
canal.instance.connectionCharset = UTF-8
# enable druid Decrypt database password
canal.instance.enableDruid=false
#canal.instance.pwdPublicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALK4BUxdDltRRE5/zXpVEVPUgunvscYFtEip3pmLlhrWpacX7y7GCMo2/JM6LeHmiiNdH1FWgGCpUfircSwlWKUCAwEAAQ==

# table regex
canal.instance.filter.regex=.*\\..*
# table black regex
canal.instance.filter.black.regex=mysql\\.slave_.*
# table field filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)
#canal.instance.filter.field=test1.t_product:id/subject/keywords,test2.t_company:id/name/contact/ch
# table field black filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)
#canal.instance.filter.black.field=test1.t_product:subject/product_image,test2.t_company:id/name/contact/ch

# mq config
canal.mq.topic=canal.routing.key
# dynamic topic route by schema or table regex
#canal.mq.dynamicTopic=mytest1.user,mytest2\\..*,.*\\..*
canal.mq.partition=0
# hash partition config
#canal.mq.partitionsNum=3
#canal.mq.partitionHash=test.table:id^name,.*\\..*
#canal.mq.dynamicTopicPartitionNum=test.*:4,mycanal:6
#################################################
```
配置文件conf/canal.properties，配置信息如下：
```$xslt
...
# tcp, kafka, rocketMQ, rabbitMQ
canal.serverMode = rabbitMQ
...
##################################################
######### 		    RabbitMQ	     #############
##################################################
rabbitmq.host =127.0.0.1
rabbitmq.virtual.host =/
rabbitmq.exchange =canal.exchange
rabbitmq.username =admin
rabbitmq.password =admin
rabbitmq.deliveryMode =2
```
我这里用的是win10系统，所以在bin目录下找到startup.bat启动：启动就报错，坑呀：

要修改一下启动的脚本startup.bat：删掉下面这句，然后再启动脚本：
```$xslt
-Dlogback.configurationFile="%logback_configurationFile%"
```
## 3.canal客户端
见本项目

# 总结
依次启动mysql、rabbitmq、canal server、canal client，当mysql有数据修改操作时，在rabbitmq中应该就可以看到同步消息了。