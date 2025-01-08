package com.fnac3.deluxe.core.data;

import com.fnac3.deluxe.core.state.Menu;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Data {

    public int RatAI;
    public int CatAI;
    public int VinnieAI;

    public boolean ShadowRatAI;
    public boolean ShadowCatAI;
    public boolean ShadowVinnieAI;

    public Map<String, Star> stars;

    public boolean vSync;
    public boolean oldMenu;
    public boolean nightMusic = true;
    public boolean menuMusic = true;

    public boolean flashDebug;
    public boolean hitboxDebug;
    public boolean freeScroll;
    public boolean expandedPointer;

    public boolean laserPointer;
    public boolean hardCassette;
    public boolean faultyFlashlight;
    public boolean challenge4;

    public boolean allChallenges;

    public float[] starAlphas;
    private static final float[] zeroChallengeStarColor = new float[]{1, 0, 0};
    private static final float[] oneChallengeStarColor = new float[]{0.75f, 0, 1};
    private static final float[] twoChallengesStarColor = new float[]{0, 0.15f, 1};
    private static final float[] threeChallengesStarColor = new float[]{0, 0.9f, 1};
    private static final float[] allChallengesStarColor = new float[]{1, 0.75f, 0};

    public SaveData saveData;

    //file names
    private static final String externalName = "AppData/Roaming/Five Nights at Candy's 3 Deluxe/Save";
    static final String dirName = System.getProperty("user.home") + "/" + externalName;
    private static final String fileName = dirName + "/Game_v2.save";
    private static final String configFileName = dirName + "/Config_v2.save";
    private File configFile;

    public Data(){
        starAlphas = new float[6];
        saveData = new SaveData();
        readSaveFile();

        stars = new HashMap<>();
        createStar("Rat & Cat", 0);
        createStar("Final Night", 0);
        createStar("Stalling Duo", 0);
        createStar("Play Date", 0);
        createStar("Monster Fever", 0);
        createStar("Theater Trauma", 0);
        createStar("Custom Night", 0);
        createStar("The Deepscape", 1);

        readConfigFile();
    }

    public String modeDescriptions(String mode){
        String extra;
        extra = Math.max(0, saveData.modes.get(mode).beatType - 1) + "/4 Challenges";
        if (saveData.modes.get(mode).beatType == 0) {
            return "Not Completed | " + extra;
        }
        return "Completed | " + extra;
    }

    private void createStar(String modeName, int nightType){
        SaveData.Mode mode = saveData.modes.get(modeName);
        switch (mode.beatType){
            case 0:
                stars.put(modeName, new Star(nightType, 0.5f, 0.5f, 0.5f));
                break;
            case 1:
                stars.put(modeName, new Star(nightType, zeroChallengeStarColor));
                break;
            case 2:
                stars.put(modeName, new Star(nightType, oneChallengeStarColor));
                break;
            case 3:
                stars.put(modeName, new Star(nightType, twoChallengesStarColor));
                break;
            case 4:
                stars.put(modeName, new Star(nightType, threeChallengesStarColor));
                break;
            case 5:
                stars.put(modeName, new Star(nightType, allChallengesStarColor));
                break;
        }
    }

    public void changeStar(String modeName){
        SaveData.Mode mode = saveData.modes.get(modeName);
        switch (mode.beatType){
            case 0:
                stars.get(modeName).color[0] = 0.5f;
                stars.get(modeName).color[1] = 0.5f;
                stars.get(modeName).color[2] = 0.5f;
                break;
            case 1:
                stars.get(modeName).color = zeroChallengeStarColor;
                break;
            case 2:
                stars.get(modeName).color = oneChallengeStarColor;
                break;
            case 3:
                stars.get(modeName).color = twoChallengesStarColor;
                break;
            case 4:
                stars.get(modeName).color = threeChallengesStarColor;
                break;
            case 5:
                stars.get(modeName).color = allChallengesStarColor;
                break;
        }
    }

    private void createMode(String modeName){
        SaveData.Mode mode = saveData.new Mode();
        saveData.modes.put(modeName, mode);
    }

    private void readSaveFile(){
        File file = new File(dirName);
        file.mkdirs();

        file = new File(fileName);

        if (!file.exists()) {
            writeNewSaveFile(file);
        }
        readModes();
        if (saveData.modes == null) {
            writeNewSaveFile(file);
        }
        writeModes();
    }

    private void writeNewSaveFile(File file) {
        saveData.modes = new HashMap<>();
        createMode("Rat & Cat");
        createMode("Final Night");
        createMode("Stalling Duo");
        createMode("Play Date");
        createMode("Monster Fever");
        createMode("Theater Trauma");
        createMode("Custom Night");
        createMode("The Deepscape");

        try {
            file.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readConfigFile(){
        File file = new File(dirName);
        file.mkdirs();

        configFile = new File(configFileName);

        if (!configFile.exists()) {
            writeNewConfigFile();
        } else {
            readConfig();
        }
    }

    private void writeNewConfigFile() {
        try {
            configFile.createNewFile();
            writeConfig();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readConfig(){
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            br.readLine();
            RatAI = readIntLineConfig(br);
            CatAI = readIntLineConfig(br);
            VinnieAI = readIntLineConfig(br);

            ShadowRatAI = readBooleanLineConfig(br);
            ShadowCatAI = readBooleanLineConfig(br);
            ShadowVinnieAI = readBooleanLineConfig(br);

            Menu.nightType = readIntLineConfig(br);
            Menu.previousNightType = Menu.nightType;
            Menu.oldMenuContext = readIntLineConfig(br);

            vSync = readBooleanLineConfig(br);
            oldMenu = readBooleanLineConfig(br);
            nightMusic = readBooleanLineConfig(br);
            menuMusic = readBooleanLineConfig(br);

            flashDebug = readBooleanLineConfig(br);
            hitboxDebug = readBooleanLineConfig(br);
            freeScroll = readBooleanLineConfig(br);
            expandedPointer = readBooleanLineConfig(br);

            laserPointer = readBooleanLineConfig(br);
            hardCassette = readBooleanLineConfig(br);
            faultyFlashlight = readBooleanLineConfig(br);
            challenge4 = readBooleanLineConfig(br);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean readBooleanLineConfig(BufferedReader br) throws IOException {
        String line = br.readLine();
        return Boolean.parseBoolean(line.substring(line.indexOf('=') + 2));
    }

    private int readIntLineConfig(BufferedReader br) throws IOException {
        String line = br.readLine();
        return Integer.parseInt(line.substring(line.indexOf('=') + 2));
    }

    public void writeConfig(){
        StringBuilder sb = new StringBuilder();
        sb.append("[Config]\n")
                .append("Rat AI = ").append(RatAI).append("\n")
                .append("Cat AI = ").append(CatAI).append("\n")
                .append("Vinnie AI = ").append(VinnieAI).append("\n")
                .append("Shadow Rat AI = ").append(ShadowRatAI).append("\n")
                .append("Shadow Cat AI = ").append(ShadowCatAI).append("\n")
                .append("Shadow Vinnie AI = ").append(ShadowVinnieAI).append("\n")
                .append("Menu Night Type = ").append(Menu.nightType).append("\n")
                .append("Old Menu Context = ").append(Menu.oldMenuContext).append("\n")
                .append("vSync = ").append(vSync).append("\n")
                .append("oldMenu = ").append(oldMenu).append("\n")
                .append("nightMusic = ").append(nightMusic).append("\n")
                .append("menuMusic = ").append(menuMusic).append("\n")
                .append("flashDebug = ").append(flashDebug).append("\n")
                .append("hitboxDebug = ").append(hitboxDebug).append("\n")
                .append("freeScroll = ").append(freeScroll).append("\n")
                .append("expandedPointer = ").append(expandedPointer).append("\n")
                .append("laserPointer = ").append(laserPointer).append("\n")
                .append("hardCassette = ").append(hardCassette).append("\n")
                .append("faultyFlashlight = ").append(faultyFlashlight).append("\n")
                .append("challenge4 = ").append(challenge4).append("\n");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile))){
            bufferedWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeWin(String mode, boolean notCheating){
        if (notCheating){
            mode(saveData.modes.get(mode));
            changeStar(mode);
        }
        writeModes();
    }

    public void writeWinAndTime(String mode, boolean notCheating, String time){
        if (notCheating){
            mode(saveData.modes.get(mode));
            changeStar(mode);
            if (time != null) saveData.modes.get(mode).time = time;
        }
        writeModes();
    }

    private void mode(SaveData.Mode mode){
        boolean[] arr = new boolean[]{true, !expandedPointer && laserPointer, hardCassette, faultyFlashlight, challenge4};
        int numberOfChallenges = 0;

        for (boolean b : arr) {
            if (b) {
                numberOfChallenges++;
            }
        }

        mode.beatType = Math.max(numberOfChallenges, mode.beatType);
    }

    private void readModes(){
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)){
             saveData = (SaveData) ois.readObject();
        } catch (Exception e){
            saveData.modes = null;
        }
    }

    private void writeModes(){
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(saveData);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
