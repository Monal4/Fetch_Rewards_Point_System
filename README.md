# Fetch_Rewards_Point_System

Tools Used to Build this Service:

Language: Java 8
Framework: SpringBoot version 2+, Spring Data JPA
   Dependencies: Spring Data JPA, h2, jackson library
   
database: h2 in-memory
Testing: JUnit 4

-----------------------------------------------------------------------------------------

Minimum Requirements to run this service: JVM(or JDK latest version if possible)

-----------------------------------------------------------------------------------------

Running this service:

1) Go to cmd/terminal -> root directory(has pom.xml file) -> run mvn clean package -> go to target -> run java -jar (application).jar 
2) using IDE(Eclipse, Intellij) -> run as spring boot application(requires spring plugin if using eclipse )

once running:

go to postman(chrome plugin or desktop application) send http request to http://localhost:9004/{endpoints}

-----------------------------------------------------------------------------------------

endpoints:
-> "/balance" endpoint
    returns most recent balance as per vendors in form of total of each vendors points
    http://localhost:9004/balance
    
    throws exception of no balance if executed before adding any points initially or at any point when points spent
    more than the owned points. 

-> "/points" endpoint
    adds custom end point or uses default methods of the back end to add 5 sample end points
    http://localhost:9004/points //adds 5 vendors with points and dates specified as per doc.
    http://localhost:9004/point?Payer=Test&Points=3000&Date=1/12+2PM //http://localhost:9004/point?Payer={vendor name}&Points={amount of points}&Date={date seperated by +           instead of spaces} 
    
-> "deduct/{amount}" endpoint
    http://localhost:9004/deduct/5000
    returns the list of vendors from which the points are spent and throws balance of insufficient funds if amount exceeds owned points.
    
   Screenshots are added in root directory that tests all the endpoints.......................................

-----------------------------------------------------------------------------------------

viewing in-memory database:

run the application then navigate to http://localhost:9004/h2-console.
add driverclass: org.h2.Driver
add jdbc url: jdbc:h2:mem:transactionsdb
add username: sa
add password: password

as hibernate will map entity class to database table.
