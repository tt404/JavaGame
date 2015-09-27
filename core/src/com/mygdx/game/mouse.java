package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class mouse {
	private int type;
	private SpriteBatch batch;
	private Texture img;
	private TextureRegion image;
	private String[] crossHairTypes = {"xhairb32.png"};
	private int rotateVal;
	private player owner;
	private UIbackground inventoryBackground;
	private boolean dragging = false;
	protected float x;
	protected float y;
	private MyGdxGame game;
	protected itemObject curObjectTouched;
	private BitmapFont itemFont;
	private boolean clickedBefore = false;
	public mouse(MyGdxGame myGdxGame, player owner, int type) //color will do something one day in the distant future
	{
		this.owner = owner;
		this.type = type;
		this.game = myGdxGame;
		batch = new SpriteBatch();
		img = new Texture(crossHairTypes[type]);
		image = new TextureRegion(img, 0, 0, 19, 19);
		inventoryBackground = owner.getInventoryBackground();
		itemFont = new BitmapFont();
		//Gdx.input.setCursorCatched(true);
	}
	
	public void render()
	{
		batch.begin();
	
		if(inventoryBackground.isPointTouching(x, y) == true){}
		
		else
		{
			if(curObjectTouched == null)
			batch.draw(image, 
						Gdx.input.getX() - (image.getRegionWidth() / 2), 
						-Gdx.input.getY() + Gdx.graphics.getHeight() - (image.getRegionHeight() / 2),
						9,
						9,
						19,
						19,
						1.0f,
						1.0f,
						rotateVal);
			else if(isMouseTouchingDroppedItems())
			{
				itemFont.draw
				(
						batch, 
						curObjectTouched.getItem().getItemName(), 
						getX() - 32.0f, 
						getY() + (itemFont.getCapHeight() * 2)
				);
			}
			rotateVal += 2;
		}
		batch.end();
		if(rotateVal >= 360) rotateVal = -2;
	}
	
	public void update()
	{
		
		//******************
		// Update XY Coords.
		//******************
		x = Gdx.input.getX();
		y = -Gdx.input.getY() + Gdx.graphics.getHeight();

		//Debug.
		//System.out.println("Mouse XY (" + x + "," + y + ")");

		//******************
		// Set mouse properties.
		//******************
		
		// 	Sets the "dragging" property. Basically saying that we are dragging
		//	something in the inventory.
		/*
		if(clickedBefore == true && Gdx.input.isTouched())
		dragging = Gdx.input.isTouched();
		else
		dragging = false;
		*/
		
		//	If the mouse isn't touching the UI, then do actual stuff in the game.
		if(owner.isMouseTouchingUI() == false)
		doMouseAction();
		
		//	Sets our click status.
		clickedBefore = Gdx.input.isTouched();
	}

	
	public void doMouseAction()
	{
		//	Loops through all objects in the game and checks if our mouse is on it.
		for(int i = 0; i < game.itemObjects.size(); i++)
		{
			if	(	isMouseTouching(
								game.itemObjects.get(i).getX(), 
								game.itemObjects.get(i).getY(), 
								game.itemObjects.get(i).getWidth(),
								game.itemObjects.get(i).getHeight())
				)
			{
			
				//	If we're touching something then we say we are, and what it is.
				curObjectTouched = game.itemObjects.get(i);

				// 	Pick an item up.
				if(owner.onPickupDelay() == false && clickedBefore == false && Gdx.input.isTouched() == true)
				{
					//	Delay to prevent multiple items from being picked up
					//	in one click.
					owner.setPickupDelay(0.1f);
					curObjectTouched.pickup(owner);
					curObjectTouched = null;
				}
				
				// We are touching something. No need to continue the loop.
				return;
			}
		}

		//	We aren't touching an object if we've gotten to this point.
		curObjectTouched = null;
	}
	
	public void updateBackgroundInfo()
	{
		inventoryBackground = owner.getInventoryBackground();
	}
	
	public void setDragging(boolean val)
	{
		dragging = val;
	}
	
	public boolean isDragging()
	{
		return dragging;
	}
	
	public boolean isMouseTouching(float x, float y, float height, float width)
	{
		if(x > this.x && x < this.x + width && y > this.y && y < this.y + height)
		{
			return true;
		}
		return false;	
	}
	
	public boolean isMouseTouchingDroppedItems()
	{
		if(curObjectTouched == null)
		return false;
		return true;
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
