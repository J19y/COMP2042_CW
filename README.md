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

1. [GitHub Repository Link](#1-github-repository-link)
2. [Compilation Instructions](#2-compilation-instructions)
3. [Implemented and Working Properly](#3-implemented-and-working-properly)
4. [Implemented but Not Working Properly](#4-implemented-but-not-working-properly)
5. [Features Not Implemented](#5-features-not-implemented)
6. [New Java Classes](#6-new-java-classes)
7. [Modified Java Classes](#7-modified-java-classes)
8. [Unexpected Problems](#8-unexpected-problems)
9. [Testing](#9-testing)
10. [Architecture Summary](#10-architecture-summary)
11. [Summary](#11-summary)

---

## 1. GitHub Repository Link

[https://github.com/J19y/COMP2042_CW](https://github.com/J19y/COMP2042_CW)

---

## 2. Compilation Instructions

### 2.1 Prerequisites

#### 2.1.1 Java Development Kit (JDK 23)

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

#### 2.1.2 Git (for cloning the repository)

**Download and Install:**
- **Windows:** Download and install from [Git for Windows](https://git-scm.com/download/win)
- **macOS:** Install via Homebrew: `brew install git`
- **Linux:** Use apt: `sudo apt install git` (Debian/Ubuntu) or `sudo dnf install git` (Fedora)

**Verify installation:**
```shell
git --version
```

#### 2.1.3 Maven (Optional - Maven Wrapper included)

The project includes Maven Wrapper (`mvnw.cmd`), so Maven installation is **optional**. However, if you prefer a global Maven installation:

**Download and Install:**
- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Extract to `C:\Program Files\Apache\maven`
- Add `C:\Program Files\Apache\maven\bin` to your system PATH

**Verify installation:**
```shell
mvn --version
```

#### 2.1.4 IDE (Optional but Recommended)

Any of the following IDEs can be used:
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community Edition is free)
- [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/)
- [VS Code](https://code.visualstudio.com/) with [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

#### 2.1.5 JavaFX (Automatically Managed)

JavaFX 21.0.6 dependencies are **automatically downloaded** by Maven. No manual installation required.

---

### 2.2 Steps to Compile and Run

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

### 2.3 Running from an IDE

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

### 2.4 Troubleshooting

| Issue | Solution |
|-------|----------|
| `'java' is not recognized` | Ensure JAVA_HOME is set and `%JAVA_HOME%\bin` is in PATH |
| `javafx.controls not found` | Run via Maven (`mvnw.cmd javafx:run`) instead of direct Java execution |
| `Could not find or load main class` | Run `.\mvnw.cmd clean compile` first |
| Maven download fails | Check internet connection; try `.\mvnw.cmd -U clean compile` to force update |
| Permission denied on `mvnw` | On Linux/Mac: run `chmod +x mvnw` |

---

## 3. Implemented and Working Properly

### 3.1 Core Gameplay Features

- **Classic Tetris Mechanics:** Full implementation of brick movement (left, right, down), rotation, and hard drop functionality. Pieces lock when they cannot move further down.

- **Three Game Modes:**
  - **Classic Mode:** Standard endless Tetris with progressive scoring.
  - **Timed Mode (Rush):** 2-minute time-limited gameplay where the objective is to maximize score before time expires.
  - **Mystery Mode:** Features random events (inverted controls, fog effect, gravity changes, speed boosts) that trigger periodically, with progressive difficulty scaling every 30 seconds.

- **Scoring System:** Points awarded for line clears (50 × lines² formula) and soft drops (1 point per user-initiated drop). Hard drops award points based on distance fallen.

- **Ghost Piece Preview:** Shows where the current piece will land, rendered with subtle neon styling.

- **Next Brick Queue:** Displays up to 5 upcoming pieces with animated preview panels.

- **Line Clear Detection and Animation:** Cleared rows trigger visual feedback with score bonus notifications.

### 3.2 User Interface Features

- **Animated Main Menu:** Features falling tetromino shapes in the background, neon glow effects, and smooth fade transitions.

- **Level Selection Panel:** Interactive mode selection with animated card transitions and tooltips describing each game mode.

- **Pause System:** Press 'P' to pause. Displays a dimmed overlay with countdown timer (3-2-1-GO) when resuming.

- **Game Over Screen:** Shows final score, time played, lines cleared, and level reached. Includes Retry and Main Menu buttons with animated entrance effects.

- **Settings Panel:** Volume slider and music toggle accessible from the main menu.

- **Control Help Panel:** Visual diagram showing keyboard controls (Arrow keys/WASD, Space for hard drop, P to pause).

### 3.3 Audio System

- **Dynamic Soundtrack:** Different background tracks for each game mode (Main Menu, Classic, Rush, Mystery, Game Over).
- **Sound Effects:** Audio feedback for piece rotation and placement.
- **Volume Control:** Adjustable music volume with fade transitions between tracks.
- **Music Ducking:** Background music temporarily quiets during countdown sequences.

### 3.4 Visual Polish

- **Neon Theme:** All game elements use a cohesive neon aesthetic with glow effects and drop shadows.
- **Smooth Animations:** Piece settling animation, level-up effects, line clear flash effects.
- **Timer Display:** Digital-style font for countdown and game timer (Rush mode).

---

## 4. Implemented but Not Working Properly

- **Mystery Mode Heavy Gravity Event:** The visual indicator for heavy gravity appears, but the actual gravity multiplier does not consistently affect the drop speed during the effect duration. The fallback timer restores the state correctly, but the intermediate behavior is inconsistent.

- **Fog Effect Persistence:** In Mystery Mode, the fog overlay occasionally persists briefly after the intended duration due to timing conflicts with other random events.

- **Pause Button Hover Lag:** The pause button in gameplay sometimes experiences lag when hovering over it, causing delayed visual feedback on the hover state.

- **Game Mode Transition Delays:** Transitions between game levels occasionally lag or get delayed, resulting in brief stutters during level progression.

- **Game Mode Tooltip Inconsistency:** When hovering over the game mode selection cards, the descriptive tooltip message does not always appear consistently, requiring multiple hover attempts.

---

## 5. Features Not Implemented

- **High Score Persistence:** Scores are not saved between sessions. A leaderboard system was planned but not completed due to time constraints.

- **Customizable Key Bindings:** The `InputHandler` supports registering custom key bindings programmatically, but no UI was built to allow players to remap controls.

- **Additional Brick Types:** The `BrickRegistry` is designed for extensibility, but no custom tetromino shapes beyond the standard 7 were added.

- **Multiplayer Mode:** No networked or local two-player mode was implemented.

- **Theme Customization:** A light theme variant of the game was planned to provide visual accessibility options, but implementation proved difficult due to the extensive neon styling applied throughout the UI components. Changing the color scheme would require significant refactoring of the `ColorPalette`, `NeonGlowStyle`, and CSS stylesheets.

---

## 6. New Java Classes

### 6.1 Application Layer (`com.comp2042.tetris.application`)

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

### 6.2 Domain Layer (`com.comp2042.tetris.domain`)

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

### 6.3 Engine Layer (`com.comp2042.tetris.engine`)

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

### 6.4 Services Layer (`com.comp2042.tetris.services`)

| Class | Location | Purpose |
|-------|----------|---------|
| `MusicManager` | `services/audio/` | Singleton managing background music playback, volume control, fade transitions, and sound effects. |
| `NotificationManager` | `services/notify/` | Manages in-game notifications (score bonuses, line clear messages, event announcements). |

### 6.5 UI Layer (`com.comp2042.tetris.ui`)

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

### 6.6 Utility Layer (`com.comp2042.tetris.util`)

| Class | Location | Purpose |
|-------|----------|---------|
| `CollisionDetector` | `util/` | Static utility for detecting brick-to-board collisions. Used by movement and rotation logic. |
| `MatrixOperations` | `util/` | Static utilities for matrix operations (copy, merge, row clearing). Core algorithms extracted for testability. |

---

## 7. Modified Java Classes

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

## 8. Unexpected Problems

### 8.1 JavaFX Timeline Timing Issues in Mystery Mode

**Problem:** Random events in Mystery Mode (inverted controls, fog, gravity changes) occasionally overlapped or failed to revert properly due to concurrent `Timeline` instances modifying shared state.

**Resolution:** Implemented explicit timeline cancellation before starting new events and added null checks before stopping timelines. Used `Platform.runLater()` consistently for UI updates to avoid race conditions.

### 8.2 Ghost Piece Position Calculation

**Problem:** The ghost piece (drop preview) occasionally showed incorrect positions when the active piece was near the board edges or rotated.

**Resolution:** Refactored `CollisionDetector` to properly handle edge cases and ensured `ViewData` always calculates ghost position using the actual board state rather than a stale cache.

### 8.3 Audio Playback on Application Close

**Problem:** Closing the application while music was playing caused `MediaPlayer` disposal errors and occasional crashes.

**Resolution:** Added proper cleanup in `MusicManager.fadeOutAndStop()` with try-catch around dispose calls and ensured `Platform.exit()` is called in `Main.start()` via stage close handler.

### 8.4 Font Loading Failures

**Problem:** Custom fonts (arcade-style fonts) failed to load on some systems, causing fallback to default fonts and inconsistent visuals.

**Resolution:** Added fallback font chains in `ViewInitializer` and wrapped font loading in try-catch blocks with null checks before applying fonts to labels.

### 8.5 Input During Pause State

**Problem:** Key presses were still being processed during the pause state, causing pieces to move while the game appeared paused.

**Resolution:** Added `GameState.canAcceptInput()` check in `InputHandler` and ensured the state machine properly blocks input processing in non-PLAYING states.

### 8.6 Memory Leak in Background Animation

**Problem:** The `BackgroundEffectsManager` continued creating particles and falling shapes even after leaving the main menu, causing memory growth.

**Resolution:** Added `stopAnimation()` method and ensured it is called when transitioning to the game scene. Particles and shapes are properly removed from the scene graph when off-screen.

### 8.7 Music Files Not Loading

**Problem:** Audio files added to the resources folder were not loading into the game, resulting in silent gameplay despite the music tracks being present in the project.

**Resolution:** Ensured audio files were placed in the correct `/resources/audio/` directory and used `getClass().getResource()` with the proper path format. Added null checks in `MusicManager` to gracefully handle missing audio resources and log warnings instead of crashing.

### 8.8 Visual Regression After SRP Refactoring

**Problem:** After applying Single Responsibility Principle to large classes like `GuiController` and `MenuController`, the game visuals broke in multiple ways—misaligned elements, missing components, and incorrect rendering order.

**Resolution:** Carefully traced dependencies between extracted classes and ensured proper initialization order. Created mediator classes (`GameMediator`, `ViewInitializer`) to coordinate between the newly separated components and maintain the same visual behavior as before refactoring.

### 8.9 Animation Breaking After Code Changes

**Problem:** Various animations (menu transitions, piece settling, level-up effects) would break or behave unexpectedly after code modifications, sometimes freezing mid-animation or not triggering at all.

**Resolution:** Standardized animation handling by ensuring all `Timeline` and `Transition` objects are properly stopped before starting new ones. Added `setOnFinished()` callbacks to clean up animation state and used `Platform.runLater()` for animations triggered from non-UI threads.

### 8.10 Container Resizing Collision Problems

**Problem:** When UI containers were resized (particularly during scene transitions or window focus changes), collision detection would malfunction, causing pieces to clip through walls or overlap with locked pieces.

**Resolution:** Decoupled the visual rendering from the game logic collision calculations. Ensured `CollisionDetector` always operates on the logical grid coordinates rather than pixel positions, and added validation checks after any resize events.

### 8.11 Game Mode Feature Overlap

**Problem:** Features and soundtracks intended for specific game modes (e.g., Mystery Mode events, Rush Mode timer) would leak into other game modes, causing confusion and unintended behavior.

**Resolution:** Refactored game mode initialization to use the Template Method pattern in `BaseGameController`, with each subclass (`ClassicGameController`, `MysteryGameController`, `TimedGameController`) overriding only its specific behavior. Ensured `MusicManager.playTrack()` is called with the correct track enum in each controller's `onStart()` method, and added cleanup in mode transitions.

---

## 9. Testing

The project includes comprehensive unit tests covering:

- **Utility classes:** `CollisionDetectorTest`, `MatrixOperationsTest`
- **Domain logic:** `ScoreManagerTest`, `ClassicScoringPolicyTest`
- **Engine components:** `SimpleBoardTest`, `BrickRegistryTest`, `RandomBrickGeneratorTest`, `BrickShapeTest`, `BrickMoveTest`, `BrickPositionManagerTest`, `SpawnManagerTest`, `GameStateManagerTest`
- **Services:** `MusicManagerAPITest`, `NotificationManagerTest`
- **UI components:** `InputSystemTest`, `UIRenderingTest`, `BufferedGameViewTest`
- **Controllers:** `GameControllerTest`, `GameLoopControllerTest`, `MysteryGameControllerTest`

Tests use JUnit 5 and Mockito for mocking dependencies. Run with `mvn test`.

---

## 10. Architecture Summary

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer                             │
│  (controller/, view/, input/, render/, theme/, animation/)  │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                        │
│            (command/, port/, session/)                      │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                           │
│                 (model/, scoring/)                          │
├─────────────────────────────────────────────────────────────┤
│                      Engine Layer                           │
│    (board/, bricks/, movement/, rotation/, spawn/, state/)  │
├─────────────────────────────────────────────────────────────┤
│                     Services Layer                          │
│                   (audio/, notify/)                         │
├─────────────────────────────────────────────────────────────┤
│                     Utilities Layer                         │
│               (CollisionDetector, MatrixOperations)         │
└─────────────────────────────────────────────────────────────┘
```

The architecture follows a layered approach with clear dependency rules: upper layers depend on lower layers, never the reverse. Interfaces define contracts between layers, enabling testability and flexibility.

---

## 11. Summary

This coursework involved the comprehensive refactoring and extension of a Tetris game application, transforming it from a minimal implementation into a well-architected, maintainable software system.

### Key Achievements

| Aspect | Details |
|--------|---------|
| **Total New Classes** | 80+ Java classes across 6 architectural layers |
| **Design Patterns Applied** | 8 patterns (Strategy, Command, State, Factory, Observer, Decorator, Mediator, Template Method) |
| **Game Modes Implemented** | 3 (Classic, Rush/Timed, Mystery) |
| **Test Coverage** | 20+ test classes using JUnit 5 and Mockito |
| **Audio Tracks** | 5 dynamic soundtracks with fade transitions |

### Technical Highlights

- **Clean Architecture:** Separated concerns into UI, Application, Domain, Engine, Services, and Utility layers
- **SOLID Principles:** Applied throughout—particularly SRP in controller extraction and OCP in scoring/brick systems
- **Extensibility:** Plugin-style `BrickRegistry` and `ScoringPolicy` interfaces allow easy addition of new content
- **Reactive UI:** JavaFX property bindings enable automatic UI updates from game state changes
- **Robust Error Handling:** Graceful degradation for missing resources (fonts, audio) with fallback mechanisms

### Lessons Learned

1. **Refactoring requires careful dependency tracking**—visual regressions after SRP application taught the importance of integration testing
2. **JavaFX Timeline management is complex**—concurrent animations need explicit lifecycle management
3. **Resource loading paths are platform-sensitive**—consistent use of `getClass().getResource()` is essential
4. **Game mode isolation requires architectural discipline**—the Template Method pattern proved effective for mode-specific behavior
