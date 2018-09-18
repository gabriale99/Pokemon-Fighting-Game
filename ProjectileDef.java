package CS134FinalAssignment;

public class ProjectileDef {

	private FrameDef[] leftProjectile, rightProjectile;
	private int defLength;
	
	public ProjectileDef(int length) {
		defLength = length;
		leftProjectile = new FrameDef[length];
		rightProjectile = new FrameDef[length];
		for(int i = 0; i < length; i++) {
			leftProjectile[i] = new FrameDef();
			rightProjectile[i] = new FrameDef();
		}
	}
	
	public int getDefLength() {
		return defLength;
	}
	
	public FrameDef getFrame(int dir, int i) {
		if (dir == 0) {
			return leftProjectile[i];
		} else {
			return rightProjectile[i];
		}
	}
	
	public int getFrameWidth(int i) {
		return leftProjectile[i].getFrameWidth();
	}
	
	public int getFrameHeight(int i) {
		return leftProjectile[i].getFrameHeight();
	}
}
