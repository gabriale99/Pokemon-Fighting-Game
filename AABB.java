package CS134FinalAssignment;


public class AABB {
	
	private int xCoor, yCoor, width, height;
	
	public AABB() {
		xCoor = yCoor = width = height = 0;
	}
	
	public AABB(int x, int y, int w, int h) {
		xCoor = x;
		yCoor = y;
		width = w;
		height = h;
	}
	
	public void setXCoor(int x) {
		xCoor = x;
	}
	
	public int getXCoor() {
		return xCoor;
	}
	
	public void setYCoor(int y) {
		yCoor = y;
	}
	
	public int getYCoor() {
		return yCoor;
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public int getHeight() {
		return height;
	}
	
	public static boolean AABBIntersect(AABB box1, AABB box2) {
		// box1 to the right
		if (box1.getXCoor() > box2.getXCoor() + box2.getWidth()) {
			return false;
		}
		// box1 to the left
		if (box1.getXCoor() + box1.getWidth() < box2.getXCoor()) {
			return false;
		}
		// box1 below
		if (box1.getYCoor() > box2.getYCoor() + box2.getHeight()) {
			return false;
		}
		// box1 above
		if (box1.getYCoor() + box1.getHeight() < box2.getYCoor()) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return "x: " + xCoor + ", y:" + yCoor + ", width:" + width + ", height:" + height;
	}
}
