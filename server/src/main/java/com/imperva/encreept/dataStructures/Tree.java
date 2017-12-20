package com.imperva.encreept.dataStructures;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Optional;
import java.util.StringTokenizer;

public class Tree {
    private TreeNode root;

    public Tree(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void addNode(String dataPath) {
        StringTokenizer stringTokenizer = new StringTokenizer(dataPath, ";");
        TreeNode currentTreeNode = null;
        int counter = 1;
        while(stringTokenizer.hasMoreTokens()) {
            String data = stringTokenizer.nextToken();
            if(counter == 1) {
                if(!root.getData().equals(data)) {
                    throw new RuntimeException("root doesn't match the stored picture root");
                }
                currentTreeNode = root;
            } else {
                Optional<TreeNode> first = currentTreeNode.getChildren().stream().filter(child -> child.getData().equals(data)).findFirst();
                TreeNode tTreeNode = null;
                if(first.isPresent()) {
                    tTreeNode = first.get();
                }
                if(tTreeNode == null && stringTokenizer.hasMoreTokens()) {
                    throw new RuntimeException("Path doesn't match the stored picture dataPath");
                }
                if(tTreeNode == null && !stringTokenizer.hasMoreTokens()) {
                    TreeNode newTreeNode = new TreeNode(data,currentTreeNode.getData(), new ArrayList<>());
                    currentTreeNode.getChildren().add(newTreeNode);
                }
                if(tTreeNode != null && !stringTokenizer.hasMoreTokens()) {
                    // Already exist in our tree
                }
                if(tTreeNode != null && stringTokenizer.hasMoreTokens()) {
                    currentTreeNode = tTreeNode;
                }
            }
            counter++;
        }
    }

    @Override
    public String toString() {
        return "Tree{" +
                "root=" + root +
                '}';
    }
}