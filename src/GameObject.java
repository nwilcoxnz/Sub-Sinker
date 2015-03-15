import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public abstract class GameObject implements Collidable {
	
	// TODO
	protected	MainGame myEngine;
	protected	Animation myAnimation;
	protected	Image myCurrentImage;
	protected	Image mySprite;
	protected	Vector2D myPosition;
	protected	Vector2D myVelocity;
	protected	Vector2D myDimensions;
	protected	double myAngle;
	protected	boolean isAlive;
	protected	boolean flip;
	
	public	int myRenderLayer;

	public void Draw(){
		// Save the current transform
		myEngine.saveCurrentTransform();
		
		// ranslate to the position of the asteroid
		myEngine.translate(myPosition.x, myPosition.y);
		
		// Rotate the drawing context around the angle of the alien
		myEngine.rotate(myAngle);
		
		// Draw the object image
		if(flip == true) {
			myEngine.drawImage(FlipAnyImage(myCurrentImage), 0, 0, myDimensions.x, myDimensions.y);
		} else {
			myEngine.drawImage(myCurrentImage, 0, 0, myDimensions.x, myDimensions.y);
		}
		
		// Restore last transform to undo the rotate and translate transforms
		myEngine.restoreLastTransform();
	}
	
	public void UpdateObject(double dt) {
		if(myAnimation != null) {
			myAnimation.UpdateAnimation(dt);
			if (myAnimation.GetPlayState()) {myCurrentImage = myAnimation.GetFrame();} // If the animation is playing update the frame
		}
		
		Update(dt); // Calls an abstract function that is overidden by the child classes
		UpdateCollision();
	}
		
	public abstract void Update(double dt);
	
	//-----------------------------------------------------------//
	// COLLIDABLE INTERFACE FUNCTIONS
	//-----------------------------------------------------------//
	private Rectangle2D collisionBox;
	
	public void InitialiseCollision() {
		collisionBox = new Rectangle2D.Double(myPosition.x, myPosition.y, myDimensions.x, myDimensions.y);
	}
	
	public Rectangle2D GetCollisionBox() {
		return collisionBox;
	}
	
	public void UpdateCollision() {
		collisionBox.setRect(myPosition.x, myPosition.y, myDimensions.x, myDimensions.y);
	}
	
	public boolean CollideWith(Rectangle2D box) {
		Area objArea = new Area(box);
		if(objArea.intersects(collisionBox)) {
			return true;
		}
		
		return false;
	}
	//-----------------------------------------------------------//
	
	//-----------------------------------------------------------//
	// HEALTH MECHANICS
	//-----------------------------------------------------------//
	// Determines the amount of health the object has
	private int health;
	
	/**
	 * Function that returns the health of the game object
	 * @return returns the object's health
	 */
	public int GetHealth() {
		return health;
	}
	
	/**
	 * Set the default health of the object
	 * @param setValue - health number
	 */
	public void SetHealth(int setValue) {
		health = setValue;
	}
	
	/**
	 * Function for hit detection
	 */
	public void HitDetect() {
		health--;
		if (health <= 0) { isAlive = false; }
	}
	
	//-----------------------------------------------------------//
	
	//-----------------------------------------------------------//
	// UTILITY FUNCTIONS
	//-----------------------------------------------------------//
	/**
	 * Sets the object's starting position
	 * @param x - x-coordinate
	 * @param y - y-coordinate
	 */
	public void SetStartPosition(double x, double y) {
		myPosition = new Vector2D(x, y);
	}
	
	/**
	 * Modifys the objects position
	 * @param x - x-coordinate
	 * @param y - y-coordinate
	 */
	public void ChangePosition(double x, double y) {
		myPosition.x = x;
		myPosition.y = y;
		UpdateCollision();
	}
	
	public void SetDimensions(double h, double w) {
		myDimensions = new Vector2D(h, w);
	}
	
	/**
	 * Sets the object's start velocity
	 * @param x - x-axis speed
	 * @param y - y-axis speed
	 */
	public void SetVelocity(double x, double y) {
		myVelocity =  new Vector2D(x, y);
	}
	
	// TODO
	/**
	 * Function to change the static image and spritesheet
	 * @param staticImg - static image filename
	 * @param spriteImg - spritesheet filename
	 */
	public void ChangeAnimatedImage(String staticImg, String spriteImg, double duration, int numFrames, Vector2D rows) {
		myCurrentImage = myEngine.loadImage(MainGame.imageDir + staticImg);
		mySprite = myEngine.loadImage(MainGame.imageDir + spriteImg);
		myAnimation = new Animation(myEngine, duration, numFrames, mySprite, rows);
	}
	
	/**
	 * Function to change the static image
	 * @param filename - image filename
	 */
	public void ChangeStaticImage(String filename) {
		myCurrentImage = myEngine.loadImage(MainGame.imageDir + filename);
	}
	
	/**
	 * Function to alter object size
	 * @param size - size multiplier
	 */
	public void AlterSize(double size) {
		myDimensions.x *= size;
		myDimensions.y *= size;
		UpdateCollision();
	}
	
	/**
	 * Function that returns the class name of the object
	 * @return returns the class name
	 */
	public String GetClassType() {
		return this.getClass().getName();
	}
	
	/**
	 * Flip the image horizontally
	 */
	public void FlipImage() {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-myCurrentImage.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		myCurrentImage = op.filter((BufferedImage)myCurrentImage, null);
	}
	
	/**
	 * Flip any image
	 * @param i - image input
	 * @return flipped image
	 */
	public Image FlipAnyImage(Image i) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-i.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		i = op.filter((BufferedImage) i, null);
		return i;
	}
	
	// TODO
	public double GetAngle(GameObject target) {
		double angle = 0;
		double opposite = myEngine.distance(target.myPosition.x, target.myPosition.y, myPosition.x, target.myPosition.y);
		double ajacent = myEngine.distance(myPosition.x, target.myPosition.y, myPosition.x, myPosition.y);
		
		if(target.myPosition.x < myPosition.x) { 
			angle =  Math.atan2(opposite, ajacent);
		} else if(target.myPosition.x > myPosition.x) {
			angle = 360 - (Math.atan2(opposite, ajacent));
		}else{
		 angle = 0;
		}
		
		return angle;
	}
	
	/**
	 * Check if the game object is within the bounds of the screen
	 * @return 'true' if within bounds and 'false' if not
	 */
	public boolean WithinBounds() {
		if(myPosition.x >= 0 && myPosition.x + myDimensions.x <= MainGame.WINDOW_RES_X) {
			return true;
		}
		return false;
	}
	
	/**
	 * Function that generates a random double
	 * @param min - minimum value
	 * @param max - maximum value
	 * @return returns a random double within a range
	 */
	protected double GenerateRandom(double min, double max) {
		Random randGen = new Random();
		if(min > max) { return 0; }
		return (double)(randGen.nextInt((int)((max - min) + 1)) + min);
	}
	//-----------------------------------------------------------//
}