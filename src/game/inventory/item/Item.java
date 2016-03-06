package game.inventory.item;

import Graphics.Misc.Texture;
import game.entity.Entity;

public class Item {
	
	protected String name;
	protected int ID;
	protected int maxStack;
	protected Texture texture;
	
	protected Entity entity;
	
	public Item(String name, int ID, int maxStack, Texture texture) {
		this.name = name;
		this.ID = ID;
		this.maxStack = maxStack;
		this.texture = texture;
	}
	
	public void init(Entity entity) {
		this.entity = entity;
	}
	
	public void use(Entity Entity) {
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public Texture getTexture() {
		return texture;
	}
}
