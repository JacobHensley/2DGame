package game.inventory.item;

import Graphics.Misc.Texture;
import game.entity.Entity;

public class Pickaxe extends Item {

	public Pickaxe(String name, int ID, int maxStack, Texture texture) {
		super(name, ID, maxStack, texture);
	}
	
	public void use(Entity entity) {
	}

}
