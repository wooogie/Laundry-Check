package ch.meienberger.android.laundrycheck.custom_class_objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This Class handles the mapping from clothes to the washorders. It also keeps the information, if the clothes is already returned.
 */




public class Mapping {


    private long id = 0;
    private long washorder_id = 0;
    private long clothes_id = 0;
    private boolean clothes_returned = false;


    public Mapping(long Washorder_ID,long Clothes_ID){

        washorder_id = Washorder_ID;
        clothes_id = Clothes_ID;

    }


    public long getId() {
        return id;
    }

    public void setId(long Id) {
        id = Id;
    }

    public long getWashorder_id() {
        return washorder_id;
    }

    public long getClothes_id() {
        return clothes_id;
    }

    public boolean isClothes_returned() {
        return clothes_returned;
    }

    public void setClothes_returned(boolean clothes_returned) {
        this.clothes_returned = clothes_returned;
    }


    public boolean checkChanges(Mapping mapping1,Mapping mapping2){

        if (mapping1.isClothes_returned()!=mapping2.isClothes_returned()){
            return true;
        }

        if (mapping1.getClothes_id()!=mapping2.getClothes_id()){
            return true;
        }

        if (mapping1.getWashorder_id()!=mapping2.getWashorder_id()){
            return true;
        }

        return false;
    }
}
