#version 330 core

in vec3 vColor;
in vec3 vWorldPos;
in vec3 vNormal;

out vec4 FragColor;

uniform int  uLightMode;        // 0 = sun, 1 = point, 2 = rainbow point
uniform vec3 uLightDir;
uniform vec3 uLightColor;
uniform vec3 uPointLightPos;
uniform float uLightIntensity;
uniform vec3 uViewPos;

void main() {
    vec3 N = normalize(vNormal);

    // Choose light direction L (direction from surface TO light)
    vec3 L;

    if (uLightMode == 0) {
        // Directional sunlight: uLightDir is direction the light points (like "downwards")
        L = normalize(-uLightDir);
    } else {
        // Point light: vector from surface to light position
        L = normalize(uPointLightPos - vWorldPos);
    }

    vec3 V = normalize(uViewPos - vWorldPos);

    // Ambient
    vec3 ambient = 0.12 * uLightColor;

    // Diffuse
    float diff = max(dot(N, L), 0.0);
    vec3 diffuse = diff * uLightColor;

    // Specular (Blinn-Phong)
    vec3 H = normalize(L + V);
    float spec = pow(max(dot(N, H), 0.0), 32.0);
    vec3 specular = 0.35 * spec * uLightColor;

    // Point light attenuation (only for point modes)
    float attenuation = 1.0;
    if (uLightMode == -1) {
        FragColor = vec4(vColor, 1.0);
        return;
    }
    
    if (uLightMode != 0) {
        float d = length(uPointLightPos - vWorldPos);
        attenuation = 1.0 / (1.0 + 0.09*d + 0.032*d*d); // classic quadratic falloff
    }

    vec3 lit = (ambient + attenuation * (diffuse + specular) * uLightIntensity) * vColor;
    FragColor = vec4(lit, 1.0);
}