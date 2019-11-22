package io.reflectoring.coderadar.projectadministration.domain;

import io.reflectoring.coderadar.analyzer.domain.MetricValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/** Metadata about a commit to a Git repository. */
@Data
public class Commit {
  private Long id;
  private String name;
  private Date timestamp;
  private String comment;
  private String author;
  private boolean merged = false;
  private boolean analyzed = false;

  @ToString.Exclude private List<Commit> parents = new ArrayList<>();

  @ToString.Exclude private List<FileToCommitRelationship> touchedFiles = new ArrayList<>();

  private List<MetricValue> metricValues = new ArrayList<>();
}
