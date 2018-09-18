package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class AnimationDef {
	private FrameDef[] idleLeft, idleRight, movingLeft, movingRight, dashingLeft, dashingRight;
	private FrameDef[] attackLeft1, attackLeft2, attackLeft3, attackLeft4;
	private FrameDef[] attackRight1, attackRight2, attackRight3, attackRight4;
	private FrameDef icon, hurtLeftFrame, hurtRightFrame;
	private int fullHeart, halfHeart;
	protected String name = "";

	public AnimationDef(int idle, int moving, int dashing, int attack1, int attack2, int attack3, int attack4) {
		idleLeft = new FrameDef[idle];
		idleRight = new FrameDef[idle];
		for (int i = 0; i < idle; i++) {
			idleLeft[i] = new FrameDef();
			idleRight[i] = new FrameDef();
		}

		movingLeft = new FrameDef[moving];
		movingRight = new FrameDef[moving];
		for (int i = 0; i < moving; i++) {
			movingLeft[i] = new FrameDef();
			movingRight[i] = new FrameDef();
		}

		dashingLeft = new FrameDef[dashing];
		dashingRight = new FrameDef[dashing];
		for (int i = 0; i < dashing; i++) {
			dashingLeft[i] = new FrameDef();
			dashingRight[i] = new FrameDef();
		}

		attackLeft1 = new FrameDef[attack1];
		attackRight1 = new FrameDef[attack1];
		for (int i = 0; i < attack1; i++) {
			attackLeft1[i] = new FrameDef();
			attackRight1[i] = new FrameDef();
		}

		attackLeft2 = new FrameDef[attack2];
		attackRight2 = new FrameDef[attack2];
		for (int i = 0; i < attack2; i++) {
			attackLeft2[i] = new FrameDef();
			attackRight2[i] = new FrameDef();
		}

		attackLeft3 = new FrameDef[attack3];
		attackRight3 = new FrameDef[attack3];
		for (int i = 0; i < attack3; i++) {
			attackLeft3[i] = new FrameDef();
			attackRight3[i] = new FrameDef();
		}

		attackLeft4 = new FrameDef[attack4];
		attackRight4 = new FrameDef[attack4];
		for (int i = 0; i < attack4; i++) {
			attackLeft4[i] = new FrameDef();
			attackRight4[i] = new FrameDef();
		}

		icon = new FrameDef();
		hurtLeftFrame = new FrameDef();
		hurtRightFrame = new FrameDef();
	}
	
	protected void initHeart(GL2 gl) {
		fullHeart = FinalProj.glTexImageTGAFile(gl, "sprite/hud/fullHealth.tga", new int[2]);
		halfHeart = FinalProj.glTexImageTGAFile(gl, "sprite/hud/halfHealth.tga", new int[2]);
	}
	
	public int getFullHeart() {
		return fullHeart;
	}
	
	public int getHalfHeart() {
		return halfHeart;
	}

	public FrameDef getHurtLeftFrame() {
		return hurtLeftFrame;
	}

	public void setHurtLeftFrame(FrameDef hurtLeftFrame) {
		this.hurtLeftFrame = hurtLeftFrame;
	}

	public FrameDef getHurtRightFrame() {
		return hurtRightFrame;
	}

	public void setHurtRightFrame(FrameDef hurtRightFrame) {
		this.hurtRightFrame = hurtRightFrame;
	}

	public FrameDef getIcon() {
		return icon;
	}

	public void setIcon(FrameDef icon) {
		this.icon = icon;
	}

	public FrameDef getFrame(MotionType state, int direction, int skill, int index) {
		if (state == MotionType.IDLE) {
			if (direction == 0) {
				return idleLeft[index];
			} else {
				return idleRight[index];
			}
		} else if (state == MotionType.MOVING) {
			if (direction == 0) {
				return movingLeft[index];
			} else
				return movingRight[index];
		} else if (state == MotionType.DASHING) {
			if (direction == 0) {
				return dashingLeft[index];
			} else {
				return dashingRight[index];
			}
		} else if (state == MotionType.ATTACKING) {
			if (direction == 0) {
				if (skill == 1) {
					return attackLeft1[index];
				} else if (skill == 2) {
					return attackLeft2[index];
				} else if (skill == 3) {
					return attackLeft3[index];
				} else {
					return attackLeft4[index];
				}
			} else {
				if (skill == 1) {
					return attackRight1[index];
				} else if (skill == 2) {
					return attackRight2[index];
				} else if (skill == 3) {
					return attackRight3[index];
				} else {
					return attackRight4[index];
				}
			}
		} else {
			if (direction == 0) {
				return hurtLeftFrame;
			} else {
				return hurtRightFrame;
			}
		}
	}

	public int getFramesSize(MotionType state, int skillNum) {
		if (state == MotionType.IDLE) {
			return idleLeft.length;
		} else if (state == MotionType.MOVING) {
			return movingLeft.length;
		} else if (state == MotionType.DASHING) {
			return dashingLeft.length;
		} else {
			if (skillNum == 1) {
				return attackLeft1.length;
			} else if (skillNum == 2) {
				return attackLeft2.length;
			} else if (skillNum == 3) {
				return attackLeft3.length;
			} else {
				return attackLeft4.length;
			}
		}
	}

	// Currently all image has same size
	public int getSingleFrameWidth(MotionType state, int direction, int skill, int index) {
		return getFrame(state, direction, skill, index).getFrameWidth();
	}

	public int getSingleFrameHeight(MotionType state, int direction, int skill, int index) {
		return getFrame(state, direction, skill, index).getFrameHeight();
	}

	public String getName() {
		return name;
	}
}
