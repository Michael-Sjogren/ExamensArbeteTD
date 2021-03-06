package com.mygdx.game.entites.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Factory.EntityFactory;
import com.mygdx.game.entites.entitiycomponents.Families;
import com.mygdx.game.entites.entitiycomponents.Mappers;
import com.mygdx.game.entites.entitiycomponents.OffsetComponent;
import com.mygdx.game.entites.entitiycomponents.PositionComponent;
import com.mygdx.game.entites.entitiycomponents.projectile.DestinationComponent;
import com.mygdx.game.entites.entitiycomponents.tower.RangeComponent;

/** draws circle around the selected tower that represent the range of the tower
 * May handle the sending of selected tower information to ui in a later stage
 * **/
public class TowerSelectionSystem extends IteratingSystem {
	
	private Entity player;
	private OrthographicCamera _gameCam;
	private ShapeRenderer _shapeRenderer;

	public TowerSelectionSystem(EntityFactory factory , OrthographicCamera gameCam) {
		super(Families.TOWER);
		_gameCam = gameCam;
		player = factory.getPlayer();
		_shapeRenderer = new ShapeRenderer();
		_shapeRenderer.setAutoShapeType(true);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Entity _selectedTower = player.getComponent(DestinationComponent.class).getDestinationEntity();
		if(entity.equals(_selectedTower)){
            RangeComponent rangeComp = Mappers.RANGE_M.get(entity);
            PositionComponent posComp = Mappers.POSITION_M.get(entity);
            OffsetComponent offsetComp = Mappers.OFFSET_M.get(entity);
            drawRangeCircle(rangeComp.getRange() , posComp.position , offsetComp.offsetX , offsetComp.offsetY);
		}
	}

    private void drawRangeCircle(double range, Vector2 position, float offsetX, float offsetY) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        _shapeRenderer.setProjectionMatrix(_gameCam.combined);
        _shapeRenderer.begin();
        _shapeRenderer.setColor(0.1f,0.76f,0.1f,0.15f);
        _shapeRenderer.set(ShapeType.Filled);
        _shapeRenderer.circle(position.x + offsetX , position.y + offsetY , (float)range);
        _shapeRenderer.set(ShapeType.Line);
        _shapeRenderer.setColor(1f,1f,1f,.15f);
        _shapeRenderer.circle(position.x + offsetX , position.y + offsetY , (float)range);
        _shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

}
