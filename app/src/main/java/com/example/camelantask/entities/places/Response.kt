package com.example.camelantask.entities.places

data class Response(
    var groups: List<Group>?,
    var headerFullLocation: String?,
    var headerLocation: String?,
    var headerLocationGranularity: String?,
    var suggestedBounds: SuggestedBounds?,
    var suggestedRadius: Int?,
    var totalResults: Int?,
    var warning: Warning?
)