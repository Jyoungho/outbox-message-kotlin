plugins {
	java
	`maven-publish`

	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	val kotlinVersion = "2.1.0"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}


group = "com.albert"
version = "0.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.springframework.boot:spring-boot-starter")
	compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
	compileOnly("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	compileOnly("org.jetbrains.kotlin:kotlin-reflect")
	compileOnly("org.springframework.kafka:spring-kafka")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}