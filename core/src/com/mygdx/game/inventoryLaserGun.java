package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class inventoryLaserGun extends inventory {

	public inventoryLaserGun(MyGdxGame myGdxGame, player owner) {
		super(myGdxGame, owner);
		this.ID = 1;
		this.delayTime = 0.5f;								//[chi]  increzsed speed1
		this.inventorySprite = "laserGunInventory.png";
		this.canFire = true;
		this.canDrop = true;
		this.name = "Laser Pistol";
}

	public void fireGo()
	{
		if(owner.checkEquipped(this) == false) return;
		//System.out.print("kek");
        projectile weaponProjectile = new projectile(game, owner, 8, 16.0f, Color.RED, 500.0f, owner.getAngle(), 10);
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
