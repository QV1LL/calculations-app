package com.example.calculations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static String mainTag = "technical";

    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    private int tankCount = 4;

    private ArrayList<MenuItem> popupMenuItems = new ArrayList<MenuItem>();
    private ArrayList<Tank> savedTanks = new ArrayList<Tank>();

    private Gson gson;

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

        gson = new Gson();

        updateTimer();
        loadData();

        if(tanks.size() == 0) {
            Log.i(mainTag, "Creating new tanks...");

            for(int i = 0; i < tankCount; i++) {
                tanks.add(new Tank());
            }
        }

        setupPopupMenu();
        setupResetButton();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savePreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(int i = 0; i < tankCount; i++) {
            String tankGson = gson.toJson(tanks.get(i));

            editor.putString("tank" + i, tankGson);

            Log.i(mainTag, "Saved tank name: " + tanks.get(i).name);
        }

        editor.putString("startCounter", String.format(Locale.ROOT, "%.3f", Tank.startCounter));
        editor.putString("endCounter", String.format(Locale.ROOT, "%.3f", Tank.endCounter));

        editor.putString("other1", ((TextView) findViewById(R.id.otherText1)).getText().toString());
        editor.putString("other2", ((TextView) findViewById(R.id.otherText2)).getText().toString());
        editor.putString("other3", ((TextView) findViewById(R.id.otherText3)).getText().toString());

        editor.commit();
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savePreference", MODE_PRIVATE);

        if (sharedPreferences.getString("tank1", "") != "") {
            for(int i = 0; i < tankCount; i++) {
                String tankGson = sharedPreferences.getString("tank" + i, "");
                Tank loadTank = gson.fromJson(tankGson, Tank.class);

                tanks.add(loadTank);
            }

            setupText();
        }

        if(sharedPreferences.getString("startCounter", "") != "") {
            Tank.startCounter = Double.parseDouble(sharedPreferences.getString("startCounter", ""));
            setupText();
        }

        if(sharedPreferences.getString("endCounter", "") != "") {
            Tank.endCounter = Double.parseDouble(sharedPreferences.getString("endCounter", ""));
            setupText();
        }

        ((TextView) findViewById(R.id.otherText1)).setText(sharedPreferences.getString("other1", ""));
        ((TextView) findViewById(R.id.otherText2)).setText(sharedPreferences.getString("other2", ""));
        ((TextView) findViewById(R.id.otherText3)).setText(sharedPreferences.getString("other3", ""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    public void finish() {
        super.finish();
        saveData();
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

                updateTimer();

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

        tanks.get(currentTankIndex).startLevel = Double.parseDouble((((TextView) findViewById(R.id.startLevel)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.startLevel)).getText().toString());
        tanks.get(currentTankIndex).endLevel = Double.parseDouble((((TextView) findViewById(R.id.endLevel)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.endLevel)).getText().toString());

        tanks.get(currentTankIndex).startVolume = Double.parseDouble((((TextView) findViewById(R.id.startVolume)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.startVolume)).getText().toString());
        tanks.get(currentTankIndex).endVolume = Double.parseDouble((((TextView) findViewById(R.id.endVolume)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.endVolume)).getText().toString());

        tanks.get(currentTankIndex).startWater = Double.parseDouble((((TextView) findViewById(R.id.startWaterVolume)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.startWaterVolume)).getText().toString());
        tanks.get(currentTankIndex).endWater = Double.parseDouble((((TextView) findViewById(R.id.endWaterVolume)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.endWaterVolume)).getText().toString());

        tanks.get(currentTankIndex).startFreeDensityCounter = Double.parseDouble((((TextView) findViewById(R.id.startFreeDensityCounter)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.startFreeDensityCounter)).getText().toString());
        tanks.get(currentTankIndex).endFreeDensityCounter = Double.parseDouble((((TextView) findViewById(R.id.endFreeDensityCounter)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.endFreeDensityCounter)).getText().toString());

        tanks.get(currentTankIndex).startFreeDensityCounterValue = Double.parseDouble((((TextView) findViewById(R.id.startFreeDensityCounterValue)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.startFreeDensityCounterValue)).getText().toString());
        tanks.get(currentTankIndex).endFreeDensityCounterValue = Double.parseDouble((((TextView) findViewById(R.id.endFreeDensityCounterValue)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.endFreeDensityCounterValue)).getText().toString());

        tanks.get(currentTankIndex).start15DensityCounterValue = Double.parseDouble((((TextView) findViewById(R.id.start15DensityCounterValue)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.start15DensityCounterValue)).getText().toString());
        tanks.get(currentTankIndex).end15DensityCounterValue = Double.parseDouble((((TextView) findViewById(R.id.end15DensityCounterValue)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.end15DensityCounterValue)).getText().toString());

        tanks.get(currentTankIndex).start20DensityCounterValue = Double.parseDouble((((TextView) findViewById(R.id.start20DensityCounterValue)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.start20DensityCounterValue)).getText().toString());
        tanks.get(currentTankIndex).end20DensityCounterValue = Double.parseDouble((((TextView) findViewById(R.id.end20DensityCounterValue)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.end20DensityCounterValue)).getText().toString());

        Tank.startCounter = Double.parseDouble((((TextView) findViewById(R.id.startCounter)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.startCounter)).getText().toString());
        Tank.endCounter = Double.parseDouble((((TextView) findViewById(R.id.endCounter)).getText().toString().isEmpty()) ? "0d" : ((TextView) findViewById(R.id.endCounter)).getText().toString());

        double result = 0;

        for(Tank tank : tanks)
            result += tank.reception;

        Tank.together = result;
    }

    private void setupText() {
        ((EditText) findViewById(R.id.reservoirName)).setText(tanks.get(currentTankIndex).name);

        for (int i = 0; i < popupMenuItems.size(); i++)
            popupMenuItems.get(i).setTitle(tanks.get(i).name);

        ((EditText) findViewById(R.id.startLevel)).setText(String.format(Locale.ROOT, "%.1f", tanks.get(currentTankIndex).startLevel));
        ((EditText) findViewById(R.id.endLevel)).setText(String.format(Locale.ROOT, "%.1f", tanks.get(currentTankIndex).endLevel));

        ((EditText) findViewById(R.id.startVolume)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).startVolume));
        ((EditText) findViewById(R.id.endVolume)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).endVolume));

        ((EditText) findViewById(R.id.startWaterVolume)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).startWater));
        ((EditText) findViewById(R.id.endWaterVolume)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).endWater));

        ((TextView) findViewById(R.id.startVolumeWithoutWater)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).startVolumeWithoutWater));
        ((TextView) findViewById(R.id.endVolumeWithoutWater)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).endVolumeWithoutWater));

        ((EditText) findViewById(R.id.startFreeDensityCounter)).setText(String.format(Locale.ROOT, "%.1f", tanks.get(currentTankIndex).startFreeDensityCounter));
        ((EditText) findViewById(R.id.endFreeDensityCounter)).setText(String.format(Locale.ROOT, "%.1f", tanks.get(currentTankIndex).endFreeDensityCounter));

        ((EditText) findViewById(R.id.startFreeDensityCounterValue)).setText(String.format(Locale.ROOT, "%.2f", tanks.get(currentTankIndex).startFreeDensityCounterValue));
        ((EditText) findViewById(R.id.endFreeDensityCounterValue)).setText(String.format(Locale.ROOT, "%.2f", tanks.get(currentTankIndex).endFreeDensityCounterValue));

        ((EditText) findViewById(R.id.start15DensityCounterValue)).setText(String.format(Locale.ROOT, "%.2f", tanks.get(currentTankIndex).start15DensityCounterValue));
        ((EditText) findViewById(R.id.end15DensityCounterValue)).setText(String.format(Locale.ROOT, "%.2f", tanks.get(currentTankIndex).end15DensityCounterValue));

        ((EditText) findViewById(R.id.start20DensityCounterValue)).setText(String.format(Locale.ROOT, "%.2f", tanks.get(currentTankIndex).start20DensityCounterValue));
        ((EditText) findViewById(R.id.end20DensityCounterValue)).setText(String.format(Locale.ROOT, "%.2f", tanks.get(currentTankIndex).end20DensityCounterValue));

        ((TextView) findViewById(R.id.startTons)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).startTons));

        ((TextView) findViewById(R.id.endTons)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).endTons));

        ((TextView) findViewById(R.id.reception)).setText(String.format(Locale.ROOT, "%.3f", tanks.get(currentTankIndex).reception));

        ((EditText) findViewById(R.id.startCounter)).setText(String.format(Locale.ROOT, "%.3f", Tank.startCounter));
        ((EditText) findViewById(R.id.endCounter)).setText(String.format(Locale.ROOT, "%.3f", Tank.endCounter));

        ((TextView)findViewById(R.id.receptionByCounter)).setText(String.format(Locale.ROOT, "%.3f", Tank.endCounter - Tank.startCounter));
        ((TextView)findViewById(R.id.together)).setText(String.format(Locale.ROOT, "%.3f", Tank.together));

        ((TextView)findViewById(R.id.difference)).setText(String.format(Locale.ROOT, "%.3f", Tank.together - (Tank.endCounter - Tank.startCounter)));

        ((TextView)findViewById(R.id.percents)).setText(String.format(Locale.ROOT, "%.2f", (Tank.together - (Tank.endCounter - Tank.startCounter)) / (Tank.endCounter - Tank.startCounter) * 100) + "%");
    }

    private void updateTimer() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date currentTime = new Date();

        String result = timeFormat.format(currentTime);

        ((TextView) findViewById(R.id.currentData)).setText(result);
    }
}