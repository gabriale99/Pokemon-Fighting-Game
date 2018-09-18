package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class ElectricShotData implements AttackType {
	private int[] pos;
	private int count, direction, curFrame;
	private float secsUntilNextFrame;
	private boolean out;
	private ElectricShotDef electric;
	private AABB hitBox;
	
	public ElectricShotData(GL2 gl) {
		pos = new int[2];
		count = 300;
		direction = 0;
		curFrame = 0;
		out = false;
		electric = new ElectricShotDef(gl);
		secsUntilNextFrame = electric.getFrame(direction, 0).getFrameTimeSecs();
		hitBox = new AABB(0, 0, electric.getFrame(0, 0).getFrameWidth() * 3, electric.getFrame(0, 0).getFrameHeight() * 3);
	}
	
	@Override
	public void update(float deltaTimeMS) {
		if (count > 0) {
			count -= deltaTimeMS;
			return;
		} else out = true;
		if (curFrame == electric.getDefLength()) return;
		secsUntilNextFrame -= deltaTimeMS;
		while (secsUntilNextFrame <= 0) {
			curFrame++;
			if (curFrame == electric.getDefLength()) {
				break;
			}
			if (direction == 0) {
				int prevWidth = electric.getFrame(direction, curFrame - 1).getFrameWidth() * 3;
				int curWidth = electric.getFrame(direction, curFrame).getFrameWidth() * 3;
				int x = pos[0] + prevWidth - curWidth;
				setX(x);
			}
			hitBox.setWidth(electric.getFrame(direction, curFrame).getFrameWidth() * 3);
			hitBox.setHeight(electric.getFrame(direction, curFrame).getFrameHeight() * 3);
			secsUntilNextFrame += electric.getFrame(direction, curFrame).getFrameTimeSecs();
		}
	}

	@Override
	public void setX(int i) {
		pos[0] = i;
		hitBox.setXCoor(i);
	}

	@Override
	public void setY(int i) {
		pos[1] = i;
		hitBox.setYCoor(i);
	}

	@Override
	public void setDirection(int dir) {
		direction = dir;
	}
	
	public int getCurFrame() {
		return curFrame;
	}
	
	public int getAnimationLength() {
		return electric.getDefLength();
	}

	@Override
	public void draw(GL2 gl) {
		if (!AABB.AABBIntersect(hitBox, FinalProj.camera.getBox())) {
			return;
		}
		if (count > 0) return;
		else out= true;
		if (curFrame == electric.getDefLength()) return;
		int screenX = pos[0] - FinalProj.camera.getCameraX();
		int screenY = pos[1] - FinalProj.camera.getCameraY();
		int width = hitBox.getWidth();
		int height = hitBox.getHeight();
		FinalProj.glDrawSprite(gl, getImage(), screenX, screenY, width, height);
	}

	private int getImage() {
		return electric.getFrame(direction, curFrame).getImage();
	}

	@Override
	public AABB getBox() {
		return hitBox;
	}

	@Override
	public boolean needWholeAnimation() {
		return true;
	}

	@Override
	public boolean isOut() {
		return out;
	}

	@Override
	public int damageDealt() {
		return 1;
	}

}
