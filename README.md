# Jaya Tech Test Application

Interview test proposed by Jaya Tech

## Requirements
* Java Development Kit 17 or higher.
* Maven
* Mongo Database running locally with a Database called jaya and username=username and password=password configured.
* Docker and Docker Compose for integrated tests.

## Running

`mvn spring-boot:run`

Access swagger UI at: http://localhost:8080/swagger-ui/index.html

## Testing

`mvn test`

For tests the application will run a mongo container.

