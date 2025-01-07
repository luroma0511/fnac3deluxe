package com.fnac3.deluxe.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.enemy.*;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.state.Menu;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class Player {

    public static String lastCharacterAttack;
    public static boolean batteryAvailable;
    public static boolean batterySound;
    public static boolean flashlightStolen;
    public static float flashlightTimer;
    public static float flickerTimer;
    public static float flickerMultiplier;
    public static float flashlightAlpha;
    public static float flashlightAlphaVisibility;
    public static boolean flashlightShake;
    public static boolean foundUnderBed;
    public static boolean foundUnderBedLock;
    public static boolean scared;
    public static float buttonVisibility;
    public static float[] roomPosition = new float[2];
    public static float roomPositionSave;
    public static float[] flashlightPosition = new float[]{0, 0};
    public static boolean turningAround;
    public static float turningPosition;
    public static int roomTarget;
    public static int room;
    public static int side;
    public static boolean justStarted;
    public static float shakingPosition;
    public static boolean shakePositive;
    public static boolean tapeStolen;
    public static boolean tapePlay;
    public static boolean tapeStop;
    public static boolean tapeRewind;
    public static float playPosition;
    public static float stopPosition;
    public static float rewindPosition;
    public static boolean tapeEnd;
    public static float battleOverlay;
    public static float overlayTransparency;
    public static int blacknessTimes;
    public static float blackness;
    public static float blacknessDelay;
    public static float blacknessMultiplier;
    public static boolean freeze;
    public static boolean snapPosition;
    public static float[] snapTo = new float[]{-1, -1};
    public static int snapToSide;

    public static Music tape;
    public static float tapePosition;

    public static void reset(){
        tapeStolen = false;
        lastCharacterAttack = "";
        tapeEnd = false;
        if (tape != null) tape.stop();
        tape = Gdx.audio.newMusic(Gdx.files.local("assets/sounds/" + (Menu.nightType == 2 ? "monstergamiTape" : "tapemusic") + ".wav"));
        side = 1;
        justStarted = true;
        snapPosition = false;
        batteryAvailable = false;
        flashlightStolen = false;
        flashlightTimer = 40;
        flashlightAlphaVisibility = 1;
        flashlightAlpha = 1;
        flickerMultiplier = 0.25f;
        flashlightShake = false;
        flashlightPosition[0] = 1024;
        flashlightPosition[1] = 128;
        freeze = false;
        foundUnderBed = false;
        foundUnderBedLock = false;
        blackness = 1;
        blacknessTimes = 0;
        blacknessMultiplier = 6;
        blacknessDelay = 0;
        overlayTransparency = 0;
        battleOverlay = 0;
        tapePosition = 0;
        playPosition = 0;
        rewindPosition = 0;
        stopPosition = 0;
        tapePlay = false;
        tapeStop = false;
        tapeRewind = false;
        shakingPosition = 0;
        shakePositive = false;
        room = 0;
        roomPosition[0] = 1024;
        roomPosition[1] = 128;
        turningAround = false;
        turningPosition = 0;
        roomTarget = 0;
        scared = false;
        buttonVisibility = 0;
    }

    public static void blackness(){
        if (blacknessTimes > 0 && blackness >= 0.75f){
            blacknessTimes--;
            blackness = 0;
            return;
        }

        if (blacknessTimes == 0 && blacknessDelay > 0){
            blacknessDelay -= Gdx.graphics.getDeltaTime();
            if (blacknessDelay <= 0){
                blacknessDelay = 0;
                freeze = false;
            }
        } else if (blackness < 1){
            blackness += Gdx.graphics.getDeltaTime() * blacknessMultiplier;
            if (blackness > 1){
                blackness = 1;
            }
        }
    }

    public static void buttonVisibility(){
        if (roomPosition[1] == 0 && !turningAround && !Player.freeze){
            if (buttonVisibility < 1) {
                buttonVisibility += Gdx.graphics.getDeltaTime() * 4;
                if (buttonVisibility > 1) {
                    buttonVisibility = 1;
                }
            }
        } else {
            if (buttonVisibility > 0) {
                buttonVisibility -= Gdx.graphics.getDeltaTime() * 4;
                if (buttonVisibility < 0) {
                    buttonVisibility = 0;
                }
            }
        }
    }

    public static boolean inititiateSnapPosition(int side){
        if (side == 0 && Player.roomPosition[0] < 450 && Player.flashlightPosition[0] < 600) {
            Player.snapPosition = true;
            Player.snapToSide = side;
            return true;
        } else if (side == 2 && Player.roomPosition[0] > 1450 && Player.flashlightPosition[0] > 2300) {
            Player.snapPosition = true;
            Player.snapToSide = side;
            return true;
        } else if (side == 1 && Player.roomPosition[0] > 545 && Player.roomPosition[0] < 1585 && Player.flashlightPosition[0] >= 1300 && Player.flashlightPosition[0] <= 1850) {
            Player.snapPosition = true;
            Player.snapToSide = side;
            return true;
        }
        return false;
    }

    public static void batteryInput(float mx, float my){

        if (batteryAvailable) {
            my = 768 - my;

            int pixel = Game.batteryPixmap.getPixel(
                    (int) (mx - Player.roomPosition[0] - Player.shakingPosition),
                    (int) my) / 10_000;

            if (pixel == -1677) {
                flashlightTimer = 40;
                batteryAvailable = false;
                batterySound = true;
            }
        }
    }

    public static void flashlightFlickerMechanic(Data data, Random random){
        if (flashlightStolen){
            flashlightAlphaVisibility = 0;
            flickerTimer = 0;
            flashlightTimer = 0;
            batteryAvailable = false;
            flashlightAlpha = 0;
        } else if (data.faultyFlashlight){
            if (flashlightTimer > 0) {
                flashlightTimer -= Gdx.graphics.getDeltaTime();
                if (flashlightTimer < 0) {
                    flashlightTimer = 0;
                }
            }

            if (flashlightTimer <= 25 && !batteryAvailable) {
                batteryAvailable = true;
            }

            if (flashlightTimer <= 10) {

                if (flashlightTimer > 0) {
                    if (flickerTimer == 0) {
                        flickerTimer = 0.45f;
                        flickerMultiplier = (1 + random.nextInt(5)) * 0.075f + 0.5f;
                    }
                } else {
                    if (flashlightAlphaVisibility > 0) {
                        flashlightAlphaVisibility -= Gdx.graphics.getDeltaTime() * 2.5f;
                    }
                }
            }

            if (flashlightAlphaVisibility < 1 && flashlightTimer != 0) {
                flashlightAlphaVisibility += Gdx.graphics.getDeltaTime() * 4f;
            }

            if (flickerTimer > 0) {
                flickerTimer -= Gdx.graphics.getDeltaTime() * flickerMultiplier;
                if (flickerTimer < 0) {
                    flickerTimer = 0;
                }
            }

            flashlightAlpha = flashlightAlphaVisibility - flickerTimer;
        }
    }

    public static boolean mouseOver(float mx, float my, float x1, float x2, float y1, float y2){
        return mx >= x1 && mx <= x2 && my >= y1 && my <= y2;
    }

    public static boolean mouseOverEnemyInBed(){
        float mx = flashlightPosition[0];
        float my = flashlightPosition[1];
        Texture texture;
        int x = 0;
        int y = 179;

        if (flashlightAlpha == 0) return false;

        if (Menu.nightType == 0){
            if (Rat.room == 2 && !Rat.bedSpotted) {
                if (Rat.side == 0) {
                    texture = ImageHandler.images.get("game/Rat/Under Bed/Bed1");
                } else {
                    texture = ImageHandler.images.get("game/Rat/Under Bed/Bed2");
                    x = 1438;
                    y = 214;
                }

                Rat.bedSpotted = Player.mouseOver(mx, my,
                        x, x + texture.getWidth(),
                        y, y + texture.getHeight())
                        && Monstergami.side != 3 && !freeze;
                if (Rat.bedSpotted) {
                    return true;
                }
            }

            x = 0;
            y = 194;
            if (Cat.ai != 0 && Cat.room == 2 && !Cat.bedSpotted) {
                if (Cat.side == 0) {
                    texture = ImageHandler.images.get("game/Cat/Under Bed/Bed1");
                } else {
                    texture = ImageHandler.images.get("game/Cat/Under Bed/Bed2");
                    x = 1163;
                    y = 165;
                }

                Cat.bedSpotted = Player.mouseOver(mx, my,
                        x, x + texture.getWidth(),
                        y, y + texture.getHeight())
                        && Monstergami.side != 3 && !freeze;
                if (Cat.bedSpotted) {
                    return true;
                }
            }

            x = 0;
            y = 170;
            if (Vinnie.room == 2 && !Vinnie.bedSpotted) {
                if (Vinnie.side == 0) {
                    texture = ImageHandler.images.get("game/Vinnie/Under Bed/Bed1");
                } else {
                    texture = ImageHandler.images.get("game/Vinnie/Under Bed/Bed2");
                    x = 1249;
                    y = 195;
                }

                Vinnie.bedSpotted = Player.mouseOver(mx, my,
                        x, x + texture.getWidth(),
                        y, y + texture.getHeight())
                        && Monstergami.side != 3 && !freeze;
                return Vinnie.bedSpotted;
            }
        } else if (Menu.nightType == 1){
            if (ShadowRat.room == 2 && !ShadowRat.bedSpotted) {
                if (ShadowRat.side == 0) {
                    texture = ImageHandler.images.get("game/Shadow Rat/Under Bed/Bed1");
                } else {
                    texture = ImageHandler.images.get("game/Shadow Rat/Under Bed/Bed2");
                    x = 1438;
                    y = 214;
                }

                ShadowRat.bedSpotted = Player.mouseOver(mx, my,
                        x, x + texture.getWidth(),
                        y, y + texture.getHeight())
                        && Monstergami.side != 3 && !freeze;
                if (ShadowRat.bedSpotted) {
                    return true;
                }
            }

            x = 0;
            y = 167;
            if (ShadowCat.room == 2 && !ShadowCat.bedSpotted) {
                if (ShadowCat.side == 0) {
                    texture = ImageHandler.images.get("game/Shadow Cat/New/Under Bed/Bed1");
                } else {
                    texture = ImageHandler.images.get("game/Shadow Cat/New/Under Bed/Bed2");
                    x = 1094;
                }

                ShadowCat.bedSpotted = Player.mouseOver(mx, my,
                        x, x + texture.getWidth(),
                        y, y + texture.getHeight())
                        && Monstergami.side != 3 && !freeze;
                return ShadowCat.bedSpotted;
            }
        }
        return false;
    }

    public static void disableTape(){
        if (tape.isPlaying()) {
            tape.stop();
        }
    }

    public static boolean tapeCondition(String path, boolean tapeCondition, float mx, float my){
        my = 768 - my;

        int pixel = switch (path) {
            case "Play" -> {
                my -= 472;
                yield Game.playButton.getPixel((int) (mx - 433), (int) my);
            }
            case "Stop" -> {
                my -= 444;
                yield Game.stopButton.getPixel((int) (mx - 553), (int) my);
            }
            case "Rewind" -> {
                my -= 431;
                yield Game.rewindButton.getPixel((int) (mx - 611), (int) my);
            }
            default -> 0;
        };

        pixel /= 10_000;

        if (pixel == 0) return false;

        return (pixel == -1677) && tapeCondition;
    }

    public static void tapeFunctionality(Data data, AudioClass audioClass){

        boolean notWeaselDreamscape = Menu.nightType == 1 && !ShadowRat.tapeWeasel && !ShadowCat.tapeWeasel;
        boolean notWeaselCustomNight = Menu.nightType == 0 && !Rat.tapeWeasel && !Vinnie.tapeWeasel;
        boolean monstergamiNight = Menu.nightType == 2;

        if (Monstergami.tapeStolen && !tapeStolen){
            tapeStolen = true;
            audioClass.play("tapeTaken");
            Player.blacknessTimes = 2;
            Player.blacknessMultiplier = 6;

            if (!notWeaselCustomNight) {
                audioClass.stop("tapeWeasel");
            }

            Rat.tapeWeasel = false;
            Rat.tapePosition = 0;
            Rat.tapeSpotted = true;

            Vinnie.tapeWeasel = false;
            Vinnie.tapePosition = 0;
            Vinnie.tapeSpotted = true;

            if (tapePlay) {
                tapePosition = tape.getPosition();
                tape.stop();

                tapePlay = false;
                playPosition = 0;
            }

            if (tapeRewind) {
                audioClass.stop("tapeRewind");
                tapeRewind = false;
                rewindPosition = 0;
            }

            if (tapeStop) {
                tapeStop = false;
                stopPosition = 0;
            }
        }

        if (!tape.isPlaying() && !data.hardCassette){
            if (ShadowRat.timeUntilWeasel > 0){
                ShadowRat.timeUntilWeasel = 0;
            }

            if (ShadowCat.timeUntilWeasel > 0){
                ShadowCat.timeUntilWeasel = 0;
            }

            if (Rat.timeUntilWeasel > 0){
                Rat.timeUntilWeasel = 0;
            }

            if (Vinnie.timeUntilWeasel > 0){
                Vinnie.timeUntilWeasel = 0;
            }
        }

        if (tape.isPlaying()){
            tapePosition = tape.getPosition();
            if (!tapePlay || (Menu.nightType == 0 && (Rat.tapeWeasel || Vinnie.tapeWeasel))
                    || (Menu.nightType == 1 && (ShadowRat.tapeWeasel || ShadowCat.tapeWeasel))){
                tape.stop();
            }
        } else if (tapePlay && playPosition == 3 && (notWeaselDreamscape || notWeaselCustomNight || monstergamiNight)){
            tapePlay = false;
            tapeEnd = true;
            audioClass.play("tapeButton");
        }

        float constant = Gdx.graphics.getDeltaTime() * 30;

        if (tapePlay && playPosition < 3){
            if (playPosition == 0) {
                playPosition = 1;
                audioClass.play("tapeButton");
                tape.play();
                tape.setVolume(0.04f);
                tape.setPosition(tapePosition);
            } else {
                playPosition += constant;
                if (playPosition > 3){
                    playPosition = 3;
                }
            }
        } else if (!tapePlay && playPosition >= 1){
            playPosition -= constant;
            if (playPosition < 1){
                playPosition = 0;
            }
        }

        if (tapeRewind){
            if (tapePosition > 0) {
                tapePosition -= Gdx.graphics.getDeltaTime() * 3;
                if (tapePosition < 0) {
                    tapePosition = 0;
                }
            }
            if (rewindPosition < 3) {
                if (rewindPosition == 0) {
                    tapeEnd = false;
                    rewindPosition = 1;
                    audioClass.play("tapeButton");
                    audioClass.play("tapeRewind");
                    audioClass.loop("tapeRewind", true);
                } else {
                    rewindPosition += constant;
                    if (rewindPosition > 3) {
                        rewindPosition = 3;
                    }
                }
            } else if (tapePosition == 0){
                tapeRewind = false;
                audioClass.play("tapeButton");
                audioClass.stop("tapeRewind");
            }
        } else if (rewindPosition >= 1) {
            rewindPosition -= constant;
            if (rewindPosition < 1) {
                rewindPosition = 0;
            }
        }

        if (tapeStop && stopPosition < 4){
            if (stopPosition == 0) {
                stopPosition = 1;
                tape.stop();
                audioClass.stop("tapeWeasel");
                audioClass.play("tapeStop");
                audioClass.stop("tapeRewind");
            } else {
                stopPosition += constant;
                if (stopPosition >= 4){
                    stopPosition = 2.99f;
                    tapeStop = false;
                }
            }
        } else if (!tapeStop && stopPosition >= 1){
            stopPosition -= constant;
            if (stopPosition < 1){
                stopPosition = 0;
            }
        }
    }

    public static void buttons(float mx, float my){
        if (buttonVisibility > 0 && roomPosition[1] == 0 && !turningAround && !Player.freeze){
            boolean pressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
            float leftButton = Player.roomPosition[0] + Player.shakingPosition + 46;
            float rightButton = leftButton + (float) 1024 / 2;
            float middleButton = leftButton + (float) 1024 / 4;

            int pixel;
            switch (room) {
                case 0:
                    pixel = Game.tapeButtonPixmap.getPixel(
                            (int) (mx - 46 - Player.roomPosition[0] - Player.shakingPosition),
                            (int) my);

                    if (pressed && mouseOver(mx, my,
                            leftButton, leftButton + Game.tapeButtonPixmap.getWidth(),
                            0, Game.tapeButtonPixmap.getHeight())
                            && pixel != 0){
                        roomTarget = 2;
                        turningPosition = 1;
                    }

                    pixel = Game.bedButtonPixmap.getPixel((int) (mx - rightButton), (int) my);

                    if (pressed && mouseOver(mx, my,
                            rightButton, rightButton + Game.bedButtonPixmap.getWidth(),
                            0, Game.bedButtonPixmap.getHeight())
                            && pixel != 0){
                        roomTarget = 1;
                        turningPosition = 1;
                    }
                    break;
                case 1:
                    pixel = Game.bedBackButtonPixmap.getPixel((int) (mx - middleButton), (int) my);

                    if (pressed && mouseOver(mx, my,
                            middleButton, middleButton + Game.bedBackButtonPixmap.getWidth(),
                            0, Game.bedBackButtonPixmap.getHeight())
                            && pixel != 0){
                        roomTarget = 0;
                        turningPosition = 1;
                    }
                    break;
                case 2:
                    pixel = Game.tapeBackButtonPixmap.getPixel((int) (mx - middleButton), (int) my);

                    if (pressed && mouseOver(mx, my,
                            middleButton, middleButton + Game.tapeBackButtonPixmap.getWidth(),
                            0, Game.tapeBackButtonPixmap.getHeight())
                            && pixel != 0){
                        roomTarget = 0;
                        turningPosition = 1;
                    }
                    break;
            }
        }
    }

    public static void turningAround(AudioClass audioClass){
        float value = Gdx.graphics.getDeltaTime() * 30;
        if (room != roomTarget){
            if (turningPosition == 1) {
                if (room == 0) {
                    audioClass.play("movebed1");
                } else {
                    audioClass.play("movebed2");
                    if (room == 1){
                        audioClass.play("lookbed2");
                        audioClass.setVolume("lookbed2", 0.75f);
                    }
                }
            }
            turningPosition += value;
            int limit;
            if (room == 1){
                limit = 11;
            } else {
                limit = 12;
            }
            if (turningPosition >= limit){
                if (room == 2 && roomTarget == 0){
                    roomPosition[0] = roomPositionSave;
                    turningPosition = limit - 0.01f;
                } else if (room == 0 && roomTarget == 2){
                    roomPositionSave = roomPosition[0];
                    roomPosition[0] = 0;
                    turningPosition = limit - 0.01f;
                } else if (room == 0 && roomTarget == 1){
                    roomPosition[0] /= 2;
                    turningPosition = limit - 1.01f;
                    audioClass.play("lookbed1");
                    audioClass.setVolume("lookbed1", 0.75f);
                } else if (room == 1 && roomTarget == 0){
                    roomPosition[0] *= 2;
                    turningPosition = limit + 0.99f;
                }
                room = roomTarget;
            }
        } else if (turningPosition > 1){
            turningPosition -= value;
            if (turningPosition < 1){
                turningPosition = 0;
            }
        }
        boolean turning = turningAround;
        turningAround = turningPosition != 0;
        if (turning != turningAround){
            audioClass.play("flashlight");
        }
    }

    public static void move(float mx, float my, float viewportWidth, float viewportHeight){

        float value = Gdx.graphics.getDeltaTime() * 65;
        float distance;

        //If player is not facing the tape and not turning around
        if (room != 2 && !turningAround) {
            float leftBarrier = viewportWidth / 4;
            float rightBarrier = leftBarrier * 3;
            float limit = 21.5f;

            if (mx < leftBarrier && roomPosition[0] > 0) {
                distance = (leftBarrier - mx) / 8f;
                if (distance > limit) {
                    distance = limit;
                }

                distance *= value;

                roomPosition[0] -= distance;
                if (roomPosition[0] < 0) {
                    roomPosition[0] = 0;
                }
            } else if (mx > rightBarrier && roomPosition[0] < 2048) {
                distance = (mx - rightBarrier) / 8f;
                if (distance > limit) {
                    distance = limit;
                }
                distance *= value;

                roomPosition[0] += distance;
                if (room == 0) {
                    if (roomPosition[0] > 2048) {
                        roomPosition[0] = 2048;
                    }
                } else if (roomPosition[0] > 1024) {
                    roomPosition[0] = 1024;
                }
            }
        }

        //If player is facing the room and not turning around
        if (room == 0 && !turningAround) {
            float downBarrier = viewportHeight / 3;
            float upBarrier = downBarrier * 2;
            float limit = 12f;

            if (my < downBarrier && roomPosition[1] > 0) {
                distance = (downBarrier - my) / 5f;
                if (distance > limit) {
                    distance = limit;
                }
                distance *= value;

                roomPosition[1] -= distance;
                if (roomPosition[1] < 0) {
                    roomPosition[1] = 0;
                }
            } else if (my > upBarrier && roomPosition[1] < 256) {
                distance = (my - upBarrier) / 5f;
                if (distance > limit) {
                    distance = limit;
                }
                distance *= value;

                roomPosition[1] += distance;
                if (roomPosition[1] > 256) {
                    roomPosition[1] = 256;
                }
            }
        }
    }

    public static void moveTarget(){

        switch (snapToSide){
            case 0:
                snapTo[0] = 60;
                if (Vinnie.attack) snapTo[1] = 246;
                else snapTo[1] = 225;
                break;
            case 1:
                snapTo[0] = 1005;
                if (Vinnie.attack) snapTo[1] = 180;
                else snapTo[1] = 140;
                break;
            case 2:
                snapTo[0] = 1980;
                snapTo[1] = 250;
                break;
        }

        float distance = (snapTo[0] - roomPosition[0]);

        distance /= 10;

        if (distance > 45){
            distance = 45;
        }

        roomPosition[0] += distance * (Gdx.graphics.getDeltaTime() * 50);

        distance = (snapTo[1] - roomPosition[1]);

        distance /= 10;

        roomPosition[1] += distance * (Gdx.graphics.getDeltaTime() * 50);

    }

    public static void roomShake(){
        float limit = 3f;
        float position = shakingPosition;
        float speed = Gdx.graphics.getDeltaTime() * 45;
        if (shakePositive){
            position += speed;
            if (position >= limit){
                float remainder = position - limit;
                position = limit - remainder;
                shakePositive = false;
            }
        } else {
            position -= speed;
            if (position <= -limit){
                float remainder = position + limit;
                position = -limit - remainder;
                shakePositive = true;
            }
        }
        shakingPosition = position;
    }

    public static void flashlight(float mx, float my){
        flashlightPosition[0] = mx;
        flashlightPosition[1] = my;
    }

    public static void flashlightRender(SpriteBatch batch, FrameBuffer fbo){
        batch.end();
        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.enableBlending();
        batch.begin();
        batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_COLOR);

        Texture texture = fbo.getColorBufferTexture();
        batch.draw(texture, roomPosition[0] + shakingPosition, roomPosition[1] + texture.getHeight(),
                    texture.getWidth(), -texture.getHeight());

        batch.end();
        batch.begin();
        batch.setBlendFunction(srcFunc, dstFunc);
    }

    public static FrameBuffer flashlightPrep(SpriteBatch batch, FrameBuffer fbo, Viewport viewport, Data data, String room){

        if (fbo == null){
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }
        fbo.begin();

        batch.draw(ImageHandler.images.get(room + "Effect"), 0, 0);
        Texture texture = ImageHandler.images.get("game/Flashlight");

        float h = texture.getHeight();
        float w = texture.getWidth();

        float percentage = 1;
        //laser pointer
        if (data.laserPointer) {
            percentage -= 0.5f;
        }

        if (data.expandedPointer) {
            percentage += 0.25f;
        }

        w *= percentage;
        h *= percentage;

        batch.setColor(1, 1, 1, flashlightAlpha);
        batch.draw(texture,
                flashlightPosition[0] - w / 2,
                flashlightPosition[1] - h / 2,
                w, h);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        batch.end();
        fbo.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        return fbo;
    }
}
