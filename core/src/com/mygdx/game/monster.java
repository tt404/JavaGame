package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class monster {
	protected float x, spawnX, centerX;
	protected float y, spawnY, centerY;
	protected int radius;
	protected int baseHealth;
	protected int curHealth;
	protected float baseSpeed;
	protected float maxRoamDist;
	protected Rectangle2D hitbox;
	protected float minAttackDist;	// [Cata] Stops the monster from getting too close to the player. Optimal attacking range basically.
	protected String name;
	protected boolean dead;
	protected MyGdxGame game;
	protected ShapeRenderer monsterShape;
	protected ShapeRenderer healthBar;
	protected Vector2 angleVector;
	protected float angle;
	protected projectile defaultProjectile;
	protected boolean canFire = true;
	
	protected SpriteBatch batch;
	protected BitmapFont font;

	public ArrayList<player> attackablePlayers = new ArrayList<player>();
	protected player curPlayerTarget;
	protected monster curMonsterTarget;
	
	protected float[] flags;
	
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
	protected static final int withinProjectileRange = 0;		// [Cata] only attack if player is within projectile range.
	protected static final int alwaysAttack = 1;				// [Cata] mindlessly attack even if player isn't in range
	protected static final int withinHalfProjectileRange = 2;	// [Cata] attack only if the player is very close
	
	protected static final int movementStyle = 5; // [Cata] Default movement pattern.
	protected static final int mindlessChase = 0; // [Cata] Chases mindlessly.
	
	protected static final int curState = 6;
	protected static final int idle = 0;
	protected static final int attack = 1;
	protected static final int retreat = 2;
	
	protected static final int flagSize = 7;
	
	public monster(MyGdxGame game, float x2, float y2)
	{
		flags = new float[flagSize];
		this.game = game;
		x = x2;
	    y = y2;
	    spawnX = x;
	    spawnY = y;
		hitbox = new Rectangle2D.Float();
		hitbox.setFrame(x, y, radius, radius);
		font = new BitmapFont();
		batch = new SpriteBatch();
        game.monsters.add(this);
        dead = false;
        healthBar = new ShapeRenderer();
        flags[curState] = idle;
        angleVector = new Vector2();
	}
	
	public void render()
	{	
		// [Cata] Don't render the healthbar if monster is idle.
		if(flags[curState] == idle) return;

		// [Cata] we now render health bars
		healthBar.begin(ShapeType.Filled);	// [Cata] This begins the shapemaking magic
		
		healthBar.setColor(Color.RED);		// [Cata] Whatever we're gonna make is now the color red.
		healthBar.rect(x, y + radius + 8, radius, 6); // [Cata] Places the filled red rectangle. Ie, the background.

		healthBar.setColor(Color.GREEN); 	// [Cata] Sets the current color to green. 
		healthBar.rect(x, y + radius + 8, radius * (((float)curHealth) / ((float)baseHealth)), 6);	// [Cata] creates the green part of the healthbar.

		healthBar.end();	// [Cata] Done rendering the healthbar. Libgdx requires you to do this for some reason.
		
		// [Cata] Render name
		this.batch.begin();
		this.font.setColor(Color.WHITE);
		if((this.name.length()*this.font.getCapHeight()) < radius)
			this.font.draw(batch, this.name, x, y);
		else
			this.font.draw(batch, this.name, x - ((this.name.length()*this.font.getCapHeight())/2 - (this.radius)) , y);
		this.batch.end();
	}
	
	public void update()
	{
		centerX = x + radius/2;
		centerY = y + radius/2;
		
		// [Cata] if our target ran away, go after the next one closest to us.
		if(attackablePlayers.size() > 0)
			updateTargetList();
		
		// [Cata] if there's no1 to attack and we're not back to spawn, retreat.
		if(attackablePlayers.size() == 0 && hitbox.contains(spawnX, spawnY) == false)
			flags[curState] = retreat;
		
		else if(attackablePlayers.size() == 0 && hitbox.contains(spawnX, spawnY) == true && flags[curState] == retreat)
			flags[curState] = idle;
		
		if(flags[curState] == retreat)
		{
			for(int i = 0; i < baseSpeed; i++)
			{
				if(centerX <= spawnX + 1 && centerX >= spawnX - 1) {}
			    else if(centerX <= spawnX) x++;
				else x--;

				if(centerY <= spawnY + 1 && centerY >= spawnY - 1) {}
			    else if(centerY <= spawnY) y++;
				else y--;
			}
			
			angleVector.x = spawnX - (x + (radius / 2));
			angleVector.y = spawnY - (y + (radius / 2));
			angle = angleVector.angle();		
		}
		
		
		if(attackablePlayers.size() == 0 || curPlayerTarget == null)
			return;
		
		// [Cata] This is the logic for attacking.
		if(flags[attackStyle] == withinProjectileRange)
		{
			if(game.distance(centerX, centerY, curPlayerTarget.getCenterX(), curPlayerTarget.getCenterY()) <= defaultProjectile.travelDistance)
				fire();
				
		}
		else if(flags[attackStyle] == withinHalfProjectileRange)
		{
			if(game.distance(centerX, centerY, curPlayerTarget.getCenterX(), curPlayerTarget.getCenterY()) <= defaultProjectile.travelDistance / 2)
				fire();			
		}
		else
		{
			fire();
		}
		
			
		// [Cata] This is the ai for mindlessChase movement.
		if(flags[movementStyle] == mindlessChase)
		{
			if(flags[curState] != attack) flags[curState] = attack;

			for(int i = 0; i < (int)baseSpeed; i++)
			{
				if(game.distance(curPlayerTarget.getCenterX(), curPlayerTarget.getCenterY(), centerX, centerY) < minAttackDist)
					break;
				
				if(centerX <= curPlayerTarget.getCenterX() + 1 && centerX >= curPlayerTarget.getCenterX() - 1) {}
				else if(centerX <= curPlayerTarget.getCenterX()) x++;
				else x--;
				
				if(centerY <= curPlayerTarget.getCenterY() + 1 && centerY >= curPlayerTarget.getCenterY() - 1) {}
				else if(centerY <= curPlayerTarget.getCenterY()) y++;
				else y--;
			}

			angleVector.x = curPlayerTarget.getCenterX() - (x + (radius / 2));
			angleVector.y = curPlayerTarget.getCenterY() - (y + (radius / 2));
			angle = angleVector.angle();		
		}
	}
	
	// [Cata] removes targets from pool if they ran away, switches target, etc...
	public void updateTargetList()
	{
		if(attackablePlayers.size() == 0 && flags[curState] == attack)
			flags[curState] = retreat;
		
		for(int i = 0; i < attackablePlayers.size(); i++)
			if(game.distance(spawnX, spawnY, attackablePlayers.get(i).getCenterX(), attackablePlayers.get(i).getCenterY()) > maxRoamDist)
			{
				if(curPlayerTarget.equals(attackablePlayers.get(i)) && attackablePlayers.size() > 1)
				{
					curPlayerTarget = null;
					findNewTarget();
				}
				
				attackablePlayers.remove(i);
				updateTargetList();
				return;
			}
		
	}
	
	public void findNewTarget()
	{
		if(attackablePlayers.size() == 0)
		{
			curPlayerTarget = null;
			return;
		}
		else if(curPlayerTarget == null)
			curPlayerTarget = attackablePlayers.get(0);
		
		for(int i = 0; i < attackablePlayers.size(); i++)
			for(int j = 0; j < attackablePlayers.size(); j++)
				if(i != j && game.distance(attackablePlayers.get(i).getCenterX(), attackablePlayers.get(i).getCenterY(), spawnX, spawnY) < maxRoamDist)
					if(game.distance(x, y, attackablePlayers.get(j).getCenterX(), attackablePlayers.get(j).getCenterY()) < 
					   game.distance(x, y, attackablePlayers.get(i).getCenterX(), attackablePlayers.get(i).getCenterY()))
						curPlayerTarget = attackablePlayers.get(j);
		
		curPlayerTarget = null;
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
	
	public void damage(int amount, player who)
	{
		curHealth -= amount;
		addAttackablePlayer(who);	// [Cata] adds whoever damaged this mosnter into a "pool" of players to target.
		flags[curState] = attack;
	}
	
	public int getCurHealth()
	{
		return curHealth;
	}
	
	public void setPlayerTarget (player target)
	{
		curPlayerTarget = target;
	}
	
	public float[] getFlags()
	{
		return flags;
	}
	
	public void addAttackablePlayer(player who)
	{
		// [Cata] Adds a player to the "pool" of targets. One player cannot be listed twice.
		for(int i = 0; i < attackablePlayers.size(); i++)
			if(who.equals(attackablePlayers.get(i)))
				return;
		attackablePlayers.add(who);
	}

	// [Cata] Added this to prevent crashes during projectile damage logic.
	public void die()
	{
		dead = true;
	}
	
	// [Cata] Needed so monsters can inherit this function
	public void fire()
	{
	}
}
