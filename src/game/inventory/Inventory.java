package game.inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import Graphics.Misc.Screen;
import Input.Keyboard;
import Input.Mouse;
import game.entity.ItemEntity;
import game.inventory.item.Item;
import game.level.Level;

public class Inventory {
	
	private Slot[] slots;
	private int width, height;
	boolean visible = false;
	
	private Font numberFont = new Font("Helvetica", Font.PLAIN, 12);
	private int hoverSlot = -1;
	private int selectedSlot = 0;
	
	private int xOffset = 20;
	private int	yOffset = 20;
	
	private int slotSize = 44;
	
	public Inventory(int width, int height) {
		this.width = width;
		this.height = height;
		slots = new Slot[width * height];
		init();
	}
	
	public class Hand {
		Item item;
		int amount = 0;
		
		public void setItem(Item item, int amount) {
			this.item = item;
			this.amount = amount;
		}
		
		public void decrementItem() {
			if (item == null)
				return;
			
			if (--amount == 0)
				item = null;
		}
		
		public int incrementItem(int amount) {
			int total = this.amount + amount - item.getMaxStack();
			if (total > 0)
				amount = item.getMaxStack();
			else
				this.amount += amount;
			return total;
		}
	}
	
	private Hand hand = new Hand();
	
	private void init() {
		for (int y = 0;y < height;y++) {
			for (int x = 0;x < width;x++) {
				slots[x + y * width] = new Slot();
			}
		}
	}
	
	public void update() {
		float xc = (Mouse.getMouseX() - xOffset) / (float)slotSize;
		float yc = (Mouse.getMouseY() - yOffset) / (float)slotSize;
	
		hoverSlot = (int)xc + (int)yc * width;
		if (xc < 0 || xc >= width || yc < 0 || yc >= height)
			hoverSlot = -1;
		
		for (int i = KeyEvent.VK_1;i <= KeyEvent.VK_9;i++) {
			if (Keyboard.isKeyTyped(i))
				setSelectedSlot(i - KeyEvent.VK_0 - 1);
		}
		
		if (Keyboard.isKeyTyped(KeyEvent.VK_0))
			setSelectedSlot(9);
			
		if (visible && Mouse.mouseClicked(1) && hoverSlot != -1) {
			handleLeftClick();
		}
		
		if (visible && Mouse.mouseClicked(3) && hoverSlot != -1) {
			handleRightClick();
		}
	}
	
	private void handleLeftClick() {
		Slot slot = slots[hoverSlot];	
		
		if (hand.item == null) {
			//Grab Item
			hand.setItem(slot.item, slot.currentStack);
			slot.setItem(null, 0);
		} else if (slot.item != null) {
			//Stack Item
			if (stackItemFromHand(slot))
				return;
			//Swap Item
			swapItemWithHand(slot);
		} else {
			//Place Item	
			slot.setItem(hand.item, hand.amount);
			hand.setItem(null, 0);
		}
	}
	
	private void handleRightClick() {		
		Slot slot = slots[hoverSlot];

		if (slot.item != null && hand.item == null || hand.item != null && slot.item != null && hand.item.getID() == slot.item.getID()) {
			hand.item = slot.item;
			if (slot.currentStack > 0 && hand.amount < hand.item.getMaxStack()) {
				hand.incrementItem(1);
				slot.decrementItem();
			}
		}
	}
	
	private void swapItemWithHand(Slot slot) {
		Item tempItem = slot.item;
		int tempAmount = slot.currentStack;
		slot.setItem(hand.item, hand.amount);
		hand.setItem(tempItem, tempAmount);
	}
	
	private boolean stackItemFromHand(Slot slot) {
		if (slot.item.getID() == hand.item.getID()) {
			int leftOver = slot.incrementItem(hand.amount);
			if (leftOver > 0)
				hand.setItem(hand.item, leftOver);
			else
				hand.setItem(null, 0);
			return true;
		}
		return false;
	}
	
	
	public void render(Screen screen, int x, int y) {
		for (int i = 0;i < width;i++) {
			Slot slot = slots[i];
			slot.render(screen, x + i * slotSize, y);

			if (i == hoverSlot && visible && hand.item == null)
				slot.renderDescription(screen, Mouse.getMouseX(), Mouse.getMouseY());
			if (slot.item != null || visible)
				screen.drawString("" + (i + 1) % 10, x + i * slotSize + 5, y + 14, 0, numberFont, Color.white);
		}
		
		if (visible) {
			for (int yy = 1;yy < height;yy++) {
				int yOffset = yy * slotSize + y;
				for (int xx = 0;xx < width;xx++) {
					int xOffset = xx * slotSize + x;
					int index = xx + yy * width; 
					slots[index].render(screen, xOffset, yOffset);
					if (index == hoverSlot && hand.item == null)
						slots[index].renderDescription(screen, Mouse.getMouseX(), Mouse.getMouseY());
				}
			}
		}
		
		if (hand.item != null) {
			screen.drawTexture(hand.item.getTexture(), Mouse.getMouseX(), Mouse.getMouseY());
			if (hand.amount > 1)
				screen.drawString("" + hand.amount, Mouse.getMouseX() + 5, Mouse.getMouseY() + 35, 0, numberFont, Color.white);
		}		
	}

	public boolean giveItem(Item item, int amount) {
		return addExistingItem(item, amount) ? true : AddAItemToEmpty(item, amount);
	}
	
	public void setItem(Item item, int amount, int index) {
		if (index < 0 || index > slots.length)
			return;
		slots[index].setItem(item, amount);
	}
	
	private boolean AddAItemToEmpty(Item item, int amount) {
		int index = getItemSlot(null);
		if (index == -1)
			return false;
		slots[index].setItem(item, amount);
		return true;
	}
	
	public boolean addExistingItem(Item item, int amount) {
		int index = getItemSlot(item);
		if (index == -1)
			return false;
		
		slots[index].currentStack += amount;
		return true;
	}
	
	private int getItemSlot(Item item) {
		for (int i = 0;i < slots.length;i++) {
			if (slots[i].item == item) 
				return i;
		}
		return -1;
	}
	
	public void drop(Slot slot, Level level, float x, float y, float velocityX, float velocityY) {
		if (slot.item != null) {
			ItemEntity item = new ItemEntity(slot.item, x, y, velocityX, velocityY);
			level.addEntity(item);
			slot.item = null;
			slot.currentStack = 0;
		}
	}

	private void setSelectedSlot(int index) {
		for (int i = 0;i < 10;i++) {
			slots[i].textureIndex = 0;
		}
		slots[index].textureIndex = 1;
		selectedSlot = index;
	}
	
	public Slot getSelectedSlot() {
		return slots[selectedSlot];
	}
	
	public Item getSelectedItem() {
		int index = selectedSlot;
		if (index < 0)
			return null;
		return slots[index].item;
	}
	
	public void toggle() {
		visible = !visible;
	} 
	 
	public boolean getVisible() {
		return visible;
	}
}
