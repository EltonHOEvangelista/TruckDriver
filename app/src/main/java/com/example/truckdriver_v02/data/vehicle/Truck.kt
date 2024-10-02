package com.example.truckdriver_v02.data.vehicle

class Truck : Vehicle {
    override var vehicleId: Int = 0
    override var plate: String? = null
    override var vin: String? = null
    override var manufacturer: String? = null
    override var model: String? = null
    override var vehicleColor: String? = null
    override var capacity: String? = null
    override var trailer: String? = null
    override var ownerAccountNumber: Int = 0
    override var latitude: String? = null
    override var longitude: String? = null
}
