package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.postgresql.util.PSQLException;

import java.sql.*;

public class Controller {

    private final ObservableList<Row> data_obs = FXCollections.observableArrayList();

    private int insert_click = 0;

    @FXML
    private Text no_id_text;

    @FXML
    private Text red_del;

    @FXML
    private Text ex_text, green_del, ex_fk;

    @FXML
    private Button button_show;

    @FXML
    private Button button_ok;

    @FXML
    private Button update_but;

    @FXML
    private Button delete_button;

    @FXML
    private TableView table_id;

    @FXML
    private ComboBox comboList, combo_field;

    @FXML
    private Button button_insert;

    @FXML
    private Text green_text, red_text, down_text;

    @FXML
    private TextField delete_field;

    @FXML
    void initialize() {
        comboList.getItems().removeAll(comboList.getItems());
        comboList.getItems().addAll(
                "person",
                "sportsman",
                "trainer",
                "stage",
                "rang",
                "gym",
                "timetable",
                "competition",
                "fighter",
                "fighter_category",
                "abonement",
                "sportsmanabonement");
        comboList.getSelectionModel().select(0);
        setComboField();

        update_but.setOnAction(event -> {
            try {
                reload_text();
                updateData(comboList.getSelectionModel().getSelectedItem().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        button_show.setOnAction(event -> {
            try {
                reload_text();
                insert_click = 0;
                selectData(comboList.getSelectionModel().getSelectedItem().toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        button_ok.setOnAction(event -> {
            try {
                reload_text();
                if (insert_click != 0)
                    enterData(comboList.getSelectionModel().getSelectedItem().toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        button_insert.setOnAction(event -> {
            try {
                reload_text();
                insert_click++;
                fill_data(table_id, comboList.getSelectionModel().getSelectedItem().toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        delete_button.setOnAction(event -> {
            try {
                reload_text();

                table_id.getItems().clear();    // for press "insert" every one for "add"
                table_id.getColumns().clear();

                insert_click = 0;
                delete(comboList.getSelectionModel().getSelectedItem().toString(), combo_field.getSelectionModel().getSelectedItem().toString(), delete_field.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        comboList.setOnAction(event ->
                setComboField()
        );
    }

    private void setComboField() {
        combo_field.getItems().removeAll(combo_field.getItems());
        switch (comboList.getSelectionModel().getSelectedItem().toString()) {
            case "person":
                combo_field.getItems().addAll(
                        "id",
                        "name",
                        "sex",
                        "birthday",
                        "phone_number"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "sportsman":
                combo_field.getItems().addAll(
                        "id",
                        "id_person",
                        "group",
                        "value_since",
                        "value_until",
                        "id_rang"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "trainer":
                combo_field.getItems().addAll(
                        "id",
                        "id_person",
                        "pay",
                        "value_since",
                        "value_until",
                        "id_rang"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "gym":
                combo_field.getItems().addAll(
                        "id",
                        "name",
                        "adress",
                        "phone_number"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "stage":
                combo_field.getItems().addAll(
                        "id",
                        "name"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "rang":
                combo_field.getItems().addAll(
                        "id",
                        "value",
                        "title"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "timetable":
                combo_field.getItems().addAll(
                        "id",
                        "day_of_week",
                        "start_time",
                        "finish_time",
                        "trainer",
                        "gym",
                        "group"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "abonement":
                combo_field.getItems().addAll(
                        "id",
                        "price",
                        "title",
                        "time"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "sportsmanabonement":
                combo_field.getItems().addAll(
                        "id",
                        "id_abonement",
                        "id_sportsman",
                        "valid_from",
                        "valid_to"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "fighter":
                combo_field.getItems().addAll(
                        "id",
                        "id_sportsman",
                        "id_competition",
                        "result",
                        "id_category"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "competition":
                combo_field.getItems().addAll(
                        "id",
                        "name",
                        "place",
                        "date",
                        "time_start"
                );
                combo_field.getSelectionModel().select(0);
                break;
            case "fighter_category":
                combo_field.getItems().addAll(
                        "id",
                        "age",
                        "sex",
                        "weight"
                );
                combo_field.getSelectionModel().select(0);
                break;
            default:
                combo_field.getItems().addAll();
        }
    }

    private void reload_text() {
        no_id_text.setVisible(false);
        red_del.setVisible(false);
        ex_text.setVisible(false);
        green_del.setVisible(false);
        down_text.setVisible(false);
        green_text.setVisible(false);
        red_text.setVisible(false);
        ex_fk.setVisible(false);
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/KarateClub";
    private static final String USER = "postgres";
    private static final String PASSWORD = "karate24";

    private Connection c = null;

    void connect() {
        try {
            c = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void disconnect() {
        try {
            if (c != null) {
                c.close();
                System.out.println("Database disconnection successful");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void delete(String table, String field, String value) throws SQLException {
        if (delete_field.getText().equals("") || !isExistReccord(table, field, value)) {
            no_id_text.setVisible(true);
            return;
        }

        connect();
        String SQL = "delete from " + table + " where " + field + " = " + "'" + value + "'";
        PreparedStatement p_state;

        try {
            p_state = c.prepareStatement(SQL);
            p_state.executeUpdate();
            green_del.setVisible(true);
        } catch (SQLException ex) {
            red_del.setVisible(true);
        }
        disconnect();
    }


    // кнопка add
    private void updateData(String table) throws SQLException {
        connect();
        String SQL;
        PreparedStatement p_state;

        if (data_obs.get(0).getTextField1().getText().equals("") || !isExistReccord(table, "id", data_obs.get(0).getTextField1().getText())) {
            no_id_text.setVisible(true);
            return;
        }

        try {
            switch (table) {
                case "person":
                    SQL = "update person set id = ?, name = ?, sex = ?, birthday = ?,phone_number = ? where id = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setLong(5, Long.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.INTEGER);


                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "sportsman":
                    SQL = "update sportsman set id  = ?, id_person = ?, \"group\" = ?, value_since = ?, value_until = ?, id_rang = ? where id = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));


                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setDate(5, Date.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.DATE);

                    if (!data_obs.get(0).getTextField6().getText().equals(""))
                        p_state.setInt(6, Integer.valueOf((data_obs.get(0).getTextField6().getText())));
                    else
                        p_state.setNull(6, Types.INTEGER);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "trainer":
                    SQL = "update trainer set id = ?, id_person = ?, pay = ?, value_since = ?, value_until = ?, id_rang = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setDate(5, Date.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.DATE);

                    if (!data_obs.get(0).getTextField6().getText().equals(""))
                        p_state.setInt(6, Integer.valueOf((data_obs.get(0).getTextField6().getText())));
                    else
                        p_state.setNull(6, Types.INTEGER);


                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "fighter":
                    SQL = "update fighter set id = ?, id_sportsman = ?, id_competition = ?, result = ?, id_category = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));


                    if (!data_obs.get(0).getTextField4().getText().equals(""))
                        p_state.setInt(4, Integer.valueOf((data_obs.get(0).getTextField4().getText())));
                    else
                        p_state.setNull(4, Types.INTEGER);

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setInt(5, Integer.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.INTEGER);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "rang":
                    SQL = "update rang set id = ?, value = ?, title = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "stage":
                    SQL = "update stage set id = ?, name = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "gym":
                    SQL = "update gym set id = ?, name = ?, adress = ?, " + " phone_number = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());

                    if (!data_obs.get(0).getTextField4().getText().equals(""))
                        p_state.setLong(4, Long.valueOf((data_obs.get(0).getTextField4().getText())));
                    else
                        p_state.setNull(4, Types.INTEGER);


                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "timetable":
                    SQL = "update timetable set id = ?, day_of_week = ?, start_time = ?, finish_time = ?, trainer  = ?, gym = ?, \"group\" = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("") || data_obs.get(0).getTextField5().getText().equals("") || data_obs.get(0).getTextField6().getText().equals("") || data_obs.get(0).getTextField7().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setTime(3, Time.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setTime(4, Time.valueOf(data_obs.get(0).getTextField4().getText()));
                    p_state.setInt(5, Integer.valueOf(data_obs.get(0).getTextField5().getText()));
                    p_state.setInt(6, Integer.valueOf(data_obs.get(0).getTextField6().getText()));
                    p_state.setInt(7, Integer.valueOf(data_obs.get(0).getTextField7().getText()));

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "competition":
                    SQL = "update competition set id = ?, name = ?, place = ?, date = ?, time_start = ? where id = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("") || data_obs.get(0).getTextField5().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));
                    p_state.setTime(5, Time.valueOf(data_obs.get(0).getTextField5().getText()));

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "fighter_category":
                    SQL = "update fighter_category set id = ?, age = ?, sex = ?, weight = ? where id  = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    p_state.setString(4, data_obs.get(0).getTextField4().getText());

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "sportsmanabonement":
                    SQL = "update sportsmanabonement set id = ?, id_abonement = ?, id_sportsman = ?, valid_from = ?, valid_to = ? where id = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setDate(5, Date.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.DATE);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "abonement":
                    SQL = "update abonement set id = ?, price = ?, title = ?, time = ? where id = " + data_obs.get(0).getTextField1().getText();
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));

                    if (!data_obs.get(0).getTextField3().getText().equals(""))
                        p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    else
                        p_state.setString(3, null);

                    if (!data_obs.get(0).getTextField4().getText().equals(""))
                        p_state.setString(4, data_obs.get(0).getTextField4().getText());
                    else
                        p_state.setString(4, null);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;
            }
        } catch (IllegalArgumentException ex) {
            ex_text.setVisible(true);
        } catch (PSQLException exception){
            ex_fk.setVisible(true);
        }
        disconnect();
    }


    // кнопка add
    private void enterData(String table) throws SQLException {
        connect();
        String SQL;
        PreparedStatement p_state;

        if (isExistReccordById(table, data_obs.get(0).getTextField1().getText())) {
            red_text.setVisible(true);
            return;
        }

        try {
            switch (table) {
                case "person":
                    SQL = "insert into person values (?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, Integer.valueOf(data_obs.get(0).getTextField1().getText()));
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setLong(5, Long.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.INTEGER);


                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "sportsman":
                    SQL = "insert into sportsman values (?, ?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("sportsman") + 1);
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));


                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setDate(5, Date.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.DATE);

                    if (!data_obs.get(0).getTextField6().getText().equals(""))
                        p_state.setInt(6, Integer.valueOf((data_obs.get(0).getTextField6().getText())));
                    else
                        p_state.setNull(6, Types.INTEGER);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "trainer":
                    SQL = "insert into trainer values (?, ?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("trainer") + 1);
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setDate(5, Date.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.DATE);

                    if (!data_obs.get(0).getTextField6().getText().equals(""))
                        p_state.setInt(6, Integer.valueOf((data_obs.get(0).getTextField6().getText())));
                    else
                        p_state.setNull(6, Types.INTEGER);


                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "fighter":
                    SQL = "insert into fighter values (?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("fighter") + 1);
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));


                    if (!data_obs.get(0).getTextField4().getText().equals(""))
                        p_state.setInt(4, Integer.valueOf((data_obs.get(0).getTextField4().getText())));
                    else
                        p_state.setNull(4, Types.INTEGER);

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setInt(5, Integer.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.INTEGER);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "rang":
                    SQL = "insert into rang values (?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("rang") + 1);
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "stage":
                    SQL = "insert into stage values (?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("stage") + 1);
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "gym":
                    SQL = "insert into gym values (?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("gym") + 1);
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());

                    if (!data_obs.get(0).getTextField4().getText().equals(""))
                        p_state.setLong(4, Long.valueOf((data_obs.get(0).getTextField4().getText())));
                    else
                        p_state.setNull(4, Types.INTEGER);


                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "timetable":
                    SQL = "insert into timetable values (?, ?, ?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("") || data_obs.get(0).getTextField5().getText().equals("") || data_obs.get(0).getTextField6().getText().equals("") || data_obs.get(0).getTextField7().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("timetable") + 1);
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setTime(3, Time.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setTime(4, Time.valueOf(data_obs.get(0).getTextField4().getText()));
                    p_state.setInt(5, Integer.valueOf(data_obs.get(0).getTextField5().getText()));
                    p_state.setInt(6, Integer.valueOf(data_obs.get(0).getTextField6().getText()));
                    p_state.setInt(7, Integer.valueOf(data_obs.get(0).getTextField7().getText()));

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "competition":
                    SQL = "insert into competition values (?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("") || data_obs.get(0).getTextField5().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("competition") + 1);
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));
                    p_state.setTime(5, Time.valueOf(data_obs.get(0).getTextField5().getText()));

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "fighter_category":
                    SQL = "insert into fighter_category values (?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("fighter_category") + 1);
                    p_state.setString(2, data_obs.get(0).getTextField2().getText());
                    p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    p_state.setString(4, data_obs.get(0).getTextField4().getText());

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "sportsmanabonement":
                    SQL = "insert into sportsmanabonement values (?, ?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("") || data_obs.get(0).getTextField3().getText().equals("") || data_obs.get(0).getTextField4().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("sportsmanabonement") + 1);
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));
                    p_state.setInt(3, Integer.valueOf(data_obs.get(0).getTextField3().getText()));
                    p_state.setDate(4, Date.valueOf(data_obs.get(0).getTextField4().getText()));

                    if (!data_obs.get(0).getTextField5().getText().equals(""))
                        p_state.setDate(5, Date.valueOf((data_obs.get(0).getTextField5().getText())));
                    else
                        p_state.setNull(5, Types.DATE);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;

                case "abonement":
                    SQL = "insert into abonement values (?, ?, ?, ?)";
                    p_state = c.prepareStatement(SQL);

                    if (data_obs.get(0).getTextField2().getText().equals("")) {
                        red_text.setVisible(true);
                        down_text.setVisible(true);
                        break;
                    }

                    p_state.setInt(1, getMaxId("abonement") + 1);
                    p_state.setInt(2, Integer.valueOf(data_obs.get(0).getTextField2().getText()));

                    if (!data_obs.get(0).getTextField3().getText().equals(""))
                        p_state.setString(3, data_obs.get(0).getTextField3().getText());
                    else
                        p_state.setString(3, null);

                    if (!data_obs.get(0).getTextField4().getText().equals(""))
                        p_state.setString(4, data_obs.get(0).getTextField4().getText());
                    else
                        p_state.setString(4, null);

                    p_state.executeUpdate();
                    green_text.setVisible(true);
                    break;
            }
        } catch (IllegalArgumentException ex) {
            ex_text.setVisible(true);
        }
        disconnect();
    }


    // button insert
    private void fill_data(TableView t, String table) {
        t.getItems().clear();
        t.getColumns().clear();

        TableColumn col1;
        TableColumn col2;
        TableColumn col3;
        TableColumn col4;
        TableColumn col5;
        TableColumn col6;
        TableColumn col7;
        switch (table) {
            case "person":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("name");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("sex");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("birthday");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("phone_number");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));


                data_obs.add(new Row(5));
                data_obs.get(0).getTextField3().setPromptText("male/female");
                data_obs.get(0).getTextField4().setPromptText("YYYY-MM-DD");
                data_obs.get(0).getTextField5().setPromptText("without '+'");
                t.getColumns().addAll(col1, col2, col3, col4, col5);
                break;

            case "sportsman":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("id person");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("id group");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("value since");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("value until");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));

                col6 = new TableColumn("id rang");
                col6.setMaxWidth(100);
                col6.setCellValueFactory(new PropertyValueFactory<Row, String>("textField6"));


                data_obs.add(new Row(6));
                data_obs.get(0).getTextField4().setPromptText("YYYY-MM-DD");
                data_obs.get(0).getTextField5().setPromptText("YYYY-MM-DD");
                t.getColumns().addAll(col1, col2, col3, col4, col5, col6);
                break;

            case "trainer":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("id person");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("pay");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("value since");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("value until");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));

                col6 = new TableColumn("id rang");
                col6.setMaxWidth(100);
                col6.setCellValueFactory(new PropertyValueFactory<Row, String>("textField6"));


                data_obs.add(new Row(6));
                data_obs.get(0).getTextField4().setPromptText("YYYY-MM-DD");
                data_obs.get(0).getTextField5().setPromptText("YYYY-MM-DD");
                t.getColumns().addAll(col1, col2, col3, col4, col5, col6);
                break;

            case "fighter":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("id sportsman");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("id competition");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("result");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("id category");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));

                data_obs.add(new Row(5));
                t.getColumns().addAll(col1, col2, col3, col4, col5);
                break;

            case "rang":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("value");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("title");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                data_obs.add(new Row(3));
                data_obs.get(0).getTextField2().setPromptText("1..10");
                data_obs.get(0).getTextField3().setPromptText("kye/dan");
                t.getColumns().addAll(col1, col2, col3);
                break;

            case "stage":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("name");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                data_obs.add(new Row(2));
                t.getColumns().addAll(col1, col2);
                break;

            case "gym":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("name");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("adress");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("phone number");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                data_obs.add(new Row(4));
                data_obs.get(0).getTextField4().setPromptText("without '+'");
                t.getColumns().addAll(col1, col2, col3, col4);
                break;

            case "timetable":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("day of week");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("start time");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("finish time");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("id trainer");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));

                col6 = new TableColumn("id gym");
                col6.setMaxWidth(100);
                col6.setCellValueFactory(new PropertyValueFactory<Row, String>("textField6"));

                col7 = new TableColumn("id group");
                col7.setMaxWidth(100);
                col7.setCellValueFactory(new PropertyValueFactory<Row, String>("textField7"));

                data_obs.add(new Row(7));
                data_obs.get(0).getTextField3().setPromptText("00:00:00");
                data_obs.get(0).getTextField4().setPromptText("00:00:00");
                t.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);
                break;

            case "competition":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("name");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("place");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("date");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("time start");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));

                data_obs.add(new Row(5));
                data_obs.get(0).getTextField4().setPromptText("YYYY-MM-DD");
                data_obs.get(0).getTextField5().setPromptText("00:00:00");
                t.getColumns().addAll(col1, col2, col3, col4, col5);
                break;


            case "fighter_category":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("age");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("sex");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("weight");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                data_obs.add(new Row(4));
                data_obs.get(0).getTextField2().setPromptText("14-15");
                data_obs.get(0).getTextField4().setPromptText("50-60kg");
                t.getColumns().addAll(col1, col2, col3, col4);
                break;

            case "abonement":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("price");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("title");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("time");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                data_obs.add(new Row(4));
                data_obs.get(0).getTextField4().setPromptText("month/year");
                t.getColumns().addAll(col1, col2, col3, col4);
                break;

            case "sportsmanabonement":
                col1 = new TableColumn("id");
                col1.setMaxWidth(100);
                col1.setCellValueFactory(new PropertyValueFactory<Row, TextField>("textField1"));

                col2 = new TableColumn("id abonement");
                col2.setMaxWidth(100);
                col2.setCellValueFactory(new PropertyValueFactory<Row, String>("textField2"));

                col3 = new TableColumn("id sportsman");
                col3.setMaxWidth(100);
                col3.setCellValueFactory(new PropertyValueFactory<Row, String>("textField3"));

                col4 = new TableColumn("valid from");
                col4.setMaxWidth(100);
                col4.setCellValueFactory(new PropertyValueFactory<Row, String>("textField4"));

                col5 = new TableColumn("valid to");
                col5.setMaxWidth(100);
                col5.setCellValueFactory(new PropertyValueFactory<Row, String>("textField5"));

                data_obs.add(new Row(5));
                data_obs.get(0).getTextField4().setPromptText("YYYY-MM-DD");
                data_obs.get(0).getTextField5().setPromptText("YYYY-MM-DD");
                t.getColumns().addAll(col1, col2, col3, col4, col5);
                break;

            default:
                System.out.println("error!");

        }
        t.setItems(data_obs);


    }

    ObservableList<ObservableList> data = FXCollections.observableArrayList();


    //CONNECTION DATABASE
    private void selectData(String table) throws SQLException {

        table_id.getItems().clear();
        table_id.getColumns().clear();
        connect();
        try {
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from " + table + " order by id asc";
            ResultSet rs = c.createStatement().executeQuery(SQL);

             // TABLE COLUMN ADDED DYNAMICALLY
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                    if (param.getValue().get(j) != null)
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    else return new SimpleStringProperty();
                });
                table_id.getColumns().addAll(col);
            }


            //Data add to ObservableList
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            table_id.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        disconnect();
    }

    boolean isExistReccord(String table, String field, String value) throws SQLException {
        Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "select " + field + " from " + table + " where " + field + " = " + "'" + value + "'";
        PreparedStatement p_state = c.prepareStatement(query);
        ResultSet res;

        try {
            res = p_state.executeQuery();
        } catch (SQLException e) {
            return false;
        }

        return res.next();

    }

    boolean isExistReccordById(String table, String value) throws SQLException {
        Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "select id from " + table + " where id  " + " = " + value;
        PreparedStatement p_state = c.prepareStatement(query);
        ResultSet res;

        try {
            res = p_state.executeQuery();
        } catch (SQLException e) {
            return false;
        }

        return res.next();

    }

    int getMaxId(String table) throws SQLException {
        Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "select max(id) from " + table;
        PreparedStatement p_state = c.prepareStatement(query);
        ResultSet res = p_state.executeQuery();
        int result = 0;
        while (res.next()) {
            result = Integer.parseInt(res.getString(1));
        }
        return result;
    }

}
