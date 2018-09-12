/* CS2150Coursework.java
 * Name: Mohammad Azfor Redwan Khan
 * User name: khanmar
 * 
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- [Ry(45) T(0,0,0)]SpaceBox (R Varies with user interaction)
 *  |
 *  +-- []Rocket
 * 		|
 * 		+-- [Rx(-90)T(0, 2.0, 0)]Head
 * 		|
 * 		+-- [Rx(-90)T(0, 0, 0)]Body
 * 		|
 * 		+-- [Rx(-90)T(0,-0.5,0)]Exhaust
 * 		|
 * 		+-- [Rx(-90)T(0, 0.5, 0)]Fire (varies with user interaction)
 * 		|
 * 		+-- [Ry(-45)T(-0.5,-0.3,0)]Fin 1
 * 		|
 * 		+-- [Ry(45)T(-0.5,-0.3,0)]Fin 2
 * 		|
 * 		+-- [Ry(225)T(-0.5,-0.3,0)]Fin 3
 * 		|
 * 		+-- [Ry(-225)T(-0.5,-0.3,0)]Fin 4
 * 
 */
package coursework.khanmar;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * My submission consists of a rocket flying in space. I thought this would be a
 * good take on the space theme as there is many possible ways to animate a
 * rocket. The scene is set in space with the rocket stationary at the start.
 * 
 * The user can control the speed of the rocket as well as making the rocket do
 * a spin. As mentioned below the user can use the keyboard to control the speed
 * of the rocket and when the user left clicks with the mouse the rocket does a
 * spins.
 * 
 * <p>
 * Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis,
 * respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down
 * cursor keys to increase or decrease the viewpoint's distance from the scene
 * origin
 * 
 * <li>Press the Up key to increase the speed of the rocket.
 * <li>Press the Down key to reduce the speed of the rocket.
 * <li>Left mouse click makes the rocket do a 360 degree turn.
 * <li>Press the R key to reset the animation back to the original state.
 * 
 * </ul>
 *
 */
public class CS2150Coursework extends GraphicsLab {
	//texture variable
	private Texture spaceTexture;
	//spaceBox (skyBox) initial position
	private float spaceBoxSpin = 0.0f;
	//speed of the "rocket"
	private float speed = 0.0f;
	//variable that holds maximum speed of the rocket
	private final float maxSpeed = 100.0f;
	//variable that holds minimum speed of the rocket
	private final float minSpeed = 0.0f;
	//position of the fire relative to the exhaust tip
	private float fireHeight = 0.5f;
	//variable to stop the rocket spinning when the program runs
	private boolean spin = false;
	//spin variable
	private float rocketSpin = 0.0f;

	// TODO: Feel free to change the window title and default animation scale here
	public static void main(String args[]) {
		new CS2150Coursework().run(WINDOWED, "CS2150 Coursework Submission", 0.01f);
	}

	protected void initScene() throws Exception {// TODO: Initialise your resources here - might well call other methods
		// you write.

		// Loads Texture, note: I copied the texture from the BorgCube sample provided in the labs
		spaceTexture = loadTexture("coursework/khanmar/Texture/stars.bmp");

		// global ambient light level
		float globalAmbient[] = { 0.8f, 0.8f, 0.8f, 1.0f };
		// set the global ambient lighting
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

		// White light
		float diffuse0[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		// Dim ambient
		float ambient0[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		// Light position
		float position0[] = { 0f, -1f, 0f, 1.0f };

		// Properties for the light
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
		// enable light
		GL11.glEnable(GL11.GL_LIGHT0);

		// enable lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// ensure that all normals are re-normalised after transformations automatically
		GL11.glEnable(GL11.GL_NORMALIZE);

	}

	protected void checkSceneInput() {// TODO: Check for keyboard and mouse input here
		//When up key is down the background spins faster and the fire out of the exhaust increases
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && speed <= maxSpeed) {
			speed += 0.1f;
			fireHeight -= 0.001f;

		}

		//When key is down the background slows down and the fire gets smaller
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && speed >= minSpeed) {
			speed -= 0.1f;
			fireHeight += 0.001f;
		}

		//When you click the mouse the rocket does a 360 spin in the y axis
		if (Mouse.isButtonDown(0)) {
			spin = true;
			rocketSpin = 0f;

		}

		//When the R key is pressed the code restarts with default variables
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			speed = 0f;
			fireHeight = 1f;
			spin = false;
			rocketSpin = 0.0f;
		}

	}

	protected void updateScene() {
		// TODO: Update your scene variables here - remember to use the current
		// animation scale value
		// (obtained via a call to getAnimationScale()) in your modifications so that
		// your animations
		// can be made faster or slower depending on the machine you are working on
		spaceBoxSpin += speed * getAnimationScale();

		if (spin == true && rocketSpin <= 360f) {
			rocketSpin += 100f * getAnimationScale();
		}

	}

	protected void renderScene() {// TODO: Render your scene here - remember that a scene graph will help you
		// write this method!
		// It will probably call a number of other methods you will write.

		// Applies a texture and gives a rotating animation to the method
		GL11.glPushMatrix();
		{
			// Makes the texture visible
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			Colour.WHITE.submit();

			// Enable texturing and binds a texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, spaceTexture.getTextureID());

			// Rotates the spaceBox
			GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(spaceBoxSpin, -1f, 0f, 0f);
			drawSpaceBox();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{

			GL11.glRotatef(rocketSpin, 0.0f, 1f, 0.0f);

			// Calling method that draws the custom object (rocket)

			rocket();

			// Draw fire
			GL11.glPushMatrix();
			{
				// how shiny are the front faces of the fire
				float fireShininess = 1.0f;
				// specular reflection of the front faces of the fire
				float fireSpecular[] = { 1f, 0.5f, 0f, 1.0f };
				// diffuse reflection of the front faces of the fire
				float fireDiffuse[] = { 1.0f, 0.2f, 0f, 1.0f };

				// set the material properties for the sun using OpenGL
				GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, fireShininess);
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireSpecular));
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(fireDiffuse));

				fire(fireHeight);
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();

	}

	protected void setSceneCamera() {
		// call the default behaviour defined in GraphicsLab. This will set a default
		// perspective projection
		// and default camera settings ready for some custom camera positioning below...
		super.setSceneCamera();

		// TODO: If it is appropriate for your scene, modify the camera's position and
		// orientation here
		// using a call to GL11.gluLookAt(...)

		GLU.gluLookAt(7.0f, 2.5f, 7.0f, 
				0.0f, 0.0f, 0.0f, 
				0.0f, 1.0f, 0.0f);

	}

	protected void cleanupScene() {// TODO: Clean up your resources here
	}

	// method to draw the body of the rocket. Since the built in opengl cylinder is an open ended one we need to close the cylinder.
	private void drawRocketBody() {
		float baseRadius = 0.25f;
		float topRadius = 0.25f;
		float height = 2f;
		int slices = 10;
		int stacks = 10;

		//creates the open ended cylinder
		GL11.glPushMatrix();
		{

			GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
			new Cylinder().draw(baseRadius, topRadius, height, slices, stacks);

		}
		GL11.glPopMatrix();

		//adding two disks on both sides closes it
		GL11.glPushMatrix();
		{
			GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			GL11.glTranslatef(0f, 0f, 0f);
			new Disk().draw(0f, baseRadius, slices, stacks);

		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
			GL11.glTranslatef(0f, 0f, height);
			new Disk().draw(0f, baseRadius, slices, stacks);
		}
		GL11.glPopMatrix();

	}

	// Method to draw the exhaust shape
	private void drawExhaustCylinder() {
		float baseRadius = 0.25f;
		float topRadius = 0.15f;
		float height = 0.5f;
		int slices = 10;
		int stacks = 10;

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(0f, -0.50f, 0.0f);
			GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);

			new Cylinder().draw(baseRadius, topRadius, height, slices, stacks);

		}
		GL11.glPopMatrix();



	}

	// Method for the head of the rocket which is a cone
	private void drawRocketHead() {

		GL11.glPushMatrix();
		{
			float baseRadius = 0f;
			float topRadius = 0.25f;
			float height = 0.5f;
			int slices = 10;
			int stacks = 10;


			GL11.glTranslatef(0f, 2f, 0f);
			GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);

			new Cylinder().draw(topRadius, baseRadius, height, slices, stacks);
		}
		GL11.glPopMatrix();

	}

	// method for the fire that comes out the exhaust
	private void drawFire() {
		GL11.glPushMatrix();
		{
			float baseRadius = 0f;
			float topRadius = 0.14f;
			float height = 1f;
			int slices = 10;
			int stacks = 10;
			GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

			new Cylinder().draw(topRadius, baseRadius, height, slices, stacks);
		}
		GL11.glPopMatrix();
	}

	//method to draw the fin
	private void drawFin() {
		Vertex v1 = new Vertex(0f, 0f,  0.05f);
		Vertex v2 = new Vertex(0f,  0.5f,  0.05f);
		Vertex v3 = new Vertex( 0.3f,  0.8f,  0.05f);
		Vertex v4 = new Vertex( 0.3f, 0.3f,  0.05f);
		Vertex v5 = new Vertex(0f, 0f,  -0.05f);
		Vertex v6 = new Vertex(0f,  0.5f,  -0.05f);
		Vertex v7 = new Vertex(0.3f,  0.8f,  -0.05f);
		Vertex v8 = new Vertex( 0.3f, 0.3f,  -0.05f);

		// draw the near face:
			GL11.glBegin(GL11.GL_POLYGON);
			{


				v3.submit();
				v2.submit();
				v1.submit();
				v4.submit();
			}
			GL11.glEnd();

			// draw the left face:
			GL11.glBegin(GL11.GL_POLYGON);
			{


				v2.submit();
				v6.submit();
				v5.submit();
				v1.submit();
			}
			GL11.glEnd();

			// draw the right face:
			GL11.glBegin(GL11.GL_POLYGON);
			{


				v7.submit();
				v3.submit();
				v4.submit();
				v8.submit();
			}
			GL11.glEnd();

			// draw the top face:
			GL11.glBegin(GL11.GL_POLYGON);
			{


				v7.submit();
				v6.submit();
				v2.submit();
				v3.submit();
			}
			GL11.glEnd();

			// draw the bottom face:
			GL11.glBegin(GL11.GL_POLYGON);
			{


				v4.submit();
				v1.submit();
				v5.submit();
				v8.submit();
			}
			GL11.glEnd();

			// draw the far face:
			GL11.glBegin(GL11.GL_POLYGON);
			{


				v6.submit();
				v7.submit();
				v8.submit();
				v5.submit();
			}
			GL11.glEnd();



	}



	// Method to draw the box in which the rocket will be in
	private void drawSpaceBox() {
		Vertex v1 = new Vertex(-10.5f, -9f, 10.5f);
		Vertex v2 = new Vertex(-10.5f, 12f, 10.5f);
		Vertex v3 = new Vertex(10.5f, 12f, 10.5f);
		Vertex v4 = new Vertex(10.5f, -9f, 10.5f);
		Vertex v5 = new Vertex(-10.5f, -9f, -10.5f);
		Vertex v6 = new Vertex(-10.5f, 12f, -10.5f);
		Vertex v7 = new Vertex(10.5f, 12f, -10.5f);
		Vertex v8 = new Vertex(10.5f, -9f, -10.5f);

		//Here I write the code for the cube that the rocket will be in. 
		//The faces are made in reverse so that the interior gets texture instead of the exterior. 

		//front face 
		GL11.glBegin(GL11.GL_POLYGON);
		{

			new Normal(v4.toVector(), v1.toVector(), v2.toVector(), v3.toVector()).submit();
			GL11.glTexCoord2d(1.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v3.submit();
		}
		GL11.glEnd();

		//left face

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v5.toVector(), v6.toVector(), v2.toVector()).submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v5.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
		}
		GL11.glEnd();

		//right face:

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v8.toVector(), v4.toVector(), v3.toVector(), v7.toVector()).submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v7.submit();
		}
		GL11.glEnd();

		//top face

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(), v2.toVector(), v6.toVector(), v7.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v7.submit();
		}
		GL11.glEnd();

		//bottom face

		GL11.glBegin(GL11.GL_POLYGON);
		{
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v5.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
		}
		GL11.glEnd();

		//far face

		GL11.glBegin(GL11.GL_POLYGON);
		{

			new Normal(v6.toVector(), v7.toVector(), v8.toVector(), v5.toVector()).submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v5.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v6.submit();
		}
		GL11.glEnd();
	}




	//method that combines all the parts of the rocket and applies a consistant shader 
	private void rocket() {
		GL11.glPushMatrix();
		// how shiny are the front faces of the fire
		float Shininess = 0.0f;
		// specular reflection of the front faces of the fire
		float Specular[] = { 0f, 0f, 0f, 0.0f };
		// diffuse reflection of the front faces of the fire
		float Diffuse[] = { 1.0f, 1f, 1f, 1.0f };

		// set the material properties for the sun using OpenGL
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));

		// Body of the rocket

		drawRocketBody();

		// Exhausts of the rocket

		drawExhaustCylinder();

		// Head of the rocket

		drawRocketHead();

		// Fins of the rocket
		GL11.glPushMatrix();
		{
			GL11.glRotatef(-45, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(-0.5f, -0.3f, 0f);
			drawFin();
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		{
			GL11.glRotatef(45, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(-0.5f, -0.3f, 0f);
			drawFin();
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glRotatef(225, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(-0.5f, -0.3f, 0f);
			drawFin();
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		{
			GL11.glRotatef(-225, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(-0.5f, -0.3f, 0f);
			drawFin();
		}
		GL11.glPopMatrix();

		GL11.glPopMatrix();
	}

	// Method for the fire that will come out of the exhaust. This will take in a variable that changes the possition of the fire.
	private void fire(float height) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0f, height, 0.0f);
		drawFire();
		GL11.glPopMatrix();



	}

}
