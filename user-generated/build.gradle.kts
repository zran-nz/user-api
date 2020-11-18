import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val kotlinVersion = "1.4.10"
    jacoco
    id("org.springframework.boot") version "2.3.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    `maven-publish`
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "com.github.b1412"
version = "0.1.9"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven(url = "https://jitpack.io")
}

dependencies {
    api(project(":user-base"))
    val arrowVersion = "0.10.3"
    implementation("com.github.b1412:api-common:a95b7b6c49")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    springboot()
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("mysql:mysql-connector-java:6.0.5")
    arrow(arrowVersion)
    graphql()
    implementation("org.jooq:joor-java-8:0.9.12")
    implementation("io.github.microutils:kotlin-logging:1.7.6")
    implementation("io.jsonwebtoken:jjwt:0.7.0")
    implementation("commons-beanutils:commons-beanutils:1.9.4")

}


fun DependencyHandlerScope.springboot() {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-autoconfigure-processor")

}

fun DependencyHandlerScope.arrow(arrowVersion: String) {
    implementation("io.arrow-kt:arrow-fx:$arrowVersion")
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
}

fun DependencyHandlerScope.graphql() {
    implementation("com.graphql-java-kickstart:playground-spring-boot-starter:5.10.0")
    implementation("com.graphql-java:graphql-spring-boot-starter:5.0.2")
    implementation("com.graphql-java:graphiql-spring-boot-starter:5.0.2")
    implementation("com.graphql-java:graphql-java-tools:5.2.4")
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/b1412/user-api")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                artifactId = tasks.jar.get().archiveBaseName.get()
            }
        }
    }
}
