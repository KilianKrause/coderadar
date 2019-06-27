package io.reflectoring.coderadar.projectadministration.port.driver.user.password;

import io.reflectoring.coderadar.projectadministration.port.driver.user.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordCommand {
  @NotBlank private String refreshToken;

  @NotBlank
  @Length(min = 8, max = 64)
  @ValidPassword
  private String newPassword;
}
