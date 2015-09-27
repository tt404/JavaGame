package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class object {
	private MyGdxGame game;
	private int ID;
	private String sprite;
	private String name;
	private boolean canUse = false;
	private boolean canAnalyze = false;
	private float x;
	private float y;
	private inventory item;
	boolean dead = false;
	public object(MyGdxGame myGdxGame, float x, float y)
	{
		this.game = myGdxGame;
		this.ID = 0;
		this.name = "";
	}
	
	public void die()
	{
		dead = true;
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public boolean canUse()
	{
		return canUse;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getSprite()
	{
		return "null.png";
	}
	
	public String getName()
	{
		return name;
	}
}
