# Spring-Boot-Docker

### Environment Setup
Install docker toolbox

[`https://www.docker.com/products/docker-toolbox`](https://www.docker.com/products/docker-toolbox)

### Maven Install

Maven command to build the docker image.

``` 
git clone https://github.com/Arun4D/spring-boot-study.git
mvn clean package docker:build
docker run --name spring-boot-hello-world-v1  -e "SPRING_PROFILES_ACTIVE=default" -e "SERVER.PORT=8080" -p 8080:8080 -t spring-boot-hello-world:v1
```

### H2 database Configuration

* The default h2 console url to run this spring boot application in local environment

    ````
    http://localhost:8080/console/
    ````
    > Note: Port is based on SERVER.PORT configuration. Default spring boot server port 8080


* If this application running in docker. 

    ````
    $ docker-machine  ip default
    192.168.99.100
    
    http://192.168.99.100:8080/console/
    ````
    > Note: Assumed that `default` docker machine  is used. If different docker machine used use `docker-machine  ip <docker-machine name>`

* Use the following details to connect to the database.

    ````
    url=jdbc:h2:mem:PersonDB
    username=sa
    password=
    ````