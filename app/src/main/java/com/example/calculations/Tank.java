package com.example.calculations;

public class Tank {

    public String name;
    private static String defaultName = "резервуар";

    public float startLevel = 0;
    public float endLevel = 0;

    public float startVolume = 0;
    public float endVolume = 0;

    public float startWater = 0;
    public float endWater = 0;

    public float startVolumeWithoutWater;
    public float endVolumeWithoutWater;

    public float startFreeDensityCounter = 0;
    public float startFreeDensityCounterValue = 0;

    public float endFreeDensityCounter = 0;
    public float endFreeDensityCounterValue = 0;

    public float start15DensityCounterValue = 0;
    public float end15DensityCounterValue = 0;

    public float start20DensityCounterValue = 0;
    public float end20DensityCounterValue = 0;

    public float startTons;
    public float endTons;
    public float reception;

    public static float together;

    public static float startCounter = 0;
    public static float endCounter = 0;

    public Tank() {
        this.name = defaultName;
    }

    public void setupInstances() {
        this.startVolumeWithoutWater = this.startVolume - this.startWater;
        this.endVolumeWithoutWater = this.endVolume - this.endWater;

        this.startTons = this.startVolumeWithoutWater * this.startFreeDensityCounterValue / 1000;
        this.endTons = this.endVolumeWithoutWater * this.endFreeDensityCounterValue / 1000;

        this.reception = this.endTons - this.startTons;
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
