package com.example.td_android_studio.RandomUserGenerator

data class LocationRUG (
    var street : AddressRUG? = null,
    var city: String ?=null,
    var state: String ?= null,
    var country: String ?= null
) {
    override fun toString(): String {
        return "${street} ${city}, ${state} ${country}"
    }
}
