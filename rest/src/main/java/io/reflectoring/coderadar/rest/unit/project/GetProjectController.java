package io.reflectoring.coderadar.rest.unit.project;

import io.reflectoring.coderadar.core.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.core.projectadministration.port.driver.project.get.GetProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetProjectController {
  private final GetProjectUseCase getProjectUseCase;

  @Autowired
  public GetProjectController(GetProjectUseCase getProjectUseCase) {
    this.getProjectUseCase = getProjectUseCase;
  }

  @GetMapping(path = "/projects/{projectId}")
  public ResponseEntity getProject(@PathVariable Long projectId) {
    try {
      return new ResponseEntity<>(getProjectUseCase.get(projectId), HttpStatus.OK);
    } catch (ProjectNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}