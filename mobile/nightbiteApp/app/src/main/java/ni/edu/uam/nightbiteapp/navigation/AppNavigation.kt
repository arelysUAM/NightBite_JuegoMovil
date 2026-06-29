package ni.edu.uam.nightbiteapp.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.mock.GameResultsData
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.data.local.mock.NightProgressData
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.screens.AccountScreen
import ni.edu.uam.nightbiteapp.ui.screens.AgeCheckScreen
import ni.edu.uam.nightbiteapp.ui.screens.GamePlaceholderScreen
import ni.edu.uam.nightbiteapp.ui.screens.GameResultScreen
import ni.edu.uam.nightbiteapp.ui.screens.GenderSelectionScreen
import ni.edu.uam.nightbiteapp.ui.screens.HomeScreen
import ni.edu.uam.nightbiteapp.ui.screens.LevelIntroScreen
import ni.edu.uam.nightbiteapp.ui.screens.LoginScreen
import ni.edu.uam.nightbiteapp.ui.screens.PasswordScreen
import ni.edu.uam.nightbiteapp.ui.screens.AchievementsScreen
import ni.edu.uam.nightbiteapp.ui.screens.PlayerCreationScreen
import ni.edu.uam.nightbiteapp.ui.screens.RegisterScreen
import ni.edu.uam.nightbiteapp.ui.screens.SettingsScreen
import ni.edu.uam.nightbiteapp.ui.screens.StartScreen
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModelFactory
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModelFactory
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModel
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModelFactory
import ni.edu.uam.nightbiteapp.ui.screens.WantedPosterTransitionScreen
import ni.edu.uam.nightbiteapp.game.TutorialGameResult
import androidx.compose.runtime.key
import ni.edu.uam.nightbiteapp.data.local.database.NightBiteDatabase
import ni.edu.uam.nightbiteapp.data.repository.GameProgressRepository
import ni.edu.uam.nightbiteapp.data.remote.RetrofitClient
import ni.edu.uam.nightbiteapp.data.repository.ProgressSyncRepository
import ni.edu.uam.nightbiteapp.ui.screens.TutorialLoadingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val sessionManager = remember {
        SessionManager(context.applicationContext)
    }

    val nightBiteDatabase = remember(context) {
        NightBiteDatabase.getDatabase(context.applicationContext)
    }

    val gameProgressDao = remember(nightBiteDatabase) {
        nightBiteDatabase.gameProgressDao()
    }

    val gameProgressRepository = remember(gameProgressDao) {
        GameProgressRepository(gameProgressDao)
    }

    val progressSyncRepository = remember(gameProgressDao) {
        ProgressSyncRepository(
            gameProgressDao = gameProgressDao,
            apiService = RetrofitClient.apiService
        )
    }

    val userSession by sessionManager.userSessionFlow.collectAsState(
        initial = UserSession()
    )

    var activeUserId by remember {
        mutableStateOf<Long?>(null)
    }

    var pendingLoginUsername by remember {
        mutableStateOf("")
    }

    var pendingLoginPassword by remember {
        mutableStateOf("")
    }

    var showLastStepsWelcomeMessage by remember {
        mutableStateOf(false)
    }

    var showHomeWelcomeToast by remember {
        mutableStateOf(false)
    }

    var latestTutorialResult by remember {
        mutableStateOf<TutorialGameResult?>(null)
    }

    var latestRuntimeResultLevelId by remember {
        mutableStateOf<Int?>(null)
    }

    var homeRefreshKey by remember {
        mutableStateOf(0)
    }

    fun navigateBackToHome() {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun navigateToLoginKeepingPrefill() {
        val returnedToLogin = navController.popBackStack(
            route = Routes.LOGIN,
            inclusive = false
        )

        if (!returnedToLogin) {
            navController.navigate(Routes.LOGIN) {
                launchSingleTop = true
            }
        }
    }

    fun navigateToStartAfterInactivity(
        session: UserSession
    ) {
        activeUserId = session.userId
        pendingLoginUsername = ""
        pendingLoginPassword = ""
        showLastStepsWelcomeMessage = false
        showHomeWelcomeToast = false

        navController.navigate(Routes.START) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    DisposableEffect(lifecycleOwner, userSession) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    coroutineScope.launch {
                        sessionManager.saveLastActiveTime()
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    coroutineScope.launch {
                        val shouldResetNavigation =
                            sessionManager.shouldResetNavigationByInactivity(
                                inactivityLimitMillis = INACTIVITY_LIMIT_MILLIS
                            )

                        if (shouldResetNavigation) {
                            navigateToStartAfterInactivity(userSession)
                        }

                        sessionManager.updateLastActiveTime()
                    }
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            val startViewModel: StartViewModel = viewModel(
                factory = StartViewModelFactory(sessionManager)
            )

            StartScreen(
                viewModel = startViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.START) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = { user ->
                    activeUserId = user.id

                    if (user.player == null) {
                        showLastStepsWelcomeMessage = true

                        navController.navigate(Routes.GENDER_SELECTION) {
                            popUpTo(Routes.START) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        showLastStepsWelcomeMessage = false

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.START) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Routes.TUTORIAL_LOADING) {
            TutorialLoadingScreen(
                onTutorialLoaded = {
                    navController.navigate(Routes.gamePlaceholder(0)) {
                        popUpTo(Routes.TUTORIAL_LOADING) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                initialUsername = pendingLoginUsername,
                initialPassword = pendingLoginPassword,
                onNavigateToRegister = {
                    navController.navigate(Routes.AGE_CHECK)
                },
                onNavigateToHome = { user ->
                    activeUserId = user.id

                    val isFreshRegisterLogin = pendingLoginPassword.isNotBlank()

                    pendingLoginUsername = ""
                    pendingLoginPassword = ""

                    if (user.player == null) {
                        showLastStepsWelcomeMessage = !isFreshRegisterLogin
                        showHomeWelcomeToast = true

                        navController.navigate(Routes.GENDER_SELECTION) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        showLastStepsWelcomeMessage = false
                        showHomeWelcomeToast = true

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                onExitApp = {
                    activity?.finish()
                }
            )
        }

        composable(Routes.AGE_CHECK) {
            AgeCheckScreen(
                onAgeApproved = { age ->
                    navController.navigate(Routes.registerWithAge(age))
                },
                onBackToLogin = {
                    navController.popBackStack(Routes.LOGIN, false)
                }
            )
        }

        composable(
            route = Routes.REGISTER_WITH_AGE,
            arguments = listOf(
                navArgument("age") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val age = backStackEntry.arguments?.getInt("age") ?: 13

            RegisterScreen(
                age = age,
                onBackToLogin = {
                    navController.popBackStack(Routes.LOGIN, false)
                },
                onBackToAgeCheck = {
                    navController.popBackStack()
                },
                onRegisterSuccess = { username, password ->
                    pendingLoginUsername = username
                    pendingLoginPassword = password

                    navigateToLoginKeepingPrefill()
                },
                onEmailAlreadyRegistered = { email ->
                    pendingLoginUsername = email
                    pendingLoginPassword = ""

                    navigateToLoginKeepingPrefill()
                }
            )
        }

        composable(Routes.GENDER_SELECTION) {
            val playerCreationViewModel: PlayerCreationViewModel =
                viewModel(
                    factory = PlayerCreationViewModelFactory(sessionManager)
                )

            val playerCreationUiState by playerCreationViewModel.uiState.collectAsState()

            GenderSelectionScreen(
                viewModel = playerCreationViewModel,
                showWelcomeBackMessage = showLastStepsWelcomeMessage,
                onExitApp = {
                    activity?.finish()
                },
                onPlayerCreated = {
                    showLastStepsWelcomeMessage = false

                    coroutineScope.launch {
                        sessionManager.markPlayerCreated(
                            gender = playerCreationUiState.gender
                        )
                    }

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.GENDER_SELECTION) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.HOME) {
            LaunchedEffect(showHomeWelcomeToast) {
                if (showHomeWelcomeToast) {
                    Toast.makeText(
                        context,
                        "¡Bienvenido a NightBite!",
                        Toast.LENGTH_SHORT
                    ).show()

                    showHomeWelcomeToast = false
                }
            }

            key(homeRefreshKey) {
                HomeScreen(
                    userId = activeUserId ?: userSession.userId,
                    userSession = userSession,
                    onNavigateToLevelIntro = { levelId ->
                        if (levelId == 0) {
                            navController.navigate(Routes.TUTORIAL_LOADING) {
                                popUpTo(Routes.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(Routes.levelIntro(levelId)) {
                                popUpTo(Routes.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateToPlayerCreation = {
                        navController.navigate(Routes.PLAYER_CREATION)
                    },
                    onNavigateToAchievements = {
                        navController.navigate(Routes.ACHIEVEMENTS)
                    },
                    onNavigateToSettings = {
                        navController.navigate(Routes.SETTINGS)
                    },
                    onLogout = {
                        activeUserId = null
                        pendingLoginUsername = ""
                        pendingLoginPassword = ""
                        showLastStepsWelcomeMessage = false

                        coroutineScope.launch {
                            sessionManager.clearSession()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onExitApp = {
                        activity?.finish()
                    }
                )
            }
        }

        composable(
            route = Routes.LEVEL_INTRO,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId")

            val selectedLevel = levelId?.let {
                NightLevelsData.getLevelById(it)
            }

            LevelIntroScreen(
                level = selectedLevel,
                onStartLevel = {
                    if (levelId != null) {
                        navController.navigate(
                            Routes.gamePlaceholder(levelId)
                        )
                    }
                },
                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.GAME_PLACEHOLDER,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0
            val currentUserId = activeUserId ?: userSession.userId

            fun savePlaceholderResult(
                resultType: GameResultType,
                stars: Int
            ) {
                val resultContent = GameResultsData.getResultContent(
                    levelId = levelId,
                    resultType = resultType,
                    stars = stars
                )

                val starsToSave = resultContent.safeStars

                NightProgressData.saveLevelStars(
                    userId = currentUserId,
                    levelId = levelId,
                    stars = starsToSave
                )

                val shouldUnlockNextLevel =
                    resultType.unlocksNextLevel &&
                            starsToSave == 3 &&
                            levelId < 4

                if (shouldUnlockNextLevel) {
                    NightProgressData.unlockNextLevel(
                        userId = currentUserId,
                        completedLevelId = levelId
                    )
                }
            }

            GamePlaceholderScreen(
                levelId = levelId,
                onNavigateToResult = { resultType, stars, runtimeResult ->
                    latestTutorialResult = runtimeResult
                    latestRuntimeResultLevelId = levelId

                    savePlaceholderResult(
                        resultType = resultType,
                        stars = stars
                    )

                    val shouldShowWantedPoster =
                        shouldShowWantedPosterTransition(
                            levelId = levelId,
                            resultType = resultType
                        )

                    if (shouldShowWantedPoster) {
                        navController.navigate(
                            Routes.wantedPosterTransition(
                                levelId = levelId,
                                resultType = resultType.name,
                                stars = stars
                            )
                        )
                    } else {
                        navController.navigate(
                            Routes.gameResult(
                                levelId = levelId,
                                resultType = resultType.name,
                                stars = stars
                            )
                        )
                    }
                },
                onRestartLevel = {
                    if (levelId == 0) {
                        navController.navigate(Routes.TUTORIAL_LOADING) {
                            popUpTo(Routes.gamePlaceholder(levelId)) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(Routes.levelIntro(levelId)) {
                            popUpTo(Routes.gamePlaceholder(levelId)) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                onBackToHome = {
                    navigateBackToHome()
                }
            )
        }

        composable(
            route = Routes.WANTED_POSTER_TRANSITION,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                },
                navArgument("resultType") {
                    type = NavType.StringType
                },
                navArgument("stars") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0

            val resultTypeName =
                backStackEntry.arguments?.getString("resultType")
                    ?: GameResultType.LEVEL_OUT_OF_LIVES.name

            val stars = backStackEntry.arguments
                ?.getInt("stars")
                ?.coerceIn(0, 3)
                ?: 0

            WantedPosterTransitionScreen(
                playerGender = userSession.playerGender,
                onTransitionFinished = {
                    navController.navigate(
                        Routes.gameResult(
                            levelId = levelId,
                            resultType = resultTypeName,
                            stars = stars
                        )
                    ) {
                        popUpTo(Routes.WANTED_POSTER_TRANSITION) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.GAME_RESULT,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                },
                navArgument("resultType") {
                    type = NavType.StringType
                },
                navArgument("stars") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0
            val resultTypeName = backStackEntry.arguments?.getString("resultType")

            val earnedStars = backStackEntry.arguments
                ?.getInt("stars")
                ?.coerceIn(0, 3)
                ?: 0

            val resultType = resultTypeName?.let { name ->
                runCatching {
                    GameResultType.valueOf(name)
                }.getOrNull()
            }

            if (resultType == null) {
                NightMessageDialog(
                    title = "Resultado no encontrado",
                    message = "No se pudo cargar el resultado de esta jornada.",
                    confirmText = "Volver al mapa",
                    dismissText = null,
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        navigateBackToHome()
                    },
                    onDismiss = {
                        navigateBackToHome()
                    }
                )
            } else {
                val resultContent = GameResultsData.getResultContent(
                    levelId = levelId,
                    resultType = resultType,
                    stars = earnedStars
                )

                val currentUserId = activeUserId ?: userSession.userId ?: 0L

                val starsToPersist = earnedStars.coerceIn(0, 3)

                val shouldUnlockNextLevel =
                    starsToPersist == 3 && levelId < 4

                val runtimeTutorialResult = latestTutorialResult
                    .takeIf {
                        latestRuntimeResultLevelId == levelId
                    }

                fun saveAndUnlockResultProgress() {
                    coroutineScope.launch {
                        val completedOrdersToSave =
                            runtimeTutorialResult?.completedOrders
                                ?: resultContent.completedOrders

                        val totalOrdersToSave =
                            runtimeTutorialResult?.totalOrders
                                ?: resultContent.totalOrders

                        val elapsedTimeToSave =
                            runtimeTutorialResult?.elapsedTimeSeconds
                                ?: 0f

                        val averageDeliveryTimeToSave =
                            runtimeTutorialResult?.let { tutorialResult ->
                                if (tutorialResult.completedOrders > 0) {
                                    tutorialResult.totalDeliveryTimeSeconds / tutorialResult.completedOrders
                                } else {
                                    0f
                                }
                            } ?: 0f

                        val scoreToSave =
                            runtimeTutorialResult?.score
                                ?: (starsToPersist * 100)

                        gameProgressRepository.saveLevelResult(
                            userId = currentUserId,
                            levelId = levelId,
                            stars = starsToPersist,
                            score = scoreToSave,
                            completedOrders = completedOrdersToSave,
                            totalOrders = totalOrdersToSave,
                            resultType = resultType.name,
                            elapsedTimeSeconds = elapsedTimeToSave,
                            averageDeliveryTimeSeconds = averageDeliveryTimeToSave
                        )

                        /*
                         * Temporal:
                         * mantenemos NightProgressData por ahora para que HomeScreen
                         * y AchievementsScreen sigan funcionando hasta conectarlos a Room.
                         */
                        NightProgressData.saveLevelStars(
                            userId = currentUserId,
                            levelId = levelId,
                            stars = starsToPersist
                        )

                        if (shouldUnlockNextLevel) {
                            NightProgressData.unlockNextLevel(
                                userId = currentUserId,
                                completedLevelId = levelId
                            )
                        }

                        if (currentUserId != 0L) {
                            progressSyncRepository.syncProgress(currentUserId)
                        }

                        homeRefreshKey += 1
                    }
                }

                LaunchedEffect(
                    currentUserId,
                    levelId,
                    resultType,
                    starsToPersist
                ) {
                    saveAndUnlockResultProgress()
                }

                GameResultScreen(
                    levelId = levelId,
                    resultType = resultType,
                    stars = earnedStars,
                    runtimeTimeText = runtimeTutorialResult?.elapsedTimeText,
                    runtimeCompletedOrders = runtimeTutorialResult?.completedOrders,
                    runtimeTotalOrders = runtimeTutorialResult?.totalOrders,
                    runtimeAverageDeliveryTimeText = runtimeTutorialResult?.averageDeliveryTimeText,
                    onRetryLevel = {
                        saveAndUnlockResultProgress()

                        latestTutorialResult = null
                        latestRuntimeResultLevelId = null

                        navController.navigate(Routes.levelIntro(levelId)) {
                            popUpTo(Routes.HOME) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onContinueToNextLevel = {
                        saveAndUnlockResultProgress()

                        latestTutorialResult = null
                        latestRuntimeResultLevelId = null

                        if (resultType == GameResultType.FINAL_WIN) {
                            navigateBackToHome()
                        } else if (shouldUnlockNextLevel) {
                            val nextLevelId = levelId + 1

                            navController.navigate(
                                Routes.levelIntro(nextLevelId)
                            ) {
                                popUpTo(Routes.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        } else {
                            navigateBackToHome()
                        }
                    },
                    onBackToHome = {
                        saveAndUnlockResultProgress()

                        latestTutorialResult = null

                        navigateBackToHome()
                    }
                )
            }
        }

        composable(Routes.ACHIEVEMENTS) {
            val currentUserId = activeUserId ?: userSession.userId ?: 0L

            val roomProgress by gameProgressRepository
                .observeProgress(currentUserId)
                .collectAsState(initial = null)

            val roomBadges by gameProgressRepository
                .observeBadges(currentUserId)
                .collectAsState(initial = emptyList())

            val earnedBadgeLevels = remember(roomBadges) {
                roomBadges.map { badge ->
                    badge.levelId
                }.toSet()
            }

            AchievementsScreen(
                userSession = userSession,
                currentLevel = (roomProgress?.maxUnlockedLevel ?: 0) + 1,
                earnedBadgeLevels = earnedBadgeLevels,
                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PLAYER_CREATION) {
            val playerCreationViewModel: PlayerCreationViewModel = viewModel(
                factory = PlayerCreationViewModelFactory(sessionManager)
            )

            PlayerCreationScreen(
                viewModel = playerCreationViewModel,
                onPlayerCreated = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.PLAYER_CREATION) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onBackToHome = {
                    navigateBackToHome()
                }
            )
        }

        composable(Routes.SETTINGS) {
            val accountCredentialsViewModel: AccountCredentialsViewModel = viewModel(
                factory = AccountCredentialsViewModelFactory(
                    sessionManager = sessionManager,
                    progressSyncRepository = progressSyncRepository
                )
            )

            val accountUiState by accountCredentialsViewModel.uiState.collectAsState()

            var showLogoutConfirmation by remember {
                mutableStateOf(false)
            }

            var isLeavingSession by remember {
                mutableStateOf(false)
            }

            if (!isLeavingSession) {
                SettingsScreen(
                    userSession = userSession,
                    onBackToHome = {
                        navController.popBackStack()
                    },
                    onNavigateToAccount = {
                        navController.navigate(Routes.ACCOUNT)
                    },
                    onNavigateToPassword = {
                        navController.navigate(Routes.PASSWORD)
                    },
                    onLogout = {
                        showLogoutConfirmation = true
                    },
                    onDeleteAccountClick = {
                        accountCredentialsViewModel.onDeleteAccountClick()
                    }
                )
            }

            if (showLogoutConfirmation) {
                NightMessageDialog(
                    title = "Cerrar sesión",
                    message = "¿Deseas cerrar tu sesión actual?",
                    confirmText = "Cerrar sesión",
                    dismissText = "Cancelar",
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        showLogoutConfirmation = false
                        isLeavingSession = true
                        activeUserId = null
                        pendingLoginUsername = ""
                        pendingLoginPassword = ""
                        showLastStepsWelcomeMessage = false

                        coroutineScope.launch {
                            sessionManager.clearSession()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onDismiss = {
                        showLogoutConfirmation = false
                    }
                )
            }

            if (accountUiState.showDeleteAccountDialog) {
                NightMessageDialog(
                    title = "Eliminar cuenta",
                    message = "Esta acción eliminará tu cuenta y tu ficha de repartidor. No podrás recuperar estos datos. ¿Deseas continuar?",
                    confirmText = "Eliminar",
                    dismissText = "Cancelar",
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        isLeavingSession = true
                        accountCredentialsViewModel.deleteAccount(userSession.userId)
                    },
                    onDismiss = {
                        accountCredentialsViewModel.dismissDeleteAccountDialog()
                    }
                )
            }

            if (accountUiState.showAccountDeletedDialog) {
                NightMessageDialog(
                    title = "Cuenta eliminada",
                    message = "Tu cuenta fue eliminada correctamente. Volverás al inicio de sesión.",
                    confirmText = "Aceptar",
                    dismissText = null,
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        accountCredentialsViewModel.finishAccountDeletedFlow {
                            activeUserId = null
                            pendingLoginUsername = ""
                            pendingLoginPassword = ""
                            showLastStepsWelcomeMessage = false

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onDismiss = null
                )
            }
        }

        composable(Routes.PASSWORD) {
            PasswordScreen(
                userId = activeUserId ?: userSession.userId,
                onBackToSettings = {
                    navController.popBackStack()
                },
                onPasswordUpdated = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                    coroutineScope.launch {
                        sessionManager.clearSession()
                    }
                }
            )
        }

        composable(Routes.ACCOUNT) {
            val accountCredentialsViewModel: AccountCredentialsViewModel = viewModel(
                factory = AccountCredentialsViewModelFactory(
                    sessionManager = sessionManager,
                    progressSyncRepository = progressSyncRepository
                )
            )

            AccountScreen(
                userSession = userSession,
                viewModel = accountCredentialsViewModel,
                onBackToSettings = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    activeUserId = null
                    pendingLoginUsername = ""
                    pendingLoginPassword = ""
                    showLastStepsWelcomeMessage = false

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                    homeRefreshKey += 1

                    activeUserId = userSession.userId
                    pendingLoginUsername = ""
                    pendingLoginPassword = ""
                    showLastStepsWelcomeMessage = false
                    showHomeWelcomeToast = false

                    navController.navigate(Routes.START) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun shouldShowWantedPosterTransition(
    levelId: Int,
    resultType: GameResultType
): Boolean {
    val isOutOfLivesResult =
        resultType == GameResultType.LEVEL_OUT_OF_LIVES ||
                resultType == GameResultType.FINAL_OUT_OF_LIVES

    val isAfterTutorial = levelId in 1..4

    return isAfterTutorial && isOutOfLivesResult
}

private const val INACTIVITY_LIMIT_MILLIS = 10 * 60 * 1000L