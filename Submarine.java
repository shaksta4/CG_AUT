/**
 * This class holds methods and variables used in creating a submarine
 * It draws each part of the submarine seperately and then has one class called draw_submarine which
 * uses each of the helper methods to create the submarine
 * All the parts of the sub are drawn relative to the hull. 
 *
 * @author Shakeel
 */

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

public class Submarine {

	//Instance variables
	private GLUT glut;
	private GLU glu;
	private double bodyRadius, bodyLength;
	private int finAngle, variableFinAngle;
	private boolean isFilled;
	private double[] location = {0, 0, 0};
	private double heading;
	private double rollDeg, pitchDeg = 0;
	private double rollDegIncrement = 2;
	private double pitchDegIncrement = 2;
	private double speed = 0.1;
	private double headingIncrement = 2;
	
	private float[] hullColor = {0.118f, 0.565f, 1, 1};
	private float[] propellorColor = {0.2f, 0.2f, 0.2f, 1};
	private float[] windowColor = {0.765f, 0.765f, 0.765f, 0.5f};
	private float[] periscopeColor = {1, 0, 0, 1};
	
	
	//Default constructor which initialises values
	public Submarine()
	{
		this.glut = new GLUT();
		this.glu = new GLU();
		this.bodyRadius = 0.25;
		this.bodyLength = 0.7;
		this.finAngle = 90;
		this.isFilled = true;
		this.heading = 0;
	}
	
	/**
	 * This method draws the hull of the submarine. It uses a cylinder and two spheres as the ends of the cylinder
	 * to give the oval shape.
	 * 
	 * @param gl
	 * @param quadric
	 */
	public void draw_hull(GL2 gl, GLUquadric quadric)
	{
		gl.glPushMatrix();
			setMaterial(gl, hullColor);
			//gl.glColor3d(0.118, 0.565, 1.000);
			glu.gluCylinder(quadric, bodyRadius, bodyRadius, bodyLength, 20, 10); // body
			gl.glPushMatrix();
				gl.glTranslated(0, 0, bodyLength);
				glu.gluSphere(quadric, bodyRadius, 20, 10); // front
				gl.glTranslated(0, 0, -bodyLength);
				glu.gluSphere(quadric, bodyRadius, 20, 10); // back
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	
	/**
	 * This method draws a single fin of the propeller. It draws a cone
	 * and then scales it so it looks flat.
	 * 
	 * @param gl
	 * @param quadric
	 */
	public void draw_fin(GL2 gl, GLUquadric quadric)
	{
		gl.glPushMatrix();
			gl.glScaled(0.25, 1, 1);
			glu.gluCylinder(quadric, 0, 0.05, 0.15, 20, 10);
		gl.glPopMatrix();
	}
	
	/**
	 * 
	 * This method draws 4 fins and a torus around the fins to encase the propeller. 
	 * This method also makes it so the propeller is animated and rotating.
	 * 
	 * @param gl
	 * @param quadric
	 */
	public void draw_propeller(GL2 gl, GLUquadric quadric)
	{
		gl.glPushMatrix();
			setMaterial(gl, propellorColor);
			//gl.glColor3d(0.2, 0.2, 0.2);
			gl.glTranslated(0, 0, bodyLength-0.65);
			gl.glRotated(variableFinAngle, 0, 0, 1);
			gl.glPushMatrix();
				if(isFilled)
				{
					glut.glutSolidTorus(0.02, 0.15, 20, 360);
				}
				else
				{
					glut.glutWireTorus(0.02, 0.15, 20, 360);
				}
				gl.glRotated(finAngle, 0, 1, 0);
				draw_fin(gl, quadric);
				gl.glRotated(finAngle, 1, 0, 0);
				draw_fin(gl, quadric);
				gl.glRotated(finAngle, 1, 0, 0);
				draw_fin(gl, quadric);
				gl.glRotated(finAngle, 1, 0, 0);
				draw_fin(gl, quadric);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	
	/**
	 * This method simply draws the little red periscope on top of the submarine.
	 * Very simple, just uses cylinders and translation to get the parts relative to the sub body
	 * 
	 * @param gl
	 * @param quadric
	 */
	public void draw_periscope(GL2 gl, GLUquadric quadric)
	{
		gl.glPushMatrix();
			setMaterial(gl, periscopeColor);
			//gl.glColor3d(1, 0, 0);
			gl.glTranslated(0, 0.3, bodyLength-0.35);
			gl.glRotated(90, 1, 0, 0);
			glu.gluCylinder(quadric, 0.01, 0.01, 0.05, 10, 20);
			gl.glPushMatrix();
				gl.glTranslated(0, -0.0125, 0);
				gl.glRotated(-90, 1, 0, 0);
				glu.gluCylinder(quadric, 0.01, 0.01, 0.05, 10, 20);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	
	/**
	 * This method draws a single window on the side of the submarine. Again very simple
	 * Uses a torus to make the window.
	 * 
	 * @param gl
	 * @param quadric
	 */
	public void draw_window(GL2 gl, GLUquadric quadric)
	{
		gl.glPushMatrix();
			setMaterial(gl, windowColor);
			//gl.glColor3d(0.765, 0.765, 0.765);
			gl.glTranslated(-0.25, 0, bodyLength-0.2);
			gl.glRotated(90, 0, 1, 0);
			if(isFilled)
			{
				glut.glutSolidTorus(0.01, 0.075, 20, 360);
			}
			else
			{
				glut.glutWireTorus(0.01, 0.075, 20, 360);
			}
		gl.glPopMatrix();
	}
	
	/**
	 * This method uses all the helper methods to actually create the submarine. 
	 * 
	 * @param gl
	 * @param quadric
	 */
	public void draw_submarine(GL2 gl, GLUquadric quadric)
	{
		gl.glPushMatrix();
			gl.glTranslated(location[0], location[1], location[2]);
			gl.glRotated(heading, 0, 1, 0);
			gl.glRotated(rollDeg, 0, 0, 1);
			gl.glRotated(pitchDeg, 1, 0, 0);
			this.draw_hull(gl, quadric);
			this.draw_periscope(gl, quadric);
			this.draw_window(gl, quadric);
			gl.glPushMatrix();
				gl.glTranslated(0, 0, -0.3);
				this.draw_propeller(gl, quadric);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	
	public void moveUp()
	{
		if(pitchDeg >= -25)
		{
			pitchDeg -= pitchDegIncrement;
		}		
	}
	
	public void moveDown()
	{
		if(pitchDeg <= 25)
		{
			pitchDeg += pitchDegIncrement;
		}
	}
	
	public void moveForward()
	{
		location[0] += Math.sin(Math.toRadians(heading))*speed;
		location[1] += Math.sin(Math.toRadians(-pitchDeg*2))*speed;
		location[2] += Math.cos(Math.toRadians(heading))*speed;
		variableFinAngle+=5;
	}
	
	public void moveBack()
	{
		location[0] -= Math.sin(Math.toRadians(heading))*speed;
		location[1] -= Math.sin(Math.toRadians(-pitchDeg*2))*speed;
		location[2] -= Math.cos(Math.toRadians(heading))*speed;
		variableFinAngle-=5;
	}
	
	public void moveLeft()
	{
		if(rollDeg <= 15)
		{
			rollDeg += rollDegIncrement;
		}
		
		heading += headingIncrement;
		heading = heading % 360;
	}
	
	public void moveRight()
	{
		if(rollDeg >= -15)
		{
			rollDeg -= rollDegIncrement;
		}
		
		heading -= headingIncrement;
		heading = heading % 360;
	}
	
	public double[] getSubLoc()
	{
		return location;
	}
	
	public double getHeading()
	{
		return heading;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public void defaultRollPitchYaw()
	{
		pitchDeg = 0;
		rollDeg = 0;
	}
	
	public void defaultPitch()
	{
		pitchDeg = 0;
	}
	
	
	public void setSpotlight(GL2 gl)
	{
		float diffuse[] = { 2, 2, 2, 0 };
		float specular[] = { 2, 2, 2, 0 };
		float position[] = {(float)this.getSubLoc()[0], (float)this.getSubLoc()[1]-(float)this.bodyRadius, (float)this.getSubLoc()[2], 1};
		float direction[] = {(float)this.getSubLoc()[0], (float)this.getSubLoc()[1], (float)this.getSubLoc()[2]};
		
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_CONSTANT_ATTENUATION, 0);
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_LINEAR_ATTENUATION, 0);
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_QUADRATIC_ATTENUATION, 1);
		
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, specular, 0);

		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, position, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, direction, 0);
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, 360);
	}
	
	public void setMaterial(GL2 gl, float[] color)
	{
		float[] ambient = color;
		float[] diffuse = color;
		float[] specular = color;
		float shininess = 100;
		
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);
		
	}
	
	/**
	 * This method allows the user to switch between wireframe or filled
	 * 
	 */
	public void switchFilled()
	{
		if(this.isFilled)
		{
			this.isFilled = false;
		}
		else
		{
			this.isFilled = true;
		}
	}
}
