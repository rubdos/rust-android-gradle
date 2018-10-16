package com.nishtahir

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import java.io.File
import kotlin.reflect.KClass

operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T = getByType(type.java)

fun Project.getToolchainDirectory(): File {
    // Share a single toolchain directory, if one is configured, but fall back to per-project
    // toolchains.
    val globalDir: String? = System.getenv("ANDROID_NDK_TOOLCHAIN_DIR")
    if (globalDir == null) {
        val oldDefault = File(projectDir, ".cargo/toolchain")
        var defaultDir = File(System.getProperty("java.io.tmpdir"), "rust-android-ndk-toolchains")
        println("Note: previosuly, toolchains defaulted to ${oldDefault.absolutePath}.")
        println("Moved to ${defaultDir.absolutePath}, or override via the ANDROID_NDK_TOOLCHAIN_DIR environment variable.")
        return defaultDir.absoluteFile
    }
    return File(globalDir).absoluteFile
}
