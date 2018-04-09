package com.github.CS3733_D18_Team_F_Project_0.graph;

import org.junit.Test;

public class TestNewNodeBuilder {

    @Test(expected = AssertionError.class)
    public void testEmptyNodeCreation() {
        new NewNodeBuilder().build();
    }

}
