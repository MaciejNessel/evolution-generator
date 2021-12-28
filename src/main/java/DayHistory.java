import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DayHistory {
    private final Integer numOfAllAnimals;
    private final Integer numOfAllGrass;
    private final Integer numOfDay;
    private final Integer avgEnergyOfLivingAnimals;
    private final Integer avgLifeLength;
    private final Integer avgOwnedChildren;
    private ArrayList<Integer> mostPopularGenotype = null;

    // Contains all statistics listed in the specification for the day (epoch)
    DayHistory(int numOfDay, int numOfAllAnimals, int numOfAllGrass, Integer avgEnergyOfLivingAnimals,
               Integer avgLifeLength, Integer avgOwnedChildren, HashMap<ArrayList<Integer>, Integer>  genotypes){
        this.numOfAllAnimals = numOfAllAnimals;
        this.numOfAllGrass = numOfAllGrass;
        this.avgEnergyOfLivingAnimals = avgEnergyOfLivingAnimals;
        this.avgLifeLength = avgLifeLength;
        this.avgOwnedChildren = avgOwnedChildren;
        this.numOfDay = numOfDay;

        Integer maxValues=(Collections.max(genotypes.values()));
        for (Map.Entry<ArrayList<Integer>, Integer> entry : genotypes.entrySet()) {
            if (entry.getValue() == maxValues) {
                mostPopularGenotype = entry.getKey();
            }
        }

    }

    public String[] getDayHistoryString(){
        return new String[]{numOfDay.toString(), numOfAllAnimals.toString(), numOfAllGrass.toString(), avgEnergyOfLivingAnimals.toString(), avgLifeLength.toString(), avgOwnedChildren.toString()};
    }

    public Integer getAvgOwnedChildren() {
        return avgOwnedChildren;
    }

    public Integer getAvgEnergyOfLivingAnimals() {
        return avgEnergyOfLivingAnimals;
    }

    public Integer getAvgLifeLength() {
        return avgLifeLength;
    }

    public Integer getNumOfAllAnimals() {
        return numOfAllAnimals;
    }

    public Integer getNumOfAllGrass() {
        return numOfAllGrass;
    }

    public Integer getNumOfDay() {
        return numOfDay;
    }

    public ArrayList<Integer> getMostPopularGenotype(){
        return this.mostPopularGenotype;
    }

}
