package com.mandas.tiled2d.utils;

public class IntPair extends Pair<Integer, Integer> implements Comparable<IntPair> {

    public IntPair(int first, int second) {
        super(first, second);
    }
    public IntPair(Integer first, Integer second) {
        super(first, second);
    }

    @Override
    public int compareTo(IntPair o) {
        if (this.first.equals(o.first)) {
            return this.second.compareTo(o.second);
        } else {
            return this.first.compareTo(o.first);
        }
    }
    
}
