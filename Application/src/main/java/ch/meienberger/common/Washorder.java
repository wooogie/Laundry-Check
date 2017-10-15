package ch.meienberger.common;

import java.util.UUID;

/**
 * This Class represents a washorder on a laundry with all the data which are important related to it.
 */

public class Washorder {


    private String id;
    private String name = "";
    private String address = "";
    private String delivery_date = "";
    private String pickup_date = "";
    private int clothes_count = 0;
    private String comments = "";

    //in cents
    private int price = 0;

    public Washorder(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public int getClothes_count() {
        return clothes_count;
    }

    public void setClothes_count(int clothes_count) {
        this.clothes_count = clothes_count;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    //for test and remove after, TODO
    @Override
    public String toString() {
       String output = "Id = " + this.id + " ; Name = " + this.name;

        return output;
    }

}
