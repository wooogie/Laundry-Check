package ch.meienberger.android.laundrycheck.custom_class_objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This Class represents a clothes which can be washed.
 */




public class Clothes implements Cloneable {

    public static final String BITMAP_ENDING = "_bitmap.png";

    private long id = 0;
    private String name = "";
    private String rfid_id = "";
    private String picture = "";
    private int washcount = 0;
    private String last_washed = "";
    private int pieces = 0;
    private Clothestype clothestype = Clothestype.Others;

    public enum Clothestype{
        Others(0),
        Pullover(1),
        Socks(2),
        Pants(3),
        Underwear(4),
        TShirt(5),
        Skirt(6),
        Dress(7);

        public final int value;

        Clothestype(final int value) {
            this.value = value;
        }

         public Clothestype getValue(int value){
            for(Clothestype e: Clothestype.values()){
                if(e.value == value){
                    return e;
                }
            }
            return Clothestype.Others; //clothestype not found
        }

         Clothestype getFromName(String value){
            for(Clothestype e: Clothestype.values()){
                if(e.name().equalsIgnoreCase(value)){
                    return e;
                }
            }
            return Clothestype.Others; //clothestype not found
        }
    }

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

    public String getPicturePath() {
        return picture;
    }

    public String getBitmapPath() {
        return picture.replace(".jpg",BITMAP_ENDING);
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
        this.washcount = this.washcount = this.washcount + 1;

        //generate Date and set last washed date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        this.last_washed = formattedDate;
    }

    public void undo_washed(){
        this.washcount = this.washcount - 1;
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

    public Clothestype getClothestype() {
        return clothestype;
    }

    public void setClothestype(Clothestype clothestype) {
        this.clothestype = clothestype;
    }

    public boolean checkChanges(Clothes otherClothes){

        if (!this.getPicturePath().contentEquals(otherClothes.getPicturePath())){
            return true;
        }
        if (this.getClothestype()!=otherClothes.getClothestype()){
            return true;
        }
        if (!this.getLast_washed().contentEquals(otherClothes.getLast_washed())){
            return true;
        }
        if (!this.getName().contentEquals(otherClothes.getName())){
            return true;
        }
        if (!this.getRfid_id().contentEquals(otherClothes.getRfid_id())){
            return true;
        }
        if (this.getPieces()!=otherClothes.getPieces()){
            return true;
        }
        if (this.getWashcount()!=otherClothes.getWashcount()){
            return true;
        }

        return false;
    }

    @Override
    public Clothes clone(){
        try {
            return (Clothes)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
