package CPE200.proj.succ.model.staticObject;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Wall extends GameObject {

    public Wall(int i , int j){
        super(i,j);
        super.texture = new Texture("brick_wall.png");
        super.type = GameObjectType.Wall;
    }

}
