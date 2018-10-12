package org.waglesid.structs.parenttree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class ParentTree<T> {

    private final Map<T, TreeNode<T>> map = new ConcurrentHashMap<>();

    public TreeNode<T> get(T data) {
        return map.getOrDefault(data, null);
    }

    public void add(T data) {
        map.computeIfAbsent(data, TreeNode::new);
    }

    public void update(T old, T data) {
        final TreeNode<T> n = map.get(old);
        n.setData(data);
    }

    public TreeNode<T> remove(T data) {
        return map.remove(data);
    }

    public void addParent(T data, T parent) {
        final TreeNode<T> p = map.computeIfAbsent(parent, TreeNode::new);
        map.computeIfAbsent(data, t -> {
            final TreeNode<T> newNode = new TreeNode<>(t, p);
            p.addChild(newNode);
            return newNode;
        });
    }

    public T parent(T data) {
        final TreeNode<T> n = map.get(data);
        return n == null ? null :
                n.getParent() == null ? null : n.getParent().getData();
    }

    public List<T> allParents(T data) {
        final T p = parent(data);
        if (p == null)
            return Collections.emptyList();
        final List<T> list = new ArrayList<>();
        list.add(p);
        list.addAll(allParents(p));
        return list;
    }

    public List<T> children(T data) {
        final TreeNode<T> n = map.get(data);
        return n.getChildren().stream()
                .map(TreeNode::getData).collect(Collectors.toList());
    }

    public List<T> allChildren(T data) {
        final List<T> list = children(data);
        if (list == null || list.isEmpty())
            return Collections.emptyList();
        for (T t: list)
            list.addAll(allChildren(t));
        return list;
    }

    public List<T> siblings(T data) {
        final TreeNode<T> n = map.get(data);
        return n.getParent() == null ? Collections.emptyList() : new ArrayList(n.getParent().getChildren());
    }
}
