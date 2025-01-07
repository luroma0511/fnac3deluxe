package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Menu;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class Monstergami {

    public static boolean active;
    public static boolean wait;
    public static boolean pause;

    private static int leftSide;
    private static int rightSide;
    public static int side = -1;
    public static float cooldownTimer;
    public static float patienceHealthTimer;
    public static int moves;
    public static float attackTimer;
    public static int attackPosition;
    public static float twitchPosition;
    public static boolean shaking;
    public static boolean twitchingNow;
    public static int attackPositionTarget;
    public static float moveToPosition;
    public static float timeToFlash;
    public static float aggression;
    public static int skips;
    public static float timeToStealTape;
    public static boolean tapeStolen;

    public static boolean jumpscare;
    public static float jumpscareTimer;
    public static float jumpscareTime = 0.9f;
    public static int jumpscareI;

    private static boolean takeLight;

    public static boolean attack;

    public static float ambienceVolume;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static boolean hitboxHit;

    public static void reset(AudioClass audioClass){
        aggression = 0;
        skips = 0;
        tapeStolen = false;
        if (Menu.nightType == 2) cooldownTimer = 0;
        else cooldownTimer = 45;
        audioClass.stop("monstergami");
        audioClass.loop("monstergami", true);
        if (hitboxPosition == null) {
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }

        leftSide = 0;
        rightSide = 0;
        takeLight = false;
        jumpscare = false;
        jumpscareI = 0;
        jumpscareTime = 0.9f;
        wait = false;
        attack = false;
        pause = true;
        side = -1;
        shaking = false;
        twitchingNow = false;
        hitboxHit = false;
        attackPosition = 0;
        attackPositionTarget = 0;
        moveToPosition = 0;
        patienceHealthTimer = 0;
    }

    public static void input() {
        if (jumpscareI == 0 && !takeLight && !Player.turningAround && !Player.freeze && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Data data, Random random, AudioClass audioClass) {

        if (jumpscareI == 0) {
            if (timeToStealTape > 0) {
                timeToStealTape -= Gdx.graphics.getDeltaTime();
            } else if (side != -1){
                if (Player.tape.isPlaying() || (Player.playPosition > 0 && Player.stopPosition != 0)) tapeStolen = true;
            }

            if (pause || (side == -1 && cooldownTimer > 0)) {
                if (ambienceVolume > 0 && !takeLight) {
                    ambienceVolume -= Gdx.graphics.getDeltaTime() / 3;

                    if (ambienceVolume <= 0) {
                        ambienceVolume = 0;
                        audioClass.stop("monstergami");
                    } else {
                        audioClass.setVolume("monstergami", ambienceVolume);
                    }
                }

                if (cooldownTimer > 0){
                    cooldownTimer -= Gdx.graphics.getDeltaTime();
                    if (cooldownTimer <= 0) cooldownTimer = 0;
                    else return;
                }

                if (Menu.nightType == 2) pause = false;
            } else if (side == -1) {
                attackPosition = 0;
                attackPositionTarget = attackPosition;

                if (Menu.nightType == 2) timeToStealTape = 0.75f;
                else timeToStealTape = 4;

                if (Player.side == -1) {
                    side = random.nextInt(3);
                } else {
                    if (Player.side == 0) {
                        if ((Cat.ai != 0 && Cat.side == 2)
                                || (ShadowCat.active && ShadowCat.room == 4 && ShadowCat.side == 2)) {
                            side = 1;
                        } else {
                            side = 1 + random.nextInt(2);
                        }
                    } else if (Player.side == 2) {
                        if (Cat.ai != 0 && Cat.side == 0
                                || (ShadowCat.active && ShadowCat.room == 4 && ShadowCat.side == 0)) {
                            side = 1;
                        } else {
                            side = random.nextInt(2);
                        }
                    } else {
                        if (Cat.ai != 0 && Cat.side == 0
                                || (ShadowCat.active && ShadowCat.room == 4 && ShadowCat.side == 0)) {
                            side = 2;
                        } else if (Cat.ai != 0 && Cat.side == 2
                                || (ShadowCat.active && ShadowCat.room == 4 && ShadowCat.side == 2)) {
                            side = 0;
                        } else {
                            side = 2 * random.nextInt(2);
                        }
                    }
                }
                ambienceVolume = 0.65f;
                audioClass.play("monstergami");
                audioClass.loop("monstergami", true);
                audioClass.setVolume("monstergami", ambienceVolume);
                audioClass.play("walking_in");
                if (Player.blacknessDelay == 0) {
                    Player.blacknessMultiplier = 6;
                    Player.blacknessTimes = 2;
                }
                attackTimer = 7;
                cooldownTimer = 8;
                moves = 12;

                timeToFlash = 0.4f;
                patienceHealthTimer = 2f;
            } else {
                if (!Player.freeze && wait && !Player.turningAround && Player.room == 1 && side == 3) {
                    wait = false;
                    audioClass.play("bed");
                }

                if (!attack && !takeLight && moves > 0) {
                    if (!Player.turningAround && Player.room == 0) {
                        attack = Player.inititiateSnapPosition(side);
                        if (attack) {
                            cooldownTimer = 1f;
                            Player.snapPosition = false;
                            Player.lastCharacterAttack = "Monstergami";
                        }
                    }
                }

                if (Player.blacknessTimes == 0) {
                    shaking = hitboxHit;
                }

                if (shaking) {
                    twitchPosition += Gdx.graphics.getDeltaTime() * 30;
                    if (twitchPosition >= 2) {
                        twitchPosition = 0;
                    }
                } else {
                    twitchPosition = 0;
                }

                if (cooldownTimer > 0 && !Player.freeze && !shaking && ((side == 3 && wait) || !Player.snapPosition)) {
                    cooldownTimer -= Gdx.graphics.getDeltaTime();
                    if (cooldownTimer <= 0) {
                        cooldownTimer = 0;
                    }
                }

                if (side == 3 && !wait){
                    attackTimer -= Gdx.graphics.getDeltaTime();
                    if (attackTimer < 0) {
                        attackTimer = 0;
                    }
                } else if (side < 3 && attack && Player.snapPosition){
                    attackTimer -= Gdx.graphics.getDeltaTime();
                    if (attackTimer < 0) {
                        attackTimer = 0;
                    }
                }

                if (Player.blacknessTimes == 0 && takeLight){
                    attack = false;
                    audioClass.play("tapeTaken");
                    Player.flashlightStolen = true;
                    Player.snapPosition = false;
                    pause = true;
                    side = -1;
                }

                if (Player.freeze && timeToFlash == 0 && Player.blacknessTimes == 0) {
                    Player.blacknessMultiplier = 1.25f;
                    shaking = false;
                    if (attack) {
                        wait = true;
                        audioClass.play("crawl");
                        moves = 12;
                        timeToFlash = 0.65f;
                        side = 3;
                        cooldownTimer = 8;
                        attackTimer = 16f;
                        attackPosition = 1;
                        attackPositionTarget = 1;
                        patienceHealthTimer = 2f;
                    } else {
                        side = -1;
                        pause = true;
                        if (Menu.nightType == 2) cooldownTimer = 10;
                        else cooldownTimer = 45;
                    }
                }

                if (shaking && !Player.freeze) {
                    timeToFlash -= Gdx.graphics.getDeltaTime();
                    if (Player.snapPosition) {
                        patienceHealthTimer += Gdx.graphics.getDeltaTime() / 2;
                    }
                } else if (moveToPosition == 0 && Player.snapPosition) {
                    patienceHealthTimer -= Gdx.graphics.getDeltaTime();

                    if (patienceHealthTimer < 0) {
                        patienceHealthTimer = 0;
                    }
                }

                if (!Player.freeze && timeToFlash <= 0) {
                    timeToFlash = 0;
                    if (shaking) moves--;
                    shaking = false;
                    if (moves == 0 || attackTimer == 0) {
                        wait = false;
                        Player.snapPosition = false;
                        Player.freeze = true;
                        Player.blacknessMultiplier = 6f;
                        Player.blacknessTimes = 3;
                        Player.blacknessDelay = 0.5f;
                        audioClass.stop("twitch");
                        if (attackTimer == 0) {
                            audioClass.play("thunder");
                        } else {
                            audioClass.play("scaryImpact");
                            attack = false;
                        }
                        return;
                    }
                }

                if (!Player.freeze && moveToPosition == 0 && (timeToFlash == 0 || skips > 0)) {
                    if (!Player.snapPosition) {
                        Player.snapPosition = true;
                        Player.snapToSide = side;
                    }

                    if (skips == 0) {
                        int chance = random.nextInt((int) aggression + 1);
                        if (side == 3 && chance > 1) chance = random.nextInt(2);

                        if (chance > 1) {
                            skips = chance - 1;
                            chance = random.nextInt(2);
                        }

                        if (chance == 0) timeToFlash = 0.3f + (0.05f * random.nextInt(3));
                        else timeToFlash = 0.15f + (0.05f * random.nextInt(2));
                    } else skips--;

                    moveToPosition = 3;

                    if (side == 3) {
                        if (attackPosition == 0 || attackPosition == 2) {
                            attackPositionTarget = 1;
                        } else {
                            if (leftSide == 3) {
                                attackPositionTarget = 2;
                            } else if (rightSide == 3){
                                attackPositionTarget = 0;
                            } else {
                                attackPositionTarget = 2 * random.nextInt(2);
                            }

                            if (attackPositionTarget == 0){
                                leftSide++;
                                rightSide = 0;
                            } else {
                                rightSide++;
                                leftSide = 0;
                            }
                        }
                    } else {
                        if (attackPosition == 0) {
                            attackPositionTarget = 2 + random.nextInt(2);
                        } else if (attackPosition == 1) {
                            attackPositionTarget = 3 + random.nextInt(2);
                        } else if (attackPosition == 2) {
                            if (random.nextInt(2) == 1) attackPositionTarget = 0;
                            else attackPositionTarget = 4;
                        } else if (attackPosition == 3){
                            attackPositionTarget = random.nextInt(2);
                        } else {
                            attackPositionTarget = 1 + random.nextInt(2);
                        }
                    }
                }

                if (moveToPosition > 0) {
                    if (moveToPosition == 3) {
                        audioClass.play("dodge");
                    }
                    moveToPosition -= Gdx.graphics.getDeltaTime() * 19;
                    if (moveToPosition < 1 && attackPosition != attackPositionTarget) {
                        attackPosition = attackPositionTarget;
                    }
                    if (moveToPosition < 0) {
                        moveToPosition = 0;
                    }
                    shaking = false;
                }

                if (!Player.freeze && (cooldownTimer == 0 || patienceHealthTimer == 0)){
                    if (Menu.nightType == 2) {
                        jumpscareI = 1;
                    } else if (!takeLight){
                        takeLight = true;
                        Player.blacknessTimes = 3;
                        Player.blacknessMultiplier = 6;
                    }
                }
            }
        } else if (!jumpscare){
            jumpscare = true;
            jumpscareTimer = 0.05f;
            Player.blacknessTimes = 1;
            Player.blacknessMultiplier = 12;
            audioClass.stopAllSounds();
            audioClass.play("monstergamiJumpscare");
            audioClass.setVolume("monstergamiJumpscare", 0.5f);
        } else if (jumpscareTime > 0){
            jumpscareTime -= Gdx.graphics.getDeltaTime();
            if (jumpscareTime <= 0) {
                jumpscareTime = 0;
                audioClass.stop("monstergamiJumpscare");
            }
            if (jumpscareTimer > 0){
                jumpscareTimer -= Gdx.graphics.getDeltaTime();
                if (jumpscareTimer <= 0){
                    jumpscareTimer += 0.05f;
                    jumpscareI = 1 + random.nextInt(6);
                }
            }
        }

        if (timeToFlash > 0) {
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
            if (side != -1 && !pause) {
                if (side == 3 && Player.room == 1) {
                    hitboxHit = true;
                } else if (Player.room == 0){
                    hitboxHit = true;
                }
            }
        }
    }

    public static void hitboxes(Data data){
        hitboxHit = false;
        hitboxPosition[0] = -1;
        hitboxPosition[1] = -1;
        hitboxDistance = 0;
        if (attackPosition != attackPositionTarget) return;
        if (side == 0){
            hitboxDistance = 70;
            if (attackPosition == 0){
                hitboxPosition[0] = 376;
                hitboxPosition[1] = 750;
            } else if (attackPosition == 1){
                hitboxPosition[0] = 248;
                hitboxPosition[1] = 669;
            } else if (attackPosition == 2){
                hitboxPosition[0] = 258;
                hitboxPosition[1] = 574;
            } else if (attackPosition == 3) {
                hitboxPosition[0] = 503;
                hitboxPosition[1] = 564;
            } else {
                hitboxPosition[0] = 511;
                hitboxPosition[1] = 660;
            }
        } else if (side == 1){
            hitboxDistance = 90;
            if (attackPosition == 0){
                hitboxPosition[0] = 1529;
                hitboxPosition[1] = 729;
            } else if (attackPosition == 1) {
                hitboxPosition[0] = 1370;
                hitboxPosition[1] = 626;
            } else if (attackPosition == 2){
                hitboxPosition[0] = 1390;
                hitboxPosition[1] = 510;
            } else if (attackPosition == 3) {
                hitboxPosition[0] = 1693;
                hitboxPosition[1] = 511;
            } else {
                hitboxPosition[0] = 1702;
                hitboxPosition[1] = 637;
            }
        } else if (side == 2){
            hitboxDistance = 115;
            if (attackPosition == 0){
                hitboxPosition[0] = 2561;
                hitboxPosition[1] = 890;
            } else if (attackPosition == 1){
                hitboxPosition[0] = 2363;
                hitboxPosition[1] = 745;
            } else if (attackPosition == 2){
                hitboxPosition[0] = 2350;
                hitboxPosition[1] = 529;
            } else if (attackPosition == 3) {
                hitboxPosition[0] = 2773;
                hitboxPosition[1] = 521;
            } else {
                hitboxPosition[0] = 2774;
                hitboxPosition[1] = 716;
            }
        } else {
            hitboxDistance = 140;
            if (attackPosition == 0){
                hitboxPosition[0] = 569;
                hitboxPosition[1] = 515;
            } else if (attackPosition == 1){
                hitboxPosition[0] = 1017;
                hitboxPosition[1] = 393;
            } else {
                hitboxPosition[0] = 1448;
                hitboxPosition[1] = 510;
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

    public static void render(SpriteBatch batch){
        String texture;
        String sideTexture;
        float x;
        float y;

        if (!jumpscare) {
            if (side == 0) {
                sideTexture = "Left";
            } else if (side == 1) {
                sideTexture = "Middle";
            } else if (side == 2){
                sideTexture = "Right";
            } else if (side == 3){
                sideTexture = "Bed";
            } else {
                return;
            }
            String position = null;
            if (attackPosition == attackPositionTarget) {
                switch (attackPosition) {
                    case 0:
                        if (side == 3){
                            position = "Left";
                        } else {
                            position = "Up";
                        }
                        break;
                    case 1:
                        if (side == 3){
                            position = "Middle";
                        } else {
                            position = "UpLeft";
                        }
                        break;
                    case 2:
                        if (side == 3){
                            position = "Right";
                        } else {
                            position = "DownLeft";
                        }
                        break;
                    case 3:
                        position = "DownRight";
                        break;
                    case 4:
                        position = "UpRight";
                        break;
                }
                position += ((int) twitchPosition + 1);
            } else {
                int move = (int) moveToPosition;

                if (side == 3){
                    if (attackPosition == 0 && attackPositionTarget == 1) {
                        position = "MoveLeft" + (3 - move);
                    } else if (attackPosition == 1 && attackPositionTarget == 0) {
                        position = "MoveLeft" + move;
                    } else if (attackPosition == 1 && attackPositionTarget == 2) {
                        position = "MoveRight" + (3 - move);
                    } else if (attackPosition == 2 && attackPositionTarget == 1) {
                        position = "MoveRight" + move;
                    }
                } else {
                    if (attackPosition == 0 && attackPositionTarget == 1) {
                        position = "MoveUpToUpLeft" + (3 - move);
                    } else if (attackPosition == 1 && attackPositionTarget == 0) {
                        position = "MoveUpToUpLeft" + move;
                    } else if (attackPosition == 1 && attackPositionTarget == 2) {
                        position = "MoveUpLeftToDownLeft" + (3 - move);
                    } else if (attackPosition == 2 && attackPositionTarget == 1) {
                        position = "MoveUpLeftToDownLeft" + move;
                    } else if (attackPosition == 2 && attackPositionTarget == 3) {
                        position = "MoveDownLeftToDownRight" + (3 - move);
                    } else if (attackPosition == 3 && attackPositionTarget == 2) {
                        position = "MoveDownLeftToDownRight" + move;
                    } else if (attackPosition == 3 && attackPositionTarget == 4) {
                        position = "MoveDownRightToUpRight" + (3 - move);
                    } else if (attackPosition == 4 && attackPositionTarget == 3) {
                        position = "MoveDownRightToUpRight" + move;
                    } else if (attackPosition == 4 && attackPositionTarget == 0) {
                        position = "MoveUpRightToUp" + (3 - move);
                    } else if (attackPosition == 0 && attackPositionTarget == 4) {
                        position = "MoveUpRightToUp" + move;
                    } else if (attackPosition == 0 && attackPositionTarget == 2) {
                        position = "MoveUpToDownLeft" + (3 - move);
                    } else if (attackPosition == 2 && attackPositionTarget == 0) {
                        position = "MoveUpToDownLeft" + move;
                    } else if (attackPosition == 2 && attackPositionTarget == 4) {
                        position = "MoveDownLeftToUpRight" + (3 - move);
                    } else if (attackPosition == 4 && attackPositionTarget == 2) {
                        position = "MoveDownLeftToUpRight" + move;
                    } else if (attackPosition == 4 && attackPositionTarget == 1) {
                        position = "MoveUpRightToUpLeft" + (3 - move);
                    } else if (attackPosition == 1 && attackPositionTarget == 4) {
                        position = "MoveUpRightToUpLeft" + move;
                    } else if (attackPosition == 1 && attackPositionTarget == 3) {
                        position = "MoveUpLeftToDownRight" + (3 - move);
                    } else if (attackPosition == 3 && attackPositionTarget == 1) {
                        position = "MoveUpLeftToDownRight" + move;
                    } else if (attackPosition == 3 && attackPositionTarget == 0) {
                        position = "MoveDownRightToUp" + (3 - move);
                    } else if (attackPosition == 0 && attackPositionTarget == 3) {
                        position = "MoveDownRightToUp" + move;
                    }
                }
            }
            texture = "Battle/" + sideTexture + "/" + position;
            if (side == 0) {
                x = 115;
                y = 234;
            } else if (side == 1) {
                x = 1217;
                y = 165;
            } else if (side == 2){
                x = 2151;
                y = 93;
            } else {
                x = 0;
                y = 131;
            }

            if (texture == null) return;
            boolean passed = side != 3 && !Player.turningAround && Player.room == 0;

            if (!passed && side == 3 && !Player.turningAround && Player.room == 1){
                passed = true;
            }

            if (!passed) return;
        } else {
            texture = "Jumpscare/Jumpscare" + jumpscareI;
            x = Player.roomPosition[0] + Player.shakingPosition;
            y = Player.roomPosition[1];
        }
        batch.draw(ImageHandler.images.get("game/Monstergami/" + texture), x, y);

        if (side != 3 && jumpscareI == 0){
            batch.draw(ImageHandler.images.get("game/room/FullRoomBed"), 0, 0);
        }
    }
}
