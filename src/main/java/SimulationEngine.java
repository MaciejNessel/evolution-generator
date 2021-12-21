import java.util.ArrayList;

public class SimulationEngine implements IEngine{

    InitialParameters initialParameters;
    IWorldMap map;
    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    public SimulationEngine(InitialParameters initialParameters, IWorldMap map){
        this.initialParameters = initialParameters;
        this.map = map;
        this.observers.add((IPositionChangeObserver)map);
        this.putFirstAnimalsOnMap(initialParameters.numberOfSpawningAnimals);
        this.map.placeGrass();
    }

    private void putFirstAnimalsOnMap(int cnt){
        for(int i = 0; i<cnt; i++){
            this.map.placeAnimal(new Animal(null, null, initialParameters), observers);
        }
    }

    // Performs operations ordered for each new day (epoch)
    public void oneDayActivities(){
        map.newDay();
        map.removeDeathAnimal();
        map.moveAnimals();
        map.eating();
        map.animalReproduction(observers);
        map.placeGrass();
    }

    @Override
    public void run() {
        oneDayActivities();
    }


}
