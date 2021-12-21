import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application implements IPositionChangeObserver {

    InitialParameters config;
    IWorldMap firstMap;
    GridPane firstMapGrid;
    IWorldMap secondMap;
    GridPane secondMapGrid;
    IEngine firstEngine;
    IEngine secondEngine;

    public void start(Stage primaryStage){
        this.config = new InitialParameters();

        this.firstMap = new AbstractWorldMap(this.config, false, false, this);
        this.firstEngine = new SimulationEngine(this.config, this.firstMap, this);
        this.firstMapGrid = putMapElements();
        Scene a = new Scene(this.firstMapGrid, 1000, 500);
        primaryStage.setScene(a);
        primaryStage.show();
        Thread status = new Thread(() ->{
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.firstEngine.run();
        });
        status.start();

    }

    private GridPane putMapElements(){
        int scale = 500 / (Math.max(config.mapHeight, config.mapWidth) + 1);
        GridPane result = new GridPane();
        //Is step or jungle
        for(int i = 0; i<this.config.mapHeight; i++){
            for(int j = 0; j<this.config.mapWidth; j++){
                Rectangle view = new Rectangle(scale, scale, new Color(0,0.6,0,1));
                if(this.config.isPositionInJungle(new Vector2d(j, i)))
                   view = new Rectangle(scale, scale, new Color(0,0.3,0,1));
                result.add(view, j+1, i+1, 1, 1);
            }
        }

        for(int i = 0; i<this.config.mapHeight; i++){
            for(int j = 0; j<this.config.mapWidth; j++){
                ArrayList<Animal> animals = this.firstMap.animalsAt(new Vector2d(j,i));
                Grass grass = this.firstMap.grassAt(new Vector2d(j,i));
                if(grass!=null){
                    result.add(grass.view(scale), j+1, i+1, 1, 1);
                }

                if(animals != null && animals.size()>0){
                    Animal animalToView = animals.get(0);
                    for(Animal animal : animals){
                        if(animal.getEnergy() > animalToView.getEnergy()){
                            animalToView = animal;
                        }
                    }
                    result.add(animalToView.view(scale), j+1, i+1, 1, 1);
                }
            }
        }

        return result; 
    }

    public void updateMap(){
        Platform.runLater(() -> {
            try{
                firstMapGrid.getChildren().clear();
                firstMapGrid.getChildren().add(putMapElements());
            }
            catch (NullPointerException e){
                System.out.println("Error updateMap");
            }});
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        //updateMap();
        //System.out.println("UPDATE");
    }
}