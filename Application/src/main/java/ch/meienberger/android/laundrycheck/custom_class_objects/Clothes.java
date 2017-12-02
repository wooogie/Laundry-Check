package ch.meienberger.android.laundrycheck.custom_class_objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This Class represents a clothes which can be washed.
 */




public class Clothes {


    private long id = 0;
    private String name = "";
    private String rfid_id = "";
    private String picture = "";
    private int washcount = 0;
    private String last_washed = "";
    private int pieces = 0;
    private int clothestype = 0;

    public Clothes(){

    }



    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfid_id() {
        return rfid_id;
    }

    public void setRfid_id(String rfid_id) {
        this.rfid_id = rfid_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getWashcount() {
        return washcount;
    }

    public void setWashcount(int washcount) {
        this.washcount = washcount;
    }

    public void washed() {
        this.washcount = this.washcount++;

        //generate Date and set last washed date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        this.last_washed = formattedDate;
    }

    public String getLast_washed() {
        return last_washed;
    }

    public void setLast_washed(String last_washed) {
        this.last_washed = last_washed;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public int getClothestype() {
        return clothestype;
    }

    public void setClothestype(int clothestype) {
        this.clothestype = clothestype;
    }
}
