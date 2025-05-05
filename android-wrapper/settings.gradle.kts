rootProject.name = "FlashcardApp"

include(":app")
include(":ktor-module")

project(":ktor-module").projectDir = File(rootDir, "../") 