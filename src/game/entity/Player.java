package game.entity;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;
import Input.Keyboard;
import Input.Mouse;
import game.Main;
import game.inventory.Inventory;
import game.inventory.item.Bow;
import game.inventory.item.Item;
import game.inventory.item.Sword;
import game.level.tile.Tile;

public class Player extends Mob {
	
	private Texture texture = new Texture("res/Textures/Player.png");
	
	private int jumpTimer, maxJumpTimer = 15;
	boolean canJump = false;
	
	private Inventory inv = new Inventory(10, 5);
	
	//Debug
	public Font debugFont = new Font("Verdana", 0, 20);
	Item itemSword = new Sword("Fiery Greatsword", 1, 1, new Texture("res/Textures/Sword.png"));
	Item itemHook = new Item("Hook Shot", 2, 1, new Texture("res/Textures/Hook.png"));
	Item itemPickaxe = new Item("Fiery Pickaxe", 3, 1, new Texture("res/Textures/Pickaxe.png"));
	Item itemArrow = new Item("Arrow", 4, 60, new Texture("res/Textures/Arrow.png"));
	Bow itemBow = new Bow("Wooden Bow", 5, 1, new Texture("res/Textures/Bow.png"));

	private int pickupTimer;
	
	public Player(float x, float y) {
		this.x = x;
		this.y = y;

		//Debug
		inv.giveItem(itemSword, 1);
		inv.giveItem(itemPickaxe, 1);
		inv.giveItem(itemBow, 1);
		inv.giveItem(itemArrow, 58);
		
		inv.giveItem(itemArrow, 1);

		itemSword.init(this);
		itemPickaxe.init(this);
		itemHook.init(this);
		itemBow.init(this);
		itemArrow.init(this);
		
		width = texture.getWidth() - 1;
		height = texture.getHeight() - 1;
	}
	
	public void pickupItem() {
		List<ItemEntity> itemEntities = overLapingItems();
		
		if (itemEntities.size() > 0 && pickupTimer < 60)
			pickupTimer += itemEntities.size();
		else if (pickupTimer > 0)
			pickupTimer -= 1;
		
		for (int i = 0;i < itemEntities.size();i++) {
			if (pickupTimer >= 60) {
				ItemEntity item = itemEntities.get(i);
				TakeItemFromLevel(item);
				itemEntities.remove(i);
				if (itemEntities.size() >= 1)
					pickupTimer = 20;
				else
					pickupTimer = 0;
			}
		}		
	}
	
	public List<ItemEntity> overLapingItems() {
		List<ItemEntity> result = new ArrayList<ItemEntity>();
		for (int i = 0;i < level.getEntities().size();i++) {
			Entity e = level.getEntities().get(i);
			if (e instanceof ItemEntity && e.getBounds().overlaps(getBounds())) {
				ItemEntity item = (ItemEntity) e;
				result.add(item);
			}
		}
		return result;
	}
	
	public void TakeItemFromLevel(ItemEntity item) {
		inv.giveItem(item.getItem(), item.getAmount());
		level.getEntities().remove(item);
	}
	
	public void update() {
		move();
		inv.update();

		Item item = inv.getSelectedItem();
		if (Mouse.mouseClicked(1)) {
			if (item != null)
				item.use(this);
		}	
	
		if (Mouse.getMouseButton() == 3 && inv.getHoverSlot() == -1 && inv.getHand().item != null)
			inv.getHand().drop(inv.getHand().amount, level, x, y, 2 * dir, -1);
		
		pickupItem();
		
		int xo = (int) (x + width / 2) - Main.WIDTH / 2;
		int yo = (int) (y + height / 2) - Main.HEIGHT / 2;
		
		if (xo < 0)
			xo = 0;
		if (yo < 0)
			yo = 0;
		
		int maxXO = level.getWidth() * Tile.SIZE - Main.WIDTH;
		if (xo > maxXO)
			xo = maxXO;
		
		int maxYO = level.getHeight() * Tile.SIZE - Main.HEIGHT;
		if (yo > maxYO)
			yo = maxYO;
		
		level.setOffset(xo, yo);
	}
	
	public void render(Screen screen) {
		int xo = (int) x - level.getXOffset();
		int yo = (int) y - level.getYOffset();
		
		//Debug
		Item item = inv.getSelectedItem();
		if (item != null && item instanceof Sword)  {
			Sword new_name = (Sword) item;
			new_name.debugRender(screen, this);
		}
		
		screen.drawTexture(dir == 1 ? texture.flip() : texture,(int) xo,(int) yo);

		Item selcectedItem = inv.getSelectedItem();
		if (selcectedItem != null)
			screen.drawTexture(dir == -1 ? selcectedItem.getTexture().flip() : selcectedItem.getTexture(), xo + (dir == -1 ? -20 : 10), yo - 6);
		
		inv.render(screen, 20, 20);
	}
	
	public void move() {
		float xa = 0.0f, ya = 0.0f; 
		
		if (!onGround)
			acceleration = 0.25f;
		else {
			acceleration = 0.3f;
			velocityY = gravity;
		}
		
		if (Keyboard.isKeyPressed(KeyEvent.VK_A) || Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
			if (dir != -1) 
				velocityX = 0;
			velocityX -= acceleration;
			dir = -1;
		}  else	if (Keyboard.isKeyPressed(KeyEvent.VK_D) || Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
			if (dir != 1) 
				velocityX = 0;
			velocityX += acceleration;
			dir = 1;
		}
		
		if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
			if (jumpTimer > 0)
				velocityY = -4.7f;
			canJump	= false;
		} else {
			jumpTimer = 0;
			canJump = true;
		}
		
		//Debug
		if (Keyboard.isKeyTyped(KeyEvent.VK_F)) {
			ArrowEntity arrow = new ArrowEntity(itemArrow, new Texture("res/textures/ArrowEntity_2.png"), x, y, 10 * dir, -5f, (float) Math.toDegrees(itemBow.getAngleToMouse(this)));
			level.addEntity(arrow);
		}
		
		if (Keyboard.isKeyTyped(KeyEvent.VK_ESCAPE))
			inv.toggle();
			
		if (collideWithTop)
			velocityY = 0;
		
		if (onGround && canJump)
			jumpTimer = maxJumpTimer;
		if (jumpTimer > 0)
			jumpTimer--;
		
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
		
		if (velocityX > maxHorizontalSpeed) 
			velocityX = maxHorizontalSpeed;
		else if (velocityX < -maxHorizontalSpeed) 
			velocityX = -maxHorizontalSpeed;
		
		if (velocityY > maxVerticalSpeed) 
			velocityY = maxVerticalSpeed;
		else if (velocityY < -maxVerticalSpeed) 
			velocityY = -maxVerticalSpeed;
		
		xa += velocityX;
		ya += velocityY;
		move(xa, ya);
	}
	
	public Inventory getInv() {
		return inv;
	}
}
