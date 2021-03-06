package com.mygdx.game.entites.entityinformation.towers;

import com.mygdx.game.entites.entityinformation.EntityInformation;
import com.mygdx.game.utils.Assets;

public class MissleTurret extends EntityInformation {
	public MissleTurret() {
		setDamage(50d);
		setFireRate(1);
		setRange(150d);
//		setSplashRadius(55f);
		setMaxTargets(3);
		setIsSplash(false);
		setAnimationStateData(Assets.missleTowerAnimationState.getData());
		setSkeleton(Assets.missleTowerSkeleton);
		setCost(100d);
		setDescription("The missile tower fires mutlible missles at air targets.\n\nCan not fire at ground targets.");
		setName("Missile Tower");
		setOffsetX(0);
		setOffsetY(0);
		setMultiTarget(true);
	}	
}
