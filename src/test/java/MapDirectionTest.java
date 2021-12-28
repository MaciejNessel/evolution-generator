import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void directionToVector(){
        MapDirection direction = MapDirection.NORTH;
        assertEquals(direction.toVector(), new Vector2d(0,1));
        direction = MapDirection.EAST;
        assertEquals(direction.toVector(), new Vector2d(1,0));
        direction = MapDirection.WEST;
        assertEquals(direction.toVector(),  new Vector2d(-1,0));
        direction = MapDirection.SOUTH;
        assertEquals(direction.toVector(), new Vector2d(0,-1));
        direction = MapDirection.NORTHEAST;
        assertEquals(direction.toVector(), new Vector2d(1,1));
        direction = MapDirection.SOUTHEAST;
        assertEquals(direction.toVector(), new Vector2d(1,-1));
        direction = MapDirection.NORTHWEST;
        assertEquals(direction.toVector(), new Vector2d(-1,1));
        direction = MapDirection.SOUTHWEST;
        assertEquals(direction.toVector(), new Vector2d(-1,-1));
    }

    @Test
    public void changeDirection(){
        MapDirection direction = MapDirection.NORTH;
        direction = direction.oneChangeDirection();
        assertEquals(direction, MapDirection.NORTHEAST);
        direction = direction.oneChangeDirection();
        assertEquals(direction, MapDirection.EAST);
    }

    @Test
    public void NumberOfDirection(){
        MapDirection direction = MapDirection.numberToDirection(1);
        assertEquals(direction, MapDirection.NORTHWEST);
    }


}
