@ECHO OFF

SET java_path=C:\path\to\java.exe

SET classpaths_paths=C:\Users\you\tools\ltl-plain-java-cli\target\classes;C:\Users\you\.m2\repository\org\jsoup\jsoup\1.23.1\jsoup-1.23.1.jar;C:\Users\you\.m2\repository\com\mysql\mysql-connector-j\26.7.0\mysql-connector-j-26.7.0.jar;C:\Users\you\.m2\repository\com\google\protobuf\protobuf-java\4.31.1\protobuf-java-4.31.1.jar

SET main_path=src\main\java\org\example\Main.java

REM The below line runs the ltl program.
%java_path% -classpath %classpaths_paths% %main_path%