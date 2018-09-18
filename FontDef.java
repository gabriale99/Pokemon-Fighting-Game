package CS134FinalAssignment;
import java.util.Iterator;
import java.util.stream.IntStream;

import com.jogamp.opengl.GL2;

public class FontDef {

	private int lineHeight;
	GlyphDef[] fonts;

	public FontDef(GL2 gl) {
		init(gl);
	}

	public void init(GL2 gl) {
		fonts = new GlyphDef[95];
		int[] size = new int[2];
		for (int i = 0; i < 95; i++) {
			if (i < 9) {
				fonts[i] = new GlyphDef("char0" + (i + 1));
				fonts[i].setTex(FinalProj.glTexImageTGAFile(gl, "Font/fonts/char0" + (i + 1) + ".tga", size));
				fonts[i].setWidth(size[0]);
			} else {
				fonts[i] = new GlyphDef("char" + (i + 1));
				fonts[i].setTex(FinalProj.glTexImageTGAFile(gl, "Font/fonts/char" + (i + 1) + ".tga", size));
				fonts[i].setWidth(size[0]);
			}
		}
		lineHeight = size[1];
	}

	public void drawText(GL2 gl, String str, int x, int y) {
		IntStream stream = str.codePoints();
		Iterator<Integer> it = stream.iterator();

		for (int i = 0; i < str.length(); i++) {
			int index = it.next() - 32;
			GlyphDef glyph = fonts[index];
			FinalProj.glDrawSprite(gl, glyph.getTex(), x, y, glyph.getWidth(), 50);
			x += glyph.width;
		}
	}

	public int measureText(String str) {
		IntStream stream = str.codePoints();
		Iterator<Integer> it = stream.iterator();
		int width = 0;
		for (int i = 0; i < str.length(); i++) {
			int index = it.next() - 32;
			GlyphDef glyph = fonts[index];
			width += glyph.width;
		}
		return width;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}
}
