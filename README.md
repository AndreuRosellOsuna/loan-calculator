# Loan calculator
Recipes for Java 8 and Spring Boot

#### Build application
Just launch `mvn package`

#### Launch application in windows
To start the application in windows, first build the application and extract the generated assembly zip (for example loan-calculator.application-1.0-SNAPSHOT-assembly.zip) in any folder of your windows system. Then you can execute the file `launcher.bat` with the file containing the market and the desired loan amount as the arguments. For example:
```
C:\MyFolder> launcher.bat market.csv 1000
```

#### Launch application in UNIX systems
To start the application in unix systems, first build the application and extract the generated assembly zip (for example loan-calculator.application-1.0-SNAPSHOT-assembly.zip) in any folder of your windows system. Then you can execute the file `launcher.sh` with the file containing the market and the desired loan amount as the arguments. For example:
```
/home/andreu/myFolder$ ./launcher.sh market.csv 1000
```
