import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


class Generator {


    private static final String URL = "jdbc:postgresql://localhost:5432/KarateClub";
    private static final String USER = "postgres";
    private static final String PASSWORD = "karate24";

    private Connection connection = null;
    private Data data = new Data();

    void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database disconnection successful");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void insertTablePerson(int persons) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into person values(?,?,?,?,?)");
        int id = getMaxId("person"); // последняя запись id в текущей таблице

        for (int i = 0; i < persons; i++) {
            id++;
            String name = data.randomName(35);
            String sex = data.sex.get(data.randomNumber(0, data.sex.size() - 1));
            LocalDate birth = data.randomDate();
            Date date = java.sql.Date.valueOf(birth);
            long phone_number = data.randomPhoneNumber();

            p_state.setInt(1, id);
            p_state.setString(2, name);
            p_state.setString(3, sex);
            p_state.setDate(4, date);
            p_state.setLong(5, phone_number);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Person: " + persons + " row(s) were added");

    }

    void insertTableStage(int stages) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into stage values(?,?)");
        int id = getMaxId("stage"); // последняя запись id в текущей таблице

        for (int i = 0; i < stages; i++) {
            id++;
            String name = data.randomName(10);

            p_state.setInt(1, id);
            p_state.setString(2, name);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Stage: " + stages + " row(s) were added");

    }

    void insertTableRang(int ranges) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into rang values(?,?,?)");
        int id = getMaxId("rang"); // последняя запись id в текущей таблице

        for (int i = 0; i < ranges; i++) {
            id++;
            String rangValue = data.randomName(2);
            String rangTitle = data.randomName(3);

            p_state.setInt(1, id);
            p_state.setString(2, rangValue);
            p_state.setString(3, rangTitle);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Rang: " + ranges + " row(s) were added");

    }

    void insertTableSportsman(int sportsmen) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into sportsman values(?,?,?,?,?,?)");
        int id = getMaxId("sportsman"); // последняя запись id в текущей таблице

        for (int i = 0; i < sportsmen; i++) {
            id++;
            int id_person = getRandomElement(getAll_id("person"));
            int id_group = getRandomElement(getAll_id("stage"));
            int id_rang = getRandomElement(getAll_id("rang"));

            LocalDate l_date_since = data.randomDate();
            Date date_since = java.sql.Date.valueOf(l_date_since);
            LocalDate l_date_until = data.randomDate();
            Date date_until = java.sql.Date.valueOf(l_date_until);

            while (l_date_since.compareTo(l_date_until) >= 0) {
                l_date_since = data.randomDate();
                date_since = java.sql.Date.valueOf(l_date_since);
                l_date_until = data.randomDate();
                date_until = java.sql.Date.valueOf(l_date_until);
            }

            p_state.setInt(1, id);
            p_state.setInt(2, id_person);
            p_state.setInt(3, id_group);
            p_state.setDate(4, date_since);
            p_state.setDate(5, date_until);
            p_state.setInt(6, id_rang);


            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Sportsman: " + sportsmen + " row(s) were added");

    }

    void insertTableTrainer(int trainers) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into trainer values(?,?,?,?,?,?)");
        int id = getMaxId("trainer"); // последняя запись id в текущей таблице

        for (int i = 0; i < trainers; i++) {
            id++;
            int id_person = getRandomElement(getAll_id("person"));
            int pay = data.randomNumber(1, 100000);
            int id_rang = getRandomElement(getAll_id("rang"));

            LocalDate l_date_since = data.randomDate();
            Date date_since = java.sql.Date.valueOf(l_date_since);
            LocalDate l_date_until = data.randomDate();
            Date date_until = java.sql.Date.valueOf(l_date_until);

            while (l_date_since.compareTo(l_date_until) >= 0) {
                l_date_since = data.randomDate();
                date_since = java.sql.Date.valueOf(l_date_since);
                l_date_until = data.randomDate();
                date_until = java.sql.Date.valueOf(l_date_until);
            }

            p_state.setInt(1, id);
            p_state.setInt(2, id_person);
            p_state.setInt(3, pay);
            p_state.setDate(4, date_since);
            p_state.setDate(5, date_until);
            p_state.setInt(6, id_rang);


            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Trainer: " + trainers + " row(s) were added");

    }

    void insertTableGym(int gyms) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into gym values(?,?,?,?)");
        int id = getMaxId("gym"); // последняя запись id в текущей таблице

        for (int i = 0; i < gyms; i++) {
            id++;
            String name = data.randomName(10);
            String address = data.randomName(10);
            long phoneNumber = data.randomPhoneNumber();

            p_state.setInt(1, id);
            p_state.setString(2, name);
            p_state.setString(3, address);
            p_state.setLong(4, phoneNumber);


            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Gym: " + gyms + " row(s) were added");

    }

    void insertTableTimetable(int notes) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into timetable values(?,?,?,?,?,?,?)");
        int id = getMaxId("timetable"); // последняя запись id в текущей таблице

        for (int i = 0; i < notes; i++) {
            id++;

            int randomDay = data.randomNumber(1, 7);
            String day = DayOfWeek.of(randomDay).toString();

            int secInDay = 24 * 60 * 60;
            Random random1 = new Random();
            Time time1 = new Time((long) random1.nextInt(secInDay));

            Random random2 = new Random();
            Time time2 = new Time((long) random2.nextInt(secInDay));

            while (time1.compareTo(time2) >= 0) {
                random1 = new Random();
                time1 = new Time((long) random1.nextInt(secInDay));

                random2 = new Random();
                time2 = new Time((long) random2.nextInt(secInDay));
            }

            Time time_since = (Time) Time.valueOf(String.valueOf(time1));
            Time time_until = (Time) Time.valueOf(String.valueOf(time2));


            int id_trainer = getRandomElement(getAll_id("trainer"));
            int id_gym = getRandomElement(getAll_id("gym"));
            int id_group = getRandomElement(getAll_id("stage"));

            p_state.setInt(1, id);
            p_state.setString(2, day);
            p_state.setTime(3, time_since);
            p_state.setTime(4, time_until);
            p_state.setInt(5, id_trainer);
            p_state.setInt(6, id_gym);
            p_state.setInt(7, id_group);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Timetable: " + notes + " row(s) were added");

    }


    void insertTableAbonement(int abonements) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into abonement values(?,?,?,?)");
        int id = getMaxId("abonement"); // последняя запись id в текущей таблице

        for (int i = 0; i < abonements; i++) {
            id++;
            String title = data.randomName(10);
            int price = data.randomNumber(1, 100000);
            String rand_ab_time = data.abonement_time.get(data.randomNumber(0, data.abonement_time.size() - 1));


            p_state.setInt(1, id);
            p_state.setInt(2, price);
            p_state.setString(3, title);
            p_state.setString(4, rand_ab_time);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Abonement: " + abonements + " row(s) were added");

    }


    void insertTableSportsmanAbonement(int ind_abonements) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into sportsmanabonement values(?,?,?,?,?)");
        int id = getMaxId("sportsmanabonement"); // последняя запись id в текущей таблице

        for (int i = 0; i < ind_abonements; i++) {
            id++;

            int id_abonement = getRandomElement(getAll_id("abonement"));
            int id_sportsman = getRandomElement(getAll_id("sportsman"));

            LocalDate l_date_since = data.randomDate();
            Date date_since = java.sql.Date.valueOf(l_date_since);
            LocalDate l_date_until = data.randomDate();
            Date date_until = java.sql.Date.valueOf(l_date_until);

            while (l_date_since.compareTo(l_date_until) >= 0) {
                l_date_since = data.randomDate();
                date_since = java.sql.Date.valueOf(l_date_since);
                l_date_until = data.randomDate();
                date_until = java.sql.Date.valueOf(l_date_until);
            }

            p_state.setInt(1, id);
            p_state.setInt(2, id_abonement);
            p_state.setInt(3, id_sportsman);
            p_state.setDate(4, date_since);
            p_state.setDate(5, date_until);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Sportsman abonement: " + ind_abonements + " row(s) were added");

    }


    void insertTableCompetition(int competitions) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into competition values(?,?,?,?,?)");
        int id = getMaxId("competition"); // последняя запись id в текущей таблице

        for (int i = 0; i < competitions; i++) {
            id++;

            String name = data.randomName(35);
            String place = data.randomName(35);

            LocalDate l_date = data.randomDate();
            Date date = java.sql.Date.valueOf(l_date);

            int secInDay = 24 * 60 * 60;
            Random random1 = new Random();
            Time time = new Time((long) random1.nextInt(secInDay));

            p_state.setInt(1, id);
            p_state.setString(2, name);
            p_state.setString(3, place);
            p_state.setDate(4, date);
            p_state.setTime(5, time);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Competition: " + competitions + " row(s) were added");

    }


    void insertTableFighterCategory(int f_categories) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into fighter_category values(?,?,?,?)");
        int id = getMaxId("fighter_category"); // последняя запись id в текущей таблице

        for (int i = 0; i < f_categories; i++) {
            id++;

            String sex = data.sex.get(data.randomNumber(0, data.sex.size() - 1));
            String age = data.ageList.get(data.randomNumber(0, data.ageList.size() - 1));
            String weight = data.weightList.get(data.randomNumber(0, data.weightList.size() - 1));

            p_state.setInt(1, id);
            p_state.setString(2, age);
            p_state.setString(3, sex);
            p_state.setString(4, weight);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Fighter category: " + f_categories + " row(s) were added");

    }

    void insertTableFighter(int fighters) throws SQLException {
        PreparedStatement p_state = connection.prepareStatement("insert into fighter values(?,?,?,?,?)");
        int id = getMaxId("fighter"); // последняя запись id в текущей таблице

        for (int i = 0; i < fighters; i++) {
            id++;

            int id_sportsman = getRandomElement(getAll_id("sportsman"));
            int id_competition = getRandomElement(getAll_id("competition"));
            int result = data.randomNumber(0, 3);
            int id_category = getRandomElement(getAll_id("fighter_category"));

            p_state.setInt(1, id);
            p_state.setInt(2, id_sportsman);
            p_state.setInt(3, id_competition);
            p_state.setInt(4, result);
            p_state.setInt(5, id_category);

            p_state.executeUpdate();
        }

        p_state.close();
        System.out.println("Table Fighter: " + fighters + " row(s) were added");

    }

    private int getMaxId(String table) throws SQLException {
        String query = "select max(id) from " + table;
        PreparedStatement p_state = connection.prepareStatement(query);
        ResultSet res = p_state.executeQuery();
        int result = 0;
        while (res.next()) {
            result = Integer.parseInt(res.getString(1));
        }
        return result;
    }

    private ArrayList getAll_id(String table) throws SQLException {
        String query = "select id from " + table;
        PreparedStatement p_state = connection.prepareStatement(query);
        ResultSet res = p_state.executeQuery();

        ArrayList id_list = new ArrayList();

        while (res.next()) {
            id_list.add(res.getInt(1));
        }
        return id_list;
    }

    private int getRandomElement(ArrayList list) {
        Random rand = new Random();
        return (int) list.get(rand.nextInt(list.size()));
    }
}
