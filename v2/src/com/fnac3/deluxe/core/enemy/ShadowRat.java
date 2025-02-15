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

public class ShadowRat {

    public static boolean active;
    public static int room;
    public static int side = -1;
    public static float doorCooldown;
    public static float cooldownTimer;
    public static boolean leaveRoom;
    public static float doorAnimation;
    public static boolean doorLock;
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
    public static int teleports;
    public static int teleportTarget;
    public static boolean bedSpotted;
    public static float patienceTimer;
    public static float bedPatienceTimer;
    public static boolean tapeSpotted;
    public static float timeUntilWeasel;
    public static boolean tapeWeasel;
    public static float tapePosition;
    public static boolean jumpscare;
    public static float jumpscareTimer;
    public static float jumpscareTime;
    public static int jumpscareI;

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
            jumpscare = true;
            jumpscareTimer = 0.05f;
            Player.blacknessTimes = 1;
            Player.blacknessMultiplier = 12;
            audioClass.stopAllSounds();
            audioClass.play("shadowJumpscare");
        } else if (jumpscareTime > 0){
            jumpscareTime -= Gdx.graphics.getDeltaTime();
            if (jumpscareTime <= 0) {
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
                            x = 2484;
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
            if (texture == null) return;
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
        batch.draw(ImageHandler.images.get("game/Shadow Rat/" + texture), x, y);
    }

    public static void reset(){
        doorLock = false;
        if (hitboxPosition == null){
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }
        tapeWeasel = false;
        doorCooldown = 6;
        cooldownTimer = 5;
        knocks = 0;
        knockTimer = 0;
        room = 0;
        teleports = 0;
        attackTime = 0;
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
        jumpscareTime = 0.9f;
    }

    public static void doorMechanic(Random random, AudioClass audioClass){
        if (doorCooldown > 0 && Game.doorTurn == 0 && !Player.freeze){
            doorCooldown -= Gdx.graphics.getDeltaTime();
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
                if (cooldownTimer > 0) {
                    side = random.nextInt(3);

                    if (!ShadowCat.active || ShadowCat.room != 1) {
                        side = random.nextInt(3);
                    } else if (ShadowCat.side == 0){
                        side = 1 + random.nextInt(2);
                    } else if (ShadowCat.side == 1){
                        side = 2 * random.nextInt(2);
                    } else if (ShadowCat.side == 2){
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
                if (ShadowCat.room == 1) cooldownTimer -= Gdx.graphics.getDeltaTime() / 2;
                else cooldownTimer -= Gdx.graphics.getDeltaTime();
            }

            if (doorAnimation < 13) {
                doorAnimation += Gdx.graphics.getDeltaTime() * 25;
                if (doorAnimation > 13){
                    doorAnimation = 13;
                }
            }
            if (cooldownTimer <= 0 && Player.blackness == 1 && Player.blacknessTimes == 0){
                cooldownTimer = 5;
                room = 1;
                teleportTarget = 1;
                attackPosition = 0;
                attackPositionTarget = 0;
                patienceTimer = 0.75f;
                Player.blacknessTimes = 3;
                Player.blacknessMultiplier = 6;
                attackTime = 3.5f;
                teleports = 0;
                int multiplier = Game.hourOfGame / 2;
                if (Game.hourOfGame == 12) multiplier = 0;
                timeToFlash = (0.65f - (0.1f * multiplier));
                chanceToSkip = 4;
                audioClass.play("walking_in");
                if (side == ShadowCat.side && ShadowCat.room == 1) jumpscareI = 1;
            }
        }
    }

    public static void doorRetreat(AudioClass audioClass){
        audioClass.play("spotted");
        peekSpotted = false;
        doorCooldown = 6;
        if (Game.doorTurn != 0) cooldownTimer = 5;
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
        if (cooldownTimer > 0 && !attack && teleports != teleportTarget){
            if (Monstergami.side == -1) {
                cooldownTimer -= Gdx.graphics.getDeltaTime();
                if (cooldownTimer < 0) {
                    cooldownTimer = 0;
                }
            }
        }

        if (teleports == teleportTarget && !attack && Player.blacknessTimes == 0) {
            Player.blacknessMultiplier = 1.25f;
            room = 2;
            if (ShadowVinnie.ai != 0) Game.doorTurn = 1;
            dontSoundShake = false;
            shaking = false;
            bedPatienceTimer = 4;
            cooldownTimer = 10;
            audioClass.play("crawl");
            tapeSpotted = false;
            if (!tapeWeasel) {
                tapePosition = 12f;
                if (data.hardCassette) timeUntilWeasel = 0.01f;
                else timeUntilWeasel = 0.25f + random.nextInt(2) + random.nextFloat();
            }

            if (ShadowCat.active && ShadowCat.room == 2) {
                if (ShadowCat.side == 0) side = 2;
                else side = 0;
            } else side = 2 * random.nextInt(2);
        }

        if (teleports == 0 && !attack && Player.flashlightAlpha > 0 && !Player.turningAround && Player.room == 0) {
            attack = Player.inititiateSnapPosition(side);
            if (attack) Player.lastCharacterAttack = "Shadow";
        }

        if (attack) {
            shaking = hitboxHit;

            if (shaking) {
                attackTime -= Gdx.graphics.getDeltaTime();
                timeToFlash -= Gdx.graphics.getDeltaTime();
                patienceTimer += Gdx.graphics.getDeltaTime();
                if (timeToFlash <= 0 && attackTime <= 0) {
                    Player.snapPosition = false;
                    if (teleports == teleportTarget) {
                        attack = false;
                        dontSoundShake = true;
                        audioClass.play("thunder");
                        Player.blacknessTimes = 3;
                        Player.blacknessMultiplier = 6;
                        Player.blacknessDelay = 0.5f;
                        Player.freeze = true;
                    } else {
                        attackTime = 3.5f;
                        teleports++;
                        patienceTimer = 1.75f;
                        Player.blacknessTimes = 1;
                        Player.blacknessMultiplier = 6;

                        int previousSide = side;

                        if (side == 0){
                            if (ShadowCat.room == 4 && ShadowCat.side == 2) side = 2;
                            else side = random.nextInt(2) + 1;
                        } else if (side == 1){
                            if (ShadowCat.room == 4){
                                if (ShadowCat.side == 0) side = 0;
                                else side = 2;
                            } else side = 2 * random.nextInt(2);
                        } else {
                            if (ShadowCat.room == 4 && ShadowCat.side == 0) side = 0;
                            else side = random.nextInt(2);
                        }

                        if (previousSide < side) audioClass.play("dodgeRight");
                        else audioClass.play("dodgeLeft");

                        int difference = previousSide - side;
                        if (difference == -2 || difference == 2) patienceTimer += 0.75f;

                        int multiplier = Game.hourOfGame / 2;
                        if (Game.hourOfGame == 12) multiplier = 0;
                        timeToFlash = (0.65f - (0.05f * multiplier));
                        chanceToSkip = 5;
                        attackPosition = 0;
                        attackPositionTarget = attackPosition;
                    }
                    return;
                }
            } else if (moveToPosition == 0){
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

                if (chanceToSkip > 1 && random.nextInt(chanceToSkip) != 0) {
                    int multiplier = Game.hourOfGame / 2;
                    if (Game.hourOfGame == 12) multiplier = 0;
                    timeToFlash = (0.3f - (0.05f * multiplier));
                    chanceToSkip--;
                } else {
                    timeToFlash = 0;
                    chanceToSkip++;
                }
                patienceTimer = 0.5f;
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
                moveToPosition -= Gdx.graphics.getDeltaTime() * 25;
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

        if (cooldownTimer == 0 || patienceTimer == 0){
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

            if (bedPatienceTimer == 0){
                jumpscareI = 1;
            }

            if (cooldownTimer > 0) return;
            boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);

            if (lookingAway) {
                cooldownTimer = 1.75f;
                audioClass.play("peek");
                room = 3;
                bedSpotted = false;
                patienceTimer = 1.75f;
                tapeSpotted = false;
                timeToFlash = 2.5f;
                Player.blacknessMultiplier = 6;
                attackPosition = 0;
                attackPositionTarget = 0;
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
                    attack = false;
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
                doorCooldown = 6;
                cooldownTimer = 5;
                leaveRoom = true;
                doorAnimation = 18;
                audioClass.play("leave");
            }
        }
    }
}
