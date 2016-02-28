package game;

import Graphics.Misc.Texture;
import Graphics.Window.Window;
import Input.Keyboard;
import Input.Mouse;
import game.entity.ArrowEntity;
import game.entity.Dummy;
import game.entity.Player;
import game.inventory.item.Item;
import game.level.Level;

public class Main {

	private Window window;
	private Level level;
	private Player player;
	private Keyboard key = new Keyboard();
	private Mouse mouse = new Mouse();
	public static final int WIDTH = 960;
	public static final int HEIGHT = 540;
	
	private Dummy dummy;
	
	ArrowEntity arrow = new ArrowEntity(new Item("Arrow", 5, 3, new Texture("res/Textures/Arrow.png")), new Texture("res/Textures/ArrowEntity.png"), 90.0f, 1.0f, 1.0f, 100, 100);
	
	//TODO:	Add Collision System For ArrowEntity Class (1)
	//TODO: Add Bow Class (2)
	//TODO: Add System For Shooting Arrows From Bow (3)
	
	//TODO: drawLine() Method in Screen Work - Consult Cherno
	//TODO: Drop Item and Pickup Item Systems Work - Consult Cherno
	//TODO: OBB and AABB Works - Consult Cherno
	
	public Main() {
		window = new Window("2DGame", WIDTH, HEIGHT);
		level = new Level();
		level.genLevelFromFile("res/levels/Level.png");
		player = new Player(100.0f, 100.0f);
		dummy = new Dummy(100.0f, 100.0f);
		
		window.addKeyListener(key);
		window.addMouseListener(mouse);
		window.addMouseMotionListener(mouse);

		level.addEntity(player);
		level.addEntity(dummy);
		level.addEntity(arrow);
		window.showWindow();
	}

	public static void main(String args[]) {
		Main main = new Main();
		
		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int updates = 0;
	    double delta = 0.0;
		long timer = System.currentTimeMillis();

	    while(!main.window.closed()) {
	    	long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if(delta >= 1.0) {
            	main.level.update();
                updates++;
                delta--;
            }

            main.window.clear(0xFF000224);
        	main.level.render(main.window.getScreen());
            main.window.update();
            frames++;
            if(System.currentTimeMillis() - timer >= 1000L) {
                timer += 1000;
                System.out.println("FPS: " + frames + ", UPS: " + updates);
                frames = 0;
                updates = 0;
            }
	    }
	}
}
