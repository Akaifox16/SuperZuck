package CPE200.proj.succ.model;

import com.badlogic.gdx.graphics.Texture;

public class GameObject {
    protected Texture texture;
    protected GameObjectType type;
    protected int row ,column;

    public boolean movable(){ return false;}
    public boolean pickable(){return false;}


    public GameObject(int i , int j){
        row = i;
        column = j;
        type = GameObjectType.NULL;
    }

    public  void setCoordinate(int i , int j){
        setColumn(j);
        setRow(i);
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public void setRow(int row) {
        this.row = row;
    }

    public int column() {
        return column;
    }
    public int row() {
        return row;
    }
    public Texture getTexture() {
        return texture;
    }
    public GameObjectType getType() {
        return type;
    }

}
