package CPE200.proj.succ.model;

import CPE200.proj.succ.model.item.Flour;
import CPE200.proj.succ.model.item.FlourConverter;
import CPE200.proj.succ.model.item.Key;
import CPE200.proj.succ.model.movable.Bribe;
import CPE200.proj.succ.model.movable.Bomb;
import CPE200.proj.succ.model.movable.ThumnaZ;
import CPE200.proj.succ.model.staticObject.Door;
import CPE200.proj.succ.model.staticObject.Police;
import CPE200.proj.succ.model.staticObject.Wall;

public class GameObjectFactory {
    public GameObject create(int i,int j,GameObjectType type){
        switch (type){
            case Coke:
                return Flour.coke(i,j);
            case Flour:
                return Flour.flour(i,j);
            case Converter:
                return new FlourConverter(i,j);
            case Door:
                return Door.door(i,j);
            case StageDoor:
                return Door.StageDoor(i,j);
            case Key:
                return new Key(i,j);
            case Bribe:
                return new Bribe(i,j);
            case Thumnaz:
                return new ThumnaZ(i,j);
            case Police:
                return new Police(i,j);
            case NULL:
                return new GameObject(i,j);
            case Wall:
                return new Wall(i,j);
            case Bomb:
                return new Bomb(i,j,null);
        }
        return null;
    }
}
