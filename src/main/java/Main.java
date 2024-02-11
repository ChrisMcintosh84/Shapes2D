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

        VAO vao = new VAO();
        vao.bind();

        Triangle triangle = new Triangle();

        VBO vbo = new VBO(triangle.getVertices());
        vbo.bind();

        Shader vertexShader;
        vertexShader = Shader.loadShader(GL_VERTEX_SHADER, triangle.getVertexShaderPath());

        Shader fragmentShader;
        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, triangle.getFragmentShaderPath());

        ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.attachShaders(vertexShader, fragmentShader);
        shaderProgram.link();
        shaderProgram.use();

        int posAttrib = glGetAttribLocation(shaderProgram.getId(), "position");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);

        int colourLocation = glGetUniformLocation(shaderProgram.getId(), "color");
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = memoryStack.mallocFloat(3);
            floatBuffer.put(1.0f).put(0.0f).put(0.0f).flip();

            glUniform3fv(colourLocation, floatBuffer);
        }

        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            shaderProgram.use();
            vao.bind();
            glDrawArrays(GL_TRIANGLES, 0, 3);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        vao.unBind();
        glDeleteProgram(shaderProgram.getId());

        glfwTerminate();
    }
}