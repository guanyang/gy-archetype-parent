@echo off

::echo 当前盘符：%~d0
::echo 当前盘符和路径：%~dp0
::echo 当前盘符和路径的短文件名格式：%~sdp0
::echo 当前批处理全路径：%~f0
::echo 当前CMD默认目录：%cd%
::跳转到当前路径，添加强制跳转参数/d，可任意跳转
::cd /d %cd%

@echo 请确保当前机器已经配置maven环境变量！
::检查maven版本，采用call执行，避免闪退
call mvn -version

::设置全局package、appCode、version默认值
@set "package=org.gy.demo"
@set "appCode=demo"
@set "version=1.0.0-SNAPSHOT"

::设置全局模板archetypeGroupId，archetypeArtifactId，archetypeVersion
::1、gy-archetype-admin-web：后台web服务模板
::2、gy-archetype-business：业务逻辑模板
::3、gy-archetype-service：中台接口模板
::4、gy-archetype-util：工具类模板
::5、gy-archetype-pom：主pom模板
::6、gy-archetype-service-web：中台web服务模板
::7、gy-archetype-web：前台web服务模板
@set "archetypeGroupId=org.gy.archetype"
@set "archetypeArtifactId-admin-web=gy-archetype-admin-web"
@set "archetypeArtifactId-business=gy-archetype-business"
@set "archetypeArtifactId-service=gy-archetype-service"
@set "archetypeArtifactId-util=gy-archetype-util"
@set "archetypeArtifactId-pom=gy-archetype-pom"
@set "archetypeArtifactId-service-web=gy-archetype-service-web"
@set "archetypeArtifactId-web=gy-archetype-web"
@set "archetypeVersion=1.0-SNAPSHOT"

:start
::echo=输出空行

@echo *********设置引导*************

@echo 1）设置package，示例：org.gy.demo，请输入：1

@echo 2）设置appCode，示例：demo，请输入：2

@echo 3）设置version，示例：1.0.0-SNAPSHOT，请输入：3

@echo 4）执行generate，请输入：4

@echo 5）退出操作，请输入：5

@echo *********设置引导*************

::/p获取用户输入的值
set /p select=请输入您的选择：
if %select%==1 (
	call:setProperty "package",package
	goto start
)
if %select%==2 (
	call:setProperty "appCode",appCode
	goto start
)
if %select%==3 (
	call:setProperty "version",version
	goto start
)
if %select%==4 (
	goto generateTip
)
if %select%==5 (
	goto exit
)
else (
	@echo 您的输入有误，请重新输入！
	goto start
)

::设置属性，参数1：设置描述，参数2：全局变量引用，%1表示第一个参数，以此类推
:setProperty

	SETLOCAL
	@echo 设置%1开始... 
	
	set /p property=请设置%1，请输入：
	
	if "%property%"=="" (
		@echo 您的输入不能为空，请重新输入！
		goto start
	)
	( ENDLOCAL  
		set "%2=%property%" 
		@echo 您设置的%1属性值：%property%		
	) 

	@echo 设置%1成功...
	
	goto:eof

::generate提示确认	
:generateTip
	
	@echo 您设置的package属性值：%package%
	
	@echo 您设置的appCode属性值：%appCode%
	
	@echo 您设置的version属性值：%version%
	
	choice  /C BSC /M "generate批量处理请按B，分步操作请按S，取消请按C" 
	
	if %errorlevel%==1 (
		goto build
	)
	if %errorlevel%==2 (
		goto step
	)
	if %errorlevel%==3 (
		goto start
	)


::generate指定模块，参数1：archetypeGroupId，参数2：archetypeArtifactId，参数3：archetypeVersion，参数4：模块名称
:generateModule
	SETLOCAL
	@echo 执行%4开始... 
	
	@echo archetypeGroupId：%1 
	@echo archetypeArtifactId：%2 
	@echo archetypeVersion：%3 
	@echo 模块名称：%4
	
	call mvn archetype:generate -DarchetypeGroupId=%1 -DarchetypeArtifactId=%2 -DarchetypeVersion=%3 -DinteractiveMode=false -DarchetypeCatalog=local -DgroupId=%package% -Dversion=%version% -DartifactId=%appCode%
	
	::暂停3秒，避免重命名出现“拒绝服务”错误，直接ping本机
	ping -n 3 127.0.0.1>nul
	
	rename %appCode% %appCode%-%4
	
	ENDLOCAL

	@echo 执行%4成功...
	
	goto:eof

::一步操作	
:build

  	@echo 执行generate开始...
	
	::主pom工程
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-pom%,%archetypeVersion%,pom
	
	::工具类工程jar，供各个工程使用
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-util%,%archetypeVersion%,util
	
	::业务逻辑工程jar，供sample-admin-web、sample-service-web使用
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-business%,%archetypeVersion%,business
	
	::中台接口服务jar，供sample-web、sample-service-web使用
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service%,%archetypeVersion%,service
	
	::后台web工程war，提供后台管理服务
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-admin-web%,%archetypeVersion%,admin-web
	
	::中台接口服务实现war，提供远程服务
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service-web%,%archetypeVersion%,service-web
	
	::前台web工程war，提供前台展示服务
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-web%,%archetypeVersion%,web
	
	@echo 执行generate成功...

	goto start

::分步操作
:step

@echo *********generate步骤引导************

@echo 1）generate pom，请输入：1

@echo 2）generate util请输入：2

@echo 3）generate business，请输入：3

@echo 4）generate service，请输入：4

@echo 5）generate admin-web，请输入：5

@echo 6）generate service-web，请输入：6

@echo 7）generate web，请输入：7

@echo 8）返回设置引导，请输入：8

@echo *********generate步骤引导*************

set /p selectStep=请输入您的选择：
if %selectStep%==1 (
	::主pom工程
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-pom%,%archetypeVersion%,pom
	goto step
)
if %selectStep%==2 (
	::工具类工程jar，供各个工程使用
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-util%,%archetypeVersion%,util
	goto step
)
if %selectStep%==3 (
	::业务逻辑工程jar，供sample-admin-web、sample-service-web使用
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-business%,%archetypeVersion%,business
	goto step
)
if %selectStep%==4 (
	::中台接口服务jar，供sample-web、sample-service-web使用
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service%,%archetypeVersion%,service
	goto step
)
if %selectStep%==5 (
	::后台web工程war，提供后台管理服务
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-admin-web%,%archetypeVersion%,admin-web
	goto step
)
if %selectStep%==6 (
	::中台接口服务实现war，提供远程服务
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service-web%,%archetypeVersion%,service-web
	goto step
)
if %selectStep%==7 (
	::前台web工程war，提供前台展示服务
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-web%,%archetypeVersion%,web
	goto step
)
if %selectStep%==8 (
	goto start
)
else (
	@echo 您的输入有误，请重新输入！
	goto step
)		
	
:exit
	exit	