package pl.odavydiuk.atiperagithub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.odavydiuk.atiperagithub.dto.ApiResponseDTO;
import pl.odavydiuk.atiperagithub.dto.BranchDTO;
import pl.odavydiuk.atiperagithub.dto.GithubRepoDTO;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubRepoService {

    private final GitHubClient github;

    public List<ApiResponseDTO> getRepositoriesList(String username) {
        GithubRepoDTO[] repos = github.getListRepositoriesForUser(username);
        return Arrays.stream(repos)
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    var branches = Arrays.stream(github.getRepositoryBranchesWithLastCommitSha(username, repo.name()))
                            .map(branch ->
                                    new BranchDTO(branch.name(), branch.commit().sha()))
                            .toList();
                    return new ApiResponseDTO(repo.name(), username, branches);
                }).toList();
    }
}
