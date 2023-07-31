architectury {
    val enabledPlatforms: String by rootProject
    common(enabledPlatforms.split(","))
}

dependencies {
    val fabricVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricVersion")
}