# POOBSoccer — HeadSoccer

Juego de fútbol 1 contra 1 con vista lateral, desarrollado en **Java + Swing** para la asignatura de Programación Orientada a Objetos.

---

## Estado actual

| Elemento | Estado |
|---|---|
| Proyecto Java y ventana Swing | Listo |
| `HeadSoccerGUI` con `CardLayout` | Listo |
| Enums del dominio | Listos |
| Contrato de `Partido` | Listo (métodos vacíos) |
| Esqueletos del dominio | Listos |
| Pruebas unitarias | Pendientes |
| Lógica del juego | Pendiente (siguiente: A1) |

La ventana se abre maximizada y todavía no muestra pantallas de juego.

---

## Requisitos

- **JDK 17 o superior** (probado con JDK 26).
- Terminal de PowerShell (Windows) o cualquier shell con `javac` y `java` en el `PATH`.
- **JUnit 5** incluido en `lib/`:
  - `lib/junit-platform-console-standalone-1.10.2.jar`
- No se necesita Maven ni Gradle.

---

## Estructura del proyecto

```text
HeadSoccer/
├── src/
│   ├── domain/            # Modelo del juego (sin Swing)
│   │   ├── Partido.java
│   │   ├── Jugador.java
│   │   ├── Personaje.java
│   │   ├── Balon.java
│   │   ├── Marcador.java
│   │   ├── ResultadoPartido.java
│   │   ├── Modalidad.java
│   │   ├── TipoPartido.java
│   │   └── EstadoPartido.java
│   ├── presentation/      # Interfaz gráfica
│   │   └── HeadSoccerGUI.java
│   └── test/              # Pruebas unitarias (JUnit 5)
├── lib/                   # Dependencias versionadas (JUnit)
├── bin/                   # Salida de compilación (no se versiona)
└── README.md
```

**Convención:** los paquetes `domain` y `presentation` son **planos**; no se crean subpaquetes dentro de ellos.

---

## Compilar

Desde la raíz del proyecto, en PowerShell:

```powershell
$files = Get-ChildItem -Path src/domain, src/presentation -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $files
```

> Solo se compila el código principal (`domain` y `presentation`). Las pruebas se compilan aparte porque necesitan el `.jar` de JUnit.

Si la compilación termina sin errores, las clases quedan en `bin/`.

### Limpiar la compilación

Si hay clases viejas o un error extraño, se puede borrar `bin/` y volver a compilar:

```powershell
Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
```

---

## Ejecutar

```powershell
java -cp bin presentation.HeadSoccerGUI
```

Se abre una ventana maximizada titulada `HeadSoccer`.

---

## Pruebas unitarias

El proyecto usa **JUnit 5** mediante el `.jar` standalone de `lib/`. Las pruebas viven en `src/test/`.

### 1. Compilar las pruebas

```powershell
$testFiles = Get-ChildItem -Recurse -Filter *.java src/test | ForEach-Object { $_.FullName }
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar;bin" -d bin $testFiles
```

> En Windows el separador de classpath es `;`. En Linux/macOS es `:`.

### 2. Ejecutarlas

```powershell
java -jar "lib/junit-platform-console-standalone-1.10.2.jar" --class-path bin --scan-class-path --details=tree
```

### 3. Compilar y ejecutar las pruebas en un solo paso

```powershell
$files = Get-ChildItem -Path src/domain, src/presentation -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $files

$testFiles = Get-ChildItem -Recurse -Filter *.java src/test | ForEach-Object { $_.FullName }
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar;bin" -d bin $testFiles

java -jar "lib/junit-platform-console-standalone-1.10.2.jar" --class-path bin --scan-class-path --details=tree
```

**Nota:** actualmente `src/test/` está vacío. Las primeras pruebas se agregarán en la fase A1.

---

## Controles

| Acción | Jugador 1 | Jugador 2 |
|---|---|---|
| Moverse a la izquierda | `A` | `←` |
| Moverse a la derecha | `D` | `→` |
| Saltar | `W` | `↑` |
| Patear | `S` | `↓` |
| Usar poder | `Q` | `Ctrl derecho` |
| Pausar | `Esc` | `Esc` |

---

## Modalidades y tipos de partido

**Modalidades:**

- `PvsP`: jugador contra jugador.
- `PvsM`: jugador contra máquina.
- `MvsM`: máquina contra máquina.

**Tipos de partido:**

- **Por tiempo:** 60 segundos; si hay empate, gol de oro.
- **Por goles:** gana el primero en llegar a 3, 5 o 7 goles.
- **Supervivencia:** solo en `PvsM`; cada gol le quita un balón al rival.

---

## Reglas de arquitectura

- `domain` no conoce Swing ni la presentación.
- `Partido` es la **única frontera** entre dominio y presentación.
- La vista **dibuja y delega**; el controlador **traduce y coordina**; el dominio **decide**.
- Los parámetros ajustables (gravedad, tiempos, goles) irán en `ParametrosJuego`, no hardcodeados.
- Los puntos de extensión son `Poder`, `ReglaPartido` y `ControladorJugador`.

---

## Git

- `bin/` y los `.class` están ignorados en `.gitignore`.
- `lib/` **sí** se versiona porque contiene la dependencia de JUnit.
- Recomendación: un commit por ciclo terminado.
