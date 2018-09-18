package CS134FinalAssignment;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

class AnimationData {
	private AnimationDef def;
	private int curFrame, direction, skill, HP;
	private float secsUntilNextFrame, invincibleTimeCount = 50;
	private MotionType state;
	private AABB characterBox;
	private ArrayList<AttackType> attacks;
	private String dataName;
	private AttackName[] skills;
	private boolean invincible = false, isDashing = false;

	public AnimationData() {
		curFrame = 0;
		direction = 0;
		skill = 1;
		HP = 4;
		secsUntilNextFrame = 0;
		state = MotionType.IDLE;
		characterBox = new AABB();
		attacks = new ArrayList<>();
		dataName = "";
		skills = new AttackName[4];
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public void setDef(AnimationDef that) {
		def = that;
		characterBox.setWidth(def.getSingleFrameWidth(state, direction, skill, 0));
		characterBox.setHeight(def.getSingleFrameHeight(state, direction, skill, 0));
	}

	public AnimationDef getDef() {
		return def;
	}

	public void setCurFrame(int i) {
		curFrame = i;
	}

	public int getCurFrame() {
		return curFrame;
	}

	public void setPositionX(int x) {
		characterBox.setXCoor(x);
	}

	public int getPositionX() {
		return characterBox.getXCoor();
	}

	public void setPositionY(int y) {
		characterBox.setYCoor(y);
	}

	public int getPositionY() {
		return characterBox.getYCoor();
	}

	public void setDirection(int dir) {
		direction = dir;
	}

	public int getDirection() {
		return direction;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public void decHP(int taken) {
		if (state.equals(MotionType.HURT) || state.equals(MotionType.DASHING) || isInvincible())
			return;
		HP -= taken;
		getHurt();
	}

	public int getHP() {
		return HP;
	}

	public Boolean isAlive() {
		return HP >= 1;
	}

	public MotionType getState() {
		return state;
	}

	public AABB getBox() {
		return characterBox;
	}

	public void setSkills(AttackName input1, AttackName input2, AttackName input3, AttackName input4) {
		skills[0] = input1;
		skills[1] = input2;
		skills[2] = input3;
		skills[3] = input4;
	}

	public ArrayList<AttackType> getAttacks() {
		return attacks;
	}

	public void addAttack(AttackType at) {
		if (curFrame > 0)
			return;
		attacks.add(at);
	}

	public void clearAttacks() {
		attacks.removeAll(attacks);
	}
	
	public void stopping() {
		if (state.equals(MotionType.IDLE) || state.equals(MotionType.ATTACKING)) {
			return;
		}

		state = MotionType.IDLE;
		curFrame = 0;
		secsUntilNextFrame = def.getFrame(state, direction, 0, curFrame).getFrameTimeSecs();
	}

	public void moving(int dir) {
		if (state.equals(MotionType.MOVING) && direction == dir || state.equals(MotionType.DASHING)
				|| state.equals(MotionType.ATTACKING) || state.equals(MotionType.HURT)) {
			return;
		}
		direction = dir;
		state = MotionType.MOVING;
		curFrame = 0;
		secsUntilNextFrame = def.getFrame(state, dir, 0, curFrame).getFrameTimeSecs();
	}

	public void dashing() {
		if (state.equals(MotionType.DASHING) || state.equals(MotionType.ATTACKING) || state.equals(MotionType.HURT)) {
			return;
		}
		state = MotionType.DASHING;
		curFrame = 0;
		isDashing = true;
		secsUntilNextFrame = def.getFrame(state, direction, 0, curFrame).getFrameTimeSecs();
	}
	
	public void doneDashing() {
		isDashing = false;
	}

	public void attacking(int skillNum) {
		if (state.equals(MotionType.DASHING) || state.equals(MotionType.ATTACKING) || state.equals(MotionType.HURT)) {
			return;
		}

		state = MotionType.ATTACKING;
		curFrame = 0;
		skill = skillNum;
		secsUntilNextFrame = def.getFrame(state, direction, skill, curFrame).getFrameTimeSecs();
	}

	public void getHurt() {
		if (state == MotionType.HURT) {
			return;
		}

		state = MotionType.HURT;
		curFrame = 0;
		secsUntilNextFrame = def.getFrame(state, direction, 0, curFrame).getFrameTimeSecs();
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void updateInvincible(float deltaTimeMS) {
		if (invincible == false) {
			return;
		}
		if (invincibleTimeCount < 0) {
			invincible = false;
			invincibleTimeCount = 50;
		}
		invincibleTimeCount--;
	}

	public void attack(int skillNum, GL2 gl) {
		if (getState() == MotionType.ATTACKING)
			return;
		AttackType temp = null;
		AttackName cur = skills[skillNum - 1];
		if (cur.equals(AttackName.SLASH)) {
			temp = new SlashData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() - getBox().getWidth() / 2);
			} else {
				temp.setX(getPositionX() + getBox().getWidth() / 2);
			}
			temp.setY(getPositionY() + getBox().getHeight() / 2 - temp.getBox().getHeight() / 2);
		} else if (cur.equals(AttackName.ELECTRICSHOT)) {
			temp = new ElectricShotData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() - temp.getBox().getWidth());
			} else {
				temp.setX(getPositionX() + getBox().getWidth());
			}

			temp.setY(getPositionY());
		} else if (cur.equals(AttackName.ELECTRICBALL)) {
			temp = new ElectricBallData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() + getBox().getWidth() / 2);
			} else {
				temp.setX(getPositionX() + getBox().getWidth() - getBox().getWidth() / 2);
			}
			temp.setY(getPositionY() + getBox().getHeight() / 2 - temp.getBox().getHeight() / 2);
		} else if (cur.equals(AttackName.DRAGONCLAW)) {
			temp = new DragonClawData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() - temp.getBox().getWidth() + getBox().getWidth() / 2);
			} else {
				temp.setX(getPositionX() + getBox().getWidth() / 2);
			}
			temp.setY(getPositionY() + getBox().getHeight() / 2 - temp.getBox().getHeight() / 2);
		} else if (cur.equals(AttackName.FIREBALL)) {
			temp = new FireBallData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() - 30);
			} else {
				temp.setX(getPositionX() + getBox().getWidth() + 10);
			}
			temp.setY(getPositionY() + 13);
		} else if (cur.equals(AttackName.LIGHTNING)) {
			temp = new LightningData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() - temp.getBox().getWidth() - 50);
			} else {
				temp.setX(getPositionX() + getBox().getWidth() + 50);
			}
			temp.setY(getPositionY() + getBox().getHeight() - temp.getBox().getHeight());
		} else if (cur.equals(AttackName.DragonBreath)) {
			temp = new DragonBreathData(gl);
			if (direction == 0) {
				temp.setX(getPositionX());
			} else {
				temp.setX(getPositionX() + getBox().getWidth());
			}
			temp.setY(getPositionY() + getBox().getHeight() - temp.getBox().getHeight());
		} else if (cur.equals(AttackName.THUNDER)) {
			temp = new ThunderData(gl);
			if (direction == 0) {
				temp.setX(getPositionX() - temp.getBox().getWidth());
			} else {
				temp.setX(getPositionX() + getBox().getWidth());
			}
			temp.setY(getPositionY() + getBox().getHeight() - temp.getBox().getHeight());
		}
		attacking(skillNum);
		temp.setDirection(direction);
		addAttack(temp);
	}

	public void update(float deltaTimeMS) {
		secsUntilNextFrame -= deltaTimeMS;
		updateInvincible(deltaTimeMS);
		while (secsUntilNextFrame <= 0) {
			if (state == MotionType.HURT) {
				stopping();
				invincible = true;
				break;
			}

			curFrame++;
			if (curFrame == def.getFramesSize(state, skill)) {
				if (state == MotionType.ATTACKING) {
					state = MotionType.IDLE;
				} else if (state.equals(MotionType.DASHING) && !isDashing) {
					state = MotionType.MOVING;
				}
				curFrame = 0;
			}
			secsUntilNextFrame += def.getFrame(state, direction, skill, curFrame).getFrameTimeSecs();
		}
	}

	public void draw(GL2 gl) {
		if (state == MotionType.HURT || isInvincible()) {
			if (secsUntilNextFrame % 4 == 0) {
				return;
			}
		}
		FrameDef temp = def.getFrame(state, direction, skill, curFrame);
		int image = temp.getImage();
		int screenX = characterBox.getXCoor() - FinalProj.camera.getCameraX();
		int screenY = characterBox.getYCoor() - FinalProj.camera.getCameraY();
		int imageWidth = temp.getFrameWidth();
		int imageHeight = temp.getFrameHeight();
		FinalProj.glDrawSprite(gl, image, screenX, screenY, imageWidth, imageHeight);
	}
}
