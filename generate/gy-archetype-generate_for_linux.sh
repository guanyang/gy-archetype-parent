#!/bin/bash
echo "请确保当前机器已经配置maven环境变量！"
#检查maven版本，采用call执行，避免闪退
mvn -version

#设置全局package、appCode、version默认值
package="org.gy.demo"
appCode="demo"
version="1.0.0-SNAPSHOT"

#设置全局模板archetypeGroupId，archetypeArtifactId，archetypeVersion
#1、gy-archetype-admin-web：后台web服务模板
#2、gy-archetype-business：业务逻辑模板
#3、gy-archetype-service：中台接口模板
#4、gy-archetype-util：工具类模板
#5、gy-archetype-pom：主pom模板
#6、gy-archetype-service-web：中台web服务模板
#7、gy-archetype-web：前台web服务模板
archetypeGroupId="org.gy.archetype"
archetypeArtifactId_admin_web="gy-archetype-admin-web"
archetypeArtifactId_business="gy-archetype-business"
archetypeArtifactId_service="gy-archetype-service"
archetypeArtifactId_util="gy-archetype-util"
archetypeArtifactId_pom="gy-archetype-pom"
archetypeArtifactId_service_web="gy-archetype-service-web"
archetypeArtifactId_web="gy-archetype-web"
archetypeVersion="1.0-SNAPSHOT"
#输出空行
echo ""
#设置package/appCode/version属性
setProperty(){
	echo $1
	read propertyValue
	if [ $propertyValue ]
	then
		eval $2=$propertyValue
		echo "您设置的$2属性值：$propertyValue"
		echo "设置 $2 成功。。。"
		menu
	else
		echo '输入不能为空，请重新输入！'
		setProperty $1 $2
	fi

}
#生成模块函数
generateModule(){
	echo "执行$4开始。。。"

	echo "archetypeGroupId：$1" 
	echo "archetypeArtifactId：$2" 
	echo "archetypeVersion：$3" 
	echo "模块名称：$4"

	mvn archetype:generate -DarchetypeGroupId=$1 -DarchetypeArtifactId=$2 -DarchetypeVersion=$3 -DinteractiveMode=false -DarchetypeCatalog=local -DgroupId=$package -Dversion=$version -DartifactId=$appCode
	mv $appCode ${appCode}"-"${4}
	echo "执行$4成功。。。"

}
#一步生成所有模块
buildAll(){
	echo '执行generate开始。。。'
	#主pom工程
	generateModule $archetypeGroupId $archetypeArtifactId_pom $archetypeVersion pom
	
	#工具类工程jar，供各个工程使用
	generateModule $archetypeGroupId $archetypeArtifactId_util $archetypeVersion util
	
	#业务逻辑工程jar，供sample-admin-web、sample-service-web使用
	generateModule $archetypeGroupId $archetypeArtifactId_business $archetypeVersion business
	
	#中台接口服务jar，供sample-web、sample-service-web使用
	generateModule $archetypeGroupId $archetypeArtifactId_service $archetypeVersion service
	
	#后台web工程war，提供后台管理服务
	generateModule $archetypeGroupId $archetypeArtifactId_admin_web $archetypeVersion admin-web
	
	#中台接口服务实现war，提供远程服务
	generateModule $archetypeGroupId $archetypeArtifactId_service_web $archetypeVersion service-web
	
	#前台web工程war，提供前台展示服务
	generateModule $archetypeGroupId $archetypeArtifactId_web $archetypeVersion web
	
	echo '执行generate成功...'
	menu
}
#分步生成模块
stepBuild(){
	echo '*********generate步骤引导************'
	echo '1）generate pom，请输入：1'
	echo '2）generate util请输入：2'
	echo '3）generate business，请输入：3'
	echo '4）generate service，请输入：4'
	echo '5）generate admin-web，请输入：5'
	echo '6）generate service-web，请输入：6'
	echo '7）generate web，请输入：7'
	echo '8）返回设置引导，请输入：8'
	echo '*********generate步骤引导*************'

	read aNum
	case $aNum in
    1)  
		#主pom工程
		echo '你选择了生成 主pom工程'
		generateModule $archetypeGroupId $archetypeArtifactId_pom $archetypeVersion pom
    	stepBuild
    ;;
    2)  
		#工具类工程jar，供各个工程使用
		echo '你选择了生成 工具类工程 util'
		generateModule $archetypeGroupId $archetypeArtifactId_util $archetypeVersion util
    	stepBuild
    ;;
    3)  
		#业务逻辑工程jar，供sample-admin-web、sample-service-web使用
		echo '你选择了生成 业务逻辑工程 business'
		generateModule $archetypeGroupId $archetypeArtifactId_business $archetypeVersion business
   		stepBuild
    ;;
    4)  
		#中台接口服务jar，供sample-web、sample-service-web使用
		echo '你选择了生成 中台接口服务'
		generateModule $archetypeGroupId $archetypeArtifactId_service $archetypeVersion service
    	stepBuild
    ;;
    5)  
		#后台web工程war，提供后台管理服务
		echo '你选择了生成 后台web工程 amdin-web'
		generateModule $archetypeGroupId $archetypeArtifactId_admin_web $archetypeVersion admin-web
    	stepBuild
    ;;
    6)  
		#中台接口服务实现war，提供远程服务
		echo '你选择了生成 中台接口服务实现功能 service-web'
		generateModule $archetypeGroupId $archetypeArtifactId_service_web $archetypeVersion service-web
   		stepBuild
    ;;
    7)  
		#前台web工程war，提供前台展示服务
		echo '你选择了生成 前台web工程 web'
		generateModule $archetypeGroupId $archetypeArtifactId_web $archetypeVersion web
    	stepBuild
    ;;
    8)  
		#返回主菜单
		menu
    ;;
    *)  
		echo '你必须输入1 到 8 之间的数字'
		stepBuild
    ;;
	esac
}
#generate提示确认
generateTip(){
	echo "您设置的package属性值：$package"
	echo "您设置的appCode属性值：$appCode"
	echo "您设置的version属性值：$version"

	echo "generate批量处理请选择1，分步操作请选择2，退出请选择3"
	read aNum
	case $aNum in
    1)  
		echo '你选择了generate全部工程'
		buildAll
    ;;
    2)  
		echo '你选择了分步generate工程'
		stepBuild
    ;;
    3)  
		echo '你选择了退出'
		menu
    ;;
    *)  
		echo '你必须输入1 到 3 之间的数字'
		generateTip
    ;;
	esac
}
#菜单选择
menu(){
	echo '*********设置引导*************'
	echo '1）设置package，示例：org.gy.demo，请输入：1'
	echo '2）设置appCode，示例：demo，请输入：2'
	echo '3）设置version，示例：1.0.0-SNAPSHOT，请输入：3'
	echo '4）执行generate，请输入：4'
	echo '5）退出操作，请输入：5'
	echo '*********设置引导*************'
	read aNum
	case $aNum in
    1)  
		echo '你选择了设置 package'
		setProperty "请输入package属性，示例：org.gy.demo" "package"
    ;;
    2)  
		echo '你选择了设置 appCode'
		setProperty "请输入appCode属性，示例：demo" "appCode"
    ;;
    3)  
		echo '你选择了设置 version'
		setProperty "请输入version属性，示例：1.0.0-SNAPSHOT" "version"
    ;;
    4)  
		echo '你选择了执行 generate'
		generateTip
    ;;
    5)  
		echo '你选择了退出'
    ;;
    *)  
		echo '你必须输入1 到 5 之间的数字'
		menu
    ;;
	esac
}
#初始化调用函数
menu
