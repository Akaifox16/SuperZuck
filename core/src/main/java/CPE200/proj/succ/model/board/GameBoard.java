package CPE200.proj.succ.model.board;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.staticObject.Wall;
import CPE200.proj.succ.model.movable.Bribe;
import CPE200.proj.succ.model.movable.ThumnaZ;

public class GameBoard {
    private final GameObject[][] board;
    private int thumnazX , thumnazY; // index ของ main character
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
    public ThumnaZ getThumnaz(){
        return (ThumnaZ) board(thumnazX,thumnazY);
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

    public GameBoard(int i , int j){
        board = new GameObject[i][j];
        for (int x = 0 ; x < i ; x++) {
            for (int y = 0 ; y < j ; y++) {
                board[x][y] = new GameObject(x,y);
            }
        }

        this.row = i;
        this.column = j;
    }

    public void Stage1(){
        board[row-2][column-5] = new ThumnaZ(row-2,column-5);
        thumnazX = row-2;
        thumnazY = column-5;

        for(int x = 0 ; x < row ; x++) {
            board[x][0] = new Wall(x,0);
            board[x][column-1] = new Wall(x,column-1);
        }
        for(int y = 1 ; y < column ; y++){
            board[0][y] = new Wall(0,y);
            board[row-1][y] = new Wall(row-1,y);
        }

        board[2][4] = new Wall(2,4);
        board[3][4] = new Wall(3,4);
        board[3][5] = new Wall(3,5);
        board[3][6] = new Wall(3,6);
        board[3][7] = new Wall(3,7);
        board[4][4] = new Wall(4,4);
        board[5][4] = new Wall(5,4);
        board[1][4] = new Wall(1,4);
        board[2][8] = new Wall(2,8);
        board[2][7] = new Wall(2,7);
        board[1][9] = new Wall(1,9);
        board[1][8] = new Wall(1,8);
        board[1][7] = new Wall(1,7);
        board[3][1] = new Wall(3,1);
        board[2][1] = new Wall(2,1);
        board[2][9] = new Wall(2,9);
        board[2][4] = new Wall(2,4);
        board[4][1] = new Wall(4,1);
        board[4][8] = new Wall(4,8);
        board[4][7] = new Wall(4,7);
        board[5][1] = new Wall(5,1);
        board[5][3] = new Wall(5,3);
        board[3][2] = new Wall(3,2);
        board[5][5] = new Bribe(5,5);
        board[6][6] = new Bribe(6,6);

    }
}
