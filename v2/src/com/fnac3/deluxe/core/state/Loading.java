package com.fnac3.deluxe.core.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.FNaC3Deluxe;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.discord.Discord;
import com.fnac3.deluxe.core.enemy.*;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;
import com.fnac3.deluxe.core.util.SoundFiles;

public class Loading {

    private static FrameBuffer screenBuffer;
    private static BitmapFont loadingLabel;
    private static BitmapFont loadingText;

    private static GlyphLayout titleLayout;

    public static boolean firstFrame;
    public static boolean startLoading;
    private static float whiteAlpha;

    public static void update(int nightType, Data data, StateManager stateManager, AudioClass audioClass){
        if (!startLoading){
            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/timeFont.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            if (loadingLabel == null){
                parameter.size = 56;
                loadingLabel = fontGenerator.generateFont(parameter);
            }

            if (loadingText == null){
                parameter.size = 28;
                loadingText = fontGenerator.generateFont(parameter);
            }

            if (titleLayout == null){
                loadingLabel.setColor(1, 1, 1, 1);
                titleLayout = new GlyphLayout();
            } else {
                titleLayout.reset();
            }

            Menu.previousNightType = nightType;
            if (((nightType == 0 && data.RatAI == 0 && data.CatAI == 0 && data.VinnieAI == 0)
                    || (nightType == 1 && !data.ShadowRatAI && !data.ShadowCatAI))
                    && data.challenge4){
                Menu.nightType = 2;
            }

            titleLayout.setText(loadingLabel, Menu.modeName);

            firstFrame = true;
            whiteAlpha = 1;
            Rat.ai = 0;
            Cat.ai = 0;
            Vinnie.ai = 0;
            ShadowRat.active = false;
            ShadowCat.active = false;

            if (nightType == 0){
                Rat.ai = data.RatAI;
                Cat.ai = data.CatAI;
                Vinnie.ai = data.VinnieAI;
            } else if (nightType == 1) {
                ShadowRat.active = data.ShadowRatAI;
                ShadowCat.active = data.ShadowCatAI;
                ShadowCat.twitchyCat = data.challenge4;
                ShadowVinnie.ai = data.ShadowVinnieAI ? 20 : 0;
            }
            Monstergami.active = data.challenge4 && nightType != 1;

            ImageHandler.addImages(data, stateManager);
            for (String s: SoundFiles.gameSounds){
                audioClass.createSound(s);
            }
            audioClass.play("thunder");
            if (data.vSync) {
                Gdx.graphics.setVSync(false);
            }
            startLoading = true;
            Discord.updateStatus = true;
        } else {
            if (whiteAlpha > 0 && !firstFrame){
                whiteAlpha -= Gdx.graphics.getDeltaTime() * 2;
                if (whiteAlpha < 0){
                    whiteAlpha = 0;
                }
            }
            firstFrame = false;
        }
    }

    public static void render(SpriteBatch batch, Viewport viewport){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (screenBuffer == null){
            screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }

        screenBuffer.begin();

        batch.setColor(0, 0, 0, 1);
        batch.draw(FNaC3Deluxe.shapeBuffer.getColorBufferTexture(), 0, 0);
        batch.setColor(1, 1, 1, 1);

        loadingLabel.draw(batch, titleLayout,
                512 - titleLayout.width / 2,
                384 + titleLayout.height / 2);

        loadingText.draw(batch, "Loading: " + (int) ((float) ImageHandler.currentPercent / ImageHandler.maxPercent * 100), 16, 44);

        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Texture texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        if (Menu.nightType == 0){
            batch.setColor(1, 0, 0.25f, 1);
        } else if (Menu.nightType == 1){
            batch.setColor(0.6f, 0, 1, 1);
        } else {
            batch.setColor(0.35f, 0.175f, 0.95f, 1);
        }
        batch.draw(texture, 0, texture.getHeight(), texture.getWidth(), -texture.getHeight());
        batch.setColor(1, 1, 1, whiteAlpha);
        batch.draw(FNaC3Deluxe.shapeBuffer.getColorBufferTexture(), 0 ,0);
        batch.setColor(1, 1, 1, 1);
    }
}
