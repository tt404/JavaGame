package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class InventorySlot {
	private player owner;
	private inventory item;
	private float x;
	private float y;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private int id;
	private int width = 32;
	private int height = 32;
	private Color boxColor;
	private Texture inventoryImage;
	private boolean startedHere;
	private boolean endedHere;
	private boolean equipped = false;
	private boolean isDragging = false;
	private boolean suspendActions = false;
	private boolean lastMouseHoverUpdate;
	private boolean lastMouseClickUpdate;
	private BitmapFont itemNameFont;
	public InventorySlot(player owner, inventory item, float x, float y, int width, int height, Color color, int id)
	{
		this.owner = owner;
		this.item = item;
		shapeRenderer = new ShapeRenderer();
		this.id = id;
		boxColor = color;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		batch = new SpriteBatch();
		this.setImage(item);
		itemNameFont = new BitmapFont();
	}
	
	public void render()
	{
		batch.begin();
		if(isDragging == true)
		{
			if(this.isPointTouching(Gdx.input.getX(), owner.getMouse().getY()) == false)
			batch.draw(inventoryImage, Gdx.input.getX() - (width/2), owner.getMouse().getY() - (height / 2));	
		}
		else if(isDragging == false)
		{
			if(this.isPointTouching(Gdx.input.getX(), owner.getMouse().getY()) == true)
			itemNameFont.draw
			(
					batch, 
					item.getItemName(), 
					owner.getInventoryX() - width + (itemNameFont.getSpaceWidth() / 2), 
					owner.getInventoryY() - (((owner.getInventorySlots() / owner.getMaxRowSquares()) + 1) * (height))
			);
		}
		batch.draw(inventoryImage, x, y);
		batch.end();
		shapeRenderer.begin(ShapeType.Line);
		if(this.isPointTouching(Gdx.input.getX(), owner.getMouse().getY()) == false)
		shapeRenderer.setColor(boxColor);
		if(this.isPointTouching(Gdx.input.getX(), owner.getMouse().getY()) == true && equipped == false)
		shapeRenderer.setColor(Color.ORANGE);
		else if(equipped == true)
		shapeRenderer.setColor(Color.RED);			
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}
	
	public void setItem (inventory item)
	{
		this.item = item;
		this.setImage(item);
	}
	
	public void setImage (inventory item)
	{
		this.inventoryImage = new Texture(item.getInventorySprite());
	}
	
	public void setColor(Color color)
	{
		boxColor = color;
	}
	
	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void update()
	{
		if(this.isPointTouching(Gdx.input.getX(), owner.getMouse().getY()) == true)
		{
			if(lastMouseHoverUpdate == false && Gdx.input.isTouched() == true) //Meaning, the mouse wasn't previously hovering, but is now, AND has button1 mashed down
			{
				endedHere = false;
			}
			if(lastMouseHoverUpdate == true && Gdx.input.isTouched() == true && lastMouseClickUpdate == false) //Meaning the mouseclick originated here and not elsewhere
			{
//				System.out.println("started here");
				startedHere = true;
				isDragging = true;
				
				owner.getMouse().setDragging(true);
			}
			if(lastMouseHoverUpdate == true && Gdx.input.isTouched() == false && lastMouseClickUpdate == true) //You stopped holding button1 on this tile
			{
				endedHere = true;
				isDragging = false;
/*				
				if(startedHere == false)
				{
					//System.out.println("shiet");
					owner.switchInventorySpots(item);
				}
*/				
				if(item.isEquipable() == true && suspendActions == false && startedHere == true)
				{
					if(this.isEquipped() == false)
					{
						this.equip();
					}
					else
					{
						this.unequip();
					}
				}/*
				else
				{
					suspendActions = false;
				}*/
			}
		}

		if(Gdx.input.isTouched() == false && lastMouseClickUpdate == false && startedHere == true)
		{
			isDragging = false;
			endedHere = false;
			
			if(equipped == false				&&
			   item.ID != 0						&&
			   owner.isMouseTouchingUI() == false
			   )
			{
				System.out.println("Dropping...");
				item.drop();
				owner.take(item, 1);
			}
			else
			{
				owner.switchInventorySpots(item);
			}
			startedHere = false;
//			System.out.println("no longer started here");
			
		}
		
		lastMouseHoverUpdate = this.isPointTouching(Gdx.input.getX(), owner.getMouse().getY());
		lastMouseClickUpdate = Gdx.input.isTouched();
	}
	
	public boolean checkIfDragging()
	{
		return isDragging;
	}
	
	public boolean getStartedHere()
	{
		//System.out.println(startedHere);
		return startedHere;
	}

	public void equip()
	{
		if(item.isEquipable() == true)// && suspendActions == false)
		{
			
			if(item.isWeapon() == true)
			{
				owner.setSelectedWeapon(item);
			}
			boxColor = Color.RED;
			equipped = true;
		}
	}
	
	public void unequip()
	{
		//if(suspendActions == false)
		//{
			equipped = false;
			boxColor = Color.GREEN;
		//}
	}
	
	public boolean isEquipped()
	{
		return equipped;
	}
	
	public boolean isPointTouching(float x, float y)
	{
		if((x > this.x && x < this.x + width) && (y > this.y && y < this.y + height))
		{
			return true;
		}
		return false;
	}
	
	public void setSuspend(boolean val)
	{
		suspendActions = val;
	}
}
