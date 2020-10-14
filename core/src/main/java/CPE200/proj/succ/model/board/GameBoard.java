package CPE200.proj.succ.model.board;

import CPE200.proj.succ.Control;
import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.item.Flour;
import CPE200.proj.succ.model.item.FlourConverter;
import CPE200.proj.succ.model.item.Key;
import CPE200.proj.succ.model.staticObject.Door;
import CPE200.proj.succ.model.staticObject.Police;
import CPE200.proj.succ.model.staticObject.Wall;
import CPE200.proj.succ.model.movable.Bribe;
import CPE200.proj.succ.model.movable.ThumnaZ;

import java.util.ArrayList;
import java.util.List;


public class GameBoard {
    private final GameObject[][] board;
    private List<GameObject> inventory;
    private int thumnazX , thumnazY; // index ของ main character
    private List<Police> polices;
    private List<Flour> flours;
    private final int row;
    private final int column;

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
    public void convert(Flour flour){
        flour.convert();
        this.board[flour.row()][flour.column()] = flour;
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

    public void addPolice(int i , int j){
        Police police = new Police(i, j);
        board[i][j] = police;
        polices.add(police);
    }
    public void addFlour(int i , int j){
        Flour flour = Flour.flour(i,j);
        board[i][j] = flour;
        flours.add(flour);
    }
    public void addCoke(int i , int j){
        Flour coke = Flour.coke(i,j);
        board[i][j] = coke;
        flours.add(coke);
    }

    public void convertFlours(){
        for (Flour flour:flours) {
            flour.convert();
        }
    }
    public void checkPolice(Control game){
        for (Police police:polices) {
            GameObject left = leftObject(police);
            GameObject right = rightObject(police);
            GameObject upper = upperObject(police);
            GameObject lower = lowerObject(police);
            switch (police.getState()) {
                case Suspect:
                    if(left.getType() != GameObjectType.NULL){
                        switch (left.getType()) {
                            case Bribe:
                                toNull(left);
                                police.bribed();break;
                            case Thumnaz:
                                game.setCurrentState(GameState.GameOver);
                        }
                    }else if(right.getType() != GameObjectType.NULL){
                        switch (right.getType()) {
                            case Bribe:
                                toNull(right);
                                police.bribed();break;
                            case Thumnaz:
                                game.setCurrentState(GameState.GameOver);
                        }
                    }else if(upper.getType() != GameObjectType.NULL){
                        switch (upper.getType()) {
                            case Bribe:
                                toNull(upper);
                                police.bribed();break;
                            case Thumnaz:
                                game.setCurrentState(GameState.GameOver);
                        }
                    }else if(lower.getType() != GameObjectType.NULL){
                        switch (lower.getType()) {
                            case Bribe:
                                toNull(lower);
                                police.bribed();break;
                            case Thumnaz:
                                game.setCurrentState(GameState.GameOver);
                        }
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
            this.board[x][0] = new Wall(x,0);
            this.board[x][column-1] = new Wall(x,column-1);
        }
        for(int y = 1 ; y < column ; y++){
            this.board[0][y] = new Wall(0,y);
            this.board[row-1][y] = new Wall(row-1,y);
        }
    }
    public void Stage1(){
        this.board[3][4] = new ThumnaZ(3,4);
        this.thumnazX = 3;
        this.thumnazY = 4;
        this.fillWall();

        addFlour(2,13);
        this.board[2][14] = new Key(2,14);

        this.board[2][4] = new Wall(2,4);
        this.board[2][5] = new Wall(2,5);
        this.board[2][8] = new Wall(2,8);
        this.board[2][9] = new Wall(2,9);
        this.board[3][5] = new Wall(3,5);
        this.board[3][8] = new Wall(3,8);
        this.board[4][1] = new Wall(4,1);
        this.board[4][2] = new Wall(4,2);
        this.board[4][10] = new Wall(4,10);
        this.board[5][9] = new Wall(5,9);
        this.board[5][10] = new Wall(5,10);
        this.board[7][7] = new Wall(7,7);
        this.board[9][1] = new Wall(9,1);
        this.board[9][2] = new Wall(9,2);
        this.board[9][4] = new Wall(9,4);
        this.board[9][5] = new Wall(9,5);
        this.board[9][9] = new Wall(9,9);
        this.board[10][8] = new Wall(10,8);
        this.board[11][4] = new Wall(11,4);
        this.board[11][1] = new Wall(11,1);
        this.board[11][2] = new Wall(11,2);
        this.board[11][3] = new Wall(11,3);

        this.board[2][2] = new Bribe(2,2);
        this.board[6][6] = new Bribe(6,6);
        this.board[9][3] = Door.door(9,3);
        this.board[10][3] = Door.StageDoor(10,3);
        this.addPolice(10,15);
    }
    //need fixed
    public void Stage2(){
        this.board[3][6] = new ThumnaZ(3,6);
        this.thumnazX = 3;
        this.thumnazY = 6;
        this.fillWall();
        addFlour(6,6);
        this.board[10][6] = Door.StageDoor(10,6);
    }
    public void Stage3(){
        this.board[3][5] = new ThumnaZ(3,5);
        this.thumnazX = 3;
        this.thumnazY = 5;
        this.fillWall();
        addFlour(6,5);
        addFlour(7,5);
        addFlour(8,5);
        addCoke(9,5);
        this.board[9][6] = new FlourConverter(9,6);
        this.board[10][5] = Door.StageDoor(10,5);
    }
    public void Stage4(){
        this.board[3][2] = new ThumnaZ(3,2);
        this.thumnazX = 3;
        this.thumnazY = 2;
        this.fillWall();
        addFlour(6,2);
        this.board[10][2] = Door.StageDoor(10,2);
    }
    public void Stage5(){
        this.board[3][10] = new ThumnaZ(3,10);
        this.thumnazX = 3;
        this.thumnazY = 10;
        this.fillWall();
        addFlour(6,10);
        this.board[10][10] = Door.StageDoor(10,10);
    }
}
