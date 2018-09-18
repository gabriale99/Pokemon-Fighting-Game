package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class ThunderDef extends ProjectileDef {

	public ThunderDef(GL2 gl) {
		super(1);
		init(gl);
	}

	private void init(GL2 gl) {
		FrameDef left = getFrame(0, 0);
		FrameDef right = getFrame(1, 0);
		int leftImage = FinalProj.glTexImageTGAFile(gl,
				"sprite/AttackAnimation/Thunder/ThunderLeft.tga", left.getImageSize());
		int rightImage = FinalProj.glTexImageTGAFile(gl,
				"sprite/AttackAnimation/Thunder/ThunderRight.tga", right.getImageSize());
		left.setImage(leftImage);
		left.setFrameTimeSecs(100);
		left.setImageSize(left.getFrameWidth() * 2, left.getFrameHeight() * 2);
		right.setImage(rightImage);
		right.setFrameTimeSecs(100);
		right.setImageSize(right.getFrameWidth() * 2, right.getFrameHeight() * 2);
	}

}
