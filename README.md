# conference-booking-service
Its provides use cases to book conference rooms

Softwares
---------
Java 11,
Spring boot 2,
Sping 5,
Hibernate 5,
tomcat-embed 9,
Swager,
Logback,
MockTesting,
H2DB 1.4,
DevTools,
Actuator,
Swagger,
Logging frame work,
Junit 5


Build Process:
---------------
STEP-1 >> build      :C:\\downloads\conference-booking-service> mvn clean install <br>
STEP-2 >> run server :C:\\downloads\conference-booking-service> mvn spring-boot:run  <br>

Use swagger URL to run hit APIs and verify the data in H2DB by login (provided details below) <br>

  API-1) Get available conference rooms by requested time slots <br>
  API-2) Book conference room <br>
  
Swagger URL:
--------------
http://localhost:8080/swagger-ui/index.html

Docker Image
------------
docker build -t conference-booking-service .


Actuator
-------

http://localhost:8080/api/actuator/health

H2- DATABASE
-------------
http://localhost:8080/h2-console <br>
 	username: sa <br>
    password: password<br>
