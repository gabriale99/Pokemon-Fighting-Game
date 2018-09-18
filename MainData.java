package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class MainData extends AnimationData {

	private boolean isPlayer1;
	private int posX, posY;

	public MainData(AnimationDef def, String name, int x, int y, boolean one) {
		super();
		setDef(def);
		setPositionX(x);
		posX = x;
		setPositionY(y);
		posY = y;
		setDirection(1);
		setDataName(name);
		if (def instanceof CharizardDef) {
			setSkills(AttackName.SLASH, AttackName.DRAGONCLAW, AttackName.FIREBALL, AttackName.DragonBreath);
		} else {
			setSkills(AttackName.ELECTRICSHOT, AttackName.ELECTRICBALL, AttackName.LIGHTNING, AttackName.THUNDER);
		}
		isPlayer1 = one;
	}

	public void restore() {
		setPositionX(posX);
		setPositionY(posY);
		getAttacks().removeAll(getAttacks());
	}
	
	public void presentHealth(GL2 gl) {
		int health = getHP();
		int i = 1;
		if (isPlayer1) {
			FinalProj.glDrawSprite(gl, getDef().getIcon().getImage(), 0, 0, 75, 75);
			FinalProj.fonts.drawText(gl, getDataName(), 0, 75);
			if ((health % 2) == 0) {
				health /= 2;
				for (; i <= health; i++) {
					FinalProj.glDrawSprite(gl, getDef().getFullHeart(), i * 90, 0, 75, 75);
				}
			} else {
				health--;
				health /= 2;
				for (; i <= health; i++) {
					FinalProj.glDrawSprite(gl, getDef().getFullHeart(), i * 90, 0, 75, 75);
				}
				FinalProj.glDrawSprite(gl, getDef().getHalfHeart(), i * 90, 0, 75, 75);
			}
		} else {
			FinalProj.glDrawSprite(gl, getDef().getIcon().getImage(), 360, 0, 75, 75);
			FinalProj.fonts.drawText(gl, getDataName(), 360, 75);
			if ((health % 2) == 0) {
				health /= 2;
				for (; i <= health; i++) {
					FinalProj.glDrawSprite(gl, getDef().getFullHeart(), (i + 4) * 90, 0, 75, 75);
				}
			} else {
				health--;
				health /= 2;
				for (; i <= health; i++) {
					FinalProj.glDrawSprite(gl, getDef().getFullHeart(), (i + 4) * 90, 0, 75, 75);
				}
				FinalProj.glDrawSprite(gl, getDef().getHalfHeart(), (i + 4) * 90, 0, 75, 75);
			}
		}
	}
}
