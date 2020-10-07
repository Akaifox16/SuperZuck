package CPE200.proj.succ.model.movable;

import CPE200.proj.succ.model.GameObject;

public class MovableObject extends GameObject {
    public MovableObject(int i, int j) {
        super(i, j);
    }

    @Override
    public boolean movable() {
        return true;
    }


}
