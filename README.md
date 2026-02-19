# 3D Graphics Programming (LWJGL Renderer)

A small 3D rendering application built in **Java + LWJGL (OpenGL 3.3 core)** for **COM6025M 3D Graphics Programming**.

The project demonstrates the 3D rendering pipeline using **VBO / VAO / EBO**, **vertex + fragment shaders**, a movable **FPS-style camera**, multiple **lighting modes**, **texture mapping**, and simple **object animation**, with an on-screen UI showing controls and current lighting state.

## Project Details

- **Language:** Java
- **Build Tool:** Maven
- **Libraries:** LWJGL (OpenGL + GLFW), STB (STBEasyFont, STBImage)
- **Renderer:** OpenGL 3.3 Core Profile
- **Platform Target:** Windows (tested)

## Features (Core + Extensions)

### Core system (pass requirements)
- Animated 3D objects rendered using a camera (multiple cubes)
- GPU pipeline via **VBO / VAO / EBO** + indexed drawing
- Custom **Vertex Shader** + **Fragment Shader**

### Extensions implemented
- **Movable camera** (WASD + mouse look + vertical movement)
- **Lighting**  
  - Directional “sun” light
  - Moving point light
  - Moving rainbow point light (animated colour)
  - Toggle modes on / off via keypress + adjustable intensity (point / rainbow)
- **Texturing**
  - Texture mapping using STBImage (brick texture)
  - Mix of textured and coloured objects
- **Animation**
  - Per-object rotation
  - Additional scene animation (bobbing + scaling variations)
- **Basic UI overlay**
  - On-screen controls + lighting status (STBEasyFont)

## Controls

### Lighting (press key again to toggle on/off)
- **1** — Sunlight (Directional)
- **2** — Point Light (Moving)
- **3** — Rainbow Point Light (Moving)
- **Mouse Wheel** — Change intensity (Point / Rainbow only)

### Camera
- **W / A / S / D** — Forward / Left / Back / Right
- **Mouse** — Look (yaw / pitch)
- **SPACE** — Up
- **LEFT SHIFT** — Down
- **LEFT CTRL** — Sprint
- **R** — Reset camera
- **ESC** — Quit

## Folder Structure (inside `/src/main/`)

- `/java/` – Java source code (all engine / app logic):
    - `/com/tybingham/graphics/app/` – application + windowing + timing + input
        - `App.java` – main game / app loop (update -> render)
        - `Input.java` – keyboard + mouse + scroll handling (FPS controls + toggles)
        - `Time.java` – delta-time tracking for smooth movement / animation
        - `Window.java` – GLFW window creation, OpenGL context setup, swap / clear, input hookup
    - `/com/tybingham/graphics/math/` – maths utilities for 3D rendering
        - `Mat4.java` – 4x4 matrices (perspective, lookAt, translation / rotation / scale, multiply)
        - `Vec3.java` – 3D vector operations (positions, directions, normals, camera vectors)
    - `/com/tybingham/graphics/render/` – rendering layer (OpenGL + shaders + meshes + textures + UI)
        - `Camera.java` – FPS-style camera (yaw / pitch, movement, view matrix)
        - `Mesh.java` – VAO / VBO / EBO wrapper + draw + cleanup
        - `MeshFactory.java` – creates reusable geometry (e.g., cube vertex / index data)
        - `Renderer.java` – main 3D renderer (sets uniforms, lighting modes, draws objects)
        - `ShaderProgram.java` – shader compile / link + uniform lookups
        - `Texture.java` – texture loading via STBImage + OpenGL texture setup / binding
        - `UiRenderer.java` – on-screen text overlay using STBEasyFont (controls + light status)
        - `Utils.java` – helper functions (resource loading: shader text + binary for textures)
    - `/com/tybingham/graphics/scene/` – scene graph / object setup + per-frame animation
        - `DemoScene.java` – creates the cubes, assigns colours / textures, updates animations
        - `Object3D.java` – renderable object (mesh + transform + colour / texture flags)
        - `Scene.java` – scene interface (init / update / destroy lifecycle)
        - `Transform.java` – position / rotation / scale -> model matrix
    - `Main.java` – program entry point (starts App)

- `/resources/` – packaged assets loaded at runtime (via classpath):
    - `/shaders/` – GLSL shader files
        - `basic.vert / basic.frag` – main 3D shader pair (lighting + texturing)
        - `ui.vert / ui.frag` – UI overlay shader pair (text rendering)
    - `/textures/` – texture images
        - `brick.jpg` – brick texture used for textured cube

## How to Run the Project

### Requirements
- **Java (JDK 24 tested)**
- **Maven**

### Run

1. Install **JDK 24** (or newer) and ensure `java -version` works in your terminal.
2. Install **Maven** and ensure `mvn -version` works in your terminal.
3. Download / clone this repository to your machine.
4. Open a terminal in the **root folder** of the project (where `pom.xml` is).
5. Run the game:

   ```bash
   mvn clean compile exec:java

- The build will show BUILD SUCCESS after the game exits normally.

## Known Warnings (JDK 24)

- When running on newer Java versions (e.g., JDK 24), you may see warnings like:
    WARNING: A terminally deprecated method in sun.misc.Unsafe has been called ...
    WARNING: sun.misc.Unsafe::staticFieldBase has been called by com.google.inject.internal.aop.HiddenClassDefiner ...
    WARNING: Please consider reporting this to the maintainers of class ...
    WARNING: sun.misc.Unsafe::staticFieldBase will be removed in a future release
    [LWJGL] [ThreadLocalUtil] Unsupported JNI version detected ...
    These warnings originate from dependencies (Maven / Guice and LWJGL runtime checks) on newer JDKs. The program runs correctly despite these messages.

## Third-Party Assets and Licensing Summary

The following assets were used under appropriate copyright-free or Creative Commons licenses.

| Asset Type          | Source / License                                           | Usage                                  | Modifications (if any)               |
|---------------------|------------------------------------------------------------|----------------------------------------|--------------------------------------|
| Texture             | Poly Haven (CC0) https://polyhaven.com/a/brick_pavement_03 | Cube Texture                           | renamed to brick.jpg                 |