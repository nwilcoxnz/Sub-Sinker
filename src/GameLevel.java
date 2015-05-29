import java.util.*;

public class GameLevel {
	
	// Parent / Game Engine
	private MainGame gameParent;
	
	// Level Flags
	private boolean isCurrent;
	private boolean isBossLevel;
	
	// Level Properties
	private int enemyCount;
	private int maxLevel;
	private double weaponCooldown = 0;
	public double levelTimer;
	
	// Level Objects
	public ArrayList<GameObject> levelObjects;
	
	/**
	 * Default Constructor
	 */
	public GameLevel(MainGame e) {
		gameParent = e;
		isCurrent = true;
		isBossLevel = false;
		levelObjects = new ArrayList<GameObject>();
		maxLevel = 1;
		levelTimer = 0;
	}
	
	/**
	 * Create a new GameLevel object with a set max level
	 * @param e - GameEngine class
	 * @param numLevels - maximum number of levels
	 */
	public GameLevel(MainGame e, int numLevels) {
		this(e);
		maxLevel = numLevels;
	}
	
	/**
	 * Create a new GameLevel object with a set max level and level timer
	 * @param e - GameEngine class
	 * @param numLevels - maximum number of levels
	 * @param time - maximum game time
	 */
	public GameLevel(MainGame e, int numLevels, double time) {
		this(e, numLevels);
		levelTimer = time;
	}
	
	/**
	 * Initialise the level
	 * @param difficulty - level difficulty
	 */
	public void InitialiseLevel(int difficulty) {
		// Add the background object
		levelObjects.add(0, new Background(gameParent));
		
		// TODO: Add conditional statement for the timer
		// Add the submarines based on difficulty
		for(int i=0; i < difficulty; i++){
			levelObjects.add(new Submarine(gameParent));
		}
		
		// Add a boss submarine if the level is set as a boss level
		if(isBossLevel) {
			// TODO: Convert boss sub to a new class?
			GameObject bossSub = new Submarine(gameParent, 2, 40, 600);
			((Submarine)bossSub).weaponLimit = 3;
			((Submarine)bossSub).SetBoss();
			levelObjects.add(bossSub);
		}
	}
	
	/**
	 * Function that draws level objects on screen
	 */
	public void DrawLevel() {
		//Sort the array list based on render layer
		Collections.sort(levelObjects, new RenderLayerComparator());
		
		// Draws all the objects in the Arraylist
		for(GameObject obj : levelObjects) { obj.Draw(); }
	}
	
	/**
	 * Function for updating level objects
	 * @param dt - game time
	 */
	public void UpdateLevel(double dt) {
		// Temporary objects for comparison
		GameObject focus, target;
		
		for(int i = 0; i < levelObjects.size(); i++){
			focus = levelObjects.get(i);
			
			focus.UpdateObject(dt);
			
			//Removes object if no longer alive
			if(!focus.isAlive){ levelObjects.remove(focus); }
			
			// Comparison with other game objects
			for(int j = 0; j < levelObjects.size(); j++) {
				target = levelObjects.get(j);
				
				// Make sure its not comparing against itself
				if(target != focus) {
					// Collision checking
					if(focus.CollideWith(target.GetCollisionBox())) {
						if(focus instanceof Ship && target instanceof EnemyTorpedo) { focus.HitDetect(); target.HitDetect(); }
						if(focus instanceof Submarine && target instanceof FriendlyTorpedo) { focus.HitDetect(); target.HitDetect(); MainGame.score++; }
						if(focus instanceof FriendlyTorpedo && target instanceof EnemyTorpedo) { focus.HitDetect(); target.HitDetect(); }
					}
					
					// Submarine Fire Trigger
					if(focus instanceof Submarine && (target instanceof Ship || target instanceof FriendlyTorpedo)) {
						double centerSub = focus.GetCollisionBox().getCenterX();
						if(centerSub > target.GetCollisionBox().getMinX() && centerSub < target.GetCollisionBox().getMaxX()) {
							for(int w = 0; w < ((Submarine)focus).weaponLimit; w++) {
								if(weaponCooldown > 0.2) { ((Submarine)focus).FireMine(); weaponCooldown = 0; }
							}
						}
					}
				}
			}
		}
		
		weaponCooldown += dt;
		CountEnemies();
	}
	
	/**
	 * Function to count enemy subs currently active
	 */
	private void CountEnemies() {
		enemyCount = 0;
		
		// Locate all submarines in the object array
		for(GameObject obj : levelObjects) {
			// Increment enemyCount if found
			if(obj instanceof Submarine) { enemyCount++; }
		}
	}
	
	/**
	 * Function to get the total enemy count
	 * @return number of enemies remaining
	 */
	public int GetEnemyCount() {
		return enemyCount;
	}
	
	/**
	 * Function to get the max level
	 * @return total number of levels
	 */
	public int GetMaxLevel() {
		return maxLevel;
	}
	
	/**
	 * Function that returns the status of the level, whether it has
	 * ended or not
	 * @return 'true' if it is still playing and 'false' if not
	 */
	public boolean LevelActive() {
		return isCurrent;
	}
	
	/**
	 * Function that determines if the level spawns a boss enemy
	 */
	public void SetBossLevel() {
		isBossLevel = true;
	}
	
	/**
	 * Ends the level and clears all objects
	 */
	public void EndLevel() {
		// Set every object's isAlive value to false 
		for(GameObject obj : levelObjects) { obj.isAlive = false; }
		// Reset the flags
		isCurrent = false;
		isBossLevel = false;
		// Clear the object array
		levelObjects.clear();
	}
}
