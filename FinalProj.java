package CS134FinalAssignment;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class FinalProj {

	static int WINDOW_WIDTH = 1600;
	static int WINDOW_HEIGHT = 1200;
	final static int END_GAME_AT = 5000;
	static Random random = new Random();

	// Game status
	private static boolean startGame = false, 
			enterMenu = false, 
			characterChose = false, 
			character2Chose = false, 
			gameDone = false, 
			gameModeChose = false, 
			vsCom = true,
			gamePause = false;
	private static int endGameCount = END_GAME_AT;
	// Set this to true to make the game loop exit.
	private static boolean shouldExit;

	// The previous frame's keyboard state.
	static boolean kbPrevState[] = new boolean[256];

	// The current frame's keyboard state.
	static boolean kbState[] = new boolean[256];
	static boolean kbStatePending[] = new boolean[256];

	// Texture for the Background.
	private static int tree, grass, flower;
	private static int pokemonLogo;
	private static int[] logoSize;
	private static int[] tileArray;

	// Size of the sprite.
	private static int[] backgroundSize = new int[2];

	// Animation Data
	private static BackgroundDef bgDef;
	private static ArrayList<AnimationDef> defList;
	private static MainData main, main2;
	private static CharacterControl control1, control2;
	public static Buckets buckets;
	private static EnemyList enemies = new EnemyList();

	public static Camera camera;

	public static FontDef fonts;
	// private static int jumpTime = 0, originalY;
	// private static boolean isJumping = false;

	public static void main(String[] args) {
		GLProfile gl2Profile;
		try {
			// Make sure we have a recent version of OpenGL
			gl2Profile = GLProfile.get(GLProfile.GL2);
		} catch (GLException ex) {
			System.out.println("OpenGL max supported version is too low.");
			System.exit(1);
			return;
		}

		// Create the window and OpenGL context.
		GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setTitle("CS 134 Final Assignment");
		window.setVisible(true);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbStatePending[keyEvent.getKeyCode()] = true;
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbStatePending[keyEvent.getKeyCode()] = false;
			}
		});

		// Setup OpenGL state.
		window.getContext().makeCurrent();
		GL2 gl = window.getGL().getGL2();
		gl.glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 0, 100);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		// Game initialization goes here.
		// Initializing Background
		bgDef = new BackgroundDef();
		grass = glTexImageTGAFile(gl, "background/grass.tga", backgroundSize);
		tree = glTexImageTGAFile(gl, "background/tree.tga", backgroundSize);
		flower = glTexImageTGAFile(gl, "background/flower.tga", backgroundSize);

		tileArray = new int[bgDef.getTileSize()];
		for (int i = 0; i < bgDef.getWidth(); i++) {
			for (int j = 0; j < bgDef.getHeight(); j++) {
				int index = j * bgDef.getWidth() + i;
				if (bgDef.getTile(i, j) == 0) {
					tileArray[index] = grass;
				} else if (bgDef.getTile(i, j) == 1) {
					tileArray[index] = tree;
				} else if (bgDef.getTile(i, j) == 2) {
					tileArray[index] = flower;
				}
			}
		}
		bgDef.setBGWidth(backgroundSize[0]);
		bgDef.setBGHeight(backgroundSize[1]);
		camera = new Camera(0, 0, bgDef);
		fonts = new FontDef(gl);

		// Initializing sprite
		buckets = new Buckets(bgDef);

		defList = new ArrayList<>();
		CharizardDef charizard = new CharizardDef(gl);
		PikachuDef pikachu = new PikachuDef(gl);
		defList.add(pikachu);
		defList.add(charizard);
		enemies = new EnemyList();

		logoSize = new int[2];
		pokemonLogo = glTexImageTGAFile(gl, "Pokémon_logo.tga", logoSize);
		
		//Sound soundMain = Sound.loadFromFile("Pokémon_Theme_Song.wav");
		//soundMain.playLooping();
		// The game loop
		long lastFrameNS;
		long curFrameNS = System.nanoTime();
		while (!shouldExit) {
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
			System.arraycopy(kbStatePending, 0, kbState, 0, kbState.length);
			lastFrameNS = curFrameNS;
			curFrameNS = System.nanoTime();
			long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;

			// Actually, this runs the entire OS message pump.
			window.display();

			if (kbState[KeyEvent.VK_ESCAPE]) {
				if (startGame && !gameDone) {
					gamePause = true;
				} else {
					shouldExit = true;
				}
			}

			if (!window.isVisible()) {
				shouldExit = true;
				break;
			}

			if (gamePause) {
				pauseScreen(gl, camera.getCameraWidth() / 2);
				continue;
			}
			
			if (!startGame) {
				drawMenu(gl);
				int middle = camera.getCameraWidth() / 2;
				if (!enterMenu) {
					startScreen(gl, middle);
				} else if (!gameModeChose) {
					gameModeScreen(gl, middle);
				} else if (!characterChose) {
					chooseCharacterScreen(gl, middle);
				} else if (vsCom) {
					chooseComDifficulty(gl, middle);
				} else {
					chooseSecondCharacterScreen(gl, middle);
				}
				continue;
			}


			// Game logic goes here.
			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

			// Draw background
			drawMap(gl);
			
			// Check projectile collision and out of bounds
			checkProjColli();

			if (vsCom) {
				if (main.isAlive()) {
					control1.controlCharacter(deltaTimeMS, gl);
					camera.intelligentCamera1(deltaTimeMS, main);
				}
				for (EnemyData enemy : enemies.getEnmeyList()) {
					if (enemy.isAlive()) {
						enemy.nextDecision(main, deltaTimeMS, gl);
					}
				}
			} else {
				if (main.isAlive()) {
					control1.controlCharacter(deltaTimeMS, gl);
				}

				if (main2.isAlive()) {
					control2.controlCharacter(deltaTimeMS, gl);
				}
				camera.intelligentCamera2(deltaTimeMS, main, main2);
			}

			for (AnimationData character : buckets.getSpriteList()) {
				character.update(deltaTimeMS);
			}

			for (AnimationData character : buckets.getSpriteAppeared()) {
				//if (character.getAttacks().isEmpty()) continue;
				for (AttackType p : character.getAttacks())
					p.update(deltaTimeMS);
			}

			buckets.Update();
			buckets.drawBuckets(gl);
			for (AnimationData character : buckets.getSpriteAppeared()) {
				if (character.getAttacks().isEmpty())
					continue;
				for (AttackType at : character.getAttacks())
					at.draw(gl);
			}
			main.presentHealth(gl);
			if (!vsCom) {
				main2.presentHealth(gl);
			}

			endGameScreen(gl, deltaTimeMS);
		}

	}

	// Load a file into an OpenGL texture and return that texture.
	public static int glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {
		final int BPP = 4;

		DataInputStream file = null;
		try {
			// Open the file.
			file = new DataInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException ex) {
			System.err.format("File: %s -- Could not open for reading.", filename);
			return 0;
		}

		try {
			// Skip first two bytes of data we don't need.
			file.skipBytes(2);

			// Read in the image type. For our purposes the image type
			// should be either a 2 or a 3.
			int imageTypeCode = file.readByte();
			if (imageTypeCode != 2 && imageTypeCode != 3) {
				file.close();
				System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
				return 0;
			}

			// Skip 9 bytes of data we don't need.
			file.skipBytes(9);

			int imageWidth = Short.reverseBytes(file.readShort());
			int imageHeight = Short.reverseBytes(file.readShort());
			int bitCount = file.readByte();
			file.skipBytes(1);

			// Allocate space for the image data and read it in.
			byte[] bytes = new byte[imageWidth * imageHeight * BPP];

			// Read in data.
			if (bitCount == 32) {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = file.readByte();
				}
			} else {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = -1;
				}
			}

			file.close();

			// Load into OpenGL
			int[] texArray = new int[1];
			gl.glGenTextures(1, texArray, 0);
			int tex = texArray[0];
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0, GL2.GL_BGRA,
					GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

			out_size[0] = imageWidth;
			out_size[1] = imageHeight;
			return tex;
		} catch (IOException ex) {
			System.err.format("File: %s -- Unexpected end of file.", filename);
			return 0;
		}
	}

	public static void glDrawSprite(GL2 gl, int tex, int x, int y, int w, int h) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
		gl.glBegin(GL2.GL_QUADS);
		{
			gl.glColor3ub((byte) -1, (byte) -1, (byte) -1);
			gl.glTexCoord2f(0, 1);
			gl.glVertex2i(x, y);
			gl.glTexCoord2f(1, 1);
			gl.glVertex2i(x + w, y);
			gl.glTexCoord2f(1, 0);
			gl.glVertex2i(x + w, y + h);
			gl.glTexCoord2f(0, 0);
			gl.glVertex2i(x, y + h);
		}
		gl.glEnd();
	}

	private static void drawMenu(GL2 gl) {
		int wRange = Math.floorDiv(camera.getCameraWidth(), bgDef.getBGWidth());
		int hRange = Math.floorDiv(camera.getCameraHeight(), bgDef.getBGHeight());

		for (int i = 0; i <= wRange; i++) {
			for (int j = 0; j <= hRange; j++) {
				int offSetX = i * bgDef.getBGWidth() - camera.getCameraX();
				int offSetY = j * bgDef.getBGHeight() - camera.getCameraY();
				glDrawSprite(gl, grass, offSetX, offSetY, bgDef.getBGWidth(), bgDef.getBGHeight());
			}
		}
		int logoX = (camera.getCameraWidth() / 2) - (logoSize[0] / 2);
		glDrawSprite(gl, pokemonLogo, logoX, 0, logoSize[0], logoSize[1]);
	}
	
	private static void startScreen(GL2 gl, int middle) {
		String prompt = "Press ENTER to start";
		int width = fonts.measureText(prompt);
		fonts.drawText(gl, prompt, middle - (width / 2),
				camera.getCameraHeight() / 2 - (fonts.getLineHeight() / 2));
		if (kbState[KeyEvent.VK_ENTER]) {
			enterMenu = true;
			return;
		}
		enterMenu = false;
	}

	private static void gameModeScreen(GL2 gl, int middle) {
		String gameModePrompt = "Choose game mode";
		int width = fonts.measureText(gameModePrompt);
		fonts.drawText(gl, gameModePrompt, middle - (width / 2), 250);
		String firstChoice = "1. VS COM";
		int width1 = fonts.measureText(firstChoice);
		fonts.drawText(gl, firstChoice, middle - (width1 / 2), 400);
		String secondChoice = "2. VS PLAYER";
		int width2 = fonts.measureText(secondChoice);
		fonts.drawText(gl, secondChoice, middle - (width2 / 2), 550);

		if (kbState[KeyEvent.VK_1] && !kbPrevState[KeyEvent.VK_1]) {
			vsCom = true;
			gameModeChose = true;
		} else if (kbState[KeyEvent.VK_2] && !kbPrevState[KeyEvent.VK_2]) {
			vsCom = false;
			gameModeChose = true;
		}
	}
	
	private static void chooseCharacterScreen(GL2 gl, int middle) {
		String prompt = "Choose your character";
		int width = fonts.measureText(prompt);
		fonts.drawText(gl, prompt, middle - (width / 2), 250);

		String choosePika = "1. " + defList.get(0).getName();
		int pikaWidth = fonts.measureText(choosePika);
		fonts.drawText(gl, choosePika, middle - pikaWidth - 50, 400);
		String chooseChari = "2. " + defList.get(1).getName();
		int chariWidth = fonts.measureText(chooseChari);
		fonts.drawText(gl, chooseChari, middle + 50, 400);
		glDrawSprite(gl, defList.get(0).getIcon().getImage(), middle - pikaWidth * 3 / 4 - 50,
				400 + fonts.getLineHeight(), 150, 150);
		glDrawSprite(gl, defList.get(1).getIcon().getImage(), middle + 50 + chariWidth / 4,
				400 + fonts.getLineHeight(), 150, 150);

		if (kbState[KeyEvent.VK_1] && !kbPrevState[KeyEvent.VK_1]) {
			main = new MainData(defList.get(0), "Player 1", 300, 300, true);
			characterChose = true;
		} else if (kbState[KeyEvent.VK_2] && !kbPrevState[KeyEvent.VK_2]) {
			main = new MainData(defList.get(1), "Player 1", 300, 300, true);
			characterChose = true;
		}

		if (characterChose) {
			buckets.addSprite(main);
		}
		
		if (vsCom) {
			control1 = new CharacterControl(main, bgDef);
		} else {
			control1 = new CharacterControl(true, main, bgDef);
		}
	}
	
	private static void chooseComDifficulty(GL2 gl, int middle) {
		String gameModePrompt = "Difficulty";
		int width = fonts.measureText(gameModePrompt);
		fonts.drawText(gl, gameModePrompt, middle - (width / 2), 250);
		String firstChoice = "Level 1";
		int width1 = fonts.measureText(firstChoice);
		fonts.drawText(gl, firstChoice, middle - (width1 / 2), 400);
		String secondChoice = "Level 2";
		int width2 = fonts.measureText(secondChoice);
		fonts.drawText(gl, secondChoice, middle - (width2 / 2), 550);
		String thirdChoice = "Level 3";
		int width3 = fonts.measureText(thirdChoice);
		fonts.drawText(gl, thirdChoice, middle - (width3 / 2), 700);

		if (kbState[KeyEvent.VK_1] && !kbPrevState[KeyEvent.VK_1]) {
			enemies.level1Enemy(gl, buckets, bgDef);
			startGame = true;
		} else if (kbState[KeyEvent.VK_2] && !kbPrevState[KeyEvent.VK_2]) {
			enemies.level2Enemy(gl, buckets, bgDef);
			startGame = true;
		} else if (kbState[KeyEvent.VK_3] && !kbPrevState[KeyEvent.VK_3]) {
			enemies.level3Enemy(gl, buckets, bgDef);
			startGame = true;
		}
		
	}
	
	private static void chooseSecondCharacterScreen(GL2 gl, int middle) {
		String prompt = "Player 2 choose your character";
		int width = fonts.measureText(prompt);
		fonts.drawText(gl, prompt, middle - (width / 2), 250);

		String choosePika = "1. Pikachu";
		int pikaWidth = fonts.measureText(choosePika);
		fonts.drawText(gl, choosePika, middle - pikaWidth - 50, 400);
		String chooseChari = "2. Charizard";
		int chariWidth = fonts.measureText(chooseChari);
		fonts.drawText(gl, chooseChari, middle + 50, 400);
		glDrawSprite(gl, defList.get(0).getIcon().getImage(), middle - pikaWidth * 3 / 4 - 50,
				400 + fonts.getLineHeight(), 150, 150);
		glDrawSprite(gl, defList.get(1).getIcon().getImage(), middle + 50 + chariWidth / 4,
				400 + fonts.getLineHeight(), 150, 150);

		if (kbState[KeyEvent.VK_1] && !kbPrevState[KeyEvent.VK_1]) {
			main2 = new MainData(defList.get(0), "Player 2", 600, 600, false);
			character2Chose = true;
		} else if (kbState[KeyEvent.VK_2] && !kbPrevState[KeyEvent.VK_2]) {
			main2 = new MainData(defList.get(1), "Player 2", 600, 600, false);
			character2Chose = true;
		}

		if (character2Chose) {
			buckets.addSprite(main2);
			startGame = true;
		}
		control2 = new CharacterControl(false, main2, bgDef);
	}
	
	private static void endGameScreen(GL2 gl, long deltaTimeMS) {
		if (gameDone) {
			if (endGameCount > 0) {
				if (vsCom) {
					if (!main.isAlive()) {
						String end = "Game Over";
						int width = fonts.measureText(end);
						fonts.drawText(gl, end, (camera.getCameraWidth() / 2) - (width / 2),
								camera.getCameraHeight() / 2 - (fonts.getLineHeight() / 2));
					} else if (enemies.allDead()) {
						String win = "Victory!";
						int width = fonts.measureText(win);
						fonts.drawText(gl, win, (camera.getCameraWidth() / 2) - (width / 2),
								camera.getCameraHeight() / 2 - (fonts.getLineHeight() / 2));
					}
				} else {
					String end = " wins!";
					if (!main.isAlive() && main2.isAlive()) {
						end = main2.getDataName() + end;
					} else if (main.isAlive() && !main2.isAlive()) {
						end = "Player 1" + end;
					}
					int width = fonts.measureText(end);
					fonts.drawText(gl, end, (camera.getCameraWidth() / 2) - (width / 2),
							camera.getCameraHeight() / 2 - (fonts.getLineHeight() / 2));
				}
			} else {
				String restart = "Press enter to restart!";
				int width = fonts.measureText(restart);
				fonts.drawText(gl, restart, (camera.getCameraWidth() / 2) - (width / 2),
						camera.getCameraHeight() / 2 - (fonts.getLineHeight() / 2));
				if (kbState[KeyEvent.VK_ENTER]) {
					startGame = false;
					gameDone = false;
					characterChose = false;
					character2Chose = false;
					gameModeChose = false;
					endGameCount = END_GAME_AT;
					camera.resetCamera();
					for (AnimationData data : buckets.getSpriteAppeared()) {
						data.clearAttacks();
					}
					buckets.cleanUp();
					enemies.cleanUp();
				}

			}
			endGameCount -= deltaTimeMS;
		}
	}
	
	private static void pauseScreen(GL2 gl, int middle) {
		String resume = "1. resume", restart = "2. restart", mainMenu = "3. main menu";
		int width = fonts.measureText(resume);
		fonts.drawText(gl, resume, middle - (width / 2), 250);
		width = fonts.measureText(restart);
		fonts.drawText(gl, restart, middle - (width / 2), 400);
		width = fonts.measureText(mainMenu);
		fonts.drawText(gl, mainMenu, middle - (width / 2), 550);
		if (kbState[KeyEvent.VK_1]) {
			gamePause = false;
		} else if (kbState[KeyEvent.VK_2]) {
			restartGame();
			gamePause = false;
		} else if (kbState[KeyEvent.VK_3]) {
			buckets.cleanUp();
			enemies.cleanUp();
			startGame = false;
			characterChose = false;
			character2Chose = false;
			gameModeChose = false;
			vsCom = false;
			gamePause = false;
			camera.reset();
		}
	}
	
	private static void restartGame() {
		if (vsCom) {
			buckets.cleanUp();
			enemies.restore(buckets);
			main.restore();
			buckets.addSprite(main);
		} else {
			buckets.cleanUp();
			main.restore();
			buckets.addSprite(main);
			main2.restore();
			buckets.addSprite(main2);
		}
		camera.reset();
	}
	
	private static void drawMap(GL2 gl) {
		int tileX = Math.floorDiv(camera.getCameraX(), bgDef.getBGWidth());
		int tileY = Math.floorDiv(camera.getCameraY(), bgDef.getBGHeight());
		int wRange = Math.floorDiv(camera.getCameraX() + camera.getCameraWidth(), bgDef.getBGWidth());
		int hRange = Math.floorDiv(camera.getCameraY() + camera.getCameraHeight(), bgDef.getBGHeight());

		for (int i = tileX; i <= wRange; i++) {
			for (int j = tileY; j <= hRange; j++) {
				int index = (j * bgDef.getWidth() + i);
				if (index >= 1600)
					continue;
				int offSetX = i * bgDef.getBGWidth() - camera.getCameraX();
				int offSetY = j * bgDef.getBGHeight() - camera.getCameraY();
				glDrawSprite(gl, tileArray[index], offSetX, offSetY, bgDef.getBGWidth(), bgDef.getBGHeight());
			}
		}
	}

	public static void checkProjColli() {
		// Check if projectile goes off the map or animation done
		projOOB();
		// Check for collision with characters
		if (vsCom) comProjColliChar();
		else projColliChar();
		projColli();	
	}
	
	private static void projOOB() {
		for (AnimationData data : buckets.getSpriteAppeared()) {
			ArrayList<AttackType> check = data.getAttacks();
			for (int i = check.size() - 1; i >= 0; i--) {
				AttackType at = check.get(i);
				if (at.getBox().getXCoor() + at.getBox().getWidth() < 0
						|| at.getBox().getXCoor() > bgDef.getBGWidth() * bgDef.getWidth()) {
					check.remove(at);
				}

				if (at.getCurFrame() == at.getAnimationLength()) {
					check.remove(at);
				}
			}
		}
	}

	private static void comProjColliChar() {
		Boolean isMain = true;
		for (AnimationData player : buckets.getSpriteAppeared()) {
			if (!player.equals(main)) isMain = false;
			else isMain = true;
			ArrayList<AttackType> check = player.getAttacks();
			for (int i = check.size() - 1; i >= 0; i--) {
				AttackType at = check.get(i);
				if (!at.isOut()) continue;
				ArrayList<AnimationData> temp = buckets.getSpriteList();
				for (int j = temp.size() - 1; j >= 0; j--) {
					AnimationData data = temp.get(j);
					// Player will not get hurt by its own projectile
					if (data.equals(player)) continue;
					// Ignore team kill by enemy team
					if (enemies.isEnemy(data) && isMain == false) continue;
					AABB box1 = at.getBox();
					AABB box2 = data.getBox();
					if (AABB.AABBIntersect(box1, box2)) {
						if (!at.needWholeAnimation()) check.remove(at);
						data.decHP(at.damageDealt());
						if (data.getHP() < 1) temp.remove(data);
					}
				}
			}
		}
		if (enemies.allDead() || !main.isAlive())
			gameDone = true;
	}
	
	private static void projColliChar() {
		for (AnimationData player : buckets.getSpriteAppeared()) {
			ArrayList<AttackType> check = player.getAttacks();
			for (int i = check.size() - 1; i >= 0; i--) {
				AttackType at = check.get(i);
				if (!at.isOut()) continue;
				ArrayList<AnimationData> temp = buckets.getSpriteList();
				for (int j = temp.size() - 1; j >= 0; j--) {
					AnimationData data = temp.get(j);
					// Player will not get hurt by its own projectile
					if (data.equals(player)) continue;
					AABB box1 = at.getBox();
					AABB box2 = data.getBox();
					if (AABB.AABBIntersect(box1, box2)) {
						if (!at.needWholeAnimation()) check.remove(at);
						data.decHP(at.damageDealt());
						if (data.getHP() < 1) temp.remove(data);
					}
				}
			}
		}
		if (buckets.getSpriteList().size() <= 1) gameDone = true;
	}

	private static void projColli() {
		for (AnimationData player : buckets.getSpriteAppeared()) {
			ArrayList<AttackType> check = player.getAttacks();
			for (int i = check.size() - 1; i >= 0; i--) {
				AttackType at1 = check.get(i);
				if (!at1.isOut()) continue;
				for (AnimationData data : buckets.getSpriteList()) {
					if (data.equals(player)) continue;
					ArrayList<AttackType> temp = data.getAttacks();
					for (int j = temp.size() - 1; j >= 0; j--) {
						AttackType at2 = temp.get(j);
						if (!at2.isOut()) continue;
						AABB box1 = at1.getBox();
						AABB box2 = at2.getBox();
						if (AABB.AABBIntersect(box1, box2)) {
							check.remove(at1);
							temp.remove(at2);
						}
					}
				}
			}
		}
	}
}
