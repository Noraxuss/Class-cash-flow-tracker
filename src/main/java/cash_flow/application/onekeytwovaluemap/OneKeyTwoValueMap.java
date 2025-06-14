package cash_flow.application.onekeytwovaluemap;

import java.util.HashMap;
import java.util.Map;

public class OneKeyTwoValueMap<K, V1, V2> {
    private final Map<K, Pair<V1, V2>> map;

    public OneKeyTwoValueMap() {
        this.map = new HashMap<>();
    }

    public void put(K key, V1 value1, V2 value2) {
        map.put(key, new Pair<>(value1, value2));
    }

    public Pair<V1, V2> get(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    public Map<K, Pair<V1, V2>> getMap() {
        return map;
    }
}