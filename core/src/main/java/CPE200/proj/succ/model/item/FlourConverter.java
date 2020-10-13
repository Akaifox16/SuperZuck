package CPE200.proj.succ.model.item;

import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class FlourConverter extends ItemObject{

    public FlourConverter(int i, int j) {
        super(i, j);
        super.texture = new Texture("converter.png");
        super.type = GameObjectType.Converter;
    }
}
