package com.mygdx.game;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class monsterGoblinMage extends monster {

	public monsterGoblinMage(MyGdxGame game, float x, float y) {
		this(game, x, y, 0);
	}
	
	public monsterGoblinMage(MyGdxGame game, float x, float y, int tid) {
		super(game, x, y, tid);
		
		// [Cata] the monster flags, see monsters.java for all of them
		this.flags[behavior] = neutral;
		this.flags[attackStyle] = withinProjectileRange;
		this.flags[movementStyle] = mindlessChase;
		this.flags[attackStyle]	= alwaysAttack;
		
		// [Cata] stats
		this.baseSpeed = 1.7f;  			// [Cata] Speed?
		this.baseHealth = 25;				// [Cata] Spawn health.
		this.curHealth = 25;				// [Cata] Sets it's current health to the spawn health.
		this.radius = 96;					// [Cata] How fat is this monster?
		this.maxRoamDist = 900; 			// [Cata] How far can it leave spawn to go kill people?
		this.minAttackDist = radius * 4;	// [Cata] How far does it have to be before attacking? This also stops the monster from
											// "hugging" the player.
		this.name = "Mage Goblin";			// [Cata] Yes.

		// [Cata] This sets up our sprite.
		tex = new Texture("GobMage0.png");
		texReg = new TextureRegion(tex);
		texReg.setRegion(0, 0, radius, radius);
	}
	
	public void update()
	{
		super.update();	// [Cata] runs the update function in monster.java
	    hitbox.setFrame(x, y, radius, radius);
	}
	
	public void render(float x, float y)
	{
		super.render(x, y);	// [Cata] Renders health bar.					
		
		// [Cata] Draws the goblin sprite.
		batch.begin();
		batch.draw(texReg, x, y);
		batch.end();
	}
	
	// [Cata] a basic monster attack
	public void fire()
	{
		if(canFire == false)
			return;
		
		// [Cata] Setting up its attack.		
		defaultProjectile = new projectile(game, this, 10, 12.0f, Color.CYAN, radius*20, angle, 10);
		
        canFire = false;
		Timer.schedule(new Task(){
		    public void run() {
			        canFire = true;
				}
		   
		}, 1.0f);
	}

}
