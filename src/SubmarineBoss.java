import java.util.*;

public class SubmarineBoss extends Submarine {

	public static enum BossType { Regular, Tracker };
	public BossType subType = BossType.Regular;
	
	// Image and Sprites
	private String subImage = "SharkSub.png";
	private String spriteImage = "BigBossSubs.png";
	
	/**
	 * Default Constructor
	 * @param setEngine - Game Engine class
	 */
	public SubmarineBoss(MainGame setEngine){
		super(setEngine);
		ChangeAnimatedImage(subImage, spriteImage, 1, 14, new Vector2D(4, 4));
	}
	
	/**
	 * Create a Submarine with a set size
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 */
	public SubmarineBoss(MainGame setEngine, double size) {
		super(setEngine, size);
		ChangeAnimatedImage(subImage, spriteImage, 1, 14, new Vector2D(4, 4));
	}
	
	/**
	 * Create a Submarine with a set size and dept
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 * @param y - depth
	 */
	public SubmarineBoss(MainGame setEngine, double size, double y) {
		super(setEngine, size, y);
		ChangeAnimatedImage(subImage, spriteImage, 1, 14, new Vector2D(4, 4));
	}
	
	/**
	 * Create a Submarine with set size and health
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 * @param sethealth - submarine health
	 */
	public SubmarineBoss(MainGame setEngine, double size, int sethealth) {
		super(setEngine, size, sethealth);
		ChangeAnimatedImage(subImage, spriteImage, 1, 14, new Vector2D(4, 4));
	}
	
	/**
	 * Create a Submarine with set size, health, and depth
	 * @param setEngine - Game Engine class
	 * @param size - submarine size
	 * @param sethealth - submarine health
	 * @param y - submarine depth
	 */
	public SubmarineBoss(MainGame setEngine, double size, int sethealth, double y) {
		super(setEngine, size, sethealth, y);
		ChangeAnimatedImage(subImage, spriteImage, 1, 14, new Vector2D(4, 4));
	}
	
	
	/**
	 * Creates a new Homing Ammunition object in the position
	 * of the Submarine
	 */
	public void FireMine() {
		ArrayList<GameObject> tempArray = myEngine.level.levelObjects;
		// Checks if the submarine has an active torpedo
		for(Weapon wp : subTorpedo) {
			if(!tempArray.contains(wp) && !wp.isAlive) {
				// Fires a new torpedo if there are none active
				if(subType == BossType.Tracker) { wp.SetTracking(); }
				wp.ChangePosition(GetCollisionBox().getCenterX(), myPosition.y);
				wp.isAlive = true;
				tempArray.add(wp);
				return;
			}
		}
	}
}
