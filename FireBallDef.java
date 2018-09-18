package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class FireBallDef extends ProjectileDef {

	public FireBallDef(GL2 gl) {
		super(4);
		init(gl);
	}

	private void init(GL2 gl) {
		for (int i = 0; i < 4; i++) {
			FrameDef left = getFrame(0, i);
			FrameDef right = getFrame(1, i);
			int leftBall = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/fireball/fireBall" + (i + 1) + ".tga",
					left.getImageSize());
			int rightBall = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/fireball/fireBall" + (i + 1) + ".tga",
					right.getImageSize());
			left.setImage(leftBall);
			left.setFrameTimeSecs(50);
			left.setImageSize(left.getFrameWidth() * 3 / 2, left.getFrameHeight() * 3 / 2);
			right.setImage(rightBall);
			right.setFrameTimeSecs(50);
			right.setImageSize(right.getFrameWidth() * 3 / 2, right.getFrameHeight() * (i + 1) * 3 / 2);
		}

	}

}
