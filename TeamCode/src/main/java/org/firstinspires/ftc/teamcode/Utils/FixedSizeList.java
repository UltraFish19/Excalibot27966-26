package org.firstinspires.ftc.teamcode.Utils;

import java.util.ArrayList;

public class FixedSizeList<T> extends ArrayList<T> {
    private final int MaxSize;

    public FixedSizeList(int MaxSize) {
        this.MaxSize = MaxSize;
    }


    public int GetMaxSize() {
        return MaxSize;
    }

    @Override
    public boolean add(T element) {
        if (size() >= MaxSize) {
            remove(0); // If list size is too big remove the first element
        }
        return super.add(element);
    }
}
