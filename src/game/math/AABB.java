package game.math;

import java.awt.Rectangle;

import Graphics.Misc.Screen;

public class AABB {

	private Vector2f[] corners = new Vector2f[4];
	private float x, y, width, height;
	
	private Vector2f[] axis = new Vector2f[2];
	private float[] origin = new float[2];
	
	public AABB(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		corners[0] = new Vector2f(x, y);
		corners[1] = new Vector2f(x + width, y);
		corners[2] = new Vector2f(x + width, y + height);
		corners[3] = new Vector2f(x, y + height);
		computeAxes();
	}
	
	private void computeAxes() {
		axis[0] = corners[1].subtract(corners[0]);
		axis[1] = corners[3].subtract(corners[0]);

		// Make the length of each axis 1/edge length so we know any
		// dot product must be less than 1 to fall within the edge.
		for (int a = 0; a < 2; ++a) {
			axis[a] = axis[a].divide(axis[a].squaredLength());
			origin[a] = corners[0].dot(axis[a]);
		}
	}
	
	boolean overlaps1Way(OBB other) {
		for (int a = 0; a < 2; ++a) {

			float t = other.getCorners()[0].dot(axis[a]);

			// Find the extent of box 2 on axis a
			float tMin = t;
			float tMax = t;

			for (int c = 1; c < 4; ++c) {
				t = other.getCorners()[c].dot(axis[a]);

				if (t < tMin) {
					tMin = t;
				} else if (t > tMax) {
					tMax = t;
				}
			}

			// We have to subtract off the origin

			// See if [tMin, tMax] intersects [0, 1]
			if ((tMin > 1 + origin[a]) || (tMax < origin[a])) {
				// There was no intersection along this dimension;
				// the boxes cannot possibly overlap.
				return false;
			}
		}

		// There was no dimension along which there is no intersection.
		// Therefore the boxes overlap.
		return true;
	}
	
	public boolean overlaps(OBB other) {
		return overlaps1Way(other) && other.overlaps1Way(this);
	}
	
	public boolean overlaps(AABB other) {
	    if (corners[2].x < other.getCorners()[0].x) return false; // a is left of b
	    if (corners[0].x > other.getCorners()[2].x) return false; // a is right of b
	    if (corners[2].y < other.getCorners()[0].y) return false; // a is above b
	    if (corners[0].y > other.getCorners()[2].y) return false; // a is below b
	//    System.out.println("OVERLAP");
	    return true; // boxes overlap
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
		
		corners[0] = new Vector2f(x, y);
		corners[1] = new Vector2f(x + width, y);
		corners[2] = new Vector2f(x + width, y + height);
		corners[3] = new Vector2f(x, y + height);
		computeAxes();
	}
	
	public void render(Screen screen, int color, boolean solid) {
		screen.drawRect((int)x, (int)y, (int)width, (int)height, 0, color);
	}
	
	public Rectangle getRect() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	
	public Vector2f[] getCorners() {
		return corners;
	}

	public Vector2f[] getAxis() {
		return axis;
	}

	public float[] getOrigin() {
		return origin;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
}
