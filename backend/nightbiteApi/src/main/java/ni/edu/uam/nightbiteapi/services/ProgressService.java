package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.dto.BadgeSyncRequest;
import ni.edu.uam.nightbiteapi.dto.LevelResultProgressResponse;
import ni.edu.uam.nightbiteapi.dto.LevelResultSyncRequest;
import ni.edu.uam.nightbiteapi.dto.ProgressResponse;
import ni.edu.uam.nightbiteapi.dto.ProgressSyncRequest;
import ni.edu.uam.nightbiteapi.dto.UserBadgeResponse;
import ni.edu.uam.nightbiteapi.model.LevelResult;
import ni.edu.uam.nightbiteapi.model.PlayerProgress;
import ni.edu.uam.nightbiteapi.model.UserAccount;
import ni.edu.uam.nightbiteapi.model.UserBadge;
import ni.edu.uam.nightbiteapi.repositories.LevelResultRepository;
import ni.edu.uam.nightbiteapi.repositories.PlayerProgressRepository;
import ni.edu.uam.nightbiteapi.repositories.UserAccountRepository;
import ni.edu.uam.nightbiteapi.repositories.UserBadgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressService {

    private final UserAccountRepository userAccountRepository;
    private final PlayerProgressRepository playerProgressRepository;
    private final LevelResultRepository levelResultRepository;
    private final UserBadgeRepository userBadgeRepository;

    public ProgressService(
            UserAccountRepository userAccountRepository,
            PlayerProgressRepository playerProgressRepository,
            LevelResultRepository levelResultRepository,
            UserBadgeRepository userBadgeRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.playerProgressRepository = playerProgressRepository;
        this.levelResultRepository = levelResultRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    public ProgressResponse getProgressByUserAccountId(Long userAccountId) {
        UserAccount userAccount = getUserAccountOrThrow(userAccountId);

        PlayerProgress progress = getOrCreateProgress(userAccount);

        return buildProgressResponse(
                userAccount,
                progress
        );
    }

    public ProgressResponse syncProgress(
            Long userAccountId,
            ProgressSyncRequest request
    ) {
        if (request == null) {
            throw new RuntimeException("El progreso es obligatorio");
        }

        UserAccount userAccount = getUserAccountOrThrow(userAccountId);

        PlayerProgress progress = getOrCreateProgress(userAccount);

        int requestMaxUnlockedLevel = safeLevel(request.getMaxUnlockedLevel());

        int maxUnlockedLevel = Math.max(
                progress.getMaxUnlockedLevel(),
                requestMaxUnlockedLevel
        );

        progress.setMaxUnlockedLevel(maxUnlockedLevel);
        playerProgressRepository.save(progress);

        if (request.getLevelResults() != null) {
            for (LevelResultSyncRequest levelResultRequest : request.getLevelResults()) {
                saveLevelResult(
                        userAccount,
                        levelResultRequest
                );
            }
        }

        if (request.getBadges() != null) {
            for (BadgeSyncRequest badgeRequest : request.getBadges()) {
                saveBadge(
                        userAccount,
                        badgeRequest.getLevelId(),
                        badgeRequest.getBadgeCode()
                );
            }
        }

        PlayerProgress updatedProgress = getOrCreateProgress(userAccount);

        return buildProgressResponse(
                userAccount,
                updatedProgress
        );
    }

    private void saveLevelResult(
            UserAccount userAccount,
            LevelResultSyncRequest request
    ) {
        if (request == null) {
            return;
        }

        int levelId = safeLevel(request.getLevelId());
        int stars = safeStars(request.getBestStars());
        int score = safeNonNegative(request.getBestScore());
        int completedOrders = safeNonNegative(request.getCompletedOrders());
        int totalOrders = safeNonNegative(request.getTotalOrders());
        float elapsedTimeSeconds = safeNonNegativeFloat(request.getElapsedTimeSeconds());
        float averageDeliveryTimeSeconds = safeNonNegativeFloat(request.getAverageDeliveryTimeSeconds());

        LevelResult levelResult = levelResultRepository
                .findByUserAccountIdAndLevelId(
                        userAccount.getId(),
                        levelId
                )
                .orElseGet(() -> {
                    LevelResult newResult = new LevelResult();
                    newResult.setUserAccount(userAccount);
                    newResult.setLevelId(levelId);
                    return newResult;
                });

        boolean isBetterResult = isBetterResult(
                levelResult,
                stars,
                score
        );

        if (isBetterResult) {
            levelResult.setBestStars(stars);
            levelResult.setBestScore(score);
            levelResult.setCompletedOrders(completedOrders);
            levelResult.setTotalOrders(totalOrders);
            levelResult.setResultType(safeResultType(request.getResultType()));
            levelResult.setElapsedTimeSeconds(elapsedTimeSeconds);
            levelResult.setAverageDeliveryTimeSeconds(averageDeliveryTimeSeconds);
        }

        levelResultRepository.save(levelResult);

        if (stars == REQUIRED_STARS_FOR_UNLOCK) {
            unlockNextLevelIfNeeded(
                    userAccount,
                    levelId
            );

            saveBadge(
                    userAccount,
                    levelId,
                    badgeCodeForLevel(levelId)
            );
        }
    }

    private boolean isBetterResult(
            LevelResult currentResult,
            int newStars,
            int newScore
    ) {
        int currentStars = currentResult.getBestStars() == null
                ? 0
                : currentResult.getBestStars();

        int currentScore = currentResult.getBestScore() == null
                ? 0
                : currentResult.getBestScore();

        if (newStars > currentStars) {
            return true;
        }

        return newStars == currentStars && newScore > currentScore;
    }

    private void unlockNextLevelIfNeeded(
            UserAccount userAccount,
            int completedLevelId
    ) {
        PlayerProgress progress = getOrCreateProgress(userAccount);

        int currentMaxUnlockedLevel = progress.getMaxUnlockedLevel() == null
                ? 0
                : progress.getMaxUnlockedLevel();

        int nextLevel = Math.min(
                completedLevelId + 1,
                MAX_LEVEL_ID
        );

        int newMaxUnlockedLevel = Math.max(
                currentMaxUnlockedLevel,
                nextLevel
        );

        progress.setMaxUnlockedLevel(newMaxUnlockedLevel);
        playerProgressRepository.save(progress);
    }

    private void saveBadge(
            UserAccount userAccount,
            Integer levelIdValue,
            String badgeCodeValue
    ) {
        int levelId = safeLevel(levelIdValue);

        boolean badgeAlreadyExists = userBadgeRepository
                .existsByUserAccountIdAndLevelId(
                        userAccount.getId(),
                        levelId
                );

        if (badgeAlreadyExists) {
            return;
        }

        UserBadge badge = new UserBadge();
        badge.setUserAccount(userAccount);
        badge.setLevelId(levelId);
        badge.setBadgeCode(
                safeBadgeCode(
                        badgeCodeValue,
                        levelId
                )
        );

        userBadgeRepository.save(badge);
    }

    private ProgressResponse buildProgressResponse(
            UserAccount userAccount,
            PlayerProgress progress
    ) {
        List<LevelResultProgressResponse> levelResults =
                levelResultRepository
                        .findByUserAccountIdOrderByLevelIdAsc(userAccount.getId())
                        .stream()
                        .map(this::mapLevelResultToResponse)
                        .toList();

        List<UserBadgeResponse> badges =
                userBadgeRepository
                        .findByUserAccountIdOrderByLevelIdAsc(userAccount.getId())
                        .stream()
                        .map(this::mapBadgeToResponse)
                        .toList();

        return new ProgressResponse(
                userAccount.getId(),
                progress.getMaxUnlockedLevel(),
                progress.getUpdatedAt(),
                levelResults,
                badges
        );
    }

    private LevelResultProgressResponse mapLevelResultToResponse(
            LevelResult result
    ) {
        return new LevelResultProgressResponse(
                result.getLevelId(),
                result.getBestStars(),
                result.getBestScore(),
                result.getCompletedOrders(),
                result.getTotalOrders(),
                result.getResultType(),
                result.getElapsedTimeSeconds(),
                result.getAverageDeliveryTimeSeconds(),
                result.getUpdatedAt()
        );
    }

    private UserBadgeResponse mapBadgeToResponse(
            UserBadge badge
    ) {
        return new UserBadgeResponse(
                badge.getLevelId(),
                badge.getBadgeCode(),
                badge.getUnlockedAt()
        );
    }

    private UserAccount getUserAccountOrThrow(Long userAccountId) {
        if (userAccountId == null) {
            throw new RuntimeException("El id de la cuenta de usuario es obligatorio");
        }

        return userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));
    }

    private PlayerProgress getOrCreateProgress(UserAccount userAccount) {
        return playerProgressRepository
                .findByUserAccountId(userAccount.getId())
                .orElseGet(() -> {
                    PlayerProgress progress = new PlayerProgress();
                    progress.setUserAccount(userAccount);
                    progress.setMaxUnlockedLevel(0);
                    return playerProgressRepository.save(progress);
                });
    }

    private int safeLevel(Integer levelId) {
        if (levelId == null) {
            return 0;
        }

        return Math.max(
                0,
                Math.min(levelId, MAX_LEVEL_ID)
        );
    }

    private int safeStars(Integer stars) {
        if (stars == null) {
            return 0;
        }

        return Math.max(
                0,
                Math.min(stars, REQUIRED_STARS_FOR_UNLOCK)
        );
    }

    private int safeNonNegative(Integer value) {
        if (value == null) {
            return 0;
        }

        return Math.max(
                0,
                value
        );
    }

    private float safeNonNegativeFloat(Float value) {
        if (value == null) {
            return 0f;
        }

        return Math.max(
                0f,
                value
        );
    }

    private String safeResultType(String resultType) {
        if (resultType == null || resultType.isBlank()) {
            return "UNKNOWN_RESULT";
        }

        return resultType.trim();
    }

    private String safeBadgeCode(
            String badgeCode,
            int levelId
    ) {
        if (badgeCode == null || badgeCode.isBlank()) {
            return badgeCodeForLevel(levelId);
        }

        return badgeCode.trim();
    }

    private String badgeCodeForLevel(int levelId) {
        return switch (levelId) {
            case 0 -> "tutorial_badge";
            case 1 -> "shadow_wanderer_badge";
            case 2 -> "spectral_dogs_badge";
            case 3 -> "deformed_clients_badge";
            case 4 -> "lost_delivery_badge";
            default -> "unknown_badge";
        };
    }

    private static final int MAX_LEVEL_ID = 4;
    private static final int REQUIRED_STARS_FOR_UNLOCK = 3;
}