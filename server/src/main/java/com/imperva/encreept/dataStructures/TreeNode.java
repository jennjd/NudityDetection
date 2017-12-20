package com.imperva.encreept.dataStructures;

import java.util.List;

public class TreeNode {
    private String data;
    private String parent;
    private List<TreeNode> children;

    public TreeNode(String data, String parent, List<TreeNode> children) {
        this.data = data;
        this.parent = parent;
        this.children = children;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "data='" + data + '\'' +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
