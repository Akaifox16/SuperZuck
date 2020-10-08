package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameObject;
import com.badlogic.gdx.graphics.Texture;

public class ThumnaZ extends GameObject {
    public ThumnaZ(int i,int j){
        super(i,j);
        super.texture = new Texture("suit_man.png");
        super.type = GameObjectType.Thumnaz;
    }


}
