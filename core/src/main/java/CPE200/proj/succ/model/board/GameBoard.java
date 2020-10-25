package CPE200.proj.succ.model.board;

import CPE200.proj.succ.Control;
import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectFactory;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.item.Flour;
import CPE200.proj.succ.model.movable.Bomb;
import CPE200.proj.succ.model.movable.ThumnaZ;
import CPE200.proj.succ.model.staticObject.Police;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GameBoard {
    private final GameObject[][] board;
    private final int ROW; // const row of board
    private final int COLUMN; // const column of board
    private List<GameObject> inventory;
    private List<Police> polices;
    private List<Flour> flours;
    private Set<Bomb> bombs;
    private ThumnaZ thumnaz;
    private GameObjectFactory factory;

    //setters
    public void setThumnaz(int x , int y){thumnaz.setCoordinate(x,y);}

    //convert object to specific type
    public void toNull(GameObject gameObject){
        addToBoard(gameObject.row(), gameObject.column(), GameObjectType.NULL);
    }
    public void toThumnaZ(GameObject gameObject){
        addToBoard(gameObject.row(), gameObject.column(),GameObjectType.Thumnaz);
    }
    public void toBribe(GameObject gameObject){
        addToBoard(gameObject.row(), gameObject.column(), GameObjectType.Bribe);
    }
    public void toBomb(GameObject toBomb , Bomb bomb){
        Bomb moved = new Bomb(toBomb.row(), toBomb.column(), bomb);
        board[toBomb.row()][toBomb.column()] = moved;
        bombs.remove(bomb);
        bombs.add(moved);
    }

    //add object to board
    private void addToBoard(int i, int j , GameObjectType type){
        GameObject obj = factory.create(i,j,type);
        board[i][j] = obj;
        switch (type){
            case Thumnaz:
                thumnaz = (ThumnaZ) obj;
                break;
            case Police:
                polices.add((Police) obj);break;
            case Flour:
            case Coke:
                flours.add((Flour)obj);break;
            case Bomb:
                bombs.add((Bomb)obj);break;
        }

    }

    // getters
    public GameObject board(int i , int j){
        if(i >= 0 && i < ROW  && j >= 0 && j < COLUMN) return  board[i][j];
        return null;
    }
    public int getRow() {
        return ROW;
    }
    public int getColumn() {
        return COLUMN;
    }

    public GameObject getThumnaz(){
        return  thumnaz;
    }
    public List<GameObject> getInventory() {
        return inventory;
    }
    public List<Flour> getFlours() {
        return flours;
    }

    //get left,right,upper,lower object
    public GameObject leftObject(GameObject temp){return board(temp.row(), temp.column()-1);}
    public GameObject rightObject(GameObject temp){return board(temp.row(),temp.column()+1);}
    public GameObject upperObject(GameObject temp){return board(temp.row()-1,temp.column());}
    public GameObject lowerObject(GameObject temp){return board(temp.row()+1,temp.column());}

    private boolean canNext(GameObject obj, GameObject next){
        if(obj.getType() == GameObjectType.Door || obj.getType() == GameObjectType.StageDoor) return true;
        return  obj.pickable() || (obj.movable() && (next.getType() == GameObjectType.NULL || next.pickable() ));
    }
    // check if object can go left,right,up,down
    public boolean canLeft(GameObject left){
        return canNext(left,leftObject(left));
    }
    public boolean canRight(GameObject right){
        return canNext(right,rightObject(right));
    }
    public boolean canUp(GameObject upper){
       return canNext(upper,upperObject(upper));
    }
    public boolean canDown(GameObject lower){
        return canNext(lower,lowerObject(lower));
    }

    //convert all flour
    public void convertFlours(){
        for (Flour flour:flours) {
            flour.convert();
        }
    }

    private void checkBombAdjacent(Control game , Set<Bomb> btoom , Bomb bomb , GameObject obj){
        if(bomb.check(obj)){
            if(bomb.getDelay() == 0) {
                switch (obj.getType()) {
                    case Thumnaz:
                        game.toGameOver();
                        break;
                    case Police:
                        toNull(obj);
                        btoom.add(bomb);
                        System.out.println(bomb.toString() + " added!");
                        break;
                }
            }
        }
    }

    public void checkBomb(Control game){
        Set<Bomb> Btoom = new HashSet<Bomb>();
        for (Bomb bomb:bombs) {
            checkBombAdjacent(game,Btoom,bomb,leftObject(bomb));
            checkBombAdjacent(game,Btoom,bomb,rightObject(bomb));
            checkBombAdjacent(game,Btoom,bomb,upperObject(bomb));
            checkBombAdjacent(game,Btoom,bomb,lowerObject(bomb));
            System.out.println(bomb.toString());
        }
        for(Bomb toNull:Btoom){
            toNull(toNull);
        }
        bombs.removeAll(Btoom);
    }

    public void checkPolice(Control game) {
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
                        toNull(thumnaz);
                        police.caught();
                        game.toGameOver();
                        break;
                    }
                case Sleep:
                    if(police.getBribeCoolDown() > 0)
                        police.countdown();
                    else {
                        if(left.getType() == GameObjectType.Thumnaz ||
                        right.getType() == GameObjectType.Thumnaz ||
                        upper.getType() == GameObjectType.Thumnaz ||
                        lower.getType() == GameObjectType.Thumnaz){
                            police.suspect();
                        }
                    }break;
            }
        }
    }
    //check if collect all flour
    public boolean checkFlours(){
        return flours.isEmpty();
    }
    //check if inventory have key
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

    //constructor
    public GameBoard(int i , int j){
        this.board = new GameObject[i][j];
        for (int x = 0 ; x < i ; x++) {
            for (int y = 0 ; y < j ; y++) {
                this.board[x][y] = new GameObject(x,y);
            }
        }
        this.ROW = i;
        this.COLUMN = j;
        this.factory = new GameObjectFactory();
        this.inventory = new ArrayList<GameObject>();
        this.flours = new ArrayList<Flour>();
        this.polices = new ArrayList<Police>();
        this.bombs = new HashSet<Bomb>();
    }
    //new gameboard method
    public GameBoard newBoard(GameState state){
        GameBoard newBoard =  new GameBoard(12,18);
        switch (state){
            case Stage1: newBoard.jsonReader("Map/Stage1.json");;break;
            case Stage2: newBoard.jsonReader("Map/Stage2.json");;break;
            case Stage3: newBoard.jsonReader("Map/Stage3.json");;break;
            case Stage4: newBoard.jsonReader("Map/Stage4.json");;break;
            case Stage5: newBoard.jsonReader("Map/Stage5.json");;break;
        }
        return newBoard;
    }

    private void jsonReader(String stage){
        Json json = new Json();
        ArrayList<String> strings = json.fromJson(ArrayList.class ,String.class, Gdx.files.internal(stage).readString());
        int i = 0 , j;
        for(String s : strings){
            j=0;
            for(Character c : s.toCharArray()){
                switch (c){
                    case 'W':addToBoard(i,j,GameObjectType.Wall);break;
                    case 'F':addToBoard(i,j,GameObjectType.Flour);break;
                    case 'P':addToBoard(i,j,GameObjectType.Police);break;
                    case 'D':addToBoard(i,j,GameObjectType.Door);break;
                    case 'K':addToBoard(i,j,GameObjectType.Key);break;
                    case 'E':addToBoard(i,j,GameObjectType.StageDoor);break;
                    case 'X':addToBoard(i,j,GameObjectType.NULL);break;
                    case 'M':addToBoard(i,j,GameObjectType.Bribe);break;
                    case 'O':addToBoard(i,j,GameObjectType.Thumnaz);break;
                    case 'C':addToBoard(i,j,GameObjectType.Coke);break;
                    case 'A':addToBoard(i,j,GameObjectType.Converter);break;
                    case 'B':addToBoard(i,j,GameObjectType.Bomb);break;
                }
                j++;
            }
            i++;
        }
    }
}
