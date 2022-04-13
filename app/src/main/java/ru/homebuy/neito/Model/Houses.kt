package ru.homebuy.neito.Model

class Houses {
    private var costV: String
    private var locationV: String
    private var roomsV: String
    private var squareV: String
    private var image: String
    private var infoV: String
    private var put: String
    private var date: String
    private var time: String

    class Houses() {

    }

    constructor(costV: String, locationV: String, roomsV: String, squareV: String, image: String, infoV: String, put: String, date: String, time: String) {

        this.costV = costV
        this.locationV = locationV
        this.roomsV = roomsV
        this.squareV = squareV
        this.image = image
        this.infoV = infoV
        this.put = put
        this.date = date
        this.time = time
    }


    fun getCostV(): String {
        return costV
    }

    fun setCostV(costV: String) {
        this.costV = costV
    }

    fun getLocationV(): String {
        return locationV
    }

    fun setLocationV(locationV: String) {
        this.locationV = locationV
    }

    fun getRoomsV(): String {
        return roomsV
    }

    fun setRoomsV(roomsV: String) {
        this.roomsV = roomsV
    }

    fun getSquareV(): String {
        return squareV
    }

    fun setSquareV(squareV: String) {
        this.squareV = squareV
    }

    fun getImage(): String {
        return image
    }

    fun setImage(image: String?) {
        this.image = image!!
    }

    fun getInfoV(): String {
        return infoV
    }

    fun setInfoV(infoV: String) {
        this.infoV = infoV
    }

    fun getPut(): String {
        return put
    }

    fun setPut(put: String) {
        this.put = put
    }

    fun getDate(): String {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getTime(): String {
        return time
    }

    fun setTime(date: String) {
        this.time = time
    }
}
