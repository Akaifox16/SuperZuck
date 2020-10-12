package CPE200.proj.succ.model.item;

import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Key extends ItemObject{
    public Key(int i , int j){
        super(i,j);
        super.texture = new Texture("key.png");
        super.type = GameObjectType.Key;
    }
}
