import java.util.*;

public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{

    // Map config
    public final InitialParameters initialParameters;
    public final boolean wallIsFence;
    private final boolean isMagical;
    private int grassOfStep;
    private int grassOfJungle;
    private int cntOfMagicalUse = 0;
    private IPositionChangeObserver app;
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

    public AbstractWorldMap(InitialParameters config, boolean wallIsFence, boolean isMagical, IPositionChangeObserver app){
        this.initialParameters = config;
        this.wallIsFence = wallIsFence;
        this.isMagical = isMagical;
        this.grassOfStep = config.stepField;
        this.grassOfJungle = config.jungleField;
        this.app = app;
    }

    @Override
    public void placeAnimal(Animal animal, ArrayList<IPositionChangeObserver> observers) {
        Vector2d position = animal.getPosition();
        if(canPutAnimal(position)){
            ArrayList<Animal> animals = animalMap.get(position);
            if(animals == null) {
                animals = new ArrayList<>();
                animals.add(animal);
                animalMap.put(position, animals);
                animalsList.add(animal);
                for(IPositionChangeObserver observer : observers){
                    animal.addObserver(observer);
                }
                animal.addObserver(app);
            }
            else{
                animalMap.get(position).add(animal);
                animalsList.add(animal);
            }
        }
    }

    @Override
    public boolean placeGrass() {
        int numOfAnimalsOnStep = getNumberOfOccupiedFields(false);
        int numOfAnimalsOnJungle = getNumberOfOccupiedFields(true);
        // If the whole map is full of animals, the plant has no place to grow
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
            ArrayList<Animal> list = animalMap.get(key);
            list.removeIf(animal -> animal.getEnergy() < 1);
        }
        ArrayList<Animal> toRemove = new ArrayList<>();
        for(Animal animal : animalsList){
            if(animal.getEnergy()<1){
                toRemove.add(animal);
            }
        }
        for(Animal animal : toRemove){
            animalsList.remove(animal);
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
        observers.add(this.app);
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
                toReproduce.add(new Animal(a, b, initialParameters));
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
            for(Animal animal:animalList){
                Grass grass = grassMap.get(animal.getPosition());
                if(grass!=null){
                    grassMap.remove(grass.getPosition());
                    animal.eatGrass(grass);
                    if(initialParameters.isPositionInJungle(grass.getPosition())){
                        this.grassOfJungle+=1;
                    }
                    else{
                        this.grassOfStep+=1;
                    }
                }
            }
        }}

    @Override
    public void newDay(){
        for(Animal a : animalsList){
            a.newDay();
        }
    }

    @Override
    public String toString(){
        System.out.println("LICZBA ZWIERZĄT: " + animalsList.size());
        System.out.println("LICZBA TRAW: " + grassMap.size());
        System.out.println(this.getNumberOfOccupiedFields(false) + ", "+this.getNumberOfOccupiedFields(false));
        return "";
    }

    // It is possible to place an animal if its position fits on the map (or the map has no walls)
    private boolean canPutAnimal(Vector2d position) {
        return !this.wallIsFence || (position.x <= this.initialParameters.mapWidth && position.y <= this.initialParameters.mapHeight);
    }

    // It is possible to  place grass if there is no grass or animals in the position
    private boolean canPutGrass(Vector2d position) {
        return grassAt(position) != null || (animalsAt(position) != null && animalsAt(position).size() != 0);
    }

    // Number of fields occupied by animals
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



}
