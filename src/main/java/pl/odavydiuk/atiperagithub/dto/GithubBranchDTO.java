package pl.odavydiuk.atiperagithub.dto;

public record GithubBranchDTO(
        String name,
        GithubCommitDTO commit
) {
}
