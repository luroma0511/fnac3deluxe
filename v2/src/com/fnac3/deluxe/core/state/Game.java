package com.fnac3.deluxe.core.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.FNaC3Deluxe;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.discord.Discord;
import com.fnac3.deluxe.core.enemy.Cat;
import com.fnac3.deluxe.core.enemy.Monstergami;
import com.fnac3.deluxe.core.enemy.Rat;
import com.fnac3.deluxe.core.enemy.ShadowCat;
import com.fnac3.deluxe.core.enemy.ShadowRat;
import com.fnac3.deluxe.core.enemy.Vinnie;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class Game {

    private static FrameBuffer fbo;
    private static FrameBuffer roomBuffer;
    private static FrameBuffer screenBuffer;

    private static BitmapFont gameoverFont;
    private static BitmapFont gameoverSelectionsFont;
    private static GlyphLayout layout;
    private static float gameoverScreenAlpha;
    private static boolean fontsAdded;
    private static boolean retryButton;
    private static boolean menuButton;
    private static boolean pressedRetry;
    private static boolean pressedMenu;
    private static boolean dreamscapeStart;
    private static float gameoverStatic;
    private static float zoomCharacter;
    private static float monstergamiCooldown;
    private static int monstergamiTimes;
    private static boolean monstergamiPositive;
    private static float monstergamiFrames;
    private static float gameoverAlpha;

    public static float screenAlpha;
    public static Music ambience;
    public static Music theaterMusic;

    public static String gameoverReason;
    public static String nightTime;
    public static float time;
    public static float hour;
    public static int previousHourOfGame;
    public static int hourOfGame;
    public static float purpleSlownessTimer;
    public static float purpleTime;
    public static float purpleTimeTarget;

    public static boolean firstFrame;
    public static boolean gameover;
    public static boolean restart;
    public static boolean win;
    public static float winAlpha;
    public static float winDuration;
    private static float clockAnimation;
    private static Random random;

    public static Pixmap playButton;
    public static Pixmap stopButton;
    public static Pixmap rewindButton;

    public static Pixmap batteryPixmap;
    public static Pixmap tapeButtonPixmap;
    public static Pixmap bedButtonPixmap;
    public static Pixmap tapeBackButtonPixmap;
    public static Pixmap bedBackButtonPixmap;

    private static final TextureRegion roomRegion = new TextureRegion();

    public static void loadPixmaps(){
        playButton = ImageHandler.loadImageBuffer("game/room/Tape/Buttons/PlayButton");
        stopButton = ImageHandler.loadImageBuffer("game/room/Tape/Buttons/StopButton");
        rewindButton = ImageHandler.loadImageBuffer("game/room/Tape/Buttons/RewindButton");

        batteryPixmap = ImageHandler.loadImageBuffer("game/room/Tape/batteryHitbox");

        tapeButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/TapePlayer");
        tapeBackButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/TapePlayerBack");
        bedButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/UnderBed");
        bedBackButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/UnderBedBack");
    }

    public static void start(Data data, AudioClass audioClass){
        audioClass.stopAllSounds();
        if (random == null) {
            random = new Random();
        }
        retryButton = false;
        menuButton = false;
        screenAlpha = 0;
        if (ambience != null){
            ambience.stop();
        }
        if (theaterMusic != null){
            theaterMusic.stop();
        }
        gameover = false;
        winDuration = 0;
        win = false;
        winAlpha = 0;
        Player.reset();
        Monstergami.reset(audioClass);
        Rat.reset();
        Cat.reset(random, audioClass);
        Vinnie.reset();
        ShadowRat.reset();
        ShadowCat.reset(audioClass, random);
        screenAlpha = 0;
        firstFrame = true;
        dreamscapeStart = true;
        restart = false;
        hour = 60;
        time = 0;
        hourOfGame = 12;
        previousHourOfGame = 12;
        purpleTime = 0;
        purpleSlownessTimer = 10;
        purpleTimeTarget = 40;
        Discord.updateStatus = true;
    }

    public static void gameoverInput(float mx, float my){
        if (pressedRetry || pressedMenu) return;

        retryButton = gameoverAlpha > 0 && mx >= 394 && mx <= 455 && my >= 277 && my <= 302;

        menuButton = gameoverAlpha > 0 && mx >= 571 && mx <= 636 && my >= 277 && my <= 302;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if (menuButton){
                pressedMenu = true;
            } else if (retryButton){
                pressedRetry = true;
            }
        }
    }

    public static void input(StateManager stateManager, Viewport viewport, Vector3 v3, Data data){
        float mx = v3.x - Player.roomPosition[0] - Player.shakingPosition;
        float my = v3.y - Player.roomPosition[1];

        if (win) return;

        if (gameover){
            gameoverInput(mx, my);
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)){
            stateManager.setState(StateManager.State.MENU);
            ambience.stop();
            theaterMusic.stop();
            Player.tape.stop();
            return;
        }

        if (!Player.turningAround && !Player.freeze && (!Player.snapPosition || data.freeScroll || Monstergami.attack || Player.room != 0)) {
            Player.move(mx, my, viewport.getWorldWidth(), viewport.getWorldHeight());
        } else if (Player.snapPosition && !data.freeScroll && !Monstergami.attack){
            Player.moveTarget();
        }

        if (Player.room != 2 && (Player.scared
                || (Menu.nightType == 0 && (Rat.shaking || Vinnie.shaking))
                || (Menu.nightType == 1 && (ShadowRat.shaking || ShadowCat.shaking))
                || Monstergami.shaking)){
            Player.roomShake();
        } else {
            Player.shakingPosition = 0;
        }

        if (Player.roomPosition[0] < 3) {
            Player.roomPosition[0] = 3;
        } else if (Player.roomPosition[0] > 2045) {
            Player.roomPosition[0] = 2045;
        }

        mx += Player.roomPosition[0] + Player.shakingPosition;
        my += Player.roomPosition[1];

        Player.flashlight(mx, my);
        if (!Player.freeze && !Player.justStarted && Player.room == 0) {
            if (mx < 1024) {
                Player.side = 0;
            } else if (mx < 2047){
                Player.side = 1;
            } else {
                Player.side = 2;
            }
        } else {
            if (Player.justStarted){
                Player.justStarted = false;
            }

            if (Player.room != 0) {
                Player.side = -1;
            }
        }

        Player.buttons(mx, my);

        if (Player.room == 1 && !Player.turningAround){
            Player.foundUnderBed = Player.mouseOverEnemyInBed();
        }

        if (Player.room == 2 && !Player.turningAround && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if (!Player.tapePlay && Player.tapeCondition("Play", !Player.tapeRewind && !Player.tapeStolen, mx, my)){
                Player.tapePlay = true;
                if (Player.stopPosition > 0){
                    Player.stopPosition = 2;
                    Player.tapeStop = false;
                }
                if (Player.rewindPosition > 0){
                    Player.rewindPosition = 0;
                }
            } else if (!Player.tapeStop && Player.tapeCondition("Stop", (Player.tapePlay || Player.tapeRewind) && !Player.tapeStolen, mx, my)){
                Player.tapeStop = true;
                if (Player.tapePlay) {
                    Vinnie.tapeWeasel = false;
                    Player.playPosition = 3;
                    Player.tapePlay = false;
                    ShadowRat.tapeWeasel = false;
                    ShadowCat.tapeWeasel = false;
                    Rat.tapeWeasel = false;
                } else if (Player.tapeRewind) {
                    Player.rewindPosition = 3;
                    Player.tapeRewind = false;
                }
            } else if (!Player.tapeRewind && Player.tapeCondition("Rewind", !Player.tapePlay && !Player.tapeStolen, mx, my)){
                Player.tapeRewind = true;
                if (Player.stopPosition > 0){
                    Player.stopPosition = 2;
                    Player.tapeStop = false;
                }
                if (Player.playPosition > 0){
                    Player.playPosition = 0;
                }
            }

            Player.batteryInput(mx, my);
        }

        if (Menu.nightType == 0){
            if (Rat.ai != 0) {
                Rat.input();
                if (Player.room == 2 && !Player.turningAround) {
                    Rat.tapeSpotted = true;
                }
            }

            if (Cat.ai != 0) {
                Cat.input();
            }

            if (Vinnie.ai != 0) {
                Vinnie.input();
                if (Player.room == 2 && !Player.turningAround) {
                    Vinnie.tapeSpotted = true;
                }
            }
        } else {
            if (ShadowRat.active && ShadowCat.jumpscareI == 0) {
                ShadowRat.input();
                if (Player.room == 2 && !Player.turningAround) {
                    ShadowRat.tapeSpotted = true;
                }
            }

            if (ShadowCat.active && ShadowRat.jumpscareI == 0) {
                ShadowCat.input();
                if (Player.room == 2 && !Player.turningAround) {
                    ShadowCat.tapeSpotted = true;
                }
            }
        }

        if (Monstergami.active) {
            Monstergami.input();
        }
    }

    public static void gameoverUpdate(StateManager stateManager, AudioClass audioClass){
        if (gameoverScreenAlpha > 0){
            gameoverScreenAlpha -= Gdx.graphics.getDeltaTime() * 2;
            if (gameoverScreenAlpha < 0){
                gameoverScreenAlpha = 0;
            }
        }

        if (monstergamiCooldown > 0){
            monstergamiCooldown -= Gdx.graphics.getDeltaTime();
            if (monstergamiCooldown <= 0){
                monstergamiCooldown = 3 + random.nextInt(4);
                monstergamiTimes = 1 + random.nextInt(2);
            }
        }

        if (monstergamiTimes > 0 && !monstergamiPositive && monstergamiFrames == 0){
            monstergamiPositive = true;
            monstergamiTimes--;
        }

        if (monstergamiPositive){
            monstergamiFrames += Gdx.graphics.getDeltaTime() * 40;
            if (monstergamiFrames >= 10) {
                monstergamiFrames = 8.99f;
                monstergamiPositive = false;
            }
        } else {
            monstergamiFrames -= Gdx.graphics.getDeltaTime() * 40;
            if (monstergamiFrames <= 0) monstergamiFrames = 0;
        }

        if (zoomCharacter > 0){
            zoomCharacter -= Gdx.graphics.getDeltaTime();
            if (zoomCharacter < 0) zoomCharacter = 0;
        }

        if (zoomCharacter == 0 && gameoverAlpha < 1) {
            gameoverAlpha += Gdx.graphics.getDeltaTime() * 2;
            if (gameoverAlpha > 1) gameoverAlpha = 1;
        }

        gameoverStatic += Gdx.graphics.getDeltaTime() * 30;
        if (gameoverStatic >= 8){
            gameoverStatic = 0;
        }

        if (pressedRetry || pressedMenu){
            if (screenAlpha > 0) {
                screenAlpha -= Gdx.graphics.getDeltaTime();
                audioClass.setVolume("monstergami", screenAlpha);
                audioClass.setVolume("scaryImpact", screenAlpha);
                if (screenAlpha < 0) {
                    screenAlpha = 0;
                }
            } else {
                audioClass.stop("monstergami");
                audioClass.stop("scaryImpact");
                if (pressedRetry){
                    pressedRetry = false;
                    restart = true;
                    gameover = false;
                } else {
                    pressedMenu = false;
                    gameover = false;
                    stateManager.setState(StateManager.State.MENU);
                }
            }
        }
    }

    public static void update(StateManager stateManager, Camera camera, Viewport viewport, Data data, AudioClass audioClass) {
        if (stateManager.getState() != StateManager.State.GAME) return;

        if (!fontsAdded){
            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/timeFont.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            fontParameter.color = Color.WHITE;
            fontParameter.size = 56;
            gameoverFont = fontGenerator.generateFont(fontParameter);

            fontParameter.size = 28;
            gameoverSelectionsFont = fontGenerator.generateFont(fontParameter);

            layout = new GlyphLayout();

            fontsAdded = true;
        }

        if (win) {
            clockAnimation += Gdx.graphics.getDeltaTime() * 60;
            if (clockAnimation >= 4) {
                clockAnimation = 0;
            }
            if (screenAlpha > 0) {
                if (Gdx.graphics.getDeltaTime() < 0.5f) {
                    screenAlpha -= Gdx.graphics.getDeltaTime() * 2;
                    if (screenAlpha < 0) {
                        screenAlpha = 0;
                    }
                }
            }

            if (winDuration < 6) {
                if (screenAlpha == 0) {
                    winAlpha += Gdx.graphics.getDeltaTime();
                    if (winAlpha > 1) {
                        winAlpha = 1;
                    }
                }

                winDuration += Gdx.graphics.getDeltaTime();
                if (winDuration > 6) {
                    winDuration = 6;
                }
            } else if (winAlpha > 0) {
                winAlpha -= Gdx.graphics.getDeltaTime();
                if (winAlpha < 0) {
                    winAlpha = 0;
                }
            } else {
                stateManager.setState(StateManager.State.MENU);
            }
            return;
        }

        if (gameover) {
            gameoverUpdate(stateManager, audioClass);
            return;
        }

        if (ambience == null) {
            ambience = Gdx.audio.newMusic(Gdx.files.local("assets/sounds/ambience.wav"));
            ambience.setLooping(true);
            ambience.play();
        } else if (!ambience.isPlaying() && stateManager.getState() == StateManager.State.GAME) {
            ambience.play();
        }

        if (dreamscapeStart && data.nightMusic && Menu.modeName.contains("The Dreamscape")){
            audioClass.play("dreamTheme");
            audioClass.setVolume("dreamTheme", 0.2f);
            audioClass.loop("dreamTheme", true);
        }
        dreamscapeStart = false;

        if (theaterMusic == null) {
            theaterMusic = Gdx.audio.newMusic(Gdx.files.local("assets/sounds/theaterMusic.wav"));
            theaterMusic.setLooping(true);
            theaterMusic.setVolume(0.3f);
            if (Menu.modeName.contains("Rat & Cat Theater") && data.nightMusic){
                theaterMusic.play();
            }
        } else if (!theaterMusic.isPlaying() && stateManager.getState() == StateManager.State.GAME) {
            if (Menu.modeName.contains("Rat & Cat Theater") && data.nightMusic){
                theaterMusic.play();
            }
        }

        if (screenAlpha < 1 && !firstFrame) {
            if (Gdx.graphics.getDeltaTime() < 0.5f) {
                screenAlpha += Gdx.graphics.getDeltaTime() * 4;
                if (screenAlpha > 1) {
                    screenAlpha = 1;
                }
            }
        }

        firstFrame = false;

        float add = Gdx.graphics.getDeltaTime();

        if (Menu.nightType != 2 && Player.tape.isPlaying()) {
            if (data.hardCassette) time += add;
            else time += add * 1.5f;
        } else if (Menu.nightType == 2){
            time += add;
        }

        boolean tapeWeasel = Vinnie.tapeWeasel || Rat.tapeWeasel || ShadowRat.tapeWeasel || ShadowCat.tapeWeasel;
        if (data.hardCassette && tapeWeasel) time -= add;
        if (time < 0) time = 0;

        if (time / hour >= 1) {
            hourOfGame = (int) (time / hour);
            if (previousHourOfGame != hourOfGame){
                Discord.updateStatus = true;
                previousHourOfGame = hourOfGame;
            }
        }

        if (Menu.nightType == 2){
            int hour = (int) time / 60;
            nightTime = hour + ":";
            int tempTime = (int) (time % 60);
            if (tempTime < 10) nightTime += 0;
            nightTime += tempTime;
        }

        if ((Menu.nightType != 2 && hourOfGame == 6) || (Menu.nightType == 2 && Player.tapeEnd)) {
            win = true;
            if (Menu.nightType == 0){
                data.writeWin(Menu.modeName,
                        !data.flashDebug && !data.hitboxDebug && (!data.expandedPointer || data.laserPointer)
                );
            } else if (Menu.nightType == 1){
                data.writeWin("The Dreamscape",
                        ShadowRat.active && ShadowCat.active && !data.flashDebug && !data.hitboxDebug && (!data.expandedPointer || data.laserPointer)
                );
            } else if (Menu.nightType == 2){
                data.writeWinAndTime("Monstergami Night",
                        !data.flashDebug && !data.hitboxDebug && (!data.expandedPointer || data.laserPointer),
                        nightTime);
            }
            audioClass.stopAllSounds();
            audioClass.play("win");
            ambience.stop();
            theaterMusic.stop();
            Player.tape.stop();
            camera.position.x = viewport.getWorldWidth() / 2 + Player.roomPosition[0] + Player.shakingPosition;
            camera.position.y = viewport.getWorldHeight() / 2 + Player.roomPosition[1];
            camera.update();
            return;
        }

        Player.tapeFunctionality(data, audioClass);
        Player.flashlightFlickerMechanic(data, random);
        if (Player.batterySound){
            audioClass.play("battery");
            Player.batterySound = false;
        }

        boolean jumpscare = false;

        if (Menu.nightType == 0 && (Rat.jumpscare || Cat.jumpscare || Vinnie.jumpscare)) {
            jumpscare = true;
        } else if (Menu.nightType == 1 && (ShadowRat.jumpscare || ShadowCat.jumpscare)) {
            jumpscare = true;
        } else if (Menu.nightType == 2 && Monstergami.jumpscare){
            jumpscare = true;
        }

        if (data.hardCassette) {
            if ((Player.tape.isPlaying() && !Player.tapeEnd) || jumpscare) {
                purpleSlownessTimer = 10;
                if (purpleTime > 0) {
                    purpleTime -= Gdx.graphics.getDeltaTime() * 100;
                    if (purpleTime < 0) {
                        purpleTime = 0;
                    }
                }
            } else {
                if (purpleSlownessTimer > 0) {
                    purpleSlownessTimer -= Gdx.graphics.getDeltaTime();
                    if (purpleSlownessTimer < 0) {
                        purpleSlownessTimer = 0;
                    }
                }

                if (purpleSlownessTimer == 0) {
                    purpleTime += Gdx.graphics.getDeltaTime() * 1.5f;
                } else {
                    purpleTime += Gdx.graphics.getDeltaTime() / 2;
                }
            }

            if (purpleTime >= purpleTimeTarget) {
                gameover = true;
                Discord.updateStatus = true;
                Player.battleOverlay = 0;
                Player.overlayTransparency = 0;
                Player.scared = false;
                Player.shakingPosition = 0;
                gameoverScreenAlpha = 1;
                Player.roomPosition[0] = 0;
                Player.roomPosition[1] = 0;
                audioClass.stopAllSounds();
                ambience.stop();
                theaterMusic.stop();
                gameoverReason = "Fell asleep";
                if (Menu.nightType == 2) {
                    zoomCharacter = 1;
                    gameoverAlpha = 0;
                    monstergamiFrames = 9.99f;
                    monstergamiPositive = false;
                    monstergamiTimes = 0;
                    monstergamiCooldown = 3 + random.nextInt(4);
                    audioClass.play("scaryImpact");
                    audioClass.play("monstergami");
                    audioClass.loop("monstergami", true);
                }
            }
        }

        if (!gameover) {
            if (Player.foundUnderBed && !Player.foundUnderBedLock) {
                audioClass.play("bed");
                Player.foundUnderBedLock = true;
            } else if (Player.foundUnderBedLock && Player.room != 1) {
                Player.foundUnderBedLock = false;
                Player.foundUnderBed = false;
            }

            boolean attack = false;

            if (Menu.nightType == 0) {
                if (Rat.ai != 0 && (!jumpscare || Rat.jumpscare)) {
                    Rat.update(random, data, audioClass);
                    if (Rat.attack) {
                        attack = true;
                    }
                }

                if (Cat.ai != 0 && (!jumpscare || Cat.jumpscare)){
                    Cat.update(random, data, audioClass);
                }

                if (Vinnie.ai != 0 && (!jumpscare || Vinnie.jumpscare)) {
                    Vinnie.update(random, data, audioClass);
                    if (Vinnie.attack) {
                        attack = true;
                    }
                }

                if (Monstergami.side == -1) {
                    if (!Player.freeze && ((Rat.shaking && !Rat.twitchingNow)
                            || (Cat.room == 1 && Cat.shaking && !Cat.twitchingNow)
                            || (Vinnie.shaking && !Vinnie.twitchingNow))) {
                        audioClass.play("twitch");
                        audioClass.loop("twitch", true);
                    }
                }

                if ((Rat.room == 1 && Rat.attack) || (Rat.room == 3 && Rat.timeToFlash > 0)) {
                    Rat.twitchingNow = Rat.shaking && !jumpscare;
                }

                if (Cat.room == 4 && Cat.timeToFlash > 0) {
                    if (Cat.shaking && !Cat.twitchingNow && !jumpscare) {
                        audioClass.play("catPulse");
                        audioClass.loop("catPulse", true);
                        Cat.twitchingNow = true;
                    } else if (jumpscare || (!Cat.shaking && Cat.twitchingNow)) {
                        audioClass.stop("catPulse");
                        Cat.twitchingNow = false;
                    }
                }

                if ((Vinnie.room == 1 && Vinnie.attack) || (Vinnie.room == 3 && Vinnie.timeToFlash > 0)) {
                    Vinnie.twitchingNow = Vinnie.shaking && !jumpscare;
                }

                if (Monstergami.side == -1) {
                    if ((!Rat.shaking || Rat.dontSoundShake)
                            && (Cat.room != 1 || !Cat.shaking || Cat.dontSoundShake)
                            && (!Vinnie.shaking || Vinnie.dontSoundShake)) {
                        audioClass.stop("twitch");
                    }
                }

            } else if (Menu.nightType == 1) {
                if (ShadowRat.active && ShadowCat.jumpscareI == 0) {
                    ShadowRat.update(random, data, audioClass);
                    if (ShadowRat.attack) {
                        attack = true;
                    }
                }

                if (ShadowCat.active && ShadowRat.jumpscareI == 0) {
                    ShadowCat.update(random, data, audioClass);
                    if (ShadowCat.attack) {
                        attack = true;
                    }
                }
            }

            if (Monstergami.active) {
                Monstergami.update(data, random, audioClass);
                if (Monstergami.attack) {
                    attack = true;
                }
                if (Monstergami.side != -1){
                    if (Menu.nightType == 2) {
                        Monstergami.aggression = Player.tapePosition / 30;
                        audioClass.setPitch("monstergami", 1 - Player.tapePosition / 360);
                    } else {
                        Monstergami.aggression = (time / hour) / 1.25f;
                        audioClass.setPitch("monstergami", 1 - (time / hour) / 15);
                    }
                }
            }

            Player.blackness();

            if (!jumpscare) {

                Player.turningAround(audioClass);
                Player.buttonVisibility();

                if (!Player.scared && attack) {
                    if (!Monstergami.attack) {
                        audioClass.play("attack_begin");
                        if (!ShadowCat.attack) {
                            audioClass.play("attack");
                            audioClass.loop("attack", true);
                        }
                    } else {
                        audioClass.play("monstergamiAmbience");
                        audioClass.loop("monstergamiAmbience", true);
                        audioClass.setVolume("monstergamiAmbience", 0);
                    }
                    Player.scared = true;
                } else if (Player.scared) {
                    if (attack) {
                        String path = "attack";
                        if (Monstergami.attack){
                            path = "monstergamiAmbience";
                            if (audioClass.getVolume(path) < 0.8){
                                float volume = audioClass.getVolume(path) + Gdx.graphics.getDeltaTime();
                                if (volume > 0.8){
                                    volume = 0.8f;
                                }
                                audioClass.setVolume(path, volume);
                            }
                        }
                        float pitch = audioClass.getPitch(path);
                        float speed = Gdx.graphics.getDeltaTime() * 60;
                        if (Monstergami.attack){
                            pitch += 0.000435f * speed;
                        } else if (Vinnie.attack){
                            pitch += 0.000175f * speed;
                        } else {
                            pitch += 0.00025f * Gdx.graphics.getDeltaTime() * 60;
                        }

                        audioClass.setPitch(path, pitch);
                        if (Player.overlayTransparency != 1) {
                            Player.overlayTransparency = 1;
                            Player.battleOverlay = 1;
                        }
                    } else {
                        Player.scared = false;
                        audioClass.stop("attack");
                        audioClass.stop("monstergamiAmbience");
                    }
                }

                if (!Player.scared && !attack) {
                    Player.overlayTransparency -= Gdx.graphics.getDeltaTime() / 7;
                    if (Player.overlayTransparency < 0) {
                        Player.overlayTransparency = 0;
                    }
                }

                if (Player.battleOverlay <= 0 && Player.overlayTransparency != 0) {
                    Player.battleOverlay = 1;
                } else {
                    Player.battleOverlay -= Gdx.graphics.getDeltaTime() * 2;
                }
            } else {
                if (ambience.isPlaying()) {
                    ambience.stop();
                }

                if (theaterMusic.isPlaying()){
                    theaterMusic.stop();
                }

                boolean gameoverTrue = false;

                if (Menu.nightType == 0 && (Rat.jumpscareTime == 0 || Cat.jumpscareTime == 0 || Vinnie.jumpscareTime == 0)) {
                    gameoverTrue = true;
                    zoomCharacter = 1;
                    gameoverAlpha = 0;
                    if (Rat.jumpscareTime == 0) gameoverReason = "Died to Rat";
                    else if (Cat.jumpscareTime == 0) gameoverReason = "Died to Cat";
                    else gameoverReason = "Died to Vinnie";
                } else if (Menu.nightType == 1 && (ShadowRat.jumpscareTime == 0 || ShadowCat.jumpscareTime == 0)) {
                    gameoverTrue = true;
                    if (ShadowRat.jumpscareTime == 0) gameoverReason = "Died to Shadow Rat";
                    else gameoverReason = "Died to Shadow Cat";
                } else if (Menu.nightType == 2 && Monstergami.jumpscareTime == 0) {
                    gameoverTrue = true;
                    gameoverReason = "Died to Monstergami";
                    zoomCharacter = 1;
                    gameoverAlpha = 0;
                    monstergamiFrames = 9.99f;
                    monstergamiPositive = false;
                    monstergamiTimes = 0;
                    monstergamiCooldown = 3 + random.nextInt(4);
                }

                if (gameoverTrue) {
                    gameover = true;
                    Discord.updateStatus = true;
                    gameoverScreenAlpha = 1;
                    Player.battleOverlay = 0;
                    Player.overlayTransparency = 0;
                    Player.scared = false;
                    Player.shakingPosition = 0;
                    Player.roomPosition[0] = 0;
                    Player.roomPosition[1] = 0;
                    audioClass.stopAllSounds();
                    if (zoomCharacter == 1) {
                        if (Menu.nightType == 0) audioClass.play("gameoverCustomNight");
                        else {
                            audioClass.play("scaryImpact");
                            audioClass.play("monstergami");
                            audioClass.loop("monstergami", true);
                        }
                    }
                } else {
                    if (!Player.freeze) {
                        Player.scared = false;
                        Player.buttonVisibility = 0;
                        Player.battleOverlay = 0;
                        Player.overlayTransparency = 0;
                        Player.disableTape();
                        Player.freeze = true;
                    }
                }
            }
        }

        camera.position.x = viewport.getWorldWidth() / 2 + Player.roomPosition[0] + Player.shakingPosition;
        camera.position.y = viewport.getWorldHeight() / 2 + Player.roomPosition[1];
        camera.update();
    }

    private static void gameoverRender(SpriteBatch batch, Viewport viewport) {

        screenBuffer.begin();

        batch.setColor(0, 0, 0, 1);
        batch.draw(FNaC3Deluxe.shapeBuffer.getColorBufferTexture(), 0, 0);

        switch (Menu.nightType){
            case 0 -> {
                batch.setColor(1, 1, 1, 1);
                String name = null;
                if (Rat.jumpscareI != 0) {
                    name = "game/gameover/rat";
                    if (zoomCharacter == 0) name += 2;
                    else name += 1;
                } else if (Cat.jumpscareI != 0) {
                    name = "game/gameover/cat";
                    if (zoomCharacter == 0) name += 2;
                    else name += 1;
                } else if (Vinnie.jumpscareI != 0) {
                    name = "game/gameover/vinnie";
                    if (zoomCharacter == 0) name += 2;
                    else name += 1;
                }
                if (name != null) {
                    Texture texture = ImageHandler.images.get(name);

                    float width = texture.getWidth() * 8;
                    float height = texture.getHeight() * 8;

                    float zoom = (float) Math.sin(Math.toRadians(zoomCharacter * 90)) * 2;
                    width /= (3 - zoom);
                    height /= (3 - zoom);

                    float position = (float) Math.sin(Math.toRadians(gameoverAlpha * 90));

                    float x = viewport.getWorldWidth() / 2 - width / 2;
                    float y = viewport.getWorldHeight() / 2 - height / 2 + (position * 150);
                    batch.draw(texture, x, y, width, height);
                }
            }
            case 1 -> {
                batch.setColor(0.5f, 0.5f, 0.5f, 1);
                if (ShadowRat.jumpscareI != 0) batch.draw(ImageHandler.images.get("game/gameover/shadowRat"), 0, 0);
                else if (ShadowCat.jumpscareI != 0) batch.draw(ImageHandler.images.get("game/gameover/shadowCat"), 0, 0);
            }
            case 2 -> {
                batch.setColor(1, 1, 1, 1);
                Texture texture = ImageHandler.images.get("game/gameover/monstergami" + (10 - (int) monstergamiFrames));
                float position = (float) Math.sin(Math.toRadians(gameoverAlpha * 90));
                float x = viewport.getWorldWidth() / 2 - (float) texture.getWidth() / 2;
                float y = viewport.getWorldHeight() / 2 - (float) texture.getHeight() / 2 + (position * 200);

                batch.draw(texture, x, y);
            }
        }

        if (Menu.nightType == 0){
            batch.setColor(1f, 0, 0, 1);
        } else if (Menu.nightType == 1){
            batch.setColor(0.4f, 0, 1f, 1);
        } else {
            batch.setColor(0.35f, 0.175f, 0.95f, 1);
        }

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_DST_COLOR);

        Texture texture = ImageHandler.images.get("Static/GameoverStatic" + ((int) gameoverStatic + 1));
        batch.draw(texture, 0, 0);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        batch.setBlendFunction(srcFunc, dstFunc);

        if (Menu.nightType == 0){
            gameoverFont.setColor(1, 0, 0, gameoverAlpha);
        } else if (Menu.nightType == 1){
            gameoverFont.setColor(0.4f, 0, 1f, gameoverAlpha);
        } else {
            gameoverFont.setColor(0.35f, 0.175f, 0.95f, gameoverAlpha);
        }

        gameoverFont.draw(batch, "GAME OVER", 363.5f, 424);

        if (retryButton) {
            gameoverSelectionsFont.setColor(1, 1, 1, gameoverAlpha);
        } else {
            if (Menu.nightType == 0){
                gameoverSelectionsFont.setColor(1, 0, 0, gameoverAlpha);
            } else if (Menu.nightType == 1){
                gameoverSelectionsFont.setColor(0.4f, 0, 1, gameoverAlpha);
            } else {
                gameoverSelectionsFont.setColor(0.35f, 0.175f, 0.95f, gameoverAlpha);
            }
        }
        gameoverSelectionsFont.draw(batch, "Retry", 396, 300);

        if (menuButton) {
            gameoverSelectionsFont.setColor(1, 1, 1, gameoverAlpha);
        } else {
            if (Menu.nightType == 0){
                gameoverSelectionsFont.setColor(1, 0, 0, gameoverAlpha);
            } else if (Menu.nightType == 1){
                gameoverSelectionsFont.setColor(0.4f, 0, 1, gameoverAlpha);
            } else {
                gameoverSelectionsFont.setColor(0.35f, 0.175f, 0.95f, gameoverAlpha);
            }
        }
        gameoverSelectionsFont.draw(batch, "Menu", 573, 300);

        batch.flush();

        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        if (Menu.nightType == 0){
            batch.setColor(0.5f, 0, 0, gameoverScreenAlpha);
        } else if (Menu.nightType == 1){
            batch.setColor(0.2f, 0, 0.5f, gameoverScreenAlpha);
        } else {
            batch.setColor(0.1f, 0.0875f, 0.475f, gameoverScreenAlpha);
        }
        batch.draw(texture, Player.roomPosition[0], Player.roomPosition[1]);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(1, 1, 1, screenAlpha);
        batch.draw(texture, 0, texture.getHeight(), texture.getWidth(), -texture.getHeight());
        batch.setColor(1, 1, 1, 1);
    }

    public static void render(SpriteBatch batch, Viewport viewport, Data data){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (gameover && fontsAdded){
            gameoverRender(batch, viewport);
            return;
        }

        Texture texture = null;
        String room = null;
        boolean jumpscare = false;
        boolean side = false;
        if (Menu.nightType == 0){
            jumpscare = Rat.jumpscare || Cat.jumpscare || Vinnie.jumpscare;
            side = Rat.jumpscareType.equals("sideJumpscare") || Cat.jumpscareType.equals("sideJumpscare")
                    || Vinnie.jumpscareType.equals("vinnieSideJumpscare");
        } else if (Menu.nightType == 1){
            jumpscare = ShadowRat.jumpscare || ShadowCat.jumpscare;
        } else if (Menu.nightType == 2){
            jumpscare = Monstergami.jumpscare;
        }

        batch.setColor(1, 1, 1, 1);

        if (!jumpscare || side) {
            if (Player.turningAround) {
                room = switch (Player.room) {
                    case 0 -> "game/room/Moving/Turn Around/Moving" + (int) Player.turningPosition;
                    case 1 -> "game/room/Moving/Under Bed/Moving" + (int) Player.turningPosition;
                    case 2 -> "game/room/Moving/Turn Back/Moving" + (int) Player.turningPosition;
                    default -> null;
                };
            } else {
                switch (Player.room) {
                    case 0:
                        room = "game/room/FullRoom";
                        fbo = Player.flashlightPrep(batch, fbo, viewport, data, room);
                        break;
                    case 1:
                        room = "game/room/UnderBed";
                        fbo = Player.flashlightPrep(batch, fbo, viewport, data, room);
                        break;
                    case 2:
                        if (Player.tapeStolen){
                            room = "game/room/Tape/TapeMissing";
                        } else {
                            room = "game/room/Tape/Tape";
                        }
                        break;
                }
            }
        }

        if (roomBuffer == null){
            roomBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }

        roomBuffer.begin();

        if (!jumpscare || side) {
            batch.draw(ImageHandler.images.get(room), 0, 0);
            if (Player.room == 0 && !Player.turningAround && Menu.nightType == 1){
                batch.draw(ImageHandler.images.get("game/room/origami"), 1874, 391);
            }
        }

        if (Menu.nightType == 0) {
            if (Rat.ai != 0 && !Rat.jumpscare) {
                Rat.render(batch);
            }

            if (Vinnie.ai != 0 && !Vinnie.jumpscare){
                Vinnie.render(batch);
            }

            if (Monstergami.active && !Monstergami.jumpscare) {
                Monstergami.render(batch);
            }

            if (Cat.ai != 0 && !Cat.jumpscare) {
                Cat.render(batch);
            }
        } else if (Menu.nightType == 1) {
            if (ShadowRat.active && ShadowCat.jumpscareI == 0) {
                ShadowRat.render(batch);
            }

            if (Monstergami.active && !Monstergami.jumpscare) {
                Monstergami.render(batch);
            }

            if (ShadowCat.active && ShadowRat.jumpscareI == 0) {
                if (!ShadowCat.render(batch) && !Player.turningAround && Player.room == 0){
                    batch.draw(ImageHandler.images.get("game/room/FullRoomBed"), 0, 0);
                }
            }
        } else if (Menu.nightType == 2){
            if (Monstergami.active && !Monstergami.jumpscare) {
                Monstergami.render(batch);
            }
        }

        batch.flush();

        if (!jumpscare || side) {
            if (Player.room != 2 && !Player.turningAround) {
                Player.flashlightRender(batch, fbo);
            } else if (!Player.turningAround) {
                String tapeButtons = null;
                if (Player.stopPosition == 0) {
                    if (Player.playPosition > 0) {
                        tapeButtons = "Play" + (int) Player.playPosition;
                    } else if (Player.rewindPosition > 0) {
                        tapeButtons = "Rewind" + (int) Player.rewindPosition;
                    }
                } else {
                    if (Player.stopPosition >= 2) {
                        tapeButtons = "Stop" + (int) Player.stopPosition;
                    } else {
                        if ((int) (Player.playPosition) != 0) {
                            tapeButtons = "StopPlay1";
                        } else if ((int) (Player.rewindPosition) != 0) {
                            tapeButtons = "StopRewind1";
                        }
                    }
                }

                if (tapeButtons != null) {
                    batch.draw(ImageHandler.images.get("game/room/Tape/Buttons/" + tapeButtons),
                            416, 162);
                }

                batch.setColor(1, 1, 1, 1 - Player.flashlightAlpha);
                batch.draw(ImageHandler.images.get("game/room/Moving/Turn Back/Moving1"), 0, 0);
                batch.setColor(1, 1, 1, 1);

                if (Player.batteryAvailable) {
                    texture = ImageHandler.images.get("game/room/Tape/battery");
                    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    batch.setColor(0.2f + Player.flashlightAlpha / 1.25f, 0.2f + Player.flashlightAlpha / 1.25f, 0.2f + Player.flashlightAlpha / 1.25f, 1);
                    batch.draw(texture, 0, 0);
                    batch.setColor(1, 1, 1, 1);
                }
            }
        }

        if (Menu.nightType == 0) {
            if (Rat.ai != 0 && Rat.jumpscare) {
                Rat.render(batch);
            }

            if (Vinnie.ai != 0 && Vinnie.jumpscare){
                Vinnie.render(batch);
            }

            if (Cat.ai != 0 && Cat.jumpscare) {
                Cat.render(batch);
            }
        } else if (Menu.nightType == 1) {
            if (ShadowRat.active && ShadowRat.jumpscare) {
                ShadowRat.render(batch);
            }

            if (ShadowCat.active && ShadowCat.jumpscare) {
                ShadowCat.render(batch);
            }
        } else if (Menu.nightType == 2){
            if (Monstergami.active && Monstergami.jumpscare) {
                Monstergami.render(batch);
            }
        }

        batch.flush();
        roomBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        if (screenBuffer == null){
            screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }

        screenBuffer.begin();

        float position = Player.roomPosition[0] + Player.shakingPosition;

        roomRegion.setRegion(roomBuffer.getColorBufferTexture());
        roomRegion.flip(false, true);
        if (Menu.nightType == 2){
            batch.setColor(0.7f, 0.7f, 1, 1);
        }
        batch.draw(roomRegion, position, Player.roomPosition[1]);

        batch.setColor(1, 1, 1, 1);

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();

        batch.setBlendFunction(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE);

        texture = switch (Player.lastCharacterAttack) {
            case "Vinnie" -> ImageHandler.images.get("game/VinnieBattleOverlay");
            case "Shadow" -> ImageHandler.images.get("game/ShadowBattleOverlay");
            case "RatCat" -> ImageHandler.images.get("game/RatCatBattleOverlay");
            case "Monstergami" -> ImageHandler.images.get("game/MonstergamiBattleOverlay");
            default -> texture;
        };

        if (texture != null) {
            float alpha = -0.5f + Player.overlayTransparency + Player.battleOverlay;
            batch.setColor(1, 1, 1, 1 - alpha);
            batch.draw(texture, Player.roomPosition[0] + Player.shakingPosition, Player.roomPosition[1]);
            batch.setColor(1, 1, 1, 1);
        }

        batch.flush();
        batch.end();
        batch.enableBlending();
        batch.begin();
        batch.setBlendFunction(srcFunc, dstFunc);

        batch.setColor(0, 0, 0, 1 - Player.blackness);
        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        batch.draw(texture, position, Player.roomPosition[1]);

        batch.setColor(0.2f, 0, 0.2f, purpleTime / purpleTimeTarget);
        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        batch.draw(texture, position, Player.roomPosition[1]);

        batch.setColor(1, 1, 1, 1);
        batch.flush();

        float leftButton = position + 46;
        float rightButton = leftButton + (float) 1024 / 2;
        float middleButton = leftButton + (float) 1024 / 4;
        switch (Player.room){
            case 0:
                texture = ImageHandler.images.get("game/Buttons/TapePlayer");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, leftButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);

                texture = ImageHandler.images.get("game/Buttons/UnderBed");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, rightButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);
                break;
            case 1:
                texture = ImageHandler.images.get("game/Buttons/UnderBedBack");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, middleButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);
                break;
            case 2:
                texture = ImageHandler.images.get("game/Buttons/TapePlayerBack");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, middleButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);
                break;
        }

        if (Menu.nightType != 2) {
            texture = ImageHandler.images.get("game/time/" + hourOfGame + "AM");
            batch.draw(texture, 916 + position, 724 + Player.roomPosition[1]);
        } else {
            gameoverSelectionsFont.setColor(1, 1, 1, 1);
            layout.reset();
            layout.setText(gameoverSelectionsFont, nightTime);
            gameoverSelectionsFont.draw(batch, layout,
                    1024 - layout.width - 16 + position,
                    768 - layout.height + Player.roomPosition[1]);
        }

        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(1, 1, 1, screenAlpha);
        batch.draw(texture, position, Player.roomPosition[1] + texture.getHeight(),
                texture.getWidth(), -texture.getHeight());
        batch.setColor(1, 1, 1, 1);

        batch.flush();

        if (!win) {

            if (data.flashDebug && Player.snapPosition) {
                texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
                float offsety = Player.roomPosition[1];
                float width = viewport.getWorldWidth() / 4;

                batch.setColor(0.15f, 0, 0.2f, 1);
                batch.draw(texture, position + width - 4, offsety + 106, width * 2 + 8, 28);

                batch.setColor(0.35f, 0, 0, 1);
                batch.draw(texture, position + width, offsety + 110, width * 2, 20);

                batch.setColor(0.75f, 0, 0.65f, 1);

                float rect_value = 0;

                if (Menu.nightType == 0){
                    if (Rat.room == 1) {
                        rect_value = Math.min(Rat.patienceHealthTimer, 2) / 2;
                    } else if (Vinnie.room == 1) {
                        rect_value = Math.min(Vinnie.patienceHealthTimer, 2) / 2;
                    }
                } else if (Menu.nightType == 1){
                    if (ShadowRat.room == 1) {
                        rect_value = Math.min(ShadowRat.patienceHealthTimer, 2) / 2;
                    } else {
                        rect_value = Math.min(ShadowCat.patienceHealthTimer, 2) / 2;
                    }
                }

                if (Monstergami.active && Monstergami.side != -1){
                    rect_value = Math.min(Monstergami.patienceHealthTimer, 2) / 2;
                }
                batch.draw(texture, position + width, offsety + 110, (width * 2) * rect_value, 20);
            }

            if (data.hitboxDebug && !Player.turningAround && !Player.freeze) {
                batch.setColor(1, 0, 1, 0.25f);

                if (Menu.nightType == 0 && Player.room == 0){
                    hitboxRender(batch, Rat.hitboxDistance, Rat.hitboxPosition[0], Rat.hitboxPosition[1]);
                    hitboxRender(batch, Cat.hitboxDistance, Cat.hitboxPosition[0], Cat.hitboxPosition[1]);
                    hitboxRender(batch, Vinnie.hitboxDistance, Vinnie.hitboxPosition[0], Vinnie.hitboxPosition[1]);
                } else if (Menu.nightType == 1 && Player.room == 0){
                    hitboxRender(batch, ShadowRat.hitboxDistance, ShadowRat.hitboxPosition[0], ShadowRat.hitboxPosition[1]);
                    hitboxRender(batch, ShadowCat.hitboxDistance, ShadowCat.hitboxPosition[0], ShadowCat.hitboxPosition[1]);
                }

                if (Monstergami.active
                        && ((Monstergami.side == 3 && Player.room == 1)
                        || (Monstergami.side != -1 && Monstergami.side != 3 && Player.room == 0))){
                    hitboxRender(batch, Monstergami.hitboxDistance, Monstergami.hitboxPosition[0], Monstergami.hitboxPosition[1]);
                }
                batch.setColor(1, 1, 1, 1);
            }
        } else {
            texture = ImageHandler.images.get("Clock/Clock" + ((int) clockAnimation + 1));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            batch.setColor(1, 1, 1, winAlpha);
            batch.draw(texture,
                    position + viewport.getWorldWidth() / 2 - (float) texture.getWidth() / 2,
                    Player.roomPosition[1] + viewport.getWorldHeight() / 2 - (float) texture.getHeight() / 2);
            batch.setColor(1, 1, 1, 1);
        }
    }

    private static void hitboxRender(SpriteBatch batch, float hitboxDistance, float hitboxX, float hitboxY){
        if (hitboxX == -1) return;
        float size = hitboxDistance * 2;
        batch.draw(FNaC3Deluxe.circleShapeBuffer.getColorBufferTexture(),
                hitboxX - size / 2, hitboxY - size / 2, size, size);
    }
}
