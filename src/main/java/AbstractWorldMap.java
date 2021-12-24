import javafx.scene.control.Label;

import java.io.IOException;
import java.util.*;

public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{

    public final InitialParameters initialParameters;
    public final boolean wallIsFence;
    private final boolean isMagical;
    private int grassOfStep;
    private int grassOfJungle;
    private int cntOfMagicalUse = 0;
    private Integer avgAnimalEnergy = 0;
    private Integer deathAnimals = 0;
    private Integer avgLiveLengthAnimals = 1;
    private Integer avgChildren = 0;
    HashSet<Vector2d> toUpdate = new HashSet<>();
    private Statistics statistics = new Statistics();
    private Integer numOfDay = 0;

    // Information about objects on the map
    private final Map<Vector2d, ArrayList<Animal>> animalMap = new TreeMap<>(new Comparator<Vector2d>(){
        @Override
        public int compare(Vector2d pos1, Vector2d pos2){
            if (pos1.x > pos2.x) return 1;
            if (pos1.x < pos2.x) return -1;
            return Integer.compare(pos1.y, pos2.y);
        }
    });
    private final Map<Vector2d, Grass> grassMap = new TreeMap<>(new Comparator<Vector2d>(){
        @Override
        public int compare(Vector2d pos1, Vector2d pos2){
            if (pos1.x > pos2.x) return 1;
            if (pos1.x < pos2.x) return -1;
            return Integer.compare(pos1.y, pos2.y);
        }});
    private final ArrayList<Animal> animalsList = new ArrayList<>();


    public AbstractWorldMap(InitialParameters config, boolean wallIsFence, boolean isMagical){
        this.initialParameters = config;
        this.wallIsFence = wallIsFence;
        this.isMagical = isMagical;
        this.grassOfStep = config.stepField;
        this.grassOfJungle = config.jungleField;
    }

    @Override
    public Label getStatistics(){
        int numOfAnimals = animalsList.size();
        int numOfGrass = grassMap.size();
        //dominujący genotyp
        int childrenCounter = 0;
        avgAnimalEnergy = 0;
        for(Animal animal : animalsList) {
            childrenCounter += animal.getChildren().size();
            avgAnimalEnergy += animal.getEnergy();
        }
        if(animalsList.size()!=0){
            this.avgChildren = childrenCounter / animalsList.size();
            this.avgAnimalEnergy /= animalsList.size();
        }

        Label result = new Label("Number of animals: " + numOfAnimals + "\n" +
                "Number of grass: " + numOfGrass + "\n" +
                "Average energy level: " + avgAnimalEnergy + "\n" +
                "Average age of dead animals: " + avgLiveLengthAnimals + "\n" +
                "Average number of children: " + avgChildren + "\n" +
                "Genotype: ?");
        statistics.addDayHistory(new DayHistory(numOfDay, numOfAnimals, numOfGrass, avgAnimalEnergy, avgLiveLengthAnimals, avgChildren));
        return result;
    }

    @Override
    public void saveStatistics() throws IOException {
        this.statistics.saveStatisticsToFile();
    }

    @Override
    public void placeAnimal(Animal animal, ArrayList<IPositionChangeObserver> observers) {
        Vector2d position = animal.getPosition();
        if(canPutAnimal(position)){
            toUpdate.add(position);
            animalsList.add(animal);
            ArrayList<Animal> animals = animalMap.get(position);
            if(animals == null) {
                animals = new ArrayList<>();
                animals.add(animal);
                animalMap.put(position, animals);
                for(IPositionChangeObserver observer : observers){
                    animal.addObserver(observer);
                }
            }
            else{
                animalMap.get(position).add(animal);
            }
        }
    }

    @Override
    public boolean placeGrass() {
        int numOfAnimalsOnStep = getNumberOfOccupiedFields(false);
        int numOfAnimalsOnJungle = getNumberOfOccupiedFields(true);
        // I f the whole map is full of animals, the plant has no place to grow
        if(numOfAnimalsOnStep + numOfAnimalsOnJungle >=initialParameters.allMapField){
            return true;
        }
        if(this.grassOfJungle - numOfAnimalsOnJungle>0){
            Vector2d a = initialParameters.getRandomJunglePosition();
            while (canPutGrass(a)){
                a = initialParameters.getRandomJunglePosition();
            }
            Grass grass = new Grass(initialParameters.grassEnergyProfit, a);
            grassMap.put(grass.getPosition(), grass);
            grassOfJungle -= 1;
            this.toUpdate.add(grass.getPosition());
        }
        if(this.grassOfStep - numOfAnimalsOnStep>0){
            Vector2d a = initialParameters.getRandomStepPosition();
            int cnt = 0;
            while (canPutGrass(a)){
                a = initialParameters.getRandomStepPosition();
            }
            Grass grass = new Grass(initialParameters.grassEnergyProfit, a);
            grassMap.put(grass.getPosition(), grass);
            grassOfStep -= 1;
            this.toUpdate.add(grass.getPosition());
        }
        return true;
    }

    @Override
    public ArrayList<Animal> animalsAt(Vector2d position) {
        return animalMap.get(position);
    }

    @Override
    public Grass grassAt(Vector2d position){
        return grassMap.get(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        this.toUpdate.add(oldPosition);
        this.toUpdate.add(newPosition);
        animalMap.get(oldPosition).remove( animal);
        ArrayList<Animal> animals = animalMap.get(newPosition);
        if(animals==null){
            animals = new ArrayList<>();
            animals.add(animal);
            animalMap.put(newPosition, animals);
        }
        else {
            animals.add(animal);
        }
    }


    @Override
    public void removeDeathAnimal(){


        for(Vector2d key : animalMap.keySet()){
            ArrayList<Animal> toRemove = new ArrayList<>();
            ArrayList<Animal> list = animalMap.get(key);
            for(Animal animal : list){
                if (animal.getEnergy()<1){
                    toRemove.add(animal);
                }
            }
            for(Animal animal : toRemove){
                deathAnimals += 1;
                avgLiveLengthAnimals = ((deathAnimals - 1) * avgLiveLengthAnimals + animal.getAge()) / deathAnimals;
                list.remove(animal);
                this.animalsList.remove(animal);
                this.toUpdate.add(animal.getPosition());
            }
        }

        if(animalsList.size() == 5 && isMagical && cntOfMagicalUse < 3){
            this.cntOfMagicalUse += 1;
            System.out.println("MAGIC..............................");
            placeAnimalsMagic(animalsList);
        }
    }

    @Override
    public boolean moveAnimals(){
        if(animalsList.size()<1){
            return false;
        }
        for(Animal a : this.animalsList){
            a.move(this.wallIsFence);
        }
        return true;
    }

    @Override
    public void animalReproduction(ArrayList<IPositionChangeObserver> observers){
        ArrayList<Animal> toReproduce = new ArrayList<>();
        for(Vector2d key : animalMap.keySet()){
            ArrayList<Animal> animalList = animalMap.get(key);
            if(animalList.size()>1){
                int maxEnergyFirst = 0;
                int maxEnergySecond = 0;
                Animal a = null;
                Animal b = null;
                for(Animal animal:animalList){
                    if (animal.getEnergy()>maxEnergyFirst){
                        b = a;
                        a = animal;
                        maxEnergyFirst = animal.getEnergy();
                    }
                    else if(animal.getEnergy()>maxEnergySecond){
                        b = animal;
                        maxEnergySecond = animal.getEnergy();
                    }
                }
                if(a!=null && b!= null && a.canReproduce() && b.canReproduce()){
                    Animal newAnimal = new Animal(a, b, initialParameters);
                    toReproduce.add(newAnimal);
                    a.setChild(newAnimal);
                    b.setChild(newAnimal);
                }

            }
        }
        for (Animal animal : toReproduce){
            placeAnimal(animal, observers);
        }
    }

    @Override
    public void eating(){
        for(Vector2d key : animalMap.keySet()){
            ArrayList<Animal> animalList = animalMap.get(key);
            if(animalList!=null && animalList.size()>0 && grassMap.get(key)!=null){
                ArrayList<Animal> toFeed = new ArrayList<>();
                int maxEnergy = 0;
                for(Animal animal : animalList){
                    maxEnergy = Math.max(maxEnergy, animal.getEnergy());
                }
                for(Animal animal : animalList){
                    if(animal.getEnergy()==maxEnergy){
                        toFeed.add(animal);
                    }
                }
                Grass grass = grassMap.get(key);
                grassMap.remove(grass.getPosition());
                for(Animal animal : toFeed){
                    animal.eatGrass(grass, toFeed.size());
                }

                if(initialParameters.isPositionInJungle(grass.getPosition())){
                    this.grassOfJungle+=1;
                }
                else{
                    this.grassOfStep+=1;
                }

            }
        }}

    @Override
    public void updateAnimalsEnergy(){
        numOfDay +=1;
        for(Animal a : animalsList){
            a.updateAnimalsEnergy();
        }
    }

    @Override
    public String toString(){
        return new MapView(this, initialParameters).toString();
    }

    // It is possible to place an animal if its position fits on the map (or the map has no walls)
    private boolean canPutAnimal(Vector2d position) {
        return (!this.wallIsFence) || (this.wallIsFence && position.x <= this.initialParameters.mapWidth && position.y <= this.initialParameters.mapHeight);
    }

    // It is possible to  place grass if there is no grass or animals in the position
    private boolean canPutGrass(Vector2d position) {
        return grassAt(position) != null || (animalsAt(position) != null && animalsAt(position).size() != 0);
    }

    // Number of fields occupied by animals
    //TODO
    private int getNumberOfOccupiedFields(boolean isJungle){
        int cnt = 0;
        for(int i = 0; i<this.initialParameters.mapHeight; i++){
            for (int j = 0; j<this.initialParameters.mapWidth; j++){
                if((initialParameters.isPositionInJungle(new Vector2d(j,i)) && isJungle ) || !initialParameters.isPositionInJungle(new Vector2d(j,i)) && !isJungle){
                    if(animalsAt(new Vector2d(j,i))!=null && animalsAt(new Vector2d(j,i)).size()>0) cnt += 1;
                }
            }
        }
        return cnt;
    }

    // If isMagic - we can put 5 animals three times if their number on the map is also 5
    private void placeAnimalsMagic(ArrayList<Animal> animals){
        ArrayList<Animal> animalsToAdd = new ArrayList<>();
        for(Animal animal : animals){
            Vector2d position = initialParameters.getRandomPosition();
            while (!canPutAnimal(position)){
                position = initialParameters.getRandomPosition();
            }
            Animal newAnimal = new Animal(animal, animal, initialParameters);
            newAnimal.changePosition(position);
            animalsToAdd.add(newAnimal);
        }
        for(Animal a : animalsToAdd){
            placeAnimal(a, a.observerList);
        }
    }

    @Override
    public HashSet<Vector2d> getToUpdate(){
        return this.toUpdate;
    }

    @Override
    public void clearUpdate() {
        toUpdate.clear();
    }




}
