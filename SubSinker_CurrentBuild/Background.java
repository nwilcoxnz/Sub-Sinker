
public class Background extends GameObject {
	
	public Background(MainGame e) {
		// Engine
		myEngine = e;
		
		// Image
		myCurrentImage = e.loadImage(MainGame.imageDir + "gameBackground.png");
		
		// Position (x, y)
		SetStartPosition(0, 0);
		
		// Dimension (h, w)
		SetDimensions(MainGame.WINDOW_RES_X, MainGame.WINDOW_RES_Y);
		
		// Initialise Gameplay Properties
		InitialiseCollision();
		isAlive = true;
	}
	
	@Override
	public void Update(double dt) {
		// Empty
	}
}
