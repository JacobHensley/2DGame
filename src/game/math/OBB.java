package game.math;

import Graphics.Misc.Screen;

public class OBB {

	private Vector2f[] corners = new Vector2f[4];
	private Vector2f[] axis = new Vector2f[2];
	private float[] origin = new float[2];

	private float width, height;

	public OBB(Vector2f center, float width, float height, float angle) {
		this.width = width;
		this.height = height;
		set(center, width, height, angle);
	}

	public void set(float x, float y, float angle) {
		set(new Vector2f(x, y), width, height, angle);
	}

	public void set(Vector2f center, float width, float height, float angle) {
		angle = angle * (float) (Math.PI / 180.0f);
		Vector2f x = new Vector2f((float) Math.cos(angle), (float) Math.sin(angle));
		Vector2f y = new Vector2f((float) -Math.sin(angle), (float) Math.cos(angle));

		x = x.multiply(width / 2.0f);
		y = y.multiply(height / 2.0f);

		corners[0] = center.subtract(x).subtract(y);
		corners[1] = center.add(x).subtract(y);
		corners[2] = center.add(x).add(y);
		corners[3] = center.subtract(x).add(y);
		
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

	private boolean overlaps1Way(OBB other) {
		for (int a = 0; a < 2; ++a) {

			float t = other.corners[0].dot(axis[a]);

			// Find the extent of box 2 on axis a
			float tMin = t;
			float tMax = t;

			for (int c = 1; c < 4; ++c) {
				t = other.corners[c].dot(axis[a]);

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

	boolean overlaps1Way(AABB other) {
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
		return overlaps1Way(other) && other.overlaps1Way(this);
	}

	public void render(Screen screen, int color , boolean solid) {
		for (int c = 0; c < 4; c++) {
			screen.drawLine((int) corners[c & 3].x, (int) corners[c & 3].y, (int) corners[(c + 1) & 3].x, (int) corners[(c + 1) & 3].y, color);
		}
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
