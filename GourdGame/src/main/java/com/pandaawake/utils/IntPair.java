package com.pandaawake.utils;

public class IntPair extends Pair<Integer, Integer> implements Comparable<IntPair> {

    public IntPair(Integer first, Integer second) {
        super(first, second);
    }

    @Override
    public int compareTo(IntPair o) {
        if (this.first == o.first) {
            return this.second.compareTo(o.second);
        } else {
            return this.first.compareTo(o.first);
        }
    }
    
}
