package sample;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ControllerTest {
    private Controller controller = new Controller();

    @Test
    public void isExistReccord() throws SQLException {
        assertTrue(controller.isExistReccord("person", "id", "1"));
        assertTrue(controller.isExistReccord("gym", "name", "LFE"));
        assertTrue(controller.isExistReccord("stage", "name", "Adult"));
        assertTrue(controller.isExistReccord("timetable", "day_of_week", "friday"));
        assertTrue(controller.isExistReccord("sportsman", "id_rang", "22"));
        assertFalse(controller.isExistReccord("sportsman", "id_rang", "999"));
    }

    @Test
    public void getMaxId() throws SQLException {
        assertEquals(controller.getMaxId("person"), 115 );
        assertEquals(controller.getMaxId("gym"), 15 );
        assertEquals(controller.getMaxId("trainer"), 67);
        assertEquals(controller.getMaxId("sportsman"), 95);
        assertEquals(controller.getMaxId("timetable"), 329);
    }
}