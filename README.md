<div align="center">

# AimRobot-server4j
This is a remote web service for AimRobot series tool

这是一个配套于AimRobot系列工具（如**AimRobotLite**）的远程服务端。
借助这个服务端，你可以搭建自己的中继服务器，可以让任何一个AimRobotLite连接至中继服务器，进而实现远程控制、数据交互等功能。

<p> 
  <a href="mailto:hcilnete@gmail.com"><img src="https://img.shields.io/badge/Email-hcilnete@gmail.com-blue" height="16px" /> </a>
  <a href="https://github.com/H4rry217/AimRobot-server4j/releases"><img src="https://img.shields.io/badge/Release下载-AimRobot_server4j-red" height="16px" /> </a>
</p> 

</div>

## 发电地址
**[爱发电](https://afdian.net/a/h4rry217)**

![AFDIAN](_staticres/afdian-H4rry217.jpg "二维码")

## 使用须知
* 本项目AimRobot-server4j (以下简称ARSJ) 是基于`Java17`、`SpringBoot3`技术栈的服务端程序，如果使用者希望搭建自己的ARSJ中继服务器，**需要有一定的动手能力**。
* ARSJ可以使用GraalVM构建原生Native应用，所以发布对应Release版本时，不仅会提供Jar程序，同时也会提供Windows下的Native可执行程序。
* ARSJ遵循 `AGPL3.0` 协议，作者也不会禁止使用者对于ARSJ源码的任何二次修改，也就是意味着 **你可以拿ARSJ干任何你喜欢的事**。
* ARSJ允许你远程管理AimRobotLite，但通常不会直接操作ARSJ，而是搭配对应的 [Mirai](https://github.com/mamoe/mirai) ~~（QQ机器人）~~ 插件来使你能够在**QQ群远程管理**。

## 使用方法
参考 **[使用文档](ARSJ_HELP.md)**

## 关联项目
- **[AimRobotLite](https://github.com/H4rry217/AimRobotLite)**
- **[AimRobotQQBot](https://github.com/H4rry217/AimRobot-QQBot)**

## 联系方式
如果程序使用途中出现BUG等问题，而你又不会在GitHub上开Issue，那么你可以通过 hcilnete@gmail.com 联系我