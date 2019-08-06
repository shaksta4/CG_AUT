import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
	
	double fov, aspect, near, far;
	double camX, camY, camZ;
	double lookatX, lookatY, lookatZ;
	Submarine tracker;
	
	public Camera(Submarine tracker)
	{
		this.fov = 90;
		this.aspect = 1;
		this.near = 0.3;
		this.far = 100;
		
		this.tracker = tracker;
	}
	
	public void updateCamera()
	{
		double distance = -2.136;
		
		camX = tracker.getSubLoc()[0] + Math.sin(Math.toRadians(tracker.getHeading()))*distance;// where the camera is
		camY = tracker.getSubLoc()[1] + 0.75;
		camZ = tracker.getSubLoc()[2] + Math.cos(Math.toRadians(tracker.getHeading()))*distance;
		
		lookatX = tracker.getSubLoc()[0]; // what its looking at
		lookatY = tracker.getSubLoc()[1];
		lookatZ = tracker.getSubLoc()[2];
	}
	
	public void setCamera(GL2 gl, GLU glu)
	{
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(fov, aspect, near, far);
	
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		updateCamera();
		glu.gluLookAt(camX, camY, camZ, // where the camera is
					  lookatX, lookatY, lookatZ, //what its looking at
					  0, 1, 0);
	}
	
}
