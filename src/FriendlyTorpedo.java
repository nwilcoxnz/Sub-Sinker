
public class FriendlyTorpedo extends Weapon {
	
	private String torpedoImg = "FriendlyTorpedoSprite.png";
	
	// Constructor
	FriendlyTorpedo(MainGame e, double xPos, double yPos) {
		
		// Call super constructor
		super(e, xPos, yPos);
		 
		// Set image
		myCurrentImage = e.loadImage(MainGame.imageDir + torpedoImg);
		
	}
	
	@Override
	public void Update(double dt) {
		myPosition.y += myVelocity.y * dt;
		
		// If it has left the screen, make sure it is deleted from the ArrayList in MainGame
		if (myPosition.y >= MainGame.WINDOW_RES_Y) { isAlive = false; }
		
		if(!isAlive) { myEngine.level.levelObjects.add(new Explosion(myEngine, myPosition.x - 20, myPosition.y)); }
	}
}
