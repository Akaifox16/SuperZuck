package CPE200.proj.succ.model.item;

import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Flour extends ItemObject{
    public Flour(int i,int j){
        super(i,j);
        super.texture = new Texture("powder.png");
        super.type = GameObjectType.Flour;
    }
}
