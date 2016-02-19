package game.inventory;

import java.awt.Color;
import java.awt.Font;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;
import game.inventory.item.Item;

public class Slot {

	public Item item;
	public int currentStack = 0;
	
	private Texture[] textures = new Texture[] {new Texture("res/textures/Slot_Mod.png"), new Texture("res/textures/SelectedSlot_Mod.png")};
	
	private Font numberFont = new Font("Helvetica", Font.PLAIN, 12);
	private Font descFont = new Font("Helvetica", Font.BOLD, 18);
	
	public int textureIndex = 0;
	
	public Slot() {
	}
	
	public void update() {
	}
	
	public void render(Screen screen, int x, int y) {
		screen.drawTexture(textures[textureIndex], x, y);
		
		if (item == null)
			return;
		screen.drawTexture(item.getTexture(), x, y);
		if (currentStack > 1)
			screen.drawString("" + currentStack, x + 7, y + 34, 0, numberFont, Color.white);
	}
	
	public void renderDescription(Screen screen, int x, int y) {
		if (item == null)
			return;
		if (currentStack > 1) 
			screen.drawString(item.getName() + " (" + currentStack + ")", x + 15, y + 30, 0, descFont, Color.white);
		else
			screen.drawString(item.getName(), x + 15, y + 30, 0, descFont, Color.white);
	}
	
	public void setItem(Item item, int amount) {
		this.item = item;
		currentStack = amount;
	}

	public void decrementItem() {
		if (item == null)
			return;
		
		if (--currentStack == 0)
			item = null;
	}
	
	public void decrementItem(int amount) {
		if (item == null)
			return;
		
		if (currentStack - amount <= 0)
			item = null;
		else
			currentStack -= amount;
	}
	
	public int incrementItem(int amount) {
		int total = currentStack + amount - item.getMaxStack();
		if (total > 0)
			currentStack = item.getMaxStack();
		else
			currentStack += amount;
		return total;
	}
	
}
