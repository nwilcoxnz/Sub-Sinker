
public class Explosion extends GameObject {
	
	// Initialise explosion
	double explosionTimer;
	
	// Default constructor
	public Explosion(MainGame e){}
	
	// Constructor
	public Explosion(MainGame e, double xPos, double yPos){
		
		// Allow alterations to the game engine
		myEngine = e;
		
		// Set timers initial value
		explosionTimer = 0;
		
		// Set images
		mySprite = e.loadImage(MainGame.imageDir + "explosionSprites.png");
		myAnimation = new Animation(myEngine, 1, 14, mySprite, new Vector2D(4,4));
		
		// Set dimensions
		SetDimensions(60, 60);
		
		// Set position
		SetStartPosition(xPos, yPos);
		
		// Make sure it is displayed and not deleted
		InitialiseCollision();
		isAlive = true;
		
		// Play an explosion sound if the user has enabled sounds
		if (myEngine.soundOn){ myEngine.playSoundFile("Gun.wav", -20); }
	}
	
	public Explosion(MainGame e, double xPos, double yPos, double size) {
		this(e, xPos, yPos);
		AlterSize(size);
	}

	@Override
	public void Update(double dt) {
		explosionTimer += dt;
		if (explosionTimer < 1) { myAnimation.Play(); }
		if(!myAnimation.GetPlayState()) { isAlive = false; }
	}
}

/* 
 * Explosion sprites from http://1.bp.blogspot.com/-h4gHvGnPfH0/UmFUg1riZlI/AAAAAAAAAFU/FGgUImTIGbU/s640/explosjon3.png
 * Artist: Bodhi Gray
 */
