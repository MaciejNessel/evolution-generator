import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class App extends Application {
    private boolean isStartedFirst = false;
    private boolean isStartedSecond = false;
    private SimulationViewElements firstSimulation;
    private SimulationViewElements secondSimulation;
    private Stage primaryStage;
    private InitialParameters config;

    private void newThread( SimulationViewElements simulation){
        Thread name = new Thread((simulation.getEngine()));
        name.start();
    }

    public void startSimulation(){
        int scale = Math.max(1, 300 / (Math.max(config.mapHeight, config.mapWidth) + 1));

        this.firstSimulation = new SimulationViewElements(new AbstractWorldMap(config, false, config.isMagicalFirst), config, this, scale);
        this.secondSimulation = new SimulationViewElements(new AbstractWorldMap(config, true, config.isMagicalSecond), config, this, scale);

        GridPane together = new GridPane();

        Button startButtonFirst = new Button("Start first simulation");
        startButtonFirst.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                if(isStartedFirst){
                    startButtonFirst.setText("Start first simulation");
                    firstSimulation.pauseSimulation();
                    isStartedFirst = false;
                }
                else{
                    startButtonFirst.setText("Pause first simulation");
                    newThread(firstSimulation);
                    isStartedFirst = true;
                }

            }
        });
        Button saveFirst = new Button("Save first to CSV");
        saveFirst.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                try {
                    firstSimulation.saveToFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        Button saveSecond = new Button("Save second to CSV");
        saveSecond.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                try {
                    secondSimulation.saveToFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        Button startButtonSecond = new Button("Start second simulation");
        startButtonSecond.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                if(isStartedSecond){
                    startButtonSecond.setText("Start second simulation");
                    secondSimulation.pauseSimulation();
                    isStartedSecond = false;
                }
                else{
                    startButtonSecond.setText("Pause second simulation");
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
        together.add(new Label("Map without wall:"), 0, 0);
        together.add(firstSimulation.getMapView(), 0,1);
        together.add(new Label("Map with wall:"), 0, 2);
        together.add(secondSimulation.getMapView(), 0, 3);
        VBox firstButtons = new VBox(startButtonFirst, saveFirst);
        together.add(firstButtons, 1, 1);
        firstButtons.setAlignment(Pos.CENTER);
        VBox secondButtons = new VBox(startButtonSecond, saveSecond);
        secondButtons.setAlignment(Pos.CENTER);
        together.add(secondButtons, 1, 3);
        VBox firstGraph = firstSimulation.getMap().getGraph();
        firstGraph.setMaxHeight(350);
        VBox secondGraph = secondSimulation.getMap().getGraph();
        secondGraph.setMaxHeight(350);
        together.add(firstGraph, 2, 1);

        firstGraph.setMinWidth(800);
        together.add(secondGraph, 2, 3);
        VBox buttonRestart = new VBox(restartButton);

        together.addColumn(3, buttonRestart);
        together.setAlignment(Pos.CENTER);
        ScrollPane sceneScroll = new ScrollPane();
        sceneScroll.setContent(together);
        sceneScroll.setFitToWidth(true);
        sceneScroll.setFitToHeight(true);
        Scene a = new Scene(sceneScroll);

        this.primaryStage.setScene(a);
        this.primaryStage.setMaximized(true);
    }

    private void newInsert() {
        this.primaryStage.setMaximized(false);

        primaryStage.setScene(config.getScene());
        primaryStage.show();
        System.gc();

    }

    public void start(Stage primaryStage) throws InterruptedException {
        this.config = new InitialParameters(this);
        this.primaryStage = primaryStage;
        primaryStage.setScene(config.getScene());
        primaryStage.setTitle("Evolution Generator");
        primaryStage.getIcons().add(new Image("file:src/main/resources/icon.png"));
        primaryStage.show();
    }

    public void updateMap(Map<Vector2d, Object> toUpdate, IWorldMap map){
        Platform.runLater(()->{
            SimulationViewElements actualSimulation;
            if(this.firstSimulation.getMap() == map){
                actualSimulation = this.firstSimulation;
            }
            else{
                actualSimulation = this.secondSimulation;
            }
            actualSimulation.updatePositions(toUpdate);
        });
    }
}