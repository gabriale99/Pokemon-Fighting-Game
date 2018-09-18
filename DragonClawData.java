package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class DragonClawData implements AttackType{
	private int[] pos;
	private int count, direction, curFrame;
	private float secsUntilNextFrame;
	private boolean out;
	private DragonClawDef dragonClaw;
	private AABB hitBox;
	
	public DragonClawData(GL2 gl) {
		pos = new int[2];
		count = 250;
		direction = 0;
		curFrame = 0;
		out = false;
		dragonClaw = new DragonClawDef(gl);
		secsUntilNextFrame = dragonClaw.getFrame(direction, 0).getFrameTimeSecs();
		hitBox = new AABB(0, 0, dragonClaw.getFrame(0, 0).getFrameWidth(), dragonClaw.getFrame(0, 0).getFrameHeight());
	}
	
	@Override
	public void update(float deltaTimeMS) {
		if (count > 0) {
			count -= deltaTimeMS;
			return;
		} else out = true;
		if (curFrame == dragonClaw.getDefLength()) return;
		secsUntilNextFrame -= deltaTimeMS;
		int prevHeight = hitBox.getHeight() / 2;
		while (secsUntilNextFrame <= 0) {
			curFrame++;
			if (curFrame == dragonClaw.getDefLength()) {
				break;
			}
			if (direction == 0) {
				int prevWidth = dragonClaw.getFrame(direction, curFrame - 1).getFrameWidth();
				int curWidth = dragonClaw.getFrame(direction, curFrame).getFrameWidth();
				int x = pos[0] + prevWidth - curWidth;
				setX(x);
			}
			hitBox.setWidth(dragonClaw.getFrame(direction, curFrame).getFrameWidth());
			hitBox.setHeight(dragonClaw.getFrame(direction, curFrame).getFrameHeight());
			setY(hitBox.getYCoor() + prevHeight - hitBox.getHeight() / 2);
			secsUntilNextFrame += dragonClaw.getFrame(direction, curFrame).getFrameTimeSecs();
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
		return dragonClaw.getDefLength();
	}

	@Override
	public void draw(GL2 gl) {
		if (!AABB.AABBIntersect(hitBox, FinalProj.camera.getBox())) {
			return;
		}
		if (count > 0)
			return;
		if (curFrame == dragonClaw.getDefLength()) {
			 return;
		}
		int screenX = pos[0] - FinalProj.camera.getCameraX();
		int screenY = pos[1] - FinalProj.camera.getCameraY();
		int width = hitBox.getWidth();
		int height = hitBox.getHeight();
		FinalProj.glDrawSprite(gl, getImage(), screenX, screenY, width, height);
	}

	private int getImage() {
		return dragonClaw.getFrame(direction, curFrame).getImage();
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
		return 2;
	}
}
