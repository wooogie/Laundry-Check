package ch.meienberger.android.laundrycheck.custom_class_objects;

/**
 * This Class represents a washorder on a laundry with all the data which are important related to it.
 */

public class Washorder implements Cloneable{


    private long id = 0;
    private String name = "";
    private String address = "";
    private String delivery_date = "";
    private String pickup_date = "";
    private int clothes_count = 0;
    private String comments = "";

    //in cents
    private int price = 0;

    public Washorder(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

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

    public boolean checkChanges(Washorder otherWashorder){

        if (!this.getName().contentEquals(otherWashorder.getName())){
            return true;
        }
        if (!this.getAddress().contentEquals(otherWashorder.getAddress())){
            return true;
        }
        if (!this.getDelivery_date().contentEquals(otherWashorder.getDelivery_date())){
            return true;
        }
        if (!this.getPickup_date().contentEquals(otherWashorder.getPickup_date())){
            return true;
        }
        if (!this.getComments().contentEquals(otherWashorder.getComments())){
            return true;
        }
        if (this.getClothes_count()!=otherWashorder.getClothes_count()){
            return true;
        }
        if (this.getPrice()!=otherWashorder.getPrice()){
            return true;
        }
        return false;
    }

    @Override
    public Washorder clone(){
        try {
            return (Washorder) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
