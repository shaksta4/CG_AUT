/**
 * 
 * This class holds variables and methods used in creating the origin lines for a 3D space.
 * It draws negative space values with stipple lines so you know which side of the screen is positive and which side 
 * is negative.
 * 
 * @author Shakeel
 */

import com.jogamp.opengl.GL2;

public class Origin {
	
	public Origin()
	{

	}
	
	public void draw(GL2 gl)
	{
		gl.glBegin(GL2.GL_LINES);
		
		// X IS RED
		gl.glColor3d(1, 0, 0); 
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(1, 0, 0);
		
		// Y IS GREEN
		gl.glColor3d(0, 1, 0); 
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 1, 0);
		
		//Z IS BLUE
		gl.glColor3d(0, 0, 1); 
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 1);
		
		gl.glEnd();
		
		//Draw negative part of screen
		gl.glEnable(GL2.GL_LINE_STIPPLE); 	 
		
		gl.glLineStipple(1, (short) 0x0101); 
		gl.glBegin(GL2.GL_LINES); 
		
		gl.glColor3d (1, 0, 0); 	// Red for x axis
		gl.glVertex3d(-1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		
		gl.glColor3d(0, 1, 0); 	// Green for y axis
		gl.glVertex3d(0, -1, 0);
		gl.glVertex3d(0, 0, 0);
		
		gl.glColor3d(0, 0, 1); 	// Blue for z axis
		gl.glVertex3d(0, 0, -1);
		gl.glVertex3d(0, 0, 0);
		
		gl.glEnd();
		gl.glDisable(GL2.GL_LINE_STIPPLE);
		
	}
	
	
}
