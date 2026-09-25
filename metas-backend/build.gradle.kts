// build.gradle.kts
//
// Stack:
//   Java 24, Spring Boot 3.5.x, PostgreSQL, Flyway, Redis (cache),
//   Spring Security (OAuth2 Resource Server / JWT), springdoc-openapi.
//
// NOTA: o padrão de engenharia original pedia Java 25 / Spring Boot 4,
// mas essas versões foram ajustadas para o ambiente local (JDK 24 +
// último release estável do Spring Boot 3.x). Reavaliar quando o
// ambiente/repositório interno oferecer Java 25 e Spring Boot 4 estáveis.

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.empresa"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // --- Web / validação ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // --- Persistência ---
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    // --- Cache (Redis) ---
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // --- Segurança: login local emitindo JWT, validado como OAuth2
    // Resource Server (o próprio backend atua como emissor/validador
    // enquanto não existe um IdP externo — ver README) ---
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    // --- Documentação (OpenAPI/Swagger) ---
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // --- Leitura/escrita de planilhas Excel (upload da planilha de
    // metas, import/export de permissões) ---
    implementation("org.apache.poi:poi-ooxml:5.3.0")

    // --- Utilitário só de infraestrutura (NUNCA usado no core/domínio) ---
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // --- Testes ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
