import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Triangle {
    private VAO vao;
    private VBO vbo;
    private ShaderProgram shaderProgram;
    private final float[] vertices = {
            // Vertex positions for a simple equilateral triangle
            0.0f, 0.5f, 0.0f,  // Top
            -0.5f, -0.5f, 0.0f, // Bottom left
            0.5f, -0.5f, 0.0f  // Bottom right
    };
    private int verticesCount = vertices.length / 3;
    private final String vertexShaderPath = "src/main/resources/triangleVertexShader.glsl";
    private final String fragmentShaderPath = "src/main/resources/triangleFragmentShader.glsl";

    public Triangle() {
        Shader vertexShader, fragmentShader;

        vertexShader = Shader.loadShader(GL_VERTEX_SHADER, vertexShaderPath);
        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, fragmentShaderPath);

        shaderProgram = new ShaderProgram();
        shaderProgram.attachShaders(vertexShader, fragmentShader);
        shaderProgram.link();
        shaderProgram.use();

        vao = new VAO();
        vao.bind();

        vbo = new VBO(vertices);
        vbo.bind();

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            FloatBuffer vertexBuffer = memoryStack.mallocFloat(vertices.length);
            vertexBuffer.put(vertices).flip();
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        }

        int posAttrib = shaderProgram.getAttributeLocation("position");
        shaderProgram.enableVertexAttribute(posAttrib);
        shaderProgram.pointVertexAttribute(posAttrib, 2, 3 * Float.BYTES, 0);

        int colourLocation = shaderProgram.getUniformLocation("color");
        Vector3f color = new Vector3f(1.0f, 0.0f, 0.0f);
        shaderProgram.setUniform(colourLocation, color);

        vbo.unBind();
        vao.unBind();
    }

    public void render() {
        shaderProgram.use();
        vao.bind();

        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println("Triangle error: " + error);
        }

        glDrawArrays(GL_TRIANGLES, 0, verticesCount);

        vao.unBind();
        shaderProgram.unUse();
    }
}
