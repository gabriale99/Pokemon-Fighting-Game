package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class LightningDef {
	private FrameDef left, right;

	public LightningDef(GL2 gl) {
		left = new FrameDef();
		right = new FrameDef();
		int leftImage = FinalProj.glTexImageTGAFile(gl,
				"sprite/AttackAnimation/Thunder/ThunderLeft.tga", left.getImageSize());
		int rightImage = FinalProj.glTexImageTGAFile(gl,
				"sprite/AttackAnimation/Thunder/ThunderRight.tga", right.getImageSize());
		left.setImage(leftImage);
		left.setFrameTimeSecs(300);
		left.setImageSize(left.getFrameWidth() * 2, left.getFrameHeight() * 2);
		right.setImage(rightImage);
		right.setFrameTimeSecs(300);
		right.setImageSize(right.getFrameWidth() * 2, right.getFrameHeight() * 2);
	}

	public FrameDef getFrame(int dir) {
		if (dir == 0) return left;
		else return right;
	}

	public int getDefLength() {
		return 1;
	}
}
