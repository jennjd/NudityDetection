package com.imperva.encreept.dataStructures;

import java.util.ArrayList;

public class TestTree {
    public static void main(String[] args) {
        TreeNode root = new TreeNode("1", null, new ArrayList<>());
        Tree tree = new Tree(root);
        tree.addNode("1;2");
        tree.addNode("1;3");
        tree.addNode("1;4");
        tree.addNode("1;3;5");
        tree.addNode("1;3;6");
        tree.addNode("1;3;6;7");
        tree.addNode("1;3;6;8");
        tree.addNode("1;3;6;8");
        tree.addNode("1;3;6;8");
        tree.addNode("1;3;6;8");

//        System.out.println(tree.getJson());
    }
}
