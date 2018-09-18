package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class FireBallData extends ProjectileData {

	private FireBallDef def;
	
	public FireBallData(GL2 gl) {
		super();
		def = new FireBallDef(gl);
		setProjectileDef(def);
		setCount(400);
	}
	
	@Override
	public void update(float deltaTimeMS) {
		if (count > 0) {
			count -= deltaTimeMS;
			return;
		} else out = true;
		secsUntilNextFrame -= deltaTimeMS;
		if (direction == 0) {
			int temp = (int) (pos[0] - 500 * deltaTimeMS / 1000);
			setX(temp);
		} else {
			int temp = (int) (pos[0] + 500 * deltaTimeMS / 1000);
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
		return 2;
	}
}
