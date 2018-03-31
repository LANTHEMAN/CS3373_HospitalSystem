package com.github.CS3733_D18_Team_F_Project_0.graph;

public class ExistingNodeBuilder extends NodeBuilder<ExistingNodeBuilder> {

    /**
     * A builder class to make nodes from existing entries in the database
     */
    public ExistingNodeBuilder() {
        super(ExistingNodeBuilder.class);
    }

    /**
     * @return the node built from this builder class
     */
    public Node build() {
        // there must be a nodeID
        if (nodeID == null) {
            throw new AssertionError("Existing nodes must have a nodeID.");
        }

        // parse fields from the nodeID if it exists
        if (nodeID.length() != 10) {
            throw new AssertionError("Invalid Node ID format. Not 10 characters 1ong.");
        }

        if (position == null) {
            throw new AssertionError("Node must contain a position");
        }
        if (wireframePosition == null) {
            throw new AssertionError("Node must contain a wireframePosition");
        }
        if (shortName == null) {
            throw new AssertionError("Node must contain a shortName");
        }
        if (building == null) {
            throw new AssertionError("Node must contain a building");
        }

        String nodeType = nodeID.substring(1, 5);
        String floor = nodeID.substring(nodeID.length() - 2);

        return new Node(position, wireframePosition, 0, nodeID, floor, building, nodeType, shortName);
    }
}
