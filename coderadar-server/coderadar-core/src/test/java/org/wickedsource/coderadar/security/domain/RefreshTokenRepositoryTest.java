package org.wickedsource.coderadar.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.coderadar.factories.databases.DbUnitFactory;
import org.wickedsource.coderadar.projectadministration.domain.User;
import org.wickedsource.coderadar.testframework.template.IntegrationTestTemplate;

@Transactional
public class RefreshTokenRepositoryTest extends IntegrationTestTemplate {

  @Autowired private RefreshTokenRepository repository;

  @Test
  @DatabaseSetup(DbUnitFactory.RefreshTokens.REFRESH_TOKENS)
  public void load() throws Exception {
    Optional<RefreshToken> refreshToken = repository.findById(100L);
    assertThat(refreshToken.isPresent()).isTrue();
    User user = refreshToken.get().getUser();
    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo("radar");
  }

  @Test
  @DatabaseSetup(DbUnitFactory.RefreshTokens.REFRESH_STATIC_TOKENS)
  public void findByToken() throws Exception {
    RefreshToken refreshToken =
        repository.findByToken(
            "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlcmFkYXIiLCJleHAiOjE0ODQ1MTUzOTUsInR5cGUiOiJSRUZSRVNIIiwiaWF0IjoxNDg0NTE1NDU1LCJ1c2VySWQiOiIxIiwidXNlcm5hbWUiOiJyYWRhciJ9.zfkyc5jkPiAUEt7nU25SJxKprcPiXaiq0Q6bCJ_RrQo");
    assertThat(refreshToken).isNotNull();
  }

  @Test
  @DatabaseSetup(DbUnitFactory.RefreshTokens.REFRESH_STATIC_TOKENS)
  public void deleteByUser() throws Exception {
    User user = new User();
    user.setId(2L);
    int count = repository.deleteByUser(user);
    assertThat(count).isEqualTo(1);
  }
}
