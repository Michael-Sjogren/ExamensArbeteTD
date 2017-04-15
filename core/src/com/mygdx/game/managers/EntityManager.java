package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Factory.EntityFactory;
import com.mygdx.game.entites.systems.CameraDirComponent;
import com.mygdx.game.entites.systems.CameraMovementSystem;
import com.mygdx.game.entites.systems.HealthSystem;
import com.mygdx.game.entites.systems.InputHandler;
import com.mygdx.game.entites.systems.MoveToSystem;
import com.mygdx.game.entites.systems.PlayerInputSystem;
import com.mygdx.game.entites.systems.RenderSystem;

public class EntityManager {
    private final EntityFactory _entityFactory;
    private Engine _ashleyEngine;
	private WaveTimeManager _waveManager;
	private OrthographicCamera _gameCamera;
	private InputHandler inputhandler;

    public EntityManager(Engine ashleyEngine , SpriteBatch batch, OrthographicCamera gameCamera, InputHandler inputhandler){
        Entity playerEntity = new Entity();
        CameraDirComponent dirComp = new CameraDirComponent();
        playerEntity.add(dirComp);
    	
    	this._ashleyEngine = ashleyEngine;
		this._gameCamera = gameCamera;
		this.inputhandler = inputhandler;
        _entityFactory = new EntityFactory(ashleyEngine);
        MoveToSystem moveToSystem = new MoveToSystem();
        RenderSystem renderSystem = new RenderSystem(batch);
        HealthSystem healthSystem = new HealthSystem(batch);
        PlayerInputSystem playerInputSys = new PlayerInputSystem();
        inputhandler.registerInputHandlerSystem(playerInputSys);
        CameraMovementSystem camSys = new CameraMovementSystem(gameCamera);
        ashleyEngine.addSystem(moveToSystem);
        ashleyEngine.addSystem(renderSystem);
        ashleyEngine.addSystem(healthSystem);
        ashleyEngine.addSystem(playerInputSys);
        ashleyEngine.addSystem(camSys);
        ashleyEngine.addEntity(playerEntity);
        _waveManager = new WaveTimeManager(_entityFactory);
    }

    public void update(float deltaTime){
        _ashleyEngine.update(deltaTime);
        _waveManager.tick(deltaTime);
    }
    public EntityFactory getEntityFactory(){
    	return _entityFactory;
    }
}
