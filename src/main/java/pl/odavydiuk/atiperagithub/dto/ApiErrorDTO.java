package pl.odavydiuk.atiperagithub.dto;

public record ApiErrorDTO(
        int status,
        String message
) {
}
