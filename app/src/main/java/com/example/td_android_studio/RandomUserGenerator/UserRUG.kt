package com.example.td_android_studio.RandomUserGenerator

data class UserRUG (
    var gender: String ?=null,
    var name: NameRUG ?= null,
    var location: LocationRUG?=null,
    var phone: String ?=null,
    var picture: PictureRUG?=null
)
