package CPE200.proj.succ.model.staticObject;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Door extends GameObject {
    public Door(int i, int j , Texture texture , GameObjectType type) {
        super(i, j);
        super.texture = texture;
        super.type = type;
    }

    public static Door StageDoor(int i , int j){
        return new Door(i,j ,new Texture("stageDoor.png"),GameObjectType.StageDoor);
    }

}
