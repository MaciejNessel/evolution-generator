import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    Text animalDetails = new Text();

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
        dayCnt = new Text("Day: 0");
        GridPane together = new GridPane();
        together.add(result, 0, 0);
        together.add(dayCnt, 0, 1);
        together.add(animalDetails, 0 ,2);

        return together;
    }


    public void pauseSimulation(){
        this.engine.pause();
    }

    public void updatePositions(HashSet<Vector2d> positions){
            update(positions);
            day += 1;
            dayCnt.setText("Day: "+day);
    }

    public void  update(HashSet<Vector2d> positions){
        for(Vector2d position : positions){
            //Clear all elements on map
            this.mapViewElements.get(position).getChildren().clear();

            ArrayList<Animal> animals = this.map.animalsAt(position);
            Grass grass = this.map.grassAt(position);

            if(grass!=null && (animals == null || animals.size()==0)){
                this.mapViewElements.get(position).getChildren().add(grass.view(scale));
            }
            if(animals != null && animals.size()>0){
                Animal animalToView = animals.get(0);
                for(Animal animal : animals){
                    if(animal.getEnergy() > animalToView.getEnergy()){
                        animalToView = animal;
                    }
                }
                Circle shape = animalToView.view(scale);
                //String text = animalToView.getGenotype().toString();
                String text = animalToView.getEnergy().toString() + "\n" + animalToView.getGenotype().toString() + "\n" + animalToView.a.toString();
                this.mapViewElements.get(position).getChildren().add(shape);
                shape.setOnMouseClicked(e -> {
                    this.animalDetails.setText(text);
                });
            }
        }


    }
    public void saveToFile() throws IOException {
        this.map.saveStatistics();
    }

}
