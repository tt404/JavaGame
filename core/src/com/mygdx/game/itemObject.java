package com.mygdx.game;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class itemObject {
	protected MyGdxGame game;
	protected int ID;
	private Texture texture;
	protected String name;
	private boolean canUse = false;
	private boolean canPickup = true;
	private boolean canAnalyze = false;
	private inventory item;
	private SpriteBatch batch;
	private float x;
	private float y;
	private float renderX;
	private float renderY;
	private float width = 32;
	private float height = 32;
	private ShapeRenderer shapeRenderer;
	private boolean dead;
	public itemObject(MyGdxGame myGdxGame, inventory item, float x, float y)
	{
		this.game = myGdxGame;
		this.ID = item.getID();
		this.name = item.getItemName();
		this.item = item;
		this.x = x;
		this.y = y;
		this.texture = new Texture(item.getInventorySprite());
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		myGdxGame.itemObjects.add(this);	// [Cata] dropped items auto-add to the game.
	}
	
	public boolean canUse()
	{
		return canUse;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void render(float x, float y)
	{
		this.renderX = x;
		this.renderY = y;
		batch.begin();
		batch.draw(texture, x - width, y - height);
		batch.end();
		if(game.debug == true)
		{
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.WHITE);			
			shapeRenderer.rect(x - width, y - height, width, height);
			shapeRenderer.end();
		}
	}
	
	public void update()
	{
		//System.out.println("Object XY (" + x + "," + y + ")");
	}
	
	public void pickup(player who)
	{
		who.give(item, 1);
		dead = true;
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public void die()
	{
		dead = true;
	}
	
	public String getSprite()
	{
		return item.getItemName();
	}
	
	public String getName()
	{
		return name;
	}

	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getRenderX()
	{
		return renderX;
	}
	
	public float getRenderY()
	{
		return renderY;
	}

	public float getWidth()
	{
		return width;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	public inventory getItem()
	{
		return item;
	}
}