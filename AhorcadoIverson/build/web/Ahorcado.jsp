<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Juego del Ahorcado IM</title>
    <link rel="stylesheet" href="Css/CssAhorcado.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>🎮 JUEGO DEL AHORCADO IM 🎮</h1>
            <div class="info-panel">
                <div class="timer-section">
                    <span>⏱️ Tiempo: </span>
                    <span id="timer">00:00</span>
                </div>
                <div class="attempts-section">
                    <span>💀 Intentos: </span>
                    <span id="attempts">3</span>
                </div>
            </div>
        </header>

        <main>
            <div class="game-area">
                <div class="hangman-section">
                    <div class="hangman-container">
                        <img id="hangman-image" src="images/Poste.png" alt="Ahorcado">
                        <div id="hangman-display">
                            <div class="hangman-drawing">
                                <div id="gallows">
                                    <div class="base"></div>
                                    <div class="pole"></div>
                                    <div class="top"></div>
                                    <div class="noose"></div>
                                </div>
                                <div id="person">
                                    <div class="head" style="display: none;"></div>
                                    <div class="body" style="display: none;"></div>
                                    <div class="left-arm" style="display: none;"></div>
                                    <div class="right-arm" style="display: none;"></div>
                                    <div class="left-leg" style="display: none;"></div>
                                    <div class="right-leg" style="display: none;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="word-section">
                    <div class="word-display">
                        <div id="word-container"></div>
                    </div>
                    
                    <div class="hints-section">
                        <h3>💡 Pistas de la palabra:</h3>
                        <div id="hints-container">
                            <div class="hint" id="hint1">🔍 Pista 1: <span id="hint-text-1">---</span></div>
                            <div class="hint" id="hint2">🔍 Pista 2: <span id="hint-text-2">---</span></div>
                            <div class="hint" id="hint3">🔍 Pista 3: <span id="hint-text-3">---</span></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="input-section">
                <div class="letter-input">
                    <input type="text" id="letter-input" maxlength="1" placeholder="Ingresa una letra">
                    <button id="guess-btn">Adivinar</button>
                </div>
                
                <div class="used-letters">
                    <h4>Letras usadas:</h4>
                    <div id="used-letters-container"></div>
                </div>
            </div>

            <div class="controls">
                <button id="start-btn" class="control-btn start">🎯 INICIAR</button>
                <button id="pause-btn" class="control-btn pause" disabled>⏸️ PAUSAR</button>
                <button id="restart-btn" class="control-btn restart">🔄 REINICIAR</button>
            </div>
        </main>

        <div class="game-status">
            <div id="message-container">
                <p id="game-message">¡Presiona INICIAR para comenzar!</p>
            </div>
        </div>

        <!-- Modal para fin de juego -->
        <div id="game-over-modal" class="modal" style="display: none;">
            <div class="modal-content">
                <h2 id="modal-title">¡Fin del Juego!</h2>
                <p id="modal-message">Mensaje del juego</p>
                <div class="modal-stats">
                    <p>⏱️ Tiempo total: <span id="final-time">00:00</span></p>
                    <p>🔍 Palabra era: <span id="revealed-word">---</span></p>
                </div>
                <button id="play-again-btn" class="control-btn">🎮 JUGAR DE NUEVO</button>
            </div>
        </div>
    </div>


    <script type="text/javascript">
        var palabraSession = '<%= session.getAttribute("palabra") != null ? session.getAttribute("palabra") : "" %>';
        var pistaSession1 = '<%= session.getAttribute("pista1") != null ? session.getAttribute("pista1") : "" %>';
        var pistaSession2 = '<%= session.getAttribute("pista2") != null ? session.getAttribute("pista2") : "" %>';
        var pistaSession3 = '<%= session.getAttribute("pista3") != null ? session.getAttribute("pista3") : "" %>';
        
        console.log("Datos de sesión cargados:");
        console.log("Palabra:", palabraSession);
        console.log("Pistas:", pistaSession1, pistaSession2, pistaSession3);
    </script>
    
    <script src="Js/JsAhorcado.js"></script>
</body>
</html>