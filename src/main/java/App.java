import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application {

    Thread firstThread;
    Thread secondThread;

    SimulationViewElements firstSimulation;
    SimulationViewElements secondSimulation;


    public void newThread(Thread name, SimulationViewElements simulation){
        name = new Thread(() ->{
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simulation.getEngine().run();
        });
        name.start();
    }


    public void start(Stage primaryStage){
        InitialParameters config = new InitialParameters();
        int scale = Math.max(1, 500 / (Math.max(config.mapHeight, config.mapWidth) + 1));

        this.firstSimulation = new SimulationViewElements(new AbstractWorldMap(config, false, false), config, this, scale);
        this.secondSimulation = new SimulationViewElements(new AbstractWorldMap(config, true, false), config, this, scale);

        HBox together = new HBox();
        together.getChildren().addAll(this.firstSimulation.getMapView(), this.secondSimulation.getMapView());
        Scene a = new Scene(together, 1000, 500);

        newThread(this.firstThread, firstSimulation);
        newThread(this.secondThread, secondSimulation);

        primaryStage.setScene(a);
        primaryStage.show();


    }


    public void updateMap(ArrayList<Vector2d> toDelete, ArrayList<Vector2d> toAdd, IWorldMap map){

        Platform.runLater(() -> {
            SimulationViewElements actualSimulation;
            if(this.firstSimulation.getMap() == map){
                actualSimulation = this.firstSimulation;
            }
            else{
                actualSimulation = this.secondSimulation;
            }

            try{
                for(Vector2d pos : toDelete){
                    actualSimulation.clear(pos);
                }
                for(Vector2d pos : toAdd){
                    actualSimulation.clear(pos);
                    actualSimulation.update(pos);

                }


            }
            catch (NullPointerException e){
                System.out.println("Error updateMap");
            }});
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}