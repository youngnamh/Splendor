package project.view.splendor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import project.App;

public class HorizontalPlayerInfoGui extends HBox implements PlayerInfoGui{
  private final PlayerPosition playerPosition;
  private final String playerName;
  private final int initialTokenNum;

  public HorizontalPlayerInfoGui(PlayerPosition playerPosition, String playerName,int initialTokenNum) {
    this.playerPosition = playerPosition;
    this.playerName = playerName;
    this.initialTokenNum = initialTokenNum;
    // TODO: The fxml associated with this class, must be bind to controller = project.App
    FXMLLoader fxmlLoader;
    if (playerPosition.equals(PlayerPosition.TOP)) {
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_h_top.fxml"));
    } else if (playerPosition.equals(PlayerPosition.BOTTOM)) {
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_h_bottom.fxml"));
    } else {
      // will throw exception if not LEFT OR RIGHT
      fxmlLoader = new FXMLLoader();
    }
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }



  public PlayerPosition getPlayerPosition() {
    return playerPosition;
  }

  public String getPlayerName(){
    return playerName;
  }


  public Map<Colour, Map<PlayerTokenInfo, Text>> getHorizontalPlayerTokenHandInfo(PlayerPosition playerPosition) {
    Map<Colour, Map<PlayerTokenInfo, Text>> result = new HashMap<>();
    Colour[] colours = App.getAllColours();
    Map<PlayerTokenInfo, Text> info = new HashMap<>();
    Map<PlayerTokenInfo, Text> goldInfo = new HashMap<>();
    if (this.playerPosition.equals(PlayerPosition.TOP)) {
      for (int i = 0; i < 5; i++) {
        Group curGroup = (Group) this.getChildren().get(i);
        info.put(PlayerTokenInfo.GEM, (Text) curGroup.getChildren().get(1));
        info.put(PlayerTokenInfo.TOKEN, (Text) curGroup.getChildren().get(3));
        result.put(colours[i], info);
      }
      Group curGroup = (Group) this.getChildren().get(6);
      goldInfo.put(PlayerTokenInfo.TOKEN, (Text) curGroup.getChildren().get(1));
    } else if (this.playerPosition.equals(PlayerPosition.BOTTOM)) {
      for (int i = 1; i < 6; i++) {
        Group curGroup = (Group) this.getChildren().get(i);
        info.put(PlayerTokenInfo.GEM, (Text) curGroup.getChildren().get(1));
        info.put(PlayerTokenInfo.TOKEN, (Text) curGroup.getChildren().get(3));
        result.put(colours[i], info);
      }
      Group curGroup = (Group) this.getChildren().get(6);
      goldInfo.put(PlayerTokenInfo.TOKEN, (Text) curGroup.getChildren().get(1));
    }
    result.put(Colour.GOLD, goldInfo);
    return result;
  }

  public Map<PlayerVisibleInfo, Text> getHorizontalPlayerInfo(PlayerPosition playerPosition){
    Map<PlayerVisibleInfo, Text> resultMap = new HashMap<>();
    if (playerPosition.equals(PlayerPosition.TOP)){
      Group group = (Group) this.getChildren().get(6);
      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(9));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(6));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(7));

    } else if (playerPosition.equals(PlayerPosition.BOTTOM)){
      Group group = (Group) this.getChildren().get(0);
      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(9));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(6));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(7));
    }
    return resultMap;
  }


  private void giveInitialStartTokens() {
    Map<Colour, Map<PlayerTokenInfo, Text>> allTokenColourMap = getHorizontalPlayerTokenHandInfo(playerPosition);
    Colour[] baseColours = App.getBaseColours();
    for (Colour c : baseColours) {
      Map<PlayerTokenInfo, Text> oneColourMap = allTokenColourMap.get(c);
      Text tokenText = oneColourMap.get(PlayerTokenInfo.TOKEN);
      tokenText.setText(initialTokenNum+"");
    }
    Text goldTokenText = allTokenColourMap.get(Colour.GOLD).get(PlayerTokenInfo.TOKEN);
    goldTokenText.setText(initialTokenNum+"");
  }


  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);

  }
}

