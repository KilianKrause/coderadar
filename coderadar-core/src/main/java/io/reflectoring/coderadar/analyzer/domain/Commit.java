package io.reflectoring.coderadar.analyzer.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/** Metadata about a commit to a Git repository. */
@Data
public class Commit {
  private Long id;
  private String name;
  private Date timestamp; // TODO: A date converter should be used here.
  private String comment;
  private String author;
  private boolean merged = false;
  private boolean analyzed = false;
  private Integer sequenceNumber;

  private List<Commit> parents = new ArrayList<>();

  private List<FileToCommitRelationship> touchedFiles = new LinkedList<>();

  private List<MetricValue> metricValues = new LinkedList<>();

  public void setComment(String comment) {
    if (comment.length() > 255) {
      // truncating commit message if too long for database
      this.comment = comment.substring(0, 252) + "...";
    } else {
      this.comment = comment;
    }
  }
}
