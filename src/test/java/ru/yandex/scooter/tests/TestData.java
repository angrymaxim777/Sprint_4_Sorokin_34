package ru.yandex.scooter.tests;

public class TestData {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String phone;
    private final String deliveryDate;
    private final String rentalPeriod;
    private final String color;
    private final String comment;

    public TestData(String firstName, String lastName, String address,
                    String phone, String deliveryDate, String rentalPeriod,
                    String color, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getDeliveryDate() { return deliveryDate; }
    public String getRentalPeriod() { return rentalPeriod; }
    public String getColor() { return color; }
    public String getComment() { return comment; }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}