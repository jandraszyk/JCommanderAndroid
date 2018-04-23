package com.example.janek.jcommanderandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Janek on 18.04.2018.
 */

public class FileModelAdapter extends ArrayAdapter<FileModel> {

    private ArrayList<FileModel> files;
    private Activity context;

    public FileModelAdapter(@NonNull Activity context, int textViewResourceId, @NonNull ArrayList<FileModel> objects) {
        super(context, textViewResourceId, objects);
        this.files = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.list_file, null,true);
        }
        final FileModel fileModel = files.get(position);
        if(fileModel != null) {
            if(fileModel.isSelected()) {
                v.setBackgroundColor(Color.GRAY);
            } else {
                v.setBackgroundColor(Color.TRANSPARENT);
            }

            final ImageView icon = v.findViewById(R.id.icon);
            if (fileModel.getCurrentDirectory().isDirectory()) {
                icon.setImageResource(R.drawable.ic_icon);
            } else {
                icon.setImageResource(R.drawable.ic_file);
            }
            TextView fileName = v.findViewById(R.id.fileName);
            TextView fileSize = v.findViewById(R.id.fileSize);
            TextView fileDate = v.findViewById(R.id.fileDate);
            if(fileName != null) {
                fileName.setText(fileModel.getFileName());
            }
            if (fileSize != null) {
                fileSize.setText(fileModel.getFileSize());
            }
            if (fileDate != null) {
                fileDate.setText(fileModel.getFileLastModified());
            }

        }
        return v;
    }

    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }
}
