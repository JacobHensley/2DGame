package game.entity;

import java.awt.Color;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;
import game.inventory.item.Item;
import game.math.OBB;
import game.math.Vector2f;

public class ArrowEntity extends ItemEntity {

	private float angle;
	private Texture texture;
	
	public ArrowEntity(Item item, float x, float y) {
		super(item, x, y);
	}
	
	public ArrowEntity(Item item, Texture texture, float angle, float velocityX, float velocityY, float x, float y) {
		super(item, x, y);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.angle = angle;
		this.texture = texture;
		width = texture.getWidth();
		height = texture.getHeight();
	}
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		screen.drawTexture(texture, (int)x - level.getXOffset(), (int)y - level.getYOffset());
		
		getBoundBox().debugRender(screen, level, Color.red, false);
	}
	
	public OBB getBoundBox() {
		return new OBB(new Vector2f(x + width / 2, y + height / 2), width, height, angle);
	}
}
