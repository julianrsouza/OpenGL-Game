package trabalhopg;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import trabalhopg.eventlisteners.KeyListener;
import trabalhopg.eventlisteners.MouseListener;
import trabalhopg.exception.NotFoundException;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 600;
	private String title;
	private long glfwWindow;
	private List< GameObject > gameObjects = new ArrayList<>();
	
	private static Window window = null;
	
	public Window() {
		this.title = "Game Window";
	}
	
	public static Window get() {
		if ( Window.window == null ) {
			Window.window = new Window();
		}
		
		return Window.window;
	}
	
	public void run() {
		init();
		loop();
		
		glfwFreeCallbacks( glfwWindow );
		glfwDestroyWindow( glfwWindow );
		
		glfwTerminate();
		glfwSetErrorCallback( null ).free();
	}
	
	public void init() {
		GLFWErrorCallback.createPrint( System.err ).set();
		//Inicializar o GLFW
		if ( !glfwInit() ) {
			throw new IllegalStateException( "Não foi possível inicializar o GLFW." ); 
		}
		
		//Configurar o GLFW
		glfwDefaultWindowHints();
		glfwWindowHint( GLFW_VISIBLE, GLFW_FALSE );
		glfwWindowHint( GLFW_RESIZABLE, GLFW_TRUE );
		glfwWindowHint( GLFW_MAXIMIZED, GLFW_TRUE );
		
		//Criar a janela
		glfwWindow = glfwCreateWindow( this.WINDOW_WIDTH, this.WINDOW_HEIGHT, this.title, NULL, NULL );
		
		if ( glfwWindow == NULL ) {
			throw new IllegalStateException( "Não foi possível criar a janela do GLFW." );
		}
		
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback( glfwWindow, KeyListener::keyCallback );
		
		//Fazer o contexto atual do OpenGL
		glfwMakeContextCurrent( glfwWindow );
		GL.createCapabilities();
		
		//Ativar o v-sync
		glfwSwapInterval( 1 );
		//Deixar a janela visível
		glfwShowWindow( glfwWindow );
	}
	
	public void loop() {
		initObject( "Player", new float[]{ -0.8f, -0.8f, -0.6f, -0.8f, -0.6f, -0.4f, -0.8f, -0.4f } );
		initObject( "Ground", new float[]{ -1f, -1f, 1f, -1f, 1f, -0.8f, -1f, -0.8f } );
		while ( !terminateGame() ) {      
			glfwPollEvents();
			glClearColor( 1.0f, 1.0f, 1.0f, 1.0f);
			glClear( GL_COLOR_BUFFER_BIT );
			try {
				makePlayerActions();
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
			renderObjects();
			glfwSwapBuffers( glfwWindow );
		}
	}
	
	private GameObject getPlayer() {
		GameObject player = new GameObject();
		try {
			player = getGameObject( "Player" );
		} catch ( NotFoundException e ) {
			System.out.println( e.getMessage() );
			System.exit( 1 );
		}
		return player;
	}
	
	private void makePlayerActions() throws NotFoundException {
		GameObject player = getPlayer();
		float gravity = -0.1f;
		float maxJumpHeight = 0f;
		if ( KeyListener.isKeyPressed( GLFW_KEY_D ) ) {
			player.setSpeedPositionX( 0.05f );
		}
		else if ( KeyListener.isKeyPressed( GLFW_KEY_A ) ) {
			player.setSpeedPositionX( -0.05f );
		}
		else {
			player.setSpeedPositionX( 0f );
		}
		
		if ( KeyListener.isKeyPressed( GLFW_KEY_W ) ) {
			player.setSpeedPositionY( 0.2f );
		} else {
			if( player.checkCollision( this.getGameObject( "Ground" ) ) ) {
				player.setSpeedPositionY( 0f );
			} else {
				player.setSpeedPositionY( player.getSpeedPositionY() + gravity );
			}
		}
		player.move();
	}
	
	private GameObject getGameObject( String name ) throws NotFoundException {
		Optional< GameObject > gameObject = gameObjects.stream().filter( object -> name.equals( object.getObjectName() ) )
				.findFirst();
		if ( !gameObject.isPresent() ) {
			throw new NotFoundException( "Objeto " + name + " não encontrado." );
		}
		return gameObject.get();
	}
	
	private void initObject( String objectName, float[] vertices ) {
		gameObjects.add( new GameObject( objectName, vertices, 0, 0 ) );
	}
	
	private void renderObjects() {
		for ( GameObject gameObject : gameObjects ) {
			gameObject.render();
		}
	}
	
	private boolean terminateGame() {
		return glfwWindowShouldClose( glfwWindow ) || KeyListener.isKeyPressed( GLFW_KEY_ESCAPE );
	}
}
