package ni.edu.uam.nightbiteapi.controllers;

import jakarta.validation.Valid;
import ni.edu.uam.nightbiteapi.dto.ProgressResponse;
import ni.edu.uam.nightbiteapi.dto.ProgressSyncRequest;
import ni.edu.uam.nightbiteapi.services.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(
            ProgressService progressService
    ) {
        this.progressService = progressService;
    }

    @GetMapping("/account/{userAccountId}")
    public ResponseEntity<ProgressResponse> getProgressByUserAccountId(
            @PathVariable Long userAccountId
    ) {
        ProgressResponse response =
                progressService.getProgressByUserAccountId(userAccountId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/account/{userAccountId}/sync")
    public ResponseEntity<ProgressResponse> syncProgress(
            @PathVariable Long userAccountId,
            @Valid @RequestBody ProgressSyncRequest request
    ) {
        ProgressResponse response =
                progressService.syncProgress(
                        userAccountId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}