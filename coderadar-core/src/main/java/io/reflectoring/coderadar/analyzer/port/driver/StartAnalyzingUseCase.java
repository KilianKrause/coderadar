package io.reflectoring.coderadar.analyzer.port.driver;

public interface StartAnalyzingUseCase {

  /**
   * Starts the analysis of a project.
   *
   * @param projectId The id of the project to analyze.
   * @param branchName The branch to analyze.
   */
  void start(long projectId, String branchName);
}
