package CPE200.proj.succ.model;

import com.badlogic.gdx.graphics.Texture;

public class Wall extends GameObject {

    public Wall(int i , int j){
        super(i,j);
        super.texture = new Texture("Wall.png");
        super.type = GameObjectType.Wall;
    }

}
