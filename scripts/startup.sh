
#Add dependencies to classpath from target/lib
for file in $VYUUDHA_HOME/target/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

#Add Vyuudha jar files to classpath from target/
for file in $VYUUDHA_HOME/target/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

java -Dlog4j.configuration=src/main/java/log4j.properties -cp $CLASSPATH com.dds.init.InitDDS