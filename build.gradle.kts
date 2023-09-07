import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.7.20"
	kotlin("plugin.spring") version "1.7.20"
	kotlin("plugin.jpa") version "1.7.20"
	kotlin("kapt") version "1.7.20"
//	id("nu.studer.jooq") version "7.1.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}
extra["xc_version"] = "$version"
extra["springCloudVersion"] = "2021.0.4"

tasks.jar {
	enabled = false
}

dependencies {
	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.13")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.13")
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.85.Final:osx-aarch_64")

//	implementation("org.jooq:jooq:3.16.7")
//	implementation("org.jooq:jooq-codegen:3.16.7")
//	implementation("org.jooq:jooq-meta:3.16.7")
//	implementation("org.jooq:jooq-kotlin:3.16.7")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-jooq") {
//		exclude(group = "org.jooq")
//	}

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("com.playtika.reactivefeign:feign-reactor-spring-cloud-starter:3.2.6")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.liquibase:liquibase-core")

	implementation("com.ninja-squad:springmockk:3.1.2")
	implementation("org.springframework.boot:spring-boot-starter-test"){
		exclude(module = "mokito-core")
	}

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
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
