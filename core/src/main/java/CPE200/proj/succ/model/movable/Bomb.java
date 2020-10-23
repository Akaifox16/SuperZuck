package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.graphics.Texture;

public class Bomb extends MovableObject{
    private boolean enable;
    private int delay;

    public boolean isEnable() {
        return enable;
    }
    public int getDelay() {
        return delay;
    }
    public void countdown(){
        delay--;
    }

    public boolean check(GameObject adjacent){
        if(enable){
            if(delay == 0) return adjacent.getType() == GameObjectType.Police || adjacent.getType() == GameObjectType.Thumnaz;
            else countdown();
        }
        return false;
    }

    public Bomb(int i, int j , Bomb bomb) {
        super(i, j);
        texture = new Texture("c4.png");
        type = GameObjectType.Bomb;

        if(bomb != null) {
            delay = bomb.delay;
            enable = true;
        }
        else {
            delay = 144;
            enable = false;
        }

    }
}
