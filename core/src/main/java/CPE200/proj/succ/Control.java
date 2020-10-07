package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.TurnState;
import CPE200.proj.succ.model.board.GameBoard;


public class Control {
    private GameState currentState = GameState.MainMenu; // stage ปัจจุบันของตัวเกม
    private GameBoard gameBoard;
    private TurnState currentTurn = TurnState.Player_Move;


    public Control(){
        gameBoard = new GameBoard(12,18);
        gameBoard.Stage1();
    }

    public GameBoard gameBoard() { return gameBoard; }
    public TurnState getCurrentTurn() {
        return currentTurn;
    }
    public GameState getCurrentState() {
        return currentState;
    }
    public void setCurrentState(GameState currentState) { this.currentState = currentState; }

    public void moveRight(){
        GameObject rightTile = gameBoard.board(gameBoard.getThumnazX(), gameBoard.getThumnazY()+1);
        if(rightTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(rightTile);
            gameBoard.setThumnazX(gameBoard.getThumnazX());
            gameBoard.setThumnazY(gameBoard.getThumnazY()+1);
        }else if(gameBoard().canRight(rightTile)){
            gameBoard.toBribe(gameBoard.rightObject(rightTile));
            gameBoard.toThumnaZ(rightTile);
            gameBoard.toNull(gameBoard.getThumnaz());
            gameBoard.setThumnazX(gameBoard.getThumnazX());
            gameBoard.setThumnazY(gameBoard.getThumnazY()+1);
        }

    }
    public void moveLeft(){
        GameObject leftTile = gameBoard.board(gameBoard.getThumnazX(), gameBoard.getThumnazY()-1);
        if(leftTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(leftTile);
            gameBoard.setThumnazX(gameBoard.getThumnazX());
            gameBoard.setThumnazY(gameBoard.getThumnazY()-1);
        }else if(gameBoard.canLeft(leftTile)){
            gameBoard.toBribe(gameBoard.leftObject(leftTile));
            gameBoard.toThumnaZ(leftTile);
            gameBoard.toNull(gameBoard.getThumnaz());
            gameBoard.setThumnazX(gameBoard.getThumnazX());
            gameBoard.setThumnazY(gameBoard.getThumnazY()-1);
        }
    }
    public void moveUp(){
        GameObject upTile = gameBoard.board(gameBoard.getThumnazX()-1, gameBoard.getThumnazY());
        if(upTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(upTile);
            gameBoard.setThumnazX(gameBoard.getThumnazX()-1);
            gameBoard.setThumnazY(gameBoard.getThumnazY());
        }else if(gameBoard().canUp(upTile)){
            gameBoard.toBribe(gameBoard.upperObject(upTile));
            gameBoard.toThumnaZ(upTile);
            gameBoard.toNull(gameBoard.getThumnaz());
            gameBoard.setThumnazX(gameBoard.getThumnazX()-1);
            gameBoard.setThumnazY(gameBoard.getThumnazY());
        }
    }
    public void moveDown(){
        GameObject downTile = gameBoard.board(gameBoard.getThumnazX()+1, gameBoard.getThumnazY());
        if(downTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(downTile);
            gameBoard.setThumnazX(gameBoard.getThumnazX()+1);
            gameBoard.setThumnazY(gameBoard.getThumnazY());
        }else if(gameBoard().canDown(downTile)){
            gameBoard.toBribe(gameBoard.lowerObject(downTile));
            gameBoard.toThumnaZ(downTile);
            gameBoard.toNull(gameBoard.getThumnaz());
            gameBoard.setThumnazX(gameBoard.getThumnazX()+1);
            gameBoard.setThumnazY(gameBoard.getThumnazY());
        }
    }
}
