# DOPOSoccer

> Videojuego de fútbol arcade 2D, 1 contra 1 y vista lateral, desarrollado en **Java + Swing** para el curso de **Desarrollo Orientado Por Objetos (DOPO)** de la Escuela Colombiana de Ingeniería.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Swing](https://img.shields.io/badge/UI-Swing-blue)
![JUnit](https://img.shields.io/badge/Tests-JUnit%205-25A162)
![Estado](https://img.shields.io/badge/estado-en%20desarrollo-yellow)

DOPOSoccer lleva la experiencia de *Head Soccer* al computador: partidos rápidos, tiros de poder con efectos, estadísticas mejorables, perfiles con progresión, una tienda de desbloqueos y un torneo de eliminación directa. El proyecto prioriza una **arquitectura MVC limpia**, pruebas unitarias y un modelo de dominio independiente de la interfaz.

---

## Tabla de contenido

1. [Características](#características)
2. [Vista previa](#vista-previa)
3. [Cómo se juega](#cómo-se-juega)
4. [Controles](#controles)
5. [Modalidades y tipos de partido](#modalidades-y-tipos-de-partido)
6. [Personajes y poderes](#personajes-y-poderes)
7. [Estadísticas, vida y daño](#estadísticas-vida-y-daño)
8. [Puntos, mejoras y tienda](#puntos-mejoras-y-tienda)
9. [Perfiles y persistencia](#perfiles-y-persistencia)
10. [Torneo](#torneo)
11. [Requisitos](#requisitos)
12. [Estructura del proyecto](#estructura-del-proyecto)
13. [Compilar y ejecutar](#compilar-y-ejecutar)
14. [Pruebas unitarias](#pruebas-unitarias)
15. [Arquitectura](#arquitectura)
16. [Estado del proyecto](#estado-del-proyecto)
17. [Plan de entregas](#plan-de-entregas)
18. [Autores y contexto académico](#autores-y-contexto-académico)

---

## Características

- Partido 1 contra 1 en 2D con vista lateral, física de balón con gravedad y rebotes.
- **Dos modalidades:** jugador vs jugador (PvsP) y jugador vs máquina (PvsM).
- **Dos tipos de partido:** por tiempo (con muerte súbita) y por goles, ambos configurables.
- **Seis personajes**, cada uno con un tiro de poder y un efecto único sobre el rival.
- **Tiros de poder** con aura, daño y efectos (empuje, electrocutado, inconsciente, confusión, controles invertidos y barril).
- **Estadísticas mejorables** (`speed`, `jump`, `kick`, `survival`) y economía de puntos.
- **Sistema de vida y daño** con aturdimiento.
- **Perfiles de jugador** con nombre, puntos, estadísticas y desbloqueos propios.
- **Tienda** para desbloquear personajes, balones y escenarios.
- **Torneo** de eliminación directa contra la máquina, con inscripción y premios.
- **Configuración visual:** elección de balón y escenario.
- **Controles personalizables** y **sonido** (música de fondo y efectos de sonido).
- **Tres resoluciones** de cancha configurables.
- **Persistencia completa:** guardar/abrir partidas y guardar/abrir/exportar/importar el progreso.
- **Perfiles de máquina:** torpe, ofensiva, defensiva y experta.
- Manejo de errores mediante excepciones específicas y registro de logs.

---

## Vista previa

El diseño de la interfaz se documenta en [`Bosquejo de interfaz.jpg`](Bosquejo%20de%20interfaz.jpg) y el modelo de clases en [`HeadSoccer.asta`](HeadSoccer.asta). La especificación funcional completa está en [`HeadSoccer/!DOPO-FIN-2026-02.pdf`](HeadSoccer/!DOPO-FIN-2026-02.pdf).

---

## Cómo se juega

DOPOSoccer es un partido de fútbol 2D de dos jugadores. Cada jugador defiende el arco de su lado de la pantalla y debe meter el balón en el arco contrario.

- Los jugadores pueden **caminar, saltar y patear**.
- El balón está afectado por la **gravedad** y rebota en el suelo, los arcos y los bordes de la cancha.
- Después de cada gol, los personajes y el balón vuelven a su posición inicial y el balón cae desde el centro.
- La condición de victoria depende del **tipo de partido** elegido.
- La información es **abierta**: cancha, balón, personajes, marcador, tiempo, vida y barras de poder siempre visibles.

---

## Controles

Ambos jugadores comparten el mismo teclado. Las teclas se pueden **personalizar** desde la pantalla de configuración; estos son los valores por defecto:

| Acción | Jugador 1 | Jugador 2 |
|---|---|---|
| Moverse a la izquierda | `A` | `←` |
| Moverse a la derecha | `D` | `→` |
| Saltar | `W` | `↑` |
| Patear | `S` | `↓` |
| Activar poder | `Q` | `Ctrl derecho` |
| Pausar | `Esc` | `Esc` |

---

## Modalidades y tipos de partido

### Modalidades

| Modalidad | Descripción |
|---|---|
| **PvsP** | Dos jugadores humanos en el mismo teclado. |
| **PvsM** | Uno de los jugadores es controlado por la máquina. |

La máquina puede jugar con uno de estos **perfiles**:

1. **Torpe:** elige sus movimientos al azar.
2. **Ofensiva:** su prioridad es anotar.
3. **Defensiva:** su prioridad es defender su arco.
4. **Experta:** decide la acción según el estado del partido.

### Tipos de partido

| Tipo | Regla de victoria |
|---|---|
| **Por tiempo** | Dura los minutos configurados. Gana quien tenga más goles; si hay empate, se juega **muerte súbita** (gana el primero que anote). |
| **Por goles** | Gana el primero en llegar al número de goles configurado. Sin límite de tiempo. |

La **resolución de la cancha** se elige al iniciar: `1280×720`, `1600×900` o `1920×1080`.

---

## Personajes y poderes

Cada personaje representa un país (o universo) y tiene un tiro de poder único. La barra de poder se llena en **10 segundos** y sube **un cuarto (¼)** cuando el jugador recibe un gol.

| Personaje | País | Tiro de poder | Efecto si golpea al rival | Duración | Daño base |
|---|---|---|---|---|---|
| Jungkook | Corea del Sur | **Aura Azul** | Empuje: el rival es lanzado hacia su propio arco | 1.5 s | 40 |
| Goku | Japón | **Rayo** | Electrocutado: no puede saltar y se mueve al 50 % de su velocidad | 3 s | 60 |
| Mario | Italia | **Tornado** | Inconsciente: es lanzado al aire y no puede moverse, saltar ni patear | 3 s | 40 |
| Bob Esponja | Estados Unidos | **Ilusión** | Confusión: el balón se divide en 8 (7 falsos) y el rival no sabe cuál es el real | 2.5 s | 30 |
| Betty la fea | Colombia | **Beso de la Fea** | Controles invertidos: los movimientos del rival se invierten | 2.5 s | 30 |
| El Chavo del Ocho | México | **Barril** | Barril: las estadísticas del rival vuelven a su valor inicial y su barra de poder queda en cero | 5 s | 35 |

Cuatro personajes están disponibles desde el inicio (**Betty la fea, El Chavo del Ocho, Goku y Bob Esponja**); **Jungkook** y **Mario** se desbloquean en la tienda.

### Reglas de los poderes

1. Con la barra llena, el jugador presiona la tecla de poder: la barra se vacía y empieza a recargarse.
2. Al activarlo, el personaje queda con un **aura visible** (sin límite de tiempo).
3. Cuando el personaje con aura **toca el balón**, ejecuta su tiro de poder y el aura desaparece.
4. Si el tiro **golpea al rival**, se aplican daño y efecto.
5. Si el tiro **entra al arco sin tocar al rival**, el gol cuenta, pero no hay daño ni efecto.
6. No se puede activar otro poder mientras el aura esté activa.
7. Los efectos **no se acumulan**: si el rival ya está bajo un efecto, el nuevo lo reemplaza y dura su propio tiempo. Al marcarse un gol se quitan todos los efectos.

---

## Estadísticas, vida y daño

Cada jugador tiene cuatro estadísticas que van de **1 a 10** y se aplican a cualquier personaje que elija.

| Estadística | Efecto |
|---|---|
| `Speed` | Velocidad de desplazamiento. |
| `Jump` | Altura del salto. |
| `Kick` | Daño al rival. **No** cambia la fuerza del balón. |
| `Survival` | Vida máxima. |

Fórmulas:

```text
multiplicadorKick = 1 + 0.10 × (Kick − 1)
vidaMax           = 100 × (1 + 0.10 × (Survival − 1))
daño de patada    = 10 × multiplicadorKick
daño de poder     = DañoTiro × multiplicadorKick
```

- La vida base es **100** y el daño base de una patada es **10**.
- La vida nunca baja de cero. Cuando llega a **cero**, el jugador queda **aturdido 3 segundos** (no puede moverse, saltar ni patear) y luego recupera su vida máxima.
- Los tiros de poder **no aturden por sí mismos**: cada uno aplica su propio efecto.

**Ejemplo:** Camerún con `Kick = 5` contra un rival con `Survival = 3`.

```text
Vida del rival       = 100 × (1 + 0.10 × 2) = 120
Multiplicador        = 1 + 0.10 × 4 = 1.4
Patada               = 10 × 1.4 = 14   → quedan 106
Tiro de poder (Rayo) = 60 × 1.4 = 84   → quedan 36 y electrocutado 3 s
```

---

## Puntos, mejoras y tienda

### Puntos y mejoras

- Cada partido otorga **100 puntos al ganador** y **25 al perdedor**.
- Los puntos se gastan al inicio de los partidos para **mejorar estadísticas**.
- El precio de cada punto de mejora se **duplica**: `500`, `1000`, `2000`, `4000`…
- Cada punto de mejora suma **+10 % sobre el valor base** de la estadística.

### Tienda

La tienda permite desbloquear contenido para el perfil activo:

| Contenido | Precio |
|---|---|
| Personaje | 500 puntos |
| Balón | 300 puntos |
| Escenario | 400 puntos |

Lo que compra un perfil **no** se desbloquea para los demás perfiles.

### Contenido visual

- **Balones:** Mario y Betty (iniciales), BTS (desbloqueable).
- **Escenarios:** Fondo de Bikini, Tenkaichi Budokai y Vecindad del Chavo (iniciales); Reino Champiñón (desbloqueable).

---

## Perfiles y persistencia

### Perfiles

Cada persona puede crear uno o varios **perfiles de jugador**. Un perfil guarda de forma independiente:

- Nombre e identificador único.
- Puntos y estadísticas.
- Personajes, balones y escenarios desbloqueados.

### Persistencia

- **Progreso** (conjunto de perfiles) se puede:
  - **Guardar como** y **abrir** en archivos binarios `.dat` (serialización).
  - **Exportar** e **importar** en archivos de texto `.txt` (una línea por ítem, campos separados por `;`).
- **Partida en curso** se puede **guardar como** y **abrir** en `.dat`, conservando el estado completo del partido y los perfiles participantes.

---

## Torneo

Además de los partidos sueltos, existe un **torneo de eliminación directa** con cuatro participantes: el jugador humano y tres máquinas.

- La **inscripción** cuesta **100 puntos**.
- El **campeón** recibe **500 puntos** y el **subcampeón**, **150**.
- Los partidos del torneo **no reparten puntos por sí mismos**.
- Las máquinas **mejoran sus estadísticas al avanzar de ronda**; el humano no.
- La otra semifinal y, si el humano queda eliminado, la final, se simulan automáticamente.

---

## Requisitos

- **JDK 17 o superior** (probado con versiones recientes de JDK).
- **PowerShell** (Windows) o cualquier shell con `javac` y `java` en el `PATH`.
- **JUnit 5** incluido en `lib/`:
  - `lib/junit-platform-console-standalone-1.10.2.jar`
- No se necesita Maven ni Gradle.

---

## Estructura del proyecto

```text
HeadSoccer/                        ← raíz del repositorio
├── README.md
├── Bosquejo de interfaz.jpg
├── HeadSoccer.asta
├── Retrospectivas/                ← retrospectivas de los ciclos
│   └── Retrospectiva Ciclo 1.pdf
└── HeadSoccer/                    ← proyecto Java
    ├── !DOPO-FIN-2026-02.pdf      ← especificación final
    ├── src/
    │   ├── domain/                # Modelo del juego (sin Swing)
    │   ├── presentation/          # Interfaz gráfica (Swing)
    │   └── test/                  # Pruebas unitarias (JUnit 5)
    ├── resources/                 # Imágenes, fuentes y audio
    │   ├── images/                # Personajes, balones, escenarios, etc.
    │   ├── audio/                 # Música de fondo y efectos
    │   └── fuentes/               # Tipografías del juego
    ├── lib/                       # Dependencias (JUnit)
    ├── bin/                       # Salida de compilación (no versionada)
    ├── logs/                      # Registro de errores (no versionado)
    └── doc/                       # Documentación y diagramas
```

> Los paquetes `domain` y `presentation` son **planos**: no se crean subpaquetes dentro de ellos. Los archivos `bin/`, `logs/` y los `.class` están ignorados en `.gitignore`; `lib/` y `resources/` sí se versionan.

---

## Compilar y ejecutar

Todos los comandos se ejecutan desde la **carpeta del proyecto Java** (la carpeta `HeadSoccer/` que contiene `src/`, `lib/`, `resources/` y `bin/`). Es importante ejecutar el juego desde esta carpeta para que `resources/` se encuentre por ruta relativa.

### Compilar

```powershell
$files = Get-ChildItem -Path src/domain, src/presentation -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d bin $files
```

Si la compilación termina sin errores, las clases quedan en `bin/`.

### Ejecutar

```powershell
java -cp bin presentation.DOPOSoccerGUI
```

Se abre la ventana principal de **DOPOSoccer**.

### Limpiar la compilación

```powershell
Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
```

---

## Pruebas unitarias

El proyecto usa **JUnit 5** mediante el `.jar` standalone de `lib/`. Las pruebas viven en `src/test/`.

### Compilar las pruebas

```powershell
$testFiles = Get-ChildItem -Recurse -Filter *.java src/test | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/junit-platform-console-standalone-1.10.2.jar;bin" -d bin $testFiles
```

> En Windows el separador de classpath es `;`. En Linux/macOS es `:`.

### Ejecutarlas

```powershell
java -jar "lib/junit-platform-console-standalone-1.10.2.jar" --class-path bin --scan-class-path --details=tree
```

### Compilar y ejecutar todo en un solo paso

```powershell
$files = Get-ChildItem -Path src/domain, src/presentation -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d bin $files

$testFiles = Get-ChildItem -Recurse -Filter *.java src/test | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/junit-platform-console-standalone-1.10.2.jar;bin" -d bin $testFiles

java -jar "lib/junit-platform-console-standalone-1.10.2.jar" --class-path bin --scan-class-path --details=tree
```

---

## Arquitectura

El proyecto sigue el patrón **MVC**:

- **Dominio (`domain`):** reglas, física, jugadores, poderes, vida, puntos, tienda, torneo y persistencia. No conoce Swing.
- **Presentación (`presentation`):** un único `JFrame` (`DOPOSoccerGUI`) con `CardLayout`; cada pantalla es un `JPanel` con su controlador.
- **Controladores:** traducen la entrada y coordinan el flujo; no dibujan.

Principios:

- `Partido` es la **única frontera** entre dominio y presentación.
- `FisicaPartido`, `ColisionesArcos`, `GestorPoderes` y `FabricaJugadores` son colaboradores internos: `Partido` los orquesta y ellos resuelven una sola responsabilidad cada uno.
- La vista **dibuja y delega**; el controlador **traduce y coordina**; el dominio **decide**.
- Los números ajustables viven en `ParametrosJuego`, no hardcodeados.
- `Poder`, `Efecto`, `ReglaPartido` y `ControladorIA` son **puntos de extensión**: agregar personajes, poderes, efectos, perfiles de IA o reglas no obliga a reescribir el núcleo.
- El dominio se prueba con JUnit sin abrir Swing.

---

## Estado del proyecto

| Área | Estado |
|---|---|
| Proyecto Java y ventana Swing (`DOPOSoccerGUI`) | ✅ |
| Modelo base del dominio (partido, jugador, balón, cancha, física) | ✅ |
| Estadísticas 1–10, vida, puntos y configuración del jugador | ✅ |
| Modalidades (`PvsP`/`PvsM`), tipos de partido, perfiles de IA y resoluciones | ✅ |
| Pantallas (inicio, menú, configuración, selección, juego, pausa, resultado) | ✅ |
| Poderes y tiros de poder (6 personajes) | ✅ |
| IA con perfiles | ✅ |
| Perfiles, tienda, mejoras y desbloqueos | ✅ |
| Torneo de eliminación directa | ✅ |
| Persistencia (guardar/abrir partidas y progreso, exportar/importar) | ✅ |
| Sonido, configuración visual y controles personalizables | ✅ |
| Pruebas unitarias del dominio | ✅ |
| Excepciones específicas y log de errores | ✅ |

**Leyenda:** ✅ implementado · 🚧 en progreso · ⏳ pendiente.

---

## Plan de entregas

| Versión | Contenido | Fecha |
|---|---|---|
| **Versión uno** | Presentación + Dominio, configuración del juego, nivel básico **PvsP** | S12 · Sa oct 24 |
| **Versión dos** | Persistencia + Dominio, guardar/abrir partidas, nivel medio **PvsM** | S15 · Do nov 15 |
| **Versión tres** | Lo anterior + puntos, mejoras y persistencia completa | S16 · Sa nov 21 |
| **Versión cuatro** | HUD final, excepciones/log, pruebas y entrega | S17 · Do nov 22 |
| **Competencia** | Entrega final con todos los requisitos cumplidos | S18 · Vi dic 04 |

---

## Autores y contexto académico

**Institución:** Escuela Colombiana de Ingeniería
**Asignatura:** Desarrollo Orientado Por Objetos (DOPO)
**Proyecto:** Final 2026-2 — DOPOSoccer

- **Propuesta:** Mateo Sánchez García
- **Colaboradores:** Wilmar Edison Beltrán y Santiago Hernández Sarmiento
- **Implementación y pruebas:** equipo del proyecto

### Referencias del juego original

- *Head Soccer* — D&D Dream (2012). TouchArcade.
- *Head Soccer* — D&D Dream.
- *Head Sports* (serie de videojuegos) — TV Tropes.

---

## Notas

- El proyecto se inspira en *Head Soccer* de D&D Dream, pero **no** distribuye sus recursos gráficos ni de audio originales; el arte, la música y los efectos del proyecto son recursos propios incluidos en `resources/`.
- `bin/`, `logs/` y los `.class` están ignorados en `.gitignore`; `lib/` y `resources/` sí se versionan.
- Se recomienda un commit por cada ciclo terminado.
