package ch.meienberger.android.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 Contains common functions
 */

public class common {

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotate(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap =  rotate(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap =  rotate(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                bitmap =  flip(bitmap, true, false);
                break;

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                bitmap =  flip(bitmap, false, true);
                break;

            default:
                break;
        }

        return  bitmap;
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


}
