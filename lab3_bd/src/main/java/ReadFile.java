import java.io.*;

class ReadFile {

    private int persons, sportsmen, trainers, stages, ranges, abonements, sportsman_abon, competitions, fighter_categoty, fighters, gyms, timetables;

    ReadFile(String fileName) throws IOException {
        readFile(fileName);
    }

    private void readFile(String fileName) throws IOException {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                readLines(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            System.exit(1);
        }
    }

    private void readLines(String line) throws IOException {
        String[] words;
        if (!line.contains(":"))
            throw new IllegalArgumentException("Wrong format in line: " + line);
        else words = line.replaceAll("\\s", "").split(":");
        if (words.length != 2)
            throw new IllegalArgumentException("Wrong format in line: " + line);


        words[0] = words[0].trim().toLowerCase();
        int number = Integer.parseInt(words[1]);
        switch (words[0]) {
            case "persons":
                persons = number;
                break;
            case "stages":
                stages = number;
                break;
            case "ranges":
                ranges = number;
                break;
            case "sportsmen":
                sportsmen = number;
                break;
            case "trainers":
                trainers = number;
                break;
            case "gyms":
                gyms = number;
                break;
            case "timetables":
                timetables = number;
                break;
            case "abonements":
                abonements = number;
                break;
            case "sportsman_abonements":
                sportsman_abon = number;
                break;
            case "competitions":
                competitions = number;
                break;
            case "fighter_category":
                fighter_categoty = number;
                break;
            case "fighters":
                fighters = number;
                break;
        }
    }


    int getPersons() {
        return persons;
    }

    int getSportsmen() {
        return sportsmen;
    }

    int getTrainers() {
        return trainers;
    }

    int getStages() {
        return stages;
    }

    int getRanges() {
        return ranges;
    }

    int getAbonements() {
        return abonements;
    }

    int getSportsman_abon() {
        return sportsman_abon;
    }

    int getCompetitions() {
        return competitions;
    }

    int getFighter_category() {
        return fighter_categoty;
    }

    int getFighters() {
        return fighters;
    }

    int getGyms() {
        return gyms;
    }

    int getTimetables() {
        return timetables;
    }
}
