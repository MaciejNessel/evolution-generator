import java.util.ArrayList;

public class SimulationEngine implements IEngine{
    private boolean isStarted = false;
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
        app.updateMap(map.getToUpdate(), map, map.getStatistics());
    }

    private void putFirstAnimalsOnMap(int cnt){
        for(int i = 0; i<cnt; i++){
            this.map.placeAnimal(new Animal(null, null, initialParameters), observers);
        }
    }

    @Override
    public void run() {
        this.isStarted = true;
        while (this.isStarted){
            map.updateAnimalsEnergy();
            map.eating();
            map.removeDeathAnimal();
            if(!map.moveAnimals()){
                app.updateMap(map.getToUpdate(), map, map.getStatistics());
                System.out.println("END");
                break;
            }
            map.animalReproduction(observers);
            map.placeGrass();

            app.updateMap(map.getToUpdate(), map, map.getStatistics());

            System.out.println(map);
            try {
                Thread.sleep(initialParameters.delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.clearUpdate();



    }
    }
    @Override
    public void pause(){
        this.isStarted = false;
    }
}
