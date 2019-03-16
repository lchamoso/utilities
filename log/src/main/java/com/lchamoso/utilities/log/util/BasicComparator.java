package com.lchamoso.utilities.log.util;

import java.lang.reflect.Method;
import java.util.Comparator;

public class BasicComparator implements Comparator {

    private String attribute = new String();

    public BasicComparator(final String attribute) {
        this.attribute = attribute;
    }

    private Object getValueAttribute(final Object obj) {
        try {
            final Method m = obj.getClass().getMethod(
                    "get" + this.attribute.substring(0, 1).toUpperCase() + this.attribute.substring(1), null);
            return m.invoke(obj, null);

        } catch (final Exception ex) {

        }

        return null;
    }

    @Override
    public int compare(final Object o1, final Object o2) {
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if ((o1 != null) && (o2 == null)) {
            return 1;
        }
        if ((o1 == null) && (o2 != null)) {
            return -1;
        }

        final Object valueObject1 = getValueAttribute(o1);
        final Object valueObject2 = getValueAttribute(o2);

        if ((valueObject1 == null) && (valueObject2 == null)) {
            return 0;
        }
        if ((valueObject1 != null) && (valueObject2 == null)) {
            return 1;
        }
        if ((valueObject1 == null) && (valueObject2 != null)) {
            return -1;
        }

        final boolean isComparable1 = valueObject1 instanceof Comparable;
        final boolean isComparable2 = valueObject2 instanceof Comparable;

        if (!isComparable1 && !isComparable2) {
            return 0;
        }
        if (isComparable1 && !isComparable2) {
            return 1;
        }
        if (!isComparable1 && isComparable2) {
            return -1;
        }

        return ((Comparable) valueObject1).compareTo(valueObject2);
    }

}
