package CPE200.proj.succ.model.staticObject;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;



public class Police extends GameObject {
    private PoliceState state;
    private int bribeCoolDown;

    public void bribed(){
        setBribeCoolDown(20);
        state = PoliceState.Sleep;
        texture = new Texture("red boi.png");
    }

    public void setBribeCoolDown(int bribeCoolDown) {
        this.bribeCoolDown = bribeCoolDown;
    }
    public void suspect(){this.state = PoliceState.Suspect;this.texture = new Texture("alertpolice.png");}

    public PoliceState getState() {
        return state;
    }
    public int getBribeCoolDown() {
        return bribeCoolDown;
    }

    public Police(int i, int j) {
        super(i, j);
        super.texture = new Texture("red boi.png");
        super.type = GameObjectType.Police;
        state = PoliceState.Sleep;
    }


}
