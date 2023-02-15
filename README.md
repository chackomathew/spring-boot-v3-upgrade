# Spring Boot V3 Upgrade Issues

The project is intended to be a reproducer for issues during upgrade of Spring Boot v3, Spring Data JPA, HIbernate v6.X, etc.

## Requirements:
- Java JDK 17
- Docker
- Spring Boot 3.0.2
- Hibernate 6.X
- Testcontainers + Junit + Postgres container

## Issue I: JPA derived query methods failing on boolean mapping using AttributeConverter https://github.com/spring-projects/spring-data-jpa/issues/2800

## Issue Description:
The issue occurs when Spring creates a query for a repository method with boolean parameter in the name which is mapped using an Attribute Converter like YesNoConverter.

## Error Message:
org.springframework.dao.InvalidDataAccessResourceUsageException: JDBC exception executing SQL [select u1_0.id,u1_0.deleted,u1_0.first_name,u1_0.last_name from users u1_0 where not(u1_0.deleted)
Caused by: org.postgresql.util.PSQLException: ERROR: argument of NOT must be type boolean, not type character

