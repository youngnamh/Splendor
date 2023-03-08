package project.view.lobby.communication;

/**
 * User class used to store user info locally.
 */
public class User {

  private final String username;
  private final String refreshToken;
  private final String authority;
  private String accessToken;

  /**
   * Constructor of a user instance.
   *
   * @param pusername    username
   * @param paccessToken user access token
   * @param pauthority   user authority
   */
  public User(String pusername, String paccessToken, String refreshToken, String pauthority) {
    username = pusername;
    accessToken = paccessToken;
    this.refreshToken = refreshToken;
    authority = pauthority;
  }

  public String getUsername() {
    return username;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String newAccessToken) {
    accessToken = newAccessToken;
  }

  public String getAuthority() {
    return authority;
  }

  public String getRefreshToken() {
    return refreshToken;
  }


}
