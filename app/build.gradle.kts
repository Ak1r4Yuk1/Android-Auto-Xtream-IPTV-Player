import java.util.Properties
import java.io.File
import com.android.build.api.artifact.SingleArtifact
import org.gradle.configurationcache.extensions.capitalized

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = 36
    namespace = "com.akira.aaplayer"

    defaultConfig {
        applicationId = "com.akira.aaplayer"
        minSdk = 29
        targetSdk = 36
        versionCode = 2
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePropsFile = rootProject.file("app/keystore.properties")
            val keystoreProps = Properties()
            if (keystorePropsFile.exists()) {
                keystoreProps.load(keystorePropsFile.inputStream())
            }

            storeFile = file("aaplayer.jks")
            storePassword = keystoreProps.getProperty("storePassword")
            keyAlias = keystoreProps.getProperty("keyAlias")
            keyPassword = keystoreProps.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }

    
    abstract class RenameApkTask : DefaultTask() {
        @get:InputDirectory
        @get:PathSensitive(PathSensitivity.RELATIVE)
        abstract val inputDir: DirectoryProperty

        @get:OutputDirectory
        abstract val outputDir: DirectoryProperty

        @get:Input
        abstract val appName: Property<String>

        @get:Input
        abstract val versionNameProp: Property<String>

        @get:Input
        abstract val debugSuffixProp: Property<String>

        @TaskAction
        fun run() {
            val inDir = inputDir.get().asFile
            val outDir = outputDir.get().asFile
            outDir.deleteRecursively()
            outDir.mkdirs()

            val app = appName.get()
            val vName = versionNameProp.get()
            val debugSuffix = debugSuffixProp.get()

            inDir.listFiles()?.filter { it.extension == "apk" }?.forEach { f ->
                val newName = "${app}-${vName}${debugSuffix}.apk"
                val dest = File(outDir, newName)
                f.copyTo(dest, overwrite = true)
                println("APK Renamed and Copied to: ${dest.absolutePath}")
            }
        }
    }

    androidComponents {
        onVariants { variant ->
            val vNameStr = android.defaultConfig.versionName ?: "unknown"
            val appNameStr = "AAPlayer"
            val isDebug = variant.buildType == "debug"
            val debugSuffixStr = if (isDebug) "_debug" else ""

            val renameTaskProvider = tasks.register<RenameApkTask>("${variant.name}RenameApk") {
                inputDir.set(variant.artifacts.get(SingleArtifact.APK))
                
                outputDir.set(layout.buildDirectory.dir("renamedApks/${variant.name}"))
                
                appName.set(appNameStr)
                versionNameProp.set(vNameStr)
                debugSuffixProp.set(debugSuffixStr)
            }
            
             afterEvaluate {
                 val assembleTaskName = "assemble${variant.name.replaceFirstChar { it.uppercase() }}"
                 if (tasks.findByName(assembleTaskName) != null) {
                     tasks.named(assembleTaskName).configure {
                         finalizedBy(renameTaskProvider)
                     }
                 }
             }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.car.app)

    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}