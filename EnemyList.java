package CS134FinalAssignment;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class EnemyList {

	ArrayList<EnemyData> enemyList, enemyListStore;

	public EnemyList() {
		enemyList = new ArrayList<>();
		enemyListStore = new ArrayList<>();
	}

	public void addEnemy(EnemyData enemy) {
		enemyList.add(enemy);
		EnemyData store = enemy.copy();
		enemyListStore.add(store);
	}

	public void restore(Buckets bucket) {
		enemyList = new ArrayList<EnemyData>(enemyListStore);
		enemyListStore.removeAll(enemyListStore);
		for (EnemyData ed: enemyList) {
			bucket.addSprite(ed);
			enemyListStore.add(ed.copy());
		}
	}
	
	public ArrayList<EnemyData> getEnmeyList() {
		return enemyList;
	}

	public Boolean isEnemy(AnimationData character) {
		return enemyList.contains(character);
	}

	public void level1Enemy(GL2 gl, Buckets bucket, BackgroundDef bgDef) {
		int width = FinalProj.fonts.measureText("Loading...");
		FinalProj.fonts.drawText(gl, "Loading...", FinalProj.camera.getCameraWidth() - width,
				FinalProj.camera.getCameraHeight() - FinalProj.fonts.getLineHeight());
		for (int i = 0; i < 3; i++) {
			AnimationDef def = new PikachuDef(gl);
			int x = FinalProj.random.nextInt(1500) + 1000;
			int y = FinalProj.random.nextInt(750) + 1000;
			while (bgDef.getCollision(x / bgDef.getBGWidth(), y / bgDef.getBGHeight()) == 1) {
				x = FinalProj.random.nextInt(1500) + 1000;
				y = FinalProj.random.nextInt(750) + 1000;
			}
			EnemyData enemy = new EnemyData(def, x, y, 1, false, bgDef);
			addEnemy(enemy);
			bucket.addSprite(enemy);
			System.out.println("Pika " + i + " is ready");
		}
	}

	public void level2Enemy(GL2 gl, Buckets bucket, BackgroundDef bgDef) {
		for (int i = 0; i < 2; i++) {
			int width = FinalProj.fonts.measureText("Loading...");
			FinalProj.fonts.drawText(gl, "Loading...", FinalProj.camera.getCameraWidth() - width,
					FinalProj.camera.getCameraHeight() - FinalProj.fonts.getLineHeight());
			AnimationDef def = new PikachuDef(gl);
			int x = FinalProj.random.nextInt(1500) + 1000;
			int y = FinalProj.random.nextInt(750) + 1000;
			while (bgDef.getCollision(x / bgDef.getBGWidth(), y / bgDef.getBGHeight()) == 1) {
				x = FinalProj.random.nextInt(1500) + 1000;
				y = FinalProj.random.nextInt(750) + 1000;
			}
			EnemyData enemy = new EnemyData(def, x, y, FinalProj.random.nextInt(2), true, bgDef);
			addEnemy(enemy);
			bucket.addSprite(enemy);
		}

		for (int i = 0; i < 2; i++) {
			int width = FinalProj.fonts.measureText("Loading...");
			FinalProj.fonts.drawText(gl, "Loading...", FinalProj.camera.getCameraWidth() - width,
					FinalProj.camera.getCameraHeight() - FinalProj.fonts.getLineHeight());
			AnimationDef def = new CharizardDef(gl);
			int x = FinalProj.random.nextInt(1500) + 1000;
			int y = FinalProj.random.nextInt(750) + 1000;
			while (bgDef.getCollision(x / bgDef.getBGWidth(), y / bgDef.getBGHeight()) == 1) {
				x = FinalProj.random.nextInt(1500) + 1000;
				y = FinalProj.random.nextInt(750) + 1000;
			}
			EnemyData enemy = new EnemyData(def, x, y, FinalProj.random.nextInt(2), false, bgDef);
			addEnemy(enemy);
			bucket.addSprite(enemy);
		}
	}

	public void level3Enemy(GL2 gl, Buckets bucket, BackgroundDef bgDef) {
		int width = FinalProj.fonts.measureText("Loading...");
		FinalProj.fonts.drawText(gl, "Loading...", FinalProj.camera.getCameraWidth() - width,
				FinalProj.camera.getCameraHeight() - FinalProj.fonts.getLineHeight());
		for (int i = 0; i < 3; i++) {
			AnimationDef def = new PikachuDef(gl);
			int x = FinalProj.random.nextInt(1500) + 1000;
			int y = FinalProj.random.nextInt(750) + 1000;
			while (bgDef.getCollision(x / bgDef.getBGWidth(), y / bgDef.getBGHeight()) == 1) {
				x = FinalProj.random.nextInt(1500) + 1000;
				y = FinalProj.random.nextInt(750) + 1000;
			}
			EnemyData enemy = new EnemyData(def, x, y, FinalProj.random.nextInt(2), true, bgDef);
			addEnemy(enemy);
			bucket.addSprite(enemy);
			System.out.println("Pika " + i + " is ready");
		}

		for (int i = 0; i < 3; i++) {
			AnimationDef def = new CharizardDef(gl);
			int x = FinalProj.random.nextInt(1500) + 1000;
			int y = FinalProj.random.nextInt(750) + 1000;
			while (bgDef.getCollision(x / bgDef.getBGWidth(), y / bgDef.getBGHeight()) == 1) {
				x = FinalProj.random.nextInt(1500) + 1000;
				y = FinalProj.random.nextInt(750) + 1000;
			}
			EnemyData enemy = new EnemyData(def, x, y, FinalProj.random.nextInt(2), true, bgDef);
			addEnemy(enemy);
			bucket.addSprite(enemy);
			System.out.println("Chari " + i + " is ready");
		}
	}

	public Boolean allDead() {
		for (AnimationData enemy : enemyList) {
			if (enemy.isAlive()) {
				return false;
			}
		}
		return true;
	}

	public void cleanUp() {
		enemyList.removeAll(enemyList);
		enemyListStore.removeAll(enemyListStore);
	}
}
