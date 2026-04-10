package io.cordova.test2_6720b;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ZalobaActivity extends AppCompatActivity {

    private String targetUid, targetName, reporterUid, reporterName, screenshotPath;
    private EditText reasonInput;
    private Button sendBtn;
    private ProgressBar progress;
    private TextView targetInfo;
    private File screenshotFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaloba);

        // Получаем данные из Intent
        targetUid = getIntent().getStringExtra("target_uid");
        targetName = getIntent().getStringExtra("target_name");
        reporterUid = getIntent().getStringExtra("reporter_uid");
        reporterName = getIntent().getStringExtra("reporter_name");
        screenshotPath = getIntent().getStringExtra("screenshot_path");

        if (screenshotPath != null) {
            screenshotFile = new File(screenshotPath);  // 🔥 Создаём File объект
        }

        // Инициализация UI
        reasonInput = findViewById(R.id.reasonInput);
        sendBtn = findViewById(R.id.sendBtn);
        progress = findViewById(R.id.progress);
        targetInfo = findViewById(R.id.targetInfo);

        targetInfo.setText("Пользователь: " + (targetName != null ? targetName : targetUid));

        sendBtn.setOnClickListener(v -> {
            String reason = reasonInput.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "Введите причину жалобы", Toast.LENGTH_SHORT).show();
                return;
            }
            sendComplaint(reason);
        });
    }

    private void sendComplaint(String reason) {
        sendBtn.setEnabled(false);
        progress.setVisibility(View.VISIBLE);

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://rieltorov.net/save-complaint.php");
                conn = (HttpURLConnection) url.openConnection();
                String boundary = "----AndroidFormBoundary" + System.currentTimeMillis();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());


                // 🔥 Логируем ВСЕ данные перед отправкой
                Log.d("COMPLAINT_DUMP", "=== ОТРАВЛЯЕМ НА СЕРВЕР ===");
                Log.d("COMPLAINT_DUMP", "reporter_uid: " + (reporterUid != null ? reporterUid : "NULL"));
                Log.d("COMPLAINT_DUMP", "reporter_name: " + (reporterName != null ? reporterName : "NULL"));
                Log.d("COMPLAINT_DUMP", "target_uid: " + (targetUid != null ? targetUid : "NULL"));
                Log.d("COMPLAINT_DUMP", "target_name: " + (targetName != null ? targetName : "NULL"));
                Log.d("COMPLAINT_DUMP", "reason: [" + reason + "] (len=" + reason.length() + ")");
                Log.d("COMPLAINT_DUMP", "screenshotFile: " + (screenshotFile != null ? screenshotFile.getAbsolutePath() + " (" + screenshotFile.length() + " bytes)" : "NULL"));
                Log.d("COMPLAINT_DUMP", "=== КОНЕЦ ДАМПА ===");

                // Текстовые поля
                writeField(dos, boundary, "reporter_uid", reporterUid);
                writeFieldUTF8(dos, boundary, "reporter_name", reporterName);
                writeField(dos, boundary, "target_uid", targetUid);
                writeFieldUTF8(dos, boundary, "target_name", targetName);
                writeFieldUTF8(dos, boundary, "reason", reason);

                // Файл скриншота
                if (screenshotFile != null && screenshotFile.exists()) {
                    FileInputStream fis = new FileInputStream(screenshotFile);
                    writeFieldFile(dos, boundary, "screenshot", screenshotFile.getName(), fis);
                    fis.close();
                }

                // Завершение boundary
                dos.writeBytes("--" + boundary + "--\r\n");
                dos.flush();
                dos.close();

                int code = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder resp = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) resp.append(line);
                reader.close();

                final int finalCode = code;
                final String finalResp = resp.toString();
                new Handler(Looper.getMainLooper()).post(() -> {
                    progress.setVisibility(View.GONE);
                    sendBtn.setEnabled(true);


                    if (finalCode == 200 && finalResp.contains("\"status\":\"ok\"")) {
                        Toast.makeText(this, "Жалоба отправлена", Toast.LENGTH_SHORT).show();
                        // Очистка кэша
                        if (screenshotFile != null && screenshotFile.exists()) screenshotFile.delete();
                        finish();
                    } else {
                        Toast.makeText(this, "Ошибка сервера: " + finalCode, Toast.LENGTH_LONG).show();
                        Log.e("COMPLAINT", finalResp);
                    }
                });

            } catch (Exception e) {



                new Handler(Looper.getMainLooper()).post(() -> {
                    progress.setVisibility(View.GONE);
                    sendBtn.setEnabled(true);


                    // 🔥 ПОКАЗЫВАЕМ ТОЧНУЮ ОШИБКУ
                    String err = e.getClass().getSimpleName() + ": " + e.getMessage();
                    Toast.makeText(this, err, Toast.LENGTH_LONG).show();
                    Log.e("COMPLAINT_DEBUG", "❌ " + err);
                    e.printStackTrace(); // Полный стек в Logcat
                });

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    private void writeField(DataOutputStream dos, String boundary, String name, String value) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        dos.writeBytes(value + "\r\n");
    }

    private void writeFieldFile(DataOutputStream dos, String boundary, String fieldName, String fileName, InputStream is) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"\r\n");
        dos.writeBytes("Content-Type: image/jpeg\r\n\r\n");
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) != -1) dos.write(buffer, 0, len);
        dos.writeBytes("\r\n");
    }
    // 🔥 Хелпер для текстовых полей (с явным UTF-8)
    private void writeFieldUTF8(OutputStream out, String boundary, String name, String value) throws IOException {
        String part = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" +
                value + "\r\n";
        // 🔥 КЛЮЧЕВОЕ: явное кодирование в UTF-8
        out.write(part.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    // 🔥 Хелпер для файла
    private void writeFileUTF8(OutputStream out, String boundary, String fieldName, String fileName, InputStream is) throws IOException {
        String header = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"\r\n" +
                "Content-Type: image/jpeg\r\n\r\n";
        out.write(header.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) != -1) out.write(buffer, 0, len);
        out.write("\r\n".getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
}
