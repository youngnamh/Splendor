package project.view.splendor.communication;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;

/**
 * Generic Action abstract class for blackboard architecture. Implement your own actions, representing kinds of actions
 * performed by players in a blackboard architecture. Actions are generated by a custom ActionGenerator and performed by
 * a custom ActionInterpreter.
 */
//
//@JsonType(
//    property = "type",
//    subtypes = {
//        @JsonSubtype(clazz = PurchaseAction.class, name = "purchaseAction"),
//        @JsonSubtype(clazz = ReserveAction.class, name = "reserveAction"),
//        @JsonSubtype(clazz = TakeTokenAction.class, name = "takeTokenAction")
//    }
//)
public interface Action {
  //Useful when the client receives the map of Actions, to identify whether action
  //is CardAction or not
  //private boolean isCardAction;
  //private String type;
  //
  //public String getType() {
  //  return type;
  //}
  //
  //public void setType(String type) {
  //  this.type = type;
  //}
  //
  //public Action(boolean isCardAction) {
  //  this.isCardAction = isCardAction;
  //}
  //
  //public boolean getIsCardAction() {
  //  return isCardAction;
  //}
  //
  //public void setCardAction(boolean cardAction) {
  //  isCardAction = cardAction;
  //}

  // abstract method that leave to subclass to implement
  // the reason why it returns void is that we only provide the
  // VALID ACTIONS MAP to player when generated them, so there is no need to check
  // whether the Action player chose is valid or not. They must be
  void execute(GameInfo currentGameState, PlayerInGame playerState);
  boolean checkIsCardAction();
}