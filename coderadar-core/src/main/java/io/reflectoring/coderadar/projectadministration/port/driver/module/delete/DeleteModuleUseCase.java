package io.reflectoring.coderadar.projectadministration.port.driver.module.delete;

public interface DeleteModuleUseCase {

  /**
   * Deletes a module given its id.
   *
   * @param moduleId The id of the module
   * @param projectId The id of the project
   */
  void delete(long moduleId, long projectId);
}
