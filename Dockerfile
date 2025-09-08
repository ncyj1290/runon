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

# Create non-root user
RUN addgroup --system tomcat && adduser --system tomcat --ingroup tomcat

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy built WAR file from build stage
COPY --from=build /app/target/runon-1.0.0-BUILD-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Change ownership
RUN chown -R tomcat:tomcat /usr/local/tomcat

# Switch to non-root user
USER tomcat:tomcat

# Expose port 8080
EXPOSE 8080

# Add health check (removed curl dependency)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
#     CMD curl -f http://localhost:8080/ || exit 1

# Run Tomcat
CMD ["catalina.sh", "run"]