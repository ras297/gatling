plugins {
    scala
    id("io.gatling.gradle") version "3.14.9.8"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.gatling:gatling-core:3.14.9.8")
    implementation("io.gatling:gatling-http:3.14.9.8")
    implementation("io.gatling:gatling-charts-highcharts:3.14.9.8")
}