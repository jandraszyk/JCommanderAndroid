package com.example.janek.jcommanderandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
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

public class MoveFileTask extends AsyncTask<Void,Void,String> {

    File file1, file2;
    private AlertDialog progressDialog;
    private OutputStream outputStream;
    private Context context;
    private boolean canceled = false;
    private boolean dismissed = false;
    private boolean wasDir = false;
    private FileModelAdapter adapter;



    public void init(File file1, File file2, Context context,FileModelAdapter adapter) {
        this.file1 = file1;
        this.file2 = file2;
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {

                AlertDialog.Builder progressBuilder = new AlertDialog.Builder(context);
                progressBuilder.setView(R.layout.pop_up)
                        .setTitle(R.string.moving_file)
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

                moveFile(file1, file2);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(!canceled && !dismissed)
            progressDialog.dismiss();
        adapter.notifyDataSetChanged();
        Toast.makeText(context, R.string.moving_done, Toast.LENGTH_SHORT).show();
    }

    private void moveFile(File source, File directory) throws IOException {

        File dir = new File(directory.getAbsolutePath() + "/" + source.getName());
        if(source.isDirectory()) {
            wasDir = true;
            if(!dir.exists()) {
                dir.mkdir();
                System.out.println("Directory moved from " +source + " to " + directory);
            }
            System.out.println("Files moving from " +source + " to " + directory);

            String[] files = source.list();
            for(String srcFile : files) {
                File src = new File(source, srcFile);
                File dst = new File(dir, srcFile);
                moveFile(src, dst);
            }
        } else {
            System.out.println("Files moving from " +source + " to " + directory);

            InputStream inputStream = new FileInputStream(source);
            if(wasDir) {
                outputStream = new FileOutputStream(directory);
            } else {
                outputStream = new FileOutputStream(directory.getPath() + "/" + source.getName());
            }
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                if(!canceled)
                    outputStream.write(buffer, 0, length);

            }

            inputStream.close();
            outputStream.close();
            if(canceled) {
                File canceledFile = new File(directory.getPath() + "/" + source.getName());
                FileUtils.deleteQuietly(canceledFile);
            } else {
                FileUtils.deleteQuietly(source);
            }

            System.out.println("File moved from " + source + " to " + directory);
        }
    }


}
