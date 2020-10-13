package CPE200.proj.succ.model.item;

import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Flour extends ItemObject{
    private Flour(int i,int j ,Texture texture , GameObjectType type){
        super(i,j);
        super.texture = texture;
        super.type = type;
    }

    public static Flour flour(int i ,int j){
        return new Flour(i,j, new Texture("powder.png"),GameObjectType.Flour);
    }
    public static Flour coke(int i ,int j){
        return new Flour(i,j, new Texture("coke.png"),GameObjectType.Flour);
    }
}
