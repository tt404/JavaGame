package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	
	//Array list of various entities.
	public ArrayList<player> players = new ArrayList<player>();
	public ArrayList<projectile> projectiles = new ArrayList<projectile>();
	public ArrayList<itemObject> itemObjects = new ArrayList<itemObject>();
	public ArrayList<monster> monsters = new ArrayList<monster>();
	
	//List of flags for monsters
	
	//Other stuff
	SpriteBatch batch;
	Boolean debug = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		player negro = new player(this, 0, 5.0f, 100, 0); //f means its float
		players.add(negro);
	}

	public void render () {
		renderAllEntities();
		updateAllEntities();
	}
	
	public void renderAllEntities()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for(int i = 0; i < itemObjects.size(); i++)
		{
			if(players.get(0).canRender(itemObjects.get(i).getX(), itemObjects.get(i).getY()) == true)
			itemObjects.get(i).render();
		}
		
		for(int i = 0; i < monsters.size(); i++)
		{
			monsters.get(i).render();
		}
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).render();
		}
		
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).render();
		}
	}
	
	public void updateAllEntities()
	{
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).update();
		}	
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).update();
			if(projectiles.get(i).isDead() == true)
			{
				projectiles.remove(i);
			}
		}

		for(int i = 0; i < monsters.size(); i++)
		{
			monsters.get(i).update();
		}

		for(int i = 0; i < itemObjects.size(); i++)
		{
			itemObjects.get(i).update();
			if(itemObjects.get(i).isDead() == true)
			{
				itemObjects.remove(i);
			}
		}
		
		// Do logic for projectile damage.
		if(projectiles.size() > 0 && monsters.size() > 0)
			doProjectileDamageLogic();
	}
	
	public void doProjectileDamageLogic()
	{
		for(int i = 0; i < projectiles.size(); i++)
		{
			for(int j = 0; j < monsters.size(); j++)
			{
				if(monsters.get(j).getHitbox().intersects(projectiles.get(i).getHitbox()))
				{
					//simplifying for the sake of sanity lol
					projectile proj = projectiles.get(i);
					monster mon = monsters.get(j);
					int[] flags = mon.getFlags();
					
					//dont do damage if monster is invulnerable.
					if(flags[mon.invulnerable] == 1)
						return;
					
					//damage the monster and destroy the projectile
					monsters.get(j).damage(projectiles.get(j).getDamage());
					projectiles.remove(i);
					
					//set the monster to aggro whoever attacked it
					
					
					//kill monster if its health is low
					if(monsters.get(j).getCurHealth() <= 0)
						monsters.remove(j);
				}
			}
		}
	}
	
	public float distance(double x1, double y1, double x2, double y2) 
	{ 
		return (float) Math.sqrt(((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
	}
}
