package io.reflectoring.coderadar.rest.integration.user;

import io.reflectoring.coderadar.core.projectadministration.port.driver.user.login.LoginUserCommand;
import io.reflectoring.coderadar.rest.integration.ControllerTestTemplate;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class LoginUserControllerIntegrationTest extends ControllerTestTemplate {

  @Test
  void loginUserSuccessfully() throws Exception {
    LoginUserCommand command = new LoginUserCommand("username", "password");
    mvc().perform(post("/auth").content(toJson(command)));
  }
}
