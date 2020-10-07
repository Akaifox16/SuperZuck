package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Bribe extends MovableObject {
    public Bribe(int i, int j) {
        super(i, j);
        super.texture = new Texture("bribe.png");
        super.type = GameObjectType.Bribe;
    }
}
