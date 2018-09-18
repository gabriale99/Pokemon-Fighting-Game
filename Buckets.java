package CS134FinalAssignment;
import java.util.ArrayList;
import java.util.HashMap;

import com.jogamp.opengl.GL2;

public class Buckets {

	private BackgroundDef background;
	private HashMap<int[], ArrayList<AnimationData>> buckets;
	private HashMap<int[], Boolean> drawDecision;
	private ArrayList<AnimationData> spriteList, spriteAppeared, trash; // Store all existing sprites
	private int[] bucketSize;
	
	public Buckets(BackgroundDef bg) {
		this.background = bg;
		buckets = new HashMap<>();
		drawDecision = new HashMap<>();
		spriteList = new ArrayList<>();
		spriteAppeared = new ArrayList<>();
		trash = new ArrayList<>();
		int mapWidth = background.getWidth() * background.getBGWidth();
		int mapHeight = background.getHeight() * background.getBGHeight();
		bucketSize = new int[] {mapWidth / 4, mapHeight / 4};
		init();
	}
	
	public ArrayList<AnimationData> getSpriteList() {
		return spriteList;
	}
	
	public ArrayList<AnimationData> getSpriteAppeared() {
		return spriteAppeared;
	}
	
	// Separate map into 16 buckets
	private void init() {
		for(int i = 0; i < 4; i++) {
			for (int k = 0; k < 4; k++) {
				int[] bucket = new int[] {bucketSize[0] * i, bucketSize[1] * k};
				buckets.put(bucket, new ArrayList<>());
				drawDecision.put(bucket, false);
			}
		}
	}
	
	public void Update() {
		if (spriteList.isEmpty()) return;
		checkBuckets();
		for(AnimationData data: spriteList) {
			for (int[] bucket: buckets.keySet()) {
				if (checkWithinBucket(data, bucket)) {
					buckets.get(bucket).add(data);
				}
			}
		}
	}
	
	private void checkBuckets() {
		for (int[] bucket: buckets.keySet()) {
			for (AnimationData data: buckets.get(bucket)) {
				if (!spriteList.contains(data)) {
					trash.add(data);
				}
			}
			buckets.get(bucket).removeAll(trash);
		}
	}
	
	private boolean checkWithinBucket(AnimationData character, int[] bucket) {
		return AABB.AABBIntersect(character.getBox(), new AABB(bucket[0], bucket[1], bucketSize[0], bucketSize[1]));
	}
	
	private void decide() {
		Camera cam = FinalProj.camera;
		int tempX = 0;
		int tempY = 0;
		for (int[] bucket: buckets.keySet()) {
			AABB bucketBox = new AABB(bucket[0], bucket[1], bucketSize[0], bucketSize[1]);
			if(AABB.AABBIntersect(bucketBox, cam.getBox())) {
				drawDecision.replace(bucket, true);
				tempX = bucket[0];
				tempY = bucket[1];
			} else {
				if (bucket[0] + bucketSize[0] == tempX || bucket[1] + bucketSize[1] == tempY)
					drawDecision.replace(bucket, true);
				else
					drawDecision.replace(bucket, false);
			}
		}
	}
	
	public void drawBuckets(GL2 gl) {
		decide();
		for(int[] bucket: buckets.keySet()) {
			if(drawDecision.get(bucket)) {
				for(AnimationData data: buckets.get(bucket)) {
					//System.out.println("Drew");
					data.draw(gl);
				}
				//System.out.println("All drew\n");
			}
		}
	}
	
	public void addSprite(AnimationData data) {
		spriteList.add(data);
		spriteAppeared.add(data);
	}
	
	public void removeSprite(AnimationData data) {
		spriteList.remove(data);
	}
	
	public void cleanUp() {
		spriteList.removeAll(spriteList);
		spriteAppeared.removeAll(spriteAppeared);
	}
}
