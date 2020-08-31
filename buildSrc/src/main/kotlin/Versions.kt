import kotlin.String
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {
    const val com_fasterxml_jackson_dataformat: String = "2.11.2"

    const val it_unibo_alice_tuprolog: String = "4.1.1"

    const val org_jetbrains_kotlin: String = "1.4.0"

    const val io_vertx: String = "3.9.2"

    const val org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin: String = "0.2.2"

    const val com_github_breadmoirai_github_release_gradle_plugin: String = "2.2.12"

    const val com_github_johnrengelman_shadow_gradle_plugin: String = "6.0.0"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.7.0"

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.4.0"

    const val com_jfrog_bintray_gradle_plugin: String = "1.8.5"

    const val jackson_datatype_jsr310: String = "2.11.2"

    const val commons_collections4: String = "4.4"

    const val logback_classic: String = "1.2.3"

    const val jackson_core: String = "2.11.2"

    const val named_regexp: String = "0.2.6"

    const val commons_cli: String = "1.4"

    const val javatuples: String = "1.2"

    const val slf4j_api: String = "1.7.30"

    const val clikt: String = "2.2.0"

    const val junit: String = "4.13"

    const val jool: String = "0.9.14"

    /**
     * Current version: "6.6.1"
     * See issue 19: How to update Gradle itself?
     * https://github.com/jmfayard/buildSrcVersions/issues/19
     */
    const val gradleLatestVersion: String = "6.6.1"
}

/**
 * See issue #47: how to update buildSrcVersions itself
 * https://github.com/jmfayard/buildSrcVersions/issues/47
 */
val PluginDependenciesSpec.buildSrcVersions: PluginDependencySpec
    inline get() =
            id("de.fayard.buildSrcVersions").version(Versions.de_fayard_buildsrcversions_gradle_plugin)
