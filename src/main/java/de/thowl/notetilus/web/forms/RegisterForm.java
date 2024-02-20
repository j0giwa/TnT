package de.thowl.notetilus.web.forms;

import lombok.Data;

@Data
public class RegisterForm {

  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String password;
  private String password2;
}
