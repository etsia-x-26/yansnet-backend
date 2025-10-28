rootProject.name = "etsia-backend"

include(
    "application",
    "common",
    "interaction",
    "message",
    "notification",
    "post",
    "user",
    "auth",
    "channel"
)

project(":auth").projectDir = file("auth")
project(":common").projectDir = file("common")
project(":interaction").projectDir = file("interaction")
project(":message").projectDir = file("message")
project(":notification").projectDir = file("notification")
project(":post").projectDir = file("post")
project(":user").projectDir = file("user")
project(":channel").projectDir = file("channel")


//pluginManagement {
//    repositories {
//        maven("https://repo.spring.io/milestone")
//        maven("https://repo.spring.io/snapshot")
//        gradlePluginPortal()
//    }
//}