package com.mygdx.game.stages;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.LevelManager;
import com.mygdx.game.utils.Assets;

public class GameStage extends Stage{

	public static int PlAYER_HEALTH = 30;
	public static boolean GAME_OVER = false;
	public static boolean START_GAME = false;

	private final SpriteBatch _batch;
	private final EntityManager _entityManager;
	private OrthogonalTiledMapRenderer renderer;
	private int xDir = 0;
	private int yDir = 0;
	
	private OrthographicCamera _camera;

	public GameStage(Engine ashleyEngine,EntityManager entityManager, SpriteBatch batch) {
		Assets.loadGameStageAssets();
		
		renderer = new OrthogonalTiledMapRenderer(LevelManager.tiledMap);
		_batch = batch;
		_entityManager = entityManager;
		_camera = new OrthographicCamera();
		System.out.println("*************** To Start the game , Press Enter! ***************");
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setView(_camera);
		renderer.render();
		_batch.begin();
		_entityManager.update(Gdx.graphics.getDeltaTime());
		_batch.setProjectionMatrix(_camera.combined);
		_batch.end();
	}

	@Override
	public void act() {
		moveCamera();
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if (!START_GAME)
				System.out.println("Game Started , Spawning first Wave");
			START_GAME = true;
		}
		if (PlAYER_HEALTH == 0) {
			GAME_OVER = true;
		}
		super.act();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		LevelManager.tiledMap.dispose();
		_batch.dispose();
	}

	private void moveCamera() {
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			yDir = 1;
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			yDir = -1;
		}else {
			yDir = 0;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			xDir = -1;
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			xDir = 1;
		}else {
			xDir = 0;
		}
		final float cameraSpeed = 10f;
		_camera.viewportWidth = Gdx.graphics.getWidth() / 3;
		_camera.viewportHeight = Gdx.graphics.getHeight() / 3;
		float cameraPosX = _camera.position.x;
		float cameraPosY = _camera.position.y;
		_camera.position.set((int) cameraPosX + xDir * cameraSpeed,
				(int) cameraPosY + yDir * cameraSpeed, 0);
		_camera.update();
		float startX = _camera.viewportWidth / 2;
		float startY = _camera.viewportHeight / 2;
		float width = startX * 2;
		float height = startY * 2;
		setCameraBoundary(_camera, startX, startY, LevelManager.mapPixelWidth - width,
				LevelManager.mapPixelHeight - height);
	}

	private static void setCameraBoundary(Camera camera, float startX, float startY, float width, float height) {
		Vector3 position = camera.position;
		if (position.x < startX) {
			position.x = startX;
		}
		if (position.y < startY) {
			position.y = startY;
		}
		if (position.x > startX + width) {
			position.x = startX + width;
		}
		if (position.y > startY + height) {
			position.y = startY + height;
		}
		camera.position.set(position);
		camera.update();
	}

}
