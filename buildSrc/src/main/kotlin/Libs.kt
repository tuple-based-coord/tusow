import kotlin.String

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Update this file with
 *   `$ ./gradlew buildSrcVersions`
 */
object Libs {
    /**
     * https://github.com/FasterXML/jackson-dataformat-xml
     */
    const val jackson_dataformat_xml: String =
            "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:" +
            Versions.com_fasterxml_jackson_dataformat

    /**
     * https://github.com/FasterXML/jackson-dataformats-text
     */
    const val jackson_dataformat_yaml: String =
            "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:" +
            Versions.com_fasterxml_jackson_dataformat

    /**
     * http://tuprolog.unibo.it
     */
    const val `2p_core`: String = "it.unibo.alice.tuprolog:2p-core:" +
            Versions.it_unibo_alice_tuprolog

    /**
     * http://tuprolog.unibo.it
     */
    const val `2p_presentation`: String = "it.unibo.alice.tuprolog:2p-presentation:" +
            Versions.it_unibo_alice_tuprolog

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_scripting_compiler_embeddable: String =
            "org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:" +
            Versions.org_jetbrains_kotlin

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_stdlib_jdk8: String = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:" +
            Versions.org_jetbrains_kotlin

    const val vertx_core: String = "io.vertx:vertx-core:" + Versions.io_vertx

    const val vertx_unit: String = "io.vertx:vertx-unit:" + Versions.io_vertx

    const val vertx_web: String = "io.vertx:vertx-web:" + Versions.io_vertx

    const val vertx_web_client: String = "io.vertx:vertx-web-client:" + Versions.io_vertx

    const val org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin: String =
            "org.danilopianini.git-sensitive-semantic-versioning:org.danilopianini.git-sensitive-semantic-versioning.gradle.plugin:" +
            Versions.org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin

    const val com_github_breadmoirai_github_release_gradle_plugin: String =
            "com.github.breadmoirai.github-release:com.github.breadmoirai.github-release.gradle.plugin:" +
            Versions.com_github_breadmoirai_github_release_gradle_plugin

    const val com_github_johnrengelman_shadow_gradle_plugin: String =
            "com.github.johnrengelman.shadow:com.github.johnrengelman.shadow.gradle.plugin:" +
            Versions.com_github_johnrengelman_shadow_gradle_plugin

    const val de_fayard_buildsrcversions_gradle_plugin: String =
            "de.fayard.buildSrcVersions:de.fayard.buildSrcVersions.gradle.plugin:" +
            Versions.de_fayard_buildsrcversions_gradle_plugin

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String =
            "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:" +
            Versions.org_jetbrains_kotlin_jvm_gradle_plugin

    const val com_jfrog_bintray_gradle_plugin: String =
            "com.jfrog.bintray:com.jfrog.bintray.gradle.plugin:" +
            Versions.com_jfrog_bintray_gradle_plugin

    /**
     * https://github.com/FasterXML/jackson-modules-java8
     */
    const val jackson_datatype_jsr310: String =
            "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:" +
            Versions.jackson_datatype_jsr310

    /**
     * https://commons.apache.org/proper/commons-collections/
     */
    const val commons_collections4: String = "org.apache.commons:commons-collections4:" +
            Versions.commons_collections4

    /**
     * http://logback.qos.ch
     */
    const val logback_classic: String = "ch.qos.logback:logback-classic:" + Versions.logback_classic

    /**
     * https://github.com/FasterXML/jackson-core
     */
    const val jackson_core: String = "com.fasterxml.jackson.core:jackson-core:" +
            Versions.jackson_core

    const val named_regexp: String = "com.github.tony19:named-regexp:" + Versions.named_regexp

    /**
     * http://commons.apache.org/proper/commons-cli/
     */
    const val commons_cli: String = "commons-cli:commons-cli:" + Versions.commons_cli

    /**
     * http://www.javatuples.org
     */
    const val javatuples: String = "org.javatuples:javatuples:" + Versions.javatuples

    /**
     * http://www.slf4j.org
     */
    const val slf4j_api: String = "org.slf4j:slf4j-api:" + Versions.slf4j_api

    /**
     * 2.8.0
     */
    const val clikt: String = "com.github.ajalt:clikt:" + Versions.clikt

    /**
     * http://junit.org
     */
    const val junit: String = "junit:junit:" + Versions.junit

    const val jool: String = "org.jooq:jool:" + Versions.jool
}
