package com.willpower.banner;

public class IModel<E> {
    private E data;
    private int type;

    public IModel(E data, int type) {
        this.data = data;
        this.type = type;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
