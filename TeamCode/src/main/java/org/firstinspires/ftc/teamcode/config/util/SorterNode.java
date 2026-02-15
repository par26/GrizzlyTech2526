package org.firstinspires.ftc.teamcode.config.util;

public class SorterNode {

    private NodeOption storedNode;

    public enum NodeOption {
        PURPLE,
        GREEN,
        EMPTY
    }

    public SorterNode(NodeOption node) {
        storedNode = node;
    }

    public void setNode(NodeOption node) {
        storedNode = node;
    }

    public NodeOption getStoredNode() {
        return storedNode;
    }


}
