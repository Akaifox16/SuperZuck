package CPE200.proj.succ.model.staticObject;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Door extends GameObject {

    public Door(int i, int j , Texture texture , GameObjectType type) {
        super(i, j);
        super.texture = texture;
        super.type = type;
        if(type == GameObjectType.StageDoor) sfx = Gdx.audio.newSound(Gdx.files.internal("sound/next stage.mp3"));
        else sfx = Gdx.audio.newSound(Gdx.files.internal("sound/door.mp3"));
    }


    public static Door StageDoor(int i , int j){
        return new Door(i,j ,new Texture("stageDoor.png"),GameObjectType.StageDoor);
    }

    public static Door door(int i, int j){
        return new Door(i,j,new Texture("lockedDoor.png"),GameObjectType.Door);
    }
}
