package CPE200.proj.succ.model.staticObject;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;



public class Police extends GameObject {
    private PoliceState state;
    private int bribeCoolDown;
    public void bribed(boolean soundOn){
        bribeCoolDown = 35;
        state = PoliceState.Sleep;
        texture = new Texture("Police/police.png");
        sfx = Gdx.audio.newSound(Gdx.files.internal("sound/tuksin.mp3"));
        play(soundOn);
    }
    public void caught(boolean soundOn){
        this.state = PoliceState.Caught;
        this.texture = new Texture("Police/custody.png");
        sfx = Gdx.audio.newSound(Gdx.files.internal("sound/It's flour.mp3"));
        play(soundOn);
    }
    public void suspect(boolean soundOn){
        this.state = PoliceState.Suspect;this.texture = new Texture("Police/alertpolice.png");
        sfx = Gdx.audio.newSound(Gdx.files.internal("sound/enemy spotted.mp3"));
        play(soundOn);
    }
    public void countdown(){
        this.bribeCoolDown--;
    }

    public boolean check(GameObject obj){
        /*switch (getState()) {
            case Suspect:
                if(obj.getType() == GameObjectType.Bribe || obj.getType() == GameObjectType.Thumnaz) return true;
            case Sleep:
                if(bribeCoolDown > 0)
                    countdown();
                else {
                    if(obj.getType() == GameObjectType.Thumnaz){
                        suspect();
                    }
                }
                return false;
        }*/
        return false;
    }

    public PoliceState getState() {
        return state;
    }
    public int getBribeCoolDown() {
        return bribeCoolDown;
    }

    public Police(int i, int j) {
        super(i, j);
        super.texture = new Texture("Police/police.png");
        super.type = GameObjectType.Police;
        state = PoliceState.Sleep;
    }


}
