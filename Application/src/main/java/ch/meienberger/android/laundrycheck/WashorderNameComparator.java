package ch.meienberger.android.laundrycheck;

import java.util.Comparator;

/**
 * Created by Silvan on 15.10.2017.
 */

public class WashorderNameComparator implements Comparator<Washorder>
{
    public int compare(Washorder left, Washorder right) {
        return right.getName().compareTo(left.getName());
    }
}