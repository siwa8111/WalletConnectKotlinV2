plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.squareup.sqldelight")
    `maven-publish`
}

tasks.withType<Test> {
    useJUnitPlatform()
}

android {
    namespace = "com.walletconnect.sign"
    compileSdk = 32

    defaultConfig {
        minSdk = MIN_SDK
        targetSdk = 32

        aarMetadata {
            minCompileSdk = MIN_SDK
            targetSdk = 32
        }

        testInstrumentationRunner = "com.walletconnect.sign.test.utils.WCTestRunner"
        testInstrumentationRunnerArguments += mutableMapOf("runnerBuilder" to "de.mannodermaus.junit5.AndroidJUnit5Builder")
        testInstrumentationRunnerArguments += mutableMapOf("clearPackageData" to "true")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("qa"){
            initWith(getByName("debug"))
            signingConfig = signingConfigs.getByName("debug")

            buildConfigField("String", "PROJECT_ID", "\"${System.getenv("TEST_PROJECT_ID") ?: ""}\"")
            buildConfigField("String", "WC_RELAY_URL", "\"${System.getenv("TEST_RELAY_URL") ?: ""}\"")
        }
    }

    testBuildType = "qa"

    compileOptions {
        sourceCompatibility = jvmVersion
        targetCompatibility = jvmVersion
    }

    kotlinOptions {
        jvmTarget = jvmVersion.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.time.ExperimentalTime"
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    packagingOptions {
        resources.excludes += setOf(
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md",
            "META-INF/AL2.0",
            "META-INF/LGPL2.1"
        )
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}

dependencies {
    okhttp()
    bouncyCastle()
    coroutines()
    moshi()
    scarlet()
    sqlDelight()
    security()
    koin()
    multibaseJava()

    androidXTest()
    jUnit5()
    robolectric()
    mockk()
    timber()
    testJson()
}