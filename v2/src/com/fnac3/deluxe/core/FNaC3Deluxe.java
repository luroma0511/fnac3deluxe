package com.fnac3.deluxe.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.discord.Discord;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.state.Loading;
import com.fnac3.deluxe.core.state.Menu;
import com.fnac3.deluxe.core.state.StateManager;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.HashMap;
import java.util.Map;

public class FNaC3Deluxe extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	StateManager stateManager;
	AudioClass audioClass;
	Map<String, BitmapFont> bitmapFontMap;
	Map<String, FreeTypeFontGenerator> freeTypeFontGeneratorMap;
	public static FrameBuffer shapeBuffer;
	public static FrameBuffer circleShapeBuffer;

	Viewport viewport;
	OrthographicCamera camera;
	final Vector3 mousePosition = new Vector3();

	public static int scrollAmount;

	int windowMode = 1;
	boolean once;
	boolean focus;
	Data data;
	
	@Override
	public void create() {
		Discord.start();
		audioClass = new AudioClass();
		audioClass.createSound("thunder");
		audioClass.createSound("deluxeMenu");
		audioClass.createSound("candysMenu");
		audioClass.createSound("dreamTheme");
		audioClass.createSound("select");
		data = new Data();
		stateManager = new StateManager();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		bitmapFontMap = new HashMap<>();
		freeTypeFontGeneratorMap = new HashMap<>();
		stateManager.setState(StateManager.State.MENU);
		camera = new OrthographicCamera(1024, 768);
		camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
		camera.setToOrtho(false);
		camera.update();
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		Gdx.input.setInputProcessor(this);
	}

	public void resize(int width, int height){
		if (viewport != null){
			viewport.update(width, height);
			viewport.apply();
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (shapeBuffer == null){
			shapeBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);

			shapeBuffer.begin();
			batch.enableBlending();
			batch.begin();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(1, 1, 1, 1);

			shapeRenderer.rect(0, 0, 1024, 768);
			shapeRenderer.flush();
			shapeRenderer.end();

			batch.flush();
			shapeBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
			batch.end();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		}

		if (circleShapeBuffer == null){
			circleShapeBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);

			circleShapeBuffer.begin();
			batch.enableBlending();
			batch.begin();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(1, 1, 1, 1);

			shapeRenderer.ellipse(0, 0, 1024, 768);
			shapeRenderer.flush();
			shapeRenderer.end();

			batch.flush();
			circleShapeBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
			batch.end();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		}

		mousePosition.x = Gdx.input.getX();
		mousePosition.y = Gdx.input.getY();
		mousePosition.z = 0;

		Vector3 v3 = viewport.unproject(mousePosition);
		float mx = v3.x;
		float my = v3.y;

		if (!once && Gdx.input.isKeyJustPressed(Input.Keys.F4)
				&& !Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
			once = true;
			windowMode++;
			if (windowMode > 4) windowMode = 1;

			switch (windowMode){
				case 1 -> {
					Gdx.graphics.setWindowedMode(1024, 768);
					viewport = new FitViewport(1024, 768, camera);
					viewport.update(1024, 768);
					viewport.apply();
				}
				case 2 -> {
					Gdx.graphics.setUndecorated(true);
				}
				case 3 -> {
					Graphics.Monitor currentMonitor = Gdx.graphics.getMonitor();
					Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currentMonitor);
					Gdx.graphics.setFullscreenMode(displayMode);
					Gdx.graphics.setUndecorated(false);
				}
				case 4 -> {
					viewport = new StretchViewport(1024, 768, camera);
					viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					viewport.apply();
				}
			}
		}

		if (!Gdx.input.isKeyPressed(Input.Keys.F4)){
			once = false;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SYM)) {
			focus = true;
			if (windowMode > 2){
				Graphics.Monitor currentMonitor = Gdx.graphics.getMonitor();
				Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currentMonitor);
				Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
			}
		}

		switch (stateManager.getState()) {
			case MENU:
				Menu.input(mx, my, stateManager, data, audioClass);
				Menu.update(camera, stateManager, data, audioClass);
				Menu.render(batch, viewport, mx, my, data);
				break;
			case LOADING:
				Loading.update(Menu.nightType, data, stateManager, audioClass);
				Loading.render(batch, viewport);
				if (ImageHandler.doneLoading) {
					if (data.vSync) {
						Gdx.graphics.setVSync(true);
					}
					stateManager.setState(StateManager.State.GAME);
					Loading.startLoading = false;
					Game.restart = true;
				}
				break;
			case GAME:
				if (Game.restart || (!Game.gameover && !Game.win && Gdx.input.isKeyJustPressed(Input.Keys.R))) {
					Game.start(data, audioClass);
				}
				Game.input(stateManager, viewport, v3, data);
				Game.update(stateManager, camera, viewport, data, audioClass);
				Game.render(batch, viewport, data);
				break;
		}
		batch.end();

		ImageHandler.load();

		if (Discord.updateStatus) {
			Discord.update(data, stateManager.getState().toString(), Menu.modeName, Game.hourOfGame,
					data.flashDebug || data.hitboxDebug || (data.expandedPointer && !data.laserPointer));
		}

		scrollAmount = 0;
	}
	
	@Override
	public void dispose() {
		Discord.end();
		ImageHandler.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (focus){
			Graphics.Monitor currentMonitor = Gdx.graphics.getMonitor();
			Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currentMonitor);
			if (windowMode > 2) {
				Gdx.graphics.setFullscreenMode(displayMode);
			}
			focus = false;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchCancelled(int i, int i1, int i2, int i3) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		scrollAmount = (int) amountY;
		return false;
	}
}
