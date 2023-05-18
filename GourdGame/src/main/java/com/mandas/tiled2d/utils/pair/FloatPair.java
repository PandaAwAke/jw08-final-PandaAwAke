package com.mandas.tiled2d.utils.pair;

public class FloatPair extends Pair<Float, Float> implements Comparable<FloatPair> {

    public FloatPair(float first, float second) {
        super(first, second);
    }
    public FloatPair(Float first, Float second) {
        super(first, second);
    }
    public FloatPair(IntPair intPair) {
        super(intPair.first.floatValue(), intPair.second.floatValue());
    }

    @Override
    public int compareTo(FloatPair o) {
        if (this.first.equals(o.first)) {
            return this.second.compareTo(o.second);
        } else {
            return this.first.compareTo(o.first);
        }
    }
    
}
