package CS134FinalAssignment;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;

public class CharacterControl {

	final static int MAIN_MOVE_FPS = 300;
	final static long FOLLOW_VELOCITY = 250;
	final static int MOVE_TIME = 50;
	final static int TOTAL_SQUARE_MOVE_TIME = 400;
	final static int ATTACK_TIME = 1500;
	final static int SCREEN_MOVE_FPS = MAIN_MOVE_FPS;
	final static int MAX_CAMERA_AND_MAIN_DISTANCE_X = 500;
	final static int MAX_CAMERA_AND_MAIN_DISTANCE_Y = 500;
	final static int NEXT_DOUBLE_TAP_IN = 500; // MS
	final static int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
	final static int END_GAME_AT = 5000;
	final static int DASH_DISTANCE = 150;
	
	// Player v.s. Player
	// Player 1 control
	// Skill 1, 2, 3, 4
	private short[] player1Attack = { KeyEvent.VK_SHIFT, KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C };
	// left, right, up, down
	private short[] player1Movement = { KeyEvent.VK_D, KeyEvent.VK_G, KeyEvent.VK_R, KeyEvent.VK_F };
	// Player 2 control
	private short[] player2Attack = { KeyEvent.VK_M, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD, KeyEvent.VK_SLASH };
	private short[] player2Movement = { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN };
	
	private boolean isPlayer;
	private AnimationData player;
	private BackgroundDef bgDef;
	private short[] attack, control;
	private int playerFDT, playerSDT, playerNTC = NEXT_DOUBLE_TAP_IN, 
			playerApproach = DASH_DISTANCE, playerDashDirection;
	private boolean playerFIT = false, playerSIT = false;
	
	public CharacterControl(boolean isPlayer1, MainData data, BackgroundDef bg) {
		isPlayer = isPlayer1;
		player = data;
		bgDef = bg;
		if (isPlayer) {
			attack  = player1Attack;
			control = player1Movement;
		} else {
			attack  = player2Attack;
			control = player2Movement;
		}
	}
	
	public CharacterControl(MainData data, BackgroundDef bg) {
		isPlayer = true;
		player = data;
		bgDef = bg;
		attack = new short[]{KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V};
		control = player2Movement;
	}
	
	public CharacterControl(EnemyData data, BackgroundDef bg) {
		player = data;
		bgDef = bg;
	}
	
	public void controlCharacter(long deltaTimeMS, GL2 gl) {
		long time = deltaTimeMS;

		boolean checkDT = doubleTap();

		if (playerApproach != DASH_DISTANCE) {
			dash(playerDashDirection, deltaTimeMS);
			return;
		}

		if (playerFIT)
			playerNTC -= deltaTimeMS;

		if (player.getState().equals(MotionType.ATTACKING) || player.getState().equals(MotionType.HURT)) {
			return;
		}

		if (FinalProj.kbState[attack[0]]) {
			player.attack(1, gl);
		} else if (FinalProj.kbState[attack[1]]) {
			player.attack(2, gl);
		} else if (FinalProj.kbState[attack[2]]) {
			player.attack(3, gl);
		} else if (FinalProj.kbState[attack[3]]) {
			player.attack(4, gl);
		} else if (FinalProj.kbState[control[0]] && FinalProj.kbState[control[2]]) {
			player.moving(0);
			moveToLeft(time);
			moveUpward(time);
		} else if (FinalProj.kbState[control[1]] && FinalProj.kbState[control[2]]) {
			player.moving(1);
			moveToRight(time);
			moveUpward(time);
		} else if (FinalProj.kbState[control[0]] && FinalProj.kbState[control[3]]) {
			player.moving(0);
			moveToLeft(time);
			moveDownward(time);
		} else if (FinalProj.kbState[control[1]] && FinalProj.kbState[control[3]]) {
			player.moving(1);
			moveToRight(time);
			moveDownward(time);
		} else if (FinalProj.kbState[control[0]]) {
			if (!playerFIT &FinalProj.kbPrevState[control[0]]) {
				playerFDT = LEFT;
				playerFIT = true;
			} else if (!playerSIT && !FinalProj.kbPrevState[control[0]]) {
				playerSDT = LEFT;
				playerSIT = true;
			}

			if (checkDT)
				dash(0, deltaTimeMS);
			else {
				player.moving(0);
				moveToLeft(time);
			}
		} else if (FinalProj.kbState[control[1]]) {
			if (!playerFIT) {
				playerFDT = RIGHT;
				playerFIT = true;
			} else if (!playerSIT && !FinalProj.kbPrevState[control[1]]) {
				playerSDT = RIGHT;
				playerSIT = true;
			}

			if (checkDT)
				dash(1, deltaTimeMS);
			else {
				player.moving(1);
				moveToRight(time);
			}
		} else if (FinalProj.kbState[control[2]]) {
			if (!playerFIT) {
				playerFDT = UP;
				playerFIT = true;
			} else if (!playerSIT && !FinalProj.kbPrevState[control[2]]) {
				playerSDT = UP;
				playerSIT = true;
			}

			if (checkDT)
				dash(2, deltaTimeMS);
			else {
				player.moving(player.getDirection());
				moveUpward(time);
			}
		} else if (FinalProj.kbState[control[3]]) {
			if (!playerFIT) {
				playerFDT = DOWN;
				playerFIT = true;
			} else if (!playerSIT && !FinalProj.kbPrevState[control[3]]) {
				playerSDT = DOWN;
				playerSIT = true;
			}

			if (checkDT)
				dash(3, deltaTimeMS);
			else {
				player.moving(player.getDirection());
				moveDownward(time);
			}
		} else {
			player.stopping();
		}
		return;
	}
	
	public void moveToLeft(long deltaTime) {
		int fps = MAIN_MOVE_FPS * (int) deltaTime / 1000;
		player.setPositionX(player.getPositionX() - fps);
		if (player.getPositionX() < 0) {
			player.setPositionX(0);
		}
		int x = player.getPositionX();
		int y = player.getPositionY();
		int height = player.getBox().getHeight();
		int tileX = x / bgDef.getBGWidth();
		int startY = y / bgDef.getBGHeight();
		int endY = (y + height) / bgDef.getBGHeight();
		for (int i = startY; i <= endY; i++) {
			if (i * bgDef.getWidth() + tileX >= 1600)
				continue;
			if (bgDef.getCollision(tileX, i) == 1) {
				AABB tile = new AABB(tileX * bgDef.getBGWidth(), i * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(player.getBox(), tile)) {
					// System.out.println("Intersect");
					player.setPositionX(tileX * bgDef.getBGWidth() + bgDef.getBGWidth() + 1);
					break;
				}
			}
		}

		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (player.equals(other))
				continue;
			int otherX = other.getPositionX();
			if (AABB.AABBIntersect(player.getBox(), other.getBox())) {
				// System.out.println(true);
				player.setPositionX(otherX + other.getBox().getWidth() + 1);
			}
		}
	}

	public void moveToRight(long deltaTime) {
		int fps = MAIN_MOVE_FPS * (int) deltaTime / 1000;
		player.setPositionX(player.getPositionX() + fps);
		if (player.getPositionX() + player.getDef().getSingleFrameWidth(player.getState(), player.getDirection(),
				player.getSkill(), player.getCurFrame()) > bgDef.getWidth() * bgDef.getBGWidth()) {
			player.setPositionX(bgDef.getWidth() * bgDef.getBGWidth()
					- player.getDef().getSingleFrameWidth(player.getState(), player.getDirection(), 0, player.getCurFrame()));
		}
		int x = player.getPositionX();
		int y = player.getPositionY();
		int width = player.getBox().getWidth();
		int height = player.getBox().getHeight();
		int tileX = (x + width) / bgDef.getBGWidth();
		int startY = y / bgDef.getBGHeight();
		int endY = (y + height) / bgDef.getBGHeight();
		for (int i = startY; i <= endY; i++) {
			if (i * bgDef.getWidth() + tileX >= 1600)
				continue;
			if (bgDef.getCollision(tileX, i) == 1) {
				AABB tile = new AABB(tileX * bgDef.getBGWidth(), i * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(player.getBox(), tile)) {
					player.setPositionX(tileX * bgDef.getBGWidth() - player.getBox().getWidth() - 1);
					break;
				}
			}
		}

		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (player.equals(other))
				continue;
			int otherX = other.getPositionX();
			if (AABB.AABBIntersect(player.getBox(), other.getBox())) {
				// System.out.println(true);
				player.setPositionX(otherX - player.getBox().getWidth() - 1);
			}
		}
	}

	public void moveUpward(long deltaTime) {
		int fps = MAIN_MOVE_FPS * (int) deltaTime / 1000;
		player.setPositionY(player.getPositionY() - fps);
		if (player.getPositionY() < 0) {
			player.setPositionY(0);
		}

		int x = player.getPositionX();
		int y = player.getPositionY();
		int height = player.getBox().getHeight();
		int tileY = y / bgDef.getBGHeight();
		int startX = x / bgDef.getBGWidth();
		int endX = (x + height) / bgDef.getBGWidth();
		for (int i = startX; i <= endX; i++) {
			if (tileY * bgDef.getWidth() + i >= 1600)
				continue;
			if (bgDef.getCollision(i, tileY) == 1) {
				AABB tile = new AABB(i * bgDef.getBGWidth(), tileY * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(player.getBox(), tile)) {
					player.setPositionY(tileY * bgDef.getBGHeight() + bgDef.getBGHeight() + 1);
					break;
				}
			}
		}

		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (player.equals(other))
				continue;
			int otherY = other.getPositionY();
			if (AABB.AABBIntersect(player.getBox(), other.getBox())) {
				player.setPositionY(otherY + other.getBox().getHeight() + 1);
			}
		}
	}

	public void moveDownward(long deltaTime) {
		int fps = MAIN_MOVE_FPS * (int) deltaTime / 1000;
		player.setPositionY(player.getPositionY() + fps);
		if (player.getPositionY() + player.getDef().getSingleFrameHeight(player.getState(), player.getDirection(),
				player.getSkill(), player.getCurFrame()) > bgDef.getHeight() * bgDef.getBGHeight()) {
			player.setPositionY(bgDef.getHeight() * bgDef.getBGHeight()
					- player.getDef().getSingleFrameHeight(player.getState(), player.getDirection(), 0, player.getCurFrame()));
		}
		int x = player.getPositionX();
		int y = player.getPositionY();
		int width = player.getBox().getWidth();
		int height = player.getBox().getHeight();
		int tileY = (y + height) / bgDef.getBGHeight();
		int startX = x / bgDef.getBGWidth();
		int endX = (x + width) / bgDef.getBGWidth();
		for (int i = startX; i <= endX; i++) {
			if (tileY * bgDef.getWidth() + i >= 1600)
				continue;
			if (bgDef.getCollision(i, tileY) == 1) {
				AABB tile = new AABB(i * bgDef.getBGWidth(), tileY * bgDef.getBGHeight(), bgDef.getBGWidth(),
						bgDef.getBGHeight());
				if (AABB.AABBIntersect(player.getBox(), tile)) {
					player.setPositionY(tileY * bgDef.getBGHeight() - player.getBox().getHeight() - 1);
					break;
				}
			}
		}
		for (AnimationData other : FinalProj.buckets.getSpriteList()) {
			if (player.equals(other))
				continue;
			int otherY = other.getPositionY();
			if (AABB.AABBIntersect(player.getBox(), other.getBox())) {
				// System.out.println(true);
				player.setPositionY(otherY - player.getBox().getHeight() - 1);
			}
		}
	}

	private void dash(int dir, long deltaTime) {
		int deltaTime2 = (int) deltaTime * 2;
		player.dashing();
		if (playerApproach <= 0) {
			playerApproach = DASH_DISTANCE;
			player.doneDashing();
			return;
		}
		
		playerDashDirection = dir;
		playerApproach -= MAIN_MOVE_FPS * deltaTime2 / 1000;
		
		if (dir == 0) {
			moveToLeft(deltaTime2);
		} else if (dir == 1) {
			moveToRight(deltaTime2);
		} else if (dir == 2) {
			moveUpward(deltaTime2);
		} else {
			moveDownward(deltaTime2);
		}
	}

	private boolean doubleTap() {
		boolean doubleTapped = false;
		if (playerNTC < 0 && !playerSIT) {
			playerFIT = false;
			playerNTC = NEXT_DOUBLE_TAP_IN;
		}
		if (playerFIT && playerSIT) {
			if (playerFDT == playerSDT) {
				// System.out.println("First Double tapped");
				doubleTapped = true;
			}
			playerFIT = false;
			playerSIT = false;
			playerNTC = NEXT_DOUBLE_TAP_IN;
		}

		return doubleTapped;
	}

}
