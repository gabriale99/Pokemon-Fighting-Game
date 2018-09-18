package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class DragonClawDef {

	private FrameDef[] left, right;
	private int defLength;
	
	public DragonClawDef(GL2 gl) {
		defLength = 4;
		left = new FrameDef[defLength];
		right = new FrameDef[defLength];
		
		for (int i = 0; i < defLength; i++) {
			left[i] = new FrameDef();
			right[i] = new FrameDef();
			int leftImage = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/Slash/leftDragonClaw" + ( i + 1 ) +".tga", left[i].getImageSize());
			int rightImage = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/Slash/rightDragonClaw" + ( i + 1 ) +".tga", right[i].getImageSize());
			left[i].setImage(leftImage);
			left[i].setFrameTimeSecs(75);
			left[i].setImageSize(left[i].getImageSize()[0] * 2, left[i].getImageSize()[1] * 2);
			right[i].setImage(rightImage);
			right[i].setFrameTimeSecs(75);
			right[i].setImageSize(right[i].getImageSize()[0] * 2, right[i].getImageSize()[1] * 2);
		}
	}

	public int getDefLength() {
		return defLength;
	}
	
	public FrameDef getFrame(int dir, int i) {
		if (dir == 0) {
			return left[i];
		} else {
			return right[i];
		}
	}
}
