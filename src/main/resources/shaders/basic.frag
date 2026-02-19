#version 330 core

in vec3 vColor;
in vec3 vWorldPos;
in vec3 vNormal;
in vec2 vUV;

out vec4 FragColor;

uniform int  uUseTexture;
uniform vec3 uTint;
uniform int  uLightMode;        // 0 = Sun, 1 = Point, 2 = Rainbow Point
uniform vec3 uLightDir;
uniform vec3 uLightColor;
uniform vec3 uPointLightPos;
uniform float uLightIntensity;
uniform vec3 uViewPos;

uniform sampler2D uTex0;

void main() {
    vec3 N = normalize(vNormal);

    vec3 L;
    if (uLightMode == 0) {
        L = normalize(-uLightDir);
    } else {
        L = normalize(uPointLightPos - vWorldPos);
    }

    vec3 V = normalize(uViewPos - vWorldPos);

    vec3 ambient = 0.12 * uLightColor;

    float diff = max(dot(N, L), 0.0);
    vec3 diffuse = diff * uLightColor;

    vec3 H = normalize(L + V);
    float spec = pow(max(dot(N, H), 0.0), 32.0);
    vec3 specular = 0.35 * spec * uLightColor;

    float attenuation = 1.0;
    if (uLightMode != 0) {
        float d = length(uPointLightPos - vWorldPos);
        attenuation = 1.0 / (1.0 + 0.09*d + 0.032*d*d);
    }

    // Base colour (always available)
    vec3 baseColor = vColor * uTint;

    // Choose texture or plain colour
    vec3 albedo = baseColor;
    if (uUseTexture == 1) {
        vec3 tex = texture(uTex0, vUV).rgb;
        albedo = tex * baseColor;
    }

    vec3 lit = (ambient + attenuation * (diffuse + specular) * uLightIntensity) * albedo;
    FragColor = vec4(lit, 1.0);
}