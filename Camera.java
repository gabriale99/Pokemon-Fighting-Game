package CS134FinalAssignment;

public class Camera {
	
	final static int SCREEN_MOVE_FPS = CharacterControl.MAIN_MOVE_FPS;
	
	private int cameraX,  cameraY, width, height;
	private BackgroundDef bgDef;
	private AABB cameraBox;
	
	public Camera(int x, int y, BackgroundDef bg) {
		cameraX = x;
		cameraY = y;
		bgDef = bg;
		width = FinalProj.WINDOW_WIDTH;
		height = FinalProj.WINDOW_HEIGHT;
		cameraBox = new AABB(cameraX, cameraY, width, height);
	}
	
	public void setCameraX(int x) {
		cameraX = x;
		cameraBox.setXCoor(x);
	}
	
	public void setCameraY(int y) {
		cameraY = y;
		cameraBox.setYCoor(y);
	}
	
	public void reset() {
		cameraX = 0;
		cameraY = 0;
	}
	
	public int getCameraX() {
		return cameraX;
	}
	
	public int getCameraY() {
		return cameraY;
	}
	
	public int getCameraWidth() {
		return width;
	}
	
	public int getCameraHeight() {
		return height;
	}
	
	public AABB getBox() {
		return cameraBox;
	}
	
	public void resetCamera() {
		setCameraX(0);
		setCameraY(0);
	}
	
	private void cameraToLeft(long deltaTime) {
		int newX = cameraX - (int) (SCREEN_MOVE_FPS * deltaTime / 1000);
		setCameraX(newX);
	}

	private void cameraToRight(long deltaTime) {
		int newX = getCameraX() + (int) (SCREEN_MOVE_FPS * deltaTime / 1000);
		setCameraX(newX);
	}

	private void cameraUpward(long deltaTime) {
		int newY = getCameraY() - (int) (SCREEN_MOVE_FPS * deltaTime / 1000);
		setCameraY(newY);

	}

	private void cameraDownward(long deltaTime) {
		int newY = getCameraY() + (int) (SCREEN_MOVE_FPS * deltaTime / 1000);
		setCameraY(newY);
	}
	
	public void intelligentCamera1(long deltaTimeMS, AnimationData data) {
		boolean check1 = data.getPositionX() - cameraX < 300;
		boolean check2 = data.getPositionX() - cameraX > width - 300 - data.getBox().getWidth();
		boolean check3 = data.getPositionY() - cameraY < 300;
		boolean check4 = data.getPositionY() - cameraY > height - 300 - data.getBox().getHeight();

		if (check1) {
			cameraToLeft(deltaTimeMS);
		}
		if (check2) {
			cameraToRight(deltaTimeMS);
		}
		if (check3) {
			cameraUpward(deltaTimeMS);
		}
		if (check4) {
			cameraDownward(deltaTimeMS);
		}
		checkOutOfBound();
	}
	
	public void intelligentCamera2(long deltaTimeMS, AnimationData data1, AnimationData data2) {
		int x = (data1.getPositionX() + data2.getPositionX()) / 2;
		int y = (data1.getPositionY() + data2.getPositionY()) / 2;
		boolean check1 = x - cameraX < width / 2;
		boolean check2 = x - cameraX > width / 2;
		boolean check3 = y - cameraY < height / 2;
		boolean check4 = y - cameraY > height / 2;
		boolean xNeedFix = false, yNeedFix = false;
		if (check1) {
			cameraToLeft(deltaTimeMS);
			if (x != cameraX + width / 2) {
				xNeedFix = true;
			}
		}
		if (check2) {
			cameraToRight(deltaTimeMS);
			if (x != cameraX + width / 2) {
				xNeedFix = true;
			}
		}
		if (check3) {
			cameraUpward(deltaTimeMS);
			if (y != cameraY + height / 2) {
				yNeedFix = true;
			}
		}
		if (check4) {
			cameraDownward(deltaTimeMS);
			if (y != cameraY + height / 2) {
				yNeedFix = true;
			}
		}

		if (xNeedFix) {
			int settle = x - width / 2;
			setCameraX(settle);
		}

		if (yNeedFix) {
			int settle = y - height / 2;
			setCameraY(settle);
		}
		checkOutOfBound();
	}
	
	public void checkOutOfBound() {
		if (cameraX < 0) {
			cameraX = 0;
		}
		
		if (cameraX + FinalProj.WINDOW_WIDTH > bgDef.getWidth() * bgDef.getBGWidth()) {
			cameraX = bgDef.getWidth() * bgDef.getBGWidth() - FinalProj.WINDOW_WIDTH;
		}
		
		if (cameraY < 0) {
			cameraY = 0;
		}
		
		if (cameraY + FinalProj.WINDOW_HEIGHT > bgDef.getHeight() * bgDef.getBGHeight()) {
			cameraY = bgDef.getHeight() * bgDef.getBGHeight() - FinalProj.WINDOW_HEIGHT;
		}
	}
}
