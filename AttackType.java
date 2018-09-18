package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public interface AttackType {
	public void update(float deltaTimeMS);
	public int getCurFrame();
	public int getAnimationLength();
	public int damageDealt();
	public void setX(int i);
	public void setY(int i);
	public void setDirection(int dir);
	public boolean needWholeAnimation();
	public boolean isOut();
	public void draw(GL2 gl);
	public AABB getBox();
}
