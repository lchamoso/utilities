package com.lchamoso.utilities.log.util;

import java.lang.reflect.Field;
import java.util.Comparator;

public class FieldComparator implements Comparator<Field>{

    @Override
    public int compare(final Field field1, final Field field2) {

        if ((field1 == null) && (field2 == null)) {
            return 0;
        }
        if ((field1 != null) && (field2 == null)) {
            return 1;
        }
        if ((field1 == null) && (field2 != null)) {
            return -1;
        }

        if ((field1.getName() == null) && (field2.getName() == null)) {
            return 0;
        }
        if ((field1.getName() != null) && (field2.getName() == null)) {
            return 1;
        }
        if ((field1.getName() == null) && (field2.getName() != null)) {
            return -1;
        }

        return field1.getName().compareTo(field2.getName());
    }

}
