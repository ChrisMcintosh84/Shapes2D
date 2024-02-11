import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL33.glViewport;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class Renderer {
    private static MyDebugCallback debugCallback = new MyDebugCallback();
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private long window;
    Triangle triangle;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "Renderer", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glViewport(0, 0, WIDTH, HEIGHT);

        glDebugMessageCallback(debugCallback, 0);
        glEnable(GL_DEBUG_OUTPUT);

        triangle = new Triangle();

        while (!glfwWindowShouldClose(window)) {
//            input();
            glClear(GL_COLOR_BUFFER_BIT);
            triangle.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println("OpenGL error: " + error);
        }
    }

    private void input() {

    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
