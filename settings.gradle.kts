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
    "channel",
    "comment",
    "group",
    "event",
    "job"
)

project(":application").projectDir = file("application")
project(":auth").projectDir = file("auth")
project(":common").projectDir = file("common")
project(":interaction").projectDir = file("interaction")
project(":message").projectDir = file("message")
project(":notification").projectDir = file("notification")
project(":post").projectDir = file("post")
project(":user").projectDir = file("user")
project(":group").projectDir = file("group")
project(":channel").projectDir = file("channel")
project(":comment").projectDir = file("comment")
project(":event").projectDir = file("event")
project(":job").projectDir = file("job")


//pluginManagement {
//    repositories {
//        maven("https://repo.spring.io/milestone")
//        maven("https://repo.spring.io/snapshot")
//        gradlePluginPortal()
//    }
//}