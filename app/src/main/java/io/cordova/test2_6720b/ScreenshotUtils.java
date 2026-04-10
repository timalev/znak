package io.cordova.test2_6720b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenshotUtils {

    /**
     * Делает скриншот View и сохраняет в кэш приложения.
     * @return Файл со скриншотом или null при ошибке.
     */
    public static File captureToCache(View view, Context context) {
        try {
            // 🔥 Проверка: View должен быть отрисован
            if (view == null || view.getWidth() == 0 || view.getHeight() == 0) {
                // Пробуем измерить вручную, если View ещё не отрисован
                view.measure(
                        View.MeasureSpec.makeMeasureSpec(context.getResources().getDisplayMetrics().widthPixels, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            }

            if (view.getWidth() == 0 || view.getHeight() == 0) {
                return null; // Не удалось получить размеры
            }

            // Создаём Bitmap
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            // Сохраняем в кэш
            File file = new File(context.getCacheDir(), "complaint_screen_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos); // 85% качества
            fos.flush();
            fos.close();

            bitmap.recycle(); // Освобождаем память
            return file;

        } catch (OutOfMemoryError e) {
            // Памяти не хватило для большого скриншота
            return null;
        } catch (Exception e) {
            // Любая другая ошибка
            return null;
        }
    }
}