dependencies {
    api(Libs.slf4j_api)
    api(project(":utils"))
    api(Libs.kotlin_stdlib_jdk8)

    testImplementation(Libs.junit)
    testImplementation(project(":test-utils"))
}
