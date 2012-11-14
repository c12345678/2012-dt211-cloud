// vim: set ts=2 sw=2 expandtab:

/*
 * Credits: Rewokring of TwitBase code from HBase In Action
 *
 * https://github.com/hbaseinaction/twitbase
 *
 */

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
