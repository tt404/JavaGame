package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class player {
	private float baseSpeed;
	private int baseHealth;
	private int curHealth;
	private SpriteBatch batch;
	private ShapeRenderer playerShape;
	private float x;
	private float y;
	private int radius;
	private double angle;
	private Vector2 angleVector;
	private MyGdxGame game;
	private int ID;
	private boolean displayHealth;
	private ArrayList<inventory> playerInventory = new ArrayList<inventory>();
	private ArrayList<InventorySlot> playerInventorySquares = new ArrayList<InventorySlot>();
	private int selectedWeapon = 0;
	private BitmapFont font;
	private int playerInventoryCap;
	private float inventoryX;
	private float inventoryY;
	private int maxRowSquares = 4;
	private int rowSquares = 0;
	private int colSquares = 0;
	private int inventoryWidth = 32;
	private int inventoryHeight = 32;
	private float pickupDist = 64;
	private float renderDist = 1024;
	private String inventoryMessage = "Inventory";
	private UIbackground inventoryBackground;
	private boolean canPickup = true;
	private Rectangle2D hitbox;
	mouse playerCrosshair;
	inventory emptyspace;
	private Timer healthTimer;		// [Cata] This is a timer to display health above the player during combat.
	private Task healthTimerTask; 	// [Cata] This is the action being done once the timer has finished counting down.

	public player(MyGdxGame myGdxGame, int ID, float baseSpeed, int health, int crosshairType) {

		// *******initializes the player
		this.baseSpeed = baseSpeed;
		this.baseHealth = this.curHealth = health;	// [Cata] base health is ur max health, cur health is ur current health.
		radius = 48;
		x = Gdx.graphics.getWidth() / 2;
		y = Gdx.graphics.getHeight() / 2;
		playerShape = new ShapeRenderer();
		Math.sqrt((baseSpeed * baseSpeed) + (baseSpeed * baseSpeed));
		angleVector = new Vector2();
		this.game = myGdxGame;
		this.ID = ID;
		playerCrosshair = new mouse(myGdxGame, this, crosshairType);
		hitbox = new Rectangle2D.Float();
		healthTimer = new Timer();
		healthTimerTask = new Task(){
		    public void run() {
		        displayHealth = false;
			};
		};

		// *********for various messages.
		font = new BitmapFont();
		batch = new SpriteBatch();

		// *********inventory shit
		inventoryX = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 8);
		inventoryY = Gdx.graphics.getHeight() / 2;
		emptyspace = new inventory(game, this);
		inventoryBackground = new UIbackground(this, inventoryX, inventoryY, 1, 1, Color.GRAY, 0);
		this.setInventorySlots(8);

		// ********Starting items
		inventoryLaserGun laserGun = new inventoryLaserGun(myGdxGame, this);
		this.give(laserGun, 1);
		this.equip(laserGun);
		inventoryRustySword rustySword = new inventoryRustySword(myGdxGame, this);
		this.give(rustySword, 1);
		inventoryMonsterSpawner monsterSpawner = new inventoryMonsterSpawner(myGdxGame, this);
		this.give(monsterSpawner, 1);
	}

	public void render() {

		// ****************player
		playerShape.begin(ShapeType.Filled);
		playerShape.setColor(Color.ORANGE);
		playerShape.rect(x, y, radius, radius);

		if (game.debug == true) {
			playerShape.setColor(Color.GREEN);
			playerShape.rectLine(x + (radius / 2), y + (radius / 2), Gdx.input.getX(), getMouse().getY(), 8);
		}
		
		// [Cata] Squeeze in a healthbar render here...		
		if(displayHealth == true)
		{
			playerShape.setColor(Color.RED);		// [Cata] Whatever we're gonna make is now the color red.
			playerShape.rect(x, y + radius + 8, radius, 6); // [Cata] Places the filled red rectangle. Ie, the background.

			playerShape.setColor(Color.GREEN); 	// [Cata] Sets the current color to green. 
			playerShape.rect(x, y + radius + 8, radius * (((float)curHealth) / ((float)baseHealth)), 6);	// [Cata] creates the green part of the healthbar.
		}

		playerShape.end();

		// ***************Inventory message
		batch.begin();
		font.setColor(Color.ORANGE);
		font.draw(batch, inventoryMessage, inventoryX, inventoryY);
		batch.end();

		// ****************inventory slots rendering
		inventoryBackground.render();
		for (int i = 0; i < playerInventoryCap; i++) {
			playerInventorySquares.get(i).render();
		}
		playerCrosshair.render();
	}

	public void update() {
		playerCrosshair.update();
		angleVector.x = Gdx.input.getX() - (x + (radius / 2));
		angleVector.y = getMouse().getY() - (y + (radius / 2));
		angle = angleVector.angle();

		for (int i = 0; i < playerInventorySquares.size(); i++) {
			playerInventorySquares.get(i).update();
		}

		// System.out.println(angle);
		if (Gdx.input.isKeyPressed(Keys.W)) {
			if (Gdx.input.isKeyPressed(Keys.D)) {
				x += Math.sin(Math.toRadians(45)) * baseSpeed;
				y += Math.cos(Math.toRadians(45)) * baseSpeed;
			} else if (Gdx.input.isKeyPressed(Keys.A)) {
				x += Math.sin(Math.toRadians(315)) * baseSpeed;
				y += Math.cos(Math.toRadians(315)) * baseSpeed;
			} else if (Gdx.input.isKeyPressed(Keys.W)) {
				y += baseSpeed;
			}

			// do nothing otherwise
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			if (Gdx.input.isKeyPressed(Keys.D)) {
				x += Math.sin(Math.toRadians(135)) * baseSpeed;
				y += Math.cos(Math.toRadians(135)) * baseSpeed;
			} else if (Gdx.input.isKeyPressed(Keys.A)) {
				x += Math.sin(Math.toRadians(225)) * baseSpeed;
				y += Math.cos(Math.toRadians(225)) * baseSpeed;
			} else if (Gdx.input.isKeyPressed(Keys.S)) {
				y -= baseSpeed;
			}
		} else if (Gdx.input.isKeyPressed(Keys.A)) {
			x -= baseSpeed;
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			x += baseSpeed;
		}

		if (Gdx.input.isTouched()) {
			playerInventory.get(selectedWeapon).fire();
		}

		for (int i = 0; i < playerInventorySquares.size(); i++) {
			playerInventorySquares.get(i).update();
		}
		
		hitbox.setFrame(x, y, radius, radius);
	}

	public boolean checkinventory(inventory item, int amount) {
		int count = 0;
		for (int i = 0; i < playerInventory.size(); i++) {
			if (playerInventory.get(i).equals(item)) {
				count++;
			}
			if (count == amount) {
				return true;
			}
		}
		return false;
	}

	public void give(inventory item, int amount) {
		int counter = amount;
		if (checkFreeInventorySpace() > counter) {
			for (int i = 0; i < playerInventory.size(); i++) {
				if (playerInventory.get(i).getID() == 0) {
					playerInventory.set(i, item);
					playerInventorySquares.get(i).setItem(item);
					counter--;
					if (counter == 0) {
						return;
					}
				}
			}
		} else {
			System.out.println("You need more space than that.");
			item.drop();
		}
	}

	public void take(inventory item, int amount) {
		int count = 0;
		for (int i = 0; i < playerInventory.size(); i++) {
			if (playerInventory.get(i).equals(item) && playerInventorySquares.get(i).isEquipped() == false) {
				if (game.debug == true)
					System.out.println("Taking " + amount + " " + item.getItemName() + ".");
				playerInventory.set(i, emptyspace);
				playerInventorySquares.get(i).setItem(emptyspace);
				count++;
			}
			if (count == amount) {
				break;
			}
		}
	}

	public void switchInventorySpots(inventory item) {
		int holdnumber;
		holdnumber = playerInventory.indexOf(item);
		for (int i = 0; i < playerInventory.size(); i++) {
			if (
			// playerInventorySquares.get(i).getStartedHere() == true &&
			playerInventorySquares.get(i).isPointTouching(Gdx.input.getX(), getMouse().getY()) == true) {
				// if(game.debug == true)
				if (i != holdnumber && playerInventory.get(holdnumber).ID != 0) {
					System.out.println("Switching, " + item.getItemName() + "(" + holdnumber + ")" + " with "
							+ playerInventory.get(i).getItemName() + "(" + i + ").");

					boolean switchItem = playerInventorySquares.get(i).isEquipped();
					boolean originItem = playerInventorySquares.get(holdnumber).isEquipped();

					unequip(playerInventory.get(holdnumber));
					unequip(playerInventory.get(i));

					playerInventory.set(holdnumber, playerInventory.get(i));
					playerInventorySquares.get(holdnumber).setItem(playerInventory.get(i));

					playerInventory.set(i, item);
					playerInventorySquares.get(i).setItem(item);

					// playerInventorySquares.get(holdnumber).setSuspend(true);

					if (originItem == true)
						playerInventorySquares.get(i).equip();
					if (switchItem == true)
						playerInventorySquares.get(holdnumber).equip();
				}
			}
		}
	}

	public void setInventorySlots(int slots) {
		for (int i = 0; i < playerInventoryCap; i++) {
			if (playerInventory.get(i).getID() != 0) {
				System.out.println("Error, your inventory must be empty before switching backpacks");
				return;
			}
		}
		playerInventoryCap = slots;
		playerInventory.clear();
		playerInventorySquares.clear();
		for (int i = 0; i < playerInventoryCap; i++) {
			playerInventory.add(emptyspace);

			if (rowSquares == maxRowSquares) {
				rowSquares = 0;
				colSquares++;
			}
			InventorySlot blanksquare = new InventorySlot(this, emptyspace,
					inventoryX + (rowSquares * inventoryWidth)
							- ((inventoryMessage.length() * font.getSpaceWidth()) / 2) + (rowSquares * 2),
					inventoryY - (font.getCapHeight() * 2) - inventoryHeight - (colSquares * inventoryHeight)
							- (colSquares * 2),
					inventoryWidth, inventoryHeight, Color.GREEN, emptyspace.getID());
			playerInventorySquares.add(blanksquare);
			rowSquares++;
		}
		colSquares = 0;
		rowSquares = 0;
		inventoryBackground.setX(inventoryX - ((inventoryMessage.length() * font.getSpaceWidth()) / 2));
		inventoryBackground.setY(inventoryY - (font.getCapHeight() * 2) - (inventoryHeight * 2)
				- (2 * (playerInventorySquares.size() / 4)) + 2);
		inventoryBackground.setWidth(maxRowSquares * inventoryWidth + (2 * maxRowSquares)); // 2
																							// *
																							// maxRowSquares
																							// is
																							// there
																							// because
																							// of
																							// the
																							// gap
																							// between
																							// each
																							// squares
		inventoryBackground.setHeight((playerInventorySquares.size() / maxRowSquares) * inventoryHeight
				+ (2 * (playerInventorySquares.size() / 4) - 2)); // see above
	}

	public boolean isMouseTouchingUI() {
		/****************************
		 * Check Inventory Slots
		 ****************************/

		if (playerCrosshair.x > playerInventorySquares.get(0).getX()
				&& playerCrosshair.x < playerInventorySquares.get(maxRowSquares - 1).getX() + inventoryWidth // &&
		// playerCrosshair.y > playerInventorySquares.get(0).getY() &&
		// playerCrosshair.y <
		// playerInventorySquares.get(playerInventorySquares.size() - 1).getY()
		// + inventoryHeight
		)
			return true;

		/*
		 * for(int i = 0; i < playerInventoryCap; i++) {
		 * if(playerInventorySquares.get(i).isPointTouching(Gdx.input.getX(),
		 * -getMouse().getY() + Gdx.graphics.getHeight()) == true) {
		 * System.out.println("Mouse X: " + (float)Gdx.input.getX() +
		 * " Mouse Y: " + (float)(-getMouse().getY() +
		 * Gdx.graphics.getHeight())); System.out.println("Inv X: " +
		 * playerInventorySquares.get(i).getX() + " Inv Y: " +
		 * playerInventorySquares.get(i).getY()); System.out.println(
		 * "Inv Width: " + playerInventorySquares.get(i).getWidth() +
		 * " Inv Height: " + playerInventorySquares.get(i).getHeight()); return
		 * true; } }
		 */
		return false;
	}

	public int getInventorySlots() {
		return playerInventoryCap;
	}

	public void setSelectedWeapon(inventory item) {
		playerInventorySquares.get(selectedWeapon).unequip();
		selectedWeapon = playerInventory.indexOf(item);
	}

	public void equip(inventory item) {
		for (int i = 0; i < playerInventory.size(); i++) {
			if (playerInventory.get(i).equals(item) && playerInventory.get(i).isEquipable() == true) {
				playerInventorySquares.get(i).equip();
			}
		}
	}

	public void unequip(inventory item) {
		for (int i = 0; i < playerInventory.size(); i++) {
			if (playerInventory.get(i).equals(item) && playerInventory.get(i).isEquipable() == true) {
				playerInventorySquares.get(i).unequip();
			}
		}
	}

	public boolean checkEquipped(inventory item) {
		if (playerInventorySquares.get(playerInventory.indexOf(item)).isEquipped() == true) {
			return true;
		}
		return false;
	}

	public int checkFreeInventorySpace() {
		int counter = 0;
		for (int i = 0; i < playerInventoryCap; i++) {
			if (playerInventory.get(i).getID() == 0) {
				counter++;
			}
		}
		return counter;
	}

	public float getCenterX() {
		return x + (radius / 2);
	}

	public float getCenterY() {
		return y + (radius / 2);
	}

	public float getAngle() {
		return (float) angle;
	}

	public Vector2 getVector2() {
		return angleVector;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public int getRadius() {
		return radius;
	}

	public UIbackground getInventoryBackground() {
		return inventoryBackground;
	}

	public mouse getMouse() {
		return playerCrosshair;
	}

	public float getInventoryX() {
		return inventoryX;
	}

	public float getInventoryY() {
		return inventoryY;
	}

	public int getMaxRowSquares() {
		return maxRowSquares;
	}

	public MyGdxGame getGame() {
		return game;
	}

	public boolean canPickup(float x, float y) {
		if (game.distance(getCenterX(), getCenterY(), x, y) <= pickupDist)
			return true;

		return false;
	}

	public boolean canRender(float x, float y) {
		if (game.distance(getCenterX(), getCenterY(), x, y) <= renderDist)
			return true;

		return false;
	}

	public void setPickupDelay(float time) {
		canPickup = false;
		Timer.schedule(new Task() {
			public void run() {
				canPickup = true;
			}

		}, time);
	}

	public boolean onPickupDelay() {
		return !canPickup;
	}
	
	public Rectangle2D getHitbox()
	{
		return hitbox;
	}
	
	// [Cata] A timer to display the bar
	public void combatIndicatorHelper()
	{
		if(displayHealth == false)
		{
			displayHealth = true;
			runHealthTimer();
		}
		else
		{
			healthTimerTask.cancel();
			runHealthTimer();
		}

	}
	
	// [Cata] Taking damage now triggers the overhead healthbar.
	public void damage(int amount, monster who)
	{
		// [Cata] Temporary death prevention. Comment the if-statement below once proper death mechanics
		// have been added.		
		if(curHealth - amount >= 0)
		curHealth -= amount;
		combatIndicatorHelper();
	}
	
	public void runHealthTimer()
	{
		healthTimer.schedule(healthTimerTask, 3.0f);		
	}
}
