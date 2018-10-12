package org.waglesid.structs.parenttree;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class TreeNode<T> {

    private T data;

    private TreeNode<T> parent;

    private Set<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
    }

    public TreeNode(T data, TreeNode<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public void addChild(TreeNode<T> child) {
        if (children == null)
            children = new HashSet<>();
        children.add(child);
    }

    public Set<TreeNode<T>> getChildren() {
        return children == null ? Collections.emptySet() : children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(data, treeNode.data) &&
                Objects.equals(parent, treeNode.parent) &&
                Objects.equals(children, treeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, parent.data, children);
    }
}
