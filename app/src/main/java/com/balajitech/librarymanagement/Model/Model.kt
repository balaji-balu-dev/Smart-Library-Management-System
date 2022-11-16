package com.balajitech.librarymanagement.Model;

class Model {
    var name: String? = null
    var city: String? = null
    var profilepic: String? = null
    var phoneNumber: String? = null
    var address: String? = null
    var userId: String? = null
    var pincode: String? = null
    var categoryImage: String? = null
    var category: String? = null
    var bookLocation: String? = null
    var bookName: String? = null
    var booksCount: String? = null
    var imageUrl: String? = null
    var pushKey: String? = null
    var notification: String? = null

    constructor() {}
    constructor(
        name: String?,
        city: String?,
        profilepic: String?,
        phoneNumber: String?,
        address: String?,
        userId: String?,
        pincode: String?,
        categoryImage: String?,
        category: String?,
        bookLocation: String?,
        bookName: String?,
        booksCount: String?,
        imageUrl: String?,
        pushKey: String?,
        notification: String?
    ) {
        this.name = name
        this.city = city
        this.profilepic = profilepic
        this.phoneNumber = phoneNumber
        this.address = address
        this.userId = userId
        this.pincode = pincode
        this.categoryImage = categoryImage
        this.category = category
        this.bookLocation = bookLocation
        this.bookName = bookName
        this.booksCount = booksCount
        this.imageUrl = imageUrl
        this.pushKey = pushKey
        this.notification = notification
    }
}