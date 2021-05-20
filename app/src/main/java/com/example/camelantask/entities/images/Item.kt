package com.example.camelantask.entities.images

data class Item(
    var checkin: Checkin?,
    var createdAt: Int?,
    var height: Int?,
    var id: String?,
    var prefix: String?,
    var source: Source?,
    var suffix: String?,
    var user: User?,
    var visibility: String?,
    var width: Int?
)