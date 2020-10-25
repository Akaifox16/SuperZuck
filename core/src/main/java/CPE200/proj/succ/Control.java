package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.TurnState;
import CPE200.proj.succ.model.board.GameBoard;
import CPE200.proj.succ.model.movable.Bomb;
import CPE200.proj.succ.model.staticObject.Police;
import com.badlogic.gdx.audio.Music;


public class Control {
    private GameBoard gameBoard; // กระดานของตัวเกม
    private GameState currentState = GameState.MainMenu; // stage ปัจจุบันของตัวเกม
    private TurnState currentPhase = TurnState.Player_Move;// turn phase ปัจจุบัน


    public Control(){
        gameBoard = new GameBoard(11,18);
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
                this.currentPhase = TurnState.Object_Cool_down;break;
            case Object_Cool_down:
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
        switch (currentState) {
            case Stage1:
                this.currentState = GameState.Stage2;
                break;
            case Stage2:
                this.currentState = GameState.Stage3;
                break;
            case Stage3:
                this.currentState = GameState.Stage4;
                break;
            case Stage4:
                this.currentState = GameState.Stage5;
                break;
            case Stage5:
                this.currentState = GameState.GameClear;
                break;
        }
    }
    public void manageNextObject(GameObject obj , GameObject nextObj){
        switch (obj.getType()){
            case Bribe:
                if(nextObj.getType() == GameObjectType.NULL) {
                    gameBoard.toBribe(nextObj);
                    moveThumnaz(obj);
                }
                break;
            case Bomb:
                if(nextObj.getType() == GameObjectType.NULL) {
                    gameBoard.toBomb(nextObj,(Bomb)obj);
                    moveThumnaz(obj);
                }
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
                break;
        }
    }

    private void move(GameObject current , GameObject next , boolean canNext){
        if(current.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(current);
            gameBoard.setThumnaz(current.row(),current.column());
            nextPhase();
        }else if(canNext){
            manageNextObject(current,next);
            nextPhase();
        }
    }

    public void moveRight(){
        GameObject rightTile = gameBoard().rightObject(gameBoard.getThumnaz());
        move(rightTile,gameBoard.rightObject(rightTile),gameBoard.canRight(rightTile));
    }

    public void moveLeft(){
        GameObject leftTile = gameBoard().leftObject(gameBoard.getThumnaz());
        move(leftTile,gameBoard.leftObject(leftTile),gameBoard.canLeft(leftTile));
    }

    public void moveUp(){
        GameObject upTile = gameBoard.upperObject(gameBoard.getThumnaz());
        move(upTile,gameBoard.upperObject(upTile), gameBoard().canUp(upTile));
    }
    public void moveDown(){
        GameObject downTile = gameBoard().lowerObject(gameBoard.getThumnaz());
        move(downTile, gameBoard().lowerObject(downTile), gameBoard().canDown(downTile));
    }

    public void toGameOver(){
        setCurrentState(GameState.GameOver);
    }
}
