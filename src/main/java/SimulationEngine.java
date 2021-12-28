import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SimulationEngine implements Runnable{
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
        Map<Vector2d, Object> toUpdate = createToUpdate(map.getToUpdate());
        app.updateMap(toUpdate, map);
    }

    private void putFirstAnimalsOnMap(int cnt){
        for(int i = 0; i<cnt; i++){
            this.map.placeAnimal(new Animal(null, null, initialParameters), observers);
        }
    }

    private Map<Vector2d, Object>  createToUpdate(HashSet<Vector2d> toUpdate){
        Map<Vector2d, Object> toUpdateNew = new HashMap<>();
        for(Vector2d position : toUpdate){
            if(map.animalsAt(position)!=null && map.animalsAt(position).size()>0){
                Animal toInsert = null;
                for(Animal animal : map.animalsAt(position)){
                    if(toInsert == null || animal.getEnergy()>toInsert.getEnergy()){
                        toInsert = animal;
                    }
                }
                toUpdateNew.put(position, toInsert);
            }
            else{
                toUpdateNew.put(position, map.grassAt(position));
            }
        }
        return toUpdateNew;
    }

    @Override
    public void run() {
        this.isStarted = true;
        while (this.isStarted){
            map.getStatistics();
            map.updateAnimalsEnergy();
            map.eating();
            map.removeDeathAnimal();
            if(!map.moveAnimals()){
                this.isStarted = false;
                break;
            }
            map.animalReproduction(observers);
            map.placeGrass();
            Map<Vector2d, Object> toUpdate = createToUpdate(map.getToUpdate());
            app.updateMap(toUpdate, map);
            map.clearUpdate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause(){
        this.isStarted = false;
    }
}
