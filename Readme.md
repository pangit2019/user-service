## Problem statement
Create a simple command line JAVA application with database access.
Based on an input from the command line provide the following functionality:
 
1. Provide help 
2. Add User (id, firstName, surname) 
3. Edit User (firstName, surname) 
4. Delete User (id) 
5. Count Number of Users 
6. List Users Additional Requirements 
7. Ability to Add User from XML

## Solution

1) Read user input from console using system reader.
2) Parse user input using apache command line library.
3) Use HSQL in memory database to store and process data.
4) Use of JAXB for unmarshalling xml string to java object.
5) Spring logs are diverted to log file to keep console clean.


## Technologies used

- Java 8
- Maven
- Spring boot
- Spring JPA
- Junit
- HSQL DB

## How to run

- Compile and build project using command `mvn clean install`
- Run following command to start application:

```
java -jar target\user-service-0.0.1-SNAPSHOT.jar start-cli
```

Executing above command will open cli for application. Type `user -h` to find out all commands.

## Sample commands

1. Help -> `user -h`
2. Add User (id, firstName, surname) -> `user -a 1:johnny:depp`
3. Edit User (firstName, surname) -> `user -e 1:James:depp`
4. Delete User (id) -> `user -d 1`
5. Count Number of Users -> `user -c`
6. List Users Additional Requirements -> `user -l`
7. Ability to Add User from XML -> `user -xa <user><id>2</id><firstName>James</firstName><surName>Lawlor</surName></user>`
8. Exit from application -> `exit`