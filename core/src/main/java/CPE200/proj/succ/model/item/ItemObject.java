package CPE200.proj.succ.model.item;

import CPE200.proj.succ.model.GameObject;

public class ItemObject extends GameObject {
    public ItemObject(int i, int j) {
        super(i, j);
    }

    @Override
    public boolean pickable() {
        return true;
    }
}
