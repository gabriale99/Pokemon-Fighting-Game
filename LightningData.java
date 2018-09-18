package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class LightningData implements AttackType{
	private int[] pos;
	private int count, direction, curFrame;
	private float secsUntilNextFrame;
	private boolean out;
	private LightningDef thunder;
	private AABB hitBox;
	
	public LightningData(GL2 gl) {
		pos = new int[2];
		count = 250;
		direction = 0;
		curFrame = 0;
		out = false;
		thunder = new LightningDef(gl);
		secsUntilNextFrame = thunder.getFrame(direction).getFrameTimeSecs();
		hitBox = new AABB(0, 0, thunder.getFrame(0).getFrameWidth(), thunder.getFrame(0).getFrameHeight());
	}
	
	@Override
	public void update(float deltaTimeMS) {
		if (count > 0) {
			count -= deltaTimeMS;
			return;
		} else out = true;
		secsUntilNextFrame -= deltaTimeMS;
		while (secsUntilNextFrame <= 0) {
			curFrame++;
			if (curFrame >= getAnimationLength()) {
				break;
			}
			secsUntilNextFrame += thunder.getFrame(direction).getFrameTimeSecs();
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
		hitBox.setYCoor(i + hitBox.getHeight() * 3 / 4);
	}

	@Override
	public void setDirection(int dir) {
		direction = dir;
	}
	
	public int getCurFrame() {
		return curFrame;
	}
	
	public int getAnimationLength() {
		return thunder.getDefLength();
	}

	@Override
	public void draw(GL2 gl) {
		if (!AABB.AABBIntersect(hitBox, FinalProj.camera.getBox())) return;
		if (count > 0) return;
		else out = true;
		int screenX = pos[0] - FinalProj.camera.getCameraX();
		int screenY = pos[1] - FinalProj.camera.getCameraY();
		int width = hitBox.getWidth();
		int height = hitBox.getHeight();
		FinalProj.glDrawSprite(gl, getImage(), screenX, screenY, width, height);
	}

	private int getImage() {
		return thunder.getFrame(direction).getImage();
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
