#version 330 core

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec4 aCol;

uniform mat4 uOrtho;
uniform float uScale;   // NEW

out vec4 vColor;

void main() {
    vColor = aCol;

    vec2 scaled = aPos * uScale;          // scale up in pixel space
    gl_Position = uOrtho * vec4(scaled, 0.0, 1.0);
}