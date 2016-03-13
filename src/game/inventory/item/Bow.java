package game.inventory.item;

import Graphics.Misc.Texture;
import Input.Mouse;
import game.entity.Entity;
import game.entity.Player;

public class Bow extends Item {
	
	public Bow(String name, int ID, int maxStack, Texture texture) {
		super(name, ID, maxStack, texture);
	}

	public void use(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
		}
	}
	
	public float getAngleToMouse(Entity entity) {
		float mx = Mouse.getMouseX();
		float my = Mouse.getMouseY();
		float ex = entity.getX() - entity.getLevel().getXOffset();
		float ey = entity.getY() - entity.getLevel().getYOffset();
		
		float angle = (float)Math.atan2(my - ey, mx - ex);
		return angle;
	}
	
}
