package com.fnac3.deluxe.core.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.FNaC3Deluxe;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.data.Star;
import com.fnac3.deluxe.core.data.TextString;
import com.fnac3.deluxe.core.discord.Discord;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {

    private static FrameBuffer screenBuffer;
    public static Pixmap starPixmap;

    private static float shadowRatButtonAlpha;
    private static float shadowCatButtonAlpha;
    private static float shadowVinnieButtonAlpha;
    private static float shadowRatAlpha;
    private static float shadowCatAlpha;
    private static float shadowVinnieAlpha;

    private static float ratButtonAlpha;
    private static float catButtonAlpha;
    private static float vinnieButtonAlpha;
    private static float ratAlpha;
    private static float catAlpha;
    private static float vinnieAlpha;
    private static float whiteAlpha;

    private static float candyAlpha;
    private static float candyFrame;

    public static boolean ratButtonHovered;
    public static boolean catButtonHovered;
    public static boolean vinnieButtonHovered;

    private static int ratAIWidth;
    private static int catAIWidth;
    private static int vinnieAIWidth;

    private static float blackFade;
    private static boolean writeConfig;
    private static float readyAlpha;
    public static float staticScreen;
    public static boolean loaded;
    public static boolean renderReady;
    public static boolean loading;
    public static boolean fontsLoaded;
    private static String mode;
    public static String modeName;
    public static boolean modeSwitch;
    private static boolean leftArrow;
    private static boolean rightArrow;
    private static int leftArrowPosition;
    private static int rightArrowPosition;
    private static GlyphLayout layoutModeTemp;

    public static int nightType;
    public static int previousNightType;

    private static String textToRender;
    private static int previousTextCase;
    private static int textCase;
    private static int previousEnemyCase;
    private static int enemyCase;
    private static StringBuilder textBuilder;
    private static Map<String, String> textValues;
    private static List<String> textRenderList;
    private static Map<String, Integer> captionLayoutWidthInformation;
    private static int captionWidth;
    private static float textValue;

    private static float charactersX;
    private static float charactersY;

    private static boolean challenge1;
    private static boolean challenge2;
    private static boolean challenge3;
    private static boolean challenge4;
    private static boolean allChallenges;

    private static boolean vSync;
    private static boolean oldMenu;
    private static boolean nightMusic;
    private static boolean menuMusic;
    private static String menuMusicName;

    private static float oldMenuAlpha;
    public static int oldMenuContext;
    private static float context0Alpha;
    private static float context1Alpha;
    private static float context2Alpha;
    private static float context3Alpha;

    private static float button1Alpha;
    private static float button2Alpha;
    private static float button3Alpha;
    private static float button4Alpha;
    private static float button5Alpha;
    private static float button6Alpha;
    private static float button7Alpha;
    private static float button8Alpha;
    private static float button9Alpha;
    private static float button10Alpha;

    private static boolean mode1;
    private static boolean mode2;
    private static boolean mode3;
    private static boolean mode4;
    private static boolean mode5;
    private static boolean mode6;
    private static boolean enableAll;
    private static boolean disableAll;
    private static boolean nightModes;
    private static boolean challenges;
    private static boolean settings;
    private static boolean options;

    private static boolean flashDebug;
    private static boolean hitboxDebug;
    private static boolean freeScroll;
    private static boolean expandedPointer;

    private static float pitchTarget;
    private static float volume;
    private static boolean arrowHovered;

    private static BitmapFont menuFontHeading;
    private static BitmapFont menuFontTitle;
    private static BitmapFont menuFontLabel;
    private static BitmapFont menuFontCaption;

    private static boolean playDeluxe;

    public static boolean mouseOver(float mx, float my, float x1, float y1, float x2, float y2){
        return mx >= x1 && mx <= x2 && my >= y1 && my <= y2;
    }

    public static void input(float mx, float my, StateManager stateManager, Data data, AudioClass audioClass){
        if (!loaded) return;

        boolean leftPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        charactersX = (512 - mx) / 20;
        charactersY = (384 - my) / 20;

        mode1 = false;
        mode2 = false;
        mode3 = false;
        mode4 = false;
        mode5 = false;
        mode6 = false;
        enableAll = false;
        disableAll = false;
        allChallenges = false;
        writeConfig = false;

        if (!data.oldMenu) {
            int yPos = 666;
            int xPos = 806;
            vSync = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            oldMenu = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            nightMusic = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            menuMusic = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);

            yPos -= 82;
            flashDebug = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            hitboxDebug = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            freeScroll = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            expandedPointer = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);

            yPos -= 82;
            challenge1 = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            challenge2 = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            challenge3 = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            challenge4 = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
            yPos -= 36;
            allChallenges = mouseOver(mx, my, xPos, yPos, xPos + 32, yPos + 32);
        } else {
            int yPos = 696;

            mode1 = oldButtonCondition(mx, my, yPos, 0);
            challenge1 = oldButtonCondition(mx, my, yPos, 1);
            vSync = oldButtonCondition(mx, my, yPos, 2);
            flashDebug = oldButtonCondition(mx, my, yPos, 3);

            yPos -= 64;
            mode2 = oldButtonCondition(mx, my, yPos, 0);
            challenge2 = oldButtonCondition(mx, my, yPos, 1);
            oldMenu = oldButtonCondition(mx, my, yPos, 2);
            hitboxDebug = oldButtonCondition(mx, my, yPos, 3);

            yPos -= 64;
            mode3 = oldButtonCondition(mx, my, yPos, 0);
            challenge3 = oldButtonCondition(mx, my, yPos, 1);
            nightMusic = oldButtonCondition(mx, my, yPos, 2);
            freeScroll = oldButtonCondition(mx, my, yPos, 3);

            yPos -= 64;
            mode4 = oldButtonCondition(mx, my, yPos, 0);
            challenge4 = oldButtonCondition(mx, my, yPos, 1);
            menuMusic = oldButtonCondition(mx, my, yPos, 2);
            expandedPointer = oldButtonCondition(mx, my, yPos, 3);

            yPos -= 64;
            mode5 = oldButtonCondition(mx, my, yPos, 0);
            enableAll = oldButtonCondition(mx, my, yPos);

            yPos -= 64;
            mode6 = oldButtonCondition(mx, my, yPos, 0);
            disableAll = oldButtonCondition(mx, my, yPos);

            yPos -= 64;
            nightModes = mouseOver(mx, my, 812, yPos, 1008, yPos + 56);
            yPos -= 64;
            challenges = mouseOver(mx, my, 812, yPos, 1008, yPos + 56);
            yPos -= 64;
            settings = mouseOver(mx, my, 812, yPos, 1008, yPos + 56);
            yPos -= 64;
            options = mouseOver(mx, my, 812, yPos, 1008, yPos + 56);
        }

        textCase = 0;
        enemyCase = 0;

        if (oldMenu){
            textModifier(TextString.oldMenuText);
            textCase = 14;
        } else if (nightModes && nightType == 1) {
            textModifier("Night Modes unavailable in The Deepscape.");
            textCase = 15;
            nightModes = false;
        } else if (freeScroll){
            textModifier(TextString.freeScrollText);
            textCase = 1;
        } else if (flashDebug){
            textModifier(TextString.flashHPText);
            textCase = 2;
        } else if (hitboxDebug){
            textModifier(TextString.hitboxText);
            textCase = 3;
        } else if (expandedPointer){
            textModifier(TextString.expandedPointerText);
            textCase = 4;
        } else if (challenge1){
            textModifier(TextString.laserPointerText);
            textCase = 5;
        } else if (challenge2){
            textModifier(TextString.hardCassetteText);
            textCase = 6;
        } else if (challenge3){
            textModifier(TextString.faultyFlashlight);
            textCase = 7;
        } else if (challenge4){
            if (nightType == 0) textModifier(TextString.monstergami);
            else textModifier(TextString.twitchyCatText);
            textCase = 8;
        } else if (allChallenges){
            textModifier(TextString.allChallengesText);
            textCase = 9;
        }

        if ((!data.oldMenu && mouseOver(mx, my, 806, 18, 806 + 196, 18 + 99))
                || mouseOver(mx, my, 812, 16, 1008, 112)){
            if (readyAlpha < 0.25f){
                readyAlpha += Gdx.graphics.getDeltaTime() * 2;
                if (readyAlpha > 0.25f){
                    readyAlpha = 0.25f;
                }
            }
            if (leftPressed){
                stateManager.setState(StateManager.State.LOADING);
                loaded = false;
            }
        } else {
            if (readyAlpha > 0){
                readyAlpha -= Gdx.graphics.getDeltaTime() * 2;
                if (readyAlpha < 0){
                    readyAlpha = 0;
                }
            }
        }

        if (nightType == 0){
            ratButtonHovered = mouseOver(mx, my,
                    132 + charactersX, 278 + charactersY,
                    132 + 148 + charactersX, 278 + 74 + charactersY);
            ratButtonAlpha = nightButtonAlpha(ratButtonHovered, data.RatAI != 0, ratButtonAlpha);
            ratAlpha = nightCharacterAlpha(data.RatAI != 0, ratAlpha);
            data.RatAI = nightCustomNightAI(audioClass, ratButtonHovered, data.RatAI, 1, TextString.ratText);

            catButtonHovered = mouseOver(mx, my,
                    347 + charactersX, 278 + charactersY,
                    347 + 148 + charactersX, 278 + 74 + charactersY);
            catButtonAlpha = nightButtonAlpha(catButtonHovered, data.CatAI != 0, catButtonAlpha);
            catAlpha = nightCharacterAlpha(data.CatAI != 0, catAlpha);
            data.CatAI = nightCustomNightAI(audioClass, catButtonHovered, data.CatAI, 2, TextString.catText);

            vinnieButtonHovered = mouseOver(mx, my,
                    562 + charactersX, 278 + charactersY,
                    562 + 148 + charactersX, 278 + 74 + charactersY);
            vinnieButtonAlpha = nightButtonAlpha(vinnieButtonHovered, data.VinnieAI != 0, vinnieButtonAlpha);
            vinnieAlpha = nightCharacterAlpha(data.VinnieAI != 0, vinnieAlpha);
            data.VinnieAI = nightCustomNightAI(audioClass, vinnieButtonHovered, data.VinnieAI, 3, TextString.vinnieText);

        } else if (nightType == 1) {
            boolean condition1 = mouseOver(mx, my,
                    148 + charactersX, 278 + charactersY,
                    296 + charactersX, 278 + 74 + charactersY);
            boolean condition2 = data.ShadowRatAI;

            shadowRatButtonAlpha = nightButtonAlpha(condition1, condition2, shadowRatButtonAlpha);
            shadowRatAlpha = nightCharacterAlpha(condition2, shadowRatAlpha);
            data.ShadowRatAI = nightShadowNightAI(audioClass, condition1, data.ShadowRatAI, 4, TextString.shadowRatText);

            condition1 = mouseOver(mx, my,
                    347 + charactersX, 278 + charactersY,
                    347 + 148 + charactersX, 278 + 74 + charactersY);
            condition2 = data.ShadowCatAI;

            shadowCatButtonAlpha = nightButtonAlpha(condition1, condition2, shadowCatButtonAlpha);
            shadowCatAlpha = nightCharacterAlpha(condition2, shadowCatAlpha);
            data.ShadowCatAI = nightShadowNightAI(audioClass, condition1, data.ShadowCatAI, 5, TextString.shadowCatText);

            condition1 = mouseOver(mx, my,
                    562 + charactersX, 278 + charactersY,
                    562 + 148 + charactersX, 278 + 74 + charactersY);
            condition2 = data.ShadowVinnieAI;

            shadowVinnieButtonAlpha = nightButtonAlpha(condition1, condition2, shadowVinnieButtonAlpha);
            shadowVinnieAlpha = nightCharacterAlpha(condition2, shadowVinnieAlpha);
            data.ShadowVinnieAI = nightShadowNightAI(audioClass, condition1, data.ShadowVinnieAI, 6, TextString.shadowVinnieText);
        }

        for (Star star: data.stars.values()){
            star.hovered = false;
        }

        if (starPixmap != null && mouseOver(mx, my, 385, 100, 385 + 96, 100 + 96)) {
            float pixmapY = 768 - my;
            int pixel = starPixmap.getPixel((int) (mx - 385), (int) (pixmapY - 572));
            if (pixel != 0 && modeName != null){
                data.stars.get(modeName).hovered = true;
                if (!modeName.equals("Custom Night") && !modeName.isEmpty()){
                    textModifier(data.modeDescriptions(modeName));
                    textCase = 11;
                }
            }
        }

        if (mouseOver(mx, my, 18, 18, 60, 60)){
            textModifier(TextString.descriptionText);
            textCase = 12;
        } else if (mouseOver(mx, my, 18, 76, 60, 118)){
            textModifier(TextString.controlText);
            textCase = 13;
        }

        if (nightType == 0 && !data.oldMenu){
            leftArrow = mouseOver(mx, my, leftArrowPosition, 54, leftArrowPosition + 24, 54 + 28);
            rightArrow = mouseOver(mx, my, rightArrowPosition, 54, rightArrowPosition + 24, 54 + 28);
        } else {
            leftArrow = false;
            rightArrow = false;
        }

        arrowHovered = mouseOver(mx, my, 260, 712, 280, 740);
        if (leftPressed){
            if (arrowHovered) {
                arrowHovered = false;
                modeSwitch = true;
                nightType++;
                if (nightType == 3) nightType = 0;

                if (nightType != 0 && data.oldMenu && oldMenuContext == 0) oldMenuContext = 1;

                if (nightType == 1) {
                    pitchTarget = 0.75f;
                } else {
                    pitchTarget = 1;
                }

                if (nightType == 0 || nightType == 2) {
                    playDeluxe = true;
                    audioClass.stop(menuMusicName);
                }

                audioClass.play("thunder");
                whiteAlpha = 1;
                writeConfig = true;
            } else if (rightArrow) {
                audioClass.play("select");
                switch (mode) {
                    case "Custom Night":
                    case "Theater Trauma":
                        aiModeChange("Rat & Cat", data);
                        break;
                    case "Rat & Cat":
                        aiModeChange("Final Night", data);
                        break;
                    case "Final Night":
                        aiModeChange("Stalling Duo", data);
                        break;
                    case "Stalling Duo":
                        aiModeChange("Play Date", data);
                        break;
                    case "Play Date":
                        aiModeChange("Monster Fever", data);
                        break;
                    case "Monster Fever":
                        aiModeChange("Theater Trauma", data);
                        break;
                }
                modeSwitch = true;
                writeConfig = true;
            } else if (leftArrow){
                audioClass.play("select");
                switch (mode) {
                    case "Custom Night":
                    case "Rat & Cat":
                        aiModeChange("Theater Trauma", data);
                        break;
                    case "Final Night":
                        aiModeChange("Rat & Cat", data);
                        break;
                    case "Stalling Duo":
                        aiModeChange("Final Night", data);
                        break;
                    case "Play Date":
                        aiModeChange("Stalling Duo", data);
                        break;
                    case "Monster Fever":
                        aiModeChange("Play Date", data);
                        break;
                    case "Theater Trauma":
                        aiModeChange("Monster Fever", data);
                        break;
                }
                modeSwitch = true;
                writeConfig = true;
            } else if (mode1 || mode2 || mode3 || mode4 || mode5 || mode6){
                String value;
                if (mode1) value = "Rat & Cat";
                else if (mode2) value = "Final Night";
                else if (mode3) value = "Stalling Duo";
                else if (mode4) value = "Play Date";
                else if (mode5) value = "Monster Fever";
                else value = "Theater Trauma";

                audioClass.play("select");
                if (mode.equals(value)) aiModeChange("Reset", data);
                else aiModeChange(value, data);
                modeSwitch = true;
                writeConfig = true;
            } else if (enableAll) {
                audioClass.play("select");
                toggleAll(data, true);
                writeConfig = true;
            } else if (disableAll) {
                audioClass.play("select");
                toggleAll(data, false);
                writeConfig = true;
            } else if (nightModes) {
                audioClass.play("select");
                oldMenuContext = 0;
                writeConfig = true;
            } else if (challenges) {
                audioClass.play("select");
                oldMenuContext = 1;
                writeConfig = true;
            } else if (settings) {
                audioClass.play("select");
                oldMenuContext = 2;
                writeConfig = true;
            } else if (options){
                audioClass.play("select");
                oldMenuContext = 3;
                writeConfig = true;
            } else if (vSync) {
                audioClass.play("select");
                data.vSync = !data.vSync;
                Gdx.graphics.setVSync(data.vSync);
                writeConfig = true;
            } else if (oldMenu) {
                audioClass.play("select");
                data.oldMenu = !data.oldMenu;
                oldMenuContext = 2;
                writeConfig = true;
            } else if (nightMusic) {
                audioClass.play("select");
                data.nightMusic = !data.nightMusic;
                writeConfig = true;
            } else if (menuMusic){
                audioClass.play("select");
                data.menuMusic = !data.menuMusic;
                writeConfig = true;
            } else if (flashDebug){
                audioClass.play("select");
                data.flashDebug = !data.flashDebug;
                writeConfig = true;
            } else if (hitboxDebug){
                audioClass.play("select");
                data.hitboxDebug = !data.hitboxDebug;
                writeConfig = true;
            } else if (freeScroll) {
                audioClass.play("select");
                data.freeScroll = !data.freeScroll;
                modeSwitch = true;
                writeConfig = true;
            } else if (expandedPointer){
                audioClass.play("select");
                data.expandedPointer = !data.expandedPointer;
                modeSwitch = true;
                writeConfig = true;
            } else if (challenge1){
                audioClass.play("select");
                data.laserPointer = !data.laserPointer;
                modeSwitch = true;
                writeConfig = true;
            } else if (challenge2){
                audioClass.play("select");
                data.hardCassette = !data.hardCassette;
                modeSwitch = true;
                writeConfig = true;
            } else if (challenge3){
                audioClass.play("select");
                data.faultyFlashlight = !data.faultyFlashlight;
                modeSwitch = true;
                writeConfig = true;
            } else if (challenge4){
                audioClass.play("select");
                data.challenge4 = !data.challenge4;
                modeSwitch = true;
                writeConfig = true;
            } else if (allChallenges){
                audioClass.play("select");
                if (data.allChallenges){
                    data.laserPointer = false;
                    data.hardCassette = false;
                    data.faultyFlashlight = false;
                    data.challenge4 = false;
                } else {
                    data.laserPointer = true;
                    data.hardCassette = true;
                    data.faultyFlashlight = true;
                    data.challenge4 = true;
                }
                modeSwitch = true;
                writeConfig = true;
            }
        }
        if (writeConfig) data.writeConfig();
    }

    public static void update(Camera camera, StateManager stateManager, Data data, AudioClass audioClass){
        float time = Gdx.graphics.getDeltaTime();

        if (data.menuMusic) volume += time * 6;
        else volume -= time * 6;
        volume = volume < 0 ? 0 : volume > 1 ? 1 : volume;

        if (loaded) {
            if (data.oldMenu){
                oldMenuAlpha += time * 4;
                if (oldMenuAlpha > 1) oldMenuAlpha = 1;
            } else {
                oldMenuAlpha -= time * 4;
                if (oldMenuAlpha < 0) oldMenuAlpha = 0;
            }

            if (blackFade < 1){
                blackFade += time * 3;
                if (blackFade > 1) blackFade = 1;
            }

            if (whiteAlpha > 0){
                whiteAlpha -= time * 2.5f;
                if (whiteAlpha < 0 || whiteAlpha > 1) whiteAlpha = 0;
            }

            if (data.challenge4) {
                candyFrame += time * 30;
                if (candyFrame > 31) candyFrame = 31;
            } else {
                candyFrame -= time * 30;
                if (candyFrame < 0) candyFrame = 0;
            }

            candyAlpha = Math.min(10, candyFrame) / 10;

            if (modeSwitch || layoutModeTemp == null){
                if (modeSwitch) {
                    if (nightType == 0) {
                        if (data.RatAI == 20 && data.CatAI == 20 && data.VinnieAI == 0) {
                            mode = "Rat & Cat";
                        } else if (data.RatAI == 0 && data.CatAI == 0 && data.VinnieAI == 20) {
                            mode = "Final Night";
                        } else if (data.RatAI == 20 && data.CatAI == 0 && data.VinnieAI == 20) {
                            mode = "Stalling Duo";
                        } else if (data.RatAI == 10 && data.CatAI == 5 && data.VinnieAI == 10) {
                            mode = "Play Date";
                        } else if (data.RatAI == 15 && data.CatAI == 10 && data.VinnieAI == 15) {
                            mode = "Monster Fever";
                        } else if (data.RatAI == 20 && data.CatAI == 20 && data.VinnieAI == 20) {
                            mode = "Theater Trauma";
                        } else {
                            mode = "Custom Night";
                        }
                    } else if (nightType == 1){
                        mode = "The Deepscape";
                    }
                    modeSwitch = false;

                    Discord.updateStatus = true;
                }

                if (layoutModeTemp == null) {
                    layoutModeTemp = new GlyphLayout(menuFontTitle, "AI: " + data.RatAI);
                } else {
                    layoutModeTemp.reset();
                    layoutModeTemp.setText(menuFontTitle, "AI: " + data.RatAI);
                }
                ratAIWidth = (int) (74 - layoutModeTemp.width / 2);

                layoutModeTemp.reset();
                layoutModeTemp.setText(menuFontTitle, "AI: " + data.CatAI);
                catAIWidth = (int) (74 - layoutModeTemp.width / 2);

                layoutModeTemp.reset();
                layoutModeTemp.setText(menuFontTitle, "AI: " + data.VinnieAI);
                vinnieAIWidth = (int) (74 - layoutModeTemp.width / 2);

                layoutModeTemp.reset();

                modeName = mode;
                layoutModeTemp.setText(menuFontTitle, modeName);
                leftArrowPosition = 433 - (int) (layoutModeTemp.width / 2) - 56;
                rightArrowPosition = 433 + (int) (layoutModeTemp.width / 2) + 34;
            }

            for (String s: data.stars.keySet()) {
                data.stars.get(s).sizeAnimation();
                data.stars.get(s).setVisibility(nightType, data.saveData.modes.get(s).beatType != 0);
            }

            if (nightType == 0 || nightType == 1) menuMusicName = "deluxeMenu";
            else menuMusicName = "candysMenu";

            if (playDeluxe && stateManager.getState() == StateManager.State.MENU){
                audioClass.play(menuMusicName);
                audioClass.loop(menuMusicName, true);
                playDeluxe = false;
            }

            audioClass.setVolume(menuMusicName, volume * 0.75f);
            if (pitchTarget != 0){
                audioClass.setPitch(menuMusicName, pitchTarget);
                pitchTarget = 0;
            }

            if (staticScreen >= 0 && staticScreen < 8) {
                staticScreen += time * 40;
            }

            if (staticScreen < 0 || staticScreen >= 8){
                staticScreen = 0;
            }

            //old menu alphas
            if (oldMenuContext == 0) {
                context0Alpha += time * 3;
                if (context0Alpha > 1) context0Alpha = 1;
            } else {
                context0Alpha -= time * 3;
                if (context0Alpha < 0) context0Alpha = 0;
            }

            if (oldMenuContext == 1) {
                context1Alpha += time * 3;
                if (context1Alpha > 1) context1Alpha = 1;
            } else {
                context1Alpha -= time * 3;
                if (context1Alpha < 0) context1Alpha = 0;
            }

            if (oldMenuContext == 2) {
                context2Alpha += time * 3;
                if (context2Alpha > 1) context2Alpha = 1;
            } else {
                context2Alpha -= time * 3;
                if (context2Alpha < 0) context2Alpha = 0;
            }

            if (oldMenuContext == 3) {
                context3Alpha += time * 3;
                if (context3Alpha > 1) context3Alpha = 1;
            } else {
                context3Alpha -= time * 3;
                if (context3Alpha < 0) context3Alpha = 0;
            }

            if ((oldMenuContext == 0 && mode.equals("Rat & Cat"))
                || (oldMenuContext == 1 && data.laserPointer)
                || (oldMenuContext == 2 && data.vSync)
                || (oldMenuContext == 3 && data.flashDebug)
                || mode1 || challenge1 || vSync || flashDebug){
                button1Alpha += time * 3;
                if (button1Alpha > 0.25f) button1Alpha = 0.25f;
            } else {
                button1Alpha -= time * 3;
                if (button1Alpha < 0) button1Alpha = 0;
            }

            if ((oldMenuContext == 0 && mode.equals("Final Night"))
                || (oldMenuContext == 1 && data.hardCassette)
                || (oldMenuContext == 2 && data.oldMenu)
                || (oldMenuContext == 3 && data.hitboxDebug)
                || mode2 || challenge2 || oldMenu || hitboxDebug){
                button2Alpha += time * 3;
                if (button2Alpha > 0.25f) button2Alpha = 0.25f;
            } else {
                button2Alpha -= time * 3;
                if (button2Alpha < 0) button2Alpha = 0;
            }

            if ((oldMenuContext == 0 && mode.equals("Stalling Duo"))
                || (oldMenuContext == 1 && data.faultyFlashlight)
                || (oldMenuContext == 2 && data.nightMusic)
                || (oldMenuContext == 3 && data.freeScroll)
                || mode3 || challenge3 || nightMusic || freeScroll){
                button3Alpha += time * 3;
                if (button3Alpha > 0.25f) button3Alpha = 0.25f;
            } else {
                button3Alpha -= time * 3;
                if (button3Alpha < 0) button3Alpha = 0;
            }

            if ((oldMenuContext == 0 && mode.equals("Play Date"))
                || (oldMenuContext == 1 && data.challenge4)
                || (oldMenuContext == 2 && data.menuMusic)
                || (oldMenuContext == 3 && data.expandedPointer)
                || mode4 || challenge4 || Menu.menuMusic || expandedPointer){
                button4Alpha += time * 3;
                if (button4Alpha > 0.25f) button4Alpha = 0.25f;
            } else {
                button4Alpha -= time * 3;
                if (button4Alpha < 0) button4Alpha = 0;
            }

            if ((oldMenuContext == 0 && mode.equals("Monster Fever"))
                || mode5 || enableAll){
                button5Alpha += time * 3;
                if (button5Alpha > 0.25f) button5Alpha = 0.25f;
            } else {
                button5Alpha -= time * 3;
                if (button5Alpha < 0) button5Alpha = 0;
            }

            if ((oldMenuContext == 0 && mode.equals("Rat & Cat Theater"))
                || mode6 || disableAll){
                button6Alpha += time * 3;
                if (button6Alpha > 0.25f) button6Alpha = 0.25f;
            } else {
                button6Alpha -= time * 3;
                if (button6Alpha < 0) button6Alpha = 0;
            }

            if (nightModes || oldMenuContext == 0){
                button7Alpha += time * 3;
                if (button7Alpha > 0.25f) button7Alpha = 0.25f;
            } else {
                button7Alpha -= time * 3;
                if (button7Alpha < 0) button7Alpha = 0;
            }

            if (challenges || oldMenuContext == 1){
                button8Alpha += time * 3;
                if (button8Alpha > 0.25f) button8Alpha = 0.25f;
            } else {
                button8Alpha -= time * 3;
                if (button8Alpha < 0) button8Alpha = 0;
            }

            if (settings || oldMenuContext == 2){
                button9Alpha += time * 3;
                if (button9Alpha > 0.25f) button9Alpha = 0.25f;
            } else {
                button9Alpha -= time * 3;
                if (button9Alpha < 0) button9Alpha = 0;
            }

            if (options || oldMenuContext == 3){
                button10Alpha += time * 3;
                if (button10Alpha > 0.25f) button10Alpha = 0.25f;
            } else {
                button10Alpha -= time * 3;
                if (button10Alpha < 0) button10Alpha = 0;
            }

            if (textCase != 0 && textValue < 0.25f){
                textValue += time;
                if (textValue > 0.25f) textValue = 0.25f;
            } else if (textCase == 0 && textValue > 0){
                textValue -= time;
                if (textValue < 0) textValue = 0;
            }

            if (textCase != 0 && (textCase != previousTextCase || enemyCase != previousEnemyCase)) {
                menuTextShapeWidth();
                captionWidth = captionLayoutWidthInformation.get(textToRender);
            }

            previousTextCase = textCase;
            previousEnemyCase = enemyCase;

            data.allChallenges = data.laserPointer && data.hardCassette
                    && data.faultyFlashlight && data.challenge4;
        } else {
            if (stateManager.getState() == StateManager.State.MENU){
                if (!loading) {
                    ImageHandler.addImages(data, stateManager);
                    if (starPixmap == null) starPixmap = ImageHandler.loadImageBuffer("menu/starHitbox");
                    if (!fontsLoaded){
                        fontsLoaded = true;
                        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/timeFont.ttf"));
                        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

                        fontParameter.size = 40;
                        fontParameter.color = Color.WHITE;
                        menuFontTitle = fontGenerator.generateFont(fontParameter);

                        fontParameter.size = 32;
                        menuFontHeading = fontGenerator.generateFont(fontParameter);

                        fontParameter.size = 20;
                        menuFontLabel = fontGenerator.generateFont(fontParameter);

                        fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/captionFont.ttf"));
                        fontParameter.size = 14;

                        menuFontCaption = fontGenerator.generateFont(fontParameter);

                        mode = "Custom Night";
                        modeName = mode;
                    }

                    if (textValues == null){
                        textValues = new HashMap<>();
                    }

                    if (textBuilder == null){
                        textBuilder = new StringBuilder();
                    }

                    if (textRenderList == null){
                        textRenderList = new ArrayList<>();
                    }

                    if (captionLayoutWidthInformation == null){
                        captionLayoutWidthInformation = new HashMap<>();
                    }

                    camera.position.x = 512;
                    camera.position.y = 384;
                    camera.update();

                    loading = true;
                    audioClass.stopAllSounds();
                } else if (ImageHandler.doneLoading){
                    loading = false;
                    loaded = true;
                    staticScreen = 0;
                    whiteAlpha = 0;
                    modeSwitch = true;
                    renderReady = true;
                    Discord.updateStatus = true;
                    nightType = previousNightType;
                    playDeluxe = true;
                    if (nightType == 1) pitchTarget = 0.75f;
                    blackFade = 0;
                }
            } else {
                audioClass.stopAllSounds();
            }
        }
    }

    private static void challengeRender(SpriteBatch batch, boolean cond1, boolean cond2, float x, float y, float r, float b, float a){
        Texture texture;
        texture = ImageHandler.images.get("menu/challenge_" + (cond1 ? "on" : "off"));

        if (cond2) batch.setColor(1, 1, 1, a);
        else batch.setColor(r, 0, b, a);

        batch.draw(texture, x, y);
    }

    private static void menuTextShapeWidth(){
        textRenderList.clear();
        textRenderList.addAll(Arrays.asList(textBuilder.toString().split("\n")));

        if (!captionLayoutWidthInformation.containsKey(textToRender)) {
            int maxWidth = 0;
            GlyphLayout layout = new GlyphLayout();

            for (String s: textRenderList) {
                layout.setText(menuFontCaption, s);
                maxWidth = (int) Math.max(maxWidth, layout.width);
                layout.reset();
            }
            captionLayoutWidthInformation.put(textToRender, maxWidth);
        }
    }

    private static void textModifier(String text){
        if (textToRender != null && textToRender.equals(text)) return;

        textToRender = text;
        textBuilder.delete(0, textBuilder.length());
        if (textValues.containsKey(text)){
            textBuilder.append(textValues.get(text));
        } else {
            textBuilder.append(text);
            int index = -1;
            int linePosition = 0;
            int difference = 0;
            int distance = 50;
            for (int i = 0; i < text.length(); i++) {
                linePosition++;
                difference++;
                if (text.charAt(i) == ' ') {
                    index = i;
                    difference = 0;
                }

                if (text.charAt(i) == '\n'){
                    index = -1;
                    difference = 0;
                    linePosition = 0;
                } else if (linePosition % distance == 0) {
                    textBuilder.replace(index, index + 1, "\n");
                    linePosition = difference;
                }
            }
            textValues.put(text, textBuilder.toString());
        }
    }

    public static void render(SpriteBatch batch, Viewport viewport, float mx, float my, Data data){
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        if (!renderReady) return;
        if (screenBuffer == null){
            screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }
        screenBuffer.begin();

        float r = nightType == 0 || nightType == 2 ? 1 : 0.6f;
        float b = nightType == 0 || nightType == 2 ? 0.25f : 1;

        //static
        Texture texture = ImageHandler.images.get("Static/Static" + ((int) staticScreen + 1));
        batch.setColor(r / 2, 0, b / 2, 1);
        batch.draw(texture, 0, 0);
        texture = ImageHandler.images.get("Static/GameoverStatic" + ((int) staticScreen + 1));
        batch.setColor(r / 2, 0, b / 2, 0.75f);
        batch.draw(texture, 0, 0);

        //characters

        // custom night
        if (nightType == 0) {
            texture = ImageHandler.images.get("menu/candy/monster/" + (int) (candyFrame + 1));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            batch.setColor(1, 1, 1, candyAlpha);
            batch.draw(texture, charactersX + 187, charactersY + 190);

            texture = ImageHandler.images.get("menu/vinnie");
            float color = 0.5f + vinnieAlpha;
            batch.setColor(color, color, color, 1);
            batch.draw(texture, charactersX + 450, charactersY + 120);

            texture = ImageHandler.images.get("menu/cat");
            color = 0.5f + catAlpha;
            batch.setColor(color, color, color, 1);
            batch.draw(texture, charactersX + 280, charactersY + 153);

            texture = ImageHandler.images.get("menu/rat");
            color = 0.5f + ratAlpha;
            batch.setColor(color, color, color, 1);
            batch.draw(texture, charactersX - 13, charactersY + 140);
        }

        // shadow night
        if (nightType == 1) {
            texture = ImageHandler.images.get("menu/candy/shadow/" + (int) (candyFrame + 1));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            batch.setColor(1, 1, 0.75f, candyAlpha);
            batch.draw(texture, charactersX + 187, charactersY + 184);

            texture = ImageHandler.images.get("menu/shadowvinnie");
            float color = 0.5f + shadowVinnieAlpha;
            batch.setColor(color, color, color, 1);
            batch.draw(texture, charactersX + 438, charactersY + 149);

            texture = ImageHandler.images.get("menu/shadowcat");
            color = 0.5f + shadowCatAlpha;
            batch.setColor(color, color, color, 1);
            batch.draw(texture, charactersX + 586, charactersY + 134, -texture.getWidth(), texture.getHeight());

            texture = ImageHandler.images.get("menu/shadowrat");
            color = 0.5f + shadowRatAlpha;
            batch.setColor(color, color, color, 1);
            batch.draw(texture, charactersX - 13, charactersY + 140);
        }

        //character buttons box
        if (nightType == 0){
            customNightAIBox(batch, data, ImageHandler.images.get("menu/ai_box"), r, b, (1 - oldMenuAlpha));
            customNightAIBox(batch, data, ImageHandler.images.get("menu/old/ai_box"), r, b, oldMenuAlpha);
            batch.setColor(1, 1, 1, 1);
        }

        if (nightType == 1){
            shadowNightAIBox(batch, data, "menu/ai_box_", r, b, (1 - oldMenuAlpha));
            shadowNightAIBox(batch, data, "menu/old/ai_box_", r, b, oldMenuAlpha);
            batch.setColor(1, 1, 1, 1);
        }

        int xPos = 806;
        int xPosLabel = xPos + 40;
        int yPos = 736;

        menuFontTitle.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuFontTitle.setColor(r, 0, b, 1);
        String title;
        if (nightType == 0) title = "Custom Night";
        else if (nightType == 1) title = "Shadow Night";
        else title = "Candy's Night";
        menuFontTitle.draw(batch, title, 24, 768 - 24);
        if (layoutModeTemp != null) menuFontTitle.draw(batch, modeName, 433 - layoutModeTemp.width / 2, 84);

        menuFontLabel.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuFontLabel.setColor(r, 0, b, 1);
        menuFontLabel.draw(batch, nightType == 0 ? "Click or scroll to adjust AI" : "Click to toggle AI", 24, 696);

        //Deluxe Menu UI
        float alpha = 1 - oldMenuAlpha;
        menuFontHeading.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuFontHeading.setColor(r, 0, b, alpha);
        menuFontHeading.draw(batch, "Settings", xPos, yPos);
        yPos -= 190;
        menuFontHeading.draw(batch, "Options", xPos, yPos);
        yPos -= 190;
        menuFontHeading.draw(batch, "Challenges", xPos, yPos);

        menuFontLabel.setColor(r, 0, b, alpha);
        yPos = 690;
        menuFontLabel.draw(batch, "V-Sync", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Old Menu", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Night Music", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Menu Music", xPosLabel, yPos);

        //cheat labels
        yPos -= 82;
        menuFontLabel.draw(batch, "Flash Debug", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Hitbox Debug", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Free Scroll", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Expanded Pointer", xPosLabel, yPos);

        //challenge labels
        yPos -= 82;
        menuFontLabel.draw(batch, "Laser Pointer", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Hard Cassette", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "Faulty Flashlight", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, nightType == 0 ? "Monster Candy" : nightType == 1 ? "Shadow Candy" : "Shadow Challenge", xPosLabel, yPos);
        yPos -= 36;
        menuFontLabel.draw(batch, "All Challenges", xPosLabel, yPos);

        //settings box
        yPos = 666;
        challengeRender(batch, data.vSync, vSync, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.oldMenu, oldMenu, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.nightMusic, nightMusic, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.menuMusic, menuMusic, xPos, yPos, r, b, alpha);

        //cheat boxes
        yPos -= 82;
        challengeRender(batch, data.flashDebug, flashDebug, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.hitboxDebug, hitboxDebug, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.freeScroll, freeScroll, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.expandedPointer, expandedPointer, xPos, yPos, r, b, alpha);

        //challenge boxes
        yPos -= 82;
        challengeRender(batch, data.laserPointer, challenge1, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.hardCassette, challenge2, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.faultyFlashlight, challenge3, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.challenge4, challenge4, xPos, yPos, r, b, alpha);
        yPos -= 36;
        challengeRender(batch, data.allChallenges, allChallenges, xPos, yPos, r, b, alpha);

        //Old Menu UI
        int buttonX = 812;
        int buttonY = 696;

        //Button 1
        batch.setColor(r, 0, b, context0Alpha * (0.75f + button1Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/mode1"), buttonX, buttonY);
        batch.setColor(r, 0, b, context1Alpha * (0.75f + button1Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/laserPointer"), buttonX, buttonY);
        batch.setColor(r, 0, b, context2Alpha * (0.75f + button1Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/vsync"), buttonX, buttonY);
        batch.setColor(r, 0, b, context3Alpha * (0.75f + button1Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/flash"), buttonX, buttonY);

        //Button 2
        buttonY -= 64;
        batch.setColor(r, 0, b, context0Alpha * (0.75f + button2Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/mode2"), buttonX, buttonY);
        batch.setColor(r, 0, b, context1Alpha * (0.75f + button2Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/hardCassette"), buttonX, buttonY);
        batch.setColor(r, 0, b, context2Alpha * (0.75f + button2Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/oldMenu"), buttonX, buttonY);
        batch.setColor(r, 0, b, context3Alpha * (0.75f + button2Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/hitbox"), buttonX, buttonY);

        //Button 3
        buttonY -= 64;
        batch.setColor(r, 0, b, context0Alpha * (0.75f + button3Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/mode3"), buttonX, buttonY);
        batch.setColor(r, 0, b, context1Alpha * (0.75f + button3Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/faultyFlashlight"), buttonX, buttonY);
        batch.setColor(r, 0, b, context2Alpha * (0.75f + button3Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/nightMusic"), buttonX, buttonY);
        batch.setColor(r, 0, b, context3Alpha * (0.75f + button3Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/freeScroll"), buttonX, buttonY);

        //Button 4
        buttonY -= 64;
        batch.setColor(r, 0, b, context0Alpha * (0.75f + button4Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/mode4"), buttonX, buttonY);
        batch.setColor(r, 0, b, context1Alpha * (0.75f + button4Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/" + (nightType == 0 ? "monstergami" : "twitchyCat")), buttonX, buttonY);
        batch.setColor(r, 0, b, context2Alpha * (0.75f + button4Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/menuMusic"), buttonX, buttonY);
        batch.setColor(r, 0, b, context3Alpha * (0.75f + button4Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/expandedPointer"), buttonX, buttonY);

        //Button 5
        buttonY -= 64;
        batch.setColor(r, 0, b, context0Alpha * (0.75f + button5Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/mode5"), buttonX, buttonY);
        batch.setColor(r, 0, b, (1 - context0Alpha) * (0.75f + button5Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/enableAll"), buttonX, buttonY);

        //Button 6
        buttonY -= 64;
        batch.setColor(r, 0, b, context0Alpha * (0.75f + button6Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/mode6"), buttonX, buttonY);
        batch.setColor(r, 0, b, (1 - context0Alpha) * (0.75f + button6Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/disableAll"), buttonX, buttonY);

        //Extra Buttons
        buttonY -= 64;
        batch.setColor(r, 0, b, (0.75f + button7Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/nightModes"), buttonX, buttonY);
        buttonY -= 64;
        batch.setColor(r, 0, b, (0.75f + button8Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/challenges"), buttonX, buttonY);
        buttonY -= 64;
        batch.setColor(r, 0, b, (0.75f + button9Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/settings"), buttonX, buttonY);
        buttonY -= 64;
        batch.setColor(r, 0, b, (0.75f + button10Alpha) * oldMenuAlpha);
        batch.draw(ImageHandler.images.get("menu/old/options"), buttonX, buttonY);

        //ready
        texture = ImageHandler.images.get("menu/ready");
        batch.setColor(r, 0, b, (0.75f + readyAlpha) * (1 - oldMenuAlpha));
        batch.draw(texture, 806, 18);

        texture = ImageHandler.images.get("menu/old/ready");
        batch.setColor(r, 0, b, (0.75f + readyAlpha) * oldMenuAlpha);
        batch.draw(texture, 812, 16);
        batch.setColor(1, 1, 1, 1);

        //stars
        Star star = data.stars.get(modeName);
        texture = ImageHandler.images.get("menu/star");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(star.color[0], star.color[1], star.color[2], star.visibility + star.alphaVisibility);
        batch.draw(texture,
                433 - star.size / 2,
                148 - star.size / 2,
                star.size,
                star.size);

        //change night
        texture = ImageHandler.images.get("menu/nightArrow");
        if (arrowHovered) batch.setColor(1, 1, 1, 1);
        else batch.setColor(r, 0, b, 1);
        batch.draw(texture, 260, 712);

        //game mode
        if (nightType == 0){
            if (rightArrow) batch.setColor(1, 1, 1, (1 - oldMenuAlpha));
            else batch.setColor(r, 0, b, (1 - oldMenuAlpha));
            batch.draw(texture, rightArrowPosition, 54);

            if (leftArrow) batch.setColor(1, 1, 1, (1 - oldMenuAlpha));
            else batch.setColor(r, 0, b, (1 - oldMenuAlpha));
            batch.draw(texture, leftArrowPosition + 24, 54, -24, 28);
        }

        //icons
        texture = ImageHandler.images.get("menu/info");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(r, 0, b, 1);
        batch.draw(texture, 18, 18, 42, 42);

        texture = ImageHandler.images.get("menu/help");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(r, 0, b, 1);
        batch.draw(texture, 18, 76, 42, 42);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(1, 1, 1, blackFade);
        batch.draw(texture, 0, texture.getHeight(), texture.getWidth(), -texture.getHeight());

        if (textValue > 0) {
            float mouseX = mx + 10;
            boolean reverse = false;
            if (mx > viewport.getWorldWidth() / 2) {
                mouseX -= captionWidth - 10;
                reverse = true;
            }
            float distance = 20;
            float shapeHeight = distance * (textRenderList.size() + 1) - 8;
            float height = my - shapeHeight / 2;
            if (my < 124){
                height = my;
            }

            batch.setColor(0, 0, 0, textValue * 3.5f);
            texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();

            if (reverse) {
                batch.draw(texture, mouseX - 30, height, captionWidth + 10, shapeHeight);
            } else {
                batch.draw(texture, mouseX + 5, height, captionWidth + 10, shapeHeight);
            }

            menuFontCaption.setColor(1, 1, 1, textValue * 4);
            menuFontCaption.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            height = shapeHeight / 2;
            if (my < 124){
                height = shapeHeight;
            }

            for (int i = 0; i < textRenderList.size(); i++) {
                float y = my - (distance * i) - 10;
                if (reverse) {
                    menuFontCaption.draw(batch, textRenderList.get(i), mouseX - 25, y + height);
                } else {
                    menuFontCaption.draw(batch, textRenderList.get(i), mouseX + 10, y + height);
                }
            }
        }

        batch.setColor(1, 1, 1, whiteAlpha);
        batch.draw(FNaC3Deluxe.shapeBuffer.getColorBufferTexture(), 0, 0);
        batch.setColor(1, 1, 1, 1);

        renderReady = loaded;

        batch.flush();
    }

    private static float nightButtonAlpha(boolean hovered, boolean condition2, float alpha){
        if (hovered || condition2) {
            if (alpha < 0.25f) {
                alpha += Gdx.graphics.getDeltaTime() * 2;
                if (alpha > 0.25f) alpha = 0.25f;
            }
        } else {
            if (alpha > 0) {
                alpha -= Gdx.graphics.getDeltaTime() * 2;
                if (alpha < 0) alpha = 0;
            }
        }
        return alpha;
    }

    private static float nightCharacterAlpha(boolean condition2, float alpha){
        if (condition2) {
            if (alpha < 0.5f) {
                alpha += Gdx.graphics.getDeltaTime() * 4;
                if (alpha > 0.5f) alpha = 0.5f;
            }
        } else {
            if (alpha > 0) {
                alpha -= Gdx.graphics.getDeltaTime() * 4;
                if (alpha < 0) alpha = 0;
            }
        }
        return alpha;
    }

    private static int nightCustomNightAI(AudioClass audioClass, boolean hovered, int ai, int enemyCase, String text){
        if (!hovered) return ai;
        textModifier(text);
        textCase = 10;
        Menu.enemyCase = enemyCase;
        modeSwitch = true;
        writeConfig = true;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            ai = 20;
            audioClass.play("select");
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            ai = 0;
            audioClass.play("select");
        } else if (FNaC3Deluxe.scrollAmount != 0){
            ai -= FNaC3Deluxe.scrollAmount;
            if (ai < 0) ai = 0;
            else if (ai > 20) ai = 20;
        } else {
            modeSwitch = false;
            writeConfig = false;
        }
        return ai;
    }

    private static boolean nightShadowNightAI(AudioClass audioClass, boolean hovered, boolean ai, int enemyCase, String text){
        if (!hovered) return ai;
        textModifier(text);
        textCase = 10;
        Menu.enemyCase = enemyCase;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            audioClass.play("select");
            modeSwitch = true;
            writeConfig = true;
            ai = !ai;
        }
        return ai;
    }

    private static void aiModeChange(String modeName, Data data){
        switch (modeName) {
            case "Rat & Cat" -> {
                data.RatAI = 20;
                data.CatAI = 20;
                data.VinnieAI = 0;
            }
            case "Final Night" -> {
                data.RatAI = 0;
                data.CatAI = 0;
                data.VinnieAI = 20;
            }
            case "Stalling Duo" -> {
                data.RatAI = 20;
                data.CatAI = 0;
                data.VinnieAI = 20;
            }
            case "Play Date" -> {
                data.RatAI = 10;
                data.CatAI = 5;
                data.VinnieAI = 10;
            }
            case "Monster Fever" -> {
                data.RatAI = 15;
                data.CatAI = 10;
                data.VinnieAI = 15;
            }
            case "Theater Trauma" -> {
                data.RatAI = 20;
                data.CatAI = 20;
                data.VinnieAI = 20;
            }
            default -> {
                data.RatAI = 0;
                data.CatAI = 0;
                data.VinnieAI = 0;
            }
        }
    }

    private static boolean oldButtonCondition(float mx, float my, int yPos, int menuContext){
        return oldMenuContext == menuContext && mouseOver(mx, my, 812, yPos, 1008, yPos + 56);
    }

    private static boolean oldButtonCondition(float mx, float my, int yPos){
        return oldMenuContext != 0 && mouseOver(mx, my, 812, yPos, 1008, yPos + 56);
    }

    private static void toggleAll(Data data, boolean toggle){
        switch (oldMenuContext){
            case 1 -> {
                data.laserPointer = toggle;
                data.hardCassette = toggle;
                data.faultyFlashlight = toggle;
                data.challenge4 = toggle;
                modeSwitch = true;
            }
            case 2 -> {
                data.vSync = toggle;
                data.oldMenu = toggle;
                data.nightMusic = toggle;
                data.menuMusic = toggle;
            }
            case 3 -> {
                data.flashDebug = toggle;
                data.hitboxDebug = toggle;
                data.freeScroll = toggle;
                data.expandedPointer = toggle;
            }
        }
    }

    private static void customNightAIBox(SpriteBatch batch, Data data, Texture texture, float r, float b, float alpha){
        batch.setColor(r, 0, b, -0.25f + (1 + ratButtonAlpha) * alpha);
        batch.draw(texture, 132 + charactersX, 278 + charactersY);

        batch.setColor(r, 0, b, (0.75f + catButtonAlpha) * alpha);
        batch.draw(texture, 347 + charactersX, 278 + charactersY);

        batch.setColor(r, 0, b, (0.75f + vinnieButtonAlpha) * alpha);
        batch.draw(texture, 562 + charactersX, 278 + charactersY);

        batch.setColor(1, 1, 1, 1);
        menuFontTitle.setColor(r, 0, b, -0.25f + (1 + ratButtonAlpha) * alpha);
        menuFontTitle.draw(batch, "AI: " + data.RatAI,
                132 + ratAIWidth + charactersX, 330 + charactersY);

        menuFontTitle.setColor(r, 0, b, -0.25f + (1 + catButtonAlpha) * alpha);
        menuFontTitle.draw(batch, "AI: " + data.CatAI,
                347 + catAIWidth + charactersX, 330 + charactersY);

        menuFontTitle.setColor(r, 0, b, -0.25f + (1 + vinnieButtonAlpha) * alpha);
        menuFontTitle.draw(batch, "AI: " + data.VinnieAI,
                562 + vinnieAIWidth + charactersX, 330 + charactersY);
    }

    private static void shadowNightAIBox(SpriteBatch batch, Data data, String textureName, float r, float b, float alpha){
        Texture texture = ImageHandler.images.get(textureName + (data.ShadowRatAI ? "on" : "off"));
        batch.setColor(r, 0, b, -0.25f + (1 + shadowRatButtonAlpha) * alpha);
        batch.draw(texture, 132 + charactersX, 278 + charactersY);

        texture = ImageHandler.images.get(textureName + (data.ShadowCatAI ? "on" : "off"));
        batch.setColor(r, 0, b, -0.25f + (1 + shadowCatButtonAlpha) * alpha);
        batch.draw(texture, 347 + charactersX, 278 + charactersY);

        texture = ImageHandler.images.get(textureName + (data.ShadowVinnieAI ? "on" : "off"));
        batch.setColor(r, 0, b, -0.25f + (1 + shadowVinnieButtonAlpha) * alpha);
        batch.draw(texture, 562 + charactersX, 278 + charactersY);
    }
}
