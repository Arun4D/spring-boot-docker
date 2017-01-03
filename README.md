# Spring-Boot-Docker

### Environment Setup
Install docker toolbox

[`https://www.docker.com/products/docker-toolbox`](https://www.docker.com/products/docker-toolbox)

### Maven Install

Maven command to build the docker image.

``` 
git clone https://github.com/Arun4D/spring-boot-docker.git
mvn clean package docker:build
docker run --name spring-boot-docker-v1  -e "SPRING_PROFILES_ACTIVE=default" -e "SERVER.PORT=10000" -p 10000:10000 -t spring-boot-docker:v1
```

### H2 database Configuration

* The default h2 console url to run this spring boot application in local environment

    ````
    http://localhost:10000/console/
    ````
    > Note: Port is based on SERVER.PORT configuration. Default spring boot server port 8080


* If this application running in docker. 

    ````
    $ docker-machine  ip default
    192.168.99.100
    
    http://192.168.99.100:10000/console/
    ````
    > Note: Assumed that `default` docker machine  is used. If different docker machine used use `docker-machine  ip <docker-machine name>`

* Use the following details to connect to the database.

    ````
    JDBC URL    : jdbc:h2:mem:PersonDB
    User Name   : sa
    password    :
    ````
### Run Application

Open the browser and hit the following url to invoke the service.
````
http://192.168.99.100:10000/person?firstName=arun&lastName=duraisamy
````

