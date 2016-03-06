package game.math;

public class Vector2f {

	public float x, y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	//Add by Float;
	//Subtract by Float;
	
	public Vector2f add(Vector2f other) {
		float x = this.x + other.x;
		float y = this.y + other.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f subtract(Vector2f other) {
		float x = this.x - other.x;
		float y = this.y - other.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f multiply(Vector2f other) {
		x *= other.x;
		y *= other.y;
		return this;
	}
	
	public Vector2f multiply(float other) {
		float x = this.x * other;
		float y = this.y * other;
		return new Vector2f(x, y);
	}

	public Vector2f divide(Vector2f other) {
		float x = this.x / other.x;
		float y = this.y / other.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f divide(float other) {
		float x = this.x / other;
		float y = this.y / other;
		return new Vector2f(x, y);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float squaredLength() {
		float length = length();
		return length * length;
	}
	
	public float dot(Vector2f other) {
		return x * other.x + y * other.y;
	}
}
