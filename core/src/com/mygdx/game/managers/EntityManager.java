package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Factory.EntityFactory;
import com.mygdx.game.controllers.EntityModel;
import com.mygdx.game.controllers.UIStageController;
import com.mygdx.game.entites.input.InputHandler;
import com.mygdx.game.entites.systems.*;
import com.mygdx.game.stages.UiView;
import com.mygdx.game.states.PlayState;

public class EntityManager {
	private final CoinSystem coinSystem;
    private EntityFactory _entityFactory;
	private UIStageController uiController;
	private Engine _ashleyEngine;
	private WaveTimeManager _waveManager;
	private InputHandler inputhandler;

	public EntityManager(Engine ashleyEngine, SpriteBatch batch, OrthographicCamera gameCamera,
						 InputHandler inputhandler, UiView _uiView, GameStateManager gsm) {
		this._ashleyEngine = ashleyEngine;
		this.inputhandler = inputhandler;
		
		_entityFactory = new EntityFactory(ashleyEngine);
		_waveManager = new WaveTimeManager(_entityFactory);
		EntityModel _entityModel = new EntityModel(_waveManager, _entityFactory, gsm, gameCamera);
		uiController = new UIStageController(_uiView, _entityModel);
		_entityFactory.createPlayerEntity();
		
		MoveToSystem moveToSystem = new MoveToSystem();
		coinSystem = new CoinSystem(gameCamera);
		PlayerStatSystem statSystem = new PlayerStatSystem(uiController);
		RenderSystem renderSystem = new RenderSystem(batch);
		HealthSystem healthSystem = new HealthSystem(batch , _entityFactory);
		PlayerInputSystem playerInputSys = new PlayerInputSystem();
		inputhandler.registerInputHandlerSystem(playerInputSys);
		CameraMovementSystem camSys = new CameraMovementSystem(gameCamera);
		ashleyEngine.addSystem(statSystem);
		ashleyEngine.addSystem(moveToSystem);
		ashleyEngine.addSystem(renderSystem);
		ashleyEngine.addSystem(healthSystem);
		ashleyEngine.addSystem(playerInputSys);
		ashleyEngine.addSystem(camSys);
		ashleyEngine.addSystem(coinSystem);
	}


    public void update(float deltaTime) {
		if (PlayState.PAUSE) {
			if (!uiController.getPauseWindow().isVisible())
				System.out.println("show pause window");
			uiController.getPauseWindow().setVisible(true);
		} else {
			if (uiController.getPauseWindow().isVisible()) {
				System.out.println("hide pause window");
				uiController.getPauseWindow().setVisible(false);
			}
			inputhandler.pullInput();
			_waveManager.tick(deltaTime);
			_ashleyEngine.update(deltaTime);
		}
	}

	public void dispose() {
		coinSystem.dispose();
		_entityFactory = null;
		_waveManager = null;
	}
}
