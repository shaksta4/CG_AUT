/**
 * This class is the first part of the assingment. It holds functionality where
 * the submarine is drawn to the screen with an origin. Users can press buttons (printed to the console)
 * to switch the view of the submarine. It is not toggled, you must hold down the button to view each side of the sub
 * 
 * @author Shakeel
 */


import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;

public class SubBuild implements GLEventListener, KeyListener {

	//Instance variables
	Origin myOrigin = new Origin();
	GLUquadric quadric;
	int camX = 0;
	int camY = 0;
	int camZ = 0;
	int shapeType = GLU.GLU_FILL;
	double camAngle = 0;
	Submarine mySub = new Submarine();
	
	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		// clear the depth and color buffers
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		quadric = glu.gluNewQuadric();
		
		//using default camera & projection 
		
		//set up lights
		this.lights(gl);


		glu.gluQuadricDrawStyle(quadric, shapeType); 
		
		gl.glPushMatrix();
			gl.glRotated(camAngle, camX, camY, camZ);
			mySub.draw_submarine(gl, quadric);
		gl.glPopMatrix();

		gl.glPushMatrix();
			gl.glDisable(GL2.GL_LIGHTING); // Disable lighting
			gl.glDisable(GL2.GL_LIGHT0);
			
			gl.glRotated(camAngle, camX, camY, camZ);
			myOrigin.draw(gl);
			
			gl.glEnable(GL2.GL_LIGHTING); // Enable it again
			gl.glEnable(GL2.GL_LIGHT0);

		gl.glPopMatrix();
		
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
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		height = (height == 0) ? 1 : height; // prevent divide by zero

		// Set the viewport to cover the new window
		gl.glViewport(0, 0, width, height);

	}

	private void lights(GL2 gl) {
		
		// lighting stuff
		float ambient[] = { 0, 0, 0, 1 };
		float diffuse[] = { 1, 1, 1, 1 };
		float specular[] = { 1, 1, 1, 1 };
		float position0[] = { 1, 1, 1, 0 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
	
		//enable lights
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
	
		//draw using standard glColor
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		switch(key)
		{
			case KeyEvent.VK_A : { camY = 1; camAngle = -90; break; }
			case KeyEvent.VK_D : { camY = 1; camAngle =  90; break; }
			case KeyEvent.VK_T : { camX = 1; camAngle = -90; break; }
			case KeyEvent.VK_U : { camX = 1; camAngle =  90; break; }
			case KeyEvent.VK_S : { camY = 1; camAngle =  180; break; }
			case KeyEvent.VK_L : 
			{
				if(shapeType == GLU.GLU_FILL)
				{ 
					shapeType = GLU.GLU_LINE;
					mySub.switchFilled();
				}  
				else if(shapeType == GLU.GLU_LINE) 
				{ 
					shapeType = GLU.GLU_FILL;
					mySub.switchFilled();
				}; break;	
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		switch(key)
		{
			case KeyEvent.VK_A : { camY = 0; camAngle = 0; break; }
			case KeyEvent.VK_D : { camY = 0; camAngle = 0; break; }
			case KeyEvent.VK_T : { camX = 0; camAngle = 0; break; }
			case KeyEvent.VK_U : { camX = 0; camAngle = 0; break; }
			case KeyEvent.VK_S : { camY = 0; camAngle = 0; break; }
			case KeyEvent.VK_L : { ; break; }
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame("Sub build project");
		GLCanvas canvas = new GLCanvas();
		SubBuild app = new SubBuild();
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
				+ "A - look left\n"
				+ "D - look right\n"
				+ "S - look from front\n"
				+ "T - look from top\n"
				+ "U - look from under\n"
				+ "L - toggle wireframe/fill\n");
	}
}
