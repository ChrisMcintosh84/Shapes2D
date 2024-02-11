public class Triangle {
    private final float[] vertices = {
            // Vertex positions for a simple equilateral triangle
            0.0f, 0.5f, 0.0f,  // Top
            -0.5f, -0.5f, 0.0f, // Bottom left
            0.5f, -0.5f, 0.0f  // Bottom right
    };
    private final String vertexShaderPath = "src/main/resources/triangleVertexShader.glsl";
    private final String fragmentShaderPath = "src/main/resources/triangleFragmentShader.glsl";

    public float[] getVertices() {
        return vertices;
    }

    public String getVertexShaderPath() {
        return vertexShaderPath;
    }

    public String getFragmentShaderPath() {
        return fragmentShaderPath;
    }
}
