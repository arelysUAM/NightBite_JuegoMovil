package ni.edu.uam.nightbiteapp.ui.screens

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import ni.edu.uam.nightbiteapp.game.TutorialGameFragment
import ni.edu.uam.nightbiteapp.game.TutorialGameResult
import ni.edu.uam.nightbiteapp.game.TutorialResultBridge

@Composable
fun LibGdxTutorialScreen(
    onTutorialFinished: (TutorialGameResult) -> Unit,
    onBackToHome: () -> Unit,
    onExitApp: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity

    val mainHandler = remember {
        Handler(Looper.getMainLooper())
    }

    val lastBackPressTime = remember {
        mutableLongStateOf(0L)
    }

    var isLeavingGame by remember {
        mutableStateOf(false)
    }

    val containerId = remember {
        View.generateViewId()
    }

    val fragmentTag = remember(containerId) {
        "tutorial_game_fragment_$containerId"
    }

    fun removeGameFragmentIfNeeded() {
        val existingFragment =
            activity.supportFragmentManager.findFragmentByTag(fragmentTag)

        if (existingFragment != null) {
            activity.supportFragmentManager.commitNow(
                allowStateLoss = true
            ) {
                remove(existingFragment)
            }
        }
    }

    fun leaveGameAfterFragmentRemoval(
        nextAction: () -> Unit
    ) {
        if (isLeavingGame) return

        isLeavingGame = true

        TutorialResultBridge.clear()
        mainHandler.removeCallbacksAndMessages(null)

        removeGameFragmentIfNeeded()

        mainHandler.postDelayed(
            {
                nextAction()
            },
            NAVIGATION_AFTER_FRAGMENT_REMOVAL_DELAY
        )
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime.longValue < EXIT_PRESS_INTERVAL) {
            onExitApp()
        } else {
            lastBackPressTime.longValue = currentTime

            Toast.makeText(
                context,
                "Presiona nuevamente para salir",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    DisposableEffect(Unit) {
        TutorialResultBridge.onTutorialFinished = { result ->
            mainHandler.post {
                leaveGameAfterFragmentRemoval {
                    onTutorialFinished(result)
                }
            }
        }

        TutorialResultBridge.onBackToHomeRequested = {
            mainHandler.post {
                leaveGameAfterFragmentRemoval {
                    onBackToHome()
                }
            }
        }

        onDispose {
            mainHandler.removeCallbacksAndMessages(null)
            TutorialResultBridge.clear()

            /*
             * No removemos aquí el Fragment.
             *
             * El Fragment de LibGDX debe desmontarse antes de navegar,
             * no durante el dispose de Compose, porque si Compose elimina
             * la vista primero, LibGDX puede quedarse esperando el pause
             * del GLSurfaceView.
             */
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { viewContext ->
            FragmentContainerView(viewContext).apply {
                id = containerId
            }
        },
        update = {
            if (isLeavingGame) return@AndroidView

            val existingFragment =
                activity.supportFragmentManager.findFragmentByTag(fragmentTag)

            if (existingFragment == null) {
                activity.supportFragmentManager.commit {
                    replace(
                        containerId,
                        TutorialGameFragment(),
                        fragmentTag
                    )
                }
            }
        }
    )
}

private const val EXIT_PRESS_INTERVAL = 2 * 1000L
private const val NAVIGATION_AFTER_FRAGMENT_REMOVAL_DELAY = 800L