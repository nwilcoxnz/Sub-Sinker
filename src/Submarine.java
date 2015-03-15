import java.util.*;

public class Submarine extends GameObject {
	
	// Image and Sprites
	private String subImage = "SharkSub.png";
	private String spriteImage = "SharkSubSprite.png";
	
	// Weapon Properties
	protected ArrayList<Weapon> subTorpedo;
	public int weaponLimit;
	
	/**
	 * Default Constructor
	 * @param setEngine - Game Engine class
	 */
	public Submarine(MainGame setEngine){
		myEngine = setEngine;
		InitSub();
	}
	
	/**
	 * Create a Submarine with a set size
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 */
	public Submarine(MainGame setEngine, double size) {
		myEngine = setEngine;
		InitSub();
		AlterSize(size);
	}
	
	/**
	 * Create a Submarine with a set size and dept
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 * @param y - depth
	 */
	public Submarine(MainGame setEngine, double size, double y) {
		myEngine = setEngine;
		InitSub();
		AlterSize(size);
		SetStartPosition(0, MainGame.WINDOW_RES_Y - myDimensions.y);
	}
	
	/**
	 * Create a Submarine with set size and health
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 * @param sethealth - submarine health
	 */
	public Submarine(MainGame setEngine, double size, int sethealth) {
		myEngine = setEngine;
		InitSub();
		AlterSize(size);
		SetHealth(sethealth);
	}
	
	/**
	 * Create a Submarine with set size, health, and depth
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 * @param sethealth - submarine health
	 * @param y - submarine depth
	 */
	public Submarine(MainGame setEngine, double size, int sethealth, double y) {
		this(setEngine, size, sethealth);
		myPosition.y = y;
	}
	
	// TODO
	/**
	 * Function to initialise common submarine variables
	 */
	private void InitSub() {
		// Set the default image and sprite
		myCurrentImage = myEngine.loadImage(MainGame.imageDir + subImage);
		mySprite = myEngine.loadImage(MainGame.imageDir + spriteImage);
		myAnimation = new Animation(myEngine, 1, 14, mySprite, new Vector2D(4,4));
		flip = false;
		
		// Dimension (h, w)
		//SetDimensions(180, 100);
		SetDimensions(120, 120);
		
		// Position (x, y)
		double randomX = GenerateRandom(0, MainGame.WINDOW_RES_X - myDimensions.x - 100);
		double randomY = GenerateRandom(MainGame.WINDOW_RES_Y / 2, MainGame.WINDOW_RES_Y - myDimensions.y);
		SetStartPosition(randomX, randomY);
		
		// Velocity (vx, vy)
		SetVelocity(GenerateRandom(100, 200), 0);
		
		// Initialise object properties
		InitialiseCollision();
		SetHealth(1);
		isAlive = true;
		
		// Initialise Weapon Properties
		subTorpedo = new ArrayList<Weapon>();
		SetWeaponCount(1);
	}

	@Override
	public void Update(double dt) {
		// Update the position of the sub
		myPosition.x += myVelocity.x * dt;
		
		// Submarine bounces at the edge of the screen
		if (!WithinBounds()) {
			if (myPosition.x < 0) {
				myPosition.x = 0;
				flip = false;
			}
			if (myPosition.x + myDimensions.x > MainGame.WINDOW_RES_X) {
				myPosition.x = MainGame.WINDOW_RES_X - myDimensions.x;
				flip = true;
			}
			myVelocity.x *= -1;
		}
		
		if(!isAlive) { myEngine.level.levelObjects.add(new Explosion(myEngine, myPosition.x - 20, myPosition.y, 2)); }
		
		myAnimation.Play();
	}
	
	//----------- BEHAVIOUR METHODS -----------
	// TODO
	/**
	 * Function that changes the current ammunition count
	 * @param count - new ammo count
	 */
	public void SetWeaponCount(int count) {
		weaponLimit = count;
		subTorpedo.clear();
		// Add the torpedoes
		for(int i = 0; i < weaponLimit; i++) {
			subTorpedo.add(new EnemyTorpedo(myEngine, GetCollisionBox().getCenterX(), myPosition.y));
		}
	}
	
	// TODO
	/**
	 * Creates a new Ammunition object in the position
	 * of the Submarine
	 */
	public void FireMine() {
		ArrayList<GameObject> tempArray = myEngine.level.levelObjects;
		// Checks if the submarine has an active torpedo
		for(Weapon wp : subTorpedo) {
			if(!tempArray.contains(wp) && !wp.isAlive) {
				// Fires a new torpedo if there are none active
				wp.ChangePosition(GetCollisionBox().getCenterX(), myPosition.y);
				wp.isAlive = true;
				tempArray.add(wp);
				return;
			}
		}
	}
}
