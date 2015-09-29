package com.mygdx.game;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class projectile {
	private MyGdxGame game;
	private float x;
	private float y;
	private int radius;
	private int damage;
	private float speed;
	private Color color;
	private player owner;
	private monster owner2;
	private ShapeRenderer shapeRenderer;
	private float angle;
	private float travelDistance;
	private float maxDistance;
	protected boolean dead = false;
	private Rectangle2D.Float hitbox;

	public projectile(MyGdxGame game2, player owner, int radius, float speed, Color color, float maxDistance, float angle, int damage)
	{
		this.game = game2;
		this.radius = radius;
		this.speed = speed;
		this.color = color;
		this.owner = owner;
		this.x = owner.getX() + (owner.getRadius() / 2);
		this.y = owner.getY() + (owner.getRadius() / 2);
		this.angle = angle;
		this.maxDistance = maxDistance;
		this.damage = damage;
		hitbox = new Rectangle2D.Float(x, y, radius, radius);
		shapeRenderer = new ShapeRenderer();
	}
	
	public projectile(MyGdxGame game2, monster owner, int radius, float speed, Color color, float maxDistance, float angle, int damage)
	{
		this.game = game2;
		this.radius = radius;
		this.speed = speed;
		this.color = color;
		this.owner2 = owner;
		this.x = owner.getX() + (owner.getRadius() / 2);
		this.y = owner.getY() + (owner.getRadius() / 2);
		this.angle = angle;
		this.maxDistance = maxDistance;
		shapeRenderer = new ShapeRenderer();
	}
	
	public void render()
	{
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(color);
		 shapeRenderer.rect(x, y, 0, 0, radius, radius, 1, 1, 0);
		 shapeRenderer.end();
	}
	
	public void update()
	{
		for(int i = 0; i < speed; i++)
		{
			y += Math.sin(Math.toRadians(angle)) * 1; //move by 1 unit
			x += Math.cos(Math.toRadians(angle)) * 1; //move by 1 unit
			travelDistance++;			
		}
		
		if(travelDistance > maxDistance)
		{
			dead = true;
		}
		hitbox.setFrame(x, y, radius, radius);
	}
	
	public boolean isTouching()
	{
		return true;
	}
	
	public Rectangle2D getHitbox()
	{
		return hitbox;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	// [Cata] Added this to prevent crashes during projectile damage logic.
	public void die()
	{
		dead = true;
	}
}
