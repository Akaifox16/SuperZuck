package CPE200.proj.succ.model.board;

import CPE200.proj.succ.Control;
import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.item.Flour;
import CPE200.proj.succ.model.staticObject.Police;
import CPE200.proj.succ.model.staticObject.Wall;
import CPE200.proj.succ.model.movable.Bribe;
import CPE200.proj.succ.model.movable.ThumnaZ;

import java.util.ArrayList;
import java.util.List;


public class GameBoard {
    private final GameObject[][] board;
    private int thumnazX , thumnazY; // index ของ main character
    private List<Police> polices;
    private final int row;
    private final int column;

    public GameObject board(int i , int j){
        return  board[i][j];
    }
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
    public GameObject leftObject(GameObject temp){return board(temp.row(), temp.column()-1);}
    public GameObject rightObject(GameObject temp){return board(temp.row(),temp.column()+1);}
    public GameObject upperObject(GameObject temp){return board(temp.row()-1,temp.column());}
    public GameObject lowerObject(GameObject temp){return board(temp.row()+1,temp.column());}
    public boolean canLeft(GameObject left){
        if(left.movable())
            return leftObject(left).getType() == GameObjectType.NULL;
        return false;
    }
    public boolean canRight(GameObject right){
        if(right.movable()){
            return rightObject(right).getType() == GameObjectType.NULL;
        }
        return false;
    }
    public boolean canUp(GameObject upper){
        if(upper.movable())
            return upperObject(upper).getType() == GameObjectType.NULL;
        return false;
    }
    public boolean canDown(GameObject lower){
        if(lower.movable())
            return lowerObject(lower).getType() == GameObjectType.NULL;
        return false;
    }
    public void addPolice(int i , int j){
        Police police = new Police(i, j);
        board[i][j] = police;
        polices.add(police);
    }

    public void checkPolice(Control game){
        for (Police police:polices) {
            GameObject left = leftObject(police);
            GameObject right = rightObject(police);
            GameObject upper = upperObject(police);
            GameObject lower = lowerObject(police);
            switch (police.getState()) {
                case Suspect:
                    switch (left.getType()) {
                        case Bribe:
                            toNull(left);
                            police.bribed();break;
                        case Thumnaz:
                            game.setCurrentState(GameState.GameOver);
                    }
                    switch (right.getType()) {
                        case Bribe:
                            toNull(right);
                            police.bribed();break;
                        case Thumnaz:
                            game.setCurrentState(GameState.GameOver);
                    }
                    switch (upper.getType()) {
                        case Bribe:
                            toNull(upper);
                            police.bribed();break;
                        case Thumnaz:
                            game.setCurrentState(GameState.GameOver);
                    }
                    switch (lower.getType()) {
                        case Bribe:
                            toNull(lower);
                            police.bribed();break;
                        case Thumnaz:
                            game.setCurrentState(GameState.GameOver);
                    }
                case Sleep:
                    if(police.getBribeCoolDown() > 0)
                        police.setBribeCoolDown(police.getBribeCoolDown() - 1);
                    else {
                        switch (left.getType()) {
                            case Thumnaz:
                                police.suspect();
                        }
                        switch (right.getType()) {
                            case Thumnaz:
                                police.suspect();
                        }
                        switch (upper.getType()) {
                            case Thumnaz:
                                police.suspect();
                        }
                        switch (lower.getType()) {
                            case Thumnaz:
                                police.suspect();
                        }
                    }
            }
        }
    }

    public GameBoard(int i , int j){
        board = new GameObject[i][j];
        for (int x = 0 ; x < i ; x++) {
            for (int y = 0 ; y < j ; y++) {
                board[x][y] = new GameObject(x,y);
            }
        }
        this.row = i;
        this.column = j;

        polices = new ArrayList<Police>();
    }

    public void Stage1(){
        board[3][4] = new ThumnaZ(3,4);
        thumnazX = 3;
        thumnazY = 4;

        for(int x = 0 ; x < row ; x++) {
            board[x][0] = new Wall(x,0);
            board[x][column-1] = new Wall(x,column-1);
        }
        for(int y = 1 ; y < column ; y++){
            board[0][y] = new Wall(0,y);
            board[row-1][y] = new Wall(row-1,y);
        }
        board[2][13] = new Flour(2,13);
        board[2][4] = new Wall(2,4);
        board[2][5] = new Wall(2,5);
        board[2][8] = new Wall(2,8);
        board[2][9] = new Wall(2,9);
        board[3][5] = new Wall(3,5);
        board[3][8] = new Wall(3,8);
        board[4][1] = new Wall(4,1);
        board[4][2] = new Wall(4,2);
        board[4][10] = new Wall(4,10);
        board[5][9] = new Wall(5,9);
        board[5][10] = new Wall(5,10);
        board[7][7] = new Wall(7,7);
        board[9][1] = new Wall(9,1);
        board[9][2] = new Wall(9,2);
        board[9][4] = new Wall(9,4);
        board[9][5] = new Wall(9,5);
        board[9][9] = new Wall(9,9);
        board[10][8] = new Wall(10,8);
        board[11][4] = new Wall(11,4);
        board[11][1] = new Wall(11,1);
        board[11][2] = new Wall(11,2);
        board[11][3] = new Wall(11,3);
        board[2][2] = new Bribe(2,2);
        board[6][6] = new Bribe(6,6);
        addPolice(10,15);
    }
}
