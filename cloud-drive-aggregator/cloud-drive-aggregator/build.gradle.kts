plugins {
    java
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.liquibase.gradle") version "2.0.4"
}

group ="ru.shakurov.diploma"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // database
    implementation("org.liquibase:liquibase-core:4.11.0")
    implementation("org.postgresql:postgresql:42.3.6")

    // utils
    implementation("org.apache.commons:commons-lang3:3.12.0")

    //mapstruct
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.4.2.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // lombok
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    // api (external)
    implementation("com.yandex.android:disk-restapi-sdk:1.03")
    implementation("com.dropbox.core:dropbox-core-sdk:5.2.0")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.0.1")

    // algo
    implementation("com.google.ortools:ortools-java:9.3.10497")
    implementation("com.google.ortools:ortools-darwin:8.2.9004")


    // test
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
