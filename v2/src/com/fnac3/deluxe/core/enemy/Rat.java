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

public class Rat {

    public static int ai;
    public static boolean doorLock;
    public static int room;
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
    public static int attackPositionTarget;
    public static float moveToPosition;
    public static float timeToFlash;
    public static int chanceToSkip;
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
    public static boolean pause;

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
        pause = Vinnie.room != 0 || Cat.room == 1 || Cat.room == 3 || Monstergami.side != -1 || Player.freeze;
        if (jumpscareI == 0) {
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
                jumpscareType = "roomJumpscare";
                Player.blacknessTimes = 1;
                Player.blacknessMultiplier = 5;
                jumpscareTime = 1.5f;
            } else if (room == 2 || room == 3){
                jumpscareType = "bedJumpscare";
                Player.blacknessTimes = 1;
                Player.blacknessMultiplier = 5;
                jumpscareTime = 1.35f;
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
                    if (jumpscareI > 28 && jumpscareType.equals("bedJumpscare")){
                        jumpscareI = 28;
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
                        hitboxPosition[0] = 481;
                        hitboxPosition[1] = 663;
                    } else if (side == 1){
                        hitboxDistance = 70;
                        hitboxPosition[0] = 1531;
                        hitboxPosition[1] = 696;
                    } else {
                        hitboxDistance = 80;
                        hitboxPosition[0] = 2525;
                        hitboxPosition[1] = 715;
                    }
                }
                break;
            case 1:
                if (attackPosition != attackPositionTarget) break;
                if (side == 0){
                    hitboxDistance = 70;
                    if (attackPosition == 0){
                        hitboxPosition[0] = 376;
                        hitboxPosition[1] = 765;
                    } else if (attackPosition == 1){
                        hitboxPosition[0] = 289;
                        hitboxPosition[1] = 621;
                    } else {
                        hitboxPosition[0] = 470;
                        hitboxPosition[1] = 595;
                    }
                } else if (side == 1){
                    hitboxDistance = 80;
                    if (attackPosition == 0){
                        hitboxPosition[0] = 1521;
                        hitboxPosition[1] = 720;
                    } else if (attackPosition == 1){
                        hitboxPosition[0] = 1315;
                        hitboxPosition[1] = 574;
                    } else {
                        hitboxPosition[0] = 1686;
                        hitboxPosition[1] = 604;
                    }
                } else {
                    hitboxDistance = 90;
                    if (attackPosition == 0){
                        hitboxPosition[0] = 2560;
                        hitboxPosition[1] = 869;
                    } else if (attackPosition == 1){
                        hitboxPosition[0] = 2402;
                        hitboxPosition[1] = 680;
                    } else {
                        hitboxPosition[0] = 2734;
                        hitboxPosition[1] = 651;
                    }
                }
                break;
            case 3:
                hitboxDistance = 80;
                if (side == 0){
                    hitboxPosition[0] = 710;
                    hitboxPosition[1] = 450;
                } else {
                    hitboxPosition[0] = 2361;
                    hitboxPosition[1] = 468;
                }
                break;
        }

        hitboxDistance = Utils.setHitboxDistance(data, hitboxDistance);
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
                            x = 380;
                            y = 372;
                        } else if (side == 1) {
                            x = 1411;
                            y = 382;
                        } else {
                            x = 2483;
                            y = 321;
                        }
                    } else if (doorAnimation >= 1) {
                        texture = "Leaving/" + sideTexture + "/Leaving" + (int) (19 - doorAnimation);
                        if (side == 0) {
                            x = 280;
                            y = 376;
                        } else {
                            x = 2490;
                            y = 285;
                        }
                    }
                    break;
                case 1:
                    lookInRoom = true;
                    String position = null;
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
                        if ((attackPosition == 2 && attackPositionTarget == 0)
                                || (attackPosition < attackPositionTarget
                                && !(attackPosition == 0 && attackPositionTarget == 2))) {
                            move = 3 - (int) moveToPosition;
                        }
                        if ((attackPosition == 0 && attackPositionTarget == 1)
                                || (attackPosition == 1 && attackPositionTarget == 0)) {
                            position = "MoveDownLeft" + move;
                        } else if ((attackPosition == 1 && attackPositionTarget == 2)
                                || (attackPosition == 2 && attackPositionTarget == 1)) {
                            position = "MoveLeftRight" + move;
                        } else if ((attackPosition == 0 && attackPositionTarget == 2)
                                || (attackPosition == 2 && attackPositionTarget == 0)) {
                            position = "MoveDownRight" + move;
                        }
                    }
                    texture = "Battle/" + sideTexture + "/" + position;
                    if (side == 0) {
                        x = 142;
                        y = 327;
                    } else if (side == 1) {
                        x = 1144;
                        y = 45;
                    } else {
                        x = 2165;
                        y = 195;
                    }
                    break;
                case 2:
                    if (Player.room == 1 && Monstergami.side != 3) {
                        lookUnderBed = true;
                        if (side == 0) {
                            texture = "Under Bed/Bed1";
                            y = 179;
                        } else {
                            texture = "Under Bed/Bed2";
                            x = 1438;
                            y = 214;
                        }
                    } else if (Player.room == 2 && tapePosition >= 1) {
                        lookAtTape = true;
                        x = 194;
                        y = 374;
                        texture = "Tape/Leaving" + (int) (13 - tapePosition);
                    }
                    break;
                case 3:
                    lookInRoom = true;
                    if (side == 0) {
                        texture = "Under Bed/Left/Left" + ((int) twitchPosition + 1);
                        x = 415;
                        y = 217;
                    } else {
                        texture = "Under Bed/Right/Right" + ((int) twitchPosition + 1);
                        x = 2128;
                        y = 195;
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
            if (jumpscareType.equals("bedJumpscare")){
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
        batch.draw(ImageHandler.images.get("game/Rat/" + texture), x, y);
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
        jumpscareTime = -1;
        tapeWeasel = false;
        doorCooldown = (float) (4 + 0.3 * (20 - ai));
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
    }

    public static void doorMechanic(Random random, AudioClass audioClass){
        if (doorCooldown > 0){
            doorCooldown -= Gdx.graphics.getDeltaTime();
            if (pause && !doorLock && doorCooldown <= 0){
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
                    if (Vinnie.ai == 0 || Vinnie.side == -1) {
                        side = random.nextInt(3);
                    } else if (Vinnie.side == 0){
                        side = 1 + random.nextInt(2);
                    } else if (Vinnie.side == 1){
                        side = 2 * random.nextInt(2);
                    } else if (Vinnie.side == 2){
                        side = random.nextInt(2);
                    }

                    if (cooldownTimer > 0.375f) {
                        knockHard = (int) cooldownTimer <= 3;
                        if (cooldownTimer <= 0.5f){
                            knocks = 2;
                        } else {
                            knocks = 3;
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
                attackTime = 11 - 0.05f * (20 - ai);
                attackPosition = 0;
                attackPositionTarget = 0;
                patienceHealthTimer = 2f;
                patienceTimer = 0.75f;
                Player.blacknessTimes = 3;
                Player.blacknessMultiplier = 6;
                timeToFlash = 0.85f;
                chanceToSkip = 10;
                knocks = 0;
                knockTimer = 0;
                audioClass.play("walking_in");
            }
        }
    }

    public static void doorRetreat(AudioClass audioClass){
        audioClass.play("spotted");
        peekSpotted = false;
        doorCooldown = (float) (4 + 0.3 * (20 - ai));
        if (Game.doorTurn != 0) cooldownTimer = 5;
        doorLock = false;
    }

    public static void knockAtDoor(AudioClass audioClass){
        if (knockTimer == 0) {
            if (knocks > 0) {
                knocks--;
                if (knockHard){
                    audioClass.play("hard_knock");
                } else {
                    audioClass.play("knock");
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
            Player.blacknessMultiplier = 1.25f;
            room = 2;
            if (Vinnie.ai != 0) Game.doorTurn = 1;
            dontSoundShake = false;
            shaking = false;
            bedPatienceTimer = 5;
            cooldownTimer = 10;
            audioClass.play("crawl");
            tapeSpotted = false;
            if (!tapeWeasel && !Player.tapeStolen) {
                tapePosition = 12f;
                if (data.hardCassette) timeUntilWeasel = 0.01f;
                else timeUntilWeasel = 0.25f + random.nextInt(2) + random.nextFloat() + (0.1f * (20 - ai));
            }

            if (Cat.room == 2){
                if (Cat.side == 0) side = 2;
                else side = 0;
            } else side = 2 * random.nextInt(2);
        }

        if (attackTime != 0 && !attack && Player.flashlightAlpha > 0 && !Player.turningAround && Player.room == 0) {
            attack = Player.inititiateSnapPosition(side);
            if (attack) Player.lastCharacterAttack = "RatCat";
        }

        if (attack) {
            shaking = hitboxHit;

            if (attackTime > 0) {
                attackTime -= Gdx.graphics.getDeltaTime();
                if (attackTime < 0) {
                    attackTime = 0;
                }
            }

            if (shaking) {
                timeToFlash -= Gdx.graphics.getDeltaTime();
                patienceHealthTimer += Gdx.graphics.getDeltaTime() / 4;
                if (timeToFlash <= 0 && attackTime == 0) {
                    Player.snapPosition = false;
                    attack = false;
                    dontSoundShake = true;
                    audioClass.play("thunder");
                    Player.blacknessTimes = 3;
                    Player.blacknessMultiplier = 6;
                    Player.blacknessDelay = 0.5f;
                    Player.freeze = true;
                }
            } else if (moveToPosition == 0){
                patienceHealthTimer -= Gdx.graphics.getDeltaTime();
                if (patienceHealthTimer < 0){
                    patienceHealthTimer = 0;
                }

                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer < 0){
                    patienceTimer = 0;
                }
            }

            if (attackTime > 0 && moveToPosition == 0 && timeToFlash <= 0) {
                if (!Player.snapPosition){
                    Player.snapPosition = true;
                    Player.snapToSide = side;
                }

                if (chanceToSkip > 0 && random.nextInt(chanceToSkip) != 0) {
                    timeToFlash = 0.35f + (0.05f * random.nextInt(5)) + 0.015f * (20 - ai);
                    chanceToSkip--;
                } else {
                    timeToFlash = 0;
                    chanceToSkip = 10;
                }
                patienceTimer = 0.65f + 0.025f * (20 - ai);
                moveToPosition = 3;

                if (attackPosition == 0) {
                    attackPositionTarget = 1 + random.nextInt(2);
                } else if (attackPosition == 1) {
                    attackPositionTarget = 2 * random.nextInt(2);
                } else {
                    attackPositionTarget = random.nextInt(2);
                }
            }

            if (moveToPosition > 0) {
                if (moveToPosition == 3) {
                    audioClass.play("dodge");
                }
                float multiplier = 22f;
                moveToPosition -= Gdx.graphics.getDeltaTime() * multiplier;
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

        if (cooldownTimer == 0 || patienceTimer == 0 || patienceHealthTimer == 0){
            jumpscareI = 1;
        }
    }

    public static void bedMechanic(Data data, AudioClass audioClass){
        if (cooldownTimer > 0 && !Player.freeze){
            if (Monstergami.side == -1 && Cat.room != 1 && Vinnie.room != 1) {
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

            if (cooldownTimer > 0) return;
            boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);

            if (lookingAway) {
                cooldownTimer = 2f + 0.2f * (20 - ai);
                audioClass.play("peek");
                room = 3;
                bedSpotted = false;
                patienceTimer = 2f + 0.2f * (20 - ai);
                tapeSpotted = false;
                timeToFlash = 2.4f;
                Player.blacknessMultiplier = 6;
            } else {
                jumpscareI = 1;
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
            } else {
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
                doorCooldown = (float) (4 + 0.3 * (20 - ai));
                cooldownTimer = 5;
                leaveRoom = true;
                doorAnimation = 18;
                audioClass.play("leave");
            }
        }
    }
}
