package com.example.camelantask.entities.images

data class Photos(
    var count: Int?,
    var dupesRemoved: Int?,
    var items: List<Item>?
)