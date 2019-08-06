import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class OceanSurface extends Grid{

	Texture myTexture;
	
	public OceanSurface(int size, float[] color, double height) 
	{
		super(size, color, height);
		
		try
		{
			myTexture = TextureIO.newTexture(new File("SeaText2.jpg"), true);
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("File not found!!");
		}
	}
	
	@Override
	public void draw(GL2 gl)
	{
		gl.glEnable(GL2.GL_NORMALIZE);
		myTexture.bind(gl);
		myTexture.enable(gl);
		
		gl.glColor4fv(color, 0);
		
		gl.glPushMatrix();
		
			for(int x = 0 ; x < size; x++)
			{
				for(int z = 0; z < size; z++)
				{
					if(isFilled)
					{
						gl.glBegin(GL2.GL_QUADS);
					}
					else
					{
						gl.glBegin(GL2.GL_LINE_STRIP);
					}
					
					gl.glTexCoord2d(0, 0);
					gl.glNormal3d(0, 1, 0);
					gl.glVertex3d(x, height, z);
					
					gl.glTexCoord2d(0, 1);
					gl.glNormal3d(0, 1, 0);
					gl.glVertex3d(x, height, z+1);
					
					gl.glTexCoord2d(1, 1);
					gl.glNormal3d(0, 1, 0);
					gl.glVertex3d(x+1, height, z+1);
					
					gl.glTexCoord2d(1, 0);
					gl.glNormal3d(0, 1, 0);
					gl.glVertex3d(x+1, height, z);
					
					gl.glEnd();
				}
			}
		gl.glPopMatrix();
		myTexture.disable(gl);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}
}