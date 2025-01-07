package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class Vinnie {

    public static int ai;
    public static int room;
    public static boolean doorLock;
    public static int side = -1;
    public static float doorCooldown;
    public static float cooldownTimer;
    public static boolean leaveRoom;
    public static float doorAnimation;
    public static boolean peekSpotted;
    public static int attackPosition;
    public static float twitchPosition;
    public static boolean shaking;
    public static boolean dontSoundShake;
    public static boolean twitchingNow;
    public static float moveToPosition;
    public static boolean dodgePlaying;
    public static float timeToFlash;
    public static boolean attack;
    public static float attackTime;
    public static boolean bedSpotted;
    public static float patienceTimer;
    public static float patienceHealthTimer;
    public static float bedPatienceTimer;
    public static boolean tapeSpotted;
    public static float timeUntilWeasel;
    public static boolean tapeWeasel;
    public static float tapePosition;
    public static boolean jumpscare;
    public static String jumpscareType;
    public static float jumpscareTimer;
    public static float jumpscareTime;
    public static int jumpscareI;
    public static int jumps;
    public static int jumpTarget;
    public static float jumpAnimation;
    public static boolean moveLock = false;
    public static boolean pause;
    private static int jumpCase;

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
        if (jumpscareI == 0 && Player.room == 0 && !Player.turningAround && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Random random, Data data, AudioClass audioClass){
        pause = Rat.room != 0 || (Cat.room != 2 && Cat.room != 4) || Monstergami.side != -1 || Player.freeze;
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
            if (room == 1){
                jumpscareType = "vinnieRoomJumpscare";
                Player.blacknessTimes = 1;
                Player.blacknessMultiplier = 5;
                jumpscareTime = 1.5f;
            } else if (room == 2 || room == 3){
                jumpscareType = "vinnieBedJumpscare";
                Player.blacknessTimes = 1;
                Player.blacknessMultiplier = 5;
                jumpscareTime = 1.5f;
            }
            if (!jumpscareType.isEmpty()) {
                audioClass.play(jumpscareType);
                jumpscare = true;
                jumpscareTimer = 0.0375f;
            }
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
                    if (jumpscareI > 29 && jumpscareType.equals("vinnieBedJumpscare")){
                        jumpscareI = 29;
                    } else if (jumpscareI > 35){
                        jumpscareI = 35;
                    }
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
            if (room == 0 && doorAnimation == 13) {
                hitboxHit = true;
            } else if (room == 1 && attack){
                hitboxHit = true;
            } else if (room == 3 && timeToFlash > 0){
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
                        hitboxPosition[0] = 473;
                        hitboxPosition[1] = 656;
                    } else if (side == 1){
                        hitboxDistance = 70;
                        hitboxPosition[0] = 1531;
                        hitboxPosition[1] = 710;
                    } else {
                        hitboxDistance = 80;
                        hitboxPosition[0] = 2539;
                        hitboxPosition[1] = 683;
                    }
                }
                break;
            case 1:
                if (moveToPosition != 0 || jumpAnimation != 0) break;
                if (side == 0){
                    hitboxDistance = 70;
                    if (attackPosition == 0){
                        hitboxPosition[0] = 408;
                        hitboxPosition[1] = 870;
                    } else if (attackPosition == 1){
                        hitboxPosition[0] = 262;
                        hitboxPosition[1] = 770;
                    } else if (attackPosition == 2){
                        hitboxPosition[0] = 262;
                        hitboxPosition[1] = 649;
                    } else if (attackPosition == 3){
                        hitboxPosition[0] = 407;
                        hitboxPosition[1] = 549;
                    } else if (attackPosition == 4){
                        hitboxPosition[0] = 572;
                        hitboxPosition[1] = 635;
                    } else {
                        hitboxPosition[0] = 567;
                        hitboxPosition[1] = 791;
                    }
                } else if (side == 1){
                    hitboxDistance = 80;
                    if (attackPosition == 0){
                        hitboxPosition[0] = 1513;
                        hitboxPosition[1] = 815;
                    } else if (attackPosition == 1){
                        hitboxPosition[0] = 1356;
                        hitboxPosition[1] = 757;
                    } else if (attackPosition == 2){
                        hitboxPosition[0] = 1350;
                        hitboxPosition[1] = 598;
                    } else if (attackPosition == 3){
                        hitboxPosition[0] = 1541;
                        hitboxPosition[1] = 494;
                    } else if (attackPosition == 4){
                        hitboxPosition[0] = 1706;
                        hitboxPosition[1] = 557;
                    } else {
                        hitboxPosition[0] = 1706;
                        hitboxPosition[1] = 725;
                    }
                } else {
                    hitboxDistance = 90;
                    if (attackPosition == 0){
                        hitboxPosition[0] = 2551;
                        hitboxPosition[1] = 883;
                    } else if (attackPosition == 1){
                        hitboxPosition[0] = 2373;
                        hitboxPosition[1] = 793;
                    } else if (attackPosition == 2){
                        hitboxPosition[0] = 2363;
                        hitboxPosition[1] = 601;
                    } else if (attackPosition == 3){
                        hitboxPosition[0] = 2558;
                        hitboxPosition[1] = 502;
                    } else if (attackPosition == 4){
                        hitboxPosition[0] = 2742;
                        hitboxPosition[1] = 584;
                    } else {
                        hitboxPosition[0] = 2745;
                        hitboxPosition[1] = 770;
                    }
                }
                break;
            case 3:
                hitboxDistance = 80;
                if (side == 0){
                    hitboxPosition[0] = 632;
                    hitboxPosition[1] = 433;
                } else {
                    hitboxPosition[0] = 2301;
                    hitboxPosition[1] = 406;
                }
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
                        if ((int) moveToPosition % 2 == 0) {
                            position = switch (attackPosition) {
                                case 0 -> "Up";
                                case 1 -> "UpLeft";
                                case 2 -> "DownLeft";
                                case 3 -> "Down";
                                case 4 -> "DownRight";
                                case 5 -> "UpRight";
                                default -> null;
                            };
                            position += ((int) twitchPosition + 1);
                        } else {
                            if (moveToPosition > 0) {
                                if (attackPosition == 0) {
                                    position = "MoveUpLeft";
                                } else if (attackPosition == 1) {
                                    position = "MoveLeft";
                                } else if (attackPosition == 2) {
                                    position = "MoveDownLeft";
                                } else if (attackPosition == 3) {
                                    position = "MoveDownRight";
                                } else if (attackPosition == 4) {
                                    position = "MoveRight";
                                } else {
                                    position = "MoveUpRight";
                                }
                            } else {
                                if (attackPosition == 1) {
                                    position = "MoveUpLeft";
                                } else if (attackPosition == 2) {
                                    position = "MoveLeft";
                                } else if (attackPosition == 3) {
                                    position = "MoveDownLeft";
                                } else if (attackPosition == 4) {
                                    position = "MoveDownRight";
                                } else if (attackPosition == 5) {
                                    position = "MoveRight";
                                } else {
                                    position = "MoveUpRight";
                                }
                            }
                        }
                        texture = "Battle/" + sideTexture + "/" + position;
                    } else {
                        if (jumpTarget == 21) {
                            sideTexture = "Jump Right";
                        } else if (jumpTarget == 19){
                            sideTexture = "Jump Left";
                        }
                        texture = "Battle/" + sideTexture + "/Jump" + jumpI;
                    }

                    if (jumpI != 0) {
                        if (jumpTarget == 21){
                            y = switch (jumpI) {
                                case 1 -> {
                                    x = 198;
                                    yield 331;
                                }
                                case 2 -> {
                                    x = 199;
                                    yield 334;
                                }
                                case 3 -> {
                                    x = 213;
                                    yield 293;
                                }
                                case 4 -> {
                                    x = 242;
                                    yield 275;
                                }
                                case 5 -> {
                                    x = 281;
                                    yield 249;
                                }
                                case 6 -> {
                                    x = 335;
                                    yield 237;
                                }
                                case 7 -> {
                                    x = 391;
                                    yield 243;
                                }
                                case 8 -> {
                                    x = 471;
                                    yield 256;
                                }
                                case 9 -> {
                                    x = 536;
                                    yield 267;
                                }
                                case 10 -> {
                                    x = 593;
                                    yield 264;
                                }
                                case 11 -> {
                                    x = 650;
                                    yield 262;
                                }
                                case 12 -> {
                                    x = 707;
                                    yield 251;
                                }
                                case 13 -> {
                                    x = 787;
                                    yield 260;
                                }
                                case 14 -> {
                                    x = 873;
                                    yield 205;
                                }
                                case 15 -> {
                                    x = 956;
                                    yield 177;
                                }
                                case 16 -> {
                                    x = 1041;
                                    yield 136;
                                }
                                case 17 -> {
                                    x = 1116;
                                    yield 137;
                                }
                                case 18 -> {
                                    x = 1171;
                                    yield 124;
                                }
                                case 19 -> {
                                    x = 1204;
                                    yield 138;
                                }
                                case 20 -> {
                                    x = 1202;
                                    yield 137;
                                }
                                case 21 -> {
                                    x = 1211;
                                    yield 135;
                                }
                                default -> y;
                            };
                        } else {
                            y = switch (jumpI) {
                                case 1 -> {
                                    x = 1218;
                                    yield 132;
                                }
                                case 2 -> {
                                    x = 1227;
                                    yield 131;
                                }
                                case 3 -> {
                                    x = 1241;
                                    yield 126;
                                }
                                case 4 -> {
                                    x = 1273;
                                    yield 117;
                                }
                                case 5 -> {
                                    x = 1313;
                                    yield 117;
                                }
                                case 6 -> {
                                    x = 1359;
                                    yield 108;
                                }
                                case 7 -> {
                                    x = 1417;
                                    yield 131;
                                }
                                case 8 -> {
                                    x = 1504;
                                    yield 185;
                                }
                                case 9 -> {
                                    x = 1599;
                                    yield 188;
                                }
                                case 10 -> {
                                    x = 1679;
                                    yield 194;
                                }
                                case 11 -> {
                                    x = 1754;
                                    yield 201;
                                }
                                case 12 -> {
                                    x = 1833;
                                    yield 196;
                                }
                                case 13 -> {
                                    x = 1914;
                                    yield 197;
                                }
                                case 14 -> {
                                    x = 1989;
                                    yield 195;
                                }
                                case 15 -> {
                                    x = 2059;
                                    yield 190;
                                }
                                case 16 -> {
                                    x = 2112;
                                    yield 193;
                                }
                                case 17 -> {
                                    x = 2168;
                                    yield 191;
                                }
                                case 18 -> {
                                    x = 2210;
                                    yield 207;
                                }
                                case 19 -> {
                                    x = 2222;
                                    yield 211;
                                }
                                default -> y;
                            };
                        }
                    } else {
                        if (side == 0) {
                            x = 166;
                            y = 288;
                        } else if (side == 1) {
                            x = 1185;
                            y = 130;
                        } else {
                            x = 2222;
                            y = 184;
                        }
                    }
                    break;
                case 2:
                    if (Player.room == 1 && Monstergami.side != 3) {
                        lookUnderBed = true;
                        if (side == 0) {
                            texture = "Under Bed/Bed1";
                            y = 170;
                        } else {
                            texture = "Under Bed/Bed2";
                            x = 1249;
                            y = 195;
                        }
                    } else if (Player.room == 2 && tapePosition >= 1) {
                        lookAtTape = true;
                        x = 197;
                        y = 439;
                        texture = "Tape/Leaving" + (int) (15 - tapePosition);
                    }
                    break;
                case 3:
                    lookInRoom = true;
                    if (side == 0) {
                        texture = "Under Bed/Left/Left" + ((int) twitchPosition + 1);
                        x = 502;
                        y = 239;
                    } else {
                        texture = "Under Bed/Right/Right" + ((int) twitchPosition + 1);
                        x = 2173;
                        y = 178;
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
            String type;
            if (jumpscareType.equals("vinnieBedJumpscare")){
                type = "Bed Jumpscare";
            } else {
                type = "Room Jumpscare";
            }
            texture = "Jumpscare/" + type + "/Jumpscare" + jumpscareI;
            x = Player.roomPosition[0] + Player.shakingPosition;
            y = Player.roomPosition[1];
        }
        if (texture == null) return;
        batch.setColor(1, 1, 1, 1);
        batch.draw(ImageHandler.images.get("game/Vinnie/" + texture), x, y);
    }

    public static void reset(){
        moveLock = false;
        doorLock = false;
        jumpscareType = "";
        if (hitboxPosition == null){
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }
        vinnieLaughTime = 2;
        moveToPosition = 0;
        attackPosition = 0;
        jumpCase = 0;
        jumpscareTime = -1;
        tapeWeasel = false;
        doorCooldown = (float) (5 + 0.3 * (20 - ai));
        cooldownTimer = 5;
        knocks = 0;
        knockTimer = 0;
        bedPatienceTimer = 1;
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
                    if (Rat.ai == 0 || Rat.side == -1) {
                        side = random.nextInt(3);
                    } else if (Rat.side == 0){
                        side = 1 + random.nextInt(2);
                    } else if (Rat.side == 1){
                        side = 2 * random.nextInt(2);
                    } else if (Rat.side == 2){
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
                cooldownTimer = 3 + 0.3f * (20 - ai);
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
                jumps = 3;
                attackTime = 3.5f;
                attackPosition = 0;
                patienceHealthTimer = 2;
                patienceTimer = 0.75f + 0.025f * (20 - ai);
                Player.blacknessTimes = 3;
                Player.blacknessMultiplier = 6;
                timeToFlash = 0.45f + (0.05f * random.nextInt(6)) + (0.0125f * (20 - ai));
                knocks = 0;
                knockTimer = 0;
                audioClass.play("walking_in");
            }
        }
    }

    public static void doorRetreat(AudioClass audioClass){
        audioClass.play("spotted");
        peekSpotted = false;
        doorCooldown = (float) (5 + 0.3 * (20 - ai));
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
            dontSoundShake = false;
            bedPatienceTimer = 1.25f + 0.1f * (20 - ai);
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
                Player.lastCharacterAttack = "Vinnie";
            }
        }

        if (attack) {
            shaking = hitboxHit;

            float multiplier = 19;
            if (jumpAnimation > 0){
                multiplier = 23;
            }

            if (jumpTarget == 0) {

                if (shaking && moveToPosition == 0) {
                    if (attackTime > 0) {
                        attackTime -= Gdx.graphics.getDeltaTime();
                        if (attackTime < 0) {
                            attackTime = 0;
                        }
                    }

                    timeToFlash -= Gdx.graphics.getDeltaTime();
                    patienceHealthTimer += Gdx.graphics.getDeltaTime() / 4;
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
                            patienceTimer = 0.75f + 0.025f * (20 - ai);
                            if (attackPosition > 3) {
                                moveToPosition = 2 * (6 - attackPosition);
                            } else if (attackPosition < 3) {
                                moveToPosition = -2 * attackPosition;
                            } else {
                                if (random.nextInt(2) == 1) {
                                    moveToPosition = -6;
                                } else {
                                    moveToPosition = 6;
                                }
                            }
                            audioClass.play("vinnieDodge");
                            audioClass.loop("vinnieDodge", true);
                            dodgePlaying = true;
                        }
                    } else if (attackTime > 0 && timeToFlash <= 0) {
                        if (!Player.snapPosition) {
                            Player.snapPosition = true;
                            Player.snapToSide = side;
                        }

                        timeToFlash = 0.45f + (0.05f * random.nextInt(6)) + (0.0125f * (20 - ai));
                        patienceTimer = 0.75f + 0.025f * (20 - ai);
                        moveToPosition = 2 * (1 + random.nextInt(7));
                        if (random.nextInt(2) == 1) {
                            moveToPosition = -moveToPosition;
                        }
                        audioClass.play("vinnieDodge");
                        audioClass.loop("vinnieDodge", true);
                        dodgePlaying = true;
                    }
                }
            } else {
                if (jumpAnimation == 0) {
                    if (shaking && moveToPosition == 0) {
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
                        attackTime = 3.5f;
                        if (side == 1 && Cat.side == Player.side) patienceTimer = 3;
                        else patienceTimer = 1f;
                        patienceHealthTimer = 2f;
                        timeToFlash = 0.45f + (0.05f * random.nextInt(6)) + (0.0125f * (20 - ai));
                    }
                }
            }

            if (moveToPosition != 0) {
                if (moveToPosition > 0) {
//                if (Game.hourOfGame != 12){
//                    multiplier *= 1 + (Game.hourOfGame * 0.0625f);
//                }
                    moveToPosition -= Gdx.graphics.getDeltaTime() * multiplier;
                    if (!moveLock && (int) moveToPosition % 2 == 0) {
                        attackPosition++;
                        if (attackPosition > 5) {
                            attackPosition = 0;
                        }
                        moveLock = true;
                    }
                    if (moveToPosition < 0) {
                        moveToPosition = 0;
                    }
                } else if (moveToPosition < 0) {
                    moveToPosition += Gdx.graphics.getDeltaTime() * multiplier;
                    if (!moveLock && (int) moveToPosition % 2 == 0) {
                        attackPosition--;
                        if (attackPosition < 0) {
                            attackPosition = 5;
                        }
                        moveLock = true;
                    }
                    if (moveToPosition > 0) {
                        moveToPosition = 0;
                    }
                }

                if ((int) moveToPosition % 2 != 0){
                    moveLock = false;
                }
                shaking = false;
            }

            if (moveToPosition < 1 && moveToPosition > -1 && dodgePlaying){
                audioClass.stop("vinnieDodge");
                dodgePlaying = false;
            }

            if (!shaking && moveToPosition == 0 && jumpAnimation == 0) {
                if (Player.snapPosition) {
                    patienceHealthTimer -= Gdx.graphics.getDeltaTime();
                    if (patienceHealthTimer < 0) {
                        patienceHealthTimer = 0;
                    }
                }

                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer < 0) {
                    patienceTimer = 0;
                }
            }
        }

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
            if (Monstergami.side == -1 && Cat.room != 1 && Rat.room != 1) {
                cooldownTimer -= Gdx.graphics.getDeltaTime();
            }

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

            if (bedPatienceTimer > 0 && bedSpotted){
                bedPatienceTimer -= Gdx.graphics.getDeltaTime();
                if (bedPatienceTimer < 0){
                    bedPatienceTimer = 0;
                }
            }

            if (Player.room == 1 && !Player.turningAround && bedPatienceTimer == 0){
                jumpscareI = 1;
            }

            boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);

            if (cooldownTimer <= 0 || (lookingAway && Rat.room == 2 && Cat.room == 2 && Monstergami.side == -1)){
                if (lookingAway) {
                    audioClass.play("peek");
                    room = 3;
                    bedSpotted = false;
                    patienceTimer = 2f + 0.2f * (20 - ai);
                    tapeSpotted = false;
                    timeToFlash = 2.4f;
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
                doorCooldown = (float) (5 + 0.3 * (20 - ai));
                cooldownTimer = 5;
                leaveRoom = true;
                doorAnimation = 18;
                audioClass.play("vinnieLeave");
            }
        }
    }
}
