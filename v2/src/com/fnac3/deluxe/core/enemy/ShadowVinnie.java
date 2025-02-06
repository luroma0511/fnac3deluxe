package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;
import com.fnac3.deluxe.core.util.Utils;

import java.util.Random;

public class ShadowVinnie {

    //general variables
    public static int ai;
    public static int room;
    public static int side = -1;
    public static float cooldownTimer;
    public static float twitchPosition;
    public static boolean shaking;
    public static boolean dontSoundShake;
    public static boolean twitchingNow;
    public static boolean dodgePlaying;
    public static float timeToFlash;
    public static float patienceTimer;

    //door variables
    public static boolean doorLock;
    public static float doorCooldown;
    public static float doorAnimation;
    public static boolean peekSpotted;
    public static boolean leaveRoom;

    //attack
    public static boolean attack;
    public static float attackPosition;
    public static float framesToMove;
    public static float attackTime;
    public static float patienceHealthTimer;
    public static int jumps;
    public static int jumpTarget;
    public static float jumpAnimation;
    private static int jumpCase;

    //bed
    public static boolean bedSpotted;
    public static float bedPatienceTimer;
    public static boolean tapeSpotted;
    public static float timeUntilWeasel;
    public static boolean tapeWeasel;
    public static float tapePosition;

    //jumpscares
    public static boolean jumpscare;
    public static String jumpscareType;
    public static float jumpscareTimer;
    public static float jumpscareTime;
    public static int jumpscareI;
    public static boolean pause;

    private static float vinnieLaughTime;
    private static int vinnielaugh;

    public static int knocks;
    public static boolean knockHard;
    public static float knockTimer;
    public static float knockDelay;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static boolean hitboxHit;

    public static void input(){
        if (jumpscareI == 0 && !Player.turningAround && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Random random, Data data, AudioClass audioClass){
        pause = ShadowRat.room != 0 || ShadowCat.room == 1 || ShadowCat.room == 3 || Player.freeze;
        if (jumpscareI == 0) {
            vinnieLaughTime -= Gdx.graphics.getDeltaTime();
            if (vinnieLaughTime <= 0){
                vinnieLaughTime += 12;
                if (vinnielaugh == 0){
                    vinnielaugh = 1 + random.nextInt(4);
                } else if (vinnielaugh == 1){
                    vinnielaugh = 2 + random.nextInt(3);
                } else if (vinnielaugh == 2){
                    if (random.nextInt(2) == 0){
                        vinnielaugh = 1;
                    } else {
                        vinnielaugh = 3 + random.nextInt(2);
                    }
                } else if (vinnielaugh == 3){
                    if (random.nextInt(2) == 0){
                        vinnielaugh = 1 + random.nextInt(2);
                    } else {
                        vinnielaugh = 4;
                    }
                } else {
                    vinnielaugh = 1 + random.nextInt(3);
                }

                audioClass.play("laugh" + vinnielaugh);
                audioClass.setVolume("laugh" + vinnielaugh, 0.75f);
            }

            switch (room) {
                case 0:
                    if (hitboxHit) {
                        doorRetreat(audioClass);
                    }
                    doorMechanic(random, audioClass);
                    knockAtDoor(audioClass);
                    break;
                case 1:
                    roomMechanic(data, random, audioClass);
                    break;
                case 2:
                    bedMechanic(data, audioClass);
                    break;
                case 3:
                    crouchMechanic(audioClass);
                    break;
            }
            if (jumpscareI != 0){
                audioClass.stopAllSounds();
            }
        } else if (!jumpscare){
            jumpscare = true;
            jumpscareTimer = 0.05f;
            Player.blacknessTimes = 1;
            Player.blacknessMultiplier = 12;
            audioClass.stopAllSounds();
            audioClass.play("shadowJumpscare");
        } else if (jumpscareTime > 0){
            jumpscareTime -= Gdx.graphics.getDeltaTime();
            if (jumpscareTime < 0) {
                jumpscareTime = 0;
                audioClass.stop("shadowJumpscare");
            }
            if (jumpscareTimer > 0){
                jumpscareTimer -= Gdx.graphics.getDeltaTime();
                if (jumpscareTimer <= 0){
                    jumpscareTimer += 0.05f;
                    jumpscareI = 1 + random.nextInt(6);
                }
            }
        }

        if (tapeSpotted && tapePosition >= 0){
            tapePosition -= Gdx.graphics.getDeltaTime() * 30;
            if (tapePosition < 0){
                tapePosition = 0;
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
            if (Player.room == 0 && room == 0 && doorAnimation == 13) {
                hitboxHit = true;
            } else if (Player.room == 0 && room == 1 && attack){
                hitboxHit = true;
            } else if (Player.room == 1 && room == 2) {
                hitboxHit = true;
            } else if (Player.room == 0 && room == 3 && timeToFlash > 0){
                hitboxHit = true;
            }
        }
    }

    public static void hitboxes(Data data){
        hitboxHit = false;
        hitboxPosition[0] = -1;
        hitboxPosition[1] = -1;
        hitboxDistance = 0;
        switch (room){
            case 0:
                if (doorCooldown == 0){
                    if (side == 0){
                        hitboxDistance = 65;
                        Utils.setHitbox(hitboxPosition, 473, 657);
                    } else if (side == 1){
                        hitboxDistance = 70;
                        hitboxPosition[0] = 1531;
                        hitboxPosition[1] = 710;
                    } else {
                        hitboxDistance = 80;
                        hitboxPosition[0] = 2544;
                        hitboxPosition[1] = 680;
                    }
                }
                break;
            case 1:
                if (jumpAnimation != 0) break;
                if (side == 0){
                    hitboxDistance = 80;
                    switch ((int) attackPosition) {
                        case 0 -> Utils.setHitbox(hitboxPosition, 372, 887);
                        case 1 -> Utils.setHitbox(hitboxPosition, 352, 882);
                        case 2 -> Utils.setHitbox(hitboxPosition, 332, 873);
                        case 3 -> Utils.setHitbox(hitboxPosition, 311, 867);
                        case 4 -> Utils.setHitbox(hitboxPosition, 293, 857);
                        case 5 -> Utils.setHitbox(hitboxPosition, 283, 848);
                        case 6 -> Utils.setHitbox(hitboxPosition, 275, 837);
                        case 7 -> Utils.setHitbox(hitboxPosition, 266, 826);
                        case 8 -> Utils.setHitbox(hitboxPosition, 257, 813);
                        case 9 -> Utils.setHitbox(hitboxPosition, 251, 799);
                        case 10 -> Utils.setHitbox(hitboxPosition, 245, 783);
                        case 11 -> Utils.setHitbox(hitboxPosition, 241, 773);

                        case 12 -> Utils.setHitbox(hitboxPosition, 239, 760);
                        case 13 -> Utils.setHitbox(hitboxPosition, 233, 745);
                        case 14 -> Utils.setHitbox(hitboxPosition, 228, 731);
                        case 15 -> Utils.setHitbox(hitboxPosition, 233, 717);
                        case 16 -> Utils.setHitbox(hitboxPosition, 238, 702);
                        case 17 -> Utils.setHitbox(hitboxPosition, 250, 687);
                        case 18 -> Utils.setHitbox(hitboxPosition, 264, 672);
                        case 19 -> Utils.setHitbox(hitboxPosition, 279, 657);
                        case 20 -> Utils.setHitbox(hitboxPosition, 294, 640);
                        case 21 -> Utils.setHitbox(hitboxPosition, 313, 633);
                        case 22 -> Utils.setHitbox(hitboxPosition, 332, 626);
                        case 23 -> Utils.setHitbox(hitboxPosition, 352, 618);

                        case 24 -> Utils.setHitbox(hitboxPosition, 371, 612);
                        case 25 -> Utils.setHitbox(hitboxPosition, 388, 615);
                        case 26 -> Utils.setHitbox(hitboxPosition, 404, 619);
                        case 27 -> Utils.setHitbox(hitboxPosition, 420, 627);
                        case 28 -> Utils.setHitbox(hitboxPosition, 436, 635);
                        case 29 -> Utils.setHitbox(hitboxPosition, 450, 641);
                        case 30 -> Utils.setHitbox(hitboxPosition, 466, 649);
                        case 31 -> Utils.setHitbox(hitboxPosition, 476, 663);
                        case 32 -> Utils.setHitbox(hitboxPosition, 487, 679);
                        case 33 -> Utils.setHitbox(hitboxPosition, 497, 699);
                        case 34 -> Utils.setHitbox(hitboxPosition, 505, 718);
                        case 35 -> Utils.setHitbox(hitboxPosition, 511, 736);

                        case 36 -> Utils.setHitbox(hitboxPosition, 516, 752);
                        case 37 -> Utils.setHitbox(hitboxPosition, 512, 766);
                        case 38 -> Utils.setHitbox(hitboxPosition, 508, 779);
                        case 39 -> Utils.setHitbox(hitboxPosition, 503, 790);
                        case 40 -> Utils.setHitbox(hitboxPosition, 498, 799);
                        case 41 -> Utils.setHitbox(hitboxPosition, 486, 813);
                        case 42 -> Utils.setHitbox(hitboxPosition, 475, 826);
                        case 43 -> Utils.setHitbox(hitboxPosition, 463, 842);
                        case 44 -> Utils.setHitbox(hitboxPosition, 450, 856);
                        case 45 -> Utils.setHitbox(hitboxPosition, 430, 867);
                        case 46 -> Utils.setHitbox(hitboxPosition, 410, 876);
                        case 47 -> Utils.setHitbox(hitboxPosition, 391, 883);
                    }
                } else if (side == 1){
                    hitboxDistance = 95;
                    switch ((int) attackPosition) {
                        case 0 -> Utils.setHitbox(hitboxPosition, 1475, 902);
                        case 1 -> Utils.setHitbox(hitboxPosition, 1449, 899);
                        case 2 -> Utils.setHitbox(hitboxPosition, 1423, 893);
                        case 3 -> Utils.setHitbox(hitboxPosition, 1395, 886);
                        case 4 -> Utils.setHitbox(hitboxPosition, 1369, 878);
                        case 5 -> Utils.setHitbox(hitboxPosition, 1346, 863);
                        case 6 -> Utils.setHitbox(hitboxPosition, 1325, 849);
                        case 7 -> Utils.setHitbox(hitboxPosition, 1305, 827);
                        case 8 -> Utils.setHitbox(hitboxPosition, 1285, 805);
                        case 9 -> Utils.setHitbox(hitboxPosition, 1274, 788);
                        case 10 -> Utils.setHitbox(hitboxPosition, 1264, 769);
                        case 11 -> Utils.setHitbox(hitboxPosition, 1261, 744);

                        case 12 -> Utils.setHitbox(hitboxPosition, 1259, 719);
                        case 13 -> Utils.setHitbox(hitboxPosition, 1271, 694);
                        case 14 -> Utils.setHitbox(hitboxPosition, 1279, 666);
                        case 15 -> Utils.setHitbox(hitboxPosition, 1288, 646);
                        case 16 -> Utils.setHitbox(hitboxPosition, 1298, 626);
                        case 17 -> Utils.setHitbox(hitboxPosition, 1314, 615);
                        case 18 -> Utils.setHitbox(hitboxPosition, 1332, 603);
                        case 19 -> Utils.setHitbox(hitboxPosition, 1351, 590);
                        case 20 -> Utils.setHitbox(hitboxPosition, 1371, 576);
                        case 21 -> Utils.setHitbox(hitboxPosition, 1398, 568);
                        case 22 -> Utils.setHitbox(hitboxPosition, 1425, 559);
                        case 23 -> Utils.setHitbox(hitboxPosition, 1453, 550);

                        case 24 -> Utils.setHitbox(hitboxPosition, 1480, 539);
                        case 25 -> Utils.setHitbox(hitboxPosition, 1507, 546);
                        case 26 -> Utils.setHitbox(hitboxPosition, 1535, 550);
                        case 27 -> Utils.setHitbox(hitboxPosition, 1561, 559);
                        case 28 -> Utils.setHitbox(hitboxPosition, 1587, 566);
                        case 29 -> Utils.setHitbox(hitboxPosition, 1609, 579);
                        case 30 -> Utils.setHitbox(hitboxPosition, 1630, 590);
                        case 31 -> Utils.setHitbox(hitboxPosition, 1649, 602);
                        case 32 -> Utils.setHitbox(hitboxPosition, 1666, 610);
                        case 33 -> Utils.setHitbox(hitboxPosition, 1679, 632);
                        case 34 -> Utils.setHitbox(hitboxPosition, 1689, 652);
                        case 35 -> Utils.setHitbox(hitboxPosition, 1696, 676);

                        case 36 -> Utils.setHitbox(hitboxPosition, 1701, 701);
                        case 37 -> Utils.setHitbox(hitboxPosition, 1698, 725);
                        case 38 -> Utils.setHitbox(hitboxPosition, 1693, 747);
                        case 39 -> Utils.setHitbox(hitboxPosition, 1685, 769);
                        case 40 -> Utils.setHitbox(hitboxPosition, 1675, 789);
                        case 41 -> Utils.setHitbox(hitboxPosition, 1657, 813);
                        case 42 -> Utils.setHitbox(hitboxPosition, 1638, 836);
                        case 43 -> Utils.setHitbox(hitboxPosition, 1617, 853);
                        case 44 -> Utils.setHitbox(hitboxPosition, 1595, 869);
                        case 45 -> Utils.setHitbox(hitboxPosition, 1566, 881);
                        case 46 -> Utils.setHitbox(hitboxPosition, 1536, 892);
                        case 47 -> Utils.setHitbox(hitboxPosition, 1505, 898);
                    }
                } else {
                    hitboxDistance = 110;
                    switch ((int) attackPosition){
                        case 0 -> Utils.setHitbox(hitboxPosition, 2554, 901);
                        case 1 -> Utils.setHitbox(hitboxPosition, 2530, 896);
                        case 2 -> Utils.setHitbox(hitboxPosition, 2507, 891);
                        case 3 -> Utils.setHitbox(hitboxPosition, 2482, 884);
                        case 4 -> Utils.setHitbox(hitboxPosition, 2458, 878);
                        case 5 -> Utils.setHitbox(hitboxPosition, 2441, 863);
                        case 6 -> Utils.setHitbox(hitboxPosition, 2422, 850);
                        case 7 -> Utils.setHitbox(hitboxPosition, 2404, 835);
                        case 8 -> Utils.setHitbox(hitboxPosition, 2385, 821);
                        case 9 -> Utils.setHitbox(hitboxPosition, 2380, 803);
                        case 10 -> Utils.setHitbox(hitboxPosition, 2375, 784);
                        case 11 -> Utils.setHitbox(hitboxPosition, 2370, 763);

                        case 12 -> Utils.setHitbox(hitboxPosition, 2364, 744);
                        case 13 -> Utils.setHitbox(hitboxPosition, 2369, 727);
                        case 14 -> Utils.setHitbox(hitboxPosition, 2375, 708);
                        case 15 -> Utils.setHitbox(hitboxPosition, 2382, 687);
                        case 16 -> Utils.setHitbox(hitboxPosition, 2389, 665);
                        case 17 -> Utils.setHitbox(hitboxPosition, 2408, 654);
                        case 18 -> Utils.setHitbox(hitboxPosition, 2430, 645);
                        case 19 -> Utils.setHitbox(hitboxPosition, 2449, 635);
                        case 20 -> Utils.setHitbox(hitboxPosition, 2469, 623);
                        case 21 -> Utils.setHitbox(hitboxPosition, 2489, 618);
                        case 22 -> Utils.setHitbox(hitboxPosition, 2513, 612);
                        case 23 -> Utils.setHitbox(hitboxPosition, 2535, 607);

                        case 24 -> Utils.setHitbox(hitboxPosition, 2558, 602);
                        case 25 -> Utils.setHitbox(hitboxPosition, 2579, 606);
                        case 26 -> Utils.setHitbox(hitboxPosition, 2599, 609);
                        case 27 -> Utils.setHitbox(hitboxPosition, 2620, 611);
                        case 28 -> Utils.setHitbox(hitboxPosition, 2641, 613);
                        case 29 -> Utils.setHitbox(hitboxPosition, 2666, 623);
                        case 30 -> Utils.setHitbox(hitboxPosition, 2691, 634);
                        case 31 -> Utils.setHitbox(hitboxPosition, 2718, 643);
                        case 32 -> Utils.setHitbox(hitboxPosition, 2744, 653);
                        case 33 -> Utils.setHitbox(hitboxPosition, 2750, 674);
                        case 34 -> Utils.setHitbox(hitboxPosition, 2757, 694);
                        case 35 -> Utils.setHitbox(hitboxPosition, 2765, 714);

                        case 36 -> Utils.setHitbox(hitboxPosition, 2773, 735);
                        case 37 -> Utils.setHitbox(hitboxPosition, 2765, 758);
                        case 38 -> Utils.setHitbox(hitboxPosition, 2756, 780);
                        case 39 -> Utils.setHitbox(hitboxPosition, 2750, 801);
                        case 40 -> Utils.setHitbox(hitboxPosition, 2742, 822);
                        case 41 -> Utils.setHitbox(hitboxPosition, 2721, 837);
                        case 42 -> Utils.setHitbox(hitboxPosition, 2701, 850);
                        case 43 -> Utils.setHitbox(hitboxPosition, 2680, 867);
                        case 44 -> Utils.setHitbox(hitboxPosition, 2660, 878);
                        case 45 -> Utils.setHitbox(hitboxPosition, 2633, 886);
                        case 46 -> Utils.setHitbox(hitboxPosition, 2607, 892);
                        case 47 -> Utils.setHitbox(hitboxPosition, 2581, 897);
                    }
                }
                break;
            case 3:
                hitboxDistance = 80;
                if (side == 0) Utils.setHitbox(hitboxPosition, 630, 434);
                else Utils.setHitbox(hitboxPosition, 2312, 418);
                break;
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
        String sideTexture;
        float x = 0;
        float y = 0;

        if (!jumpscare) {
            boolean lookInRoom = false;
            boolean lookUnderBed = false;
            boolean lookAtTape = false;

            if (side == 0) {
                sideTexture = "Left";
            } else if (side == 1) {
                sideTexture = "Middle";
            } else {
                sideTexture = "Right";
            }

            switch (room) {
                case 0:
                    lookInRoom = true;
                    if (!leaveRoom && doorAnimation >= 1) {
                        texture = "Looking Away/" + sideTexture + " Look Away/LookAway" + (int) (14 - doorAnimation);
                        if (side == 0) {
                            x = 418;
                            y = 322;
                        } else if (side == 1) {
                            x = 1409;
                            y = 380;
                        } else {
                            x = 2490;
                            y = 319;
                        }
                    } else if (doorAnimation >= 1) {
                        texture = "Leaving/" + sideTexture + "/Leaving" + (int) (19 - doorAnimation);
                        if (side == 0) {
                            x = 180;
                            y = 334;
                        } else {
                            x = 2487;
                            y = 279;
                        }
                    }
                    break;
                case 1:
                    lookInRoom = true;
                    String position;

                    int jumpI = (int) jumpAnimation;
                    if (jumpI != 0) {
                        if ((jumpTarget == 21 && side == 0) || (jumpTarget == 19 && side == 1)) {
                            jumpI = jumpTarget - jumpI + 1;
                        }
                    }

                    if (jumpI == 0) {
                        position = String.valueOf((int) attackPosition);
                        texture = "Battle/" + sideTexture + "/" + position;
                    } else {
                        if (jumpTarget == 21) {
                            sideTexture = "Left";
                        } else if (jumpTarget == 19){
                            sideTexture = "Right";
                        }
                        texture = "Battle/Jump/" + sideTexture + "/" + jumpI;
                    }

                    if (jumpI != 0) {
                        if (jumpTarget == 21){
                            float multiplier = (float) jumpI / 21;
                            float cosMultiplier = (float) (-Math.cos(Math.toRadians(180 * multiplier)) + 1) / 2;
                            float distance = 1089 * cosMultiplier;
                            x = 29 + distance;
                            y = 209;
                        } else {
                            float multiplier = (float) jumpI / 19;
                            float cosMultiplier = (float) (-Math.cos(Math.toRadians(180 * multiplier)) + 1) / 2;
                            float distance = 1105 * cosMultiplier;
                            x = 1044 + distance;
                            y = 170;
                        }
                    } else {
                        if (side == 0) {
                            x = 104;
                            y = 276;
                        } else if (side == 1) {
                            x = 1119;
                            y = 200;
                        } else {
                            x = 2175;
                            y = 174;
                        }
                    }
                    break;
                case 2:
                    if (Player.room == 1 && Monstergami.side != 3) {
                        lookUnderBed = true;
                        if (side == 0) {
                            texture = "Under Bed/Bed1";
                            y = 169;
                        } else {
                            texture = "Under Bed/Bed2";
                            x = 1204;
                            y = 180;
                        }
                    } else if (Player.room == 2 && tapePosition >= 1) {
                        lookAtTape = true;
                        x = 190;
                        y = 455;
                        texture = "Tape/Leaving" + (int) (15 - tapePosition);
                    }
                    break;
                case 3:
                    lookInRoom = true;
                    if (side == 0) {
                        texture = "Under Bed/Left/Left" + ((int) twitchPosition + 1);
                        x = 506;
                        y = 236;
                    } else {
                        texture = "Under Bed/Right/Right" + ((int) twitchPosition + 1);
                        x = 2142;
                        y = 184;
                    }
            }
            boolean passed = lookInRoom && !Player.turningAround && Player.room == 0;
            if (!passed) {
                passed = lookUnderBed && !Player.turningAround;
            }

            if (!passed) {
                passed = lookAtTape && !Player.turningAround;
            }

            if (!passed) return;
        } else {
            texture = "Jumpscare/Jumpscare" + jumpscareI;
            x = Player.roomPosition[0] + Player.shakingPosition;
            y = Player.roomPosition[1];
        }
        if (texture == null) return;
        batch.setColor(1, 1, 1, 1);
        batch.draw(ImageHandler.images.get("game/Shadow Vinnie/" + texture), x, y);
    }

    public static void reset(){
        doorLock = false;
        jumpscareType = "";
        if (hitboxPosition == null){
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }
        vinnieLaughTime = 2;
        attackPosition = 0;
        jumpCase = 0;
        jumpscareTime = 0.9f;
        tapeWeasel = false;
        doorCooldown = (float) (5 + 0.3 * (20 - ai));
        cooldownTimer = 5;
        knocks = 0;
        knockTimer = 0;
        room = 0;
        bedSpotted = false;
        jumpscare = false;
        side = -1;
        attack = false;
        shaking = false;
        dontSoundShake = false;
        twitchingNow = false;
        leaveRoom = false;
        doorAnimation = 0;
        hitboxHit = false;
        jumpscareI = 0;
        jumps = 0;
        jumpTarget = 0;
        jumpAnimation = 0;
        dodgePlaying = false;
        vinnielaugh = 0;
    }

    public static void doorMechanic(Random random, AudioClass audioClass){
        if (doorCooldown > 0){
            doorCooldown -= Gdx.graphics.getDeltaTime();
            if (pause && !doorLock && doorCooldown <= 0) {
                doorCooldown = 0.01f;
            }
            if (doorAnimation < 1){
                doorAnimation = 0;
                if (leaveRoom){
                    leaveRoom = false;
                }
            } else {
                doorAnimation -= Gdx.graphics.getDeltaTime() * 25;
            }
            if (doorCooldown <= 0){
                doorCooldown = 0;
                doorLock = true;
                if (cooldownTimer > 0) {
                    if (!ShadowRat.active || ShadowRat.side == -1) {
                        side = random.nextInt(3);
                    } else if (ShadowRat.side == 0){
                        side = 1 + random.nextInt(2);
                    } else if (ShadowRat.side == 1){
                        side = 2 * random.nextInt(2);
                    } else if (ShadowRat.side == 2){
                        side = random.nextInt(2);
                    }

                    if (cooldownTimer > 0.375f) {
                        knockHard = (int) cooldownTimer <= 3;
                        if (cooldownTimer <= 0.5f){
                            knocks = 2;
                        } else if (knockHard){
                            knocks = 3;
                        } else {
                            knocks = 1;
                        }
                        knockDelay = 0.05f + (Math.max(0.065f, 0.05f * cooldownTimer));
                        doorAnimation = 1;
                    }
                } else {
                    if (Player.side == 0){
                        side = 1 + random.nextInt(2);
                    } else if (Player.side == 1){
                        side = 2 * random.nextInt(2);
                    } else if (Player.side == 2){
                        side = random.nextInt(2);
                    } else {
                        side = random.nextInt(3);
                    }
                }
            }
        } else {
            if (cooldownTimer > 0) {
                cooldownTimer -= Gdx.graphics.getDeltaTime();
            }

            if (doorAnimation < 13) {
                doorAnimation += Gdx.graphics.getDeltaTime() * 25;
                if (doorAnimation > 13){
                    doorAnimation = 13;
                }
            }
            if (cooldownTimer <= 0 && Player.blackness == 1 && Player.blacknessTimes == 0){
                cooldownTimer = 4;
                room = 1;
                doorLock = false;
                if (side == -1){
                    if (Player.side == 0){
                        side = 1 + random.nextInt(2);
                    } else if (Player.side == 1){
                        side = 2 * random.nextInt(2);
                    } else if (Player.side == 2){
                        side = random.nextInt(2);
                    } else {
                        side = random.nextInt(3);
                    }
                }
                jumps = 5;
                framesToMove = 0;
                attackTime = 2;
                attackPosition = 0;
                patienceHealthTimer = 2;
                patienceTimer = 0.65f;
                Player.blacknessTimes = 3;
                Player.blacknessMultiplier = 6;
                timeToFlash = 0.65f;
                knocks = 0;
                knockTimer = 0;
                audioClass.play("walking_in");
                if (side == ShadowCat.side && ShadowCat.room == 1) jumpscareI = 1;
            }
        }
    }

    public static void doorRetreat(AudioClass audioClass){
        audioClass.play("spotted");
        peekSpotted = false;
        doorCooldown = 5;
        if (Game.doorTurn != 1) cooldownTimer = 5;
        doorLock = false;
    }

    public static void knockAtDoor(AudioClass audioClass){
        if (knockTimer == 0) {
            if (knocks > 0) {
                knocks--;
                if (knockHard){
                    audioClass.play("hard_knock");
                } else {
                    audioClass.play("vinnieKnock");
                    audioClass.setVolume("vinnieKnock", 0.65f);
                }

                if (knocks > 0){
                    knockTimer = knockDelay;
                }
            }
        } else {
            knockTimer -= Gdx.app.getGraphics().getDeltaTime();
            if (knockTimer < 0){
                knockTimer = 0;
            }
        }
    }

    public static void roomMechanic(Data data, Random random, AudioClass audioClass) {
        if (!Player.freeze && cooldownTimer > 0 && !attack && attackTime != 0){
            cooldownTimer -= Gdx.graphics.getDeltaTime();
            if (cooldownTimer < 0){
                cooldownTimer = 0;
            }
        }

        if (attackTime == 0 && !attack && Player.blacknessTimes == 0) {
            shaking = false;
            Player.blacknessMultiplier = 1.25f;
            room = 2;
            if (ShadowRat.active) Game.doorTurn = 0;
            dontSoundShake = false;
            bedPatienceTimer = 4;
            cooldownTimer = 10 + 0.3f * (20 - ai);
            audioClass.play("crawl");
            tapeSpotted = false;
            if (!tapeWeasel && !Player.tapeStolen) {
                tapePosition = 14f;
                if (data.hardCassette) timeUntilWeasel = 0.01f;
                else timeUntilWeasel = 0.25f + random.nextInt(2) + random.nextFloat() + (0.1f * (20 - ai));
            }

            if (data.challenge4
                    && !Monstergami.wait
                    && Monstergami.pause
                    && Rat.room != 1
                    && Monstergami.cooldownTimer == 0){
                Monstergami.pause = false;
                cooldownTimer -= 2;
            }

            if (Cat.room == 2){
                if (Cat.side == 0){
                    side = 2;
                } else {
                    side = 0;
                }
            } else if (Rat.room == 2) {
                if (Rat.side == 0){
                    side = 2;
                } else {
                    side = 0;
                }
            } else {
                side = 2 * random.nextInt(2);
            }
        }

        if (attackTime != 0 && !attack && Player.flashlightAlpha > 0 && !Player.turningAround && Player.room == 0) {
            attack = Player.inititiateSnapPosition(side);
            if (attack) {
                Player.lastCharacterAttack = "Shadow";
            }
        }

        if (attack) {
            shaking = hitboxHit;

            float multiplier = 20;
            if ((int) framesToMove != 0){
                multiplier = 80;
            }
            if (jumpAnimation > 0){
                multiplier = 23;
            }

            if (jumpTarget == 0) {

                if (shaking) {
                    if (attackTime > 0) {
                        attackTime -= Gdx.graphics.getDeltaTime();
                        if (attackTime < 0) {
                            attackTime = 0;
                        }
                    }

                    patienceTimer = 0.65f;

                    if ((int) framesToMove == 0) timeToFlash -= Gdx.graphics.getDeltaTime();
                    patienceHealthTimer += Gdx.graphics.getDeltaTime() / 3.5f;
                    if (patienceHealthTimer > 2) patienceHealthTimer = 2;
                    if (timeToFlash <= 0 && attackTime == 0) {
                        if (jumps == 0) {
                            Player.snapPosition = false;
                            attack = false;
                            dontSoundShake = true;
                            audioClass.play("thunder");
                            Player.blacknessTimes = 3;
                            Player.blacknessMultiplier = 6;
                            Player.blacknessDelay = 0.5f;
                            Player.freeze = true;
                        } else {
                            if (side == 0){
                                jumpTarget = 21;
                                jumpCase = 1;
                            } else if (side == 2){
                                jumpTarget = 19;
                                jumpCase = 0;
                            } else {
                                if ((Cat.room != 4 && random.nextInt(2) == 0) || (Cat.room == 4 && Cat.side == 0)) {
                                    jumpTarget = 21;
                                    jumpCase = 0;
                                } else {
                                    jumpTarget = 19;
                                    jumpCase = 1;
                                }
                            }
                            patienceTimer = 0.65f;

                            System.out.println("Position: " + attackPosition);

                            if (attackPosition > 24) {
                                framesToMove = 48 - attackPosition;
                            } else if (attackPosition < 24) {
                                framesToMove = -attackPosition;
                            } else {
                                if (random.nextInt(2) == 1) {
                                    framesToMove = -attackPosition;
                                } else {
                                    framesToMove = attackPosition;
                                }
                            }

                            System.out.println("FramesToMove: " + framesToMove);

                            audioClass.play("vinnieDodge");
                            audioClass.loop("vinnieDodge", true);
                            dodgePlaying = true;
                        }
                    } else if (attackTime > 0 && timeToFlash <= 0) {
                        if (!Player.snapPosition) {
                            Player.snapPosition = true;
                            Player.snapToSide = side;
                        }

                        timeToFlash = 0.2f + 0.05f * random.nextInt(3);
                        patienceTimer = 0.75f + 0.025f * (20 - ai);
                        framesToMove = 8 + (1 + random.nextInt(48));
                        if (random.nextInt(2) == 1) {
                            framesToMove = -framesToMove;
                        }
                        audioClass.play("vinnieDodge");
                        audioClass.loop("vinnieDodge", true);
                        dodgePlaying = true;
                    }
                }
            } else {
                if (jumpAnimation == 0) {
                    if (shaking && (int) framesToMove == 0 && attackPosition == 0) {
                        Player.snapPosition = false;
                        jumpAnimation = jumpTarget;
                        jumps--;
                        if (jumpCase == 0) {
                            audioClass.play("vinnieTurnLeft");
                        } else {
                            audioClass.play("vinnieTurnRight");
                        }
                    }
                } else {
                    if (jumpAnimation > 0) {
                        jumpAnimation -= Gdx.graphics.getDeltaTime() * multiplier;
                        if (jumpAnimation < 0) {
                            jumpAnimation = 0;
                        }
                    } else {
                        jumpAnimation += Gdx.graphics.getDeltaTime() * multiplier;
                        if (jumpAnimation > 0) {
                            jumpAnimation = 0;
                        }
                    }

                    if (jumpAnimation < 1) {
                        if (jumpTarget == 19) {
                            if (side == 1) {
                                side = 2;
                            } else {
                                side = 1;
                            }
                        } else if (jumpTarget == 21){
                            if (side == 0) {
                                side = 1;
                            } else {
                                side = 0;
                            }
                        }
                        if (jumpCase == 0) {
                            audioClass.stop("vinnieTurnLeft");
                        } else {
                            audioClass.stop("vinnieTurnRight");
                        }
                        jumpTarget = 0;
                        jumpAnimation = 0;
                        attackTime = 2;
                        if (side == 1 && Cat.side == Player.side) patienceTimer = 3;
                        else patienceTimer = 1f;
                        patienceHealthTimer = 2f;
                        timeToFlash = 0.65f;
                    }
                }
            }

            if (framesToMove != 0) {
                if (framesToMove > 0) {
//                if (Game.hourOfGame != 12){
//                    multiplier *= 1 + (Game.hourOfGame * 0.0625f);
//                }
                    framesToMove -= Gdx.graphics.getDeltaTime() * multiplier;
                    attackPosition += Gdx.graphics.getDeltaTime() * multiplier;
                    if (attackPosition >= 48){
                        attackPosition -= 48;
                    }
                    if (framesToMove <= 0) {
                        framesToMove = 0;
                        attackPosition = (int) attackPosition;
                    }
                } else if (framesToMove < 0) {
                    framesToMove += Gdx.graphics.getDeltaTime() * multiplier;
                    attackPosition -= Gdx.graphics.getDeltaTime() * multiplier;

                    if (framesToMove > -1) {
                        framesToMove = 0;
                        attackPosition = (int) attackPosition;
                    }
                    if (attackPosition < 0) {
                        attackPosition += 48;
                    }
                }
                shaking = false;
            }

            if ((int) framesToMove == 0 && dodgePlaying){
                audioClass.stop("vinnieDodge");
                dodgePlaying = false;
            }

            if (!shaking && (int) framesToMove == 0 && jumpAnimation == 0) {
                patienceHealthTimer -= Gdx.graphics.getDeltaTime();
                if (patienceHealthTimer < 0) {
                    patienceHealthTimer = 0;
                }

                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer < 0) {
                    patienceTimer = 0;
                }
            }
        }

        shaking = shaking && (int) framesToMove == 0;

        if (shaking) {
            twitchPosition += Gdx.graphics.getDeltaTime() * 30;
            if (twitchPosition >= 2) {
                twitchPosition = 0;
            }
        } else {
            twitchPosition = 0;
        }

        if (cooldownTimer == 0 || patienceTimer == 0 || patienceHealthTimer == 0){
            jumpscareI = 1;
        }
    }

    public static void bedMechanic(Data data, AudioClass audioClass){
        if (cooldownTimer > 0 && !Player.freeze){
            cooldownTimer -= Gdx.graphics.getDeltaTime();

            if (!tapeSpotted && timeUntilWeasel > 0){
                timeUntilWeasel -= Gdx.graphics.getDeltaTime();
                if (timeUntilWeasel <= 0){
                    timeUntilWeasel = 0;
                    tapeWeasel = true;
                    if (data.hardCassette){
                        Player.playPosition = 3;
                        Player.stopPosition = 0;
                        Player.rewindPosition = 0;
                        Player.tapeRewind = false;
                        Player.tapePlay = true;
                        Player.tapeStop = false;
                        Player.tape.stop();
                        audioClass.stop("tapeRewind");
                    }
                    audioClass.play("tapeWeasel");
                    audioClass.loop("tapeWeasel", true);
                }
            }

            if (Player.room == 1 && !Player.turningAround && bedPatienceTimer > 0){
                bedPatienceTimer -= Gdx.graphics.getDeltaTime();
                if (bedPatienceTimer <= 0){
                    bedPatienceTimer = 0;
                    jumpscareI = 1;
                }
            }

            boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);

            if (cooldownTimer <= 0 || (lookingAway && Rat.room == 2 && Cat.room == 2 && Monstergami.side == -1)){
                if (lookingAway && bedSpotted) {
                    audioClass.play("peek");
                    room = 3;
                    bedSpotted = false;
                    patienceTimer = 2f + 0.2f * (20 - ai);
                    tapeSpotted = false;
                    timeToFlash = 2.75f;
                    Player.blacknessMultiplier = 6;
                    attackPosition = 0;
                } else {
                    jumpscareI = 1;
                }
            }
        }
    }

    public static void crouchMechanic(AudioClass audioClass){
        if (shaking) {
            twitchPosition += Gdx.graphics.getDeltaTime() * 30;
            if (twitchPosition >= 2) {
                twitchPosition = 0;
            }
        } else {
            twitchPosition = 0;
        }

        if (timeToFlash > 0) {
            shaking = hitboxHit;

            if (shaking) {
                timeToFlash -= Gdx.graphics.getDeltaTime();
                if (timeToFlash <= 0){
                    timeToFlash = 0;
                    Player.freeze = true;
                    dontSoundShake = true;
                    audioClass.play("thunder");
                    Player.blacknessTimes = 3;
                    Player.blacknessMultiplier = 6;
                    Player.blacknessDelay = 0.5f;
                }
            } else if (!Player.freeze){
                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer <= 0){
                    patienceTimer = 0;
                    jumpscareI = 1;
                }
            }
        } else if (Player.blacknessTimes == 0){
            if (Player.blacknessMultiplier != 1.25f){
                Player.blacknessMultiplier = 1.25f;
            } else if (Player.blacknessDelay == 0){
                Player.freeze = false;
                shaking = false;
                twitchingNow = false;
                dontSoundShake = false;
                room = 0;
                doorCooldown = 5;
                cooldownTimer = 5;
                leaveRoom = true;
                doorAnimation = 18;
                audioClass.play("vinnieLeave");
            }
        }
    }
}
