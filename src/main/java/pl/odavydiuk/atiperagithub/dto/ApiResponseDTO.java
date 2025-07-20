package pl.odavydiuk.atiperagithub.dto;

import java.util.List;

public record ApiResponseDTO(
        String repositoryName,
        String ownerLogin,
        List<BranchDTO> branches
) {
}
