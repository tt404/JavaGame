package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class UIbackground {
	private float x;
	private float y;
	private ShapeRenderer shapeRenderer;
	private int id;
	private float width = 32;
	private float height = 32;
	private Color boxColor;
	private player owner;
	public UIbackground(player owner, float x, float y, int width, int height, Color color, int id)
	{
		this.owner = owner;
		shapeRenderer = new ShapeRenderer();
		this.id = id;
		boxColor = color;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public void render()
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(boxColor);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}

	public void setColor(Color color)
	{
		boxColor = color;
	}
	
	public float getWidth()
	{
		return width;
	}

	public float getHeight()
	{
		return height;
	}
	
	public void setWidth(float width)
	{
		this.width = width;
		owner.getMouse().updateBackgroundInfo();
	}
	
	public void setHeight(float height)
	{
		this.height = height;
		owner.getMouse().updateBackgroundInfo();
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public boolean isPointTouching(float x, float y)
	{
		if(x > this.x && x < this.x + width && y > this.y && y < this.y + height)
		{
			return true;
		}
		return false;
	}
}
