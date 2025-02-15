package com.fnac3.deluxe.core.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.enemy.*;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.state.Menu;
import com.fnac3.deluxe.core.state.StateManager;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageHandler {

    public static Map<String, Texture> images = new HashMap<>();
    private static final Map<String, Pixmap> pixmaps = new HashMap<>();
    private static final Queue<String> queue = new LinkedList<>();
    private static ExecutorService serviceLoader;
    private static boolean terminated;
    public static int currentPercent;
    private static int otherCurrentPercent;
    public static int maxPercent;
    public static boolean loading;
    public static boolean doneLoading;
    private static long time;

    public static void load(){
        if (!loading && !doneLoading){
            terminated = false;
            time = System.currentTimeMillis();
            currentPercent = 0;
            otherCurrentPercent = 0;
            pixmaps.clear();
            serviceLoader = Executors.newFixedThreadPool(3);
            queue.forEach(key -> serviceLoader.execute(() -> {
                var pixmap = loadImageBuffer(key);
                pixmaps.put(key, pixmap);
                currentPercent++;
            }));
            serviceLoader.shutdown();
            loading = true;
        } else {
            if (serviceLoader.isTerminated()) {
                terminated = true;
            }
            if (!terminated) return;
            for (String key : queue) {
                if (!pixmaps.containsKey(key) || images.containsKey(key)) continue;
                Pixmap pixmap = pixmaps.get(key);
                Texture texture = new Texture(pixmap);
                pixmap.dispose();
                images.put(key, texture);
                otherCurrentPercent++;
                if (otherCurrentPercent != maxPercent) continue;
                loading = false;
                System.out.println(System.currentTimeMillis() - time + "ms");
                queue.clear();
                pixmaps.clear();
                doneLoading = true;
                break;
            }
        }
    }

    public static Pixmap loadImageBuffer(String path){
        path = "assets/" + path + ".png";
        int[] width = new int[1];
        int[] height = new int[1];
        ByteBuffer bytes = STBImage.stbi_load(path, width, height, new int[1], 0);
        if (bytes == null) throw new RuntimeException("Image not loaded: " + path);
        Pixmap pixmap = new Pixmap(width[0], height[0], Pixmap.Format.RGBA8888);
        pixmap.setPixels(bytes);
        STBImage.stbi_image_free(bytes);
        return pixmap;
    }
    
    public static void add(String path){
        queue.add(path);
        maxPercent++;
        doneLoading = false;
    }

    public static void addImages(Data data, StateManager stateManager){
        ImageHandler.dispose();
        if (stateManager.getState() == StateManager.State.LOADING) {
            roomLoad();
            Game.loadPixmaps();
            add("game/Buttons/TapePlayer");
            add("game/Buttons/TapePlayerBack");
            add("game/Buttons/UnderBed");
            add("game/Buttons/UnderBedBack");

            for (int i = 1; i <= 8; i++){
                add("Static/GameoverStatic" + i);
            }

            for (int i = 1; i <= 4; i++){
                add("Clock/Clock" + i);
            }

            for (int i = 0; i <= 6; i++){
                add("game/time/" + (i == 0 ? 12: i) + "AM");
            }

            add("game/Flashlight");

            switch (Menu.nightType){
                case 0 -> customNightLoad();
                case 1 -> deepscape(data);
                case 2 -> monstergamiLoad();
            }
        } else if (stateManager.getState() == StateManager.State.MENU){
            menuLoad();
        }
    }

    private static void menuLoad(){
        for (int i = 1; i <= 8; i++){
            add("Static/Static" + i);
            add("Static/GameoverStatic" + i);
        }
        for (int i = 1; i <= 32; i++){
            add("menu/candy/monster/" + i);
            add("menu/candy/shadow/" + i);
        }
        add("menu/challenge_off");
        add("menu/challenge_on");
        add("menu/ready");
        add("menu/star");
        add("menu/ai_box_off");
        add("menu/ai_box_on");
        add("menu/ai_box");
        add("menu/info");
        add("menu/help");
        add("menu/nightArrow");

        add("menu/rat");
        add("menu/cat");
        add("menu/vinnie");
        add("menu/shadowrat");
        add("menu/shadowcat");
        add("menu/shadowvinnie");

        //old ui
        add("menu/old/ai_box");
        add("menu/old/ai_box_off");
        add("menu/old/ai_box_on");
        add("menu/old/ready");

        add("menu/old/nightModes");
        add("menu/old/challenges");
        add("menu/old/settings");
        add("menu/old/options");

        for (int i = 1; i <= 6; i++){
            add("menu/old/mode" + i);
        }

        add("menu/old/laserPointer");
        add("menu/old/hardCassette");
        add("menu/old/faultyFlashlight");
        add("menu/old/monstergami");
        add("menu/old/twitchyCat");

        add("menu/old/vsync");
        add("menu/old/oldMenu");
        add("menu/old/nightMusic");
        add("menu/old/menuMusic");

        add("menu/old/flash");
        add("menu/old/hitbox");
        add("menu/old/freeScroll");
        add("menu/old/expandedPointer");

        add("menu/old/enableAll");
        add("menu/old/disableAll");
    }

    public static void roomLoad(){
        String prefix = "game/room/";
        add(prefix + "FullRoom");
        add(prefix + "FullRoomEffect");
        add(prefix + "FullRoomBed");
        add(prefix + "UnderBed");
        add(prefix + "UnderBedEffect");

        String[] movingImages = new String[]{"Turn Around", "Turn Back", "Under Bed"};
        for (String move: movingImages){
            for (int i = 1; i <= 11; i++){
                if (i == 11 && move.equals("Under Bed")) break;
                add(prefix + "Moving/" + move + "/Moving" + i);
            }
        }

        prefix += "Tape/";

        add(prefix + "Tape");
        add(prefix + "TapeMissing");
        add(prefix + "battery");

        prefix += "Buttons/";

        add(prefix + "Play1");
        add(prefix + "Play2");
        add(prefix + "Play3");

        add(prefix + "Rewind1");
        add(prefix + "Rewind2");
        add(prefix + "Rewind3");

        add(prefix + "StopPlay1");
        add(prefix + "StopRewind1");
        add(prefix + "Stop2");
        add(prefix + "Stop3");
    }

    public static void deepscape(Data data){
        add("game/ShadowBattleOverlay");

        String prefix = "game/Shadow Rat/";
        if (ShadowRat.active){
            triangleAttackLoad(prefix);

            for (int i = 1; i <= 6; i++) {
                add(prefix + "Jumpscare/Jumpscare" + i);
            }

            doorAssetsLoad(prefix);
            tapeAssetsLoad(prefix, 12);
            bedAssetsLoad(prefix);

            add("game/gameover/shadowRat");
        }

        prefix = "game/Shadow Cat/New/";
        if (ShadowCat.active){
            String[] sidesList = new String[]{"Left", "Middle", "Right"};
            for (String side: sidesList) {
                add(prefix + "Battle/" + side + "/Left1");
                add(prefix + "Battle/" + side + "/Left2");
                add(prefix + "Battle/" + side + "/Right1");
                add(prefix + "Battle/" + side + "/Right2");
                add(prefix + "Battle/" + side + "/Up1");
                add(prefix + "Battle/" + side + "/Up2");
                add(prefix + "Battle/" + side + "/Down1");
                add(prefix + "Battle/" + side + "/Down2");
                add(prefix + "Battle/" + side + "/MoveDownLeft1");
                add(prefix + "Battle/" + side + "/MoveDownLeft2");
                add(prefix + "Battle/" + side + "/MoveDownRight1");
                add(prefix + "Battle/" + side + "/MoveDownRight2");
                add(prefix + "Battle/" + side + "/MoveLeftRight1");
                add(prefix + "Battle/" + side + "/MoveLeftRight2");
                add(prefix + "Battle/" + side + "/MoveUpLeft1");
                add(prefix + "Battle/" + side + "/MoveUpLeft2");
                add(prefix + "Battle/" + side + "/MoveUpRight1");
                add(prefix + "Battle/" + side + "/MoveUpRight2");
            }


            for (int i = 1; i <= 2; i++) {
                add(prefix + "Battle/Second Middle/Left" + i);
                add(prefix + "Battle/Second Middle/Right" + i);
                add(prefix + "Battle/Second Middle/Up" + i);
                add(prefix + "Battle/Second Middle/MoveDownLeft" + i);
                add(prefix + "Battle/Second Middle/MoveDownRight" + i);
                add(prefix + "Battle/Second Middle/MoveLeftRight" + i);
                add(prefix + "Under Bed/Bed" + i);
            }

            for (int i = 1; i <= 6; i++) {
                add(prefix + "Jumpscare/Jumpscare" + i);
            }

            add(prefix + "Retreat/Retreat Left/Shaking1");
            add(prefix + "Retreat/Retreat Left/Shaking2");
            add(prefix + "Retreat/Retreat Right/Shaking1");
            add(prefix + "Retreat/Retreat Right/Shaking2");

            for (int i = 1; i <= 20; i++) {
                add(prefix + "Retreat/Retreat Left/Retreat" + i);
                add(prefix + "Retreat/Retreat Right/Retreat" + i);
            }

            tapeAssetsLoad(prefix, 12);
            add("game/gameover/shadowCat");
        }
        if (ShadowVinnie.ai != 0){
            prefix = "game/Shadow Vinnie/";

            for (int i = 0; i < 48; i++){
                add(prefix + "Battle/Left/" + i);
                add(prefix + "Battle/Middle/" + i);
                add(prefix + "Battle/Right/" + i);
            }
            for (int i = 1; i <= 21; i++) {
                add(prefix + "Battle/Jump/Left/" + i);
            }

            for (int i = 1; i <= 19; i++) {
                add(prefix + "Battle/Jump/Right/" + i);
            }

            for (int i = 1; i <= 6; i++) {
                add(prefix + "Jumpscare/Jumpscare" + i);
            }

            bedAssetsLoad(prefix);
            tapeAssetsLoad(prefix, 14);
            for (int i = 1; i <= 13; i++) {
                add(prefix + "Looking Away/Left Look Away/LookAway" + i);
                add(prefix + "Looking Away/Middle Look Away/LookAway" + i);
                add(prefix + "Looking Away/Right Look Away/LookAway" + i);
            }

            add("game/gameover/shadowVinnie");
        }

        monstergamiLoad();
    }

    public static void customNightLoad() {
        add("game/RatCatBattleOverlay");
        add("game/VinnieBattleOverlay");

        String prefix = "game/Rat/";
        if (Rat.ai != 0) {
            triangleAttackLoad(prefix);

            for (int i = 1; i <= 35; i++) {
                add(prefix + "Jumpscare/Room Jumpscare/Jumpscare" + i);
            }

            for (int i = 1; i <= 28; i++) {
                add(prefix + "Jumpscare/Bed Jumpscare/Jumpscare" + i);
            }

            doorAssetsLoad(prefix);
            tapeAssetsLoad(prefix, 12);
            bedAssetsLoad(prefix);
            add("game/gameover/rat1");
            add("game/gameover/rat2");
        }

        prefix = "game/Cat/";
        if (Cat.ai != 0) {
            triangleAttackLoad(prefix);

            for (int i = 1; i <= 35; i++) {
                add(prefix + "Jumpscare/Room Jumpscare/Jumpscare" + i);
            }

            for (int i = 1; i <= 29; i++) {
                add(prefix + "Jumpscare/Bed Jumpscare/Jumpscare" + i);
            }

            for (int i = 1; i <= 58; i++) {
                add(prefix + "Retreat/Retreat Left/Retreat" + i);
                add(prefix + "Retreat/Retreat Right/Retreat" + i);
            }

            tapeAssetsLoad(prefix, 12);
            bedAssetsLoad(prefix);

            add("game/gameover/cat1");
            add("game/gameover/cat2");
        }

        prefix = "game/Vinnie/";
        if (Vinnie.ai != 0) {

            String[] sidesList = new String[]{"Left", "Middle", "Right"};
            for (String side : sidesList) {
                add(prefix + "Battle/" + side + "/DownLeft1");
                add(prefix + "Battle/" + side + "/DownLeft2");
                add(prefix + "Battle/" + side + "/DownRight1");
                add(prefix + "Battle/" + side + "/DownRight2");
                add(prefix + "Battle/" + side + "/UpLeft1");
                add(prefix + "Battle/" + side + "/UpLeft2");
                add(prefix + "Battle/" + side + "/UpRight1");
                add(prefix + "Battle/" + side + "/UpRight2");
                add(prefix + "Battle/" + side + "/Up1");
                add(prefix + "Battle/" + side + "/Up2");
                add(prefix + "Battle/" + side + "/Down1");
                add(prefix + "Battle/" + side + "/Down2");
                add(prefix + "Battle/" + side + "/MoveDownLeft");
                add(prefix + "Battle/" + side + "/MoveDownRight");
                add(prefix + "Battle/" + side + "/MoveUpLeft");
                add(prefix + "Battle/" + side + "/MoveUpRight");
                add(prefix + "Battle/" + side + "/MoveLeft");
                add(prefix + "Battle/" + side + "/MoveRight");
            }

            for (int i = 1; i <= 35; i++) {
                add(prefix + "Jumpscare/Room Jumpscare/Jumpscare" + i);
            }

            for (int i = 1; i <= 29; i++) {
                add(prefix + "Jumpscare/Bed Jumpscare/Jumpscare" + i);
            }

            for (int i = 1; i <= 21; i++) {
                add(prefix + "Battle/Jump Right/Jump" + i);
            }

            for (int i = 1; i <= 19; i++) {
                add(prefix + "Battle/Jump Left/Jump" + i);
            }

            doorAssetsLoad(prefix);
            tapeAssetsLoad(prefix, 14);
            bedAssetsLoad(prefix);
            add("game/gameover/vinnie1");
            add("game/gameover/vinnie2");
        }

        monstergamiLoad();
    }

    public static void monstergamiLoad(){
        add("game/MonstergamiBattleOverlay");

        String prefix = "game/Monstergami/";
        if (Monstergami.active){
            String[] sidesList = new String[]{"Left", "Middle", "Right"};
            for (String side : sidesList) {
                for (int i = 1; i <= 2; i++) {
                    add(prefix + "Battle/" + side + "/Up" + i);
                    add(prefix + "Battle/" + side + "/DownLeft" + i);
                    add(prefix + "Battle/" + side + "/DownRight" + i);
                    add(prefix + "Battle/" + side + "/UpLeft" + i);
                    add(prefix + "Battle/" + side + "/UpRight" + i);
                    add(prefix + "Battle/" + side + "/MoveUpToDownLeft" + i);
                    add(prefix + "Battle/" + side + "/MoveDownLeftToUpRight" + i);
                    add(prefix + "Battle/" + side + "/MoveUpRightToUpLeft" + i);
                    add(prefix + "Battle/" + side + "/MoveUpLeftToDownRight" + i);
                    add(prefix + "Battle/" + side + "/MoveDownRightToUp" + i);
                }
            }

            add(prefix + "Battle/Bed/Left1");
            add(prefix + "Battle/Bed/Left2");
            add(prefix + "Battle/Bed/MoveLeft1");
            add(prefix + "Battle/Bed/MoveLeft2");
            add(prefix + "Battle/Bed/Middle1");
            add(prefix + "Battle/Bed/Middle2");
            add(prefix + "Battle/Bed/Right1");
            add(prefix + "Battle/Bed/Right2");
            add(prefix + "Battle/Bed/MoveRight1");
            add(prefix + "Battle/Bed/MoveRight2");

            for (int i = 1; i <= 6; i++){
                add(prefix + "Jumpscare/Jumpscare" + i);
            }

            if (Menu.nightType != 2) return;
            for (int i = 1; i <= 10; i++){
                add("game/gameover/monstergami" + i);
            }
        }
    }

    private static void triangleAttackLoad(String prefix){
        String[] sidesList = new String[]{"Left", "Middle", "Right"};
        for (String side: sidesList) {
            for (int i = 1; i <= 2; i++) {
                add(prefix + "Battle/" + side + "/Left" + i);
                add(prefix + "Battle/" + side + "/Right" + i);
                add(prefix + "Battle/" + side + "/Up" + i);
                add(prefix + "Battle/" + side + "/MoveDownLeft" + i);
                add(prefix + "Battle/" + side + "/MoveDownRight" + i);
                add(prefix + "Battle/" + side + "/MoveLeftRight" + i);
            }
        }
    }

    private static void doorAssetsLoad(String prefix){
        for (int i = 1; i <= 13; i++) {
            add(prefix + "Looking Away/Left Look Away/LookAway" + i);
            add(prefix + "Looking Away/Middle Look Away/LookAway" + i);
            add(prefix + "Looking Away/Right Look Away/LookAway" + i);
        }

        for (int i = 1; i <= 18; i++) {
            add(prefix + "Leaving/Left/Leaving" + i);
            add(prefix + "Leaving/Right/Leaving" + i);
        }
    }

    private static void bedAssetsLoad(String prefix){
        for (int i = 1; i <= 2; i++) {
            add(prefix + "Under Bed/Bed" + i);
            add(prefix + "Under Bed/Left/Left" + i);
            add(prefix + "Under Bed/Right/Right" + i);
        }
    }

    private static void tapeAssetsLoad(String prefix, int limit){
        for (int i = 1; i <= limit; i++) {
            add(prefix + "Tape/Leaving" + i);
        }
    }

    public static void dispose(){
        maxPercent = 0;
        images.values().forEach(Texture::dispose);
        images.clear();
    }
}
