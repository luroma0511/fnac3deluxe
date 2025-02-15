package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;
import com.fnac3.deluxe.core.util.Utils;

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

    public static boolean pause;
    public static boolean skipped;
    public static boolean catAttackMode;
    public static boolean catAttackModeLock;
    public static int attackPosition;
    public static float twitchPosition;
    public static int attackPositionTarget;
    public static float moveToPosition;
    public static float patienceTimer;
    public static float patienceHealthTimer;
    public static boolean attack;
    public static float attackTime;
    public static boolean catHum;

    public static boolean tapeSpotted;
    public static float timeUntilWeasel;
    public static boolean tapeWeasel;
    public static float tapePosition;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static boolean hitboxHit;

    public static void input(){
        if (jumpscareI == 0 && !Player.turningAround && !Player.freeze && Player.room == 0 && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Random random, Data data, AudioClass audioClass){
        pause = Player.freeze;

        if (!catHum){
            audioClass.play("cat");
            audioClass.setVolume("cat", 0);
            audioClass.setPitch("cat", 1);
            audioClass.loop("cat", true);
            catHum = true;
        }

        float catVolume = audioClass.getVolume("cat");
        float catPitch = audioClass.getPitch("cat");

        if (catAttackMode) {
            if (!catAttackModeLock && (Rat.room == 0 || Rat.room == 3) && (Vinnie.room == 0 || Vinnie.room == 3)){
                catAttackModeLock = true;
                bedPatienceTimer = 2;
            }

            if (catAttackModeLock) {
                if (catVolume < 0.6f) {
                    audioClass.setVolume("cat", catVolume + Gdx.graphics.getDeltaTime());
                    if (audioClass.getVolume("cat") > 0.6f) audioClass.setVolume("cat", 0.6f);
                }
                audioClass.setPitch("cat", catPitch + Gdx.graphics.getDeltaTime() / 75);
            }
        } else if (catVolume < 0.15f){
            audioClass.setVolume("cat", catVolume + Gdx.graphics.getDeltaTime() / 2);
            if (audioClass.getVolume("cat") > 0.15f) audioClass.setVolume("cat", 0.15f);
        }

        if (jumpscareI == 0) {
            switch (room) {
                case 1:
                    roomMechanic(data, random, audioClass);
                    break;
                case 2:
                    bedMechanic(data, random, audioClass);
                    break;
                case 3:
                    crouchMechanic(random, audioClass);
                    break;
                case 4:
                    farBedMechanic(random, audioClass);
                    break;
            }
            if (jumpscareI != 0){
                audioClass.stopAllSounds();
            }
        } else if (!jumpscare){
            Player.blacknessTimes = 1;
            Player.blacknessMultiplier = 5;
            if (room == 1) jumpscareTime = 1.5f;
            else jumpscareTime = 1.35f;
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
                    if (room != 1 && jumpscareI > 29) jumpscareI = 29;
                    else if (jumpscareI > 35) jumpscareI = 35;
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
            if (room == 1 && attack){
                hitboxHit = true;
            } else if (room == 3 && timeToFlash > 0){
                System.out.println("true");
                hitboxHit = true;
            } else if (room == 4 && timeToFlash > 0){
                hitboxHit = true;
            }
        }
    }

    public static void hitboxes(Data data){
        hitboxHit = false;
        hitboxPosition[0] = -1;
        hitboxPosition[1] = -1;
        hitboxDistance = 0;
        switch (room) {
            case 1:
                if (attackPosition != attackPositionTarget) break;
                if (side == 0){
                    hitboxDistance = 70;
                    switch (attackPosition){
                        case 0 -> Utils.setHitbox(hitboxPosition, 366, 741);
                        case 1 -> Utils.setHitbox(hitboxPosition, 290, 632);
                        case 2 -> Utils.setHitbox(hitboxPosition, 466, 616);
                    }
                } else if (side == 1){
                    hitboxDistance = 80;
                    switch (attackPosition){
                        case 0 -> Utils.setHitbox(hitboxPosition, 1529, 724);
                        case 1 -> Utils.setHitbox(hitboxPosition, 1341, 583);
                        case 2 -> Utils.setHitbox(hitboxPosition, 1703, 605);
                    }
                } else {
                    hitboxDistance = 90;
                    switch (attackPosition){
                        case 0 -> Utils.setHitbox(hitboxPosition, 2559, 890);
                        case 1 -> Utils.setHitbox(hitboxPosition, 2395, 714);
                        case 2 -> Utils.setHitbox(hitboxPosition, 2748, 669);
                    }
                }
                break;
            case 3:
                hitboxDistance = 80;
                if (side == 0) Utils.setHitbox(hitboxPosition, 652, 440);
                else Utils.setHitbox(hitboxPosition, 2349, 512);
                break;
            case 4:
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
                        x = 171;
                        y = 322;
                    } else if (side == 1) {
                        x = 1215;
                        y = 78;
                    } else {
                        x = 2229;
                        y = 196;
                    }
                    break;
                case 2:
                    if (Player.room == 1) {
                        lookUnderBed = true;
                        if (side == 0) {
                            texture = "Under Bed/Bed1";
                            y = 194;
                        } else if (side == 2){
                            texture = "Under Bed/Bed2";
                            x = 1163;
                            y = 165;
                        }
                    } else if (Player.room == 2 && tapePosition >= 1) {
                        lookAtTape = true;
                        x = 145;
                        y = 368;
                        texture = "Tape/Leaving" + (int) (13 - tapePosition);
                    }
                    break;
                case 3:
                    lookInRoom = true;
                    if (side == 0) {
                        texture = "Under Bed/Left/Left" + ((int) twitchPosition + 1);
                        x = 515;
                        y = 231;
                    } else {
                        texture = "Under Bed/Right/Right" + ((int) twitchPosition + 1);
                        x = 2156;
                        y = 194;
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
        attack = false;
        catHum = false;
        catAttackMode = false;
        catAttackModeLock = false;
        cooldown = 12;
        jumpscareTime = -1;
        cooldownTimer = 10;
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

            side = 2 * random.nextInt(2);
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
                    catHum = false;
                    catAttackMode = false;
                    catAttackModeLock = false;
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

                if (!skipped && random.nextInt(5) == 2) {
                    if (random.nextInt(2) == 0) timeToFlash = 0.01f;
                    else timeToFlash = 0;
                    skipped = true;
                } else {
                    timeToFlash = 0.4f + 0.2f * random.nextInt(2);
                    skipped = false;
                }
                patienceTimer = 0.75f;
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
            jumpscareType = "roomJumpscare";
        }
    }

    public static void bedMechanic(Data data, Random random, AudioClass audioClass){
        if (cooldownTimer > 0 && !Player.freeze) {
            if ((!catAttackMode || catAttackModeLock)) {
                cooldownTimer -= Gdx.graphics.getDeltaTime();
                if (cooldownTimer <= 0){
                    if (Player.room == 1) cooldownTimer += Gdx.graphics.getDeltaTime();
                    else cooldownTimer = 0;
                }
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

            if (Player.room == 1 && !Player.turningAround && bedPatienceTimer > 0) {
                bedPatienceTimer -= Gdx.graphics.getDeltaTime();
                if (bedPatienceTimer < 0) {
                    bedPatienceTimer = 0;
                }
            }

            if (bedPatienceTimer == 0) {
                jumpscareI = 1;
                jumpscareType = "bedJumpscare";
            }

            if (cooldownTimer <= 0) {
                if (!catAttackMode){
                    audioClass.play("catPeek");
                    room = 4;
                    cooldownTimer = 12;
                    bedSpotted = false;
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
                } else if ((Vinnie.room == 0 && Rat.room == 0) && ((Player.side == 0 && side == 2) || (Player.side == 2 && side == 0))) {
                    audioClass.play("peek");
                    room = 3;
                    bedSpotted = false;
                    patienceTimer = 2f;
                    tapeSpotted = false;
                    timeToFlash = 2.4f;
                    Player.blacknessMultiplier = 6;
                } else {
                    jumpscareI = 1;
                    jumpscareType = "bedJumpscare";
                }
            }
        }
    }

    public static void crouchMechanic(Random random, AudioClass audioClass){
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
                    jumpscareType = "bedJumpscare";
                }
            }
        } else if (Player.blacknessTimes == 0 && Player.blacknessDelay == 0){
            Player.blacknessTimes = 2;
            Player.freeze = false;
            cooldownTimer = 2;
            shaking = false;
            twitchingNow = false;
            dontSoundShake = false;
            room = 1;
            patienceHealthTimer = 2;
            patienceTimer = 0.75f;
            timeToFlash = 0.85f;
            attackPosition = 0;
            attackPositionTarget = attackPosition;
            attackTime = 12;
            skipped = false;

            int chance = random.nextInt(2);
            if (side == 0) chance++;
            side = chance;
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
                cooldownTimer -= Gdx.graphics.getDeltaTime();
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
                jumpscareType = "bedJumpscare";
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
            bedPatienceTimer = 5;
            catAttackMode = true;

            side = 2 * random.nextInt(2);
            cooldownTimer = 10;
            if ((Vinnie.room == 2 && Vinnie.side == side) || (Rat.room == 2 && Rat.side == side)){
                if (side == 0) side = 2;
                else side = 0;
            }
            audioClass.stop("catPulse");
        }
    }
}
