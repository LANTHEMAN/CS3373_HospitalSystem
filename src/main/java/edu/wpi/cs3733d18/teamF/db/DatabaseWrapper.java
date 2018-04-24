package edu.wpi.cs3733d18.teamF.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseWrapper {

    // will filter the given ListView for the given input String
    public static void autoComplete(String input, ListView listView, String table, String field) {
        if (input.length() > 0) {
            String sql = "SELECT " + field + " FROM " + table;
            ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
            ArrayList<String> autoCompleteStrings = new ArrayList<>();

            try {
                while (resultSet.next()) {
                    String username = resultSet.getString(1);
                    if (username.toLowerCase().contains(input.toLowerCase())) {
                        autoCompleteStrings.add(username);
                    }
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            try {
                if (autoCompleteStrings.size() > 0) {
                    ObservableList<String> list = FXCollections.observableArrayList(autoCompleteStrings);
                    listView.setItems(list);
                    listView.setVisible(true);
                } else {
                    listView.setVisible(false);
                }
            } catch (Exception anyE) {
                anyE.printStackTrace();
            }
        } else {
            listView.setVisible(false);
        }
    }

    public static void filterHallAutoComplete(String input, ListView listView) {
        if (input.length() > 0) {
            ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery("SELECT longName FROM Node WHERE nodeType != 'HALL' AND UPPER(longName) LIKE UPPER('%" + input + "%')");
            ArrayList<String> hallfreeStrings = new ArrayList<>();

            try {
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    hallfreeStrings.add(name);
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            try {
                if (hallfreeStrings.size() > 0) {
                    ObservableList<String> list = FXCollections.observableArrayList(hallfreeStrings);
                    listView.setItems(list);
                    listView.setVisible(true);
                } else {
                    listView.setVisible(false);
                }
            } catch (Exception anyE) {
                anyE.printStackTrace();
            }
        } else {
            listView.setVisible(false);
        }
    }
}
