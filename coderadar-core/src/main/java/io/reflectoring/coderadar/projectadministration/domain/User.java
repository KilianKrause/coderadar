package io.reflectoring.coderadar.projectadministration.domain;

import java.util.List;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/** a user of application, who has to login to access to functionality */
@NodeEntity
@Data
public class User {
  private Long id;
  private String username;
  private String password;

  @Relationship("HAS")
  private List<RefreshToken> refreshTokens;
}