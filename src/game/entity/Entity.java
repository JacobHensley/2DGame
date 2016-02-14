package game.entity;

import java.awt.Rectangle;
import java.util.Random;

import Graphics.Misc.Screen;
import game.level.Level;
import game.level.tile.Tile;

public class Entity {
	
	protected float x, y;
	protected int width, height;
	protected Level level;

	protected int dir = -1;
	protected boolean onGround = false;
	protected boolean collideWithSide = false;
	protected boolean collideWithTop = false;
	
	float velocityX, velocityY, acceleration = 0.3f, friction = 0.2f, gravity = 0.4f;
	float maxHorizontalSpeed = 3.0f, maxVerticalSpeed = 6.0f;
	
	protected static final Random random = new Random();

	public void init(Level level) {
		this.level = level;
	}

	private float[] calculateRemainingSpace(float xa, float ya) {
		float rx = 0, ry = 0;
		float absXa = Math.abs(xa);
		float absYa = Math.abs(ya);
		
		int xOffset = xa < 0.0f ? 0 : width;
		int xs = sign(xa);
		int xm = (int) Math.ceil(absXa);
		float xo = (xm - absXa) * xs;

		for (int i = 1; i <= xm; i++) {
			int y0 = (int)(y / 16.0f);
			boolean solid = false;
			int xCount = 3;
			float a = y - y0 * 16;
			float b = Tile.SIZE * 3 - height;
			if (a > b)
				xCount = 4;
			for (int j = 0; j < xCount; j++) {
				int xt = (int)((x + xOffset + i * xs) / 16.0f);
				int yt = (int)((y) / 16.0f) + j;
				
				Tile tile = level.getTile(xt, yt);
			//	level.highlightedTiles[xt + yt * level.getWidth()] = true;
				solid = tile.isSolid();	

				if (solid) {
					collideWithSide = true;
					break;
				} else 
					collideWithSide = false;
				
			}

			if (solid) {
				if (i == 1)  
					xo = 0;
				break;
			}

			rx += xs;
		}
		
		rx -= xo * xs;
		
		int yOffset = ya < 0.0f ? 0 : height; 
		int ys = sign(ya);
		int ym = (int) Math.ceil(absYa);
		float yo = (ym - absYa) * ys;

		for (int i = 1; i <= ym; i++) {
			int x0 = (int)(x / 16.0f);
			boolean solid = false;
			int yCount = 2;
			float a = x - x0 * 16;
			float b = Tile.SIZE * 2 - width;
			if (a > b)
				yCount = 3;
			for (int j = 0; j < yCount; j++) {
				int xt = (int)((x) / 16.0f) + j;
				int yt = (int)((y + yOffset + i * ys) / 16.0f);
				
				Tile tile = level.getTile(xt, yt);
		//		level.highlightedTiles[xt + yt * level.getWidth()] = true;
				solid = tile.isSolid();
				
				if (solid) {
					if (yOffset > 0)
						onGround = true;
					else
						collideWithTop = true;
					break;
				} else {
					onGround = false;
					collideWithTop = false;
				}
					
			}

			if (solid) {
				if (i == 1)
					yo = 0;
				break;
			}

			ry += ys;
		}

		ry -= yo * ys;
		
		if (rx < 0) 
			rx = (float) Math.ceil(rx);
		
		if (ry < 0) 
			ry = (float) Math.ceil(ry);
		
		float[] values = {rx, ry};
		return values;
	}
	
	private int sign(float value) {
		return value > 0 ? 1 : -1;
	}
	
	protected void move(float xa, float ya) {
		float[] values = calculateRemainingSpace(xa, ya);
		x += values[0];
		y += values[1];
	}

	public void update() {
	}

	public void render(Screen screen) {
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Level getLevel() {
		return level;
	}

	public int getDir() {
		return dir;
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	
}
