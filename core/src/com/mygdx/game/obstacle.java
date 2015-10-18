package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class obstacle {
	MyGdxGame game;
	int width;
	int height;
	float x;
	float y;
	String sprite;
	boolean hasSprite = true;
	int flags[];
	protected ShapeRenderer defaultRender;
	protected Rectangle2D hitbox;

	protected static final int blockMovement = 0;
	protected static final int blockProjectiles = 1;
	protected static final int maxFlags = 2;
	public obstacle(MyGdxGame game, float x, float y, int width, int height)
	{
		this(game, x, y, width, height, "");
	}
	public obstacle(MyGdxGame game, float x, float y, int width, int height, String sprite)
	{
		this.game = game;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		if(sprite.length() == 0)
		{
			hasSprite = false;
			defaultRender = new ShapeRenderer();
		}
		hitbox = new Rectangle2D.Float();
		hitbox.setFrame(x, y, width, height);
		flags = new int[maxFlags];
		game.obstacles.add(this);
	}
	
	public void render()
	{
		if(hasSprite == true)
		{
			
		}
		else
		{
			defaultRender.begin(ShapeType.Filled);
			defaultRender.rect(x, y, width, height);
			defaultRender.setColor(Color.GRAY);
			defaultRender.end();
		}
	}
	
	public void update()
	{
		
	}
	
	public Rectangle2D getHitbox()
	{
		return hitbox;
	}
}
