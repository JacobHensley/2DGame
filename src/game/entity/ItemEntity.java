package game.entity;

import java.awt.Rectangle;

import Graphics.Misc.Screen;
import game.inventory.item.Item;

public class ItemEntity extends Entity {
	
	protected Item item;
	protected int amount = 0;

	public ItemEntity(Item item, float x, float y) {
		this.item = item;
		this.x = x;
		this.y = y;
	
		init();
	}
	
	public ItemEntity(Item item, int amount, float x, float y, float velocityX, float velocityY) {
		this.item = item;
		this.x = x;
		this.y = y;
		 
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.amount = amount;
		 
		init();
	}
	
	public void init() {
		acceleration = 0.1f;
		friction = 0.05f;
		gravity = 0.1f;
		maxHorizontalSpeed = 3.0f;
		maxVerticalSpeed = 6+.0f;
		 
		width = item.getTexture().getWidth() - 1;
		height = item.getTexture().getHeight() - 1;
	}
	 
	public void update() {
		float xa = 0.0f, ya = 0.0f; 
		
		if (!onGround)
			acceleration = 0.25f;
		else {
			acceleration = 0.3f;
			velocityY = gravity;
		}
		
		if (collideWithTop)
			velocityY = 0;
		
		velocityY += gravity;
		
		if (velocityX > 0) {
			velocityX -= friction;
			if (velocityX < 0) 
				velocityX = 0;		
		} else if (velocityX < 0) {
			velocityX += friction;
			if (velocityX > 0) 
				velocityX = 0;
		}
		
		if (velocityX > maxHorizontalSpeed) 
			velocityX = maxHorizontalSpeed;
		else if (velocityX < -maxHorizontalSpeed) 
			velocityX = -maxHorizontalSpeed;
		
		if (velocityY > maxVerticalSpeed) 
			velocityY = maxVerticalSpeed;
		else if (velocityY < -maxVerticalSpeed) 
			velocityY = -maxVerticalSpeed;
		
		xa += velocityX;
		ya += velocityY;
		move(xa, ya);
	}
	
	public void render(Screen screen) {
		screen.drawTexture(item.getTexture(), (int)x - level.getXOffset(), (int)y - level.getYOffset());
		
		//Debug
		Rectangle r = getBounds().getRect();
		r.x -= level.getXOffset();
		r.y -= level.getYOffset();
		screen.drawRect(r, 0, 0x00FF00);
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getAmount() {
		return amount;
	}
	
}
