# Spring-Boot-Docker

This Spring Boot 3.0 application demonstrates:
1. H2 database integration
2. Flyway data migration
3. Docker containerization with multi-stage builds
4. Spring Boot Actuator for health monitoring
5. Security best practices for containerization

## Prerequisites

- Java 17 or later
- Docker Desktop (latest version)
- Maven 3.6+

## Quick Start

```bash
# Clone the repository
git clone https://github.com/Arun4D/spring-boot-docker.git
cd spring-boot-docker

# Build and run locally
mvn clean install
```

## Docker Build and Run

### Enable Docker BuildKit (for optimized builds)

PowerShell:
```powershell
$env:DOCKER_BUILDKIT=1
```

Bash:
```bash
export DOCKER_BUILDKIT=1
```

### Build and Run Container

```bash
# Build the Docker image
docker build -t spring-boot-docker:latest .

# Run the container
docker run -d \
  --name spring-boot-app \
  -p 8080:8080 \
  -e "SPRING_PROFILES_ACTIVE=default" \
  spring-boot-docker:latest

# Check container health
docker ps
docker logs spring-boot-app
```

### Docker Build Features

- Multi-stage build for smaller final image
- Maven cache optimization
- Eclipse Temurin JDK 17 (recommended for Spring Boot 3.0)
- Security hardening with non-root user
- Built-in health checks
- Container-optimized JVM settings
 
## Application Access

### REST API Endpoints

Access the persons API:
```
http://localhost:8080/api/persons
```

### Health Check
```
http://localhost:8080/actuator/health
```

### H2 Database Console

Access the H2 database console when running:

1. **Locally**:
```
http://localhost:8080/h2-console/
```

2. **Docker**:
```
http://localhost:8080/h2-console/
```

Database Connection Details:
```
JDBC URL    : jdbc:h2:mem:PersonDB
User Name   : sa
Password    : [leave empty]
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| SPRING_PROFILES_ACTIVE | Active Spring profile | default |
| JAVA_OPTS | JVM options | -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 |

## Security Notes

- Application runs as non-root user (UID 1001)
- Container includes security hardening
- Uses OpenJDK (Eclipse Temurin) for better security updates
- Health checks enabled by default

## Troubleshooting

1. **View container logs**:
```bash
docker logs spring-boot-app
```

2. **Check container health**:
```bash
docker inspect spring-boot-app | jq '.[0].State.Health'
```

3. **Access container shell**:
```bash
docker exec -it spring-boot-app sh
```
### Run Application

Open the browser and hit the following url to invoke the service.

