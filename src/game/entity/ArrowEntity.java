package game.entity;

import java.awt.Color;
import java.awt.Font;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;
import game.inventory.item.Item;
import game.level.tile.Tile;
import game.math.OBB;
import game.math.Vector2f;

public class ArrowEntity extends ItemEntity {

	private float angle;
	private Texture texture = item.getTexture();
	private OBB obb;
	
	public Font debugFont = new Font("Verdana", 0, 20);
	
	public ArrowEntity(Item item, float x, float y) {
		super(item, x, y);
		obb = new OBB(new Vector2f(x + width / 2, y + height / 2), width, height, angle);
	}
	
	public ArrowEntity(Item item, Texture texture, float x, float y, float velocityX, float velocityY, float angle) {
		super(item, 1, x, y, velocityX, velocityY);
		this.angle = angle;
		this.texture = Texture.rotate(texture, Math.toRadians(angle));
		
		acceleration = 0.1f;
		friction = 0.1f;
		gravity = 0.2f;
		maxHorizontalSpeed = 3.0f;
		maxVerticalSpeed = 6.0f;
		
		width = 32;
		height = 14;
		obb = new OBB(new Vector2f(x + width / 2, y + height / 2), width, height, angle);
	}
	
	public void update() {
		float xa = 0.0f, ya = 0.0f; 
		
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

		texture = Texture.rotate(texture, -1);
		
	    xa += velocityX;
	    ya += velocityY;
		move(xa, ya);
	}
	
	public void move(float xa, float ya) { 
		float rx = xa;
		float ry = ya;
		
		float absXa = Math.abs(xa);
		float absYa = Math.abs(ya);
		
		for (int c = 0;c < 4;c++) { 
			Vector2f corrner =  obb.getCorners()[c];
			for (int xx = 0;xx < absXa;xx++) {
				int dx = xx * sign(xa);
				
				Tile tile = level.getTileFromEntityPos(corrner.x + dx, corrner.y);
				if (tile.isSolid() && Math.abs(dx) < Math.abs(rx))
					rx = dx; 
			}
		}
		for (int c = 0;c < 4;c++) { 
			Vector2f corrner =  obb.getCorners()[c];
			for (int yy = 0;yy < absYa;yy++) {
				int dy = yy * sign(ya);
		
				Tile tile = level.getTileFromEntityPos(corrner.x + dy, corrner.y);
				if (tile.isSolid() && Math.abs(dy) < Math.abs(ry))
					ry = dy; 
			}
		}
		
		x += rx;
		y += ry;
		
		obb = new OBB(new Vector2f(x + texture.getWidth() / 2, y + texture.getHeight() / 2), width, height, angle);
	}
	
	public void render(Screen screen) {
		screen.drawTexture(texture, (int)x - level.getXOffset(), (int)y - level.getYOffset());
		for (int i = 0;i < 4;i++) {
			screen.drawRect((int)obb.getCorners()[i].x - level.getXOffset(), (int)obb.getCorners()[i].y - level.getYOffset(), 5, 5, 0, 0x0000FF);
		}
		
		screen.drawString("Angle: " + angle, (int)x - 10 - level.getXOffset(), (int)y - 10 - level.getYOffset(), 0, debugFont, Color.white);
		obb.debugRender(screen, level, Color.red, false);
	}
	
	public OBB getOBB() {
		return obb;
	}
	
	public float getAngle() {
		return angle;
	}

}
