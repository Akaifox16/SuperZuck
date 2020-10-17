package CPE200.proj.succ.model.board;

import CPE200.proj.succ.Control;
import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectFactory;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.item.Flour;
import CPE200.proj.succ.model.staticObject.Police;
import CPE200.proj.succ.model.movable.Bribe;
import CPE200.proj.succ.model.movable.ThumnaZ;

import java.util.ArrayList;
import java.util.List;


public class GameBoard {
    private final GameObject[][] board;
    private final int row;
    private final int column;
    private List<GameObject> inventory;
    private List<Police> polices;
    private List<Flour> flours;
    private int thumnazX , thumnazY; // index ของ main character
    private GameObjectFactory factory;

    public GameObject board(int i , int j){
        return  board[i][j];
    }
    public void setThumnaz(int x , int y){setThumnazX(x);setThumnazY(y);}
    public void setThumnazX(int thumnazX) {
        this.thumnazX = thumnazX;
    }
    public void setThumnazY(int thumnazY) {
        this.thumnazY = thumnazY;
    }

    public void toNull(GameObject gameObject){
        board[gameObject.row()][gameObject.column()] = new GameObject(gameObject.row(),gameObject.column());
    }
    public void toThumnaZ(GameObject gameObject){
        board[gameObject.row()][gameObject.column()] = new ThumnaZ(gameObject.row(),gameObject.column());
    }
    public void toBribe(GameObject gameObject){
        board[gameObject.row()][gameObject.column()] = new Bribe(gameObject.row(),gameObject.column());
    }
    public void addToBoard(int i, int j , GameObjectType type){
        GameObject obj = factory.create(i,j,type);
        board[i][j] = obj;
        switch (type){
            case Thumnaz:
                this.thumnazX = i;
                this.thumnazY = j;
                break;
            case Police:
                polices.add((Police) obj);break;
            case Flour:
            case Coke:
                flours.add((Flour)obj);break;
        }

    }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public int getThumnazX() {
        return thumnazX;
    }
    public int getThumnazY() {
        return thumnazY;
    }
    public GameObject getThumnaz(){
        return  board(thumnazX,thumnazY);
    }
    public List<GameObject> getInventory() {
        return inventory;
    }
    public List<Flour> getFlours() {
        return flours;
    }

    public GameObject leftObject(GameObject temp){return board(temp.row(), temp.column()-1);}
    public GameObject rightObject(GameObject temp){return board(temp.row(),temp.column()+1);}
    public GameObject upperObject(GameObject temp){return board(temp.row()-1,temp.column());}
    public GameObject lowerObject(GameObject temp){return board(temp.row()+1,temp.column());}

    public boolean canLeft(GameObject left){
        if(left.getType() == GameObjectType.Door || left.getType() == GameObjectType.StageDoor) return true;
        return  left.pickable() || (left.movable()  && leftObject(left).getType() == GameObjectType.NULL) || (left.movable() && leftObject(left).pickable());
    }
    public boolean canRight(GameObject right){
        if(right.getType() == GameObjectType.Door || right.getType() == GameObjectType.StageDoor) return true;
        return  right.pickable() || (right.movable()  && rightObject(right).getType() == GameObjectType.NULL) || (right.movable() && rightObject(right).pickable());
    }
    public boolean canUp(GameObject upper){
        if(upper.getType() == GameObjectType.Door || upper.getType() == GameObjectType.StageDoor) return true;
        return  upper.pickable() || (upper.movable()  && upperObject(upper).getType() == GameObjectType.NULL) || (upper.movable() && upperObject(upper).pickable());
    }
    public boolean canDown(GameObject lower){
        if(lower.getType() == GameObjectType.Door || lower.getType() == GameObjectType.StageDoor) return true;
        return  lower.pickable() || (lower.movable()  && lowerObject(lower).getType() == GameObjectType.NULL) || (lower.movable() && lowerObject(lower).pickable());
    }

    public void convertFlours(){
        for (Flour flour:flours) {
            flour.convert();
        }
    }
    public void checkPolice(Control game) throws InterruptedException {
        for (Police police:polices) {
            GameObject left = leftObject(police);
            GameObject right = rightObject(police);
            GameObject upper = upperObject(police);
            GameObject lower = lowerObject(police);

            switch (police.getState()) {
                case Suspect:
                    if(     left.getType() == GameObjectType.Bribe ||
                            right.getType()== GameObjectType.Bribe ||
                            upper.getType()== GameObjectType.Bribe ||
                            lower.getType()== GameObjectType.Bribe){
                        if(left.getType() == GameObjectType.Bribe){
                            toNull(left);
                            police.bribed();break;
                        }else if(right.getType() == GameObjectType.Bribe){
                            toNull(right);
                            police.bribed();break;
                        }else if(upper.getType() == GameObjectType.Bribe){
                            toNull(upper);
                            police.bribed();break;
                        }else{
                            toNull(lower);
                            police.bribed();break;
                        }
                    }else if(left.getType() == GameObjectType.Thumnaz ||
                            right.getType()== GameObjectType.Thumnaz ||
                            upper.getType()== GameObjectType.Thumnaz ||
                            lower.getType()== GameObjectType.Thumnaz){
                        toNull(getThumnaz());
                        game.toGameOver(police);
                        break;
                    }
                case Sleep:
                    if(police.getBribeCoolDown() > 0)
                        police.setBribeCoolDown(police.getBribeCoolDown() - 1);
                    else {
                        if(left.getType() == GameObjectType.Thumnaz ||
                        right.getType() == GameObjectType.Thumnaz ||
                        upper.getType() == GameObjectType.Thumnaz ||
                        lower.getType() == GameObjectType.Thumnaz){
                            police.suspect();
                        }
                    }
            }
        }
    }
    public boolean checkFlours(){
        return flours.isEmpty();
    }
    public boolean haveKey(){
        GameObject key = null;
        for (GameObject item:inventory) {
            if(item.getType() == GameObjectType.Key){
                key = item;
            }
        }
        if(key != null){
            inventory.remove(key);
            return  true;
        }
        return false;
    }


    public GameBoard(int i , int j){
        this.board = new GameObject[i][j];
        for (int x = 0 ; x < i ; x++) {
            for (int y = 0 ; y < j ; y++) {
                this.board[x][y] = new GameObject(x,y);
            }
        }
        this.row = i;
        this.column = j;
        this.factory = new GameObjectFactory();
        this.inventory = new ArrayList<GameObject>();
        this.flours = new ArrayList<Flour>();
        this.polices = new ArrayList<Police>();
    }

    public GameBoard newBoard(GameState state){
        GameBoard newBoard =  new GameBoard(12,18);
        switch (state){
            case Stage1: newBoard.Stage1();break;
            case Stage2: newBoard.Stage2();break;
            case Stage3: newBoard.Stage3();break;
            case Stage4: newBoard.Stage4();break;
            case Stage5: newBoard.Stage5();break;
        }
        return newBoard;
    }

    public void fillWall(){
        for(int x = 0 ; x < row ; x++) {
            addToBoard(x,0,GameObjectType.Wall);
            addToBoard(x,column-1,GameObjectType.Wall);
        }
        for(int y = 1 ; y < column ; y++){
            addToBoard(0,y,GameObjectType.Wall);
            addToBoard(row-1,y,GameObjectType.Wall);
        }
    }
    public void Stage1(){
        this.fillWall();
        addToBoard(1,1,GameObjectType.Flour);
        addToBoard(1,2,GameObjectType.Flour);
        addToBoard(1,3,GameObjectType.Flour);
        addToBoard(1,4,GameObjectType.Flour);
        addToBoard(1,5,GameObjectType.Flour);
        addToBoard(1,10,GameObjectType.Wall);
        addToBoard(1,16,GameObjectType.Thumnaz);

        addToBoard(2,1,GameObjectType.Wall);
        addToBoard(2,2,GameObjectType.Wall);
        addToBoard(2,3,GameObjectType.Wall);
        addToBoard(2,4,GameObjectType.Wall);
        addToBoard(2,5,GameObjectType.Wall);
        addToBoard(2,6,GameObjectType.Wall);
        addToBoard(2,7,GameObjectType.Wall);
        addToBoard(2,10,GameObjectType.Wall);
        addToBoard(2,13,GameObjectType.Bribe);

        addToBoard(3,1,GameObjectType.Flour);
        addToBoard(3,2,GameObjectType.Flour);
        addToBoard(3,3,GameObjectType.Flour);
        addToBoard(3,4,GameObjectType.Flour);
        addToBoard(3,5,GameObjectType.Flour);
        addToBoard(3,7,GameObjectType.Wall);
        addToBoard(3,8,GameObjectType.Door);
        addToBoard(3,9,GameObjectType.Wall);
        addToBoard(3,10,GameObjectType.Wall);
        addToBoard(3,13,GameObjectType.Wall);
        addToBoard(3,14,GameObjectType.Wall);
        addToBoard(3,15,GameObjectType.Wall);
        addToBoard(3,16,GameObjectType.Wall);

        addToBoard(4,1,GameObjectType.Flour);
        addToBoard(4,2,GameObjectType.Flour);
        addToBoard(4,3,GameObjectType.Police);
        addToBoard(4,4,GameObjectType.Police);
        addToBoard(4,5,GameObjectType.Police);
        addToBoard(4,10,GameObjectType.Wall);
        addToBoard(4,14,GameObjectType.Flour);
        addToBoard(4,15,GameObjectType.Flour);
        addToBoard(4,16,GameObjectType.Flour);

        addToBoard(5,1,GameObjectType.Flour);
        addToBoard(5,2,GameObjectType.Flour);
        addToBoard(5,10,GameObjectType.Police);
        addToBoard(5,14,GameObjectType.Flour);
        addToBoard(5,15,GameObjectType.Bribe);
        addToBoard(5,16,GameObjectType.Flour);

        addToBoard(6,3,GameObjectType.Flour);
        addToBoard(6,4,GameObjectType.Flour);
        addToBoard(6,5,GameObjectType.Flour);
        addToBoard(6,7,GameObjectType.Wall);
        addToBoard(6,12,GameObjectType.Bribe);
        addToBoard(6,14,GameObjectType.Flour);
        addToBoard(6,15,GameObjectType.Flour);
        addToBoard(6,16,GameObjectType.Flour);

        addToBoard(7,1,GameObjectType.Wall);
        addToBoard(7,2,GameObjectType.Wall);
        addToBoard(7,3,GameObjectType.Wall);
        addToBoard(7,4,GameObjectType.Wall);
        addToBoard(7,5,GameObjectType.Wall);
        addToBoard(7,6,GameObjectType.Wall);
        addToBoard(7,7,GameObjectType.Wall);
        addToBoard(7,12,GameObjectType.Wall);
        addToBoard(7,14,GameObjectType.Wall);
        addToBoard(7,15,GameObjectType.Wall);
        addToBoard(7,16,GameObjectType.Wall);

        addToBoard(8,2,GameObjectType.Flour);
        addToBoard(8,3,GameObjectType.Flour);
        addToBoard(8,4,GameObjectType.Flour);
        addToBoard(8,12,GameObjectType.Wall);
        addToBoard(8,15,GameObjectType.Flour);

        addToBoard(9,2,GameObjectType.Bribe);
        addToBoard(9,3,GameObjectType.Flour);
        addToBoard(9,4,GameObjectType.Flour);
        addToBoard(8,6,GameObjectType.Wall);
        addToBoard(9,9,GameObjectType.Bribe);
        addToBoard(9,11,GameObjectType.StageDoor);
        addToBoard(9,12,GameObjectType.Wall);
        addToBoard(9,14,GameObjectType.Police);
        addToBoard(9,15,GameObjectType.Flour);
        addToBoard(9,16,GameObjectType.Flour);

        addToBoard(10,2,GameObjectType.Flour);
        addToBoard(10,3,GameObjectType.Flour);
        addToBoard(10,4,GameObjectType.Flour);
        addToBoard(10,6,GameObjectType.Wall);
        addToBoard(10,7,GameObjectType.Flour);
        addToBoard(10,8,GameObjectType.Flour);
        addToBoard(10,9,GameObjectType.Flour);
        addToBoard(10,10,GameObjectType.Flour);
        addToBoard(10,11,GameObjectType.Flour);
        addToBoard(10,12,GameObjectType.Wall);
        addToBoard(10,15,GameObjectType.Flour);
        addToBoard(10,16,GameObjectType.Key);
    }
    //need fixed
    public void Stage2(){
        this.fillWall();
    }
    public void Stage3(){
        this.fillWall();
    }
    public void Stage4(){
        this.fillWall();
    }
    public void Stage5(){
        this.fillWall();
    }
}
