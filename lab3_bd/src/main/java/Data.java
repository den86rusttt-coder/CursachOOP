import java.time.LocalDate;
import java.util.*;

class Data {

    final List<String> abonement_time = Arrays.asList("month", "half-year", "year");

    final List<String> ageList = Arrays.asList("12-13", "14-15", "16-17", "18+");

    final List<String> weightList = Arrays.asList("40-50kg", "50-60kg", "60-70kg", "70-80kg", "80kg+");

    final List<String> sex = Arrays.asList("Female", "Male");

    private final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();

    String randomName(int max) {
        int minSize = 1;
        int randomSize = randomNumber(minSize, max);

        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < randomSize; i++) {
            int index = (int) (letters.length() * Math.random());
            randomStr.append(letters.charAt(index));
        }

        return randomStr.toString();
    }

    int randomNumber(int min, int max) { // Math.random = [0,1) число
        return (int) (Math.random() * (max - min + 1) + min);
    }

    long randomPhoneNumber() {
        long min = 80000000000L;
        long max = 89999999999L;

        return (long) (Math.random() * (max - min) + min);
    }


    LocalDate randomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(1960, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2018, 12, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
