import java.util.Objects;

public class Vector2d {
    int x;
    int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return '('+String.valueOf(x)+','+String.valueOf(y)+')';
    }
    // Add
    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    @Override
    public boolean equals(Object other){
        Vector2d vector = (Vector2d) other;
        return this.x == vector.x && this.y == vector.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

}
