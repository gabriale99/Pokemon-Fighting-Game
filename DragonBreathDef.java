package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class DragonBreathDef extends ProjectileDef {

	public DragonBreathDef(GL2 gl) {
		super(6);
		init(gl);
	}

	private void init(GL2 gl) {
		for (int i = 0; i < 6; i++) {
			FrameDef left = getFrame(0, i);
			FrameDef right = getFrame(1, i);
			int breath = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/DragonBreath/DragonBreath" + (i + 1) + ".tga",
					left.getImageSize());
			left.setImage(breath);
			left.setFrameTimeSecs(50);
			left.setImageSize(left.getFrameWidth() * 3 / 2, left.getFrameHeight() * 3 / 2);
			right.setImage(breath);
			right.setFrameTimeSecs(50);
			right.setImageSize(right.getFrameWidth() * 3 / 2, right.getFrameHeight() * 3 / 2);
		}

	}

}