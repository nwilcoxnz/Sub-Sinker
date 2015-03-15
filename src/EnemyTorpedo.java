
public class EnemyTorpedo extends Weapon {
	
	private String torpedoImg = "EnemyTorpedoSprite.png"; 
	
	// Constructor
	public EnemyTorpedo(MainGame e, double xPos, double yPos) {
		// Call super constructor
		super(e, xPos, yPos);
		
		// Load image
		myCurrentImage = e.loadImage(MainGame.imageDir + torpedoImg);
		
	}
	
	@Override
	public void Update(double dt) {
		if(Tracking()) { FollowTarget(dt); }
		else { myPosition.y -= myVelocity.y * dt; }
		
		if (myPosition.y <= 200) { isAlive = false; }
		
		if(!isAlive) { myEngine.level.levelObjects.add(new Explosion(myEngine, myPosition.x - 20, myPosition.y)); }
	}
}
