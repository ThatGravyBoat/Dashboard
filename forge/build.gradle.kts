architectury {
    forge()
}

loom {
    forge {
        mixinConfig("dashboard.mixins.json")
    }
}

dependencies {
    val minecraftVersion: String by project
    val forgeVersion: String by project

    forge(group = "net.minecraftforge", name = "forge", version = "$minecraftVersion-$forgeVersion")
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to version)
    }
}