/**
 * This method is the second part of the assignment 2. It dreals with changing the camera to a
 * perspective mode and setting the clipping values and fov of the camera.
 * There is a grid drawn in the scene as well as the submarine
 * 
 * Currently, there is no movement or camera functionality. 
 * 
 * The grids are able to be toggled wireframe or fill using L
 * 
 * @author Shakeel
 */

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;

public class SubScene implements GLEventListener, KeyListener{
	//Colors
	final static float[] grey = {0.5f, 0.5f, 0.5f, 1};
	final static float[] aqua = {0.690f, 0.878f, 0.972f, 0.5f};
	final static float[] brown = {0.871f, 0.722f, 0.529f, 1};
	
	//Instance variables
	GLUquadric quadric;
	private static int SEA_LEVEL = 10; //added by JW
	private static int FLOOR_LEVEL = 0; //added by JW
	
	int shapeType = GLU.GLU_FILL;
	Origin myOrigin = new Origin();
	Submarine mySub = new Submarine();
	SeaFloor seaFloor;
	OceanSurface oceanSurface;
	boolean spotlightOn = false;
	
	
	private final Set<Integer> pressed = new HashSet<Integer>();
	
	Camera camera = new Camera(mySub);
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		// clear the depth and color buffers
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		quadric = glu.gluNewQuadric();
			
		//Set camera
		camera.setCamera(gl, glu);
		camera.updateCamera();
		
		//set up lights
		this.lights(gl);

		//draw the submarine and the origin
		glu.gluQuadricDrawStyle(quadric, shapeType); 

		gl.glPushMatrix(); 
		gl.glTranslated(-25, -0.5, -25); //shift across so sub is in scene - JW
			seaFloor.draw(gl);
			gl.glPushMatrix();
				gl.glDisable(GL2.GL_DEPTH_BUFFER_BIT);
				gl.glTranslated(0, SEA_LEVEL, 0);
				oceanSurface.draw(gl);
				gl.glEnable(GL2.GL_DEPTH_BUFFER_BIT);
			gl.glPopMatrix();
		gl.glPopMatrix();
			
		//sub
		gl.glPushMatrix();
			gl.glDisable(GL2.GL_COLOR_MATERIAL);
			mySub.draw_submarine(gl, quadric);
			gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glPopMatrix();

		//origin
		gl.glPushMatrix();
			gl.glDisable(GL2.GL_LIGHTING); // Disable lighting
			
			myOrigin.draw(gl);
			
			gl.glEnable(GL2.GL_LIGHTING); // Enable it again
		gl.glPopMatrix();
		
		this.setupFog(gl);
		this.commands();		
		gl.glFlush();
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {	
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.setSwapInterval(1);

		// enable depth test and set shading mode
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		
		seaFloor = new SeaFloor(50, brown, FLOOR_LEVEL);
		oceanSurface = new OceanSurface(50, aqua, SEA_LEVEL);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		height = (height == 0) ? 1 : height; // prevent divide by zero

		// Set the viewport to cover the new window
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(45, (double)width/height, 0.1, 100);
	}
	
	private void lights(GL2 gl) {
		
		// lighting stuff
		float ambient[] = { 0, 0, 0, 1 };
		float diffuse[] = { 1, 1, 1, 1 };
		float specular[] = { 1, 1, 1, 1 };
		float position0[] = { 0, 20, 0, 1 };
		
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);

		float ambient1[] = { 0, 0, 0, 1 };
		float diffuse1[] = { 0.5f, 0.5f, 0.7f, 1 };
		float specular1[] = { 0.5f, 0.5f, 0.7f, 1 };
		float position1[] = { 0, 10, 0, 0};
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specular1, 0);
		
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_LIGHT0);
		
		if(mySub.getSubLoc()[1] < SubScene.SEA_LEVEL-1) // if under water
		{
			gl.glEnable(GL2.GL_LIGHT1);
			gl.glDisable(GL2.GL_LIGHT0);
		}
		else // if over water
		{
			gl.glEnable(GL2.GL_LIGHT0);
			gl.glDisable(GL2.GL_LIGHT1);
		}
		
		mySub.setSpotlight(gl);
		if(spotlightOn)
		{
			gl.glEnable(GL2.GL_LIGHT2);
		}
		else
		{
			gl.glDisable(GL2.GL_LIGHT2);
		}
		//draw using standard glColor
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
	}
	
	public void setupFog(GL2 gl)
	{
		float[] fogColorUnder = { 0, 0, 0 };
		float[] fogColorOver = { 0, 0, 0 };
		
		gl.glEnable(GL2.GL_FOG);
		gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_EXP);
		
		if(mySub.getSubLoc()[1] < SubScene.SEA_LEVEL-0.65)
		{
			gl.glFogfv(GL2.GL_FOG_COLOR, fogColorUnder, 0);
			gl.glFogf(GL2.GL_FOG_DENSITY, 0.15f);
		}
		else
		{
			gl.glFogfv(GL2.GL_FOG_COLOR, fogColorOver, 0);
			gl.glFogf(GL2.GL_FOG_DENSITY, 0.05f);
		}		
	}
	
	public void commands()
	{
		if(pressed.contains(KeyEvent.VK_A))
		{
			mySub.moveLeft();	
		}
		
		if(pressed.contains(KeyEvent.VK_D))
		{
			mySub.moveRight();
		}
		
		if(pressed.contains(KeyEvent.VK_W))
		{
			mySub.moveForward();
			if(mySub.getSubLoc()[1] >= SEA_LEVEL-0.65)
			{
				mySub.defaultPitch();
			}
			
			else if(mySub.getSubLoc()[1] <= FLOOR_LEVEL)
			{ 
				mySub.defaultRollPitchYaw();
			}
		}
		
		if(pressed.contains(KeyEvent.VK_S))
		{
			mySub.moveBack();
			if(mySub.getSubLoc()[1] >= SEA_LEVEL-0.65)
			{
				mySub.defaultPitch();
			}
			
			else if(mySub.getSubLoc()[1] <= FLOOR_LEVEL)
			{ 
				mySub.defaultRollPitchYaw();
			}
		}
		
		if(pressed.contains(KeyEvent.VK_L))
		{
			seaFloor.setFilled(); 
			oceanSurface.setFilled();
		}
		
		if(pressed.contains(KeyEvent.VK_K))
		{
			seaFloor.setWireframe(); 
			oceanSurface.setWireframe();
		}
		
		if(pressed.contains(KeyEvent.VK_UP))
		{
			mySub.moveUp(); 
			if(mySub.getSubLoc()[1] >= SEA_LEVEL-0.65)
			{ 
				mySub.defaultRollPitchYaw();
			}
		}
		if(pressed.contains(KeyEvent.VK_DOWN))
		{
			mySub.moveDown();
			if(mySub.getSubLoc()[1] <= FLOOR_LEVEL)
			{ 
				mySub.defaultRollPitchYaw();
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{	
		int key = e.getKeyCode();
		pressed.add(key);
		
		if(key == KeyEvent.VK_P)
		{
			spotlightOn = true;
			System.out.println(spotlightOn);
		}
		else if(key == KeyEvent.VK_O)
		{
			spotlightOn = false;
			System.out.println(spotlightOn);
		}
		else if(key == KeyEvent.VK_SPACE)
		{
			System.out.println("x:"+mySub.getSubLoc()[0]+" y:"+mySub.getSubLoc()[1]+" z:"+ mySub.getSubLoc()[2]);
			System.out.println("heading angle:"+mySub.getHeading());
		}
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		pressed.remove(e.getKeyCode());
		int key = e.getKeyCode();
		
		switch(key)
		{
			case KeyEvent.VK_A : { mySub.defaultRollPitchYaw(); break;}
			case KeyEvent.VK_D : { mySub.defaultRollPitchYaw(); break;}
			case KeyEvent.VK_UP: { mySub.defaultRollPitchYaw(); break;}
			case KeyEvent.VK_DOWN: {mySub.defaultRollPitchYaw(); break;}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame("Sub Scene project");
		GLCanvas canvas = new GLCanvas();
		SubScene app = new SubScene();
		canvas.addGLEventListener(app);
		canvas.addKeyListener(app);
		frame.add(canvas);
		frame.setSize(800, 800);
		final FPSAnimator animator = new FPSAnimator(canvas, 60);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// Run this on another thread than the AWT event queue to
				// make sure the call to Animator.stop() completes before
				// exiting
				new Thread(new Runnable() {

					@Override
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		// Center frame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.setFocusable(true);
		canvas.requestFocus();
		animator.start();
	
		System.out.println("CONTROLS:\n "
				+ "-----------------------------\n"
				+ "W - move forward\n"
				+ "S - move backwards\n"
				+ "A - turn left\n"
				+ "D - turn right\n"
				+ "UP key - surface\n"
				+ "DOWN key - dive\n"
				+ "L -  set to fill\n"
				+ "K - set to wireframe\n"
				+ "P - set spotlight on\n"
				+ "O - set spotlight off\n"
				+ "SPACE - DEBUG");
	}
}
