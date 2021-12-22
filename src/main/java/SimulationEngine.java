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
            map.newDay();
            map.removeDeathAnimal();
            map.moveAnimals();
            map.eating();
            map.animalReproduction(observers);
            map.placeGrass();

            app.updateMap(map.getToDelete(), map.getToAdd(), map);
            map.clearAddAndDelete();
            System.out.println(map);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }


}}
