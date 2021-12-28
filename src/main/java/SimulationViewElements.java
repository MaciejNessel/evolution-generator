import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimulationViewElements {
    IWorldMap map;
    GridPane mapView;
    Map<Vector2d, Pane> mapViewElements = new HashMap<>();
    SimulationEngine engine;
    InitialParameters config;

    int day = 0;
    VBox animalDetails = new VBox();
    ScrollPane animalDetailsScroll = new ScrollPane();
    Animal selected;

    int scale;
    public SimulationViewElements(IWorldMap map, InitialParameters config, App app, int scale){
        this.map = map;
        this.config = config;
        this.engine = new SimulationEngine(config, this.map, app);
        this.scale = scale;
        this.mapView = createJungleAndSteppe();
        animalDetailsScroll.setContent(animalDetails);
        animalDetailsScroll.setMinHeight(100);

    }
    public IWorldMap getMap(){
        return this.map;
    }
    public GridPane getMapView(){
        return this.mapView;
    }
    public SimulationEngine getEngine(){
        return this.engine;
    }

    private GridPane createJungleAndSteppe(){
        GridPane result = new GridPane();
        for(int i = 0; i<this.config.mapHeight; i++){
            for(int j = 0; j<this.config.mapWidth; j++){
                StackPane actual = new StackPane();

                actual.setMinSize(Math.max(scale, 2), Math.max(scale, 2));
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
        GridPane together = new GridPane();
        together.add(result, 0, 0);
        together.add(animalDetailsScroll, 0 ,2);

        return together;
    }

    public void pauseSimulation(){
        this.engine.pause();
    }

    public void updatePositions(Map<Vector2d, Object> positions){
        if (selected != null){
            animalDetails.getChildren().clear();
            animalDetails.getChildren().add(selected.getAnimalInformation());
        }
        for(Map.Entry<Vector2d, Object> entry : positions.entrySet()){
            Vector2d position = entry.getKey();
            Object object = entry.getValue();
            if(object == null){
                this.mapViewElements.get(position).getChildren().clear();
            }
            else if(object instanceof Animal animalToView){
                Circle shape = animalToView.view(scale);
                Label l = animalToView.getAnimalInformation();
                this.mapViewElements.get(position).getChildren().add(shape);
                shape.setOnMouseClicked(e -> {
                    if(selected!=null){
                        selected.stopFollow();
                    }
                    this.animalDetails.getChildren().clear();
                    this.animalDetails.getChildren().add(l);
                    selected = animalToView;
                    selected.startFollow();
                });
            }
            else if(object instanceof Grass grass){
                this.mapViewElements.get(position).getChildren().add(grass.view(scale));
            }
        }
            day += 1;
    }

    public void saveToFile() throws IOException {
        this.map.saveStatistics();
    }

}
