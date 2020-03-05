package io.cordova.test2_6720b;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class FileLoadingTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private File destination;
    private TinderCard.FileLoadingListener fileLoadingListener;
    private Throwable throwable;

    public FileLoadingTask(String url, File destination, TinderCard.FileLoadingListener fileLoadingListener) {
        this.url = url;
        this.destination = destination;
        this.fileLoadingListener = fileLoadingListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fileLoadingListener.onBegin();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            FileUtils.copyURLToFile(new URL(url), destination);
        } catch (IOException e) {
            throwable = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fileLoadingListener.onEnd();
        if (throwable != null) {
            fileLoadingListener.onFailure(throwable);
        } else {
            fileLoadingListener.onSuccess();
        }
    }


}