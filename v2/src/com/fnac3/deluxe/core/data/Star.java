package com.fnac3.deluxe.core.data;

import com.badlogic.gdx.Gdx;

public class Star {
    public float size;
    public float minSize;
    public float maxSize;
    public int nightType;
    public float visibility;
    public float alphaVisibility;
    public float[] color;
    public boolean hovered;

    public Star(int nightType, float r, float g, float b){
        minSize = 96;
        maxSize = 114;
        size = minSize;
        this.nightType = nightType;
        this.color = new float[]{r, g, b};
    }

    public Star(int nightType, float[] rgb){
        minSize = 96;
        maxSize = 114;
        size = minSize;
        this.nightType = nightType;
        this.color = rgb;
    }

    public void sizeAnimation(){
        if (hovered){
            size += ((maxSize - size) / 8) * (Gdx.graphics.getDeltaTime() * 60);
        } else {
            size += ((minSize - size) / 8) * (Gdx.graphics.getDeltaTime() * 60);
        }
    }

    public void setVisibility(int night, boolean condition){
        if (nightType == night || nightType == -1){
            visibility += Gdx.graphics.getDeltaTime() * 2;
            if (visibility > 0.5f){
                visibility = 0.5f;
            }
        } else {
            visibility -= Gdx.graphics.getDeltaTime() * 2;
            if (visibility < 0){
                visibility = 0;
            }
        }

        if (condition && (nightType == night || nightType == -1)){
            alphaVisibility += Gdx.graphics.getDeltaTime() * 2;
            if (alphaVisibility > 0.5f){
                alphaVisibility = 0.5f;
            }
        } else {
            alphaVisibility -= Gdx.graphics.getDeltaTime() * 2;
            if (alphaVisibility < 0){
                alphaVisibility = 0;
            }
        }
    }
}
