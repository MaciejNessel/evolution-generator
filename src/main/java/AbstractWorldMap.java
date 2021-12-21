import java.util.*;

public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{

    // Map config
    public final boolean wallIsFence;
    public final InitialParameters initialParameters;
    private final boolean isMagical;
    private int grassOfStep;
    private int grassOfJungle;
    private int cntOfMagicalUse = 0;
    // Informations about objects on the map
    private  Map<Vector2d, ArrayList<Animal>> animalMap = new TreeMap<>(new Comparator<Vector2d>(){
        @Override
        public int compare(Vector2d pos1, Vector2d pos2){
            if (pos1.x > pos2.x) return 1;
            if (pos1.x < pos2.x) return -1;
            return Integer.compare(pos1.y, pos2.y);
        }
    });
    private  Map<Vector2d, Grass> grassMap = new TreeMap<>(new Comparator<Vector2d>(){
        @Override
        public int compare(Vector2d pos1, Vector2d pos2){
            if (pos1.x > pos2.x) return 1;
            if (pos1.x < pos2.x) return -1;
            return Integer.compare(pos1.y, pos2.y);
        }});
    private ArrayList<Animal> animalsList = new ArrayList<>();

    public AbstractWorldMap(InitialParameters config, boolean wallIsFence, boolean isMagical){
        this.initialParameters = config;
        this.wallIsFence = wallIsFence;
        this.isMagical = isMagical;
        this.grassOfStep = config.stepField;
        this.grassOfJungle = config.jungleField;
    }

    @Override
    public boolean canPutAnimal(Vector2d position) {
        return !wallIsFence || (position.x <= this.initialParameters.mapWidth && position.y <= this.initialParameters.mapHeight);
    }

    @Override
    public boolean canPutGrass(Vector2d position) {
        if(grassAt(position)==null && (animalsAt(position)==null || animalsAt(position).size()==0)){
            return true;
        }
        return false;
    }
    private int getNumberOfAnimals(boolean isJungle){
        int cnt = 0;
        for(int i = 0; i<this.initialParameters.mapHeight; i++){
            for (int j = 0; j<this.initialParameters.mapWidth; j++){
                if((initialParameters.isPositionInJungle(new Vector2d(j,i)) && isJungle ) || !initialParameters.isPositionInJungle(new Vector2d(j,i)) && !isJungle){
                    if(animalsAt(new Vector2d(j,i))!=null && animalsAt(new Vector2d(j,i)).size()>0){
                        cnt += 1;
                    }
                }
            }
        }
        return cnt;
    }
    @Override
    public void placeAnimal(Animal animal, ArrayList<IPositionChangeObserver> observers) {
        Vector2d position = animal.getPosition();
        if(canPutAnimal(position)){
            ArrayList<Animal> animalListByPosition = animalMap.get(position);
            if(animalListByPosition == null) {
                animalListByPosition = new ArrayList<>();
                animalListByPosition.add(animal);
                animalMap.put(position, animalListByPosition);
                animalsList.add(animal);
                for(IPositionChangeObserver observer : observers){
                    animal.addObserver(observer);
                }
            }
            else{
                animalMap.get(position).add(animal);
                animalsList.add(animal);
            }
        }
    }
    private void placeAnimalsMagic(ArrayList<Animal> animals){
        System.out.println(this);
        ArrayList<Animal> animalsToAdd = new ArrayList<>();
        for(Animal animal : animals){
            Vector2d pos = initialParameters.getRandomPosition();
            while (!canPutAnimal(pos)){
                pos = initialParameters.getRandomPosition();
            }
            Animal an = new Animal(animal, animal, initialParameters);
            an.changePosition(pos);
            animalsToAdd.add(an);
        }
        for(Animal a : animalsToAdd){
            placeAnimal(a, a.observerList);
        }
        System.out.println(this);
    }
    @Override
    public boolean placeGrass() {
        // If the whole map is full of animals, the plant has no place to grow
        if(animalMap.size()>=initialParameters.allMapField){
            return true;
        }
        if(this.grassOfJungle - getNumberOfAnimals(true)>0){
            int cnt = 0;
            Vector2d a = initialParameters.getRandomJunglePosition();
            while (!canPutGrass(a)){
                a = initialParameters.getRandomJunglePosition();
                cnt += 1;



            }
            Grass grass = new Grass(initialParameters.grassEnergyProfit, a);
            grassMap.put(grass.getPosition(), grass);
            grassOfJungle -= 1;
        }
        if(this.grassOfStep - getNumberOfAnimals(false)>0){
            Vector2d a = initialParameters.getRandomStepPosition();
            int cnt = 0;
            while (!canPutGrass(a)){
                a = initialParameters.getRandomStepPosition();
                cnt += 1;



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
            animals = new ArrayList<Animal>();
            animals.add(animal);
            animalMap.put(newPosition, animals);
        }
        else {
            animals.add(animal);
        }
    }

    public String toString(){
        System.out.println("LICZBA ZWIERZĄT: " + animalsList.size());
        System.out.println("LICZBA TRAW: " + grassMap.size());

        return new MapView( this, initialParameters).toString();
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


}
