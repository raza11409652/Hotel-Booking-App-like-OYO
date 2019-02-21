package com.flatondemand.user.fod.model;

public class BookingList {
   /*booking":[{"id":"25","bookingNo":"FOD927259",
   "bookedOn":"2019-02-03 20:27:01",
   "dateFrom":"2019-02-03",
   "dateTo":"2020-02-02","propertyName":"xyz"
   * */
   String id , bookingNo, bookedOn ,dateFrom ,propertyName , dateTo;
    public BookingList() {
    }
    public BookingList(String id, String bookingNo, String bookedOn, String dateFrom, String propertyName , String dateTo) {
        this.id = id;
        this.bookingNo = bookingNo;
        this.bookedOn = bookedOn;
        this.dateFrom = dateFrom;
        this.propertyName = propertyName;
        this.dateTo=dateTo;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
