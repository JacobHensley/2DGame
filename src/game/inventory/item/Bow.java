package game.inventory.item;

import Graphics.Misc.Texture;
import game.entity.Entity;

public class Bow extends Item {
	
	public Bow(String name, int ID, int maxStack, Texture texture) {
		super(name, ID, maxStack, texture);
	}

	public void use(Entity entity) {
	}
	
	public float getAngleToMouse(Entity entity) {
		return 0;
	}
	
}
