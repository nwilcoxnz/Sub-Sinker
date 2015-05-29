import java.util.*;

public abstract class Weapon extends GameObject {
	
	private boolean isTracking;
	
	// Default constructor
	Weapon(MainGame e){}
	
	// Constructor
	Weapon(MainGame e, double xPos, double yPos){
		
		// Allow alterations to the game engine
		myEngine = e;
		
		// Set dimensions
		//SetDimensions(10, 30);
		SetDimensions(15, 15*3);
		
		// Set position
		// x and y will be the same as the ship, and will be passed through as such 
		double x = xPos;
		double y = yPos; 
		SetStartPosition(x, y);
		
		// Set velocity
		SetVelocity(0,100);
		
		// Initialise Gameplay Properties
		InitialiseCollision();
		SetHealth(1);
		isTracking = false;
		isAlive = false;
	}
	
	// TODO: Implement weapon tracking
	/**
	 * Function to set if the weapon will track a target
	 */
	public void SetTracking() {
		isTracking = true;
	}
	
	/**
	 * Function to get if the weapon will track a target
	 * @return
	 */
	public boolean Tracking() {
		return isTracking;
	}
	
	/**
	 * Function to make the weapon track a target
	 * @param dt - game time
	 */
	public void FollowTarget(double dt) {
		ArrayList<GameObject> objArray = myEngine.level.levelObjects;
		double relativeX = 0;
		
		// Search the objects array for Ship classes
		for(GameObject obj : objArray) {
			if(obj instanceof Ship) {
				// Get the center of the Ship object
				relativeX = obj.GetCollisionBox().getCenterX();
			}
		}
		
		// Weapon travels faster
		myPosition.y -= (myVelocity.y + 100) * dt;
		// Weapon tracks the target x coordinate
		if(myPosition.x < relativeX) {
			myPosition.x += (myVelocity.y) * dt;
		}
		if(myPosition.x > relativeX) {
			myPosition.x -= (myVelocity.y) * dt;
		}
	}
}
