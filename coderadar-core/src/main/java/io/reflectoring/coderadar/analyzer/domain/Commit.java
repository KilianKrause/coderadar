package io.reflectoring.coderadar.analyzer.domain;

import io.reflectoring.coderadar.projectadministration.domain.Project;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/** Metadata about a commit to a Git repository. */
@NodeEntity
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
  private String firstParent;

  @Relationship(direction = Relationship.INCOMING, type = "HAS")
  private Project project;

  @Relationship(value = "HAS_CHANGED")
  private List<CommitToFileAssociation> touchedFiles;

  public void setComment(String comment) {
    if (comment.length() > 255) {
      // truncating commit message if too long for database
      this.comment = comment.substring(0, 252) + "...";
    } else {
      this.comment = comment;
    }
  }
}