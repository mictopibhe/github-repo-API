# github-repo-API
A RESTful service built with **Java 21** and **Spring Boot 3.5**, designed to fetch all **non-forked public repositories** for a given GitHub user and return each repository’s branches with their latest commit SHA. This project is developed under explicit constraints for a recruitment task.

## Features

- Returns public **non-fork** repositories for a given GitHub username
- Includes **branch list** and **last commit SHA** per branch
- Returns `404` with a custom message if the user does not exist
- Integrates with GitHub's REST API v3
- Includes **integration test** without mocks (using WireMock)

---

## API Contract

### Success Response

```json
[
  {
    "repositoryName": "example-repo",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "d6fde92930d4715a2b49857d24b940956b26d2d3"
      },
      {
        "name": "dev",
        "lastCommitSha": "e37dfe838fd364e37b837e4722a5be4c12345678"
      }
    ]
  },
  ...
]
```

### Error Response

For a user that does not exist:

```json
{
  "status": 404,
  "message": "User with login username not found!"
}
```

---

## Setup & Run Instructions

Generate a GitHub Personal Access Token at
https://github.com/settings/personal-access-tokens

### Option 1: Run using `application.yml`
1. Set your token in application.yml or as an environment variable:
```yaml
github:
  access-token: ghp_yourActualTokenHere
```
2. Run the app with Maven:
```bash
./mvnw spring-boot:run
```
3. Call the API 
```bash
curl http://localhost:8080/api/github/repositories/octocat
```
### Option 2: Run with environment variables

```bash
export GITHUB_ACCESS_TOKEN=ghp_your_token_here

./mvnw spring-boot:run
```

---

## Running Tests

This project includes an integration test (no mocks), using WireMock.

To run tests:
```bash
./mvnw test
```

---

## GitHub API Reference
This project uses [GitHub's REST API v3](https://developer.github.com/v3)

---

## Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Web (RestTemplate)
- WireMock (for integration testing)
- JUnit 5
