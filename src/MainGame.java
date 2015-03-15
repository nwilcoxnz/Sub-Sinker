/**
 * SUB SINKER
 * 
 * 
 * 
 * Samuel van der Kolk 12186223
 * Gian Guison 12165471
 * Philip Plant 07009860
 * Nicole Wilcox 12067844
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MainGame extends GameEngine {
//--------------------------------------------------------------------------------------
// MAIN FUNCTION
//--------------------------------------------------------------------------------------
	public static void main(String args[]) {
		engine = new MainGame();
		engine.init();
		engine.gameLoop(60);
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// GAME VARIABLES
//--------------------------------------------------------------------------------------
	// Game Engine
	private static GameEngine engine;
	
	// Screen Size
	public static int WINDOW_RES_X = 1024;
	public static int WINDOW_RES_Y = 768;
	
	// Image Directory
	public static final String imageDir = "Images/";
	
	// TODO
	// Game State Enum
	enum GameState {Menu, Options, Help, Play, GameOver, Pause};
	GameState state = GameState.Menu;

	// Difficulty Enum
	enum Difficulty {Cadet, Sailor, Veteran};
	Difficulty difficulty = Difficulty.Cadet;

	// Used to show current menu selection
	int menuOption = 0;

	// Menu Images
	Image spritesheet, menuBackground, selectionImage, helpMenu, optionsMenu, gameOverMenu, selectionOp;
	Image scoreBoard, health5, health4, health3, health2, health1;
	Image ammo6, ammo5, ammo4, ammo3, ammo2, ammo1, reloading;

	// Need to hold a reference to ship for inputs
	Ship ship;
	
	// Background object (for animation)
	Background gameBackground;

	// Used to track the players score
	static int score = 0;
	int highScore1 = 1, highScore2 = 2, highScore3 = 3;
	
	// Determines the number of submarines based on Difficulty
	private int subCount = 0;
	
	// Game Settings
	boolean soundOn = true;
	
	// TODO
	// Game Level Variables
	public GameLevel level;
	
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// INITIALISE FUNCTION
//--------------------------------------------------------------------------------------
	// TODO
	public void init() {
		// Load menu images
		menuBackground = loadImage("Images/menuBackground.png");
		selectionImage = loadImage("Images/selection.png");
		helpMenu = loadImage("Images/helpMenu.png");
		optionsMenu = loadImage("Images/optionsMenu.png");
		selectionOp = loadImage("Images/selectionOp.png");
		gameOverMenu = loadImage("Images/gameOver.png");
		
		loadHUDImages();
		
		// Sets the GameLevel class and the number of levels
		level = new GameLevel(this, 5);
		
		// Load in user data
		difficulty = Difficulty.Cadet;
		loadUserData();
		
		InitialiseLevel();
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// UPDATE LOOP
//--------------------------------------------------------------------------------------
	public void update(double dt) {
		if(state == GameState.Play && state != GameState.Pause) {
			// Update the level
			level.UpdateLevel(dt);
			
			// Check if ship(player) is still alive
			if(!ship.isAlive) { 
				state = GameState.GameOver;
				saveUserData();}
			// Check enemy count
			if(level.GetEnemyCount() < 1) {
				// End the current level
				level.EndLevel();
				if(level.currentLevel < level.maxLevel) {
					// Reinitialise if there are more levels
					level.currentLevel++;
					if(level.currentLevel == level.maxLevel) { level.SetBossLevel(); }
					InitialiseLevel();
				} else {
					// ...return to Main Menu if there are no more levels
					state = GameState.GameOver;
					saveUserData();
				}
			}
		}
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// PAINT COMPONENT & DRAW FUNCTIONS
//--------------------------------------------------------------------------------------	
	// TODO
	public void paintComponent() {
		// Clear the background to black
		changeBackgroundColor(black);
		clearBackground(WINDOW_RES_X, WINDOW_RES_Y);
		
		// Set Window Properties (title and size)
		frame.setTitle("Sub Sinker");
		setWindowSize(WINDOW_RES_X, WINDOW_RES_Y);
		
		// Menu
		if(state == GameState.Menu) {
			// Show Main Menu
			drawMenu();
			score = 0;
		} else if(state == GameState.Options) {
			// Show Options Menu
			drawOptions();
		}else if(state == GameState.Help) {
			drawHelp();
		} else if(state == GameState.Play || state == GameState.Pause) {
			drawGame();
			if(state == GameState.Pause) { drawPause(); }
		} else if (state == GameState.GameOver) {
			drawGameOver();
		}
	}
	
	public void drawGame() {
		clearBackground(WINDOW_RES_X, WINDOW_RES_Y);
		
		level.DrawLevel();
		
		if(ship.GetHealth() == 5) {
			drawImage(health5, 737, 20, 267, 84);
		} else if (ship.GetHealth() == 4) {
			drawImage(health4, 737, 20, 267, 84);
		} else if (ship.GetHealth() == 3) {
			drawImage(health3, 737, 20, 267, 84);
		} else if (ship.GetHealth() == 2) {
			drawImage(health2, 737, 20, 267, 84);
		}else if (ship.GetHealth() == 1) {
			drawImage(health1, 737, 20, 267, 84);
		}

		if(ship.GetWeaponCount() == 6) {
			drawImage(ammo6, 387, 20, 267, 84);
		} else if (ship.GetWeaponCount() == 5) {
			drawImage(ammo5, 387, 20, 267, 84);
		} else if (ship.GetWeaponCount() == 4) {
			drawImage(ammo4, 387, 20, 267, 84);
		} else if (ship.GetWeaponCount() == 3) {
			drawImage(ammo3, 387, 20, 267, 84);
		}else if (ship.GetWeaponCount() == 2) {
			drawImage(ammo2, 387, 20, 267, 84);
		}else if(ship.GetWeaponCount() == 1) {
			drawImage(ammo1, 387, 20, 267, 84);
		}else {
			drawImage(reloading, 387, 20, 267, 84);
		}	

		drawImage(scoreBoard, 20, 20, 267, 84);
		changeColor(white);
		drawText(200, 85 , Integer.toString(score), 60);
	}

	public void drawHelp() {
		drawImage(helpMenu,0, 0, WINDOW_RES_X, WINDOW_RES_Y);
	}
	
	public void drawGameOver() {
		drawImage(gameOverMenu,0, 0, WINDOW_RES_X, WINDOW_RES_Y);

		changeColor(white);
		drawText(480, (WINDOW_RES_Y/2 - 20), Integer.toString(score), 150);
		drawText(WINDOW_RES_X/2, 545, Integer.toString(highScore1), 45);
		drawText(WINDOW_RES_X/2, 590, Integer.toString(highScore2), 45);
		drawText(WINDOW_RES_X/2, 635, Integer.toString(highScore3), 45);
	}

	public void drawMenu() {
		int fontSize = 40;
		//Color fontColor = new Color(199, 187, 175);
		drawImage(menuBackground, 0, 0, WINDOW_RES_X, WINDOW_RES_Y);

		// Play
		if(menuOption == 0) {
			changeColor(white);
			drawImage(selectionImage, 100, 200, 412, 76);
			drawText(150, 250, "Play", fontSize);
		} else {
			changeColor(150, 150, 150);
			drawText(150, 250, "Play", fontSize);
		}

		// Options
		if(menuOption == 1) {
			changeColor(white);
			drawImage(selectionImage, 100, 275, 412, 76);
			drawText(150, 325, "Options", fontSize);
		} else {
			changeColor(150, 150, 150);
			drawText(150, 325, "Options", fontSize);
		}

		// Help
		if(menuOption == 2) {
			changeColor(white);
			drawImage(selectionImage, 100, 350, 412, 76);
			drawText(150, 400, "Help", fontSize);
		} else {
			changeColor(150, 150, 150);
			drawText(150, 400, "Help", fontSize);
		}

		// Exit
		if(menuOption == 3) {
			changeColor( white);
			drawImage(selectionImage, 100, 575, 412, 76);
			drawText(150, 625, "Quit", fontSize);
		} else {
			changeColor(150, 150, 150);
			drawText(150, 625, "Quit", fontSize);
		}
	}

	public void drawOptions() {
		drawImage(optionsMenu, 0, 0, WINDOW_RES_X, WINDOW_RES_Y);

		// Cadet
		if(menuOption == 0) {
			//155
			drawImage(selectionOp, 135, 275, 259, 68);
			changeColor(white);
			drawText(200, 325, "Cadet", 45);
			difficulty = Difficulty.Cadet;
		} else {
			changeColor(150, 150, 150);
			drawText(200, 325, "Cadet", 45);
		}

		// Sailor
		if(menuOption == 1) {
			changeColor(white);
			drawImage(selectionOp, 375, 275, 259, 68);
			drawText(440, 325, "Sailor", 45);
			difficulty = Difficulty.Sailor;
		} else {
			changeColor(150, 150, 150);
			drawText(440, 325, "Sailor", 45);
		}

		// Veteran
		if(menuOption == 2) {
			changeColor(white);
			drawImage(selectionOp, 605, 275, 259, 68);
			drawText(650, 325, "Veteran", 45);
			difficulty = Difficulty.Veteran;
		} else {
			changeColor(150, 150, 150);
			drawText(650, 325, "Veteran", 45);
		}

		// Sound On
		if(soundOn) {
			changeColor(white);
			drawImage(selectionOp, 225, 525, 259, 68);
			drawText(250, 575, "Sound On", 45);
		} else {
				changeColor(150, 150, 150);
				drawText(250, 575, "Sound On", 45);
		}

		// Sound Off
		if(!soundOn) {
			changeColor(white);
			drawImage(selectionOp, 555, 525, 259, 68);
			drawText(575, 575, "Sound Off", 45);
		} else {
				changeColor(150, 150, 150);
				drawText(575, 575, "Sound Off", 45);
		}
	}
	
	// TODO
	public void drawPause() {
		changeColor(white);
		drawText(WINDOW_RES_X/3, WINDOW_RES_Y/2, "Game Paused", 50);
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// INPUT / EVENT HANDLERS
//--------------------------------------------------------------------------------------
	// TODO
	public void keyPressed(KeyEvent e) {
		if(state == GameState.Menu) {
			keyPressedMenu(e);
		} else if(state == GameState.Options) {
			keyPressedOptions(e);
		} else if(state == GameState.Play) {
			keyPressedPlay(e);
		} else if (state == GameState.Help) {
			keyReleasedHelp(e);
		} else if (state == GameState.GameOver) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
				state = GameState.Menu; 
				saveUserData();
				score = 0;}
		} else if (state == GameState.Pause) {
			keyPressedPaused(e);
		}
	}

	public void keyPressedMenu(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(menuOption != 0) {menuOption--;}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(menuOption != 3) {menuOption++;}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(menuOption == 0) {
				state = GameState.Play; 
				engine.init();
				// If the user has enabled sounds play a background noise
				if (soundOn){ playSoundFileLoop("SubmarineLong.wav", -20);}}
			if(menuOption == 1) {state = GameState.Options; menuOption = 0;
				if(difficulty == Difficulty.Cadet){menuOption = 0;}
				if(difficulty == Difficulty.Sailor){menuOption = 1;}
				if(difficulty == Difficulty.Veteran){menuOption = 2;}
			}
			else if(menuOption == 2) {state = GameState.Help; menuOption = 0;} 
			else if(menuOption == 3) {System.exit(1);}
		}
	}

	public void keyPressedOptions(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {

			if(menuOption == 0){difficulty = Difficulty.Cadet;}
			if(menuOption == 1){difficulty = Difficulty.Sailor;}
			if(menuOption == 2){difficulty = Difficulty.Veteran;}

			state = GameState.Menu; menuOption = 0;
		}

		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(menuOption != 0) {
				menuOption--;
			}
		}

		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(menuOption!= 2) {
				menuOption++;
			}
		}	

		if(e.getKeyCode() == KeyEvent.VK_S) {
			if(soundOn){soundOn = false;}
			else {soundOn = true;}
		}
	}

	// TODO
	public void keyPressedPlay(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)  {ship.left  = true;}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {ship.right = true;}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {ship.FireTorpedo();}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){state = GameState.Menu;}
		if(e.getKeyCode() == KeyEvent.VK_P)		{state = GameState.Pause;}
	}
	
	// TODO
	public void keyPressedPaused(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_P) { state = GameState.Play; }
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { state = GameState.Menu; }
	}

	public void keyReleased(KeyEvent e) {
		if(state == GameState.Play) { keyReleasedPlay(e); }
		if(state == GameState.Help) { drawHelp(); }
	}

	public void keyReleasedPlay(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)  {ship.left  = false;}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {ship.right = false;}
	}

	public void keyReleasedHelp(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)  {state = GameState.Menu; menuOption = 0;}
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------	


//--------------------------------------------------------------------------------------
// CLASS FUNCTIONS
//--------------------------------------------------------------------------------------	
	// TODO
	public void InitialiseLevel(){
		// Set the difficulty
		if (difficulty == Difficulty.Cadet) { subCount = 5; }
		if (difficulty == Difficulty.Sailor) { subCount = 10; }
		if (difficulty == Difficulty.Veteran) { subCount = 15; }
		
		// Initialise the player and other game objects
		ship = new Ship(this);
		level.levelObjects.add(ship);
		level.InitialiseLevel(subCount);
		
	}
	
	// TODO
	public void loadHUDImages() {
		health5 = loadImage(imageDir + "HUD/health5.png");
		health4 = loadImage(imageDir + "HUD/health4.png");
		health3 = loadImage(imageDir + "HUD/health3.png");
		health2 = loadImage(imageDir + "HUD/health2.png");
		health1 = loadImage(imageDir + "HUD/health1.png");

		scoreBoard = loadImage(imageDir + "HUD/scoreBoard.png");

		ammo6 = loadImage(imageDir + "HUD/ammo6.png");
		ammo5 = loadImage(imageDir + "HUD/ammo5.png");
		ammo4 = loadImage(imageDir + "HUD/ammo4.png");
		ammo3 = loadImage(imageDir + "HUD/ammo3.png");
		ammo2 = loadImage(imageDir + "HUD/ammo2.png");
		ammo1 = loadImage(imageDir + "HUD/ammo1.png");
		
		reloading = loadImage(imageDir + "HUD/reloading.png");
	}
	

	//--------------------------------------------------------------------------------------
	// FILE IO
	//--------------------------------------------------------------------------------------
		public void loadUserData() {
			int i = 0;
			int diff = 0;
			int data = 0;
	        String fileName = "userData.dat";
	        String line = null;

	        try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = new FileReader(fileName);
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	            	data = Integer.parseInt(line);

	            	if(i == 0) {
	            		diff = data;
	            	}else if (i == 1) {
	            		if(data == 1) { soundOn = true;} else {soundOn = false;}
	            	} else if (i == 2) {
	            		highScore1 = data;
	            	} else if (i == 3) {
	            		highScore2 = data;
	            	} else if (i == 4) {
	            		highScore3 = data;
	            	}
	                i++;
	            }    
	            bufferedReader.close();            
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                   
	        }

	        if(diff == 2) {
	        	difficulty = Difficulty.Veteran;
	        }else if (diff == 1) {
	        	difficulty = Difficulty.Sailor;
	        } else {
	        	difficulty = Difficulty.Cadet;
	        }
	    }

		public void saveUserData() {
			int diff;
			int soundPref;
			String content;

			if(difficulty == Difficulty.Veteran) {
				diff = 2;
			} else if (difficulty == Difficulty.Sailor) {
				diff = 1;
			} else {
				diff = 0;
			}

			if(soundOn) {
				soundPref = 1;
			} else {
				soundPref = 0;
			}
			System.out.print("highScore1: " + highScore1);

			// Enter in new score if it is higher than any of the three highest score
			// If the new score is higher than all previous scores
			if (score > highScore1) {
				int temp, temp2;
				temp = highScore1;
				highScore1 = score;
				temp2 = highScore2;
				highScore2 = temp;
				highScore3 = temp2;
			}
			
			// If the new score is bigger than the middle value but smaller than the the biggest score
			if (score < highScore1 && score > highScore2) {
				int temp;
				temp = highScore2;
				highScore2 = score;
				highScore3 = temp;
			}
			
			// If the new score is higher than only the lowest score
			if (score < highScore1 && score < highScore2 && score > highScore3) {
				highScore3 = score;
			}
			System.out.print("highScore1: " + highScore1);

			System.out.print("Difficulty: ");
			System.out.print(diff);
			content = Integer.toString(diff) + "\n" + Integer.toString(soundPref) + "\n" + Integer.toString(highScore1) + "\n" + Integer.toString(highScore2) + "\n" + Integer.toString(highScore3);
		
			try {
		    	File file = new File("userData.dat");
		    	FileOutputStream outputStream = null;
		    	outputStream = new FileOutputStream(file);

		    	file.createNewFile();
		    	byte[] contentInBytes = content.getBytes();

		    	outputStream.write(contentInBytes);
		    	outputStream.flush();
		    	outputStream.close();
		    }
	    	catch (IOException e) {

			}
		}

	//--------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------		
	}