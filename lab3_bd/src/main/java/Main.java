import java.io.IOException;
import java.sql.SQLException;

public class Main {
    private static Generator generator;

    public static void main(String[] args) throws IOException, SQLException {
        generator = new Generator();
        generator.connect();
        insertTables();
        generator.disconnect();
    }

    private static void insertTables() throws SQLException, IOException {
        String fileName = "input_file";
        ReadFile readFile = new ReadFile(fileName);

        try {
            generator.insertTablePerson(readFile.getPersons());
            generator.insertTableStage(readFile.getStages());
            generator.insertTableRang(readFile.getRanges());
            generator.insertTableSportsman(readFile.getSportsmen());
            generator.insertTableTrainer(readFile.getTrainers());
            generator.insertTableGym(readFile.getGyms());
            generator.insertTableTimetable(readFile.getTimetables());
            generator.insertTableAbonement(readFile.getAbonements());
            generator.insertTableSportsmanAbonement(readFile.getSportsman_abon());
            generator.insertTableCompetition(readFile.getCompetitions());
            generator.insertTableFighterCategory(readFile.getFighter_category());
            generator.insertTableFighter(readFile.getFighters());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
