package game.inventory.item;

import Graphics.Misc.Texture;
import game.entity.Entity;

public class Axe extends Item {

	public Axe(String name, int ID, int maxStack, Texture texture) {
		super(name, ID, maxStack, texture);
	}
	
	public void use(Entity entity) {
	}

}
