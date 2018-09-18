package CS134FinalAssignment;

import java.util.Random;

import com.jogamp.opengl.GL2;

public class EnemyData extends AnimationData {

	private MotionType actionDetermined;
	private BackgroundDef bgDef;
	private int count = 1000;
	private Boolean changeAction = true, aggressiveStyle;

	public EnemyData(AnimationDef def, int x, int y, int dir, Boolean style, BackgroundDef bg) {
		super();
		setDef(def);
		setPositionX(2000);
		setPositionY(500);
		bgDef = bg;
		aggressiveStyle = style;
		if (aggressiveStyle) makeAggressiveDecision();
		else makeDefensiveDecision();
		if (def instanceof CharizardDef) {
			setSkills(AttackName.SLASH, AttackName.ELECTRICSHOT, AttackName.FIREBALL, AttackName.DragonBreath);
		} else {
			setSkills(AttackName.ELECTRICSHOT, AttackName.ELECTRICBALL, AttackName.LIGHTNING, AttackName.THUNDER);
		}
	}

	public EnemyData copy() {
		return new EnemyData(getDef(), getPositionX(), getPositionY(), getDirection(), aggressiveStyle, bgDef);
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public boolean getStyle() {
		return aggressiveStyle;
	}
	
	public BackgroundDef getBgDef() {
		return bgDef;
	}
	
	public boolean charge(AnimationData following, long deltaTime) {

		int mainX = following.getPositionX();
		int mainY = following.getPositionY();
		int mainWidth = following.getBox().getWidth();
		int enemyX = getPositionX();
		int enemyY = getPositionY();
		long time = deltaTime * 2 / 3;
		
		boolean LRMove = true, UDMove = true;

		if ((mainX - enemyX) >= mainWidth + 100) {
			moving(1);
			moveToRight(time);
			if ((mainX - enemyX) < 5) {
				setPositionX(mainX + mainWidth);
				LRMove = false;
			}
		} else if ((mainX - enemyX) <= -100) {
			moving(0);
			moveToLeft(time);
			if ((mainX - enemyX) > -5) {
				setPositionX(mainX - getBox().getWidth());
				LRMove = false;
			}
		}

		if ((mainY - enemyY) > 0) {
			moving(getDirection());
			moveDownward(time);
			if ((mainY - enemyY) < 5) {
				setPositionY(mainY);
				UDMove = false;
			}
		} else if ((mainY - enemyY) < 0) {
			moving(getDirection());
			moveUpward(time);
			if ((mainY - enemyY) > 5) {
				setPositionY(mainY);
				UDMove = false;
			}
		}

		if ((LRMove & UDMove) == false) {
			stopping();
			return true;
		}
		return false;
	}

	public boolean retreat(AnimationData retreatFrom, long deltaTime) {
		int mainX = retreatFrom.getPositionX();
		int mainY = retreatFrom.getPositionY();
		int mainWidth = retreatFrom.getBox().getWidth();
		int mainHeight = retreatFrom.getBox().getHeight();
		int enemyX = getPositionX();
		int enemyY = getPositionY();
		int enemyWidth = getBox().getWidth();
		int enemyHeight = getBox().getHeight();
		long time = deltaTime * 2 / 3;

		if ((mainX - enemyX) <= mainWidth) {
			moving(1);
			moveToRight(time);
		} else if ((mainY - enemyY) >= enemyWidth) {
			moving(0);
			moveToLeft(time);
		}

		if ((mainY - enemyY) <= mainHeight) {
			moving(getDirection());
			moveDownward(time);
		} else if ((mainY - enemyY) >= enemyHeight) {
			moving(getDirection());
			moveUpward(time);
		}

		if ((mainX - enemyX) >= 400 || (mainX - enemyX) <= -400) {
			return true;
		} else if ((mainY - enemyY) >= 200 || (mainY - enemyY) <= -200) {
			return true;
		} else {
			return false;
		}
	}

	public void enemyAttack(GL2 gl) {
		int random = new Random().nextInt(100) + 1;
		if (random <= 100 && random > 75){
			attack(1, gl);
		} else if (random <= 75 && random > 50){
			attack(2, gl);
		} else if (random <= 50 && random > 25){ 
			attack(3, gl);
		} else {
			attack(4, gl);
		}
		
	}

	public boolean randomMotion(AnimationData target, long deltaTimeMS, GL2 gl) {
		if (!this.isAlive())
			actionDetermined = MotionType.IDLE;
		if (actionDetermined == MotionType.MOVING) {
			return charge(target, deltaTimeMS);
		} else if (actionDetermined == MotionType.ATTACKING) {
			if (target.getPositionX() > getPositionX()) setDirection(1); 
			else setDirection(0);
			enemyAttack(gl);
			return true;
		} else if (actionDetermined == MotionType.RETREATING) {
			return retreat(target, deltaTimeMS);
		} else if (!target.isAlive()) {
			actionDetermined = MotionType.IDLE;
		}
		return false;
	}

	private void makeAggressiveDecision() {
		int random = new Random().nextInt(100) + 1;
		// 40% chance Attacking
		if (random <= 100 && random > 60) {
			actionDetermined = MotionType.ATTACKING;
		} else if (random <= 60 && random > 20) { // 40% chance Charging
			actionDetermined = MotionType.MOVING;
		} else { // 20% chance retreating
			actionDetermined = MotionType.RETREATING;
		}
	}

	private void makeDefensiveDecision() {
		int random = new Random().nextInt(100) + 1;
		// 20% chance Attacking
		if (random <= 100 && random > 80) {
			actionDetermined = MotionType.ATTACKING;
		} else if (random <= 80 && random > 50) { // 30% chance Charging
			actionDetermined = MotionType.MOVING;
		} else { // 50% chance retreating
			actionDetermined = MotionType.RETREATING;
		}
	}

	public void nextDecision(AnimationData target, long deltaTimeMS, GL2 gl) {
		if (!target.isAlive()) {
			stopping();
		}
		if (getState() == MotionType.HURT || getState() == MotionType.ATTACKING) return;
		changeAction = randomMotion(target, deltaTimeMS, gl);
		if (changeAction == true) {
			if(aggressiveStyle == true)
				makeAggressiveDecision();
			else
				makeDefensiveDecision();
			changeAction = false;
			count = 1000;
		} else if (count < 0) {
			if(aggressiveStyle == true)
				makeAggressiveDecision();
			else
				makeDefensiveDecision();
			count += 1000;
		}
		count -= deltaTimeMS;
	}
	
	public void moveToLeft(long deltaTime) {
		int fps = CharacterControl.MAIN_MOVE_FPS * (int) deltaTime / 1000;
		this.setPositionX(this.getPositionX() - fps);
		if (this.getPositionX() < 0) {
			this.setPositionX(0);
		}
		int x = this.getPositionX();
		int y = this.getPositionY();
		int height = this.getBox().getHeight();
		int tileX = x / bgDef.getBGWidth();
		int startY = y / bgDef.getBGHeight();
		int endY = (y + height) / bgDef.getBGHeight();
		for (int i = startY; i <= endY; i++) {
			if (i * bgDef.getWidth() + tileX >= 1600)
				continue;
			if (bgDef.getCollision(tileX, i) == 1) {
				AABB tile = new AABB(tileX * bgDef.getBGWidth(), i * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(this.getBox(), tile)) {
					// System.out.println("Intersect");
					this.setPositionX(tileX * bgDef.getBGWidth() + bgDef.getBGWidth() + 1);
					break;
				}
			}
		}

		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (this.equals(other))
				continue;
			int otherX = other.getPositionX();
			if (AABB.AABBIntersect(this.getBox(), other.getBox())) {
				// System.out.println(true);
				this.setPositionX(otherX + other.getBox().getWidth() + 1);
			}
		}
	}

	public void moveToRight(long deltaTime) {
		int fps = CharacterControl.MAIN_MOVE_FPS * (int) deltaTime / 1000;
		this.setPositionX(this.getPositionX() + fps);
		if (this.getPositionX() + this.getDef().getSingleFrameWidth(this.getState(), this.getDirection(),
				this.getSkill(), this.getCurFrame()) > bgDef.getWidth() * bgDef.getBGWidth()) {
			this.setPositionX(bgDef.getWidth() * bgDef.getBGWidth()
					- this.getDef().getSingleFrameWidth(this.getState(), this.getDirection(), 0, this.getCurFrame()));
		}
		int x = this.getPositionX();
		int y = this.getPositionY();
		int width = this.getBox().getWidth();
		int height = this.getBox().getHeight();
		int tileX = (x + width) / bgDef.getBGWidth();
		int startY = y / bgDef.getBGHeight();
		int endY = (y + height) / bgDef.getBGHeight();
		for (int i = startY; i <= endY; i++) {
			if (i * bgDef.getWidth() + tileX >= 1600)
				continue;
			if (bgDef.getCollision(tileX, i) == 1) {
				AABB tile = new AABB(tileX * bgDef.getBGWidth(), i * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(this.getBox(), tile)) {
					this.setPositionX(tileX * bgDef.getBGWidth() - this.getBox().getWidth() - 1);
					break;
				}
			}
		}

		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (this.equals(other))
				continue;
			int otherX = other.getPositionX();
			if (AABB.AABBIntersect(this.getBox(), other.getBox())) {
				// System.out.println(true);
				this.setPositionX(otherX - this.getBox().getWidth() - 1);
			}
		}
	}

	public void moveUpward(long deltaTime) {
		int fps = CharacterControl.MAIN_MOVE_FPS * (int) deltaTime / 1000;
		this.setPositionY(this.getPositionY() - fps);
		if (this.getPositionY() < 0) {
			this.setPositionY(0);
		}

		int x = this.getPositionX();
		int y = this.getPositionY();
		int height = this.getBox().getHeight();
		int tileY = y / bgDef.getBGHeight();
		int startX = x / bgDef.getBGWidth();
		int endX = (x + height) / bgDef.getBGWidth();
		for (int i = startX; i <= endX; i++) {
			if (tileY * bgDef.getWidth() + i >= 1600)
				continue;
			if (bgDef.getCollision(i, tileY) == 1) {
				AABB tile = new AABB(i * bgDef.getBGWidth(), tileY * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(this.getBox(), tile)) {
					this.setPositionY(tileY * bgDef.getBGHeight() + bgDef.getBGHeight() + 1);
					break;
				}
			}
		}

		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (this.equals(other))
				continue;
			int otherY = other.getPositionY();
			if (AABB.AABBIntersect(this.getBox(), other.getBox())) {
				this.setPositionY(otherY + other.getBox().getHeight() + 1);
			}
		}
	}

	public void moveDownward(long deltaTime) {
		int fps = CharacterControl.MAIN_MOVE_FPS * (int) deltaTime / 1000;
		this.setPositionY(this.getPositionY() + fps);
		if (this.getPositionY() + this.getDef().getSingleFrameHeight(this.getState(), this.getDirection(),
				this.getSkill(), this.getCurFrame()) > bgDef.getHeight() * bgDef.getBGHeight()) {
			this.setPositionY(bgDef.getHeight() * bgDef.getBGHeight()
					- this.getDef().getSingleFrameHeight(this.getState(), this.getDirection(), 0, this.getCurFrame()));
		}
		int x = this.getPositionX();
		int y = this.getPositionY();
		int width = this.getBox().getWidth();
		int height = this.getBox().getHeight();
		int tileY = (y + height) / bgDef.getBGHeight();
		int startX = x / bgDef.getBGWidth();
		int endX = (x + width) / bgDef.getBGWidth();
		for (int i = startX; i <= endX; i++) {
			if (tileY * bgDef.getWidth() + i >= 1600)
				continue;
			if (bgDef.getCollision(i, tileY) == 1) {
				AABB tile = new AABB(i * bgDef.getBGWidth(), tileY * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(this.getBox(), tile)) {
					this.setPositionY(tileY * bgDef.getBGHeight() - this.getBox().getHeight() - 1);
					break;
				}
			}
		}
		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (this.equals(other))
				continue;
			int otherY = other.getPositionY();
			if (AABB.AABBIntersect(this.getBox(), other.getBox())) {
				// System.out.println(true);
				this.setPositionY(otherY - this.getBox().getHeight() - 1);
			}
		}
	}


}
