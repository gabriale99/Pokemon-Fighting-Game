package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class SlashDef extends ProjectileDef{

	public SlashDef(GL2 gl) {
		super(1);
		init(gl);
	}

	private void init(GL2 gl) {
		FrameDef left = getFrame(0,0);
		FrameDef right = getFrame(1,0);
		int leftSlash = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/leftSlashAttack.tga", left.getImageSize());
		int rightSlash = FinalProj.glTexImageTGAFile(gl, "sprite/AttackAnimation/rightSlashAttack.tga", right.getImageSize());
		left.setImage(leftSlash);
		left.setFrameTimeSecs(40);
		right.setImage(rightSlash);
		right.setFrameTimeSecs(40);
	}
}
