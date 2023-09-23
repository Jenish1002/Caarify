package com.carserviceapp.carserviceapp.Models

class Requests(
    val requestId: String?,
    val serviceType: String?,
    val serviceDate: String?,
    val carOption: String?,
    val userId: String?,
) {
    constructor() : this(null, null, null, null, null)
}
