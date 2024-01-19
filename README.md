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

Use Below APIs to run and verify the data in H2DB by login (provided details below) <br>

  API-1) Get available conference rooms by requested time slots <br>
  API-2) Book conference room <br>
  
  
1) Get Available conference rooms API:<br>
URL :  GET : http://localhost:8080/api/v1/conference-rooms?startTime=10:00&endTime=10:30 <br>
Header : Content-Type : application/json <br>

2) Book conference room API : <br>

 URL > POST : http://localhost:8080/api/v1/bookings/bookConferenceRoom <br>
     Header : loggedInUser : Nagendra <br>
     		  Content-Type : application/json	<br>
     Body : { <br>
   			"startTime": "10:00:00", <br>
		   "endTime": "10:30:00", <br>
		    "participants": 2 <br>
			} <br>
 <br>

  
  
Swagger URL:
--------------
http://localhost:8080/swagger-ui/index.html <br>


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
