import dev.architectury.plugin.ArchitectPluginExtension
import groovy.lang.Closure
import io.github.pacifistmc.forgix.plugin.ForgixMergeExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    id("io.github.pacifistmc.forgix") version "1.2.6"
    id("architectury-plugin") version "3.4+"
    id("dev.architectury.loom") version "1.1.+" apply false
}

val modName = rootProject.name.lowercase()
val minecraftVersion: String by project
val modVersion: String by project
val mavenGroup: String by rootProject

architectury {
    minecraft = minecraftVersion
}

version = modVersion
group = mavenGroup

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val modLoader = project.name
    val isCommon = modLoader == rootProject.projects.common.name
    val loom: LoomGradleExtensionAPI by project

    version = modVersion
    group = mavenGroup

    base {
        archivesName.set("$modName-$modLoader-$minecraftVersion")
    }

    loom.silentMojangMappingsLicense()

    dependencies {
        "minecraft"("::${minecraftVersion}")
        "mappings"(loom.officialMojangMappings())
    }

    java {
        withSourcesJar()
    }

    tasks.jar {
        archiveClassifier.set("dev")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveClassifier.set(null as String?)
    }

    if (!isCommon) {
        configure<ArchitectPluginExtension> {
            platformSetupLoomIde()
        }

        sourceSets.main {
            val main = this

            rootProject.projects.common.dependencyProject.sourceSets.main {
                main.java.source(java)
                main.resources.source(resources)
            }
        }

        dependencies {
            compileOnly(rootProject.projects.common)
        }
    }
}

@Suppress("UNCHECKED_CAST")
configure<ForgixMergeExtension> {
    group = mavenGroup
    mergedJarName = "$modName-universal-$minecraftVersion-$version.jar"
    outputDir = "build/libs/merged"


    forge(closureOf<ForgixMergeExtension.ForgeContainer> {
        jarLocation = "build/libs/$modName-forge-$minecraftVersion-$version.jar"
    } as Closure<ForgixMergeExtension.ForgeContainer>)

    fabric(closureOf<ForgixMergeExtension.FabricContainer> {
        jarLocation = "build/libs/$modName-fabric-$minecraftVersion-$version.jar"
    } as Closure<ForgixMergeExtension.FabricContainer>)
}
