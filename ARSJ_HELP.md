# ARSJ使用帮助

## 阅前须知
**使用ARSJ，请确保你有一定的计算机动手能力！**
**使用ARSJ，请确保你有一定的计算机动手能力！**
**使用ARSJ，请确保你有一定的计算机动手能力！**

然后，需要部署ARSJ的机器能够通过外网连接，包括但不限于利用 `内网穿透技术（frp）`、`云服务器（阿里云、腾讯云）`。

## 原生部署ARSJ（直接运行在Windows环境）
1. 从 [ARSJ](https://github.com/H4rry217/AimRobot-server4j/releases) 下载最新版本压缩包，并从压缩包中找到 `AimRobot-server4j` 或 文件名中带有 `x64_Native_Windows` 类似字样的 **exe程序**。
2. 将exe程序放入个单独的文件夹（建议），然后点击开启即可。

## 使用Java部署
请确保你以安装了Java17及以上版本（其他版本不保证是否会出现问题）

1. 从 [ARSJ](https://github.com/H4rry217/AimRobot-server4j/releases) 下载最新版本压缩包，并从压缩包中找到 `jar` 后缀的程序包。
2. 将jar程序放入个单独的文件夹（建议），然后切换到对应的目录，使用 `java -jar 程序名.jar` 开启即可（Windows可以使用cmd来执行）。

## ARSJ的设置
ARSJ默认拥有2个配置文件 `application.yml` 和 `application-setting.yml`，并内置在了程序中。
如果你需要修改ARSJ的一些设置（如启动端口等），那么可以在ARSJ程序的同目录下新建对应的yml文件，并填入对应的设置（需要修改哪个设置填入哪个）。

ARSJ若更新新的功能设置，也会同步更新新的设置说明在下方。

1. **application.yml**
```yml
server:
  port: 11451
  #代表ARSJ启动的端口，默认为11451端口
```

2. **application-setting.yml**
```yml
robot-websocket:
  token: "AimRobotServer4j"
	#表示AimRobotLite和QQ机器人连接ARSJ服务器时，所必须的验证令牌
	#ARSJ会判断数据请求数据中的令牌是否和ARSJ设置中的令牌相等
	#若设置为 "" 则代表每次启动都会随机生成令牌，建议不要随意泄露令牌
```

## 指令
直接在ARSJ控制台中输入对应指令

- **getToken**
  查看当前ARSJ的Token令牌