package com.github.CS3733_D18_Team_F_Project_0.graph;

public class ExistingNodeBuilder extends NodeBuilder<ExistingNodeBuilder> {

    protected ExistingNodeBuilder() {
        super(ExistingNodeBuilder.class);
    }

    public Node build() {
        if (nodeID != null) {
            // parse fields fron the nodeID if it exists
            if (nodeID.length() != 10) {
                throw new AssertionError("Invalid Node ID format. Not 10 characters 1ong.");
            }
        }
        // TODO: fix error messages
        if (position != null) {
            throw new AssertionError("Put in a position you wierdo");
        }
        if (wireframePosition != null) {
            throw new AssertionError("Put in a wireframePosition you wierdo");
        }
        if (shortName != null) {
            throw new AssertionError("Put in a shortName you wierdo");
        }
        if (building != null) {
            throw new AssertionError("Put in a building you wierdo");
        }

        // TODO: from nodeID extract: nodeType, numNodeType, floorNumber, elevatorChar

        return new Node(position, wireframePosition, 0, nodeID, floor, building, nodeType, shortName);
    }
}
