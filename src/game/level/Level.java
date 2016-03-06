package game.level;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import Graphics.Misc.Screen;
import game.Main;
import game.entity.Entity;
import game.entity.Player;
import game.level.tile.Tile;
import game.level.tile.VoidTile;
import game.math.AABB;

public class Level {
	
	private List<Entity> entities = new ArrayList<Entity>();
	private Player player;
	private int[] levelPixels;
	private Tile[] tiles;
	private int width, height;

	private int xOffset, yOffset;
	public boolean[] highlightedTiles;
	
	public Level() {
	}
	
	public void update() {
		for (int i = 0; i < highlightedTiles.length; i++)
			highlightedTiles[i] = false;
		
		for(int i = 0;i < entities.size();i++) {
			entities.get(i).update();
		}
	}
	
	public void render(Screen screen) {
		int x0 = xOffset / Tile.SIZE;
		int y0 = yOffset / Tile.SIZE;
		
		int x1 = (xOffset + Main.WIDTH) / Tile.SIZE + 1;
		int y1 = (yOffset + Main.HEIGHT) / Tile.SIZE + 1;
		
		for (int y = y0;y < y1;y++) {
			for (int x = x0;x < x1;x++) {
				if (x < 0 || x >= this.width || y < 0 || y >= this.height)
					continue;
				
				if (getTile(x, y) instanceof VoidTile) {
					if (highlightedTiles[x + y * width])
					screen.fillRect(x * Tile.SIZE - xOffset, y * Tile.SIZE - yOffset, Tile.SIZE, Tile.SIZE, 0xffffff);
					continue;
				}
				
				if (highlightedTiles[x + y * width])
					screen.fillRect(x * Tile.SIZE - xOffset, y * Tile.SIZE - yOffset, Tile.SIZE, Tile.SIZE, 0xffffff);
				else
					getTile(x, y).render(screen, x * 16 - xOffset, y * 16 - yOffset);
			}
		}
		
		for(int i = 0;i < entities.size();i++) {
			entities.get(i).render(screen);
		}	
		
	}
	
	public void genLevelFromFile(String path) {
		BufferedImage image;
		try {
			image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			
			levelPixels = new int[width * height];
			tiles = new Tile[width * height];
			highlightedTiles = new boolean[width * height];
			image.getRGB(0, 0, width, height, levelPixels, 0, image.getWidth());
			
			for (int i = 0;i < levelPixels.length;i++) {
				if (levelPixels[i] == 0xff808080) 
					tiles[i] = Tile.stone;
				if (levelPixels[i] == 0xff9E6940) 
					tiles[i] = Tile.dirt;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addEntity(Entity entity) {
		entity.init(this);
		if (entity instanceof Player) 
			player = (Player) entity;
		entities.add(entity);
	}
	
	public List<Entity> findEntities(AABB r) {
		List<Entity> result = new ArrayList<Entity>();
		for (int i = 0;i < entities.size();i++) {
			if (entities.get(i).getBounds().overlaps(r))
				result.add(entities.get(i));
		}
		return result;
	}
	
	public Tile getTile(int x, int y) {
		if (x + y * width >= tiles.length || x + y * width < 0) 
			return Tile.voidTile;
		Tile tile = tiles[x + y * width];
		if (tile != null) 
			return tile;
		return Tile.voidTile;
	}
	
	public Tile getTileFromEntityPos(float x, float y) {
		return getTile((int)(x / 16.0f), (int)(y / 16.0f));
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Player getPlayer() {
		return player;
	}
	
}