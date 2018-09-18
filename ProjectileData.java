package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class ProjectileData implements AttackType {

	protected int[] pos;
	protected int direction, count, curFrame;
	protected boolean out;
	protected float secsUntilNextFrame;
	private ProjectileDef def;
	protected AABB projectileBox;

	public ProjectileData() {
		pos = new int[2];
		direction = 0;
		setCount(250);
		curFrame = 0;
		out = false;
		secsUntilNextFrame = 0;
		projectileBox = new AABB();
	}

	public void setProjectileDef(ProjectileDef d) {
		def = d;
		projectileBox.setWidth(def.getFrameWidth(curFrame));
		projectileBox.setHeight(def.getFrameHeight(curFrame));
	}

	public void setX(int i) {
		pos[0] = i;
		projectileBox.setXCoor(i);
	}

	public void setY(int i) {
		pos[1] = i;
		projectileBox.setYCoor(i);
	}

	public void setWidth(int w) {
		projectileBox.setWidth(w);
	}
	
	public int getWidth() {
		return projectileBox.getWidth();
	}
	
	public void setHeight(int h) {
		projectileBox.setHeight(h);
	}

	public int getHeight() {
		return projectileBox.getHeight();
	}

	public void setDirection(int i) {
		direction = i;
	}

	public int getDirection() {
		return direction;
	}
	
	public int getCurFrame() {
		return curFrame;
	}
	
	public boolean isOut() {
		return out;
	}
	
	public int getAnimationLength() {
		return def.getDefLength();
	}

	public AABB getBox() {
		return projectileBox;
	}

	public void update(float deltaTimeMS) {
		if (count > 0) {
			count -= deltaTimeMS;
			return;
		}
		secsUntilNextFrame -= deltaTimeMS;
		if (direction == 0) {
			int temp = (int) (pos[0] - 400 * deltaTimeMS / 1000);
			setX(temp);
		} else {
			int temp = (int) (pos[0] + 400 * deltaTimeMS / 1000);
			setX(temp);
		}
		while (secsUntilNextFrame <= 0) {
			curFrame++;
			if (curFrame == getAnimationLength()) {
				curFrame = 0;
			}
			projectileBox.setWidth(def.getFrameWidth(curFrame));
			projectileBox.setHeight(def.getFrameHeight(curFrame));
			secsUntilNextFrame += def.getFrame(direction, curFrame).getFrameTimeSecs();
		}
	}

	public void draw(GL2 gl) {
		if (!AABB.AABBIntersect(projectileBox, FinalProj.camera.getBox())) return;
		if (getCount() > 0) return;
		else out = true;
		int image = def.getFrame(direction, curFrame).getImage();
		int screenX = pos[0] - FinalProj.camera.getCameraX();
		int screenY = pos[1] - FinalProj.camera.getCameraY();
		FinalProj.glDrawSprite(gl, image, screenX, screenY, projectileBox.getWidth(),
				projectileBox.getHeight());
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public boolean needWholeAnimation() {
		return false;
	}

	@Override
	public int damageDealt() {
		return 1;
	}
}
