plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.3.7"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("io.freefair.lombok") version "6.1.0"
}

group = "me.lofro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenCentral()
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://libraries.minecraft.net/")
    maven("https://plugins.gradle.org/m2")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    test {
        useJUnitPlatform()
    }

    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.compilerArgs.add("-parameters")
        options.isFork=true
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)

    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    shadowJar {
        relocate("co.aikar.commands", "shadded.acf")
        relocate("co.aikar.locales", "shadded.acf.locales")
    }
}

bukkit {
    name = "Cerdomania"
    version = "1.0"
    apiVersion = "1.18"
    main = "me.lofro.cerdomania.Cerdomania"
    author = "Lofro"
    website = "https://github.com/zLofro"
}