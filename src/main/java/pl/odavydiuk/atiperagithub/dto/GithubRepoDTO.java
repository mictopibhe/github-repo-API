package pl.odavydiuk.atiperagithub.dto;

public record GithubRepoDTO(
        String name,
        GithubRepoOwnerDTO owner,
        boolean fork
) {
}
