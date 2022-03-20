import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    id("com.avast.gradle.docker-compose") version "0.14.3"
    id("nebula.integtest") version "9.6.3"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.liquibase.gradle") version "2.1.1"
    id("nebula.release") version "16.0.0"
    idea
}

liquibase {
    activities.register("main") {
        val dbUrl by project.extra.properties
        val dbUser by project.extra.properties
        val dbPassword by project.extra.properties

        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/liquibase/db.changelog-master.xml",
            "url" to dbUrl,
            "username" to dbUser,
            "password" to dbPassword,
            "driver" to "org.postgresql.Driver"
        )
    }
    runList = "main"
}

group = "com.ribbontek"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.1"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot:spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.7")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.7")
    implementation("com.vladmihalcea:hibernate-types-52:2.16.1")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("javax.inject:javax.inject:1")
    implementation("org.aspectj:aspectjweaver:1.9.9.1")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.reflections:reflections:0.10.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("com.github.javafaker:javafaker:1.0.2")
    testImplementation("org.awaitility:awaitility:4.2.0")
    integTestImplementation("org.springframework.boot:spring-boot-starter-test")

    liquibaseRuntime("org.postgresql:postgresql")
    liquibaseRuntime("org.liquibase:liquibase-core:4.10.0")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
    liquibaseRuntime("javax.xml.bind:jaxb-api:2.3.1")
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

idea {
    module {
        testSourceDirs.plusAssign(project.sourceSets["integTest"].java.srcDirs)
        testResourceDirs.plusAssign(project.sourceSets["integTest"].resources.srcDirs)
    }
}

kotlin {
    allOpen {
        annotations(
            "javax.persistence.Entity",
            "javax.persistence.MappedSuperclass",
            "javax.persistence.Embedabble"
        )
    }
}

dockerCompose.isRequiredBy(tasks.getByName("integrationTest"))

tasks.withType<BootJar> {
    this.archiveFileName.set("${archiveBaseName.get()}-final.${archiveExtension.get()}")
}

val cacheNebulaVersion by tasks.registering {
    val sourceSets = project.extensions.getByName("sourceSets") as SourceSetContainer
    sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).output.resourcesDir?.let {
        if (!it.exists()) {
            it.mkdirs()
        }
        File(it, "version.txt").writeBytes(project.version.toString().toByteArray())
    }
}

tasks {
    register<Copy>("copyGitHooks") {
        description = "Copies the git hooks from git-hooks to the .git folder."
        group = "GIT_HOOKS"
        from("$rootDir/git-hooks/") {
            include("**/*.sh")
            rename("(.*).sh", "$1")
        }
        into("$rootDir/.git/hooks")
    }

    register<Exec>("installGitHooks") {
        description = "Installs the git hooks from the git-hooks folder"
        group = "GIT_HOOKS"
        workingDir(rootDir)
        commandLine("chmod")
        args("-R", "+x", ".git/hooks/")
        dependsOn(named("copyGitHooks"))
        onlyIf {
            !Os.isFamily(Os.FAMILY_WINDOWS)
        }
        doLast {
            logger.info("Git hooks installed successfully.")
        }
    }

    register<Delete>("deleteGitHooks") {
        description = "Delete the git hooks."
        group = "GIT_HOOKS"
        delete(fileTree(".git/hooks/"))
    }

    afterEvaluate {
        tasks["clean"].dependsOn(tasks.named("installGitHooks"))
    }
}
