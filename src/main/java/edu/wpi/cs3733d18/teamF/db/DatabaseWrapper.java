package edu.wpi.cs3733d18.teamF.db;

import edu.wpi.cs3733d18.teamF.controller.User;
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

    public static void autoCompleteLocations(String input, ListView listView){
        if (input.length() > 0) {
            ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery("SELECT longName FROM Node WHERE UPPER(longName) LIKE UPPER('%" + input + "%')");
            ArrayList<String> locations = new ArrayList<>();

            try {
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    locations.add(name);
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            try {
                if (locations.size() > 0) {
                    ObservableList<String> list = FXCollections.observableArrayList(locations);
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

    public static ArrayList<User> autoCompleteUserSearch(String input) {
        ArrayList<User> autoCompleteUser = new ArrayList<>();
        if (input.length() > 0) {
            String sql = "SELECT * FROM HUser";
            try {
                ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
                while (resultSet.next()) {
                    String username = resultSet.getString(1);
                    String password = resultSet.getString(2);
                    String firstname = resultSet.getString(3);
                    String lastname = resultSet.getString(4);
                    String privilege = resultSet.getString(5);
                    String occupation = resultSet.getString(6);
                    String faceID = resultSet.getString(7);
                    User temp = new User(username, password, firstname, lastname, privilege, occupation, faceID, true);
                    String searchString = username + password + firstname + lastname + privilege + occupation + faceID;
                    if (searchString.toLowerCase().contains(input.toLowerCase())) {
                        autoCompleteUser.add(temp);
                    }

                }
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        }else{
            autoCompleteUser = allUsers();
        }
        return autoCompleteUser;
    }

    public static ArrayList<User> allUsers(){
        String sql = "SELECT * FROM HUser";
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
            while (resultSet.next()) {
                String username = resultSet.getString(1);
                String password = resultSet.getString(2);
                String firstname = resultSet.getString(3);
                String lastname = resultSet.getString(4);
                String privilege = resultSet.getString(5);
                String occupation = resultSet.getString(6);
                String faceID = resultSet.getString(7);
                User temp = new User(username, password, firstname, lastname, privilege, occupation, faceID, true);
                users.add(temp);
            }
            resultSet.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return users;
    }
}
