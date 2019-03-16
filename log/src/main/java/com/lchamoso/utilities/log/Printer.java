package com.lchamoso.utilities.log;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lchamoso.utilities.log.util.BasicComparator;
import com.lchamoso.utilities.log.util.FieldComparator;

public class Printer {
	
	private static Printer PRINTER = new Printer();

    public static String print(final Object object) {
        final StringBuilder sb = new StringBuilder();
        PRINTER.printObject(object, sb, 0);
        return sb.toString();
    }


    private void printSpaces(final StringBuilder sb, final int numSpaces) {
        for (int i = 0; i < numSpaces; i++) {
            sb.append(" ");
        }
    }

    private void logString(final StringBuilder sb, final int numSpaces, final String toLog) {
        printSpaces(sb, numSpaces);
        sb.append(toLog);
    }

    public void printObject(final Object obj, final StringBuilder sb, final int numSpaces) {
        if (obj == null) {
            logString(sb, numSpaces, "NULL_VALUE \n");
        } else if (obj instanceof List) {
            final Iterator it = ((List) obj).iterator();

            final List sorted = getSortedList((List) obj);
            logString(sb, numSpaces, obj.getClass().getName() + "  { \n");
            final Iterator itSorted = sorted.iterator();
            while (itSorted.hasNext()) {
                logString(sb, numSpaces, "   element { \n");
                printObject(itSorted.next(), sb, numSpaces + 5);
                logString(sb, numSpaces, "   } \n");
            }
            logString(sb, numSpaces, "} \n");

        } else if (obj.getClass().isArray()) {
            final Object[] array = (Object[]) obj;
            logString(sb, numSpaces, obj.getClass().getName() + "  { \n");
            for (int i = 0, a = array.length; i < a; i++) {
                logString(sb, numSpaces, "   element { \n");
                printObject(array[i], sb, numSpaces + 5);
                logString(sb, numSpaces, "   } \n");
            }
            logString(sb, numSpaces, "} \n");

        } else if (obj instanceof Map) {
            final List sortedKeys = getSortedList(new ArrayList(((Map) obj).keySet()));
            final Iterator itKeys = sortedKeys.iterator();
            logString(sb, numSpaces, obj.getClass().getName() + "  { \n");
            while (itKeys.hasNext()) {
                final Object key = itKeys.next();

                logString(sb, numSpaces, "   keyElement { \n");
                printObject(key, sb, numSpaces + 5);
                logString(sb, numSpaces, "   }\n");

                final Object valueObject = ((Map) obj).get(key);

                logString(sb, numSpaces, "   valueElement { \n");
                printObject(valueObject, sb, numSpaces + 5);
                logString(sb, numSpaces, "   }\n");
            }
            logString(sb, numSpaces, "} \n");
        }
        else if (obj.getClass().getName().startsWith("java.")) {
            logString(sb, numSpaces, obj.getClass().getName() + " { " + obj + " } \n");
        } else if (obj.getClass().isEnum()) {
            logString(sb, numSpaces, obj.getClass().getName() + " { " + obj + " } \n");
        }
        else {
            logString(sb, numSpaces, obj.getClass().getName() + "  [ \n");
            final Field[] fieldsDeclared = obj.getClass().getDeclaredFields();
            final List<Field> fieldList = Arrays.asList(fieldsDeclared);
            Collections.sort(fieldList, new FieldComparator());

            for (int i = 0, a = fieldList.size(); i < a; i++) {
                final Field field = fieldList.get(i);

                final String name = field.getName();
                if (!("$VALUES".equalsIgnoreCase(name))) {
                    try {
                        field.setAccessible(true);
                        logString(sb, numSpaces + 2, name + " \n");
                        final Object value = field.get(obj);

                        printObject(value, sb, numSpaces + 5);

                    } catch (final Exception ex) {
                        printObject("error", sb, numSpaces + 5);
                    }
                }
            }
            logString(sb, numSpaces, "] \n");
        }
    }


    private List getSortedList(final List lista) {
        if ((lista == null) || lista.isEmpty()) {
            return lista;
        }
        final Object obj = lista.get(0);
        if (obj instanceof Comparable) {
            Collections.sort(lista);
        } else {
            // find field to compare. Default: Use id field or field with similar name
            final Field[] fields = obj.getClass().getDeclaredFields();
            final List<String> fieldCandidate = new ArrayList<String>();
            for (int i = 0, a = fields.length; i < a; i++) {
                if (fields[i].getName().startsWith("id")) {
                    fieldCandidate.add(fields[i].getName());
                }
            }
            String sortBy = null;
            Collections.sort(fieldCandidate);
            if (!fieldCandidate.isEmpty()) {
                sortBy = fieldCandidate.get(0);
            }
            if (sortBy != null) {
                Collections.sort(lista, new BasicComparator(sortBy));
            }
        }
        return lista;
    }

    public static void main(final String args[]) throws Exception {
        System.out.println(Printer.print(createDummyObject()));
    }


    private static Object createDummyObject() throws Exception {
      return new Integer(12);

    }
}
