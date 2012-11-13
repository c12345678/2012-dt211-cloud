// vim: set ts=2 sw=2 expandtab:

public abstract class UserModel {

  public String user;
  public String name;
  public String email;
  public String password;

  @Override
  public String toString() {
    return String.format( "<User: %s, %s, %s>", user, name, email);
  }
}
