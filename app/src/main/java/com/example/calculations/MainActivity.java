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

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private static String mainTag = "technical";

    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    private int tankCount = 4;

    private ArrayList<MenuItem> popupMenuItems = new ArrayList<MenuItem>();

    private ArrayList<Tank> savedTanks = new ArrayList<Tank>();

    private static int currentTankIndex = 0;
    private boolean isLoadedData = false;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        for(int i = 0; i < tanks.size(); i++) {
            String key = "tank" + String.valueOf(i);

            savedInstanceState.putSerializable(key, tanks.get(i));
        }
    }

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

        if (savedInstanceState != null)
            for(int i = 0; i < tankCount; i++) {
                String key = "tank" + String.valueOf(i);
                Tank tank = (Tank) savedInstanceState.getSerializable(key);

                tanks.add(tank);
            }

        isLoadedData = (tanks.size() != 0);

        updateTimer();

        setupPopupMenu();
        setupResetButton();
    }

    private void setupResetButton() {
        Button resetButton = (Button) findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tanks.isEmpty()) {
                    tanks.get(currentTankIndex).resetInstances();
                    setupText();
                }
            }
        });
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

                for(int i = 0; i < popupMenuMenu.size(); i++) {
                    popupMenuItems.add(popupMenuMenu.getItem(i));
                }

                if (!isLoadedData)
                    for(int i = 0; i < popupMenuMenu.size(); i++) {
                        tanks.add(new Tank());
                    }

                setupTank();
                tanks.get(currentTankIndex).setupInstances();

                setupText();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemIndex = popupMenuItems.indexOf(menuItem);
                        currentTankIndex = itemIndex;

                        setupText();

                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void setupTank() {
        tanks.get(currentTankIndex).name = (((EditText) findViewById(R.id.reservoirName)).getText().toString().isEmpty()) ? "резервуар" : ((EditText) findViewById(R.id.reservoirName)).getText().toString();

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

        Tank.startCounter = Float.valueOf((((TextView) findViewById(R.id.startCounter)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.startCounter)).getText().toString());
        Tank.endCounter = Float.valueOf((((TextView) findViewById(R.id.endCounter)).getText().toString().isEmpty()) ? "0f" : ((TextView) findViewById(R.id.endCounter)).getText().toString());

        float result = 0;

        for(Tank tank : tanks)
            result += tank.reception;

        Tank.together = result;
    }

    private void setupText() {
        ((EditText) findViewById(R.id.reservoirName)).setText(tanks.get(currentTankIndex).name);

        for (int i = 0; i < popupMenuItems.size(); i++)
            popupMenuItems.get(i).setTitle(tanks.get(i).name);

        ((EditText) findViewById(R.id.startLevel)).setText(String.valueOf(tanks.get(currentTankIndex).startLevel));
        ((EditText) findViewById(R.id.endLevel)).setText(String.valueOf(tanks.get(currentTankIndex).endLevel));

        ((EditText) findViewById(R.id.startVolume)).setText(String.valueOf(tanks.get(currentTankIndex).startVolume));
        ((EditText) findViewById(R.id.endVolume)).setText(String.valueOf(tanks.get(currentTankIndex).endVolume));

        ((EditText) findViewById(R.id.startWaterVolume)).setText(String.valueOf(tanks.get(currentTankIndex).startWater));
        ((EditText) findViewById(R.id.endWaterVolume)).setText(String.valueOf(tanks.get(currentTankIndex).endWater));

        ((TextView) findViewById(R.id.startVolumeWithoutWater)).setText(String.format("%.3f", tanks.get(currentTankIndex).startVolumeWithoutWater));
        ((TextView) findViewById(R.id.endVolumeWithoutWater)).setText(String.format("%.3f", tanks.get(currentTankIndex).endVolumeWithoutWater));

        ((EditText) findViewById(R.id.startFreeDensityCounter)).setText(String.valueOf(tanks.get(currentTankIndex).startFreeDensityCounter));
        ((EditText) findViewById(R.id.endFreeDensityCounter)).setText(String.valueOf(tanks.get(currentTankIndex).endFreeDensityCounter));

        ((EditText) findViewById(R.id.startFreeDensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).startFreeDensityCounterValue));
        ((EditText) findViewById(R.id.endFreeDensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).endFreeDensityCounterValue));

        ((EditText) findViewById(R.id.start15DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).start15DensityCounterValue));
        ((EditText) findViewById(R.id.end15DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).end15DensityCounterValue));

        ((EditText) findViewById(R.id.start20DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).start20DensityCounterValue));
        ((EditText) findViewById(R.id.end20DensityCounterValue)).setText(String.valueOf(tanks.get(currentTankIndex).end20DensityCounterValue));

        ((TextView) findViewById(R.id.startTons)).setText(String.format("%.3f", tanks.get(currentTankIndex).startTons));

        ((TextView) findViewById(R.id.endTons)).setText(String.format("%.3f", tanks.get(currentTankIndex).endTons));

        ((TextView) findViewById(R.id.reception)).setText(String.format("%.3f", tanks.get(currentTankIndex).reception));

        ((EditText) findViewById(R.id.startCounter)).setText(String.valueOf(Tank.startCounter));
        ((EditText) findViewById(R.id.endCounter)).setText(String.valueOf(Tank.endCounter));

        ((TextView)findViewById(R.id.receptionByCounter)).setText(String.format("%.3f", Tank.endCounter - Tank.startCounter));
        ((TextView)findViewById(R.id.together)).setText(String.format("%.3f", Tank.together));

        ((TextView)findViewById(R.id.difference)).setText(String.format("%.3f", Tank.together - (Tank.endCounter - Tank.startCounter)));

        ((TextView)findViewById(R.id.percents)).setText(String.format("%.2f", (Tank.together - (Tank.endCounter - Tank.startCounter)) / (Tank.endCounter - Tank.startCounter) * 100) + "%");
    }

    private void updateTimer() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date currentTime = new Date();

        String result = timeFormat.format(currentTime);

        ((TextView) findViewById(R.id.currentData)).setText(result);
    }
}