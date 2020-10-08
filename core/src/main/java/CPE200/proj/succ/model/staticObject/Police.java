package CPE200.proj.succ.model.staticObject;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

enum PoliceState {
    Sleep,
    Suspect
}

public class Police extends GameObject {
    private PoliceState state;
    //private boolean bribed;
    private int bribeCoolDown;



    public Police(int i, int j) {
        super(i, j);
        super.texture = new Texture("red boi.png");
        super.type = GameObjectType.Police;
        state = PoliceState.Sleep;
    }


}
