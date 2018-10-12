package org.waglesid.structs.parenttree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParentTreeSet<K, V> {

    private final ParentTree<K> tree = new ParentTree<>();
    private final Function<V, K> keyFunc;
    private final Map<K, V> map = new HashMap<>();

    public ParentTreeSet(Function<V, K> keyFunc) {
        this.keyFunc = keyFunc;
    }

    public K add(V data) {
        final K key = keyFunc.apply(data);
        map.put(key, data);
        tree.add(key);
        return key;
    }

    public K updateOrAdd(V data) {
        return add(data);
    }

    private int deleteKey(K key) {
        return tree.remove(key) != null ? 1: 0;
    }

    public int delete(V data) {
        final K key = keyFunc.apply(data);
        return deleteKey(key);
    }

    public int deleteAll(List<V> data) {
        return data.stream().mapToInt(this::delete).sum();
    }

    public void addParent(V data, V parent) {
        final K parentKey = add(parent);
        final K key = keyFunc.apply(data);
        map.put(key, data);
        tree.addParent(key, parentKey);
    }

    public boolean contains(V data) {
        final K key = keyFunc.apply(data);
        return map.containsKey(key) && tree.get(key) != null;
    }

    public boolean containsAndEquals(V data) {
        return contains(data) && map.get(keyFunc.apply(data)).equals(data);
    }

    public List<V> getFullTree(V data) {
        final Set<V> set = new HashSet<>();
        set.addAll(getParentTreeAt(data));
        set.addAll(getTreeAt(data));
        return new ArrayList<>(set);
    }

    public List<V> getParentTreeAt(V data) {
        if (!contains(data))
            return Collections.emptyList();
        final K key = keyFunc.apply(data);
        final List<K> allParents = tree.allParents(key);
        return allParents.stream().map(map::get).collect(Collectors.toList());
    }

    public List<V> getTreeAt(V data) {
        if (!contains(data))
            return Collections.emptyList();
        final K key = keyFunc.apply(data);
        final List<K> allChildren = tree.allChildren(key);
        final List<K> allKeys = new ArrayList<>(allChildren.size() + 1);
        allKeys.add(key);
        allKeys.addAll(allChildren);
        return allKeys.stream().map(map::get).collect(Collectors.toList());
    }

    public List<V> deleteTreeAt(V data) {
        final List<V> keys = getTreeAt(data);
        final int deletedCount = keys.stream().mapToInt(this::delete).sum();
        return keys.stream().map(map::get).collect(Collectors.toList());
    }

    public V parentOf(V data) {
        final K key = keyFunc.apply(data);
        return map.get(tree.parent(key));
    }

    public List<V> childrenOf(V data) {
        final K key = keyFunc.apply(data);
        return tree.children(key).stream().map(map::get).collect(Collectors.toList());
    }
}
