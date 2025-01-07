package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class Cat {

    private static String catBreath;
    public static int ai;
    public static int room;
    public static int side = -1;
    public static float cooldownTimer;
    public static boolean shaking;
    public static boolean dontSoundShake;
    public static boolean twitchingNow;
    public static float timeToFlash;
    public static boolean bedSpotted;
    public static float bedPatienceTimer;
    public static float bedPosition;
    public static float bedPositionTarget;
    public static boolean jumpscare;
    public static String jumpscareType;
    public static float jumpscareTimer;
    public static float jumpscareTime;
    public static int jumpscareI;
    public static float cooldown;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static boolean hitboxHit;

    public static void input(){
        if (jumpscareI == 0 && !Player.turningAround && !Player.freeze && Player.room == 0 && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Random random, Data data, AudioClass audioClass){
        if (jumpscareI == 0) {
            float time = Gdx.graphics.getDeltaTime() * 60;
            float volume = audioClass.getVolume("cat");
            if (room == 1){
                if (volume < 1) {
                    volume += 0.0125f * time;
                    if (volume > 1) {
                        volume = 1;
                    }
                }
            } else {
                if (volume > 0.4f) {
                    volume -= 0.0125f * time;
                    if (volume < 0.4f) {
                        volume = 0.4f;
                    }
                }
            }

            audioClass.setVolume("cat", volume);

            switch (room) {
                case 2:
                    bedMechanic(random, audioClass);
                    break;
                case 4:
                    farBedMechanic(random, audioClass);
                    break;
            }
            if (jumpscareI != 0){
                audioClass.stopAllSounds();
            }
        } else if (!jumpscare){
            jumpscareType = "bedJumpscare";
            Player.blacknessTimes = 1;
            Player.blacknessMultiplier = 5;
            jumpscareTime = 1.5f;
            audioClass.play(jumpscareType);
            jumpscare = true;
            jumpscareTimer = 0.0375f;
        } else if (jumpscareTime > 0){
            jumpscareTime -= Gdx.graphics.getDeltaTime();
            if (jumpscareTime <= 0) {
                jumpscareTime = 0;
                audioClass.stop(jumpscareType);
            }
            if (jumpscareTimer > 0){
                jumpscareTimer -= Gdx.graphics.getDeltaTime();
                if (jumpscareTimer <= 0){
                    jumpscareTimer += 0.0375f;
                    jumpscareI++;
                    if (jumpscareI > 29){
                        jumpscareI = 29;
                    }
                }
            }
        }

        hitboxes(data);
    }

    public static void hitboxCollided(){
        float mx = Player.flashlightPosition[0];
        float my = Player.flashlightPosition[1];
        float lineA = Math.abs(mx - hitboxPosition[0]);
        float lineB = Math.abs(my - hitboxPosition[1]);

        if (Math.hypot(lineA, lineB) <= hitboxDistance){
            if (room == 4 && timeToFlash > 0){
                hitboxHit = true;
            }
        }
    }

    public static void hitboxes(Data data){
        hitboxHit = false;
        hitboxPosition[0] = -1;
        hitboxPosition[1] = -1;
        hitboxDistance = 0;
        if (room == 4) {
            if (side == 0) {
                hitboxDistance = 75;
                if (bedPosition == 23) {
                    hitboxPosition[0] = 191;
                    hitboxPosition[1] = 548;
                } else if (bedPosition == 33) {
                    hitboxPosition[0] = 214;
                    hitboxPosition[1] = 651;
                } else if (bedPosition == 44) {
                    hitboxPosition[0] = 178;
                    hitboxPosition[1] = 715;
                } else if (bedPosition == 57) {
                    hitboxPosition[0] = 186;
                    hitboxPosition[1] = 661;
                }
            } else {
                hitboxDistance = 80;
                if (bedPosition == 23) {
                    hitboxPosition[0] = 2908;
                    hitboxPosition[1] = 448;
                } else if (bedPosition == 33) {
                    hitboxPosition[0] = 2853;
                    hitboxPosition[1] = 523;
                } else if (bedPosition == 44) {
                    hitboxPosition[0] = 2838;
                    hitboxPosition[1] = 557;
                } else if (bedPosition == 57) {
                    hitboxPosition[0] = 2804;
                    hitboxPosition[1] = 620;
                }
            }
        }

        if (data.laserPointer){
            if (data.expandedPointer) {
                hitboxDistance *= 0.8f;
            } else {
                hitboxDistance *= 0.6f;
            }
        } else if (data.expandedPointer){
            hitboxDistance *= 1.2f;
        }
    }

    public static void render(SpriteBatch batch) {
        String texture = null;
        float x = 0;
        float y = 0;

        if (!jumpscare) {
            boolean lookInRoom = false;
            boolean lookUnderBed = false;

            switch (room) {
                case 2:
                    if (Player.room == 1 && Monstergami.side != 3) {
                        lookUnderBed = true;
                        if (side == 0) {
                            texture = "Under Bed/Bed1";
                            y = 194;
                        } else if (side == 2){
                            texture = "Under Bed/Bed2";
                            x = 1163;
                            y = 165;
                        }
                    }
                    break;
                case 4:
                    lookInRoom = true;
                    if (side == 0){
                        texture = "Retreat/Retreat Left/Retreat" + (58 - (int) bedPosition);
                        y = 216;
                    } else {
                        texture = "Retreat/Retreat Right/Retreat" + (58 - (int) bedPosition);
                        x = 2532;
                        y = 218;
                    }
                    break;
            }
            if (texture == null) return;
            boolean passed = lookInRoom && !Player.turningAround && Player.room == 0;
            if (!passed) {
                passed = lookUnderBed && !Player.turningAround && Player.room == 1;
            }

            if (!passed) return;
        } else {
            texture = "Jumpscare/Bed Jumpscare/Jumpscare" + jumpscareI;
            x = Player.roomPosition[0] + Player.shakingPosition;
            y = Player.roomPosition[1];
        }
        batch.setColor(1, 1, 1, 1);
        batch.draw(ImageHandler.images.get("game/Cat/" + texture), x, y);
    }

    public static void reset(Random random, AudioClass audioClass){
        jumpscareType = "";
        if (hitboxPosition == null){
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }
        cooldown = 6 + 0.3f * (20 - ai);
        jumpscareTime = -1;
        cooldownTimer = 8 + 0.3f * (20 - ai);
        bedPatienceTimer = 2;
        room = 2;
        bedSpotted = false;
        jumpscare = false;
        side = 2 * random.nextInt(2);
        shaking = false;
        dontSoundShake = false;
        twitchingNow = false;
        hitboxHit = false;
        jumpscareI = 0;
        if (ai != 0) {
            audioClass.play("cat");
            audioClass.setVolume("cat", 0.4f);
            audioClass.loop("cat", true);
            audioClass.play("crawl");
        }
    }

    public static void bedMechanic(Random random, AudioClass audioClass){
        if (side == -1){
            if (Rat.room != 2){
                if (Vinnie.side == 0){
                    side = 2;
                } else {
                    side = 0;
                }
            } else if (Vinnie.room != 2){
                if (Rat.side == 0){
                    side = 2;
                } else {
                    side = 0;
                }
            }
        } else if (cooldownTimer > 0 && !Player.freeze) {
            if (Vinnie.room == 1 || Monstergami.side != -1) {
                cooldownTimer -= Gdx.graphics.getDeltaTime() / 2;
            } else {
                cooldownTimer -= Gdx.graphics.getDeltaTime();
            }

            if (bedPatienceTimer > 0 && Player.room == 1 && Monstergami.side != 3) {
                bedPatienceTimer -= Gdx.graphics.getDeltaTime();
                if (bedPatienceTimer < 0) {
                    bedPatienceTimer = 0;
                }
            }

            if (Player.room == 1 && !Player.turningAround && bedPatienceTimer == 0) {
                jumpscareI = 1;
            }

            if (cooldownTimer <= 0 && Player.room != 1) {
                room = 4;
                cooldownTimer = cooldown;
                audioClass.play("catPeek");
                timeToFlash = 1;
                bedPosition = 0;
                side = 2 * random.nextInt(2);
                if (side == 0){
                    catBreath = "catLeft";
                } else {
                    catBreath = "catRight";
                }
                audioClass.play(catBreath);
                audioClass.loop(catBreath, true);
                audioClass.setVolume(catBreath, 0);
                bedPositionTarget = 23;
            } else if (Player.room == 1) cooldownTimer += Gdx.graphics.getDeltaTime();
        }
    }

    public static void farBedMechanic(Random random, AudioClass audioClass){
        audioClass.setVolume(catBreath, (bedPosition / 57) * 0.75f);
        shaking = hitboxHit;

        if (shaking && cooldownTimer > 0){
            timeToFlash -= Gdx.graphics.getDeltaTime();
            cooldownTimer += Gdx.graphics.getDeltaTime();

            if (timeToFlash <= 0){
                timeToFlash = 0;
                bedPositionTarget = 0;
            }
        } else if (cooldownTimer > 0 && timeToFlash > 0){
            timeToFlash += Gdx.graphics.getDeltaTime();
            if (timeToFlash > 1f){
                timeToFlash = 1f;
            }

            if (!Player.freeze) {
                if (Vinnie.room == 1) {
                    cooldownTimer -= Gdx.graphics.getDeltaTime() / 2;
                } else {
                    cooldownTimer -= Gdx.graphics.getDeltaTime();
                }
            }
        } else if (cooldownTimer <= 0 && bedPosition == bedPositionTarget) {
            if (bedPositionTarget == 23) {
                bedPositionTarget = 33;
            } else if (bedPositionTarget == 33) {
                bedPositionTarget = 44;
            } else if (bedPositionTarget == 44) {
                bedPositionTarget = 57;
            } else {
                jumpscareI = 1;
            }

            if (room != 0) {
                cooldownTimer = cooldown;
            }
        }

        if (bedPosition != bedPositionTarget){
            if (bedPosition < bedPositionTarget){
                bedPosition += Gdx.graphics.getDeltaTime() * 45;
                if (bedPosition > bedPositionTarget){
                    bedPosition = bedPositionTarget;
                }
            } else {
                bedPosition -= Gdx.graphics.getDeltaTime() * 60;
                if (bedPosition < bedPositionTarget){
                    bedPosition = bedPositionTarget;
                }
            }
        }

        if (bedPositionTarget == 0 && bedPosition == 0){
            room = 2;
            audioClass.stop(catBreath);
            bedSpotted = false;
            bedPatienceTimer = 2 + 0.25f * (20 - ai);

            if (Rat.room == 2 && Vinnie.room == 2) {
                side = -1;
            } else {
                if (Rat.room == 2) {
                    if (Rat.side == 0) {
                        side = 2;
                    } else {
                        side = 0;
                    }
                } else if (Vinnie.room == 2) {
                    if (Vinnie.side == 0) {
                        side = 2;
                    } else {
                        side = 0;
                    }
                } else {
                    side = 2 * random.nextInt(2);
                }
                audioClass.stop("catPulse");
                cooldownTimer = 8 + 0.3f * (20 - ai);
            }
        }
    }
}
