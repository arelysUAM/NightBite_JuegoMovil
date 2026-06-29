package ni.edu.uam.nightbiteapp.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication

class TutorialGameFragment : AndroidFragmentApplication() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val config = AndroidApplicationConfiguration().apply {
            useAccelerometer = false
            useCompass = false
            useImmersiveMode = true
            useWakelock = true
        }

        val levelGame = NightBiteLevelGame(
            onFinished = { result ->
                activity?.runOnUiThread {
                    TutorialResultBridge.finishTutorial(result)
                }
            },
            onBackToHome = {
                activity?.runOnUiThread {
                    TutorialResultBridge.requestBackToHome()
                }
            },
            levelConfig = NightBiteLevelConfigs.tutorial
        )

        return initializeForView(
            levelGame,
            config
        )
    }
}