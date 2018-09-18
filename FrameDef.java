package CS134FinalAssignment;
class FrameDef {
	private int image;
	private float frameTimeSecs;
	private int[] imageSize = new int[2];
	
	public void setImage(int that) {
		image = that;
	}
	
	public int getImage() {
		return image;
	}
	
	public void setFrameTimeSecs(float time) {
		frameTimeSecs = time;
	}
	
	public float getFrameTimeSecs() {
		return frameTimeSecs;
	}
	
	public void setImageSize(int w, int h) {
		imageSize[0] = w;
		imageSize[1] = h;
	}
	
	public int getFrameWidth() {
		return imageSize[0];
	}
	
	public int getFrameHeight() {
		return imageSize[1];
	}
	
	public int[] getImageSize() {
		return imageSize;
	}
}