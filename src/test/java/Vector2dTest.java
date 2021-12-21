import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Vector2dTest {
    @Test
    public void equalsTest(){
        assertNotEquals(new Vector2d(2,5), new Vector2d(2,6));
        assertEquals(new Vector2d(20,5), new Vector2d(20,5));
    }

    @Test
    public void toStringTest(){
        assertEquals("(5,6)", new Vector2d(5,6).toString());
    }

    @Test
    public void addTest(){
        assertEquals(new Vector2d(10,20), new Vector2d(4,18).add(new Vector2d(6,2)));
    }

    @Test
    public void oppositeTest(){
        assertEquals(new Vector2d(5,10), new Vector2d(-5,-10).opposite());
    }
}
