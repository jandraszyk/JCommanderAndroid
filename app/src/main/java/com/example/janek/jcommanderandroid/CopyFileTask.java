package com.example.janek.jcommanderandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Janek on 21.04.2018.
 */

public class CopyFileTask extends AsyncTask<Void, Void, String> {

    private File file1, file2;
    private AlertDialog progressDialog;
    private boolean canceled;
    private Context context;
    private boolean dismissed = false;

    private boolean wasDir = false;
    private OutputStream outputStream;

    public void init(File fileModel1, File fileModel2, Context context ){
        this.file1 = fileModel1;
        this.file2 = fileModel2;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {

            AlertDialog.Builder progressBuilder = new AlertDialog.Builder(context);
            progressBuilder.setView(R.layout.pop_up)
                    .setTitle(R.string.copying_file)
                    .setCancelable(true)
                    .setPositiveButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.dismiss();
                            canceled = true;
                            dismissed = true;
                        }
                    })
                    .setNegativeButton(R.string.dialog_hide, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.dismiss();
                        }
                    });
            progressDialog = progressBuilder.create();
            progressDialog.show();

    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

                copyFolder(file1, file2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected void onPostExecute(String result) {
        if(!canceled && !dismissed)
            progressDialog.dismiss();
        Toast.makeText(context, R.string.copy_done, Toast.LENGTH_SHORT).show();

    }

    private void copyFolder(File file, File directory) throws IOException {

        File dir = new File(directory.getAbsolutePath() + "/" + file.getName());
        if(file.isDirectory()) {
            wasDir = true;
            if(!dir.exists()) {
                dir.mkdir();
                System.out.println("Directory copied from " +file + " to " + directory);
            }
            System.out.println("Files copying from " +file + " to " + directory);

            String[] files = file.list();
            for(String srcFile : files) {
                File src = new File(file, srcFile);
                File dst = new File(dir, srcFile);
                copyFolder(src, dst);
            }
        } else {
            System.out.println("Files copying from " + file + " to " + directory);


                InputStream inputStream = new FileInputStream(file);
                if (wasDir) {
                    outputStream = new FileOutputStream(directory);
                } else {
                    outputStream = new FileOutputStream(directory.getPath() + "/" + file.getName());
                }
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    if (!canceled)
                        outputStream.write(buffer, 0, length);

                }

                inputStream.close();
                outputStream.close();
                if (canceled) {
                    File canceledFile = new File(directory.getPath() + "/" + file.getName());
                    FileUtils.deleteQuietly(canceledFile);
                }

                System.out.println("File copied from " + file + " to " + directory);

        }

    }
}
