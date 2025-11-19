rootProject.name = "etsia-backend"
include("common", "interaction")

include(
    "application",
    "common",
    "interaction",
    "message",
    "notification",
    "post",
    "user",
    "auth",
    "group"
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