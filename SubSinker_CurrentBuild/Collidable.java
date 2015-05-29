import java.awt.geom.*;

public interface Collidable {
	/**
	 * Initialise the collision box for the Game Object
	 */
	void InitialiseCollision();
	
	/**
	 * Get the collision box
	 * @return Game Object's collision box
	 */
	Rectangle2D GetCollisionBox();
	
	/**
	 * Update the collision box
	 */
	void UpdateCollision();
	
	/**
	 * Check if the object collided with another object
	 * @param box - Other object's collision box
	 */
	boolean CollideWith(Rectangle2D box);
}
