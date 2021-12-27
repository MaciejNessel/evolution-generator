public class DayHistory {
    private Integer numOfAllAnimals;
    private Integer numOfAllGrass;
    private Integer numOfDay;
    private Integer avgEnergyOfLivingAnimals;
    private Integer avgLifeLength;
    private Integer avgOwnedChildren;

    DayHistory(int numOfDay, int numOfAllAnimals, int numOfAllGrass, Integer avgEnergyOfLivingAnimals,
               Integer avgLifeLength, Integer avgOwnedChildren){
        this.numOfAllAnimals = numOfAllAnimals;
        this.numOfAllGrass = numOfAllGrass;
        this.avgEnergyOfLivingAnimals = avgEnergyOfLivingAnimals;
        this.avgLifeLength = avgLifeLength;
        this.avgOwnedChildren = avgOwnedChildren;
        this.numOfDay = numOfDay;

    }

    public int[] getDayHistory(){
        return new int[]{numOfDay, numOfAllAnimals, numOfAllGrass, avgEnergyOfLivingAnimals, avgLifeLength, avgOwnedChildren};
    }

    public String []  getDayHistoryString(){
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

}
