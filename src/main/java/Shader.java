import java.io.*;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
    private final int id;

    public Shader(int type) {
        id = glCreateShader(type);
    }

    public void source(CharSequence source) {
        glShaderSource(id, source);
    }

    public void compile() {
        glCompileShader(id);

        checkStatus();
    }

    public void delete() {
        glDeleteShader(id);
    }

    public int getId() {
        return id;
    }

    public static Shader createShader(int type, CharSequence source) {
        Shader shader = new Shader(type);
        shader.source(source);
        shader.compile();

        return shader;
    }

    public static Shader loadShader(int type, String path) {
        StringBuilder stringBuilder = new StringBuilder();

        try (InputStream inputStream = new FileInputStream(path);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load shader file" + System.lineSeparator() + e.getMessage());
        }
        CharSequence source = stringBuilder.toString();

        return createShader(type, source);
    }

    public void checkStatus() {
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(id));
        }
    }
}
