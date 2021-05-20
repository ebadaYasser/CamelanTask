package com.example.camelantask.entities.places

data class Venue(
    var categories: List<Category>?,
    var id: String?,
    var location: Location?,
    var name: String?,
    var popularityByGeo: Double?,
    var venuePage: VenuePage?
)