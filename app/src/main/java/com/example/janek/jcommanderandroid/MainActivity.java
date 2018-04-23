package com.example.janek.jcommanderandroid;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity implements leftListFragment.OnLeftFragmentInteractionListener, rightListFragment.OnRightFragmentInteractionListener {

    private ImageButton dirButton;
    private leftListFragment leftList;
    private rightListFragment rightList;
    private boolean left = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String leftDirectory = "/storage/emulated/0";
        String rightDirectory = "/storage/emulated/0/DCIM/";
        final Bundle bundleLeft = new Bundle();
        bundleLeft.putString("LEFT_DIR", leftDirectory);
        final Bundle bundleRight = new Bundle();
        bundleRight.putString("RIGHT_DIR", rightDirectory);
        dirButton = findViewById(R.id.dirButton);

        leftList = new leftListFragment();
        rightList = new rightListFragment();
        leftList.setArguments(bundleRight);
        rightList.setArguments(bundleLeft);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, leftList).commit();

        dirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(left) {
                    left = false;
                    dirButton.setImageResource(R.drawable.ic_right);
                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace( R.id.fragment_container,rightList);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                else {
                    left = true;
                    dirButton.setImageResource(R.drawable.ic_left);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, leftList);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onLeftFragmentInteraction(FileModel file) {
        rightList.updateLeftDir(file);
    }

    @Override
    public void onRightFragmentInteraction(FileModel file) {
        leftList.updateRightDir(file);
    }
}
