#version 330 core

in vec3 vColor;
in vec3 vWorldPos;
in vec3 vNormal;

out vec4 FragColor;

uniform vec3 uLightDir;   // direction TO light? we'll treat as "from light"
uniform vec3 uLightColor;
uniform vec3 uViewPos;

void main() {
    vec3 N = normalize(vNormal);
    vec3 L = normalize(-uLightDir); // light direction points "down", so invert
    vec3 V = normalize(uViewPos - vWorldPos);

    // Ambient
    vec3 ambient = 0.15 * uLightColor;

    // Diffuse
    float diff = max(dot(N, L), 0.0);
    vec3 diffuse = diff * uLightColor;

    // Specular (Blinn-Phong)
    vec3 H = normalize(L + V);
    float spec = pow(max(dot(N, H), 0.0), 32.0);
    vec3 specular = 0.35 * spec * uLightColor;

    vec3 lit = (ambient + diffuse + specular) * vColor;
    FragColor = vec4(lit, 1.0);
}