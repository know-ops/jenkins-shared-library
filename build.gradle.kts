plugins {
    id("groovy")
    kotlin("jvm") version "1.3.70"
    jacoco
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://repo.jenkins-ci.org/releases/") }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    compileOnly("org.codehaus.groovy:groovy-all:3.0.2")
    testImplementation("junit:junit:4.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("com.lesfurets:jenkins-pipeline-unit:1.3")
    testImplementation("org.mockito:mockito-core:3.3.3")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testRuntime("org.junit.vintage:junit-vintage-engine:5.6.0")
}

sourceSets["test"].withConvention(GroovySourceSet::class)  {
    groovy.srcDir("src/test/")
}

sourceSets["main"].withConvention(GroovySourceSet::class)  {
    groovy.srcDir("src/main/")
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.destination = file("${buildDir}/jacocoHtml")
    }
//    classDirectories.setFrom(
//            sourceSets.main.get().output.asFileTree.matching {
//                exclude("xxx/**/*.class")
//            }
//    )
}
