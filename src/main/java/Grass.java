public class Grass {
    int energyProfit;
    private Vector2d position;

    public Grass(int grassEnergy, Vector2d grassPosition){
        this.energyProfit = grassEnergy;
        this.position = grassPosition;
    }

    public int getEnergyProfit(){
        return this.energyProfit;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public String toString(){
        return "*";
    }
}
