package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.item.ItemObject;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class ThumnaZ extends GameObject {
    private List<ItemObject> inventory;
    public ThumnaZ(int i,int j){
        super(i,j);
        inventory = new ArrayList<ItemObject>();
        super.texture = new Texture("suit_man.png");
        super.type = GameObjectType.Thumnaz;
    }

}
