package com.mygdx.game.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface DropListener {
	void drop(Actor actor, float x, float y, InputEvent event);
}