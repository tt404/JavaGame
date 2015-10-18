package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	SpriteBatch batch;
	Texture	tex;
	TextureRegion texReg;
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
		hitbox = new Rectangle2D.Float();
		hitbox.setFrame(x, y, width, height);
		flags = new int[maxFlags];

		if(sprite.length() == 0) // [Cata] If no texture is specified draw a gray square.
		{
			hasSprite = false;
			defaultRender = new ShapeRenderer();
		}
		else // [Cata] Sets up the texture.
		{
			batch = new SpriteBatch();
			tex = new Texture(sprite);
			tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
			texReg = new TextureRegion(tex);
			texReg.setRegion(0, 0, width, height);
		}
		game.obstacles.add(this);			
	}
	
	public void render(float x, float y)
	{
		if(hasSprite == true)
		{
			batch.begin();
			batch.draw(texReg, x, y);
			batch.end();
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

	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}
