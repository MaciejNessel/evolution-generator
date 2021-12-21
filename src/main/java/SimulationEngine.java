import java.util.ArrayList;

public class SimulationEngine implements IEngine{

    InitialParameters initialParameters;
    IWorldMap map;
    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    App app;
    public SimulationEngine(InitialParameters initialParameters, IWorldMap map, App app){
        this.initialParameters = initialParameters;
        this.map = map;
        this.observers.add((IPositionChangeObserver)map);
        this.putFirstAnimalsOnMap(initialParameters.numberOfSpawningAnimals);
        this.map.placeGrass();
        this.app = app;
    }

    private void putFirstAnimalsOnMap(int cnt){
        for(int i = 0; i<cnt; i++){
            this.map.placeAnimal(new Animal(null, null, initialParameters), observers);
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.newDay();
            map.removeDeathAnimal();
            map.moveAnimals();

            map.eating();
            map.animalReproduction(observers);
            map.placeGrass();
            app.updateMap();
            System.out.println("x");

    }


}}
