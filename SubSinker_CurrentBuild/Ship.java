import java.util.*;

public class Ship extends GameObject {
	
	// Movement
	public boolean left, right;
	private boolean isFacingRight = true;
	
	// Weapon
	private ArrayList<Weapon> shipTorpedo;
	public int weaponLimit;
	
	public Ship(MainGame e){
		// Allow alterations to the game engine
		myEngine = e;
		
		// Load Image 
		myCurrentImage = e.loadImage(MainGame.imageDir + "Boat.png");
		
		// Set dimensions
		SetDimensions(120, 75);
		
		// Set position (center of the screen)
		SetStartPosition((MainGame.WINDOW_RES_X / 2) - (myDimensions.x / 2), 150);
		
		// Set velocity
		SetVelocity(0, 0);
		
		// Make sure it is displayed and not deleted
		isAlive = true;
		
		// Initialise Gameplay Properties
		InitialiseCollision();
		SetHealth(5);
		
		// Initialise the torpedo array
		shipTorpedo = new ArrayList<Weapon>();
		weaponLimit = 6;
		
		// Add the torpedoes
		for(int i = 0; i < weaponLimit; i++) {
			shipTorpedo.add(new FriendlyTorpedo(myEngine, GetCollisionBox().getCenterX(), GetCollisionBox().getMaxY()));
		}
	}
	
	@Override
	public void Update(double dt) {
		//------------------- Control the Ship's movement and orientation -------------------//
		double direction = 0, decay = 3;
		
		// If the left button is being pressed
		if(left) {
			// Flip the ship if it is facing right
			if (isFacingRight) { FlipImage(); isFacingRight = false; }
			// Set the direction of the ship
			direction = -1;
		}
		
		// If the right button is being pressed
		if(right) {
			// Flip the ship if it is facing left
			if (!isFacingRight) { FlipImage(); isFacingRight = true; }
			// Set the direction of the ship
			direction = 1;
		}
		
		// Slow the ship down if it is not being accelerated (no button is being pressed)
		if (!left && myVelocity.x < 0) { myVelocity.x += decay; }
		if (!right && myVelocity.x > 0) { myVelocity.x -= decay; }
		
		// Move the ship
		if (WithinBounds()) {
			if (myVelocity.x == 0) { myVelocity.x = 10 * direction; }
			else { myVelocity.x += (5 * direction); }
			
			// Move the ship if it is within the screen
			myPosition.x += myVelocity.x * dt;
		} else {
			// Stop the ship if it goes outside the bounds of the screen...
			myVelocity.x = 0;
			// ...then reposition the ship (cushion effect)
			if (myPosition.x < 0) myPosition.x = 0;
			if (myPosition.x + myDimensions.x > MainGame.WINDOW_RES_X) myPosition.x = MainGame.WINDOW_RES_X - myDimensions.x;
		}
	}
	
	/**
	 * Function for keeping track of ammo count
	 * @return number of available ammo
	 */
	public int GetWeaponCount() {
		int retVal = 0;
		for(Weapon wp : shipTorpedo) {
			if(!wp.isAlive) { retVal++; }
		}
		return retVal;
	}
	
	/**
	 * Function for firing ship weapons
	 */
	public void FireTorpedo() {
		ArrayList<GameObject> tempArray = myEngine.level.levelObjects;
		// Checks if the ship has an active torpedo
		for(Weapon wp : shipTorpedo) {
			if(!tempArray.contains(wp) && !wp.isAlive) {
				// Fires a new torpedo if there are none active
				wp.ChangePosition(GetCollisionBox().getCenterX(), GetCollisionBox().getMaxY());
				wp.isAlive = true;
				tempArray.add(wp);
				return;
			}
		}
	}
}

