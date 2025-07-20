package pl.odavydiuk.atiperagithub;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.odavydiuk.atiperagithub.dto.ApiResponseDTO;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8089)
public class GitHubRepoIntegrationTest {
    @LocalServerPort
    private int port;

    @Value("${github.access-token}")
    private String accessToken;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    void happyPath_shouldReturnNonForkRepositoriesWithBranches() {
        // Stubbing GitHub repos for testUser (1 fork, 1 non-fork)
        stubFor(
                get(
                        urlEqualTo("/users/testUser/repos"))
                        .withHeader("Authorization", equalTo("Bearer " + accessToken))
                        .willReturn(
                                okJson("""
                                        [
                                            {
                                            "name": "Non-fork Repo",
                                            "owner": {
                                              "login": "testUser"
                                            },
                                            "fork": false
                                            },
                                            {
                                            "name": "Fork Repo",
                                            "owner": {
                                              "login": "testUser"
                                            },
                                            "fork": true
                                            }
                                        ]
                                        """
                                )
                        )
        );

        // Stubbing branches for non-fork repo
        stubFor(
                get(
                        urlEqualTo("/repos/testUser/Non-fork%20Repo/branches"))
                        .withHeader("Authorization", equalTo("Bearer " + accessToken))
                        .willReturn(
                                okJson("""
                                        [
                                          {
                                          "name": "main",
                                          "commit": {
                                            "sha": "1z2x3c4v5b6n7m890"
                                            }
                                          },
                                          {
                                            "name": "develop",
                                            "commit": {
                                              "sha": "def456fsd56gfs5d"
                                              }
                                          }
                                        ]
                                        """
                                )
                        )
        );

        // WHEN
        String url = "http://localhost:" + port + "/api/github/repositories/testUser";

        ResponseEntity<List<ApiResponseDTO>> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ApiResponseDTO> repos = response.getBody();
        assertThat(repos).isNotNull();
        Assertions.assertNotNull(repos);
        assertThat(repos.size()).isEqualTo(1);

        var repo = repos.getFirst();
        assertThat(repo).isNotNull();
        assertThat(repo.repositoryName()).isEqualTo("Non-fork Repo");
        assertThat(repo.ownerLogin()).isEqualTo("testUser");
        assertThat(repo.branches().size()).isEqualTo(2);
        assertThat(repo.branches().get(0).name()).isEqualTo("main");
        assertThat(repo.branches().get(0).lastCommitSha()).isEqualTo("1z2x3c4v5b6n7m890");
        assertThat(repo.branches().get(1).name()).isEqualTo("develop");
        assertThat(repo.branches().get(1).lastCommitSha()).isEqualTo("def456fsd56gfs5d");
    }
}
