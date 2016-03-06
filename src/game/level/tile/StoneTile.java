package game.level.tile;

import Graphics.Misc.Screen;
import Graphics.Misc.Texture;

public class StoneTile extends Tile {

	public StoneTile(Texture texture) {
		super(texture);
	}

	public void render(Screen screen, int x, int y) {
		screen.drawTexture(texture, x, y);
	}
	
	public boolean isSolid() {
		return true;
	}
	
}
