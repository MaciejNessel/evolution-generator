import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimulationViewElements {
    IWorldMap map;
    GridPane mapView;
    Map<Vector2d, Pane> mapViewElements = new HashMap<>();
    IEngine engine;
    InitialParameters config;
    ArrayList<ArrayList<Vector2d>> queueToUpdate = new ArrayList<>();
    boolean isUpdatingNow = false;
    int day = 0;
    Text dayCnt;

    int scale;
    public SimulationViewElements(IWorldMap map, InitialParameters config, App app, int scale){
        this.map = map;
        this.config = config;
        this.engine = new SimulationEngine(config, this.map, app);
        this.scale = scale;
        this.mapView = createJungleAndSteppe();

    }
    public IWorldMap getMap(){
        return this.map;
    }
    public GridPane getMapView(){
        return this.mapView;
    }
    public IEngine getEngine(){
        return this.engine;
    }



    private GridPane createJungleAndSteppe(){
        GridPane result = new GridPane();
        for(int i = 0; i<this.config.mapHeight; i++){
            for(int j = 0; j<this.config.mapWidth; j++){
                StackPane actual = new StackPane();

                actual.setMinSize(scale, scale);
                if(this.config.isPositionInJungle(new Vector2d(j, i))){
                    actual.setStyle("-fx-background-color: #004D00FF;");
                }
                else{
                    actual.setStyle("-fx-background-color: #009900FF;");
                }
                this.mapViewElements.put(new Vector2d(j,i), actual);
                result.add(actual, j+1, i+1, 1, 1);
            }
        }
        dayCnt = new Text("0");
        result.getChildren().add(dayCnt);
        return result;
    }


    public void pauseSimulation(){
        this.engine.pause();
    }

    public void updatePositions(ArrayList<Vector2d> positions){
            update(positions);
            day += 1;
            dayCnt.setText(day+"");
    }

    public void  update(ArrayList<Vector2d> positions){
        if(isUpdatingNow){
            this.queueToUpdate.add(positions);
            return;
        }
        this.isUpdatingNow = true;
        for(Vector2d position : positions){
            this.mapViewElements.get(position).getChildren().clear();

            ArrayList<Animal> animals = this.map.animalsAt(position);
            Grass grass = this.map.grassAt(position);

            if(grass!=null){
                this.mapViewElements.get(position).getChildren().add(grass.view(scale));
            }
            if(animals != null && animals.size()>0){
                Animal animalToView = animals.get(0);
                for(Animal animal : animals){
                    if(animal.getEnergy() > animalToView.getEnergy()){
                        animalToView = animal;
                    }
                }
                this.mapViewElements.get(position).getChildren().add(animalToView.view(scale));
            }
        }
        this.isUpdatingNow = false;
        if(queueToUpdate.size()>0){
            ArrayList<Vector2d> next = queueToUpdate.get(0);
            update(next);
            queueToUpdate.remove(0);
        }

    }

}
