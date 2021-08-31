plugins {
    java
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.8")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}
