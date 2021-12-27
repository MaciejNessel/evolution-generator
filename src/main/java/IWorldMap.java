import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public interface IWorldMap {

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

    void updateAnimalsEnergy();

    HashSet<Vector2d> getToUpdate();

    void clearUpdate();

    void getStatistics();

    void saveStatistics() throws IOException;
    VBox getGraph();
}
