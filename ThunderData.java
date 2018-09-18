package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class ThunderData extends ProjectileData {

	private ThunderDef def;
	
	public ThunderData(GL2 gl) {
		super();
		def = new ThunderDef(gl);
		setProjectileDef(def);
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
			int temp = 1500 * (int)deltaTimeMS / 1000;
			if (direction == 0) {
				temp = pos[0] - temp;
				setX(temp);
			} else {
				temp = pos[0] + temp;
				setX(temp);
			}
			if (curFrame == getAnimationLength()) {
				curFrame = 0;
			}
			secsUntilNextFrame += def.getFrame(direction, curFrame).getFrameTimeSecs();
		}
	}
	
	public void draw(GL2 gl) {
		if (!AABB.AABBIntersect(projectileBox, FinalProj.camera.getBox())) {
			return;
		}
		if (getCount() > 0)
			return;
		int image = def.getFrame(direction, curFrame).getImage();
		int screenX = pos[0] - FinalProj.camera.getCameraX();
		int screenY = pos[1] - FinalProj.camera.getCameraY();
		int screenX2 = screenX, screenX3 = screenX2;
		if(direction == 0) {screenX2 += 30; screenX3 = screenX2 + 30;}
		else {screenX2 -= 30; screenX3 = screenX2 - 30;}
		FinalProj.glDrawSprite(gl, image, screenX, screenY, projectileBox.getWidth(),
				projectileBox.getHeight());
		FinalProj.glDrawSprite(gl, image, screenX2, screenY, projectileBox.getWidth(),
				projectileBox.getHeight());
		FinalProj.glDrawSprite(gl, image, screenX3, screenY, projectileBox.getWidth(),
				projectileBox.getHeight());
	}
	
	@Override
	public int damageDealt() {
		return 2;
	}
}
