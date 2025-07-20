package pl.odavydiuk.atiperagithub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.odavydiuk.atiperagithub.dto.ApiResponseDTO;
import pl.odavydiuk.atiperagithub.service.GithubRepoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github/repositories")
public class GithubRepoController {
    private final GithubRepoService service;

    @GetMapping("/{username}")
    public ResponseEntity<List<ApiResponseDTO>> getRepositoriesList(@PathVariable String username) {
        return ResponseEntity.ok(service.getRepositoriesList(username));
    }
}
