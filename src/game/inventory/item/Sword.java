package game.inventory.item;

import java.awt.Rectangle;
import java.util.List;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;
import game.entity.Entity;
import game.math.AABB;

public class Sword extends Item {

	private int reach = 26;
	
	public Sword(String name, int ID, int maxStack, Texture texture) {
		super(name, ID, maxStack, texture);
	}

	//TODO:Finish Use Method
	public void use(Entity entity) {
		List<Entity> foundEntities = entity.getLevel().findEntities(getBounds());
		for (int i = 0;i < foundEntities.size();i++) {
			if (foundEntities.get(i) != entity) 
				System.out.println(foundEntities.get(i));
		}
	}

	public void debugRender(Screen screen, Entity entity) {
		Rectangle r = getBounds().getRect();
		r.x -= entity.getLevel().getXOffset();
		r.y -= entity.getLevel().getYOffset();
		screen.drawRect(r, 0, 0x00FF00);
	}
	
	public AABB getBounds() {
		int dir = entity.getDir();
		if (dir == -1) {
			return new AABB((int)entity.getX() - reach, (int)entity.getY(), reach + entity.getWidth(), entity.getHeight());
		} else {
			return new AABB((int)entity.getX(), (int)entity.getY(), entity.getWidth() + reach, entity.getHeight());
		}
		
	}
	
}
