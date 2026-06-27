package ni.edu.uam.nightbiteapp.game

/**
 * Puente temporal entre LibGDX y Compose.
 *
 * LibGDX se ejecuta dentro de un Fragment, mientras que la navegación
 * principal está en Compose. Este objeto permite enviar eventos del
 * tutorial hacia LibGdxTutorialScreen.
 */
object TutorialResultBridge {

    var onTutorialFinished: ((TutorialGameResult) -> Unit)? = null

    var onBackToHomeRequested: (() -> Unit)? = null

    fun finishTutorial(result: TutorialGameResult) {
        onTutorialFinished?.invoke(result)
    }

    fun requestBackToHome() {
        onBackToHomeRequested?.invoke()
    }

    fun clear() {
        onTutorialFinished = null
        onBackToHomeRequested = null
    }
}