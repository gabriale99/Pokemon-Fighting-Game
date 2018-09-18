package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class ElectricShotDef {

	private FrameDef[] left, right;
	private int defLength;
	
	public ElectricShotDef(GL2 gl) {
		defLength = 6;
		left = new FrameDef[defLength];
		right = new FrameDef[defLength];
		
		for (int i = 0; i < defLength; i++) {
			left[i] = new FrameDef();
			right[i] = new FrameDef();
			int[] size = new int[2];
			int leftImage = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/Electric/electricLeft" + ( i + 1 ) +".tga", size);
			int rightImage = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/Electric/electricRight" + ( i + 1 ) +".tga", size);
			left[i].setImage(leftImage);
			left[i].setFrameTimeSecs(70);
			left[i].setImageSize(size[0], size[1]);
			right[i].setImage(rightImage);
			right[i].setFrameTimeSecs(70);
			right[i].setImageSize(size[0], size[1]);
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
