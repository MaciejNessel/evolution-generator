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


    public InitialParameters(){
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src\\main\\resources\\parameters.json")){
            Object obj = jsonParser.parse(reader);
            JSONObject parameters = (JSONObject) obj;
            this.mapWidth = Integer.parseInt(parameters.get("mapWidth").toString());
            this.mapHeight = Integer.parseInt(parameters.get("mapHeight").toString());
            this.animalStartEnergy = Integer.parseInt(parameters.get("animalStartEnergy").toString());
            this.grassEnergyProfit = Integer.parseInt(parameters.get("grassEnergyProfit").toString());
            this.dailyEnergyCost = Integer.parseInt(parameters.get("dailyEnergyCost").toString());
            this.numberOfSpawningAnimals = Integer.parseInt(parameters.get("numberOfSpawningAnimals").toString());
            this.jungleRatio = Double.parseDouble(parameters.get("jungleRatio").toString());
            this.energyToReproduce = animalStartEnergy / 5;
            this.allMapField = this.mapHeight * this.mapWidth;
            this.createJungleField();
            this.validate();

        }catch (FileNotFoundException e) {
            System.err.println("File does not exist");
        }
        catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }

    // Validation of the input data
    public void validate() throws IllegalArgumentException{
        if(this.mapWidth <= 0){ throw new IllegalArgumentException("Invalid map width");}
        if(this.mapHeight <= 0){ throw new IllegalArgumentException("Invalid map height");}
        if(this.animalStartEnergy < 0){ throw new IllegalArgumentException("Invalid animalStartEnergy");}
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
