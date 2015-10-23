package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class inventory {
	protected MyGdxGame game;
	protected player owner;
	protected int ID;
	protected float delayTime;
	protected String inventorySprite;
	protected String name;
	protected boolean canFire = false;
	protected boolean canDrop = false;
	protected boolean canUse = false;
	protected boolean canAnalyze = false;

	// [Cata] this means that the item doesn't have a "owner", as in it is on the floor and NOT in the inventory.
	public inventory(MyGdxGame myGdxGame)
	{
		this(myGdxGame, null);
	}

	public inventory(MyGdxGame myGdxGame, player owner)
	{
		this.game = myGdxGame;
		this.owner = owner;
		this.ID = 0;
		this.name = "";
	}
	
	public void fire()
	{
		//System.out.println("pew pew");

		if(canFire == true && 
		   owner.isMouseTouchingUI() == false &&
		   owner.getMouse().isMouseTouchingDroppedItems() == false &&
		   owner.getMouse().isDragging() == false)
		{
			//System.out.println("lol");
			fireGo();
			owner.combatIndicatorHelper(); // [Cata] mark the player as "in combat"
		}
			//System.out.println("canfire = " + canFire + "\nTouching UI = " + owner.isMouseTouchingUI() + "\ndropped items touch = " + owner.getMouse().isMouseTouchingDroppedItems());
	}
	
	public boolean canDrop()
	{
		return canDrop;
	}
	
	public boolean canUse()
	{
		return canUse;
	}
	
	public void fireGo()
	{
		
	}
	
	public void drop()
	{
		if(canDrop == true)
		{
			itemObject droppedItem = new itemObject(game, this, owner.getCenterX() + 16, owner.getCenterY() + 16);
		}
	}
	
	public int getID()
	{
		return ID;
	}
	
	public boolean isEquipable()
	{
		return false;
	}
	
	public boolean isWeapon()
	{
		return canFire;
	}
	
	public String getInventorySprite()
	{
		return "null.png";
	}
	
	public String getItemName()
	{
		return name;
	}
}
