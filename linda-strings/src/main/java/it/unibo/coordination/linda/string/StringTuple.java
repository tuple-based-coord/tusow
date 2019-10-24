package it.unibo.coordination.linda.string;

import it.unibo.coordination.linda.core.Tuple;

import java.util.Objects;

public interface StringTuple extends Tuple {

    @Override
    String getValue();

    static StringTuple of(String string) {
        return new StringTupleImpl(string);
    }

    default int compareTo(StringTuple o) {
        return getValue().compareTo(o.getValue());
    }

    static boolean equals(StringTuple t1, StringTuple t2) {
        if (t1 == t2) return true;
        if (t1 == null || t2 == null) return false;
        return Objects.equals(t1.getValue(), t2.getValue());
    }

    static int hashCode(StringTuple t) {
        return Objects.hashCode(t.getValue());
    }


}
