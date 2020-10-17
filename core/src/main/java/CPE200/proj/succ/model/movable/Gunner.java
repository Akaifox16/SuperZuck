package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Gunner extends MovableObject{
    private int cooldown;
    private boolean enable;

    public Gunner(int i, int j) {
        super(i, j);
        texture = new Texture("Gunner.png");
        type = GameObjectType.Gunner;
    }
}
