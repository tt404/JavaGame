package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class inventoryRustySword extends inventory {

	public inventoryRustySword(MyGdxGame myGdxGame, player owner) {
		super(myGdxGame, owner);
		this.ID = 2;
		this.delayTime = 0.3f;	// [Cata] Increased speed.
		this.inventorySprite = "rustySwordInventory.png";
		this.canFire = true;
		this.canDrop = true;
		this.name = "Rusty Sword";
}

	public void fireGo()
	{
		if(owner.checkEquipped(this) == false) return;
        projectile weaponProjectile = new projectile(game, owner, 8, 24.0f, Color.WHITE, 112.0f, owner.getAngle(), 10);
        weaponProjectile = new projectile(game, owner, 8, 24.0f, Color.WHITE, 96.0f, owner.getAngle() + 7, 10);
        weaponProjectile = new projectile(game, owner, 8, 24.0f, Color.WHITE, 96.0f, owner.getAngle() - 7, 10);
        canFire = false;
		Timer.schedule(new Task(){
		    public void run() {
			        canFire = true;
				}
		   
		}, delayTime);
	}
	
	public boolean isEquipable()
	{
		return true;
	}
	
	public String getInventorySprite()
	{
		return this.inventorySprite;
	}

}
