package io.reflectoring.coderadar.rest.unit.filepattern;

import io.reflectoring.coderadar.core.projectadministration.port.driver.filepattern.get.GetFilePatternResponse;
import io.reflectoring.coderadar.core.projectadministration.port.driver.filepattern.get.ListFilePatternsOfProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListFilePatternsOfProjectController {
  private final ListFilePatternsOfProjectUseCase listFilePatternsOfProjectUseCase;

  @Autowired
  public ListFilePatternsOfProjectController(
      ListFilePatternsOfProjectUseCase listFilePatternsOfProjectUseCase) {
    this.listFilePatternsOfProjectUseCase = listFilePatternsOfProjectUseCase;
  }

  @GetMapping(path = "/projects/{projectId}/filePatterns")
  public ResponseEntity<List<GetFilePatternResponse>> listFilePatterns(
      @PathVariable Long projectId) {
    return new ResponseEntity<>(
        listFilePatternsOfProjectUseCase.listFilePatterns(projectId), HttpStatus.OK);
  }
}
