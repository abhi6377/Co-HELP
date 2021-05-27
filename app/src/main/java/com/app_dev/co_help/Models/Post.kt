package com.app_dev.co_help.Models

class Post (
    var bloodGr: String = "",
    var city : String = "",
    var createdBy: User = User(),
    var createdAt: Long = 0L,
    var gender: String ="",
    var age: String = "",
    var email: String? =""){


    fun GetBloodGr(): String { return bloodGr}

    fun GetCity(): String {return city}

    fun GetCreatedBy(): String? {return createdBy.displayName}

    fun GetCreatedAt(): Long {return createdAt}

    fun GetGender(): String {return gender}

    fun GetAge(): String {return age}

    fun GetEmail(): String?{ return  email}

    /* CHANGED BY ME 2 - added email*/


}
