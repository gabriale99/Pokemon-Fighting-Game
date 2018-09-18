package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class PikachuDef extends AnimationDef{

	public PikachuDef(GL2 gl) {
		super(7, 6, 1, 7, 7, 7, 7);
		init(gl);
		initHeart(gl);
		name = "Pikachu";
	}

	private void init(GL2 gl) {
		for (int i = 0; i < getFramesSize(MotionType.IDLE, 0); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/idle/pikachuLeftIdle" + (i + 1) + ".tga",
					getFrame(MotionType.IDLE, 0, 0, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/idle/pikachuRightIdle" + (i + 1) + ".tga",
					getFrame(MotionType.IDLE, 1, 0, i).getImageSize());
			getFrame(MotionType.IDLE, 0, 0, i).setImage(image1);
			getFrame(MotionType.IDLE, 0, 0, i).setFrameTimeSecs(100);
			getFrame(MotionType.IDLE, 1, 0, i).setImage(image2);
			getFrame(MotionType.IDLE, 1, 0, i).setFrameTimeSecs(100);
		}
		
		for (int i = 0; i < getFramesSize(MotionType.MOVING, 0); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/running/pikachuLeftRunning" + (i + 1) + ".tga",
					getFrame(MotionType.MOVING, 0, 0, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/running/pikachuRightRunning" + (i + 1) + ".tga",
					getFrame(MotionType.MOVING, 1, 0, i).getImageSize());
			getFrame(MotionType.MOVING, 0, 0, i).setImage(image1);
			getFrame(MotionType.MOVING, 0, 0, i).setFrameTimeSecs(75);
			getFrame(MotionType.MOVING, 1, 0, i).setImage(image2);
			getFrame(MotionType.MOVING, 1, 0, i).setFrameTimeSecs(75);
		}
		
		int dashLeft = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/dashing/pikachuDashingLeft.tga",
				getFrame(MotionType.DASHING, 0, 0, 0).getImageSize());
		int dashRight = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/dashing/pikachuDashingRight.tga",
				getFrame(MotionType.DASHING, 1, 0, 0).getImageSize());
		getFrame(MotionType.DASHING, 0, 0, 0).setImage(dashLeft);
		getFrame(MotionType.DASHING, 0, 0, 0).setFrameTimeSecs(50);
		getFrame(MotionType.DASHING, 1, 0, 0).setImage(dashRight);
		getFrame(MotionType.DASHING, 1, 0, 0).setFrameTimeSecs(50);
		
		for (int i = 0; i < getFramesSize(MotionType.ATTACKING, 1); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/attacking/pikachuLeftAttack" + (i + 1) + ".tga",
					getFrame(MotionType.ATTACKING, 0, 1, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/attacking/pikachuRightAttack" + (i + 1) + ".tga",
					getFrame(MotionType.ATTACKING, 1, 1, i).getImageSize());
			getFrame(MotionType.ATTACKING, 0, 1, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 1, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 1, 1, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 1, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 0, 2, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 2, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 0, 2, i).setImageSize(
					getFrame(MotionType.ATTACKING, 0, 1, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 0, 1, i).getFrameHeight());
			getFrame(MotionType.ATTACKING, 1, 2, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 2, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 1, 2, i).setImageSize(
					getFrame(MotionType.ATTACKING, 1, 1, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 1, 1, i).getFrameHeight());
			getFrame(MotionType.ATTACKING, 0, 3, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 3, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 0, 3, i).setImageSize(
					getFrame(MotionType.ATTACKING, 0, 1, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 0, 1, i).getFrameHeight());
			getFrame(MotionType.ATTACKING, 1, 3, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 3, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 1, 3, i).setImageSize(
					getFrame(MotionType.ATTACKING, 1, 1, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 1, 1, i).getFrameHeight());
			getFrame(MotionType.ATTACKING, 0, 4, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 4, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 0, 4, i).setImageSize(
					getFrame(MotionType.ATTACKING, 0, 1, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 0, 1, i).getFrameHeight());
			getFrame(MotionType.ATTACKING, 1, 4, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 4, i).setFrameTimeSecs(75);
			getFrame(MotionType.ATTACKING, 1, 4, i).setImageSize(
					getFrame(MotionType.ATTACKING, 1, 1, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 1, 1, i).getFrameHeight());
		}
		
		int pikachuIcon = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/icon/pikachuIcon.tga",
				getIcon().getImageSize());
		getIcon().setImage(pikachuIcon);
		
		int pikachuLeftHurt = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/hurt/pikachuGetHurtLeft.tga",
				getHurtLeftFrame().getImageSize());
		getHurtLeftFrame().setImage(pikachuLeftHurt);
		getHurtLeftFrame().setFrameTimeSecs(1000);
		
		int pikachuRightHurt = FinalProj.glTexImageTGAFile(gl, "sprite/newPikachu/hurt/pikachuGetHurtRight.tga",
				getHurtRightFrame().getImageSize());
		getHurtRightFrame().setImage(pikachuRightHurt);
		getHurtRightFrame().setFrameTimeSecs(1000);
	}

}
