package pl.odavydiuk.atiperagithub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.odavydiuk.atiperagithub.dto.GithubBranchDTO;
import pl.odavydiuk.atiperagithub.dto.GithubRepoDTO;
import pl.odavydiuk.atiperagithub.exception.ResourseNotFoundException;

@Service
@RequiredArgsConstructor
public class GitHubClient {
    private final RestTemplate restTemplate;
    @Value("${github.access-token}")
    private String token;
    @Value("${github.base-url}")
    private String githubBaseUrl;

    public GithubRepoDTO[] getListRepositoriesForUser(String username) {
        String url = String.format("%s/users/%s/repos", githubBaseUrl, username);

        var httpEntity = getConfiguredEntity();

        try {
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, GithubRepoDTO[].class).getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourseNotFoundException(String.format("User with login %s not found!", username));
        }
    }

    public GithubBranchDTO[] getRepositoryBranchesWithLastCommitSha(String username, String repo) {
        String url = String.format("%s/repos/%s/%s/branches", githubBaseUrl, username, repo);

        var httpEntity = getConfiguredEntity();

        try {
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, GithubBranchDTO[].class).getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourseNotFoundException(
                    String.format("Repository %s for user with login %s not found", repo, username)
            );
        }
    }

    private HttpEntity<String> getConfiguredEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/vnd.github+json");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add("X-GitHub-Api-Version", "2022-11-28");

        return new HttpEntity<>(headers);
    }
}
