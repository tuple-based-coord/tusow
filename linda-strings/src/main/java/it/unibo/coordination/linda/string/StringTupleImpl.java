package it.unibo.coordination.linda.string;

import java.util.Objects;

class StringTupleImpl implements StringTuple {

    private final String tuple;

    StringTupleImpl(String tuple) {
        this.tuple = Objects.requireNonNull(tuple);
    }

    @Override
    public String getTuple() {
        return tuple;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof StringTuple && StringTuple.equals(this, (StringTuple) o);
    }

    @Override
    public int hashCode() {
        return StringTuple.hashCode(this);
    }

    @Override
    public String toString() {
        return "\"" + tuple + "\"";
    }
}
