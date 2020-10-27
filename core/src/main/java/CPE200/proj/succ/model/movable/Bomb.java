package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Bomb extends MovableObject{
    private boolean enable;
    private int delay;

    public int getDelay() {
        return delay;
    }
    public void countdown(){
        delay--;
    }

    public boolean check(GameObject adjacent){
        return adjacent.getType() == GameObjectType.Police || adjacent.getType() == GameObjectType.Thumnaz;
    }

    public boolean isEnable() {
        return enable;
    }

    public Bomb(int i, int j , Bomb bomb) {
        super(i, j);
        texture = new Texture("c4.png");
        type = GameObjectType.Bomb;
        sfx = Gdx.audio.newSound(Gdx.files.internal("sound/btoom.mp3"));
        if(bomb != null) {
            delay = bomb.delay;
            enable = true;
        }
        else {
            delay = 30;
            enable = false;
        }

    }
}
