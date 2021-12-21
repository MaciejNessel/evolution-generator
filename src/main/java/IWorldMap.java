import java.util.ArrayList;

public interface IWorldMap {

    /**checks if it is possible to move (or place) an animal in a given position*/
    boolean canPutAnimal(Vector2d position);

    /**checks if it is possible to place a grass in a given position*/
    boolean canPutGrass(Vector2d position);

    /**places animal on the map*/
    void placeAnimal(Animal animal, ArrayList<IPositionChangeObserver> observers);

    /**places grass on the map*/
    boolean placeGrass();

    /**Returns array with animal's objects at the given position or empty array*/
    ArrayList<Animal> animalsAt(Vector2d position);

    /**Return grass object at the given position or null*/
    Grass grassAt(Vector2d position);

    void removeDeathAnimal();

    boolean moveAnimals();

    void animalReproduction(ArrayList<IPositionChangeObserver> observers);

    void eating();

    void newDay();
}
