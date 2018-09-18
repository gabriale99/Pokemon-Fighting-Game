package CS134FinalAssignment;

public class GlyphDef {

	String texName;
	int width;
	int tex;
	
	public GlyphDef(String input) {
		texName = input;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setTex(int i) {
		tex = i;
	}
	
	public int getTex() {
		return tex;
	}
}
