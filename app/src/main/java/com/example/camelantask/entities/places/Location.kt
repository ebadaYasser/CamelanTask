package com.example.camelantask.entities.places

data class Location(
    var address: String?,
    var cc: String?,
    var city: String?,
    var country: String?,
    var crossStreet: String?,
    var distance: Int?,
    var formattedAddress: List<String>?,
    var labeledLatLngs: List<LabeledLatLng>?,
    var lat: Double?,
    var lng: Double?,
    var postalCode: String?,
    var state: String?
)