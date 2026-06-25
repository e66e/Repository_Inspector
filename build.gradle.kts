plugins {
	java
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.e66e"
version = "0.0.1-SNAPSHOT"
val enable_preview = "--enable-preview"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-restclient")
	implementation("org.wiremock.integrations:wiremock-spring-boot:4.0.9")
	implementation("org.jspecify:jspecify:1.0.0")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}
tasks.withType<JavaCompile>{
	options.compilerArgs.add(enable_preview)
}

tasks.withType<Test> {
	environment("spring.profiles.active", "test")
	useJUnitPlatform()
	jvmArgs(enable_preview)
}
