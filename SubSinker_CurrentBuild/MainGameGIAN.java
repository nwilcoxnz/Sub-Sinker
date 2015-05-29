import java.awt.*;
import java.awt.event.*;

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
	
	// Game State Enum
	enum GameState {Menu, Options, Help, Play};
	GameState state = GameState.Menu;

	// Difficulty Enum
	enum Difficulty {Cadet, Sailor, Veteran};
	Difficulty difficulty = Difficulty.Cadet;

	// Used to show current menu selection
	int menuOption = 0;

	// Menu Images
	Image spritesheet, background, selectionImage, helpMenu, optionsMenu, selectionOp;

	// Need to hold a reference to ship for inputs
	Ship ship;
	
	// Background object (for animation)
	Background gameBackground;

	// Array List that holds all the game objects
	// Currently Public static so the ship class can add projectiles to the array. Need to find a btter way of doing this.
	//private ArrayList<GameObject> gameObjects;
	
	// Determines the number of submarines based on Difficulty
	private int subCount = 0;
	
	// Game Settings
	boolean gameOver;
	boolean soundOn = true;
	
	// TODO: Update in main file
	// Game Level Variables
	public GameLevel level;
	private int currLevel;
	
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// INITIALISE FUNCTION
//--------------------------------------------------------------------------------------
	// TODO: Update in main file
	public void init() {
		// Load menu images
		background = loadImage("Images/menuBackground.png");
		selectionImage = loadImage("Images/selection.png");
		helpMenu = loadImage("Images/helpMenu.png");
		optionsMenu = loadImage("Images/optionsMenu.png");
		selectionOp = loadImage("Images/selectionOp.png");

		gameOver = false;
		
		// Sets the current level
		currLevel = 1;
		
		// Sets the GameLevel class and the number of levels
		level = new GameLevel(this, 5);
		
		InitialiseLevel();
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// UPDATE LOOP
//--------------------------------------------------------------------------------------
	// TODO: Update in the main file
	public void update(double dt) {
		if(state == GameState.Play) {
			// Update the level
			level.UpdateLevel(dt);
			
			// Check if ship(player) is still alive
			if(!ship.isAlive) { gameOver = true; }
			
			// Check enemy count
			if(level.GetEnemyCount() < 1) {
				// End the current level
				level.EndLevel();
				if(currLevel < level.GetMaxLevel()) {
					// Reinitialise if there are more levels
					currLevel++;
					if(currLevel == level.GetMaxLevel()) { level.SetBossLevel(); }
					InitialiseLevel();
				} else {
					// ...return to Main Menu if there are no more levels
					gameOver = true;
				}
			}
		}
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// PAINT COMPONENT & DRAW FUNCTIONS
//--------------------------------------------------------------------------------------	
	public void paintComponent() {
		// Clear the background to black
		changeBackgroundColor(black);
		clearBackground(WINDOW_RES_X, WINDOW_RES_Y);
		
		// Set Window Properties (title and size)
		frame.setTitle("Sub Sinker");
		setWindowSize(WINDOW_RES_X, WINDOW_RES_Y);
		
		//drawImage(gameBackground, 0, 0, WINDOW_RES_X, WINDOW_RES_Y);
		// Menu
		if(state == GameState.Menu) {
			// Show Main Menu
			drawMenu();
		} else if(state == GameState.Options) {
			// Show Options Menu
			drawOptions();
		}else if(state == GameState.Help) {
			drawHelp();
		} else if(state == GameState.Play) {
			drawGame();
		}
	}

	// TODO: Update in main file
	public void drawGame() {
		clearBackground(WINDOW_RES_X, WINDOW_RES_Y);
		
		level.DrawLevel();
	}

	public void drawHelp() {
		drawImage(helpMenu,0, 0, 1024, 768);
	}

	public void drawMenu() {
		int fontSize = 40;
		//Color fontColor = new Color(199, 187, 175);
		
		drawImage(background, 0, 0, WINDOW_RES_X, WINDOW_RES_Y);

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
		drawImage(optionsMenu, 0, 0, 1024, 768);
		//System.out.println(menuOption);

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
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------
// INPUT / EVENT HANDLERS
//--------------------------------------------------------------------------------------
	public void keyPressed(KeyEvent e) {
		if(state == GameState.Menu) {
			keyPressedMenu(e);
		} else if(state == GameState.Options) {
			keyPressedOptions(e);
		} else if(state == GameState.Play) {
			keyPressedPlay(e);
		}
		else if (state == GameState.Help) {
			keyReleasedHelp(e);
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
			if(menuOption == 0) {state = GameState.Play; engine.init();}
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

	public void keyPressedPlay(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)  {ship.left  = true;}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {ship.right = true;}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {ship.FireTorpedo();}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){state = GameState.Menu;}
	}

	public void keyReleased(KeyEvent e) {
		if(state == GameState.Play) {
			keyReleasedPlay(e);
		}

		if(state == GameState.Help) {
			drawHelp();
		}
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
	// TODO: Update in the main file
	public void InitialiseLevel(){
		// Set the difficulty
		if (difficulty == Difficulty.Cadet) { subCount = 5; }
		if (difficulty == Difficulty.Sailor) { subCount = 10; }
		if (difficulty == Difficulty.Veteran) { subCount = 15; }
		
		// Initialise the player and other game objects
		ship = new Ship(this);
		level.levelObjects.add(ship);
		level.InitialiseLevel(subCount);
		
		// Create special submarines at a certain point in the game
		if(currLevel > (level.GetMaxLevel()*.6)) {
			level.levelObjects.add(new Submarine(this, 1.5, 20));
			level.levelObjects.add(new Submarine(this, 1.5, 20));
		}
		
		System.out.println(currLevel);
	}
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------
}