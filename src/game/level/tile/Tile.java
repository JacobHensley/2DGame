package game.level.tile;

import Graphics.Misc.Screen;
import Graphics.Misc.SpriteSheet;
import Graphics.Misc.Texture;

public class Tile {
	
	protected Texture texture;
	public static final int SIZE = 16;
	private static SpriteSheet tiles = new SpriteSheet("res/textures/Tiles.png", 256);

	public static Tile stone = new StoneTile(new Texture(tiles, 16, 16, 0, 0));
	public static Tile dirt = new DirtTile(new Texture(tiles, 16, 16, 1, 0));
	public static Tile voidTile = new VoidTile(new Texture(tiles, 16, 16, 5, 5));
	
	public Tile(Texture texture) {
		this.texture = texture;
	}
	
	public void render(Screen screen, int x, int y) {
	}
	
	public boolean isSolid() {
		return false;
	}

	public Texture getTexture() {
		return texture;
	}
}
