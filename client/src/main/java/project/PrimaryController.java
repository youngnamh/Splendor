package project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The PrimaryController use to manage the general flow of the program.
 */
public class PrimaryController {


  public ImageView purchasedCard;

  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private Pane purchaseContent;

  @FXML
  private BorderPane waitingRoom;

  @FXML
  private Pane confirmPane;

  @FXML
  private Button quitGameButton;

  @FXML
  private TextField userName;


  @FXML
  private PasswordField userPassword;

  @FXML
  private BorderPane lobbyPane;

  @FXML
  private Label logInPageErrorMessage;

  @FXML
  private Button confirmButton;

  @FXML
  private Button backButton;

  @FXML
  private Pane resCardPane;

  @FXML
  private Pane devCardPane;

  @FXML
  public Button plusR;
  public Button plusW;
  public Button plusB;

  @FXML
  public Button minusR;
  public Button minusW;
  public Button minusB;

  @FXML
  public Text counterRed = new Text();
  public Text counterWhite = new Text();
  public Text counterBlack = new Text();

  @FXML
  public Text totalRed = new Text();
  public Text totalWhite = new Text();
  public Text totalBlack = new Text();

  @FXML
  private BorderPane gameBoardContent = new BorderPane();

  private ImageView newCrad;

  /**
   * The logic of handling log in. The methods check if
   * the user has input both username and user password or not
   *
   * @throws IOException IOException if fxml file not found
   */
  @FXML
  protected void onLogInButtonClick() throws IOException {
    String userNameStr = userName.getText();
    String userPasswordStr = userPassword.getText();

    // For the sake of simplicity, we only check if password and username exist
    if (Objects.equals(userNameStr, "") || Objects.equals(userPasswordStr, "")) {
      logInPageErrorMessage.setText("Please enter both valid username and password");
    } else {
      App.setRoot("LobbyService");
    }

  }

  /**
   * Go back (reset the scene) to the Scene loaded from splendor.fxml
   *
   * @throws IOException IOException if fxml file not found
   */
  @FXML
  protected void onLogOutMenuItemClick() throws IOException {
    App.setRoot("splendor");
  }

  /**
   * Close the current stage once the quitGameButton has been clicked.
   */
  @FXML
  protected void onQuitGameButtonClick() {
    Stage curStage = (Stage) quitGameButton.getScene().getWindow();
    curStage.close();
  }

  /**
   * Displays the options of actions the player can do once
   * they click on a regular development card (not orient).
   *
   * @throws IOException IOException if fxml file not found
   */
  @FXML
  protected void onDevelopmentCardShapeClick() throws IOException {
    App.setRootWithSizeTitle("splendor_card_action", 360, 170, "Make your decision");

  }

  /**
   * The logic to handle player purchasing a regular development card (not orient).
   */
  @FXML
  protected void madePurchase() throws IOException {
    gameBoardContent.getChildren();
    App.setPopUpRoot("splendor_purchase_confirm", purchaseContent.getScene());
  }

  /**
   * The logic to handle player close the pop-up Stage when they want to cancel their action.
   */
  @FXML
  protected void cancelAction() {
    Stage curStage = (Stage) purchaseContent.getScene().getWindow();
    // Just close the window without doing anything
    curStage.close();

  }


  @FXML
  protected void purchased() throws FileNotFoundException {
    InputStream stream = new FileInputStream("client/src/main/resources/project/pictures/level3/w1.png");
    Image img = new Image(stream);
    purchasedCard.setImage(img);
  }

  /**
   * The logic to handle Reserving Card (both orient and normal card can use this method).
   */
  @FXML
  protected void madeReserve() throws IOException {
    //Stage curStage = (Stage) purchaseContent.getScene().getWindow();
    Scene curScene = purchaseContent.getScene();
    App.setPopUpRoot("splendor_reserve", curScene);
    //TODO: Check condition if the reserve can be done successfully
    // then close the window

  }

  @FXML
  protected void joinGame() throws IOException {
    Stage curStage = (Stage) waitingRoom.getScene().getWindow();
    App.setRoot("splendor_game_board");
    App.setHandCard("my_development_cards");
    curStage.close();

  }

  @FXML
  protected void joinWaitingRoom() throws IOException {
    App.setRootWithSizeTitle("splendor_waiting_room", 1000, 500, "Waiting Room");
  }

  /**
   * Sets up the Choice Box options in Main Lobby.
   */
  @FXML
  public void initialize() {
    //Create observable list for game options drop down (choice box)
    ObservableList<String> gameOptionsList = FXCollections
        .observableArrayList("Splendor (Base Game)", "Splendor (Orient Expansion)");
    //gameChoices will be null until the main lobby stage is launched
    if (gameChoices != null) {
      gameChoices.setItems(gameOptionsList);
    }
  }


  /**
   * Getting rid of the confirmation pop up once "confirm" is pressed when purchasing a card.
   */
  @FXML
  public void confirmClick() throws FileNotFoundException {
    Stage curStage = (Stage) confirmPane.getScene().getWindow();
    purchasedCard = (ImageView) App.getScene().lookup("#purchasedCard");
    InputStream stream1 = new FileInputStream("client/src/main/resources/project/pictures/level3/w1.png");
    Image img1 = new Image(stream1);
    purchasedCard.setImage(img1);
    newCrad = (ImageView) App.getHandCard().lookup("#newCard");
    InputStream stream2 = new FileInputStream("client/src/main/resources/project/pictures/level3/b4.png");
    Image img2 = new Image(stream2);
    newCrad.setImage(img2);
    curStage.close();
  }

  /**
   * Getting rid of the confirmation pop up once "back" is pressed when purchasing a card.
   */
  @FXML
  protected void backClick() {
    Stage curStage = (Stage) confirmPane.getScene().getWindow();
    curStage.close();
  }

  /**
   * Getting rid of the development cards pop up once "x" is pressed when purchasing a card.
   */

  public void exitDevCard() {
    Stage curStage = (Stage) devCardPane.getScene().getWindow();
    curStage.close();
  }

  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  public void openMyCards() {
    Stage newStage = new Stage();
    newStage.setTitle("My Development Cards");
    newStage.setScene(App.getHandCard());
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    newStage.show();
  }

  /**
   * Opening the reserve card pop up once reserved card button is pressed.
   */

  public void openMyReservedCards() throws IOException {
    App.setRootWithSizeTitle("my_reserved_cards", 789, 406, "My Reserved Cards");
  }

  /**
   * Getting rid of the reserved cards pop up once "back" is pressed when purchasing a card.
   */

  public void exitReserved() {
    Stage curStage = (Stage) resCardPane.getScene().getWindow();
    curStage.close();

  }


  public int numR = 0;
  public int num2R = 7;
  public int numW = 0;
  public int num2W = 7;
  public int numB = 0;
  public int num2B = 7;

  /**
   * decrement red.
   */
  public void decrementR() {
    if ((numR - 1) >= 0) {
      numR = numR - 1;
      counterRed.setText(String.valueOf(numR));
    }
  }

  /**
   * decrement white.
   */
  public void decrementW() {
    if ((numW - 1) >= 0) {
      numW = numW - 1;
      counterWhite.setText(String.valueOf(numW));
    }
  }

  /**
   * decrement black.
   */
  public void decrementB() {
    if ((numB - 1) >= 0) {
      numB = numB - 1;
      counterBlack.setText(String.valueOf(numB));
    }
  }

  /**
   * increment red.
   */
  public void incrementR() {
    if ((numR + 1) <= num2R && numR < 2) {
      numR = numR + 1;
      counterRed.setText(String.valueOf(numR));
    }
  }

  /**
   * increment white.
   */
  public void incrementW() {
    if ((numW + 1) <= num2W && numW < 2 && numR < 2) {
      numW = numW + 1;
      counterWhite.setText(String.valueOf(numW));
    }
  }

  /**
   * increment black.
   */
  public void incrementB() {
    if ((numB + 1) <= num2B && numB < 2 && numR < 2) {
      numB = numB + 1;
      counterBlack.setText(String.valueOf(numB));
    }
  }

  /**
   * confirm/ update total gems.
   */
  public void gemConfirm() {
    num2R = num2R - numR;
    totalRed.setText(String.valueOf(num2R));
    numR = 0;
    counterRed.setText(String.valueOf(numR));

    num2W = num2W - numW;
    totalWhite.setText(String.valueOf(num2W));
    numW = 0;
    counterWhite.setText(String.valueOf(numW));

    num2B = num2B - numB;
    totalBlack.setText(String.valueOf(num2B));
    numB = 0;
    counterBlack.setText(String.valueOf(numB));
  }

}