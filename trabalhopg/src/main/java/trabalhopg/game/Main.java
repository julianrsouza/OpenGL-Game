package trabalhopg.game;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL;

import trabalhopg.Collision;
import trabalhopg.GameObject;
import trabalhopg.ParallaxLayer;
import trabalhopg.io.Window;
import trabalhopg.render.Model;
import trabalhopg.render.Texture;

public class Main {
	private static List<GameObject> objects = new ArrayList<>();
	private static GameObject player;
	private static float[] enemyVerticesA = new float[] {
			0.3f, 0.3f, 0, //top left        0
            0.9f, 0.3f, 0, //top right        1
            0.9f, -0.3f, 0, //bottom right    2
            0.3f, -0.3f, 0, //bottom left    3
	};
	private static float[] enemyVerticesB = new float[] {
			0.3f, 0.9f, 0, //top left        0
            0.9f, 0.9f, 0, //top right        1
            0.9f, 0.3f, 0, //bottom right    2
            0.3f, 0.3f, 0, //bottom left    3
	};
	private static float[] enemyVerticesC = new float[] {
			0.3f, -0.3f, 0, //top left        0
            0.9f, -0.3f, 0, //top right        1
            0.9f, -0.9f, 0, //bottom right    2
            0.3f, -0.9f, 0, //bottom left    3
	};

	private static GameObject initializePlayer() {
	    float[] vertices = new float[] {
	            -0.9f, 0.3f, 0, //top left        0
	            -0.3f, 0.3f, 0, //top right        1
	            -0.3f, -0.3f, 0, //bottom right    2
	            -0.9f, -0.3f, 0, //bottom left    3
	    };

	    float[] texturesCoords = new float[] {
	            0, 1,
	            1, 1,
	            1, 0,
	            0, 0,
	    };

	    int[] indices = new int[] {
	            0, 1, 2,
	            2, 3, 0
	    };
	    Model model = new Model(vertices, texturesCoords, indices);
	    Texture tex = new Texture("/home/syonet/github/trabalhopg/trabalhopg/textures/Fighter/Idle.png");
	    float initialX = (vertices[0] + vertices[6]) / 2;
        float initialY = (vertices[1] + vertices[3]) / 2;
	    return new GameObject(model, tex, 0, 0, 0.6f, 0.6f); 
	}
    
    private static void initializeEnemy(float[] vertices) {
    	float[] texturesCoords = new float[] {
                0, 0,
                1, 0,
                1, 1,
                0, 1,
        };

        int[] indices = new int[] {
                0, 1, 2,
                2, 3, 0
        };
        Model model = new Model(vertices, texturesCoords, indices);
        Texture tex = new Texture("/home/syonet/github/trabalhopg/trabalhopg/textures/Fighter/Idle.png");
        float initialX = (vertices[0] + vertices[6]) / 2;
        float initialY = (vertices[1] + vertices[3]) / 2;
        objects.add(new GameObject(model, tex, 0, 0, 0.6f, 0.6f));
    }
    
    private static float[] randomizeEnemyArrayUsage(float[] enemyVerticesA, float[] enemyVerticesB, float[] enemyVerticesC) {
    	Random random = new Random();
    	int randomNumber = random.nextInt(3) + 1;
    	switch (randomNumber) {
	    	case 1:
	    		return enemyVerticesA;
	    	case 2:
	    		return enemyVerticesB;
	    	case 3:
	    		return enemyVerticesC;
    	}
    	return enemyVerticesA;
    }
    
	public static void main(String[] args) {
		Window.setCallbacks();
		int loopCounter = 0;
		float enemySpeed = -0.01f;
		if (!glfwInit()) {
			throw new IllegalStateException("Falha ao inicializar o GLFW");
		} 
		
		Window window = new Window();
		window.createWindow("Game");
		
		GL.createCapabilities();
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		player = initializePlayer();
		initializeEnemy(enemyVerticesB);
		ParallaxLayer backgroundLayer = new ParallaxLayer(0.01f, "/home/syonet/github/trabalhopg/trabalhopg/textures/Background/background_1.png");
		long lastFrameTime = System.currentTimeMillis(); 
		float deltaTime = 0.0f;
		while(!window.shouldClose()) {
			long currentFrameTime = System.currentTimeMillis();
		    float elapsedTime = (currentFrameTime - lastFrameTime) / 1000.0f; 

		    lastFrameTime = currentFrameTime; 
		    deltaTime = elapsedTime;
			window.update();
			if ( window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
				glfwSetWindowShouldClose(window.getWindow(), true);
			}
			loopCounter++;
			if (loopCounter % 300 == 0) {
				initializeEnemy(randomizeEnemyArrayUsage(enemyVerticesA, enemyVerticesB, enemyVerticesC));
			}
			if (loopCounter % 1800 == 0) {
				enemySpeed *= 1.2f;
			}
			backgroundLayer.update(deltaTime);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			player.update(window.getInput());
			player.render();
			for (GameObject obj : objects) {
				obj.moveAutomatically(enemySpeed);
				obj.render();
//				if(Collision.checkCollision(player, obj)) {
//					glfwSetWindowShouldClose(window.getWindow(), true);
//				}
			}
			backgroundLayer.render(window.getWidth());
			window.swapBuffers();
		}
		
		glfwTerminate();
	}
}
