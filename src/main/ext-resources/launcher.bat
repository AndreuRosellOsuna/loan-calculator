@echo off

chcp 65001 > NUL
java -jar -Dfile.encoding=UTF-8 loan-calculator.application-1.0-SNAPSHOT.jar %1 %2
