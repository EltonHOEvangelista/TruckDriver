package com.example.truckdriver_v02.data.vehicle

class TrailerModel {
    var trailerTypeId: Int = 0
    var trailerTypeName: String

    constructor(trailerTypeId: Int, trailerTypeName: String) {
        this.trailerTypeId = trailerTypeId
        this.trailerTypeName = trailerTypeName
    }

    constructor(trailerTypeName: String) {
        this.trailerTypeName = trailerTypeName
    }
}
