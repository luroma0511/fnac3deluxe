package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class ShadowCat {

    private static String catBreath;
    public static boolean twitchyCat;
    public static boolean wait;
    public static boolean bedSideDone;
    public static boolean active;
    public static boolean skipped;
    public static int room;
    public static int side = -1;
    public static float cooldownTimer;
    public static int attackPosition;
    public static float twitchPosition;
    public static boolean shaking;
    public static boolean twitchingNow;
    public static int attackPositionTarget;
    public static float moveToPosition;
    public static float timeToFlash;
    public static boolean attack;
    public static float teleportTime;
    public static int teleports;
    public static int teleportTarget;
    public static boolean bedSpotted;
    public static float patienceTimer;
    public static float patienceHealthTimer;
    public static float bedPatienceTimer;
    public static boolean tapeSpotted;
    public static float timeUntilWeasel;
    public static boolean tapeWeasel;
    public static float tapePosition;
    public static boolean jumpscare;
    public static float jumpscareTimer;
    public static float jumpscareTime = 0.95f;
    public static int jumpscareI;
    public static boolean pause;
    public static float bedPosition;
    public static int bedTarget;
    public static boolean catHum;
    public static int middleAttackMoves;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static boolean hitboxHit;

    public static void input(){
        if (jumpscareI == 0 && !Player.turningAround && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Random random, Data data, AudioClass audioClass){
        pause = ShadowRat.room == 1 || ShadowRat.room == 3 || Player.freeze;

        if (!catHum){
            audioClass.play("cat");
            audioClass.setVolume("cat", 0);
            audioClass.setPitch("cat", 1);
            audioClass.loop("cat", true);
            catHum = true;
        }

        float catVolume = audioClass.getVolume("cat");
        float catPitch = audioClass.getPitch("cat");

        if (room == 1 || room == 3) {
            if (catVolume < 0.5f) {
                audioClass.setVolume("cat", catVolume + Gdx.graphics.getDeltaTime());
                if (audioClass.getVolume("cat") > 0.5f) audioClass.setVolume("cat", 0.5f);
            }
            if (room == 1) audioClass.setPitch("cat", catPitch + Gdx.graphics.getDeltaTime() / 75);
            else audioClass.setPitch("cat", catPitch - Gdx.graphics.getDeltaTime() / 10);
        } else if (catVolume < 0.15f){
            audioClass.setVolume("cat", catVolume + Gdx.graphics.getDeltaTime() / 2);
            if (audioClass.getVolume("cat") > 0.15f) audioClass.setVolume("cat", 0.15f);
        }

        if (jumpscareI == 0) {
            switch (room) {
                case 1:
                    roomMechanic(random, audioClass);
                    break;
                case 2:
                    bedMechanic(data, random, audioClass);
                    break;
                case 3:
                    crouchMechanic(data, random, audioClass);
                    break;
                case 4:
                    farBedMechanic(random, audioClass);
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
            audioClass.setPitch("shadowJumpscare", 0.95f);
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

        if (room != 3 || !Player.freeze) {
            if (shaking && !twitchingNow) {
                audioClass.play("twitch");
                audioClass.loop("twitch", true);
                twitchingNow = true;
            } else if (!shaking && twitchingNow) {
                audioClass.stop("twitch");
                twitchingNow = false;
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
            if (room == 1 && attack){
                hitboxHit = true;
            } else if (room == 3 && timeToFlash > 0 && moveToPosition == 0){
                hitboxHit = true;
            } else if (room == 4 && bedPosition == 0 && ShadowRat.room != 1){
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
            case 1:
                if (attackPosition != attackPositionTarget) break;
                if (side == 0) {
                    hitboxDistance = 70;
                    if (attackPosition == 0) {
                        hitboxPosition[0] = 367;
                        hitboxPosition[1] = 749;
                    } else if (attackPosition == 1) {
                        hitboxPosition[0] = 289;
                        hitboxPosition[1] = 635;
                    } else if (attackPosition == 2) {
                        hitboxPosition[0] = 466;
                        hitboxPosition[1] = 624;
                    } else {
                        hitboxPosition[0] = 369;
                        hitboxPosition[1] = 534;
                    }
                } else if (side == 1) {
                    hitboxDistance = 80;
                    if (attackPosition == 0) {
                        hitboxPosition[0] = 1532;
                        hitboxPosition[1] = 720;
                    } else if (attackPosition == 1) {
                        hitboxPosition[0] = 1342;
                        hitboxPosition[1] = 579;
                    } else if (attackPosition == 2) {
                        hitboxPosition[0] = 1712;
                        hitboxPosition[1] = 601;
                    } else {
                        hitboxPosition[0] = 1532;
                        hitboxPosition[1] = 463;
                    }
                } else {
                    hitboxDistance = 90;
                    if (attackPosition == 0) {
                        hitboxPosition[0] = 2561;
                        hitboxPosition[1] = 892;
                    } else if (attackPosition == 1) {
                        hitboxPosition[0] = 2397;
                        hitboxPosition[1] = 718;
                    } else if (attackPosition == 2) {
                        hitboxPosition[0] = 2753;
                        hitboxPosition[1] = 672;
                    } else {
                        hitboxPosition[0] = 2552;
                        hitboxPosition[1] = 553;
                    }
                }
                break;
            case 3:
                if (attackPosition != attackPositionTarget) break;
                hitboxDistance = 100;
                if (attackPosition == 0) {
                    hitboxPosition[0] = 1425;
                    hitboxPosition[1] = 795;
                } else if (attackPosition == 1) {
                    hitboxPosition[0] = 1203;
                    hitboxPosition[1] = 701;
                } else {
                    hitboxPosition[0] = 1691;
                    hitboxPosition[1] = 695;
                }
                break;
            case 4:
                if (side == 0){
                    hitboxDistance = 70;
                    hitboxPosition[0] = 253;
                    hitboxPosition[1] = 687;
                } else {
                    hitboxDistance = 90;
                    hitboxPosition[0] = 2794;
                    hitboxPosition[1] = 619;
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

    public static boolean render(SpriteBatch batch){
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
                case 1:
                    lookInRoom = true;
                    String position = null;
                    if (attackPosition == attackPositionTarget) {
                        position = switch (attackPosition) {
                            case 0 -> "Up";
                            case 1 -> "Left";
                            case 2 -> "Right";
                            case 3 -> "Down";
                            default -> null;
                        };
                        position += ((int) twitchPosition + 1);
                    } else {
                        int move = (int) moveToPosition;
                        if (attackPosition == 0 && attackPositionTarget == 1) {
                            position = "MoveDownLeft" + (3 - move);
                        } else if (attackPosition == 1 && attackPositionTarget == 0) {
                            position = "MoveDownLeft" + move;
                        } else if (attackPosition == 1 && attackPositionTarget == 2) {
                            position = "MoveLeftRight" + (3 - move);
                        } else if (attackPosition == 2 && attackPositionTarget == 1) {
                            position = "MoveLeftRight" + move;
                        } else if (attackPosition == 2 && attackPositionTarget == 0) {
                            position = "MoveDownRight" + (3 - move);
                        } else if (attackPosition == 0 && attackPositionTarget == 2) {
                            position = "MoveDownRight" + move;
                        } else if (attackPosition == 3 && attackPositionTarget == 1) {
                            position = "MoveUpLeft" + move;
                        } else if (attackPosition == 1 && attackPositionTarget == 3) {
                            position = "MoveUpLeft" + (3 - move);
                        } else if (attackPosition == 2 && attackPositionTarget == 3) {
                            position = "MoveUpRight" + move;
                        } else if (attackPosition == 3 && attackPositionTarget == 2) {
                            position = "MoveUpRight" + (3 - move);
                        }
                    }
                    texture = "Battle/" + sideTexture + "/" + position;
                    if (side == 0){
                        x = 154;
                        y = 276;
                    } else if (side == 1) {
                        x = 1202;
                        y = 55;
                    } else if (side == 2) {
                        x = 2216;
                        y = 122;
                    }
                    break;
                case 2:
                    if (Player.room == 1 && Monstergami.side != 3) {
                        lookUnderBed = true;
                        y = 167;
                        if (side == 0) {
                            texture = "Under Bed/Bed1";
                        } else {
                            texture = "Under Bed/Bed2";
                            x = 1094;
                        }
                    } else if (Player.room == 2 && tapePosition >= 1) {
                        lookAtTape = true;
                        x = 95;
                        y = 373;
                        texture = "Tape/Leaving" + (int) (13 - tapePosition);
                    }
                    break;
                case 3:
                    position = null;
                    if (attackPosition == attackPositionTarget) {
                        position = switch (attackPosition) {
                            case 0 -> "Up";
                            case 1 -> "Left";
                            case 2 -> "Right";
                            default -> null;
                        };
                        position += ((int) twitchPosition + 1);
                    } else {
                        int move = (int) moveToPosition;
                        if (attackPosition == 0 && attackPositionTarget == 1) {
                            position = "MoveDownLeft" + (3 - move);
                        } else if (attackPosition == 1 && attackPositionTarget == 0) {
                            position = "MoveDownLeft" + move;
                        } else if (attackPosition == 1 && attackPositionTarget == 2) {
                            position = "MoveLeftRight" + (3 - move);
                        } else if (attackPosition == 2 && attackPositionTarget == 1) {
                            position = "MoveLeftRight" + move;
                        } else if (attackPosition == 2 && attackPositionTarget == 0) {
                            position = "MoveDownRight" + (3 - move);
                        } else if (attackPosition == 0 && attackPositionTarget == 2) {
                            position = "MoveDownRight" + move;
                        }
                    }
                    lookInRoom = true;
                    texture = "Battle/Second Middle/" + position;
                    x = 815;
                    break;
                case 4:
                    lookInRoom = true;
                    String condition;
                    if (bedPosition >= 1){
                        condition = "Retreat" + (int) bedPosition;
                    } else {
                        condition = "Shaking" + ((int) twitchPosition + 1);
                    }
                    if (side == 0){
                        texture = "Retreat/Retreat Left/" + condition;
                        y = 303;
                    } else if (side == 2){
                        texture = "Retreat/Retreat Right/" + condition;
                        x = 2536;
                        y = 214;
                    }
                    break;
            }
            if (texture == null) return false;
            boolean passed = lookInRoom && !Player.turningAround && Player.room == 0;
            if (!passed) {
                passed = lookUnderBed && !Player.turningAround;
            }

            if (!passed) {
                passed = lookAtTape && !Player.turningAround;
            }

            if (!passed) return false;
        } else {
            texture = "Jumpscare/Jumpscare" + jumpscareI;
            x = Player.roomPosition[0] + Player.shakingPosition;
            y = Player.roomPosition[1];
        }

        batch.setColor(1, 1, 1, 1);
        batch.draw(ImageHandler.images.get("game/Shadow Cat/New/" + texture), x, y);

        if ((room == 1 || room == 3 || (room == 4 && side == 2)) && !jumpscare){
            batch.draw(ImageHandler.images.get("game/room/FullRoomBed"), 0, 0);
        }
        return true;
    }

    public static void reset(AudioClass audioClass, Random random) {
        bedSideDone = false;
        skipped = false;
        catHum = false;
        if (hitboxPosition == null) {
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }
        tapeWeasel = false;
        middleAttackMoves = 0;
        bedPatienceTimer = 1;
        bedSpotted = false;
        jumpscare = false;
        attack = false;
        shaking = false;
        twitchingNow = false;
        hitboxHit = false;
        jumpscareI = 0;
        jumpscareTime = 0.95f;
        attackPosition = 0;
        attackPositionTarget = 0;
        moveToPosition = 0;

        if (active) {
            side = 2 * random.nextInt(2);
            cooldownTimer = 5;
            room = 2;
            attack = false;
            audioClass.play("crawl");
        } else {
            room = 0;
        }
    }

    public static void roomMechanic(Random random, AudioClass audioClass) {
        shaking = hitboxHit;

        if (attack) {
            if (Player.snapPosition && teleportTime > 0) {
                teleportTime -= Gdx.graphics.getDeltaTime();
                if (teleportTime < 0) {
                    teleportTime = 0;
                }
            }

            if (shaking) {
                timeToFlash -= Gdx.graphics.getDeltaTime();
                patienceHealthTimer += Gdx.graphics.getDeltaTime() / 2;
                if (timeToFlash <= 0 && teleportTime == 0) {
                    Player.snapPosition = false;
                    shaking = false;
                    Player.blacknessTimes = 1;
                    Player.blacknessMultiplier = 6;
                    patienceTimer = 1.75f;

                    int previousSide = side;

                    if (side == 0 || side == 2) side = 1;
                    else side = 2 * random.nextInt(2);

                    if (previousSide < side) audioClass.play("dodgeRight");
                    else audioClass.play("dodgeLeft");

                    int difference = previousSide - side;
                    if (difference == -2 || difference == 2) patienceTimer += 0.75f;


                    if (teleports == teleportTarget) {
                        room = 3;
                        if (!twitchyCat) timeToFlash = 2.75f;
                        else {
                            timeToFlash = 0.65f;
                            patienceHealthTimer = 2f;
                            middleAttackMoves = 8;
                        }
                        attackPosition = 0;
                    } else {
                        teleportTime = 7;
                        teleports++;
                        patienceHealthTimer = 2f;
                        timeToFlash = 0.5f;
                        attackPosition = 1 + random.nextInt(2);
                    }
                    attackPositionTarget = attackPosition;
                    return;
                }
            } else if (moveToPosition == 0){
                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer < 0){
                    patienceTimer = 0;
                    jumpscareI = 1;
                }

                if (Player.snapPosition) {
                    patienceHealthTimer -= Gdx.graphics.getDeltaTime();
                    if (patienceHealthTimer < 0) {
                        patienceHealthTimer = 0;
                        jumpscareI = 1;
                    }
                }
            }

            if (moveToPosition == 0 && timeToFlash <= 0) {
                if (!Player.snapPosition){
                    Player.snapPosition = true;
                    Player.snapToSide = side;
                }
                patienceTimer = 0.65f;
                if (random.nextInt(5) == 2 && !skipped){
                    timeToFlash = 0;
                    skipped = true;
                } else {
                    if (twitchyCat) timeToFlash = 0.15f + 0.15f * random.nextInt(2);
                    else timeToFlash = 0.15f + 0.15f * random.nextInt(3);
                    skipped = false;
                }
                moveToPosition = 3;

                if (attackPosition == 0 || attackPosition == 3) {
                    attackPositionTarget = 1 + random.nextInt(2);
                } else if (attackPosition == 1) {
                    if (random.nextInt(2) == 0){
                        attackPositionTarget = 0;
                    } else {
                        attackPositionTarget = 2 + random.nextInt(2);
                    }
                } else if (attackPosition == 2){
                    if (random.nextInt(2) == 1){
                        attackPositionTarget = 3;
                    } else {
                        attackPositionTarget = random.nextInt(2);
                    }
                }
            }

            if (moveToPosition > 0) {
                if (moveToPosition == 3) {
                    audioClass.play("dodge");
                }
                moveToPosition -= Gdx.graphics.getDeltaTime() * 22;
                if (moveToPosition < 1 && attackPosition != attackPositionTarget) {
                    attackPosition = attackPositionTarget;
                }
                if (moveToPosition < 0) {
                    moveToPosition = 0;
                }
                shaking = false;
            }
        } else {
            if (cooldownTimer > 0 && teleports != teleportTarget){
                cooldownTimer -= Gdx.graphics.getDeltaTime();
                if (cooldownTimer < 0){
                    cooldownTimer = 0;
                    jumpscareI = 1;
                }
            }

            if (teleports == 0 && !Player.turningAround && Player.room == 0) {
                attack = Player.inititiateSnapPosition(side);
                Player.lastCharacterAttack = "Shadow";
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
    }

    public static void bedMechanic(Data data, Random random, AudioClass audioClass){
        if (cooldownTimer > 0 && !Player.freeze) {
            if (Monstergami.side == -1) {
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

            if (bedPatienceTimer > 0 && bedSpotted) {
                bedPatienceTimer -= Gdx.graphics.getDeltaTime();
                if (bedPatienceTimer < 0) {
                    bedPatienceTimer = 0;
                }
            }

            if (Player.room == 1 && !Player.turningAround && bedPatienceTimer == 0) {
                jumpscareI = 1;
            }

            if (cooldownTimer <= 0) {
                if (ShadowRat.room == 1 && ShadowRat.attack && !bedSideDone){
                    audioClass.play("catPeek");
                    bedSideDone = true;
                    room = 4;
                    cooldownTimer = 6;
                    bedSpotted = false;
                    timeToFlash = 0;
                    side = 2 * random.nextInt(2);
                    bedTarget = 21;
                    if (side == 0){
                        catBreath = "catLeft";
                    } else {
                        catBreath = "catRight";
                    }
                    audioClass.play(catBreath);
                    audioClass.loop(catBreath, true);
                    audioClass.setVolume(catBreath, 0);
                    bedPosition = bedTarget - 0.01f;
                } else if (ShadowRat.room == 0 && ((Player.side == 0 && side == 2) || (Player.side == 2 && side == 0))) {
                    audioClass.play("peek");
                    tapeSpotted = false;
                    bedSideDone = false;
                    room = 1;
                    teleportTarget = 2;
                    timeToFlash = 0.5f;
                    Player.lastCharacterAttack = "Shadow";
                    bedSpotted = false;
                    cooldownTimer = 2f;
                    patienceHealthTimer = 2f;
                    patienceTimer = 2f;
                    teleports = 0;
                    teleportTime = 7;
                    Player.blacknessMultiplier = 6;
                    attackPosition = 3;
                    attackPositionTarget = 3;
                } else {
                    jumpscareI = 1;
                }
            }
        }
    }

    public static void crouchMechanic(Data data, Random random, AudioClass audioClass){
        if (attack) {
            shaking = hitboxHit;

            if (shaking) {
                timeToFlash -= Gdx.graphics.getDeltaTime();
                patienceHealthTimer += Gdx.graphics.getDeltaTime() / 2;
                if (timeToFlash <= 0 && moveToPosition == 0){
                    if (middleAttackMoves == 0) {
                        timeToFlash = 0;
                        Player.freeze = true;
                        audioClass.stop("cat");
                        audioClass.stop("twitch");
                        audioClass.play("thunder");
                        Player.blacknessTimes = 3;
                        Player.blacknessDelay = 0.5f;
                    }
                }
            } else if (moveToPosition == 0){
                if (!Player.freeze) {
                    patienceTimer -= Gdx.graphics.getDeltaTime();
                    if (patienceTimer < 0) {
                        patienceTimer = 0;
                        jumpscareI = 1;
                    }
                }

                if (middleAttackMoves > 0 && middleAttackMoves < 8) {
                    patienceHealthTimer -= Gdx.graphics.getDeltaTime();
                    if (patienceHealthTimer < 0) {
                        patienceHealthTimer = 0;
                        jumpscareI = 1;
                    }
                }
            }

            if (moveToPosition == 0 && timeToFlash <= 0 && !Player.freeze) {
                if (random.nextInt(5) == 2 && !skipped){
                    timeToFlash = 0;
                    skipped = true;
                } else {
                    timeToFlash = 0.15f + 0.15f * random.nextInt(2);
                    middleAttackMoves--;
                    skipped = false;
                }
                patienceTimer = 0.65f;
                moveToPosition = 3;

                if (attackPosition == 0) {
                    attackPositionTarget = 1 + random.nextInt(2);
                } else if (attackPosition == 1) {
                    attackPositionTarget = 2 * random.nextInt(2);
                } else if (attackPosition == 2){
                    attackPositionTarget = random.nextInt(2);
                }
            }

            if (moveToPosition > 0) {
                if (moveToPosition == 3) {
                    audioClass.play("dodge");
                }
                moveToPosition -= Gdx.graphics.getDeltaTime() * 22;
                if (moveToPosition < 1 && attackPosition != attackPositionTarget) {
                    attackPosition = attackPositionTarget;
                }
                if (moveToPosition < 0) {
                    moveToPosition = 0;
                }
                shaking = false;
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

        if (timeToFlash == 0 && Player.freeze && Player.blacknessTimes == 0) {
            if (Player.blacknessMultiplier != 1.25f) {
                Player.blacknessMultiplier = 1.25f;
                attack = false;
                catHum = false;
                room = 2;
                if (data.challenge4 && Monstergami.cooldownTimer == 0){
                    Monstergami.pause = false;
                }
                cooldownTimer = 15;
                bedPatienceTimer = 1.25f;
                audioClass.play("crawl");
                shaking = false;
                twitchingNow = false;
                side = 2 * random.nextInt(2);
                tapeSpotted = false;
                if (!tapeWeasel) {
                    tapePosition = 12f;
                    if (data.hardCassette) timeUntilWeasel = 0.01f;
                    else timeUntilWeasel = 0.25f + random.nextInt(2) + random.nextFloat();
                }
            } else if (Player.blacknessDelay == 0) {
                Player.freeze = false;
            }
        }
    }

    public static void farBedMechanic(Random random, AudioClass audioClass){
        audioClass.setVolume(catBreath, ((bedTarget - bedPosition) / bedTarget) * 0.75f);
        float flashTimerTarget = 1.5f;
        if (twitchyCat) flashTimerTarget++;

        if (timeToFlash == flashTimerTarget){
            bedPosition += Gdx.graphics.getDeltaTime() * 45;
            if (bedPosition >= bedTarget || Player.room == 1){
                bedPosition = bedTarget;
                wait = true;
                room = 2;
                timeToFlash = 0;
                audioClass.stop(catBreath);
                bedSpotted = false;
                side = 2 * random.nextInt(2);
                cooldownTimer = 15;
                if (ShadowRat.side == side && (ShadowRat.room == 1 || ShadowRat.room == 2)){
                    if (side == 0){
                        side = 2;
                    } else {
                        side = 0;
                    }
                }
                bedPatienceTimer = 1.25f;
            }
        } else if (cooldownTimer > 0){
            shaking = hitboxHit;

            if (bedPosition > 0){
                bedPosition -= Gdx.graphics.getDeltaTime() * 45;
                if (bedPosition <= 0) bedPosition = 0;
            } else if (shaking){
                twitchPosition += Gdx.graphics.getDeltaTime() * 30;
                if (twitchPosition >= 2) {
                    twitchPosition = 0;
                }

                timeToFlash += Gdx.graphics.getDeltaTime();
                if (timeToFlash >= flashTimerTarget){
                    bedPosition = 1;
                    timeToFlash = flashTimerTarget;
                    shaking = false;
                    twitchingNow = false;
                    audioClass.stop("twitch");
                }
            } else {
                timeToFlash -= Gdx.graphics.getDeltaTime();
                if (timeToFlash < 0){
                    timeToFlash = 0;
                }

                twitchPosition = 0;
                if (!pause) cooldownTimer -= Gdx.graphics.getDeltaTime();

                if (cooldownTimer < 0){
                    cooldownTimer = 0;
                    jumpscareI = 1;
                }
            }
        }
    }
}
