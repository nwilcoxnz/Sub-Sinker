
public class LevelUpFish extends GameObject{

	public LevelUpFish(MainGame e) {
		
		// Allow alterations to the game engine
		myEngine = e;
		
		// Set dimensions
		myDimensions = new Vector2D(480,480);
		
		// Set position
		myPosition = new Vector2D(MainGame.WINDOW_RES_X,350);
		
		myCurrentImage = e.loadImage("LevelUp.png");
		
		// Set velocity
		SetVelocity(500,0);
		
		isAlive = true;
		
		InitialiseCollision();
		
		System.out.println("Creating fish...");
		
		myRenderLayer = 0;
	}

	@Override
	public void Update(double dt) {
		myPosition.x -= myVelocity.x * dt;
		if (myPosition.x < -480) {
			isAlive = false;
			System.out.println("fish died...");
		}
	}

}
