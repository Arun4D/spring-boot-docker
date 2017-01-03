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

###H2 database url
http://localhost:8080/console/
