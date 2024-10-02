package com.example.truckdriver_v02.data.account

class Driver {
    var accountId: Int? = 0
    var firstName: String? = null
    var lastName: String? = null
    var phoneNumber: Long? = 0
    var eMail: String? = null
    var passWord: String? = null
    var isActive: Int? = 0
    var driverStatus: Int? = 0

    //to validate sign in
    var isAuthenticated: Boolean? = false
    var isValidated: Boolean? = false

    constructor(
        accountId: Int,
        firstName: String?,
        lastName: String?,
        phoneNumber: Long,
        eMail: String,
        passWord: String,
        isActive: Int,
        driverStatus: Int
    ) {
        this.accountId = accountId
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.eMail = eMail
        this.passWord = passWord
        this.isActive = isActive
        this.driverStatus = driverStatus
    }

    //constructor to sign in
    constructor(email: String, password: String) {
        this.eMail = email
        this.passWord = password
    }
}
