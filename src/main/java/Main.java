import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    static MyDebugCallback debugCallback = new MyDebugCallback();
    private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scanCode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static int shaderId;

    private static float[] vertices = {
            // Vertex positions for a simple equilateral triangle
            0.0f, 0.5f, 0.0f,  // Top
            -0.5f, -0.5f, 0.0f, // Bottom left
            0.5f, -0.5f, 0.0f  // Bottom right
    };

    private static String vertexShaderString = """
            #version 450 core

            in vec3 position;

            void main() {
                gl_Position = vec4(position, 1.0);
            }""";
    private static String fragmentShaderString = """
            #version 450 core

            out vec4 fragColor;

            uniform vec3 color;

            void main() {
                fragColor = vec4(color, 1.0);
            }""";
    private static int vao;
    private static int vbo;

    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        long window = glfwCreateWindow(WIDTH, HEIGHT, "Test Render", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0, 0, WIDTH, HEIGHT);

        glDebugMessageCallback(debugCallback, 0);
        glEnable(GL_DEBUG_OUTPUT);

        glfwSetKeyCallback(window, keyCallback);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderString);
        glCompileShader(vertexShader);

        int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(vertexShader));
        }

        checkGLError();

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderString);
        glCompileShader(fragmentShader);

        status = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
        }

        checkGLError();

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glUseProgram(shaderProgram);

        status = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
        }

        checkGLError();

        int posAttrib = glGetAttribLocation(shaderProgram, "position");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);

        int colourLocation = glGetUniformLocation(shaderProgram, "color");
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = memoryStack.mallocFloat(3);
            floatBuffer.put(1.0f).put(0.0f).put(0.0f).flip();

            glUniform3fv(colourLocation, floatBuffer);
        }

        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glUseProgram(shaderProgram);
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, 3);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glBindVertexArray(0);
        glDeleteProgram(shaderProgram);

        glfwTerminate();
    }

    public static void checkGLError() {
        int error = glGetError();
        while (error != GL_NO_ERROR) {
            System.err.println("OpenGL error: " + error);
            error = glGetError();
        }
    }
}
