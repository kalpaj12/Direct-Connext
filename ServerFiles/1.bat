del *.class
javac -cp .;"mysql-connector-java-8.0.13.jar" *.java -Xlint
cls
java -cp .;"mysql-connector-java-8.0.13.jar" ServerGUI

