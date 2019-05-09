# ${artifactId}-business  
#### 由于使用模板工程构建自定义工程时，会将#{var}全部转换成#var，故排除以下文件变量过滤  
1）将src/main/resources/conf/sqlMap/SysResourceMapper.xml中占位符${package}全部替换成对应的包根路径；  
2）将src/main/resources/conf/sqlMap/SysUserMapper.xml中占位符${package}全部替换成对应的包根路径；  
3）将src/main/resources/conf/sqlMap/extend/sqlMap_sys_user.xml中占位符${package}全部替换成对应的包根路径；  
4）将src/main/resources/conf/sqlMap/extend/sqlMap_sys_user.xml中占位符${package}全部替换成对应的包根路径；  
