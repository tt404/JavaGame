package com.mygdx.game;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class monsterGoblin extends monster {
	private ShapeRenderer monsterShape;
	
	public monsterGoblin(MyGdxGame game, float x, float y) {
		super(game, x, y);
		this.flags[behavior] = neutral;
		this.flags[attackStyle] = withinProjectileRange;
		this.baseHealth = this.curHealth = 40;			//Bang He's tougher now [YASSSSS]
		this.radius = 48;
		this.name = "Goblin";
		monsterShape = new ShapeRenderer();
	}
	
	public void update()
	{
		this.hitbox.setFrame(x, y, radius, radius);
	}
	
	public void render()
	{
		this.batch.begin();
		this.font.setColor(Color.WHITE);
		this.font.draw(batch, this.name, x , y);
		this.batch.end();
		
		monsterShape.begin(ShapeType.Filled);
		monsterShape.rect(this.x, this.y, this.radius, this.radius);
		monsterShape.setColor(Color.GREEN);
		monsterShape.end();
	}

}
