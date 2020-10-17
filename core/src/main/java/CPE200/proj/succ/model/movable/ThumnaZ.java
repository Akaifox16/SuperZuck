package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.item.ItemObject;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class ThumnaZ extends GameObject {
    public ThumnaZ(int i,int j){
        super(i,j);
        super.texture = new Texture("Player/Thumnaz.png");
        super.type = GameObjectType.Thumnaz;
    }

}
