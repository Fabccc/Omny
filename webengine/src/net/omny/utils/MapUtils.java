package net.omny.utils;

import java.util.Map;
import java.util.function.UnaryOperator;

public class MapUtils {
    

    public static <K, V> Map.Entry<K, V> changeEntry(Map.Entry<K, V> entry, UnaryOperator<K> changer){
        return Map.entry(changer.apply(entry.getKey()), entry.getValue());
    } 

}
