package io.cordova.test2_6720b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
/*
public class MyTransformation extends BitmapTransformation {

    private float rotate = 0f;

    public MyTransformation(Context context, int orientation) {
        super(context);

    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int exifOrientationDegrees = getExifOrientationDegrees(90);
        return TransformationUtils.rotateImageExif(toTransform, pool, exifOrientationDegrees);
    }
    @Override
    public String getId() {
        return "com.levendeev.map.geostories4";
    }

    private int getExifOrientationDegrees(int orientation) {
        int exifInt;
        switch (orientation) {
            case 90:
                exifInt = ExifInterface.ORIENTATION_ROTATE_90;
                break;
//more cases
            default:
                exifInt = ExifInterface.ORIENTATION_NORMAL;
                break;
        }
        return exifInt;
    }

}
*/