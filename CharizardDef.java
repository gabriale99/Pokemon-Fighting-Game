package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class CharizardDef extends AnimationDef {
	
	public CharizardDef(GL2 gl) {
		super(7, 4, 4, 7, 7, 16, 16);
		init(gl);
		initHeart(gl);
		name = "Charizard";
	}

	private void init(GL2 gl) {
		for (int i = 0; i < getFramesSize(MotionType.IDLE, 0); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl, "sprite/Charizard/idle/CharizardLeftIdle" + (i + 1) + ".tga",
					getFrame(MotionType.IDLE, 0, 0, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl, "sprite/Charizard/idle/CharizardRightIdle" + (i + 1) + ".tga",
					getFrame(MotionType.IDLE, 1, 0, i).getImageSize());
			getFrame(MotionType.IDLE, 0, 0, i).setImage(image1);
			getFrame(MotionType.IDLE, 0, 0, i).setFrameTimeSecs(100);
			getFrame(MotionType.IDLE, 1, 0, i).setImage(image2);
			getFrame(MotionType.IDLE, 1, 0, i).setFrameTimeSecs(100);
		}

		for (int i = 0; i < getFramesSize(MotionType.MOVING, 0); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl,
					"sprite/Charizard/flying/CharizardLeftFlying" + (i + 1) + ".tga",
					getFrame(MotionType.MOVING, 0, 0, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl,
					"sprite/Charizard/flying/CharizardRightFlying" + (i + 1) + ".tga",
					getFrame(MotionType.MOVING, 1, 0, i).getImageSize());
			getFrame(MotionType.MOVING, 0, 0, i).setImage(image1);
			getFrame(MotionType.MOVING, 0, 0, i).setFrameTimeSecs(100);
			getFrame(MotionType.MOVING, 1, 0, i).setImage(image2);
			getFrame(MotionType.MOVING, 1, 0, i).setFrameTimeSecs(100);
			getFrame(MotionType.DASHING, 0, 0, i).setImage(image1);
			getFrame(MotionType.DASHING, 0, 0, i).setFrameTimeSecs(50);
			getFrame(MotionType.DASHING, 0, 0, i).setImageSize(getFrame(MotionType.MOVING, 0, 0, i).getFrameWidth(),
					getFrame(MotionType.MOVING, 0, 0, i).getFrameHeight());
			getFrame(MotionType.DASHING, 1, 0, i).setImage(image2);
			getFrame(MotionType.DASHING, 1, 0, i).setFrameTimeSecs(50);
			getFrame(MotionType.DASHING, 1, 0, i).setImageSize(getFrame(MotionType.MOVING, 1, 0, i).getFrameWidth(),
					getFrame(MotionType.MOVING, 1, 0, i).getFrameHeight());
		}

		for (int i = 0; i < getFramesSize(MotionType.ATTACKING, 1); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl,
					"sprite/Charizard/attacking/animation1/CharizardLeftAttackMotion" + (i + 1) + ".tga",
					getFrame(MotionType.ATTACKING, 0, 1, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl,
					"sprite/Charizard/attacking/animation1/CharizardRightAttackMotion" + (i + 1) + ".tga",
					getFrame(MotionType.ATTACKING, 1, 1, i).getImageSize());
			getFrame(MotionType.ATTACKING, 0, 1, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 1, i).setFrameTimeSecs(50);
			getFrame(MotionType.ATTACKING, 1, 1, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 1, i).setFrameTimeSecs(50);
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
		}

		for (int i = 0; i < getFramesSize(MotionType.ATTACKING, 3); i++) {
			int image1 = FinalProj.glTexImageTGAFile(gl,
					"sprite/Charizard/attacking/animation2/CharizardLeftAttackMotion" + (i + 1) + ".tga",
					getFrame(MotionType.ATTACKING, 0, 3, i).getImageSize());
			int image2 = FinalProj.glTexImageTGAFile(gl,
					"sprite/Charizard/attacking/animation2/CharizardRightAttackMotion" + (i + 1) + ".tga",
					getFrame(MotionType.ATTACKING, 1, 3, i).getImageSize());
			getFrame(MotionType.ATTACKING, 0, 3, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 3, i).setFrameTimeSecs(50);
			getFrame(MotionType.ATTACKING, 1, 3, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 3, i).setFrameTimeSecs(50);
			getFrame(MotionType.ATTACKING, 0, 4, i).setImage(image1);
			getFrame(MotionType.ATTACKING, 0, 4, i).setFrameTimeSecs(50);
			getFrame(MotionType.ATTACKING, 0, 4, i).setImageSize(
					getFrame(MotionType.ATTACKING, 0, 3, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 0, 3, i).getFrameHeight());
			getFrame(MotionType.ATTACKING, 1, 4, i).setImage(image2);
			getFrame(MotionType.ATTACKING, 1, 4, i).setFrameTimeSecs(50);
			getFrame(MotionType.ATTACKING, 1, 4, i).setImageSize(
					getFrame(MotionType.ATTACKING, 1, 3, i).getFrameWidth(),
					getFrame(MotionType.ATTACKING, 1, 3, i).getFrameHeight());
		}

		int charizardIcon = FinalProj.glTexImageTGAFile(gl, "sprite/Charizard/icon/CharizardIcon.tga",
				getIcon().getImageSize());
		getIcon().setImage(charizardIcon);

		int charizardLeftHurt = FinalProj.glTexImageTGAFile(gl, "sprite/Charizard/hurt/CharizardLeftHurt.tga",
				getHurtLeftFrame().getImageSize());
		getHurtLeftFrame().setImage(charizardLeftHurt);
		getHurtLeftFrame().setFrameTimeSecs(1000);

		int charizardRightHurt = FinalProj.glTexImageTGAFile(gl, "sprite/Charizard/hurt/CharizardRightHurt.tga",
				getHurtRightFrame().getImageSize());
		getHurtRightFrame().setImage(charizardRightHurt);
		getHurtRightFrame().setFrameTimeSecs(1000);
	}
}
