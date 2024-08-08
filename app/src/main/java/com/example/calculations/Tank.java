package com.example.calculations;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Locale;

public class Tank implements Serializable {

    public String name;
    private static String defaultName = "резервуар";

    public double startLevel = 0;
    public double endLevel = 0;

    public double startVolume = 0;
    public double endVolume = 0;

    public double startWater = 0;
    public double endWater = 0;

    public double startVolumeWithoutWater;
    public double endVolumeWithoutWater;

    public double startFreeDensityCounter = 0;
    public double startFreeDensityCounterValue = 0;

    public double endFreeDensityCounter = 0;
    public double endFreeDensityCounterValue = 0;

    public double start15DensityCounterValue = 0;
    public double end15DensityCounterValue = 0;

    public double start20DensityCounterValue = 0;
    public double end20DensityCounterValue = 0;

    public double startTons;
    public double endTons;
    public double reception;

    public static double together;

    public static double startCounter = 0;
    public static double endCounter = 0;

    public Tank() {
        this.name = defaultName;
    }

    public void setupInstances() {
        this.startVolumeWithoutWater = this.startVolume - this.startWater;
        this.endVolumeWithoutWater = this.endVolume - this.endWater;

        this.startTons = (this.startVolumeWithoutWater * this.startFreeDensityCounterValue) / 1000;
        this.endTons = (this.endVolumeWithoutWater * this.endFreeDensityCounterValue) / 1000;

        this.reception = Double.valueOf(String.format(Locale.ROOT, "%.3f", this.endTons)) - Double.valueOf(String.format(Locale.ROOT, "%.3f", this.startTons));
    }

    public void resetInstances() {
        this.name = defaultName;

        this.startLevel = 0;
        this.endLevel = 0;

        this.startVolume = 0;
        this.endVolume = 0;

        this.startWater = 0;
        this.endWater = 0;

        this.startFreeDensityCounter = 0;
        this.startFreeDensityCounterValue = 0;

        this.endFreeDensityCounter = 0;
        this.endFreeDensityCounterValue = 0;

        this.start15DensityCounterValue = 0;
        this.end15DensityCounterValue = 0;

        this.start20DensityCounterValue = 0;
        this.end20DensityCounterValue = 0;

        startCounter = 0;
        endCounter = 0;
    }
}
