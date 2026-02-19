#version 330 core

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec4 aCol;

uniform mat4 uOrtho;
uniform float uScale;

out vec4 vColor;

void main() {
    vColor = aCol;

    vec2 scaled = aPos * uScale;          // Scale up in pixel space
    gl_Position = uOrtho * vec4(scaled, 0.0, 1.0);
}