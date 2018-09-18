package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class ElectricBallData extends ProjectileData {

	private ElectricBallDef def;
	
	public ElectricBallData(GL2 gl) {
		super();
		def = new ElectricBallDef(gl);
		setProjectileDef(def);
	}
	
	@Override
	public void update(float deltaTimeMS) {
		if (count > 0) {
			count -= deltaTimeMS;
			return;
		} else out = true;
		secsUntilNextFrame -= deltaTimeMS;
		if (direction == 0) {
			int temp = (int) (pos[0] - 600 * deltaTimeMS / 1000);
			setX(temp);
		} else {
			int temp = (int) (pos[0] + 600 * deltaTimeMS / 1000);
			setX(temp);
		}
		int prevHeight = projectileBox.getHeight() / 2;
		while (secsUntilNextFrame <= 0) {
			curFrame++;
			if (curFrame == getAnimationLength()) {
				curFrame = 2;
			}
			projectileBox.setWidth(def.getFrameWidth(curFrame));
			projectileBox.setHeight(def.getFrameHeight(curFrame));
			setY(projectileBox.getYCoor() + prevHeight - projectileBox.getHeight() / 2);
			secsUntilNextFrame += def.getFrame(direction, curFrame).getFrameTimeSecs();
		}
	}
}
