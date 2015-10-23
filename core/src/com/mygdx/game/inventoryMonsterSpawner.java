package com.mygdx.game;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class inventoryMonsterSpawner extends inventory {

	public inventoryMonsterSpawner(MyGdxGame myGdxGame)
	{
		this(myGdxGame, null);
	}
	
	public inventoryMonsterSpawner(MyGdxGame myGdxGame, player owner) {
		super(myGdxGame, owner);
		this.ID = 1;
		this.delayTime = 0.75f;
		this.inventorySprite = "greenAshInventory.png";
		this.canFire = true;
		this.canDrop = true;
		this.name = "Monster Spawner";
	}

	public void fireGo()
	{
		if(owner.checkEquipped(this) == false) return;
		monsterGoblinMage spawn = new monsterGoblinMage(game, owner.getMouse().x, owner.getMouse().y);
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