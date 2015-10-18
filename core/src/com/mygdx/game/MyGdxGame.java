package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
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
	public ArrayList<monsterSpawner> monsterSpawners = new ArrayList<monsterSpawner>();
	public ArrayList<obstacle> obstacles = new ArrayList<obstacle>();
	public ArrayList<monster> monsters = new ArrayList<monster>();
	public int tidList[] = new int [100000]; // [Cata] tid list
	public player defaultPlayer; // [Cata] This is the player you play.
	public float screenCenterX;	// [Cata] This is the "center" of the screen, ie the player.
	public float screenCenterY;

	//Other stuff
	SpriteBatch batch;
	Boolean debug = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		screenCenterX = Gdx.graphics.getWidth() / 2;
		screenCenterY = Gdx.graphics.getHeight() / 2;
				
		defaultPlayer = new player(this, 0, 5.0f, 100, 0); //f means its float
		players.add(defaultPlayer);
		monsterSpawner test = new monsterSpawner(this, defaultPlayer.getCenterX(), defaultPlayer.getCenterY(), 64, 5.0f, "monsterGoblin", 3, 1);
		obstacle besttest = new obstacle(this, defaultPlayer.getCenterX(), defaultPlayer.getCenterY() + 128, 256, 32);
	}

	public void render () {
		try {
			updateAllEntities();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	
	public void renderAllEntities()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for(int i = 0; i < obstacles.size(); i++)
		{
			obstacles.get(i).render(screenCenterX - (defaultPlayer.getX() - obstacles.get(i).getX()), screenCenterY - (defaultPlayer.getY() - obstacles.get(i).getY()));
		}
		
		for(int i = 0; i < itemObjects.size(); i++)
		{
			if(players.get(0).canRender(itemObjects.get(i).getX(), itemObjects.get(i).getY()) == true)
				itemObjects.get(i).render(screenCenterX - (defaultPlayer.getX() - itemObjects.get(i).getX()), screenCenterY - (defaultPlayer.getY() - itemObjects.get(i).getY()));
		}

		// [Cata] render all projectiles first.
		for(int i = 0; i < projectiles.size(); i++)
		{
			//if(projectiles.get(i).player == null)
			projectiles.get(i).render(screenCenterX - (defaultPlayer.getX() - projectiles.get(i).getX()), screenCenterY - (defaultPlayer.getY() - projectiles.get(i).getY()));
		}
		
		for(int i = 0; i < monsters.size(); i++)
		{
			monsters.get(i).render(screenCenterX - (defaultPlayer.getX() - monsters.get(i).getX()), screenCenterY - (defaultPlayer.getY() - monsters.get(i).getY()));
		}
		/*
		for(int i = 0; i < projectiles.size(); i++)
		{
			if(projectiles.get(i).monster == null)
				projectiles.get(i).render();
		}
		*/
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).render(screenCenterX - (defaultPlayer.getX() - players.get(i).getX()), screenCenterY - (defaultPlayer.getY() - players.get(i).getY()));
		}
	}
	
	public void updateAllEntities() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).update();
		}	
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).update();
			if(projectiles.get(i).dead == true)
			{
				projectiles.get(i).die(); // [Cata] This will prevent crashes and safely loop.
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
		
		// [Cata] keep track of spawners
		for(int i = 0; i < monsterSpawners.size(); i++)
		{
			monsterSpawners.get(i).update();
		}
		
		// [Cata] Do logic for projectile damage.
		doProjectileDamageLogic();
		
		// [Cata] Projectile / Monster remover.
		removeDeadThings();

		// [Cata] After all the game logic is done, render.
		renderAllEntities();
	}
	
	public void removeDeadThings()
	{
		for(int i = projectiles.size() - 1; i >= 0; i--)
			if(projectiles.get(i).dead == true)
				projectiles.remove(i);

		for(int j = monsters.size() - 1; j >= 0; j--)
			if(monsters.get(j).dead == true)
			{				
				tidList[monsters.get(j).getTid()]--;
				monsters.remove(j);	
			}
	}
	
	public void doProjectileDamageLogic()
	{
		for(int curProj = projectiles.size() - 1; curProj >= 0; curProj--)
		{
			for(int curMon = monsters.size() - 1; curMon >= 0; curMon--)
			{
				if(projectiles.get(curProj).monster == null && monsters.get(curMon).getHitbox().intersects(projectiles.get(curProj).getHitbox()))
				{		
					float[] flags = monsters.get(curMon).getFlags();
					
					//dont do damage if monster is invulnerable.
					if(flags[monsters.get(curMon).invulnerable] == 1)
						return;
					
					// [Cata] Damage monsters only if players have fired the projectile.
					//		  This means there is no teamkilling between monsters.
					monsters.get(curMon).damage(projectiles.get(curProj).getDamage(), projectiles.get(curProj).player); // [Cata] This actually damages the monster.
					monsters.get(curMon).setPlayerTarget(projectiles.get(curProj).player); // [Cata] Now the monster is targetting the player who recently damaged it.

					// [Cata] The projectile will die even if it damaged nothing. Meaning you
					//		  can use monsters to body block projectiles.
					projectiles.get(curProj).die();
					
					//TODO: set the monster to aggro whoever attacked it
					
					
					//kill monster if its health is low
					if(monsters.get(curMon).getCurHealth() <= 0)
						monsters.get(curMon).die();
					
				}
			}
			for(int curPlayer = players.size() - 1; curPlayer >= 0; curPlayer--)
			{
				if(projectiles.get(curProj).player == null && players.get(curPlayer).getHitbox().intersects(projectiles.get(curProj).getHitbox()))
				{
					players.get(curPlayer).damage(projectiles.get(curProj).getDamage(), projectiles.get(curProj).monster);
					projectiles.get(curProj).die();	
					//TODO: Kill the player if his health falls too low.
				}				
			}
			for(int curObs = 0; curObs < obstacles.size(); curObs++)
			{
				if(projectiles.get(curProj).getHitbox().intersects(obstacles.get(curObs).getHitbox()))
					projectiles.get(curProj).die();
			}
		}	
	}
	
	public float distance(double x1, double y1, double x2, double y2) 
	{ 
		return (float) Math.sqrt(((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
	}
	
	// [Cata] Unique tids kek
	public int findUniqueTID()
	{
		for(int i = 1; i < 100000; i++)
		{
			if(tidList[i] > 0)
				return i;
		}
		return -1;
	}
	
	public boolean checkObstacleCollision(Rectangle2D thing)
	{
		for(int i = 0; i < obstacles.size(); i++)
			if(obstacles.get(i).getHitbox().intersects(thing))
			{
				return true;
			}
		return false;
	}
	
	public int tidCount(int val)
	{
		if(val < 0 || val >= 100000) return -1;
		return tidList[val];
	}
}
