import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class Animal implements IPositionChangeObserver {
    private MapDirection animalDirection;
    private Vector2d animalPosition;
    private Integer energy;
    private final Genotype genotype;
    private final InitialParameters initialParameters;
    public ArrayList<IPositionChangeObserver> observerList = new ArrayList<>();
    private ArrayList<Animal> children = new ArrayList<>();
    private int age = 1;

    public Animal(Animal parentA, Animal parentB, InitialParameters initialParameters){
        this.initialParameters = initialParameters;
        this.animalDirection = MapDirection.numberToDirection((int) (Math.random()*8));
        this.genotype = new Genotype(parentA, parentB);
        if(parentA==null || parentB==null || parentA == parentB){
            this.animalPosition = initialParameters.getRandomPosition();
            this.energy = initialParameters.animalStartEnergy;
        }
        else{
            this.animalPosition = parentA.getPosition();
            this.energy = updateEnergy(parentA, parentB);
        }
    }

    public void changePosition(Vector2d pos){
        this.animalPosition = pos;
    }

    // Animal reproductive management - an animal can reproduce when it has energy >= initial energy of the animals
    public boolean canReproduce(){
        return this.energy>=initialParameters.energyToReproduce;
    }

    private int updateEnergy(Animal parentA, Animal parentB){
        parentA.energy -= parentA.energy/4;
        parentB.energy -= parentB.energy/4;
        return (parentA.energy/4 + parentB.energy/4);
    }

    // Food - gives animals energy
    public void eatGrass(Grass grass){
        this.energy += grass.getEnergyProfit();
    }

    // Getters and setters
    public List<Integer> getGenotype(){
        return this.genotype.getGenotype();
    }
    public Integer getEnergy(){
        return this.energy;
    }
    public MapDirection getDirection(){
        return this.animalDirection;
    }
    public Vector2d getPosition() {
        return new Vector2d(this.animalPosition.x, this.animalPosition.y);
    }


    // (Observer pattern) - informs all observers about the change of the animal's position
    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        if(!observerList.isEmpty())
            for(IPositionChangeObserver observer : observerList)
            {
                observer.positionChanged(oldPosition, newPosition, this);
            }
    }

    public void addObserver(IPositionChangeObserver observer){
        observerList.add(observer);
    }

    // Moving the animal and changing its position depending on the preferences (depending on its genotype)
    public void move(boolean isWall){
        int thisMove = this.genotype.getMoveByGenotype();
        switch (thisMove){
            case 0-> {
                Vector2d newAnimalPosition = this.animalPosition.add(this.animalDirection.toVector());
                if(!isWall || newAnimalPosition.x >= 0 && newAnimalPosition.x < initialParameters.mapWidth && newAnimalPosition.y >= 0 && newAnimalPosition.y < initialParameters.mapHeight){
                    newAnimalPosition.x = (newAnimalPosition.x + initialParameters.mapWidth) % initialParameters.mapWidth;
                    newAnimalPosition.y = (newAnimalPosition.y + initialParameters.mapHeight) % initialParameters.mapHeight;
                    positionChanged(this.animalPosition, newAnimalPosition, this);
                    this.animalPosition = newAnimalPosition;
                }
            }
            case 1-> this.changeDirection(1);
            case 2-> this.changeDirection(2);
            case 3-> this.changeDirection(3);
            case 4-> {
                Vector2d newAnimalPosition = this.animalPosition.add(this.animalDirection.toVector().opposite());
                if(!isWall || newAnimalPosition.x >= 0 && newAnimalPosition.x < initialParameters.mapWidth && newAnimalPosition.y >= 0 && newAnimalPosition.y < initialParameters.mapHeight){
                newAnimalPosition.x = (newAnimalPosition.x + initialParameters.mapWidth) % initialParameters.mapWidth;
                newAnimalPosition.y =( newAnimalPosition.y + initialParameters.mapHeight)% initialParameters.mapHeight;
                positionChanged(this.animalPosition, newAnimalPosition, this);
                this.animalPosition = newAnimalPosition;}
            }
            case 5-> this.changeDirection(5);
            case 6-> this.changeDirection(6);
            case 7-> this.changeDirection(7);
        }
    }

    // Changing the direction of the animal depending on the number of turns ( numOfTurns * 45 degrees) - use in move function
    private void changeDirection(int numberOfRotation){
        for(int i = 0; i<numberOfRotation; i++){
            this.animalDirection = this.animalDirection.oneChangeDirection();
        }
    }
    // Update actual animal's energy and age
    public void updateAnimalsEnergy(){
        this.energy -= initialParameters.dailyEnergyCost;
        this.age += 1;
    }

    public String toString(){
        return animalDirection.toString() + animalPosition.toString();
    }

    public Circle view(int scale){
        Color color;

        if(0.9 * initialParameters.animalStartEnergy <= this.energy && 1.1 * initialParameters.animalStartEnergy > this.energy){
            color = new Color(0.3, 0.2, 0, 1);
        }
        else if(this.energy >= 1.1 * initialParameters.animalStartEnergy){
            color = new Color(0.2, 0.1, 0, 1);
        }
        else if(0.5 * initialParameters.animalStartEnergy <= this.energy && 0.9 * initialParameters.animalStartEnergy > this.energy){
            color = new Color(0.4, 0.2, 0, 1);
        }
        else if(0.2 * initialParameters.animalStartEnergy <= this.energy && 0.5 * initialParameters.animalStartEnergy > this.energy){
            color = new Color(0.6, 0.25, 0, 1);
        }
        else{
            color = new Color(0.8, 0.4, 0, 1);
        }
        return new Circle((double) (scale/2), color);
    }

    public ArrayList<Animal> getChildren(){return this.children;}

    public int getAge(){
        return this.age;
    }

    public void setChild(Animal animal){
        this.children.add(animal);
    }
}
