package com.fnac3.deluxe.core.util;

import com.fnac3.deluxe.core.data.Data;

public class Utils {
    public static void setHitbox(float[] hitbox, float x, float y) {
        hitbox[0] = x;
        hitbox[1] = y;
    }

    public static float setHitboxDistance(Data data, float distance){
        if (data.pointer == 1){
            distance *= 0.8f;
        } else if (data.pointer == 2){
            distance *= 0.6f;
        } else if (data.pointer == 3){
            distance *= 0.4f;
        }
        return distance;
    }
}
