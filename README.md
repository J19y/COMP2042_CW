# COMP2042 Coursework - Tetris 2042

---

| Field | Details |
|-------|---------|
| **Name** | Youssif Mahmoud Gomaa Sayed |
| **Student ID** | 20612641 |
| **Module** | COMP2042 - Developing Maintainable Software |
| **Academic Year** | 2025/2026 |

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [GitHub Repository Link](#2-github-repository-link)
3. [Compilation Instructions](#3-compilation-instructions)
   - [3.1 Prerequisites](#31-prerequisites)
   - [3.2 Steps to Compile and Run](#32-steps-to-compile-and-run)
   - [3.3 Running from an IDE](#33-running-from-an-ide)
   - [3.4 Troubleshooting](#34-troubleshooting)
4. [Maintenance Work (Refactoring)](#4-maintenance-work-refactoring)
   - [4.1 Package Restructuring](#41-package-restructuring)
   - [4.2 Basic Maintenance and Encapsulation](#42-basic-maintenance-and-encapsulation)
   - [4.3 Single Responsibility Principle (SRP) Refactoring](#43-single-responsibility-principle-srp-refactoring)
   - [4.4 Design Patterns for Extensibility](#44-design-patterns-for-extensibility)
5. [Implemented and Working Properly](#5-implemented-and-working-properly)
   - [5.1 Core Gameplay Features](#51-core-gameplay-features)
   - [5.2 User Interface Features](#52-user-interface-features)
   - [5.3 Audio System](#53-audio-system)
   - [5.4 Visual Polish](#54-visual-polish)
6. [Implemented but Not Working Properly](#6-implemented-but-not-working-properly)
7. [Features Not Implemented](#7-features-not-implemented)
8. [New Java Classes](#8-new-java-classes)
   - [8.1 Application Layer](#81-application-layer-comcomp2042tetrisapplication)
   - [8.2 Domain Layer](#82-domain-layer-comcomp2042tetrisdomain)
   - [8.3 Engine Layer](#83-engine-layer-comcomp2042tetrisengine)
   - [8.4 Services Layer](#84-services-layer-comcomp2042tetrisservices)
   - [8.5 UI Layer](#85-ui-layer-comcomp2042tetrisui)
   - [8.6 Utility Layer](#86-utility-layer-comcomp2042tetrisutil)
9. [Modified Java Classes](#9-modified-java-classes)
10. [Unexpected Problems](#10-unexpected-problems)
11. [Testing](#11-testing)
    - [11.1 Test Categories and Coverage](#111-test-categories-and-coverage)
    - [11.2 Why These Tests Matter](#112-why-these-tests-matter)
    - [11.3 Bugs Prevented by Tests](#113-bugs-prevented-by-tests)
12. [Architecture Summary](#12-architecture-summary)
    - [12.1 Final Architecture](#121-final-architecture)
    - [12.2 Architecture Transformation](#122-architecture-transformation)
    - [12.3 Why This Architecture Is More Maintainable](#123-why-this-architecture-is-more-maintainable)
13. [Summary](#13-summary)
    - [13.1 Key Achievements](#131-key-achievements)
    - [13.2 Technical Highlights](#132-technical-highlights)
    - [13.3 Personal Reflection & Lessons Learned](#133-personal-reflection--lessons-learned)

---

## 1. Project Overview

**Tetris 2042** is a modern JavaFX implementation of the classic Tetris puzzle game, featuring a neon-themed visual style and multiple gameplay modes. Players arrange falling tetromino pieces to complete horizontal lines, which are then cleared from the board. The game ends when pieces stack to the top of the playing field.

### Game Features

- **Three Game Modes:** Classic (endless), Rush (2-minute time attack), and Mystery (random chaotic events)
- **Ghost Piece Preview:** Shows where pieces will land for precise placement
- **Next Piece Queue:** Displays upcoming 5 pieces for strategic planning
- **Dynamic Soundtrack:** Mode-specific background music with smooth transitions
- **Neon Visual Theme:** Retro-futuristic aesthetic with glow effects and animations


### Coursework Scope

This project involved **maintaining** an existing Tetris codebase by refactoring it into a clean, layered architecture with 110 classes and 8 design patterns applied. The game was **extended** with new game modes, audio system, and visual polish.

---

## 2. GitHub Repository Link

[https://github.com/J19y/COMP2042_CW](https://github.com/J19y/COMP2042_CW)

---

## 3. Compilation Instructions

### 3.1 Prerequisites

#### 3.1.1 Java Development Kit (JDK 23)

This project requires **JDK 23** or higher.

**Download and Install:**
- Download from [Oracle JDK 23](https://www.oracle.com/java/technologies/downloads/#jdk23-windows) or [OpenJDK 23](https://jdk.java.net/23/)
- Run the installer and follow the installation wizard
- **Set JAVA_HOME environment variable:**
  1. Open Windows Search → Type "Environment Variables" → Click "Edit the system environment variables"
  2. Click "Environment Variables" button
  3. Under "System variables", click "New"
  4. Variable name: `JAVA_HOME`
  5. Variable value: `C:\Program Files\Java\jdk-23` (or your installation path)
  6. Find "Path" in System variables → Edit → Add `%JAVA_HOME%\bin`
  7. Click OK to save

**Verify installation:**
```shell
java --version
```
Expected output: `java 23` or higher

#### 3.1.2 Git (for cloning the repository)

**Download and Install:**
- **Windows:** Download and install from [Git for Windows](https://git-scm.com/download/win)
- **macOS:** Install via Homebrew: `brew install git`
- **Linux:** Use apt: `sudo apt install git` (Debian/Ubuntu) or `sudo dnf install git` (Fedora)

**Verify installation:**
```shell
git --version
```

#### 3.1.3 Maven (Optional - Maven Wrapper included)

The project includes Maven Wrapper (`mvnw.cmd`), so Maven installation is **optional**. However, if you prefer a global Maven installation:

**Download and Install:**
- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Extract to `C:\Program Files\Apache\maven`
- Add `C:\Program Files\Apache\maven\bin` to your system PATH

**Verify installation:**
```shell
mvn --version
```

#### 3.1.4 IDE (Optional but Recommended)

Any of the following IDEs can be used:
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community Edition is free)
- [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/)
- [VS Code](https://code.visualstudio.com/) with [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

#### 3.1.5 JavaFX (Automatically Managed)

JavaFX 21.0.6 dependencies are **automatically downloaded** by Maven. No manual installation required.

---

### 3.2 Steps to Compile and Run

#### Step 1: Clone the Repository

```shell
git clone https://github.com/J19y/COMP2042_CW.git && cd COMP2042_CW
```

#### Step 2: Build the Project

**On Windows (Command Prompt or PowerShell):**
```powershell
.\mvnw.cmd clean compile
```

**On macOS/Linux:**
```shell
./mvnw clean compile
```

#### Step 3: Run the Application

**On Windows:**
```powershell
.\mvnw.cmd javafx:run
```

**On macOS/Linux:**
```shell
./mvnw javafx:run
```

#### Step 4: Run Tests (Optional)

**On Windows:**
```powershell
.\mvnw.cmd test
```

**On macOS/Linux:**
```shell
./mvnw test
```

---

### 3.3 Running from an IDE

#### IntelliJ IDEA

1. Open IntelliJ IDEA → File → Open → Select the `COMP2042_CW` folder
2. Wait for Maven to import dependencies (progress bar at bottom)
3. Navigate to `src/main/java/com/comp2042/tetris/ui/controller/Main.java`
4. Right-click → Run 'Main.main()'

If you encounter JavaFX errors:
1. Go to Run → Edit Configurations
2. Add VM options: `--module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml,javafx.media`

#### VS Code

1. Open VS Code → File → Open Folder → Select `COMP2042_CW`
2. Install "Extension Pack for Java" if prompted
3. Open `src/main/java/com/comp2042/tetris/ui/controller/Main.java`
4. Click "Run" above the `main` method, or press F5

#### Eclipse

1. File → Import → Maven → Existing Maven Projects
2. Browse to `COMP2042_CW` folder → Finish
3. Right-click project → Run As → Java Application → Select `Main`

---

### 3.4 Troubleshooting

| Issue | Solution |
|-------|----------|
| `'java' is not recognized` | Ensure JAVA_HOME is set and `%JAVA_HOME%\bin` is in PATH |
| `javafx.controls not found` | Run via Maven (`mvnw.cmd javafx:run`) instead of direct Java execution |
| `Could not find or load main class` | Run `.\mvnw.cmd clean compile` first |
| Maven download fails | Check internet connection; try `.\mvnw.cmd -U clean compile` to force update |
| Permission denied on `mvnw` | On Linux/Mac: run `chmod +x mvnw` |

---

## 4. Maintenance Work (Refactoring)

This section details the refactoring efforts undertaken to transform the original codebase into a maintainable, extensible architecture. The work progressed in logical phases: package restructuring, basic maintenance and encapsulation, applying Single Responsibility Principle, and finally implementing design patterns to enable future extensions.

---

### 4.1 Package Restructuring

The original codebase had all classes in a flat package structure with no clear organization. Classes were reorganized into a **layered architecture** with six distinct packages:

| Package | Purpose | Key Classes |
|---------|---------|-------------|
| `application/` | Use cases, commands, session management | `BaseGameController`, `GameCommand`, `GameLoop` |
| `domain/` | Core business logic, value objects | `Score`, `ViewData`, `ScoringPolicy` |
| `engine/` | Game mechanics, board logic | `SimpleBoard`, `BrickGenerator`, `CollisionDetector` |
| `services/` | Cross-cutting concerns | `MusicManager`, `NotificationManager` |
| `ui/` | JavaFX views, renderers, input handling | `GuiController`, `BoardRenderer`, `InputHandler` |
| `util/` | Pure utility functions | `MatrixOperations`, `CollisionDetector` |

**Why This Improves Maintainability:** Clear package boundaries make the codebase navigable. Developers can locate functionality by layer. Dependencies flow downward (UI → Application → Domain → Engine), preventing circular dependencies.

---

### 4.2 Basic Maintenance and Encapsulation

Before applying major refactoring patterns, foundational cleanup was performed:

| Change | Before | After | Benefit |
|--------|--------|-------|---------|
| **Field Visibility** | Public fields accessed directly (`board.matrix[i][j]`) | Private fields with getters (`board.getCell(i, j)`) | Encapsulation enables validation and future implementation changes |
| **Magic Numbers** | Hardcoded values (`if (y > 22)`, `score += 100`) | Named constants (`BOARD_ROWS = 22`, `BASE_SCORE = 50`) | Self-documenting code, single point of change |
| **Null Checks** | Direct field access causing NPEs | `Objects.requireNonNull()` in constructors, defensive null checks | Fail-fast behavior, clearer error messages |
| **Method Extraction** | 200+ line methods with nested loops | Small focused methods (10-30 lines) | Readable, testable, reusable |
| **Javadoc Comments** | No documentation | All public classes and methods documented | API clarity for future maintainers |

---

### 4.3 Single Responsibility Principle (SRP) Refactoring

Large classes violating SRP were decomposed into focused, single-purpose classes:

#### 4.3.1 Board Class Decomposition

| Original Problem | The original `Board` class (800+ lines) handled: board state, collision detection, piece movement, row clearing, rendering, and piece generation. |
|------------------|---|
| **Extracted Classes** | |
| `SimpleBoard` | Manages the 22×10 grid matrix and piece state |
| `CollisionDetector` | Static utility for collision checks |
| `MatrixOperations` | Static utility for row clearing, matrix merging |
| `BrickPositionManager` | Tracks current piece X/Y coordinates |
| `BrickRotator` | Manages rotation state and shape matrices |
| `BoardReader` | Read-only queries for board state |
| **Improvement** | Each class has one reason to change. Utilities are pure functions—easily unit tested. |

#### 4.3.2 GUI Controller Decomposition

| Original Problem | The original `GuiController` (1200+ lines) handled: board rendering, piece animation, score display, game over screen, pause overlay, notifications, and input binding. |
|------------------|---|
| **Extracted Classes** | |
| `BoardRenderer` | Renders static board (locked pieces) |
| `ActiveBrickRenderer` | Renders falling piece and ghost piece |
| `GameOverPanel` | Game over screen with stats display |
| `PauseOverlayController` | Pause dimming and state |
| `CountdownManager` | 3-2-1-GO resume countdown |
| `NotificationPanel` | Score popup notifications |
| `GameMediator` | Coordinates between all view components |
| **Improvement** | Each renderer is independently testable. New visual features require new classes, not modifications. |

#### 4.3.3 Menu Controller Decomposition

| Original Problem | The original `MenuController` handled: button setup, animations, settings panel, level selection, and background effects. |
|------------------|---|
| **Extracted Classes** | |
| `ButtonSetupManager` | Configures menu buttons with hover effects |
| `TitleSetupManager` | Animated title display |
| `SettingsPanelManager` | Volume slider and music toggle |
| `LevelSelectionManager` | Game mode card selection |
| `BackgroundEffectsManager` | Falling tetromino animation |
| `MenuAnimationController` | Screen transitions and blur effects |
| **Improvement** | Menu features can be modified independently. Animation logic is isolated from UI setup. |

---

### 4.4 Design Patterns for Extensibility

After SRP refactoring, design patterns were applied to enable future extensions without modifying existing code:

#### 4.4.1 Strategy Pattern — Scoring System

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Different game modes needed different scoring rules. Hardcoded scoring prevented extension. |
| **Implementation** | `ScoringPolicy` interface with `scoreForLineClear(int)` and `scoreForDrop(EventSource, boolean)`. `ClassicScoringPolicy` implements 50×lines² formula. |
| **Extension Enabled** | New scoring modes (combo multipliers, time bonuses) require only a new `ScoringPolicy` implementation—no changes to game controllers. |

#### 4.4.2 Command Pattern — Input Handling

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Input handling was a massive switch statement. Adding controls or remapping keys required invasive changes. |
| **Implementation** | `GameCommand` interface with `execute(MoveEvent)`. Each action (left, right, rotate, drop) is a separate command. `CommandRegistrar` maps events to commands. |
| **Extension Enabled** | New commands can be added without modifying existing code. Mystery Mode inverts controls by simply swapping command mappings. |

#### 4.4.3 State Pattern — Game State Management

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Boolean flags (`isPaused`, `isGameOver`) led to impossible states and scattered if-else checks. |
| **Implementation** | `GameStateManager` with `GameState` enum (`MENU`, `PLAYING`, `PAUSED`, `GAME_OVER`). Each state defines valid transitions and behavior (`canAcceptInput()`, `canUpdateGame()`). |
| **Extension Enabled** | New states (e.g., COUNTDOWN, LEVEL_TRANSITION) require only enum extension. State-dependent logic is localized. |

#### 4.4.4 Factory Pattern — Board and Brick Creation

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Direct instantiation (`new Board()`, `new IBrick()`) prevented testing with mocks and custom configurations. |
| **Implementation** | `BoardFactory` interface with `SimpleBoardFactory` implementation. `BrickRegistry` singleton holds `Supplier<Brick>` for each shape. `BrickGeneratorFactory` creates generators. |
| **Extension Enabled** | Custom bricks via `BrickRegistry.register(CustomBrick::new)`. Tests inject deterministic generators. Alternative board implementations possible. |

#### 4.4.5 Template Method Pattern — Game Controllers

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Multiple game modes would require duplicating the entire game loop with minor variations. |
| **Implementation** | `BaseGameController` abstract class with hooks: `onStart()`, `onPause()`, `onResume()`, `onGameOver()`. Subclasses (`ClassicGameController`, `TimedGameController`, `MysteryGameController`) override only mode-specific behavior. |
| **Extension Enabled** | New game modes inherit all common logic. Bug fixes in base class apply to all modes automatically. |

#### 4.4.6 Observer Pattern — Game Over Detection

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Game over detection was tightly coupled to a single handler. Multiple systems needed notification (audio, UI, stats). |
| **Implementation** | `SpawnManager` maintains list of `GameOverCallback` observers. Calls all observers when spawn results in collision. |
| **Extension Enabled** | New systems can subscribe to game over events without modifying spawn logic. |

#### 4.4.7 Decorator Pattern — View Caching

| Aspect | Details |
|--------|---------|
| **Problem Solved** | Board rendering was slow due to redrawing unchanged cells every frame. Optimization required invasive changes. |
| **Implementation** | `BufferedGameView` decorates `GameView`, caching the last board state. Only redraws when matrix actually changes. |
| **Extension Enabled** | Performance optimizations are transparent to callers. Additional decorators (logging, metrics) can be stacked. |

#### 4.4.8 Mediator Pattern — View Coordination

| Aspect | Details |
|--------|---------|
| **Problem Solved** | After SRP decomposition, view components needed to communicate (line clear triggers notification AND animation). Direct coupling would create N×N dependencies. |
| **Implementation** | `GameMediator` coordinates between `BoardRenderer`, `ActiveBrickRenderer`, `NotificationPanel`, and animation triggers. Components communicate only through the mediator. |
| **Extension Enabled** | New view components integrate by connecting to the mediator only. Existing components remain unchanged. |

---

## 5. Implemented and Working Properly

### 5.1 Core Gameplay Features

- **Classic Tetris Mechanics:** Full implementation of brick movement (left, right, down), rotation, and hard drop functionality. Pieces lock when they cannot move further down.

- **Three Game Modes:**
  - **Classic Mode:** Standard endless Tetris with progressive scoring. Difficulty increases over time with faster drop speeds. Plays a calm, classic-style soundtrack.
  - **Timed Mode (Rush):** 2-minute time-limited gameplay where the objective is to maximize score before time expires. Features a prominent digital countdown timer and energetic soundtrack. Implemented via `TimedGameController` extending `BaseGameController`.
  - **Mystery Mode:** Features random events (inverted controls, fog effect, gravity changes, speed boosts) that trigger every 8–15 seconds. Progressive difficulty scaling every 30 seconds increases speed multiplier. Implemented via `MysteryGameController` with dedicated event scheduling and `GameEffectManager` for visual effects.

- **Scoring System:** Points awarded for line clears (50 × lines² formula) and soft drops (1 point per user-initiated drop). Hard drops award points based on distance fallen. Implemented via `ScoringPolicy` strategy pattern allowing different scoring rules per mode.

- **Ghost Piece Preview:** Shows where the current piece will land, rendered with subtle neon styling and transparency. Calculated by `SimpleBoard` using `CollisionDetector` to simulate downward movement. Position included in `ViewData` for rendering by `ActiveBrickRenderer`.

- **Next Brick Queue:** Displays up to 5 upcoming pieces with animated preview panels. `RandomBrickGenerator` maintains internal queue via `peekNext(5)` method. Rendered by `NextBrickRenderer` with consistent neon styling.

- **Line Clear Detection and Animation:** Cleared rows trigger visual feedback with score bonus notifications. Flash effects on cleared rows, animated score popups via `NotificationPanel`, and level-up celebration effects.

### 5.2 User Interface Features

- **Animated Main Menu:** Features falling tetromino shapes in the background managed by `BackgroundEffectsManager`, neon glow effects via `NeonGlowStyle`, and smooth fade transitions controlled by `MenuAnimationController`.

- **Level Selection Panel:** Interactive mode selection with animated card transitions managed by `LevelSelectionManager`. Tooltips describe each game mode's unique mechanics.

- **Pause System:** Press 'P' to pause. Displays a dimmed overlay via `PauseOverlayController` with countdown timer (3-2-1-GO) managed by `CountdownManager` when resuming. State managed by `GameStateManager` ensuring input is blocked during pause.

- **Game Over Screen:** Shows final score, time played, lines cleared, and level reached via `GameOverPanel`. Includes Retry and Main Menu buttons with animated entrance effects handled by `GameOverAnimator`.

- **Settings Panel:** Volume slider and music toggle accessible from the main menu, managed by `SettingsPanelManager` with direct binding to `MusicManager`.

- **Control Help Panel:** Visual diagram showing keyboard controls (Arrow keys/WASD, Space for hard drop, P to pause) managed by `ControlPanelManager`.

### 5.3 Audio System

- **Dynamic Soundtrack:** Different background tracks for each game mode (Main Menu, Classic, Rush, Mystery, Game Over) managed by `MusicManager` singleton with `Track` enum.
- **Smooth Transitions:** Fade transitions between tracks prevent jarring audio cuts when switching modes.
- **Volume Control:** Adjustable music volume with real-time slider in settings panel.
- **Music Ducking:** Background music temporarily quiets during countdown sequences for clarity.

### 5.4 Visual Polish

- **Neon Theme:** All game elements use a cohesive neon aesthetic with glow effects and drop shadows via `NeonGlowStyle` and `ColorPalette` singletons.
- **Smooth Animations:** Piece settling animation via `ActiveBrickRenderer.animateSettle()`, level-up effects, line clear flash effects—all using JavaFX `Timeline` and `Transition` APIs.
- **Timer Display:** Digital-style font for countdown and game timer (Rush mode).

---

## 6. Implemented but Not Working Properly

- **Mystery Mode Heavy Gravity Event:** The visual indicator for heavy gravity appears, but the actual gravity multiplier does not consistently affect the drop speed during the effect duration. The fallback timer restores the state correctly, but the intermediate behavior is inconsistent.

- **Game Mode Transition Delays:** Transitions between game levels occasionally lag or get delayed, resulting in brief stutters during level progression.

---

## 7. Features Not Implemented

- **Additional Brick Types:** The `BrickRegistry` is designed for extensibility, but no custom tetromino shapes beyond the standard 7 were added.

- **Multiplayer Mode:** No networked or local two-player mode was implemented.

- **Theme Customization:** A light theme variant of the game was planned to provide visual accessibility options, but implementation proved difficult due to the extensive neon styling applied throughout the UI components. Changing the color scheme would require significant refactoring of the `ColorPalette`, `NeonGlowStyle`, and CSS stylesheets.

---

## 8. New Java Classes

### 8.1 Application Layer (`com.comp2042.tetris.application`)

| Class | Location | Purpose |
|-------|----------|---------|
| `GameCommand` | `application/command/` | Interface defining the Command pattern for game actions. Each input type (left, right, rotate, drop) is encapsulated as a command object. |
| `CommandRegistrar` | `application/command/` | Manages registration and retrieval of game commands. Centralizes command mapping to event types, improving extensibility. |
| `GameplayPort` | `application/port/` | Aggregates all input-handling interfaces into a single port. Defines the contract between UI and game logic. |
| `GameModeLifecycle` | `application/port/` | Interface for game mode lifecycle events (start, pause, resume). Enables polymorphic handling of different game modes. |
| `CreateNewGame` | `application/port/` | Single-responsibility interface for triggering a new game. Extracted for SRP compliance. |
| `BaseGameController` | `application/session/` | Abstract base class implementing core game logic. Provides template methods for subclass customization. Handles input routing, scoring, and spawn management. |
| `ClassicGameController` | `application/session/` | Extends `BaseGameController` for standard Tetris gameplay. Plays the classic soundtrack on start. |
| `MysteryGameController` | `application/session/` | Implements Mystery Mode with random events (inverted controls, fog, gravity changes, speed boosts). Manages event timers and difficulty progression. |
| `TimedGameController` | `application/session/` | Implements Rush Mode with a 2-minute countdown. Triggers game over when time expires. |
| `GameLoop` | `application/session/` | Encapsulates the JavaFX Timeline-based game tick. Supports dynamic interval adjustment. |
| `GameLoopController` | `application/session/` | Facade for controlling the game loop (start, stop, restart, interval changes). |
| `GameInitializer` | `application/session/` | Factory-style class that wires together board, movement, spawn, and score components. Simplifies controller construction. |
| `GameController` | `application/session/` | Legacy interface for game controller contracts. Retained for compatibility. |

### 8.2 Domain Layer (`com.comp2042.tetris.domain`)

| Class | Location | Purpose |
|-------|----------|---------|
| `Score` | `domain/model/` | Value object encapsulating the score as a JavaFX `IntegerProperty` for reactive UI binding. |
| `ViewData` | `domain/model/` | Immutable data transfer object carrying brick shape, position, ghost position, and next brick queue. |
| `RowClearResult` | `domain/model/` | Result object returned after row clearing. Contains lines removed, updated matrix, score bonus, and cleared row indices. |
| `ShowResult` | `domain/model/` | Composite result object pairing a `RowClearResult` with current `ViewData`. Returned from drop operations. |
| `SpawnResult` | `domain/model/` | Simple result indicating whether a spawn resulted in game over. |
| `RotationInfo` | `domain/model/` | Holds the next rotation matrix and index for rotation operations. |
| `ScoringPolicy` | `domain/scoring/` | Strategy interface for scoring calculations. Allows different scoring algorithms per game mode. |
| `ClassicScoringPolicy` | `domain/scoring/` | Default implementation: 50 × lines² for clears, 1 point per user soft drop. |
| `ScoreManager` | `domain/scoring/` | Service managing score state. Wraps `Score` and exposes property for binding. |

### 8.3 Engine Layer (`com.comp2042.tetris.engine`)

| Class | Location | Purpose |
|-------|----------|---------|
| `Board` | `engine/board/` | Interface defining all board operations (move, rotate, spawn, merge, clear). |
| `SimpleBoard` | `engine/board/` | Concrete implementation of `Board`. Manages the 22×10 matrix, collision detection, and piece state. |
| `BoardPorts` | `engine/board/` | Aggregator interface exposing board capabilities as separate ports (movement, drop, read, spawn, lifecycle). |
| `SimpleBoardPorts` | `engine/board/` | Adapter wrapping `SimpleBoard` to expose it through the ports interface. |
| `BoardFactory` | `engine/board/` | Factory interface for creating board instances. Supports dependency injection. |
| `SimpleBoardFactory` | `engine/board/` | Default factory creating `SimpleBoard` instances. |
| `BoardComponentsFactory` | `engine/board/` | Factory interface for board internal components (generator, rotator, position manager). |
| `DefaultBoardComponentsFactory` | `engine/board/` | Default implementation providing standard components. |
| `BoardRead` | `engine/board/` | Read-only interface for querying board state (matrix, view data). |
| `BoardReader` | `engine/board/` | Concrete implementation of `BoardRead`. |
| `BoardLifecycle` | `engine/board/` | Interface for board reset operations (new game). |
| `BoardLifecycleManager` | `engine/board/` | Implementation managing board lifecycle. |
| `BoardMovement` | `engine/board/` | Interface for brick movement operations. |
| `BoardDropActions` | `engine/board/` | Interface for drop-related operations (merge, clear). |
| `BoardSpawner` | `engine/board/` | Interface for spawn operations. |
| `BoardState` | `engine/board/` | Represents board state for serialization/snapshot purposes. |
| `GameView` | `engine/board/` | Interface defining view update operations. Decouples game logic from UI implementation. |
| `Brick` | `engine/bricks/` | Interface for tetromino pieces. Provides rotation matrices. |
| `IBrick`, `JBrick`, `LBrick`, `OBrick`, `SBrick`, `TBrick`, `ZBrick` | `engine/bricks/` | Concrete implementations for each standard tetromino shape with their rotation states. |
| `BrickGenerator` | `engine/bricks/` | Interface for generating pieces. Supports peeking at upcoming bricks. |
| `RandomBrickGenerator` | `engine/bricks/` | Generates random pieces using a queue-based approach for next piece preview. |
| `BrickGeneratorFactory` | `engine/bricks/` | Factory interface for creating generators. |
| `RandomBrickGeneratorFactory` | `engine/bricks/` | Default factory for `RandomBrickGenerator`. |
| `BrickRegistry` | `engine/bricks/` | Singleton registry of available brick types. Allows runtime registration of custom bricks. |
| `BrickMove` | `engine/movement/` | Handles left, right, and rotation movements. Returns updated `ViewData`. |
| `BrickDrop` | `engine/movement/` | Handles soft drop logic including merge, clear, scoring, and respawn. |
| `BrickDropActions` | `engine/movement/` | Interface for drop-specific board operations. |
| `BrickMovement` | `engine/movement/` | Interface for movement operations. |
| `BrickPositionManager` | `engine/movement/` | Tracks current piece position. Provides position calculation methods. |
| `BrickRotator` | `engine/rotation/` | Manages current rotation state and provides next rotation shape. |
| `BrickSpawn` | `engine/spawn/` | Interface for spawning new pieces. |
| `SpawnManager` | `engine/spawn/` | Manages spawn operations with observer pattern for game-over notification. |
| `GameStateManager` | `engine/state/` | State machine managing game states (MENU, PLAYING, PAUSED, GAME_OVER). Uses State pattern with enum-based states. |

### 8.4 Services Layer (`com.comp2042.tetris.services`)

| Class | Location | Purpose |
|-------|----------|---------|
| `MusicManager` | `services/audio/` | Singleton managing background music playback, volume control, fade transitions, and sound effects. |
| `NotificationManager` | `services/notify/` | Manages in-game notifications (score bonuses, line clear messages, event announcements). |

### 8.5 UI Layer (`com.comp2042.tetris.ui`)

| Class | Location | Purpose |
|-------|----------|---------|
| `BackgroundEffectsManager` | `ui/animation/` | Manages animated falling shapes and particles in the main menu background. |
| `NeonFlickerEffect` | `ui/animation/` | Utility for applying flickering neon effects to UI elements. |
| `NeonFlickerEffectExamples` | `ui/animation/` | Example usage of neon effects. |
| `Main` | `ui/controller/` | Application entry point. Loads the main menu FXML. |
| `MenuController` | `ui/controller/` | FXML controller for the main menu. Coordinates sub-managers for UI sections. |
| `MenuAnimationController` | `ui/controller/` | Handles menu animations (blur, warp transitions, launch sequence). |
| `GameSceneLoader` | `ui/controller/` | Loads the game scene and instantiates the appropriate game controller based on selected mode. |
| `LevelSelectionManager` | `ui/controller/` | Manages the game mode selection panel with animated cards. |
| `ButtonSetupManager` | `ui/controller/` | Configures main menu buttons (hover effects, neon styling). |
| `TitleSetupManager` | `ui/controller/` | Sets up the animated title display. |
| `ControlPanelManager` | `ui/controller/` | Manages the controls help overlay. |
| `SettingsPanelManager` | `ui/controller/` | Manages the settings panel (volume slider, music toggle). |
| `ViewSetup` | `ui/controller/` | Utility for common view setup operations. |
| `InputHandler` | `ui/input/` | Processes keyboard input and routes to appropriate handlers. Supports custom key bindings. |
| `InputActionHandler` | `ui/input/` | Interface for handling input actions. |
| `InputEventListener` | `ui/input/` | Interface for input event callbacks. |
| `MovementInput` | `ui/input/` | Interface for movement input handling. |
| `DropInput` | `ui/input/` | Interface for drop input handling. |
| `EventSource` | `ui/input/` | Enum distinguishing user input from timer-driven events. |
| `EventType` | `ui/input/` | Enum defining input event types (DOWN, LEFT, RIGHT, ROTATE, HARD_DROP, PAUSE). |
| `MoveEvent` | `ui/input/` | Event object carrying event type and source. |
| `BoardRenderer` | `ui/render/` | Renders the static board background (locked pieces). |
| `ActiveBrickRenderer` | `ui/render/` | Renders the currently falling piece and ghost piece with animations. |
| `NextBrickRenderer` | `ui/render/` | Renders the next brick preview panel. |
| `ColorPalette` | `ui/theme/` | Singleton registry mapping brick IDs to colors. Supports custom color registration. |
| `CellColor` | `ui/theme/` | Utility for retrieving cell colors from the palette. |
| `NeonGlowStyle` | `ui/theme/` | Applies neon glow effects to rectangles (stroke, drop shadow, rounded corners). |
| `GuiController` | `ui/view/` | Main game view FXML controller. Implements `GameView` interface. Orchestrates all visual components. |
| `GameMediator` | `ui/view/` | Mediator coordinating board rendering, brick rendering, and notifications. |
| `ViewInitializer` | `ui/view/` | Handles font loading and initial view setup. |
| `GameViewDecorator` | `ui/view/` | Decorator base class for `GameView`. Enables transparent extension of view behavior. |
| `BufferedGameView` | `ui/view/` | Decorator caching the last board state to avoid redundant redraws. |
| `GameOverPanel` | `ui/view/` | Custom panel displaying game over information with styled labels and buttons. |
| `GameOverAnimator` | `ui/view/` | Handles the game over animation sequence. |
| `PauseOverlayController` | `ui/view/` | Manages the pause dimming overlay and its fade transitions. |
| `CountdownManager` | `ui/view/` | Manages the 3-2-1-GO countdown sequence when resuming from pause. |
| `GameTimer` | `ui/view/` | Tracks elapsed play time and formats it for display. |
| `GameEffectManager` | `ui/view/` | Manages visual effects (fog, heavy gravity indicators, earthquake animation). |
| `NotificationPanel` | `ui/view/` | Panel for displaying floating score notifications. |
| `RowClearMessage` | `ui/view/` | Displays animated line clear reward messages (Single, Double, Triple, Tetris). |
| `LineClearNotification` | `ui/view/` | Alternative notification style for line clears. |
| `BackgroundAnimator` | `ui/view/` | Handles animated background effects in the game view. |
| `AudioSettingsController` | `ui/view/` | Controller for in-game audio settings. |
| `TetrisMainMenu` | `ui/view/` | Alternative menu implementation (unused in final version). |

### 8.6 Utility Layer (`com.comp2042.tetris.util`)

| Class | Location | Purpose |
|-------|----------|---------|
| `CollisionDetector` | `util/` | Static utility for detecting brick-to-board collisions. Used by movement and rotation logic. |
| `MatrixOperations` | `util/` | Static utilities for matrix operations (copy, merge, row clearing). Core algorithms extracted for testability. |

---

## 9. Modified Java Classes

The original codebase was a single-file or minimal Tetris implementation. The entire architecture was refactored to follow SOLID principles, clean architecture patterns, and proper separation of concerns. Key modifications include:

- **Extracted domain logic** from UI code into dedicated domain and engine layers.
- **Applied the Strategy pattern** to scoring (`ScoringPolicy`), enabling different scoring rules per game mode.
- **Applied the Command pattern** to input handling (`GameCommand`), decoupling key presses from action execution.
- **Applied the State pattern** to game state management (`GameStateManager`), making state transitions explicit and safe.
- **Applied the Factory pattern** to board and brick creation, enabling dependency injection and testability.
- **Applied the Observer pattern** to game-over detection (`SpawnManager`), allowing multiple listeners.
- **Applied the Decorator pattern** to view rendering (`BufferedGameView`), enabling transparent performance optimization.
- **Applied the Mediator pattern** to UI coordination (`GameMediator`), reducing coupling between view components.
- **Applied the Template Method pattern** to game controllers (`BaseGameController`), allowing mode-specific customization while reusing common logic.

---

## 10. Unexpected Problems

### 10.1 JavaFX Timeline Timing Issues in Mystery Mode

**Problem:** Random events in Mystery Mode (inverted controls, fog, gravity changes) occasionally overlapped or failed to revert properly due to concurrent `Timeline` instances modifying shared state.

**Resolution:** Implemented explicit timeline cancellation before starting new events and added null checks before stopping timelines. Used `Platform.runLater()` consistently for UI updates to avoid race conditions.

### 10.2 Ghost Piece Position Calculation

**Problem:** The ghost piece (drop preview) occasionally showed incorrect positions when the active piece was near the board edges or rotated.

**Resolution:** Refactored `CollisionDetector` to properly handle edge cases and ensured `ViewData` always calculates ghost position using the actual board state rather than a stale cache.

### 10.3 Audio Playback on Application Close

**Problem:** Closing the application while music was playing caused `MediaPlayer` disposal errors and occasional crashes.

**Resolution:** Added proper cleanup in `MusicManager.fadeOutAndStop()` with try-catch around dispose calls and ensured `Platform.exit()` is called in `Main.start()` via stage close handler.

### 10.4 Font Loading Failures

**Problem:** Custom fonts (arcade-style fonts) failed to load on some systems, causing fallback to default fonts and inconsistent visuals.

**Resolution:** Added fallback font chains in `ViewInitializer` and wrapped font loading in try-catch blocks with null checks before applying fonts to labels.

### 10.5 Input During Pause State

**Problem:** Key presses were still being processed during the pause state, causing pieces to move while the game appeared paused.

**Resolution:** Added `GameState.canAcceptInput()` check in `InputHandler` and ensured the state machine properly blocks input processing in non-PLAYING states.

### 10.6 Memory Leak in Background Animation

**Problem:** The `BackgroundEffectsManager` continued creating particles and falling shapes even after leaving the main menu, causing memory growth.

**Resolution:** Added `stopAnimation()` method and ensured it is called when transitioning to the game scene. Particles and shapes are properly removed from the scene graph when off-screen.

### 10.7 Music Files Not Loading

**Problem:** Audio files added to the resources folder were not loading into the game, resulting in silent gameplay despite the music tracks being present in the project.

**Resolution:** Ensured audio files were placed in the correct `/resources/audio/` directory and used `getClass().getResource()` with the proper path format. Added null checks in `MusicManager` to gracefully handle missing audio resources and log warnings instead of crashing.

### 10.8 Visual Regression After SRP Refactoring

**Problem:** After applying Single Responsibility Principle to large classes like `GuiController` and `MenuController`, the game visuals broke in multiple ways—misaligned elements, missing components, and incorrect rendering order.

**Resolution:** Carefully traced dependencies between extracted classes and ensured proper initialization order. Created mediator classes (`GameMediator`, `ViewInitializer`) to coordinate between the newly separated components and maintain the same visual behavior as before refactoring.

### 10.9 Animation Breaking After Code Changes

**Problem:** Various animations (menu transitions, piece settling, level-up effects) would break or behave unexpectedly after code modifications, sometimes freezing mid-animation or not triggering at all.

**Resolution:** Standardized animation handling by ensuring all `Timeline` and `Transition` objects are properly stopped before starting new ones. Added `setOnFinished()` callbacks to clean up animation state and used `Platform.runLater()` for animations triggered from non-UI threads.

### 10.10 Container Resizing Collision Problems

**Problem:** When UI containers were resized (particularly during scene transitions or window focus changes), collision detection would malfunction, causing pieces to clip through walls or overlap with locked pieces.

**Resolution:** Decoupled the visual rendering from the game logic collision calculations. Ensured `CollisionDetector` always operates on the logical grid coordinates rather than pixel positions, and added validation checks after any resize events.

### 10.11 Game Mode Feature Overlap

**Problem:** Features and soundtracks intended for specific game modes (e.g., Mystery Mode events, Rush Mode timer) would leak into other game modes, causing confusion and unintended behavior.

**Resolution:** Refactored game mode initialization to use the Template Method pattern in `BaseGameController`, with each subclass (`ClassicGameController`, `MysteryGameController`, `TimedGameController`) overriding only its specific behavior. Ensured `MusicManager.playTrack()` is called with the correct track enum in each controller's `onStart()` method, and added cleanup in mode transitions.

---

## 11. Testing

### 11.1 Test Categories and Coverage

| Category | Test Classes | Purpose |
|----------|--------------|----------|
| **Utility Classes** | `CollisionDetectorTest`, `MatrixOperationsTest` | Verify core algorithms for collision detection and row clearing work correctly at boundaries and edge cases |
| **Domain Logic** | `ScoreManagerTest`, `ClassicScoringPolicyTest` | Ensure scoring calculations are accurate and property bindings update correctly |
| **Engine Components** | `SimpleBoardTest`, `BrickRegistryTest`, `RandomBrickGeneratorTest`, `BrickShapeTest`, `BrickMoveTest`, `BrickPositionManagerTest`, `SpawnManagerTest`, `GameStateManagerTest` | Validate board operations, brick generation, movement mechanics, and state transitions |
| **Services** | `MusicManagerAPITest`, `NotificationManagerTest` | Test audio playback API and notification lifecycle without requiring actual audio files |
| **UI Components** | `InputSystemTest`, `UIRenderingTest`, `BufferedGameViewTest` | Verify input routing, rendering logic, and caching behavior |
| **Controllers** | `GameControllerTest`, `GameLoopControllerTest`, `MysteryGameControllerTest` | Test game loop timing, mode-specific behavior, and controller lifecycle |

### 11.2 Why These Tests Matter

- **Regression Prevention:** After SRP refactoring split large classes into 110 smaller ones, tests ensured existing functionality wasn't broken
- **Edge Case Coverage:** `CollisionDetectorTest` catches boundary conditions (pieces at walls, floor, ceiling) that caused bugs during development
- **State Machine Validation:** `GameStateManagerTest` ensures impossible state transitions (e.g., pausing during game over) are blocked
- **Isolation Verification:** Tests confirm extracted classes work independently, validating the SRP decomposition

### 11.3 Bugs Prevented by Tests

| Test | Bug Prevented |
|------|---------------|
| `MatrixOperationsTest.clearRows()` | Rows not shifting down correctly after clearing, leaving gaps |
| `CollisionDetectorTest.edgeCases()` | Pieces clipping through walls or floor at board boundaries |
| `BrickRotatorTest.rotationCycle()` | Rotation index overflow causing array out-of-bounds exceptions |
| `SpawnManagerTest.gameOverDetection()` | Game continuing after pieces stack to top (spawn collision not detected) |
| `BufferedGameViewTest.caching()` | Performance degradation from unnecessary full-board redraws every frame |
| `GameStateManagerTest.transitions()` | Input being processed during pause state, moving pieces while paused |

**Run tests with:** `mvn test`

---

## 12. Architecture Summary

### 12.1 Final Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer                             │
│  (controller/, view/, input/, render/, theme/, animation/)  │
│                     ↓ uses interfaces ↓                     │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                        │
│            (command/, port/, session/)                      │
│                      ↓ orchestrates ↓                       │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                           │
│                 (model/, scoring/)                          │
│                         ↓ uses ↓                            │
├─────────────────────────────────────────────────────────────┤
│                      Engine Layer                           │
│    (board/, bricks/, movement/, rotation/, spawn/, state/)  │
│                         ↓ uses ↓                            │
├─────────────────────────────────────────────────────────────┤
│                     Services Layer                          │
│                   (audio/, notify/)                         │
├─────────────────────────────────────────────────────────────┤
│                     Utilities Layer                         │
│               (CollisionDetector, MatrixOperations)         │
└─────────────────────────────────────────────────────────────┘
```

**Dependency Rule:** Upper layers depend on lower layers only. No upward or circular dependencies.

### 12.2 Architecture Transformation

| Aspect | Original Codebase | Refactored Architecture |
|--------|-------------------|-------------------------|
| **Structure** | 2–3 large files, flat package | 110 classes across 6 layers |
| **Board Logic** | Single 800+ line class | 6 focused classes (`SimpleBoard`, `CollisionDetector`, `MatrixOperations`, etc.) |
| **UI Code** | 1200+ line controller | 12+ specialized renderers and managers |
| **Game Modes** | Would require copy-paste | Inheritance via `BaseGameController` + Template Method |
| **Scoring** | Hardcoded inline | Pluggable `ScoringPolicy` strategy |
| **Input** | Giant switch statement | Command pattern with `GameCommand` objects |
| **State** | Boolean flags | `GameStateManager` with State pattern |
| **Testing** | Untestable (tightly coupled) | 27 test classes with mocked dependencies |

### 12.3 Why This Architecture Is More Maintainable

| Principle | How It's Applied | Maintainability Benefit |
|-----------|------------------|-------------------------|
| **Single Responsibility** | Each class has one job (e.g., `CollisionDetector` only detects collisions) | Changes are localized; modifying collision logic doesn't risk breaking rendering |
| **Open/Closed** | `ScoringPolicy`, `BrickRegistry`, `GameCommand` are extension points | New features (scoring modes, brick types, controls) don't require modifying existing code |
| **Dependency Inversion** | Controllers depend on `BoardPorts` interface, not `SimpleBoard` | Board implementation can be swapped for testing or alternative rules |
| **Separation of Concerns** | UI layer knows nothing about collision math; Engine knows nothing about JavaFX | Teams could work on UI and engine simultaneously without conflicts |
| **Testability** | Pure utility classes, injectable dependencies, interface contracts | Bugs caught early; refactoring is safe with test coverage |

---

## 13. Summary

### 13.1 Key Achievements

| Aspect | Details |
|--------|---------|
| **Total Classes** | 110 Java classes across 6 architectural layers |
| **Design Patterns Applied** | 8 patterns (Strategy, Command, State, Factory, Observer, Decorator, Mediator, Template Method) |
| **Game Modes Implemented** | 3 (Classic, Rush/Timed, Mystery) |
| **Test Coverage** | 27 test classes using JUnit 5 and Mockito |
| **Audio Tracks** | 5 dynamic soundtracks with fade transitions |

### 13.2 Technical Highlights

- **Clean Architecture:** Separated concerns into UI, Application, Domain, Engine, Services, and Utility layers
- **SOLID Principles:** Applied throughout—particularly SRP in controller extraction and OCP in scoring/brick systems
- **Extensibility:** Plugin-style `BrickRegistry` and `ScoringPolicy` interfaces allow easy addition of new content
- **Reactive UI:** JavaFX property bindings enable automatic UI updates from game state changes
- **Robust Error Handling:** Graceful degradation for missing resources (fonts, audio) with fallback mechanisms

### 13.3 Personal Reflection & Lessons Learned

#### What Went Well

- **Incremental Refactoring Paid Off:** Breaking down the god classes step-by-step (first extract utilities, then apply SRP, then add patterns) made the transformation manageable and reduced risk of breaking everything at once.
- **Design Patterns Simplified Extensions:** Once the Template Method pattern was in place for `BaseGameController`, adding Mystery Mode and Rush Mode was straightforward—I only needed to implement the hooks, not duplicate the entire game loop.
- **Testing Caught Real Bugs:** Unit tests for `CollisionDetector` found edge cases where pieces could clip through walls at certain positions. Without tests, these would have been frustrating player-facing bugs.

#### Challenges Faced

- **Visual Regressions After SRP:** The biggest surprise was how often the game broke visually after extracting classes. Rendering order, initialization timing, and component coordination all had implicit dependencies that only became apparent after separation. The `GameMediator` pattern was the solution.
- **JavaFX Timeline Complexity:** Managing multiple concurrent `Timeline` animations (Mystery Mode events, piece settling, countdown) led to race conditions. Learning to explicitly cancel timelines and use `Platform.runLater()` consistently was essential.
- **Balancing Refactoring vs. Features:** Time pressure meant choosing between "more refactoring" and "more features." I prioritized architectural quality over feature quantity, which I believe was the right choice for a maintainability-focused coursework.
