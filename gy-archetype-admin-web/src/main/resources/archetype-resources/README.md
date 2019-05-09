# ${artifactId}-admin-web  
1)针对静态资源压缩static-zip.xml，确保指定路径下存在js或css，否则不会输出<outputDirectory>${basedir}/target/static-compress/project</outputDirectory>，
容易出现static-compress not exist；  
2）由于使用模板工程构建自定义工程时，会将#{var}全部转换成#var，故需要修改src/main/resources/conf/spring/spring-servlet.xml中（40行）#current为#{current}