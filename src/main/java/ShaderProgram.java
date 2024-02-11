import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {
    private int id;

    public ShaderProgram() {
        id = glCreateProgram();
    }

    public void attachShaders(Shader vertexShader, Shader fragmentShader) {
        glAttachShader(id, vertexShader.getId());
        glAttachShader(id, fragmentShader.getId());
    }

    public void link() {
        glLinkProgram(id);

        checkStatus();
    }

    public void use() {
        glUseProgram(id);
    }

    public void unUse() {
        glUseProgram(0);
    }

    public int getId() {
        return id;
    }

    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(id, name);
    }

    public void enableVertexAttribute(int location) {
        glEnableVertexAttribArray(location);
    }

    public void disableVertexAttribute(int location) {
        glDisableVertexAttribArray(location);
    }

    public void pointVertexAttribute(int index, int size, int stride, int pointer) {
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride, pointer);
    }

    public int getUniformLocation(CharSequence name) {
        return glGetUniformLocation(id, name);
    }

    public void setUniform(int location, int value) {
        glUniform1i(location, value);
    }

    public void setUniform(int location, Vector2f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2);
            buffer.put(value.x).put(value.y);
            buffer.flip();
            glUniform2fv(location, buffer);
        }
    }

    public void setUniform(int location, Vector3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3);
            buffer.put(value.x).put(value.y).put(value.z);
            buffer.flip();
            glUniform3fv(location, buffer);
        }
    }

    public void setUniform(int location, Vector4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4);
            buffer.put(value.x).put(value.y).put(value.z).put(value.w);
            buffer.flip();
            glUniform4fv(location, buffer);
        }
    }

    public void setUniform(int location, Matrix2f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2 * 2);
            matrix.get(buffer);
            glUniformMatrix2fv(location, false, buffer);
        }
    }

    public void setUniform(int location, Matrix3f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3 * 3);
            matrix.get(buffer);
            glUniformMatrix3fv(location, false, buffer);
        }
    }

    public void setUniform(int location, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = new Matrix4f()
                    .get(stack.mallocFloat(16));
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void delete() {
        glDeleteProgram(id);
    }

    public void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }
}
