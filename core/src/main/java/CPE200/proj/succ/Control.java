package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.TurnState;
import CPE200.proj.succ.model.board.GameBoard;


public class Control {

    private GameBoard gameBoard; // กระดานของตัวเกม
    private GameState currentState = GameState.MainMenu; // stage ปัจจุบันของตัวเกม
    private TurnState currentPhase = TurnState.Player_Move;// turn phase ปัจจุบัน


    public Control(){
        gameBoard = new GameBoard(12,18);
        gameBoard = gameBoard.newBoard(GameState.Stage1);
    }

    public GameBoard gameBoard() { return gameBoard; }
    public TurnState getCurrentPhase() {
        return currentPhase;
    }
    public GameState getCurrentState() {
        return currentState;
    }
    public void setCurrentState(GameState currentState) { this.currentState = currentState;}
    public void nextPhase(){
        switch (currentPhase){
            case Player_Move:
                this.currentPhase = TurnState.Police_Check;break;
            case Police_Check:
                this.currentPhase = TurnState.Bribe_CD;break;
            case Bribe_CD:
                this.currentPhase = TurnState.Player_Move;break;
        }
    }
    private void moveThumnaz(GameObject obj){
        gameBoard.toNull(gameBoard.getThumnaz());
        gameBoard.toThumnaZ(obj);
        gameBoard.setThumnaz(obj.row(),obj.column());
    }
    public void restartState(){
        gameBoard = gameBoard.newBoard(currentState);
    }

    public void nextStage(){
        switch (currentState){
            case Stage1:
                this.currentState = GameState.Stage2;break;
            case Stage2:
                this.currentState = GameState.Stage3;break;
            case Stage3:
                this.currentState = GameState.Stage4;break;
            case Stage4:
                this.currentState = GameState.Stage5;break;
            case Stage5:
                this.currentState = GameState.GameClear;break;
        }
    }
    public void manageNextObject(GameObject obj , GameObject nextObj){
        switch (obj.getType()){
            case Bribe:
                gameBoard.toBribe(nextObj);
                moveThumnaz(obj);
                break;
            case Coke:
                setCurrentState(GameState.GameOver);break;
            case Flour:
                gameBoard.getFlours().remove(obj);
                moveThumnaz(obj);
                break;
            case Key:
                gameBoard.getInventory().add(obj);
                moveThumnaz(obj);
                break;
            case StageDoor:
                if(gameBoard.checkFlours()){
                    nextStage();
                    restartState();
                }
                break;
            case Door:
                if(gameBoard.haveKey()){
                    moveThumnaz(obj);
                }
                break;
            case Converter:
                gameBoard.convertFlours();
                moveThumnaz(obj);
        }
    }

    public void moveRight(){
        GameObject rightTile = gameBoard.board(gameBoard.getThumnazX(), gameBoard.getThumnazY()+1);
        if(rightTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(rightTile);
            gameBoard.setThumnaz(rightTile.row(),rightTile.column());
            nextPhase();
        }else if(gameBoard().canRight(rightTile)){
            manageNextObject(rightTile,gameBoard.rightObject(rightTile));
            nextPhase();
        }

    }
    public void moveLeft(){
        GameObject leftTile = gameBoard.board(gameBoard.getThumnazX(), gameBoard.getThumnazY()-1);
        if(leftTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(leftTile);
            gameBoard.setThumnaz(leftTile.row(),leftTile.column());
            nextPhase();
        }else if(gameBoard.canLeft(leftTile)){
            manageNextObject(leftTile,gameBoard.leftObject(leftTile));
            nextPhase();
        }
    }
    public void moveUp(){
        GameObject upTile = gameBoard.board(gameBoard.getThumnazX()-1, gameBoard.getThumnazY());
        if(upTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(upTile);
            gameBoard.setThumnaz(upTile.row(),upTile.column());
            nextPhase();
        }else if(gameBoard().canUp(upTile)){
            manageNextObject(upTile,gameBoard.upperObject(upTile));
            nextPhase();
        }
    }
    public void moveDown(){
        GameObject downTile = gameBoard.board(gameBoard.getThumnazX()+1, gameBoard.getThumnazY());
        if(downTile.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(downTile);
            gameBoard.setThumnaz(downTile.row(),downTile.column());
            nextPhase();
        }else if(gameBoard().canDown(downTile)){
            manageNextObject(downTile,gameBoard.lowerObject(downTile));
            nextPhase();
        }
    }
}
