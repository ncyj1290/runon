# Build stage
FROM openjdk:11-jdk-slim as build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Runtime stage
FROM tomcat:9.0-jre11-slim

# Remove default webapps and create ROOT directory
RUN rm -rf /usr/local/tomcat/webapps/*
RUN mkdir -p /usr/local/tomcat/webapps/ROOT

# Copy and extract WAR file to ROOT directory
COPY --from=build /app/target/runon-1.0.0-BUILD-SNAPSHOT.war /tmp/app.war
RUN cd /usr/local/tomcat/webapps/ROOT && \
    jar -xf /tmp/app.war && \
    rm /tmp/app.war

# Add simple index page for testing
RUN echo '<html><body><h1>Tomcat is working!</h1><p>Spring MVC App should be available soon...</p><a href="/health">Health Check</a></body></html>' > /usr/local/tomcat/webapps/ROOT/index.html

# Set proper permissions
RUN chmod -R 755 /usr/local/tomcat/webapps

# Expose port 8080
EXPOSE 8080

# Add health check (removed curl dependency)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
#     CMD curl -f http://localhost:8080/ || exit 1

# Run Tomcat
CMD ["catalina.sh", "run"]