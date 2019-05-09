@echo off

::echo ��ǰ�̷���%~d0
::echo ��ǰ�̷���·����%~dp0
::echo ��ǰ�̷���·���Ķ��ļ�����ʽ��%~sdp0
::echo ��ǰ������ȫ·����%~f0
::echo ��ǰCMDĬ��Ŀ¼��%cd%
::��ת����ǰ·�������ǿ����ת����/d����������ת
::cd /d %cd%

@echo ��ȷ����ǰ�����Ѿ�����maven����������
::���maven�汾������callִ�У���������
call mvn -version

::����ȫ��package��appCode��versionĬ��ֵ
@set "package=org.gy.demo"
@set "appCode=demo"
@set "version=1.0.0-SNAPSHOT"

::����ȫ��ģ��archetypeGroupId��archetypeArtifactId��archetypeVersion
::1��gy-archetype-admin-web����̨web����ģ��
::2��gy-archetype-business��ҵ���߼�ģ��
::3��gy-archetype-service����̨�ӿ�ģ��
::4��gy-archetype-util��������ģ��
::5��gy-archetype-pom����pomģ��
::6��gy-archetype-service-web����̨web����ģ��
::7��gy-archetype-web��ǰ̨web����ģ��
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
::echo=�������

@echo *********��������*************

@echo 1������package��ʾ����org.gy.demo�������룺1

@echo 2������appCode��ʾ����demo�������룺2

@echo 3������version��ʾ����1.0.0-SNAPSHOT�������룺3

@echo 4��ִ��generate�������룺4

@echo 5���˳������������룺5

@echo *********��������*************

::/p��ȡ�û������ֵ
set /p select=����������ѡ��
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
	@echo ���������������������룡
	goto start
)

::�������ԣ�����1����������������2��ȫ�ֱ������ã�%1��ʾ��һ���������Դ�����
:setProperty

	SETLOCAL
	@echo ����%1��ʼ... 
	
	set /p property=������%1�������룺
	
	if "%property%"=="" (
		@echo �������벻��Ϊ�գ����������룡
		goto start
	)
	( ENDLOCAL  
		set "%2=%property%" 
		@echo �����õ�%1����ֵ��%property%		
	) 

	@echo ����%1�ɹ�...
	
	goto:eof

::generate��ʾȷ��	
:generateTip
	
	@echo �����õ�package����ֵ��%package%
	
	@echo �����õ�appCode����ֵ��%appCode%
	
	@echo �����õ�version����ֵ��%version%
	
	choice  /C BSC /M "generate���������밴B���ֲ������밴S��ȡ���밴C" 
	
	if %errorlevel%==1 (
		goto build
	)
	if %errorlevel%==2 (
		goto step
	)
	if %errorlevel%==3 (
		goto start
	)


::generateָ��ģ�飬����1��archetypeGroupId������2��archetypeArtifactId������3��archetypeVersion������4��ģ������
:generateModule
	SETLOCAL
	@echo ִ��%4��ʼ... 
	
	@echo archetypeGroupId��%1 
	@echo archetypeArtifactId��%2 
	@echo archetypeVersion��%3 
	@echo ģ�����ƣ�%4
	
	call mvn archetype:generate -DarchetypeGroupId=%1 -DarchetypeArtifactId=%2 -DarchetypeVersion=%3 -DinteractiveMode=false -DarchetypeCatalog=local -DgroupId=%package% -Dversion=%version% -DartifactId=%appCode%
	
	::��ͣ3�룬�������������֡��ܾ����񡱴���ֱ��ping����
	ping -n 3 127.0.0.1>nul
	
	rename %appCode% %appCode%-%4
	
	ENDLOCAL

	@echo ִ��%4�ɹ�...
	
	goto:eof

::һ������	
:build

  	@echo ִ��generate��ʼ...
	
	::��pom����
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-pom%,%archetypeVersion%,pom
	
	::�����๤��jar������������ʹ��
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-util%,%archetypeVersion%,util
	
	::ҵ���߼�����jar����sample-admin-web��sample-service-webʹ��
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-business%,%archetypeVersion%,business
	
	::��̨�ӿڷ���jar����sample-web��sample-service-webʹ��
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service%,%archetypeVersion%,service
	
	::��̨web����war���ṩ��̨�������
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-admin-web%,%archetypeVersion%,admin-web
	
	::��̨�ӿڷ���ʵ��war���ṩԶ�̷���
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service-web%,%archetypeVersion%,service-web
	
	::ǰ̨web����war���ṩǰ̨չʾ����
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-web%,%archetypeVersion%,web
	
	@echo ִ��generate�ɹ�...

	goto start

::�ֲ�����
:step

@echo *********generate��������************

@echo 1��generate pom�������룺1

@echo 2��generate util�����룺2

@echo 3��generate business�������룺3

@echo 4��generate service�������룺4

@echo 5��generate admin-web�������룺5

@echo 6��generate service-web�������룺6

@echo 7��generate web�������룺7

@echo 8���������������������룺8

@echo *********generate��������*************

set /p selectStep=����������ѡ��
if %selectStep%==1 (
	::��pom����
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-pom%,%archetypeVersion%,pom
	goto step
)
if %selectStep%==2 (
	::�����๤��jar������������ʹ��
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-util%,%archetypeVersion%,util
	goto step
)
if %selectStep%==3 (
	::ҵ���߼�����jar����sample-admin-web��sample-service-webʹ��
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-business%,%archetypeVersion%,business
	goto step
)
if %selectStep%==4 (
	::��̨�ӿڷ���jar����sample-web��sample-service-webʹ��
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service%,%archetypeVersion%,service
	goto step
)
if %selectStep%==5 (
	::��̨web����war���ṩ��̨�������
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-admin-web%,%archetypeVersion%,admin-web
	goto step
)
if %selectStep%==6 (
	::��̨�ӿڷ���ʵ��war���ṩԶ�̷���
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-service-web%,%archetypeVersion%,service-web
	goto step
)
if %selectStep%==7 (
	::ǰ̨web����war���ṩǰ̨չʾ����
	call:generateModule %archetypeGroupId%,%archetypeArtifactId-web%,%archetypeVersion%,web
	goto step
)
if %selectStep%==8 (
	goto start
)
else (
	@echo ���������������������룡
	goto step
)		
	
:exit
	exit	