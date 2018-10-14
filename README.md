# Rust Android Gradle Plugin

Cross compiles rust cargo projects for Android

# Usage

To begin you must first install the rust toolchains for your target platforms.

```
rustup target add armv7-linux-androideabi   # for arm
rustup target add i686-linux-android        # for x86
...
```

Next add the `cargo` configuration to android project. Point to your cargo project using `module` and add targets.
Currently supported targets are `arm`, `arm64` and `x86`, `x86_64`.

```
cargo {
    module = "../rust"
    targets = ["arm", "x86"]
}
```

Run the `cargoBuild` task to cross compile

```
./gradlew cargoBuild
```

## Additional options

The `cargo` Gradle configuration accepts many more options.

### Linking Java code to native libraries

Generated static libraries will be added to your android `jniLibs` source-sets,
when correctly referenced in the `cargo` configuration through the `libname` or `targetIncludes` options.
The latter defaults to `["$libname.so", "$libname.dylib", "$libname.dll"]`,
so the following configuration will include all `libbackend` libraries generated in the Rust project in `../rust`:

```
cargo {
    module = "../rust"
    libname = "libbackend"
}
```

Now, Java code can call into the native code using e.g.

```java
static {
    System.loadLibrary("backend");
}
```

### Native `apiLevel`

The [Android NDK](https://developer.android.com/ndk/guides/stable_apis)
also knows an API level, which can be specified using the `apiLevel` option.
This option defaults to the SDK API level.
As of API level 21, 64 bit builds are possible

### Cargo release profile

The `profile` option selects between the `--debug` and `--release` profiles in `cargo`.
*Defaults to `debug`!*

# Development

At top-level, the `publish` Gradle task updates the Maven repository
under `samples`:

```
$ ./gradlew publish
...
$ ls -al samples/maven-repo/org/mozilla/rust-android-gradle/org.mozilla.rust-android-gradle.gradle.plugin/0.4.0/org.mozilla.rust-android-gradle.gradle.plugin-0.4.0.pom
-rw-r--r--  1 nalexander  staff  670 18 Sep 10:09
samples/maven-repo/org/mozilla/rust-android-gradle/org.mozilla.rust-android-gradle.gradle.plugin/0.4.0/org.mozilla.rust-android-gradle.gradle.plugin-0.4.0.pom
```

## Sample projects

To run the sample projects:

```
$ ./gradlew -p samples/library :assembleDebug
...
$ ls -al samples/library/build//outputs/aar/library-debug.aar
-rw-r--r--  1 nalexander  staff  8926315 18 Sep 10:22 samples/library/build//outputs/aar/library-debug.aar
```

## Real projects

To test in a real project, use the local Maven repository in your `build.gradle`, like:

```
buildscript {
    repositories {
        maven {
            url "file:///Users/nalexander/Mozilla/rust-android-gradle/samples/maven-repo"
        }
    }

    dependencies {
        classpath 'org.mozilla.rust-android-gradle:plugin:0.3.0'
    }
}
```
