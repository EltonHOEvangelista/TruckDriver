package com.example.truckdriver_v02.data.vehicle

interface Vehicle {
    var vehicleId: Int
    var plate: String?
    var vin: String?
    var manufacturer: String?
    var model: String?
    var vehicleColor: String?
    var capacity: String?
    var trailer: String?
    var ownerAccountNumber: Int
    var latitude: String?
    var longitude: String?
}
