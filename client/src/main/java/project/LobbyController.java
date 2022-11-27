package project;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.codec.digest.DigestUtils;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.GameParameters;
import project.view.lobby.Session;
import project.view.lobby.SessionGui;
import project.view.lobby.SessionList;
import project.view.lobby.SessionGuiManager;
import project.view.lobby.User;

/**
 * login GUI controller.
 */
public class LobbyController {

  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private ScrollPane allSessionScrollPane;
  private List<Thread> updateThreadPool;

  @FXML
  protected void onCreateSessionButtonClick() throws UnirestException {
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User curUser = App.getUser();
    Map<String, String> gameNameMapping = lobbyRequestSender.getGameNameMapping();

    // Get the current game display name in the choice box
    String displayGameName = gameChoices.getValue();
    String gameName = gameNameMapping.get(displayGameName);
    String accessToken = curUser.getAccessToken();
    String creator = curUser.getUsername();
    lobbyRequestSender.sendCreateSessionRequest(creator, accessToken, gameName, "");
  }


  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
    // clean up local lobby session cache before logging out
    App.setUser(null);
    stopAndClearThreads();
    App.setRoot("start_page");
  }


  //private String formatSessionInfo(Session session) {
  //  List<String> curPlayers = session.getPlayers();
  //  String curPlayerStr = curPlayers.toString();
  //  String creatorName = session.getCreator();
  //  int curPlayersCount = curPlayers.size();
  //  int maxPlayerCount = session.getGameParameters().getMaxSessionPlayers();
  //  String displayGameName = session.getGameParameters().getDisplayName();
  //  return String.format(
  //      "%s, [%d/%d] players %s: \n",
  //      displayGameName,
  //      curPlayersCount,
  //      maxPlayerCount,
  //      curPlayerStr)
  //      + String.format("creator: %s", creatorName);
  //}


  private EventHandler<ActionEvent> createDeleteSessionHandler(
      VBox sessionVbox, Long sessionId,
      LobbyServiceRequestSender lobbyRequestSender,
      String accessToken) {
    return event -> {
      for (Node n : sessionVbox.getChildren()) {
        String paneSessionId = n.getAccessibleText();
        String sessionIdStr = sessionId.toString();
        if (paneSessionId != null && paneSessionId.equals(sessionIdStr)) {
          // TODO: If the onAction method involves GUI changes, defer this change by using
          //  Platform.runLater(() -> { methods_you_want_to_call })
          Platform.runLater(() -> {
            sessionVbox.getChildren().remove(n);
          });
          try {
            lobbyRequestSender.sendDeleteSessionRequest(accessToken, sessionId);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
        }
      }
    };
  }

  //private EventHandler<ActionEvent> createJoinLeaveSessionHandler(
  //    String curUserName, Long sessionId,
  //    LobbyServiceRequestSender lobbyRequestSender, String accessToken) {
  //  return event -> {
  //    Button joinAndLeaveButton = (Button) event.getSource();
  //    // If the button says "Join", send join request
  //    if (joinAndLeaveButton.getText().equals("Join")) {
  //      // First thing, a request
  //      joinAndLeaveButton.setText("Leave");
  //      try {
  //        lobbyRequestSender.sendAddPlayerRequest(accessToken, sessionId, curUserName);
  //      } catch (UnirestException e) {
  //        throw new RuntimeException(e);
  //      }
  //    } else if (joinAndLeaveButton.getText().equals("Leave")) { // otherwise, send leave request
  //      joinAndLeaveButton.setText("Join");
  //      try {
  //        lobbyRequestSender.sendRemovePlayerRequest(accessToken, sessionId, curUserName);
  //      } catch (UnirestException e) {
  //        throw new RuntimeException(e);
  //      }
  //    }
  //  };
  //}

  //// TODO: For server side, they need to handle
  ////  this POST request and send a PUT request to GameServer
  //private EventHandler<ActionEvent> createLaunchSessionHandler(
  //    Long sessionId, LobbyServiceRequestSender lobbyRequestSender, String accessToken) {
  //  return event -> {
  //    try {
  //      lobbyRequestSender.sendLaunchSessionRequest(sessionId, accessToken);
  //    } catch (UnirestException e) {
  //      throw new RuntimeException(e);
  //    }
  //  };
  //}


  //private EventHandler<ActionEvent> createPlayGameHandler() {
  //  return event -> {
  //    try {
  //      // TODO: Adjust this fxml file later when game server is done
  //      App.setRoot("splendor_base_game_board");
  //      // anything involving reloading this page, we need to clear the thread pool
  //      stopAndClearThreads();
  //    } catch (IOException e) {
  //      throw new RuntimeException(e);
  //    }
  //  };
  //}

  //
  //private EventHandler<ActionEvent> createWatchGameHandler() {
  //  return event -> {
  //    // just load the board to this user, nothing else should be done
  //    try {
  //      App.setRoot("splendor_base_game_board");
  //      stopAndClearThreads();
  //    } catch (IOException e) {
  //      throw new RuntimeException(e);
  //    }
  //  };
  //}

  @FXML
  public void joinGameDev() throws IOException {
    // TODO: For debug usage
    App.setRoot("splendor_base_game_board");
  }

  ///**
  // * create a GUI representation of session object.
  // *
  // * @param accessToken        access token
  // * @param sessionId          session id
  // * @param sessionInfoContent session info in string
  // * @return a Pane that contains info about a session
  // */
  //private Pane createSessionGui(String accessToken,
  //                              Long sessionId,
  //                              Session curSession,
  //                              Label sessionInfoContent) {
  //  LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
  //
  //  // give different buttons depending on username and creator name
  //  HBox hb;
  //  String sessionCreatorName;
  //  sessionCreatorName = curSession.getCreator();
  //  // Can be changed for better visualization
  //  Region spaceBetween = new Region();
  //  spaceBetween.setPrefWidth(200);
  //  spaceBetween.setPrefHeight(30);
  //  //
  //  User user = App.getUser();
  //  String curUserName = user.getUsername();
  //  List<String> curSessionPlayers = curSession.getPlayers();
  //
  //  // if the session is launched, add a Play Button for both creator and player
  //  if (curSession.isLaunched()) {
  //    // if the current user is not in the session, then give a Watch Button
  //    if (!curSessionPlayers.contains(curUserName)) {
  //      EventHandler<ActionEvent> watchGameHandler = createWatchGameHandler();
  //      Button watchButton = new Button("Watch");
  //      watchButton.setOnAction(watchGameHandler);
  //      hb = new HBox(sessionInfoContent, spaceBetween, watchButton);
  //    } else {
  //      EventHandler<ActionEvent> playGameHandler = createPlayGameHandler();
  //      Button playButton = new Button("Play");
  //      playButton.setOnAction(playGameHandler);
  //      hb = new HBox(sessionInfoContent, spaceBetween, playButton);
  //    }
  //  } else {
  //    // otherwise, add diff buttons for creator OR player
  //    // if the user is the creator, provides delete and launch button
  //    if (curUserName.equals(sessionCreatorName)) {
  //      EventHandler<ActionEvent> deleteSessionHandler =
  //          createDeleteSessionHandler(sessionVbox, sessionId, lobbyRequestSender, accessToken);
  //      EventHandler<ActionEvent> launchSessionHandler =
  //          createLaunchSessionHandler(sessionId, lobbyRequestSender, accessToken);
  //      Button deleteButton = new Button("Delete");
  //      Button launchButton = new Button("Launch");
  //      deleteButton.setOnAction(deleteSessionHandler);
  //      launchButton.setOnAction(launchSessionHandler);
  //      // launchButton greyed out
  //      launchButton.setDisable(true);
  //      hb = new HBox(sessionInfoContent, spaceBetween, deleteButton, launchButton);
  //    } else {
  //      EventHandler<ActionEvent> joinAndLeaveSessionHandler =
  //          createJoinLeaveSessionHandler(curUserName, sessionId, lobbyRequestSender, accessToken);
  //      String buttonContent;
  //      if (curSessionPlayers.contains(curUserName)) {
  //        buttonContent = "Leave";
  //      } else {
  //        buttonContent = "Join";
  //      }
  //      Button joinAndLeaveButton = new Button(buttonContent);
  //      joinAndLeaveButton.setOnAction(joinAndLeaveSessionHandler);
  //      hb = new HBox(sessionInfoContent, spaceBetween, joinAndLeaveButton);
  //    }
  //  }
  //
  //
  //  Pane p = new Pane(hb);
  //  p.setAccessibleText(sessionId.toString());
  //  return p;
  //}

  //protected void addOneSessionGui(String accessToken,
  //                                Long sessionId,
  //                                Session curSession,
  //                                VBox sessionVbox) {
  //  String sessionInfo = formatSessionInfo(curSession);
  //  Label sessionInfoLabel = new Label(sessionInfo);
  //  Pane newPane = createSessionGui(accessToken, sessionId, curSession, sessionInfoLabel);
  //  // defer GUI change to lobby main page
  //  Platform.runLater(() -> {
  //    sessionVbox.getChildren().add(newPane);
  //  });
  //}

  protected void removeSessionsGui(Long sessionId, VBox sessionVbox) {

    for (Iterator<Node> iterator = sessionVbox.getChildren().iterator(); iterator.hasNext(); ) {
      Node n = iterator.next();
      if (n.getAccessibleText().equals(sessionId.toString())) {
        // defer GUI remove after
        Platform.runLater(iterator::remove);
      }
    }
  }

  private Thread getUpdateOneSessionGuiThread(Long sessionId) {
    return new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      Session localSession;
      boolean isFirstCheck = true;
      LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      while (true) {
        int responseCode = 408;
        while (responseCode == 408) {
          try {
            longPullResponse =
                lobbyRequestSender.sendGetOneSessionDetailRequest(sessionId, hashedResponse);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
          responseCode = longPullResponse.getStatus();
        }
        if (responseCode == 200) {
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          // obtain the latest session info of one session
          localSession = new Gson().fromJson(longPullResponse.getBody(), Session.class);
          List<String> curPlayers = localSession.getPlayers();
          SessionGui sessionGuiNeedToUpdate = SessionGuiManager.getSessionIdGuiMap().get(sessionId);
          // bind the sessionGui to a different Session that contains diff info
          sessionGuiNeedToUpdate.setCurSession(localSession);

          if (!isFirstCheck) {
            // if it's NOT first check, we have to change buttons
            sessionGuiNeedToUpdate.updateSessionGui();
          }
          // otherwise, always check to change text
          sessionGuiNeedToUpdate.setSessionInfoText();




          //
          //
          //for (Node n : sessionVbox.getChildren()) {
          //  if (n.getAccessibleText().equals(sessionId.toString())) {
          //    // if found the corresponding GUI session, first update button if game launched
          //    // if user is not in game / in game, button is updated differently
          //    Pane sessionPane = (Pane) n;
          //    HBox sessionHbox = (HBox) sessionPane.getChildren().get(0);
          //    // if it's NOT firstCheck, we want to ONLY do button replacement!
          //    if (!isFirstCheck) {
          //      // if user is in this game, then we only need to delete the Leave button to Play
          //      // if user is NOT in this game, change the Join button to Watch
          //      // either way, we are just removing one button and create a new one
          //      String curUserName = App.getUser().getUsername();
          //      Button replaceButton;
          //      if (curPlayers.contains(curUserName)) {
          //        replaceButton = new Button("Play");
          //        replaceButton.setOnAction(createPlayGameHandler());
          //      } else {
          //        replaceButton = new Button("Watch");
          //        replaceButton.setOnAction(createWatchGameHandler());
          //      }
          //      String creatorName = localSession.getCreator();
          //      int curPlayersCount = curPlayers.size();
          //      int minPlayersCount = localSession.getGameParameters().getMinSessionPlayers();
          //      if (curUserName.equals(creatorName)) {
          //        Button launchButton = (Button) sessionHbox.getChildren().get(3);
          //        // If it's curUser, then we must check for set Enable for him/her
          //        if (launchButton.isDisabled()) {
          //          // only setDisable(false) for the launch button if we have enough players
          //          if (curPlayersCount == minPlayersCount) {
          //            Platform.runLater(() -> {
          //              launchButton.setDisable(false);
          //            });
          //          }
          //        } else {
          //          // if launchButton.isDisabled() == false, means we can click it
          //          // if it's clickable, then we should replace it here
          //          Platform.runLater(() -> {
          //            sessionHbox.getChildren().remove(2);
          //            sessionHbox.getChildren().remove(2);
          //            sessionHbox.getChildren().add(replaceButton);
          //          });
          //        }
          //      } else {
          //        // if the user is not creator, there is no point updating the launch for one
          //        // thus ONLY the remove / replace button logic here for user
          //        Platform.runLater(() -> {
          //          // remove the Button on index 2 (0: Label, 1: Region, 2: Button)
          //          sessionHbox.getChildren().remove(2);
          //          sessionHbox.getChildren().add(replaceButton);
          //        });
          //      }
          //
          //    }
          //
          //    // regardless of first check or not, we always want the info to be updated
          //    Label sessionInfoLabel = (Label) sessionHbox.getChildren().get(0);
          //    String newSessionInfo = formatSessionInfo(localSession);
          //    isFirstCheck = false;
          //    // defer updating session info
          //    Platform.runLater(() -> {
          //      sessionInfoLabel.setText(newSessionInfo);
          //    });
          //
          //  }
          //}
        }
      }
    });
  }

  private Set<Long> findDifferentSessionIds(Set<Long> setA,
                                            Set<Long> setB) {
    HashSet<Long> resultSet = new HashSet<>();
    if (setA.size() > setB.size()) {
      for (Long l : setA) {
        if (!setB.contains(l)) {
          resultSet.add(l);
        }
      }

    } else if (setB.size() > setA.size()) {
      for (Long l : setB) {
        if (!setA.contains(l)) {
          resultSet.add(l);
        }
      }
    }

    return resultSet;
  }

  private void stopAndClearThreads() {
    for (Thread t : updateThreadPool) {
      t.interrupt();
    }
    updateThreadPool.clear();
  }


  /**
   * Initializing info for local lobby.
   *
   * @throws UnirestException in case unirest failed to send a request
   */
  public void initialize() throws UnirestException {
    //SessionsManagerGui test = SessionsManagerGui.getInstance();
    //for (int i = 0; i <= 40; i++) {
    //  test.getChildren().add(new Label("test" + i));
    //}
    //
    //allSessionScrollPane.setContent(test);

    // Get all available games and pre-set the ChoiceBox
    // mainly about display on session info
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    List<GameParameters> gameParameters = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();
    updateThreadPool = new ArrayList<>();

    for (GameParameters g : gameParameters) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }
    // Available games to choose from ChoiceBox
    ObservableList<String> gameOptionsList = FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);


    // Set up the thread to keep updating sessions
    VBox sessionVbox = new VBox();
    Map<Long, SessionGui> sessionIdGuiMap = new HashMap<>();
    Thread updateAddRemoveSessionThread = new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      SessionList localSessionList = null;
      boolean isFirstCheck = true;
      while (true) {
        int responseCode = 408;
        User user = App.getUser();
        // if there is no user logged in, do not do anything
        if (user == null) {
          continue;
        }
        while (responseCode == 408) {
          try {
            longPullResponse =
                lobbyRequestSender.sendGetAllSessionDetailRequest(hashedResponse);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
          responseCode = longPullResponse.getStatus();
        }

        if (responseCode == 200) {
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          if (isFirstCheck) {
            localSessionList = new Gson().fromJson(longPullResponse.getBody(), SessionList.class);
            isFirstCheck = false;

            Map<Long, Session> localSessionsMap = localSessionList.getSessions();
            for (Long curSessionId : localSessionsMap.keySet()) {
              Session curSession = localSessionsMap.get(curSessionId);

              // create the new GUI
              SessionGui curSessionGui = new SessionGui(curSession, curSessionId, user);

              // set up the session gui
              curSessionGui.setup();

              // add the session, sessionGui to session gui manager based on sessionId map
              SessionGuiManager.addSessionDataAndGui(curSessionGui,curSession,curSessionId);

              sessionIdGuiMap.put(curSessionId, curSessionGui);
              //addOneSessionGui(user.getAccessToken(), sessionId, curSession, sessionVbox);
              Thread updateSessionInfoThread = getUpdateOneSessionGuiThread(curSessionId);
              updateThreadPool.add(updateSessionInfoThread);
              updateSessionInfoThread.start();
            }
          }

          Platform.runLater(() -> {
            allSessionScrollPane.setContent(SessionGuiManager.getInstance());
          });
          else {
            SessionList remoteSessionList =
                new Gson().fromJson(longPullResponse.getBody(), SessionList.class);

            Set<Long> remoteSessionIds = remoteSessionList.getSessionIds();
            Set<Long> localSessionIds = localSessionList.getSessionIds();
            Set<Long> diffSessionIds = findDifferentSessionIds(remoteSessionIds, localSessionIds);
            if (diffSessionIds.isEmpty()) {
              continue;
            }
            int remoteSessionCount = remoteSessionList.getSessionsCount();
            int localSessionCount = localSessionList.getSessionsCount();
            // local more than remote -> delete Session GUI
            if (localSessionCount > remoteSessionCount) {
              for (Long diffSessionId : diffSessionIds) {
                SessionGui curSessionGui =
                    SessionGuiManager.getSessionIdGuiMap().get(diffSessionId);
                SessionGuiManager.removeSessionDataAndGui(curSessionGui,diffSessionId);
              }
            } else if (remoteSessionCount > localSessionCount) {
              // remote more than local -> add Session GUI
              for (Long diffSessionId : diffSessionIds) {
                String accessToken = user.getAccessToken();
                Session curSession = remoteSessionList.getSessionById(diffSessionId);
                SessionGui newSessionGui = new SessionGui(curSession, diffSessionId, user);
                // create and start the thread that is responsible for updating this newly
                // created Session Pane
                Thread updateSessionInfoThread = getUpdateOneSessionGuiThread(diffSessionId);
                updateThreadPool.add(updateSessionInfoThread);
                updateSessionInfoThread.start();
              }
            }
            // update local sessions
            localSessionList = remoteSessionList;

          }
        }
      }
    });
    updateThreadPool.add(updateAddRemoveSessionThread);
    updateAddRemoveSessionThread.start();

  }
}

