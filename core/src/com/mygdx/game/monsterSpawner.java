package com.mygdx.game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class monsterSpawner {
	protected float x;
	protected float y;
	protected int radius;
	protected float delay;
	protected String className;
	protected MyGdxGame game;
	protected int tid;
	protected int maxSpawn;
	protected boolean canSpawn = true;
	public monsterSpawner(MyGdxGame game, float x, float y, int radius, float delay, String className, int maxSpawn, int tid)
	{
		this.game = game;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.delay = delay;
		this.className = className;
		this.tid = tid;
		this.maxSpawn = maxSpawn;
		game.monsterSpawners.add(this);
		System.out.println(this.tid);
	}
	
	public void update() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		if(game.tidCount(tid) < maxSpawn && canSpawn == true)
		{		
			canSpawn = false;
			Timer.schedule(new Task(){
			    public void run() {
				        canSpawn = true;		
				        try {
							spawn();
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
			   
			}, delay);
		}
	}
	
	public void spawn() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		Random random = new Random();
		int maxX = (int)(x+radius);
		int minX = (int)(x-radius);
		int maxY = (int)(y+radius);
		int minY = (int)(y-radius);
		
		int randX = random.nextInt(maxX - minX) + minX;
		int randY = random.nextInt(maxY - minY) + minY;
		
		String fullClassName = "com.mygdx.game." + className;
		Class clazz = Class.forName(fullClassName);
		Class[] parameters = new Class[] {MyGdxGame.class, float.class, float.class, int.class};
		Constructor constructor = clazz.getConstructor(parameters);
		Object o = constructor.newInstance(new Object[] {game, randX, randY, tid});
		//System.out.println(o);
	}
}
