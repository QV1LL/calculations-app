package com.example.calculations;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String mainTag = "technical";

    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    private ArrayList<MenuItem> popupMenuItems = new ArrayList<MenuItem>();

    private ArrayList<Tank> savedTanks = new ArrayList<Tank>();

    private static int currentTankIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupPopupMenu();
    }

    private void setupPopupMenu() {
        Button popupMenuButton = (Button) findViewById(R.id.popup_menu_button);
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                Menu popupMenuMenu = popupMenu.getMenu();

                popupMenuItems.clear();
                tanks.clear();

                boolean needLoadTanks = (tanks.size() == 0) ? true : false;

                for(int i = 0; i < popupMenuMenu.size(); i++) {
                    popupMenuItems.add(popupMenuMenu.getItem(i));

                    if(needLoadTanks)
                        tanks.add(new Tank());
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemIndex = popupMenuItems.indexOf(menuItem);
                        currentTankIndex = itemIndex;

                        setupText();

                        Log.i(mainTag, "StartLevel: " + tanks.get(currentTankIndex).startLevel);

                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void setupTank() {
        tanks.get(currentTankIndex).startLevel = Float.valueOf((((TextView) findViewById(R.id.startLevel)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.startLevel)).getText().toString());
        tanks.get(currentTankIndex).endLevel = Float.valueOf((((TextView) findViewById(R.id.endLevel)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.endLevel)).getText().toString());

        tanks.get(currentTankIndex).startVolume = Float.valueOf((((TextView) findViewById(R.id.startVolume)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.startVolume)).getText().toString());
        tanks.get(currentTankIndex).endVolume = Float.valueOf((((TextView) findViewById(R.id.endVolume)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.endVolume)).getText().toString());

        tanks.get(currentTankIndex).startWater = Float.valueOf((((TextView) findViewById(R.id.startWaterVolume)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.startWaterVolume)).getText().toString());
        tanks.get(currentTankIndex).endWater = Float.valueOf((((TextView) findViewById(R.id.endWaterVolume)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.endWaterVolume)).getText().toString());

        tanks.get(currentTankIndex).startFreeDensityCounter = Float.valueOf((((TextView) findViewById(R.id.startFreeDensityCounter)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.startFreeDensityCounter)).getText().toString());
        tanks.get(currentTankIndex).endFreeDensityCounter = Float.valueOf((((TextView) findViewById(R.id.endFreeDensityCounter)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.endFreeDensityCounter)).getText().toString());

        tanks.get(currentTankIndex).startFreeDensityCounterValue = Float.valueOf((((TextView) findViewById(R.id.startFreeDensityCounterValue)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.startFreeDensityCounterValue)).getText().toString());
        tanks.get(currentTankIndex).endFreeDensityCounterValue = Float.valueOf((((TextView) findViewById(R.id.endFreeDensityCounterValue)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.endFreeDensityCounterValue)).getText().toString());

        tanks.get(currentTankIndex).start15DensityCounterValue = Float.valueOf((((TextView) findViewById(R.id.start15DensityCounterValue)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.start15DensityCounterValue)).getText().toString());
        tanks.get(currentTankIndex).end15DensityCounterValue = Float.valueOf((((TextView) findViewById(R.id.end15DensityCounterValue)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.end15DensityCounterValue)).getText().toString());

        tanks.get(currentTankIndex).start20DensityCounterValue = Float.valueOf((((TextView) findViewById(R.id.start20DensityCounterValue)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.start20DensityCounterValue)).getText().toString());
        tanks.get(currentTankIndex).end20DensityCounterValue = Float.valueOf((((TextView) findViewById(R.id.end20DensityCounterValue)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.end20DensityCounterValue)).getText().toString());
    }

    private void setupText() {
        ((TextView) findViewById(R.id.startLevel)).setText(String.valueOf(tanks.get(currentTankIndex).startLevel));
        ((TextView) findViewById(R.id.endLevel)).setText(String.valueOf(tanks.get(currentTankIndex).endLevel));

        ((TextView) findViewById(R.id.startVolume)).setText(String.valueOf(tanks.get(currentTankIndex).startVolume));
        ((TextView) findViewById(R.id.endVolume)).setText(String.valueOf(tanks.get(currentTankIndex).endVolume));

        ((TextView) findViewById(R.id.startWaterVolume)).setText(String.valueOf(tanks.get(currentTankIndex).startWater));
        ((TextView) findViewById(R.id.endWaterVolume)).setText(String.valueOf(tanks.get(currentTankIndex).endWater));

        ((TextView) findViewById(R.id.startFreeDensityCounter)).setText(String.valueOf(tanks.get(currentTankIndex).startFreeDensityCounter));
        ((TextView) findViewById(R.id.endFreeDensityCounter)).setText(String.valueOf(tanks.get(currentTankIndex).endFreeDensityCounter));

        ((TextView) findViewById(R.id.startFreeDensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).startFreeDensityCounterValue));
        ((TextView) findViewById(R.id.endFreeDensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).endFreeDensityCounterValue));

        ((TextView) findViewById(R.id.start15DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).start15DensityCounterValue));
        ((TextView) findViewById(R.id.end15DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).end15DensityCounterValue));

        ((TextView) findViewById(R.id.start20DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).start20DensityCounterValue));
        ((TextView) findViewById(R.id.end20DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).end20DensityCounterValue));
    }
}