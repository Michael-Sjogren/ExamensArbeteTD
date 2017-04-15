package com.mygdx.game.entites.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.entites.entitiycomponents.*;
import com.mygdx.game.managers.LevelManager;

public class CameraMovementSystem extends IteratingSystem {

    private Camera _camera;
    private ComponentMapper<DirectionComponent> cm;

    public CameraMovementSystem(OrthographicCamera newCam) {
        super(Family.one(DirectionComponent.class).exclude(SkeletonComponent.class
                , RenderableComponent.class
                , PathComponent.class
                , TextureComponent.class
                , RenderableComponent.class).get());
        cm = ComponentMapper.getFor(DirectionComponent.class);
        _camera = newCam;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DirectionComponent d = cm.get(entity);
        moveCamera(d);
    }

    private void moveCamera(DirectionComponent d) {
        final float cameraSpeed = 10f;
        _camera.viewportWidth = Gdx.graphics.getWidth() / 3;
        _camera.viewportHeight = Gdx.graphics.getHeight() / 3;
        float cameraPosX = _camera.position.x;
        float cameraPosY = _camera.position.y;
        _camera.position.set((int) cameraPosX + d.xAxis * cameraSpeed,
                (int) cameraPosY + d.yAxis* cameraSpeed, 0);
        _camera.update();
        float startX = _camera.viewportWidth / 2;
        float startY = _camera.viewportHeight / 2;
        float width = startX * 2;
        float height = startY * 2;
       
      setCameraBoundary( startX, startY, LevelManager.mapPixelWidth - width,
        LevelManager.mapPixelHeight - height);
    }

    private void setCameraBoundary( float startX, float startY, float width, float height) {
        Vector3 position = _camera.position;
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
        _camera.position.set(position);
        _camera.update();
    }
}



