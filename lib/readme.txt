如下命令是将lib下的jar加载到本地maven数据仓库，Dfile是jar的路径，需要根据实际路径更改

cd trainplan/

mvn install:install-file -Dfile=lib\springside-core-4.2.2.GA.jar -DgroupId=org.springside -DartifactId=springside-core -Dversion=4.2.2.GA -Dpackaging=jar
mvn install:install-file -Dfile=lib\springside-extension-4.2.2.GA.jar -DgroupId=org.springside -DartifactId=springside-extension -Dversion=4.2.2.GA -Dpackaging=jar
mvn install:install-file -Dfile=lib\springside-metrics-4.2.2.GA.jar -DgroupId=org.springside -DartifactId=springside-metrics -Dversion=4.2.2.GA -Dpackaging=jar
mvn install:install-file -Dfile=lib\mor.railway.cmd.adapter.jar -DgroupId=mor.railway.cmd.adapter -DartifactId=mor.railway.cmd.adapter -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib\itcmor.util.jar -DgroupId=itcmor.util -DartifactId=itcmor.util -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib\jdom.jar -DgroupId=itcmor.util -DartifactId=jdom -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib\TableEditor.jar -DgroupId=TableEditor -DartifactId=TableEditor -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib\ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib\dwr.jar -DgroupId=org.directwebremoting -DartifactId=dwr -Dversion=3.0.rc2 -Dpackaging=jar
mvn install:install-file -Dfile=lib\readXls4Skb.jar -DgroupId=readXls4Skb -DartifactId=readXls4Skb -Dversion=1.0 -Dpackaging=jar 