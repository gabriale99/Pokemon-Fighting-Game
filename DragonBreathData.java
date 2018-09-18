package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class DragonBreathData extends ProjectileData {

	private DragonBreathDef def;
	
	public DragonBreathData(GL2 gl) {
		super();
		def = new DragonBreathDef(gl);
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
			int temp = (int) (pos[0] - 400 * deltaTimeMS / 1000);
			setX(temp);
		} else {
			int temp = (int) (pos[0] + 400 * deltaTimeMS / 1000);
			setX(temp);
		}
		while (secsUntilNextFrame <= 0) {
			curFrame++;
			if (curFrame == getAnimationLength()) {
				curFrame = 2;
			}
			secsUntilNextFrame += def.getFrame(direction, curFrame).getFrameTimeSecs();
		}
	}
	
	@Override
	public int damageDealt() {
		return 3;
	}
}
