package game.entity;

import java.awt.Rectangle;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;

public class Dummy extends Mob {
	
	private Texture texture = new Texture("res/Textures/Player.png");
	private int value;
	
	public Dummy(float x, float y) {
		this.x = x;
		this.y = y;
		
		width = texture.getWidth() - 1;
		height = texture.getHeight() - 1;
	}
	
	public void update() {
		float xa = 0.0f, ya = 0.0f; 
		
		if (!onGround)
			acceleration = 0.25f;
		else {
			acceleration = 0.3f;
			velocityY = gravity;
		}
		
		if (random.nextInt(40) == 0) {
			value = random.nextInt(3);
		}
		
		if (value == 0)
			moveRight();
		else if (value == 1)
			moveLeft();
		
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
		int xo = (int) x - level.getXOffset();
		int yo = (int) y - level.getYOffset();
		
		screen.drawTexture(dir == 1 ? texture.flip() : texture,(int) xo,(int) yo);
	}
	
	private void moveLeft() {
		if (dir != 2) 
			velocityX = 0;
	
		velocityX -= acceleration;

		dir = 2;
	}
	
	private void moveRight() {
		if (dir != 1) 
			velocityX = 0;
		
		velocityX += acceleration;
		
		dir = 1;
	}
}
