package io.github.e66e.git_repo_inspector;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class InspectorController {

    private final InspectorService inspectorService;

    @GetMapping("/{username}")
    public ResponseEntity<List<Repository>> getReposByUsername(@PathVariable String username) {
        List<Repository> allNonForkRepos = this.inspectorService.getAllNonForkRepos(username);

        return ResponseEntity.ok(allNonForkRepos);
    }
}
