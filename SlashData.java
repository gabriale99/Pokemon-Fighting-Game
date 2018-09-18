package CS134FinalAssignment;

import com.jogamp.opengl.GL2;

public class SlashData extends ProjectileData{

	private SlashDef def;
	
	public SlashData(GL2 gl) {
		super();
		def = new SlashDef(gl);
		setProjectileDef(def);
	}

}
