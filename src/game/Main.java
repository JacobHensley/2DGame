package game;

import Graphics.Window.Window;
import Input.Keyboard;
import Input.Mouse;
import game.entity.Dummy;
import game.entity.Player;
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

	//TODO:	Finish Collision System For ArrowEntity Class (1)
	//TODO: Make Rotating Texture More "res/textures/" (2)
	//TODO: Add Bow Class (3)
	//TODO: Add System For Shooting Arrows From Bow (4)
	//TODO: When Checking if AABB overlaps OBB Convert AABB to OBB (5)
	
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
