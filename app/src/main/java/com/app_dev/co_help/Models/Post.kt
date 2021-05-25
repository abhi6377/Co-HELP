package com.app_dev.co_help.Models

class Post (
    var bloodGr: String = "",
    var city : String = "",
    var createdBy: User = User(),
    var createdAt: Long = 0L,
    var gender: String ="",
    var age: String = ""){


    fun GetBloodGr(): String { return bloodGr}

    fun GetCity(): String {return city}

    fun GetCreatedBy(): User {return createdBy}

    fun GetCreatedAt(): Long {return createdAt}

    fun GetGender(): String {return gender}

    fun GetAge(): String {return age}


}
