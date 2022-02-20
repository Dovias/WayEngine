package lt.wayout.minecraft.plugin.wayengine.util;

import java.io.Serializable;

public class Pair<A, B> implements Serializable {
    private final A a;
    private final B b;

    public Pair(A firstValue, B secondValue) {
        this.a = firstValue;
        this.b = secondValue;
    }

    public A getFirstValue() {
        return this.a;
    }

    public B getSecondValue() {
        return this.b;
    }
}
