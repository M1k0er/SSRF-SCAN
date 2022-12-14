# 描述

SSRF-SCAN是一款被动式扫描ssrf的burpsuite插件，通过eyes.sh来探测是否对外发起请求。

# 如何使用

首先需要在eyes.sh在注册一个账号，并获取你的token和请求地址

![](https://pingo78.oss-cn-hangzhou.aliyuncs.com/images/image-20221213223910640.png)

- git clone https://github.com/M1k0er/SSRF-SCAN.git

- mvn package

- 下载插件包并编译成jar包后，在burpsuite上安装

  ![](https://pingo78.oss-cn-hangzhou.aliyuncs.com/images/image-20221213224745638.png)

- 配置第一步请求地址和token

  ![](https://pingo78.oss-cn-hangzhou.aliyuncs.com/images/image-20221213224849157.png)

# 扫描效果

![](https://pingo78.oss-cn-hangzhou.aliyuncs.com/images/image-20221213225331848.png)