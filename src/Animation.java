import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Animation {
	private GameEngine myEngine;
	private int myNumFrames;
	private Image[] myFrames;
	private double myTimer;
	private double myDuration;
	private boolean isPlaying;
	
	public Animation(MainGame engine, double duration ,int numFrames, Image spriteSheet, Vector2D rows){
		myEngine = engine; 
		myTimer = 0;
		myDuration = duration;
		myNumFrames = numFrames;
		myFrames = new Image[numFrames];
		isPlaying = false;
		
		int myFrameWidth = (int)(spriteSheet.getWidth(null)/rows.x);
		int myFrameHeight = (int)(spriteSheet.getHeight(null)/rows.y);
		
		double x;
		double y = -myFrameHeight;
		for(int i = 0; i < myNumFrames; i++) {
			x = (i % rows.x) * myFrameWidth;
			if(x == 0){y += myFrameHeight;}
			myFrames[i] = myEngine.subImage(spriteSheet, (int)x, (int)y, myFrameWidth, myFrameHeight);
		}
	}
		
	// This function allows animation to be separate from the framerate
	private int DetermineAnimationFrame(double timer, double duration, int numFrames) {
		// Get frame
		int i = (int)Math.floor(((myTimer % myDuration) / myDuration) * numFrames);
		// Check range
		if(i >= numFrames) {
			i = numFrames-1;
		}
		// Return
		return i;
	};
	
	public Image GetFrame(){
		int i = DetermineAnimationFrame(myTimer, myDuration, myNumFrames);
		return myFrames[i];
	};
	
	public void Play(){
		if(!isPlaying){
			isPlaying = true;
		}
	}
	
	public void Stop(){
		isPlaying = false;
		myTimer = 0;
	}
	
	public void UpdateAnimation(double dt) {
		// If the animation is active
		if(isPlaying) {
			// Increment timer
			myTimer += dt;

			// Check to see if explosion has finished
			if(myTimer >= myDuration) {
				isPlaying = false;
				myTimer = 0;
				//System.out.println("Animation Finished");
			}
		}
	};
	
	public boolean GetPlayState(){
		return isPlaying;
	}

}