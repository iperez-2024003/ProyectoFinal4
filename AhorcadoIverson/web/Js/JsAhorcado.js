function AhorcadoGame() {
    // Inicializo las propiedades con let
    let currentWord = null;
    let currentHints = [];
    let guessedWord = [];
    let usedLetters = [];
    let attemptsLeft = 3;
    let wrongGuesses = 0;
    let gameActive = false;
    let gamePaused = false;
    let timer = 0;
    let timerInterval = null;
    const elements = {};

    // Método para inicializar los elementos del DOM
    this.initializeElements = function() {
        elements.startBtn = document.getElementById('start-btn');
        elements.pauseBtn = document.getElementById('pause-btn');
        elements.restartBtn = document.getElementById('restart-btn');
        elements.letterInput = document.getElementById('letter-input');
        elements.guessBtn = document.getElementById('guess-btn');
        elements.wordContainer = document.getElementById('word-container');
        elements.usedLettersContainer = document.getElementById('used-letters-container');
        elements.attemptsDisplay = document.getElementById('attempts');
        elements.timerDisplay = document.getElementById('timer');
        elements.gameMessage = document.getElementById('game-message');
        elements.hintText1 = document.getElementById('hint-text-1');
        elements.hintText2 = document.getElementById('hint-text-2');
        elements.hintText3 = document.getElementById('hint-text-3');
        elements.modal = document.getElementById('game-over-modal');
        elements.modalTitle = document.getElementById('modal-title');
        elements.modalMessage = document.getElementById('modal-message');
        elements.finalTime = document.getElementById('final-time');
        elements.revealedWord = document.getElementById('revealed-word');
        elements.playAgainBtn = document.getElementById('play-again-btn');
        elements.hangmanImage = document.getElementById('hangman-image');
    };

    // Método para inicializar eventos
    this.bindEvents = function() {
        elements.startBtn.addEventListener('click', () => this.startGame());
        elements.pauseBtn.addEventListener('click', () => this.pauseGame());
        elements.restartBtn.addEventListener('click', () => this.restartGame());
        elements.guessBtn.addEventListener('click', () => this.makeGuess());
        elements.letterInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.makeGuess();
            }
        });
        elements.playAgainBtn.addEventListener('click', () => {
            this.hideModal();
            this.startGame();
        });
        elements.letterInput.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/[^a-zA-ZñÑ]/g, '').toUpperCase();
        });
    };

    // Método para inicializar el juego (reemplaza el constructor)
    this.initGame = function() {
        this.initializeElements();
        this.bindEvents();
        this.updateDisplay();
        this.loadWordFromSession();
    };

    // Método para cargar la palabra desde la sesión JSP
    this.loadWordFromSession = function() {
        // Verificar si hay datos de la palabra en la sesión (JSP los pasa como variables JavaScript)
        if (typeof palabraSession !== 'undefined' && palabraSession) {
            currentWord = palabraSession.toUpperCase();
            currentHints = [
                pistaSession1 || "Sin pista disponible",
                pistaSession2 || "Sin pista disponible", 
                pistaSession3 || "Sin pista disponible"
            ];
            console.log("Palabra cargada desde sesión:", currentWord);
        } else {
            console.log("No se encontró palabra en la sesión");
        }
    };

    // Método para iniciar el juego
    this.startGame = function() {
        // Si no hay palabra cargada, redirigir al servlet para obtener una nueva
        if (!currentWord) {
            elements.gameMessage.textContent = "Obteniendo nueva palabra...";
            window.location.href = 'PalabraAleatoria';
            return;
        }

        this.resetGame();
        this.initializeWord();
        gameActive = true;
        gamePaused = false;
        this.startTimer();
        this.updateControlButtons();
        this.updateDisplay();
        elements.letterInput.focus();
        elements.gameMessage.textContent = "¡Juego iniciado! Adivina la palabra.";
    };

    // Método para inicializar la palabra
    this.initializeWord = function() {
        guessedWord = new Array(currentWord.length).fill('_');
        this.displayHints();
    };

    // Método para pausar/reanudar el juego
    this.pauseGame = function() {
        if (gameActive) {
            gamePaused = !gamePaused;
            if (gamePaused) {
                this.pauseTimer();
                elements.pauseBtn.textContent = "▶️ CONTINUAR";
                elements.letterInput.disabled = true;
                elements.guessBtn.disabled = true;
                elements.gameMessage.textContent = "⏸️ Juego pausado";
            } else {
                this.resumeTimer();
                elements.pauseBtn.textContent = "⏸️ PAUSAR";
                elements.letterInput.disabled = false;
                elements.guessBtn.disabled = false;
                elements.letterInput.focus();
                elements.gameMessage.textContent = "▶️ Juego reanudado";
            }
        }
    };

    // Método para reiniciar el juego
    this.restartGame = function() {
        this.resetGame();
        this.updateDisplay();
        this.updateControlButtons();
        elements.gameMessage.textContent = "¡Presiona INICIAR para comenzar!";
    };

    // Método para resetear el estado del juego
    this.resetGame = function() {
        guessedWord = [];
        usedLetters = [];
        attemptsLeft = 3;
        wrongGuesses = 0;
        gameActive = false;
        gamePaused = false;
        timer = 0;
        this.stopTimer();
        this.hideModal();
        elements.hangmanImage.src = "images/Poste.png";
    };

    // Método para mostrar las pistas
    this.displayHints = function() {
        if (currentHints && currentHints.length >= 3) {
            elements.hintText1.textContent = currentHints[0];
            elements.hintText2.textContent = currentHints[1];
            elements.hintText3.textContent = currentHints[2];
        }
    };

    // Método para procesar una adivinanza
    this.makeGuess = function() {
        if (!gameActive || gamePaused) return;

        const letter = elements.letterInput.value.trim().toUpperCase();
        
        if (!letter) {
            elements.gameMessage.textContent = "⚠️ Por favor ingresa una letra";
            return;
        }

        if (usedLetters.includes(letter)) {
            elements.gameMessage.textContent = "⚠️ Ya has usado esta letra";
            elements.letterInput.value = '';
            return;
        }

        usedLetters.push(letter);
        
        if (currentWord.includes(letter)) {
            for (let i = 0; i < currentWord.length; i++) {
                if (currentWord[i] === letter) {
                    guessedWord[i] = letter;
                }
            }
            elements.gameMessage.textContent = `✅ ¡Bien! La letra "${letter}" está en la palabra`;
            
            if (!guessedWord.includes('_')) {
                this.winGame();
            }
        } else {
            attemptsLeft--;
            wrongGuesses++;
            this.showHangmanPart();
            elements.gameMessage.textContent = `❌ La letra "${letter}" no está en la palabra`;
            
            if (attemptsLeft <= 0) {
                this.loseGame();
            }
        }

        elements.letterInput.value = '';
        this.updateDisplay();
        elements.letterInput.focus();
    };

    // Método para mostrar partes del ahorcado
    this.showHangmanPart = function() {
        if (wrongGuesses === 1) {
            elements.hangmanImage.src = "images/ahorcado1.png";
        } else if (wrongGuesses === 2) {
            elements.hangmanImage.src = "images/ahorcado2.png";
        } else if (wrongGuesses === 3) {
            elements.hangmanImage.src = "images/ahorcado3.png";
        }
    };

    // Método para manejar la victoria
    this.winGame = function() {
        gameActive = false;
        this.stopTimer();
        this.showModal("🎉 ¡FELICITACIONES!", "¡Has ganado! Adivinaste la palabra correctamente.", "win");
    };

    // Método para manejar la derrota
    this.loseGame = function() {
        gameActive = false;
        this.stopTimer();
        this.showModal("💀 JUEGO TERMINADO", "Se te acabaron los intentos. ¡Inténtalo de nuevo!", "lose");
    };

    // Método para mostrar el modal
    this.showModal = function(title, message, type) {
        elements.modalTitle.textContent = title;
        elements.modalMessage.textContent = message;
        elements.finalTime.textContent = this.formatTime(timer);
        elements.revealedWord.textContent = currentWord || "---";
        
        const modalContent = elements.modal.querySelector('.modal-content');
        modalContent.className = `modal-content ${type}`;
        
        elements.modal.style.display = 'flex';
    };

    // Método para ocultar el modal
    this.hideModal = function() {
        elements.modal.style.display = 'none';
    };

    // Método para iniciar el temporizador
    this.startTimer = function() {
        timerInterval = setInterval(() => {
            timer++;
            this.updateTimerDisplay();
        }, 1000);
    };

    // Método para pausar el temporizador
    this.pauseTimer = function() {
        if (timerInterval) {
            clearInterval(timerInterval);
            timerInterval = null;
        }
    };

    // Método para reanudar el temporizador
    this.resumeTimer = function() {
        this.startTimer();
    };

    // Método para detener el temporizador
    this.stopTimer = function() {
        if (timerInterval) {
            clearInterval(timerInterval);
            timerInterval = null;
        }
    };

    // Método para actualizar la pantalla del temporizador
    this.updateTimerDisplay = function() {
        elements.timerDisplay.textContent = this.formatTime(timer);
    };

    // Método para formatear el tiempo
    this.formatTime = function(seconds) {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    };

    // Método para actualizar la interfaz
    this.updateDisplay = function() {
        elements.wordContainer.innerHTML = guessedWord
            .map(letter => `<span class="letter-box">${letter}</span>`)
            .join('');
        elements.usedLettersContainer.innerHTML = usedLetters
            .map(letter => `<span class="used-letter">${letter}</span>`)
            .join('');
        elements.attemptsDisplay.textContent = attemptsLeft;
        this.updateTimerDisplay();
    };

    // Método para actualizar los botones de control
    this.updateControlButtons = function() {
        if (gameActive) {
            elements.startBtn.disabled = true;
            elements.pauseBtn.disabled = false;
            elements.letterInput.disabled = gamePaused;
            elements.guessBtn.disabled = gamePaused;
        } else {
            elements.startBtn.disabled = false;
            elements.pauseBtn.disabled = true;
            elements.pauseBtn.textContent = "⏸️ PAUSAR";
            elements.letterInput.disabled = true;
            elements.guessBtn.disabled = true;
        }
    };

    // Llamo a initGame para inicializar el juego
    this.initGame();
}

// Inicializar el juego cuando se carga la página
document.addEventListener('DOMContentLoaded', () => {
    window.ahorcadoGame = new AhorcadoGame();
});