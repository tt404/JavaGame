package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class monster {
	protected float x, spawnX;
	protected float y, spawnY;
	protected int radius;
	protected int baseHealth;
	protected int curHealth;
	protected float baseSpeed;
	protected float maxRoamDist;
	protected Rectangle2D hitbox;
	protected String name;
	private MyGdxGame game;

	protected SpriteBatch batch;
	protected BitmapFont font;

	public ArrayList<player> attackablePlayers = new ArrayList<player>();
	protected player curPlayerTarget;
	protected monster curMonsterTarget;
	
	protected int[] flags;
	
	//0 = Aggressive; 1 = neutral; 2 = friendly
	protected static final int behavior = 0;
	protected static final int agressive = 0;
	protected static final int neutral = 1;
	protected static final int friendly = 2;
	
	//Makes monster stay in one place but will still fire.
	protected static final int stationary = 1;
	
	//Same as stationary but won't do anything at all.
	protected static final int frozen = 2;
	
	//Monster takes no damage if true
	protected static final int invulnerable = 3;
	
	//0 = attack only when in max range of projectile;
	//1 = attack even if out of range;
	//2 = attack only if target is maxrange/2 distance away.
	protected static final int attackStyle = 4;
	protected static final int withinProjectileRange = 0;
	protected static final int alwaysAttack = 1;
	protected static final int withinHalfProjectileRange = 2;
	
	protected static final int flagSize = 5;
	
	public monster(MyGdxGame game, float x2, float y2)
	{
		flags = new int[flagSize];
		this.game = game;
		x = x2;
	    y = y2;
	    spawnX = x;
	    spawnY = y;
		hitbox = new Rectangle2D.Float();
		hitbox.setFrame(x, y, radius, radius);
		font = new BitmapFont();
		batch = new SpriteBatch();
	}
	
	public void render()
	{	
	}
	
	public void update()
	{
	}

	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public Rectangle2D getHitbox()
	{
		return hitbox;
	}
	
	public void damage(int amount)
	{
		curHealth -= amount;
	}
	
	public int getCurHealth()
	{
		return curHealth;
	}
	
	public void setPlayerTarget (player target)
	{
		curPlayerTarget = target;
	}
	
	public int[] getFlags()
	{
		return flags;
	}
	
	public void addAttackablePlayer(player who)
	{
		attackablePlayers.add(who);
	}
}
