package com.mygdx.game;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class monsterGoblin extends monster {
	public monsterGoblin(MyGdxGame game, float x, float y) {
		super(game, x, y);
		
		// [Cata] the monster flags, see monsters.java for all of them
		this.flags[behavior] = neutral;
		this.flags[attackStyle] = withinProjectileRange;
		this.flags[movementStyle] = mindlessChase;
		this.flags[attackStyle]	= alwaysAttack;
		
		// [Cata] stats
		this.baseSpeed = 2.4f;  			// [Cata] Speed?
		this.baseHealth = 40;				// [Cata] Spawn health.
		this.curHealth = 40;				// [Cata] Sets it's current health to the spawn health.
		this.radius = 38;					// [Cata] How fat is this monster?
		this.maxRoamDist = 640; 			// [Cata] How far can it leave spawn to go kill people?
		this.minAttackDist = radius * 2;	// [Cata] How far does it have to be before attacking? This also stops the monster from
											// "hugging" the player.
		this.name = "Goblin";				// [Cata] Yes.
		
		//Its shots...
		defaultProjectile = new projectile(game, this, 10, 24, Color.LIGHT_GRAY, radius*2, angle, 10);


		// [Cata] other.
		monsterShape = new ShapeRenderer();
	}
	
	public void update()
	{
		super.update();	// [Cata] runs the update function in monster.java
		this.hitbox.setFrame(x, y, radius, radius);
	}
	
	public void render()
	{
		super.render();	// [Cata] Renders health bar.
		this.batch.begin();
		this.font.setColor(Color.WHITE);
		this.font.draw(batch, this.name, x , y);
		this.batch.end();
		
		monsterShape.begin(ShapeType.Filled);
		monsterShape.rect(this.x, this.y, this.radius, this.radius);
		monsterShape.setColor(Color.GREEN);
		monsterShape.end();
	}
	
	// [Cata] a basic monster attack
	public void fire()
	{
		if(canFire == false)
			return;
		
		// [Cata] Setting up its attack.		
		defaultProjectile = new projectile(game, this, 10, 10.0f, Color.LIGHT_GRAY, radius*4, angle, 10);
		game.projectiles.add(defaultProjectile);
		
        canFire = false;
		Timer.schedule(new Task(){
		    public void run() {
			        canFire = true;
				}
		   
		}, 0.7f);
	}

}
