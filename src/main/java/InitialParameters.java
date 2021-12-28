import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InitialParameters {
    // Map config
    public int mapHeight;
    public int mapWidth;
    public int allMapField;
    public int stepField;

    // Jungle config
    public double jungleRatio;
    public int jungleField;
    public Vector2d jungleLowerLeft;
    public Vector2d jungleUpperRight;

    // Change in the properties of animals over the course of the epoch
    public int grassEnergyProfit;
    public int animalStartEnergy;
    public int dailyEnergyCost;
    public int energyToReproduce;
    public int numberOfSpawningAnimals;
    public int delay;
    private Scene insertScene;

    public boolean isMagicalFirst = false;
    public boolean isMagicalSecond = false;

    public InitialParameters(App app){

        VBox container = new VBox();
        JSONParser jsonParser = new JSONParser();
        // Load parameters from json file
        try (FileReader reader = new FileReader("src\\main\\resources\\parameters.json")){
            Object obj = jsonParser.parse(reader);
            JSONObject parameters = (JSONObject) obj;
            Text title = new Text("Insert parameters \n");
            title.setFont(Font.font ("Verdana", 20));
            container.getChildren().add(title);
            container.setAlignment(Pos.CENTER);
            Label widthL = new Label("Width: ");
            TextField widthInsert = new TextField(parameters.get("mapWidth").toString());
            widthInsert.setMaxWidth(250);
            widthInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(widthL);
            container.getChildren().add(widthInsert);

            Label heightL = new Label("Height: ");
            TextField heightInsert = new TextField(parameters.get("mapHeight").toString());
            heightInsert.setMaxWidth(250);
            heightInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(heightL);
            container.getChildren().add(heightInsert);

            Label animalEnergyL = new Label("Animal start energy: ");
            TextField animalEnergyInsert = new TextField(parameters.get("animalStartEnergy").toString());
            animalEnergyInsert.setMaxWidth(250);
            animalEnergyInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(animalEnergyL);
            container.getChildren().add(animalEnergyInsert);

            TextField grassProfitInsert = new TextField(parameters.get("grassEnergyProfit").toString());
            Label grassProfitL = new Label("Grass energy: ");
            grassProfitInsert.setMaxWidth(250);
            grassProfitInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(grassProfitL);
            container.getChildren().add(grassProfitInsert);

            Label dailyEnergyCostL = new Label("Daily energy cost: ");
            TextField dailyEnergyCostInsert = new TextField(parameters.get("dailyEnergyCost").toString());
            dailyEnergyCostInsert.setMaxWidth(250);
            dailyEnergyCostInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(dailyEnergyCostL);
            container.getChildren().add(dailyEnergyCostInsert);

            Label numberOfSpawningAnimalsL = new Label("Number of spawning animals: ");
            TextField numberOfSpawningAnimalsInsert = new TextField(parameters.get("numberOfSpawningAnimals").toString());
            numberOfSpawningAnimalsInsert.setMaxWidth(250);
            numberOfSpawningAnimalsInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(numberOfSpawningAnimalsL);
            container.getChildren().add(numberOfSpawningAnimalsInsert);

            Label jungleRatioL = new Label("Jungle ratio: ");
            TextField jungleRatioInsert = new TextField(parameters.get("jungleRatio").toString());
            jungleRatioInsert.setMaxWidth(250);
            jungleRatioInsert.setAlignment(Pos.CENTER);
            container.getChildren().add(jungleRatioL);
            container.getChildren().add(jungleRatioInsert);

            Label magicalFirst = new Label("Is magical first: ");
            ChoiceBox<Boolean> choiceBoxFirst = new ChoiceBox<>();
            choiceBoxFirst.getItems().add(true);
            choiceBoxFirst.getItems().add(false);
            choiceBoxFirst.setValue(false);
            container.getChildren().add(magicalFirst);
            container.getChildren().add(choiceBoxFirst);

            Label magicalSecond = new Label("Is magical second: ");
            ChoiceBox<Boolean> choiceBoxSecond = new ChoiceBox<>();
            choiceBoxSecond.getItems().add(true);
            choiceBoxSecond.getItems().add(false);
            choiceBoxSecond.setValue(false);
            container.getChildren().add(magicalSecond);
            container.getChildren().add(choiceBoxSecond);


            Label err = new Label("incorrect input format");
            err.setTextFill(Color.RED);
            err.setFont(new Font(15));
            Button btn = new Button("Generate new map");

            btn.setOnAction(new EventHandler<ActionEvent>(){
                @Override public void handle(ActionEvent e) {
                    try{
                        mapWidth = Integer.parseInt(widthInsert.getText());
                        mapHeight = Integer.parseInt(heightInsert.getText());
                        animalStartEnergy = Integer.parseInt(animalEnergyInsert.getText());
                        grassEnergyProfit = Integer.parseInt(grassProfitInsert.getText());
                        dailyEnergyCost = Integer.parseInt(dailyEnergyCostInsert.getText());
                        numberOfSpawningAnimals = Integer.parseInt(numberOfSpawningAnimalsInsert.getText());
                        jungleRatio = Double.parseDouble(jungleRatioInsert.getText());
                        isMagicalFirst = choiceBoxFirst.getValue();
                        isMagicalSecond = choiceBoxSecond.getValue();
                        energyToReproduce = animalStartEnergy / 5;
                        allMapField = mapHeight * mapWidth;
                        if(mapWidth <= 0){ throw new IllegalArgumentException("Invalid map width");}
                        if(mapHeight <= 0){ throw new IllegalArgumentException("Invalid map height");}
                        if(animalStartEnergy < 0){ throw new IllegalArgumentException("Invalid animalStartEnergy");}
                        if(jungleRatio < 0){ throw new IllegalArgumentException("Invalid animalStartEnergy");}
                        if(jungleRatio >1){ throw new IllegalArgumentException("Invalid animalStartEnergy");}
                    } catch (IllegalArgumentException numberFormatException ) {
                        container.getChildren().remove(err);
                        container.getChildren().add(err);
                        System.out.println("incorrect input format");
                        return;
                    }
                    container.getChildren().remove(err);

                    createJungleField();
                    app.startSimulation();

            }});
            container.getChildren().add(btn);

        }catch (FileNotFoundException e) {
            System.err.println("File does not exist");
        }
        catch (IOException | ParseException e){
            e.printStackTrace();
        }
        this.insertScene = new Scene(container, 500,600 );

    }

    public Scene getScene(){
        return this.insertScene;
    }

    // Find any position (Vector2d(x,y)) based on the input data
    public Vector2d getRandomPosition(){
        return new Vector2d((int )(Math.random() * this.mapWidth), (int)(Math.random()*this.mapHeight));
    }

    // Find jungle position (Vector2d(x,y)) based on the input data
    public Vector2d getRandomJunglePosition(){
        return new Vector2d((int )(Math.random() * (this.jungleUpperRight.x - this.jungleLowerLeft.x + 1)) + this.jungleLowerLeft.x, (int)(Math.random()*(this.jungleUpperRight.y-this.jungleLowerLeft.y + 1) + this.jungleLowerLeft.y));
    }

    // Find step position (Vector2d(x,y)) based on the input data
    public Vector2d getRandomStepPosition(){

        Vector2d a = getRandomPosition();
        Vector2d ja = this.jungleLowerLeft;
        Vector2d jb = this.jungleUpperRight;
        while (isPositionInJungle(a)){
            a = getRandomPosition();
        }
        return a;
    }

    // Returns true if the position Vector is in a jungle area
    public boolean isPositionInJungle(Vector2d a){
        Vector2d ja = this.jungleLowerLeft;
        Vector2d jb = this.jungleUpperRight;
        return (a.x>=ja.x && a.x <=jb.x && a.y >= ja.y && a.y <= jb.y && jungleField!=0);
    }

    // Based on map size and jungle ratio finds the jungle position (appropriately sized and centered on the map)
    private void createJungleField(){
        double jungleRealField = mapHeight * mapWidth * jungleRatio;
        int jungleWidth = (int) Math.round((Math.sqrt(jungleRatio) * mapWidth));
        int jungleHeight = (int) Math.round(jungleRealField / jungleWidth);

        // Jungle and map dimensions must be integers >= 1 or == 0 (considers the case when the input data are very small map sizes)
        if(jungleHeight > 0 && jungleWidth > 0){
            this.jungleLowerLeft = new Vector2d((Math.max(0,(int)((this.mapWidth- jungleWidth)/2))), (int) Math.max(0, Math.min(this.mapHeight, (int)((this.mapHeight - jungleHeight) / 2))));
            this.jungleUpperRight = new Vector2d(Math.min(this.mapWidth, (jungleLowerLeft.x + jungleWidth - 1)), Math.min(mapHeight, (jungleLowerLeft.y + jungleHeight - 1)));
            this.jungleField = (this.jungleUpperRight.x - this.jungleLowerLeft.x + 1) * (this.jungleUpperRight.y - this.jungleLowerLeft.y + 1);
        }
        else{
            this.jungleField = 0;
            this.jungleLowerLeft = new Vector2d(-1, -1);
            this.jungleUpperRight = new Vector2d(-1, -1);
        }
        this.stepField = this.allMapField - this.jungleField;
    }

}
