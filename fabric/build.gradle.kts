architectury {
    fabric()
}

dependencies {
    val fabricVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricVersion")
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}