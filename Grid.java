/**
 * This class holds methods and variables needed to create the 3D grid. 

 * I use GL lines to make the wireframe and GL quads to make the filled grid
 * Its also easy to change the size and color of the grid by passing them in 
 * as parameters to the constructor. 
 * 
 * I have yet to make it center at the origin
 * 
 * @author Shakeel
 * 
 */


import com.jogamp.opengl.GL2;

public abstract class Grid {

	//Instance variables
	int size;
	double height;
	boolean isFilled;
	int x, z;
	float[] color;
	
	//Default constructor
	public Grid(int size, float[] color, double height)
	{
		this.height = 0;
		this.size = size;
		this.isFilled = true;
		this.x = 0;
		this.z = 0;
		this.color = color;
	}
	
	public abstract void draw(GL2 gl);
	
	//This method allows the user to toggle if they want it filled or not
	public void setFilled()
	{
			this.isFilled = true;
	}
	
	public void setWireframe()
	{
			this.isFilled = false;
	}
	
	
}
