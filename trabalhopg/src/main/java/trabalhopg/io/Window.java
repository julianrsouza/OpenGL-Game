package trabalhopg.io;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

public class Window {
	private long window;
	private int width = 800;
	private int height = 600;
	private Input input;
	
	public Window() {
		setSize(width, height);
	}
	
	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallback() {
			
			@Override
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		}); 
	}
	
	public void createWindow(String title) {
		window = glfwCreateWindow(width, height, title, 0, 0);
		if (window == 0) {
			throw new IllegalStateException("Falha ao inicializar a janela GLFW");
		}
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) /2);
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		input = new Input(window);
	}
	
	public void update() {
		input.update();
		glfwPollEvents();
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public long getWindow() {
		return window;
	}

	public void setWindow(long window) {
		this.window = window;
	}
	
	public Input getInput() {
		return input;
	}
}
