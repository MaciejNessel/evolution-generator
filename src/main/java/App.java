import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application {
    private boolean isStartedFirst = false;
    private boolean isStartedSecond = false;
    private SimulationViewElements firstSimulation;
    private SimulationViewElements secondSimulation;
    private Stage primaryStage;
    private InitialParameters config;
    private boolean isMagicalFirst = false;
    private boolean isMagicalSecond = false;
    private Label statisticsFirst = new Label();
    private Label statisticsSecond = new Label();

    private void newThread( SimulationViewElements simulation){
        Thread name = new Thread(() ->{
            simulation.getEngine().run();
        });
        name.start();
    }

    public void startSimulation(){
        int scale = Math.max(1, 400 / (Math.max(config.mapHeight, config.mapWidth) + 1));

        this.firstSimulation = new SimulationViewElements(new AbstractWorldMap(config, false, config.isMagicalFirst), config, this, scale);
        this.secondSimulation = new SimulationViewElements(new AbstractWorldMap(config, true, config.isMagicalSecond), config, this, scale);

        GridPane together = new GridPane();

        Button startButtonFirst = new Button("Start 1");
        startButtonFirst.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                if(isStartedFirst){
                    startButtonFirst.setText("Start 1");
                    firstSimulation.pauseSimulation();
                    isStartedFirst = false;
                }
                else{
                    startButtonFirst.setText("Pause 1");
                    newThread(firstSimulation);
                    isStartedFirst = true;
                }

            }
        });
        Button startButtonSecond = new Button("Start 2");
        startButtonSecond.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                if(isStartedSecond){
                    startButtonSecond.setText("Start 2");
                    secondSimulation.pauseSimulation();
                    isStartedSecond = false;
                }
                else{
                    startButtonSecond.setText("Pause 2");
                    newThread(secondSimulation);
                    isStartedSecond = true;
                }

            }
        });

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                isStartedFirst = false;
                isStartedSecond = false;
                firstSimulation.pauseSimulation();
                firstSimulation.getMapView().getChildren().clear();
                secondSimulation.pauseSimulation();
                secondSimulation.getMapView().getChildren().clear();
                primaryStage.close();
                newInsert();
            }
        });

        together.add(firstSimulation.getMapView(), 0,0);
        together.add(secondSimulation.getMapView(), 1, 0);
        together.add(statisticsFirst, 0, 1);
        together.add(statisticsSecond, 1, 1);
        together.add(startButtonFirst, 0, 2);
        together.add(startButtonSecond, 1, 2);
        together.add(restartButton, 0, 3);
        Scene a = new Scene(together, 1000, 500);
        this.primaryStage.setScene(a);
    }

    private void newInsert() {
        primaryStage.setScene(config.getScene());
        primaryStage.show();
        System.gc();

    }

    public void start(Stage primaryStage) throws InterruptedException {
        this.config = new InitialParameters(this);
        this.primaryStage = primaryStage;

        primaryStage.setScene(config.getScene());
        primaryStage.show();

    }

    public void updateMap(ArrayList<Vector2d> toUpdate, IWorldMap map, Label statistics){
        Platform.runLater(() -> {
            System.out.println(statistics);
            SimulationViewElements actualSimulation;
            if(this.firstSimulation.getMap() == map){
                actualSimulation = this.firstSimulation;
                this.statisticsFirst.setText(statistics.getText());
            }
            else{
                actualSimulation = this.secondSimulation;
                this.statisticsSecond.setText(statistics.getText());
            }

            actualSimulation.updatePositions(toUpdate);

            });
    }


}