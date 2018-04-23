package com.example.janek.jcommanderandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Janek on 22.04.2018.
 */

public class DeleteFileTask extends AsyncTask<Void,Void,String> {

    private File file1;
    private boolean dismissed = false;
    private Context context;
    private AlertDialog progressDialog;

    public void init(File fileModel,Context context) {
        this.file1 = fileModel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(context);
        progressBuilder.setView(R.layout.pop_up)
                .setTitle(R.string.deleting_file)
                .setCancelable(true)
                .setPositiveButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.dismiss();
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
            file1.delete();
            System.out.println("File deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(!dismissed)
            progressDialog.dismiss();
        Toast.makeText(context, R.string.delete_done, Toast.LENGTH_SHORT).show();


    }
}
