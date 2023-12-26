import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	war
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "com.whalee"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.telegram:telegrambots:5.3.0")
	implementation("org.springframework.boot:spring-boot-starter-batch:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.0")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
	// ?
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
	// ?
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.8.21")
	runtimeOnly("org.postgresql:postgresql:42.6.0")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat:3.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
