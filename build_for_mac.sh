#!/bin/sh
echo '请确保当前机器已经配置maven环境变量！'
#检查maven版本
mvn -version
#换行
echo ""
echo "请耐心等待编译完成。。。"
#设置全局installKey,deployKey默认值
installKey='install'
deployKey='deploy'

#执行命令函数
callCommand(){
	echo "执行命令$1 开始。。。"
	mvn clean $1  -Dmaven.test.skip=true -Dfile.encoding=UTF-8 -Dmaven.javadoc.skip=false -U -T 1C
	echo "执行命令$1 结束。。。\n"
}

menu(){
echo 'mvn install请选择1，mvn deploy请选择2，退出请选择3'
	read aNum
	case $aNum in
	    1)  
			echo '你选择了 mvn install'
			callCommand "$installKey"
	    ;;
	    2)  
			echo '你选择了 mvn deploy'
			callCommand "$deployKey"
	    ;;
	    3)  
			echo '你选择了退出'
	    ;;
	    *)  
			echo '你必须输入1 到 3 之间的数字'
			menu
	    ;;
	esac
}
#初始化调用函数
menu

