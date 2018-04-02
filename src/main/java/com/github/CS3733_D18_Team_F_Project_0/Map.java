package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseItem;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;
import com.github.CS3733_D18_Team_F_Project_0.graph.*;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Map implements DatabaseItem, Observer {

    // TODO: draw nodes
    // TODO: add this object to be notified by changes in a node

    DatabaseHandler dbHandler;
    Graph graph;

    public Map() {
        graph = new Graph();
        dbHandler = DatabaseSingleton.getInstance().getDbHandler();
    }

    public void createNode(Node node) {
        try {
            // test that the node does not already exist
            if (graph.getNodes(graphNode -> graphNode == node).size() == 1) {
                return;
            }

            graph.addNode(node);
            // track this node
            node.addObserver(this);

            // will only reach here if successful node creation
            String cmd = "INSERT INTO NODE VALUES ("
                    + "'" + node.getNodeID() + "'"
                    + "," + (int) node.getPosition().getX()
                    + "," + (int) node.getPosition().getY()
                    + ",'" + node.getFloor() + "'"
                    + ",'" + node.getBuilding() + "'"
                    + ",'" + node.getNodeType() + "'"
                    + ",'" + node.getShortName() + "'"
                    + ",'" + node.getShortName() + "'"
                    + ",'" + "Team F" + "'"
                    + "," + (int) node.getWireframePosition().getX()
                    + "," + (int) node.getWireframePosition().getY()
                    + ")";
            dbHandler.runAction(cmd);
            syncCSVFromDB(dbHandler);

            // TODO reflect the nodes to draw

        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }


    public void removeNode(Node node) {
        // make sure the node exists in this graph
        if (graph.getNodes(graphNode -> graphNode == node).size() == 0) {
            return;
        }

        // delete all edges with this node as an edge
        HashSet<Edge> edges = graph.getEdges(edge -> edge.hasNode(node));
        for (Edge edge : edges) {
            removeEdge(edge);
        }

        // remove the node from this graph
        graph.removeNode(node);

        // remove this node from the database
        String cmd = "DELETE FROM NODE WHERE ID='" + node.getNodeID() + "'";
        dbHandler.runAction(cmd);
        syncCSVFromDB(dbHandler);

        // TODO reflect the nodes to draw
    }

    // TODO implement
    public void addEdge(Node node1, Node node2) {
        // make sure that the nodes exist
        if (graph.getNodes(graphNode -> graphNode == node1 || graphNode == node2).size() != 2) {
            return;
        }
        // make sure the edge does not already exist
        if (graph.getEdges(edge -> edge.edgeOfNodes(node1, node2)).size() == 1) {
            return;
        }
        // make the edge
        graph.addEdge(node1, node2);

        // sync the database
        Edge edge = graph.getEdge(node1, node2);
        String cmd = "INSERT INTO EDGE VALUES ("
                + "'" + edge.getEdgeID() + "'"
                + ",(select ID from NODE where ID = '" + edge.getNode1().getNodeID() + "')"
                + ",(select ID from NODE where ID = '" + edge.getNode2().getNodeID() + "')"
                + ")";
        dbHandler.runAction(cmd);
        syncCSVFromDB(dbHandler);

        // TODO reflect the edges to draw
    }

    public void removeEdge(Node node1, Node node2) {
        // make sure that the nodes exist
        if (graph.getNodes(graphNode -> graphNode == node1 || graphNode == node2).size() != 2) {
            return;
        }
        // make sure the edge already exists
        if (!graph.edgeExists(node1, node2)) {
            return;
        }

        // save the edge
        HashSet<Edge> edges = graph.getEdges(edge -> edge.edgeOfNodes(node1, node2));
        Edge edge = edges.iterator().next();

        removeEdge(edge);
    }

    public void removeEdge(Edge edge) {
        // verify edge exists in graph
        if (!graph.edgeExists(edge)) {
            return;
        }
        // remove the edge from the graph
        graph.removeEdge(edge);

        // remove the edge from the database
        String cmd = "DELETE FROM EDGE WHERE EDGEID='" + edge.getEdgeID() + "'";
        dbHandler.runAction(cmd);
        syncCSVFromDB(dbHandler);

        // TODO reflect the edges to draw
    }


    public HashSet<Node> getNeighbors(Node node) {
        return graph.getNeighbors(node);
    }

    public HashSet<Node> getNodes() {
        return graph.getNodes();
    }

    public HashSet<Node> getNodes(Predicate<Node> filterFunction) {
        return graph.getNodes(filterFunction);
    }

    public HashSet<Edge> getEdges(Predicate<Edge> filterFunction) {
        return graph.getEdges(filterFunction);
    }

    public Edge getEdge(Node node1, Node node2) {
        return graph.getEdge(node1, node2);
    }

    public boolean edgeExists(Node node1, Node node2) {
        return graph.edgeExists(node1, node2);
    }

    public com.github.CS3733_D18_Team_F_Project_0.graph.Path getPath(Node node1, Node node2) {
        return AStar.getPath(graph, node1, node2);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof Node)) {
            return;
        }
        Node node = (Node) o;


        // if arg is null the nodeID did not change and just update the node properties
        if (arg == null) {
            String cmd = "UPDATE NODE "
                    + "SET X_COORD = " + node.getPosition().getX()
                    + ", Y_COORD = " + node.getPosition().getY()
                    + ", BUILDING = '" + node.getBuilding() + "'"
                    + ", SHORTNAME = '" + node.getShortName() + "'"
                    + ", XCOORD3D = " + node.getWireframePosition().getX()
                    + ", YCOORD3D = " + node.getWireframePosition().getY()
                    + " WHERE ID='" + node.getNodeID() + "'";
            dbHandler.runAction(cmd);
            syncCSVFromDB(dbHandler);
        }
        // if arg == String, the nodeID and all edgeIDs have to be updated in the database
        else {
            String newNodeID = (String) arg;
            if (newNodeID.equals(node.getNodeID())) {
                return;
            }

            // save all neighbors
            HashSet<Node> neighbors = new HashSet<>(graph.getNeighbors(node));
            // get the edges that have to be changed
            HashSet<Edge> edges = neighbors.stream()
                    .map(neighborNode -> graph.getEdge(node, neighborNode))
                    .collect(Collectors.toCollection(HashSet::new));

            // remove all edges
            for (Edge edge : edges) {
                removeEdge(edge);
            }
            // remove the old node
            removeNode(node);

            // create a new node
            Node newNode = new ExistingNodeBuilder()
                    .setPosition(node.getPosition())
                    .setBuilding(node.getBuilding())
                    .setNodeID(newNodeID)
                    .setShortName(node.getShortName())
                    .setWireframePosition(node.getWireframePosition())
                    .build();
            createNode(newNode);
            newNode.setAdditionalWeight(node.getAdditionalWeight());
            // reconnect the edges
            for (Node neighbor : neighbors) {
                addEdge(newNode, neighbor);
            }
        }
    }


    ////////////////////////////////////////////////////////////////
    //                                                            //
    //               DATABASE SYNCHRONIZATION                     //
    //                                                            //
    ////////////////////////////////////////////////////////////////

    @Override
    public void initDatabase(DatabaseHandler dbHandler) {
        try {
            //if the table does not yet exist in the db, initialize it
            if (!dbHandler.tableExists("NODE")) {
                System.out.println("DB: Initializing NODE table entry");
                dbHandler.runSQLScript("init_node_db.sql");

                List<String> nodeFilePaths;
                boolean existingCompiledNodeFile = false;
                // see if there is an already compiled nodes csv file
                if (new File("map/nodes.csv").exists()) {
                    nodeFilePaths = new LinkedList<>(Collections.singletonList("map/nodes.csv"));
                    existingCompiledNodeFile = true;

                } else {
                    nodeFilePaths = Files.walk(Paths.get(dbHandler.getClass().getResource("map").toURI()))
                            .filter(Files::isRegularFile)
                            .filter(path -> path.getFileName().toString().contains("odes.csv"))
                            .map(path -> path.getFileName().toString())
                            .map(path -> "map/" + path)
                            .collect(Collectors.toList());
                }

                for (String nodeFilePath : nodeFilePaths) {
                    File csvFile;
                    if (existingCompiledNodeFile) {
                        System.out.println("Missing database, Loading Nodes from map/nodes.csv");
                        csvFile = new File(nodeFilePath);
                    } else {
                        System.out.println("Missing database, generating Nodes from db/map/*odes.csv files.");
                        csvFile = new File(dbHandler.getClass().getResource(nodeFilePath).toURI().getPath());
                    }

                    CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.RFC4180);

                    for (CSVRecord record : parser) {
                        if (record.get(0).contains("nodeID")) {
                            continue;
                        }
                        String name = record.get(0);
                        int x = (int) Double.parseDouble(record.get(1));
                        int y = (int) Double.parseDouble(record.get(2));
                        String floor = record.get(3);
                        String building = record.get(4);
                        String nodeType = record.get(5);
                        String longName = record.get(6);
                        String shortName = record.get(7);
                        String teamName = record.get(8);
                        int x3d = (int) Double.parseDouble(record.get(9));
                        int y3d = (int) Double.parseDouble(record.get(10));

                        String cmd = "INSERT INTO NODE VALUES ("
                                + "'" + name + "'"
                                + "," + x
                                + "," + y
                                + ",'" + floor + "'"
                                + ",'" + building + "'"
                                + ",'" + nodeType + "'"
                                + ",'" + longName + "'"
                                + ",'" + shortName + "'"
                                + ",'" + teamName + "'"
                                + "," + x3d
                                + "," + y3d
                                + ")";
                        dbHandler.runAction(cmd);
                    }
                }
            }
        } catch (SQLException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            //if the table does not yet exist in the db, initialize it
            if (!dbHandler.tableExists("EDGE")) {
                System.out.println("DB: Initializing EDGE table entry");
                dbHandler.runSQLScript("init_edge_db.sql");

                List<String> edgeFilePaths;
                boolean existingCompiledEdgeFile = false;
                // see if there is an already compiled nodes csv file
                if (new File("map/edges.csv").exists()) {
                    edgeFilePaths = new LinkedList<>(Collections.singletonList("map/edges.csv"));
                    existingCompiledEdgeFile = true;

                } else {
                    edgeFilePaths = Files.walk(Paths.get(dbHandler.getClass().getResource("map").toURI()))
                            .filter(Files::isRegularFile)
                            .filter(path -> path.getFileName().toString().contains("dges.csv"))
                            .map(path -> path.getFileName().toString())
                            .map(path -> "map/" + path)
                            .collect(Collectors.toList());
                }

                for (String edgeFilePath : edgeFilePaths) {
                    File csvFile;
                    if (existingCompiledEdgeFile) {
                        System.out.println("Missing database, Loading Edges from map/edges.csv");
                        csvFile = new File(edgeFilePath);
                    } else {
                        System.out.println("Missing database, generating Edges from db/map/*dges.csv files.");
                        csvFile = new File(dbHandler.getClass().getResource(edgeFilePath).toURI().getPath());
                    }

                    CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.RFC4180);

                    for (CSVRecord record : parser) {
                        if (record.get(0).contains("edgeID")) {
                            continue;
                        }

                        String edgeID = record.get(0);
                        String startNode = record.get(1);
                        String endNode = record.get(2);

                        String cmd = "INSERT INTO EDGE VALUES ("
                                + "'" + edgeID + "'"
                                + ",(select ID from NODE where ID = '" + startNode + "')"
                                + ",(select ID from NODE where ID = '" + endNode + "')"
                                + ")";
                        dbHandler.runAction(cmd);
                    }
                }
            }
        } catch (IOException | SQLException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public LinkedList<String> getTableNames() {
        return new LinkedList<>(Arrays.asList("NODE", "EDGE"));
    }

    @Override
    public void syncLocalFromDB(String tableName, ResultSet resultSet) {
        try {
            switch (tableName) {
                case "NODE": {
                    while (resultSet.next()) {
                        int height;
                        String floor = resultSet.getString(1).substring(8);
                        switch (floor) {
                            case "L2":
                                height = -200;
                                break;
                            case "L1":
                                height = -100;
                                break;
                            case "0G":
                                height = 0;
                                break;
                            case "01":
                                height = 100;
                                break;
                            case "02":
                                height = 200;
                                break;
                            case "03":
                                height = 300;
                                break;
                            default:
                                throw new AssertionError("Invalid flor level stored in Database: " + floor);
                        }

                        // extract data from database
                        String nodeID = resultSet.getString(1);
                        String shortName = resultSet.getString(8);
                        String building = resultSet.getString(5);
                        Point3D position = new Point3D(Double.parseDouble(resultSet.getString(2))
                                , Double.parseDouble(resultSet.getString(3))
                                , height);
                        Point2D wireframePosition = new Point2D(Double.parseDouble(resultSet.getString(10))
                                , Double.parseDouble(resultSet.getString(11)));

                        // construct node
                        Node newNode = new ExistingNodeBuilder()
                                .setNodeID(nodeID)
                                .setShortName(shortName)
                                .setPosition(position)
                                .setWireframePosition(wireframePosition)
                                .setBuilding(building)
                                .build();

                        // add to graph
                        graph.addNode(newNode);
                        // track this new node
                        newNode.addObserver(this);
                    }
                }
                break;
                case "EDGE": {
                    while (resultSet.next()) {
                        String edgeID = resultSet.getString(1);
                        String node1Name = resultSet.getString(2);
                        String node2Name = resultSet.getString(3);
                        try {
                            Node node1 = graph.getNodes(node -> node.getNodeID().equals(node1Name)).iterator().next();
                            Node node2 = graph.getNodes(node -> node.getNodeID().equals(node2Name)).iterator().next();
                            graph.addEdge(node1, node2, edgeID);
                        } catch (NoSuchElementException e) {
                            System.out.println("[Warning] Uninitialized edge");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void syncCSVFromDB(DatabaseHandler dbHandler) {
        try {
            new File("map").mkdirs();

            // save nodes
            FileWriter fw = new FileWriter("map/nodes.csv", false);
            CSVPrinter csvPrinterNodes = new CSVPrinter(fw, CSVFormat.DEFAULT
                    .withHeader("nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName"
                            , "shortName", "teamAssigned", "xcoord3d", "ycoord3d"));
            ResultSet nodeSet = dbHandler.runQuery("SELECT * FROM NODE");
            while (nodeSet.next()) {
                csvPrinterNodes.printRecord(
                        nodeSet.getString(1)
                        , nodeSet.getString(2)
                        , nodeSet.getString(3)
                        , nodeSet.getString(4)
                        , nodeSet.getString(5)
                        , nodeSet.getString(6)
                        , nodeSet.getString(7)
                        , nodeSet.getString(8)
                        , nodeSet.getString(9)
                        , nodeSet.getString(10)
                        , nodeSet.getString(11));
            }
            csvPrinterNodes.flush();
            nodeSet.close();
            fw.close();

            // save edges
            fw = new FileWriter("map/edges.csv", false);
            CSVPrinter csvPrinterEdges = new CSVPrinter(fw, CSVFormat.DEFAULT
                    .withHeader("edgeID", "startNode", "endNode"));
            ResultSet edgeSet = dbHandler.runQuery("SELECT * FROM EDGE");
            while (edgeSet.next()) {
                csvPrinterEdges.printRecord(
                        edgeSet.getString(1)
                        , edgeSet.getString(2)
                        , edgeSet.getString(3));
            }
            csvPrinterEdges.flush();
            edgeSet.close();
            fw.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
