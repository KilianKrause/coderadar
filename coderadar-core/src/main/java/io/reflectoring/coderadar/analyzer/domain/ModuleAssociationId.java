package io.reflectoring.coderadar.analyzer.domain;

import java.io.Serializable;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity
public class ModuleAssociationId implements Serializable {

  private Long commitId;

  private Long fileId;

  private Long moduleId;

  @Override
  public int hashCode() {
    return 31 + commitId.hashCode() + fileId.hashCode() + moduleId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ModuleAssociationId)) {
      return false;
    }
    ModuleAssociationId that = (ModuleAssociationId) obj;
    return this.commitId.equals(that.commitId)
        && this.fileId.equals(that.fileId)
        && this.moduleId.equals(that.moduleId);
  }
}