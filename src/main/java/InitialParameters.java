
public class InitialParameters {
    public int mapHeight;
    public int mapWidth;
    public double jungleRatio;

    public int grassEnergyProfit;
    public int animalStartEnergy;
    public int dailyEnergyCost;
    public int energyToReproduce;
    public int numberOfSpawningAnimals;
    public Vector2d jungleLowerLeft;
    public Vector2d jungleUpperRight;
    public int field;
    public int jungleField;
    public int stepField;

    public InitialParameters(int mapHeight,
                             int mapWidth,
                             double jungleRatio,
                             int grassEnergyProfit,
                             int animalStartEnergy,
                             int dailyEnergyCost,
                             int numberOfSpawningAnimals){



        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.jungleRatio = jungleRatio;
        this.grassEnergyProfit = grassEnergyProfit;
        this.animalStartEnergy = animalStartEnergy;
        this.dailyEnergyCost = dailyEnergyCost;
        this.numberOfSpawningAnimals = numberOfSpawningAnimals;
        this.energyToReproduce = animalStartEnergy / 5;
        this.field = this.mapHeight * this.mapWidth;
        this.createJungleField();
    }

    public Vector2d getRandomPosition(){
        return new Vector2d((int )(Math.random() * this.mapWidth), (int)(Math.random()*this.mapHeight));
    }
    //add +1
    public Vector2d getRandomJunglePosition(){
        return new Vector2d((int )(Math.random() * (this.jungleUpperRight.x - this.jungleLowerLeft.x + 1)) + this.jungleLowerLeft.x, (int)(Math.random()*(this.jungleUpperRight.y-this.jungleLowerLeft.y + 1) + this.jungleLowerLeft.y));
    }

    public Vector2d getRandomNotJunglePosition(){

        Vector2d a = getRandomPosition();
        Vector2d ja = this.jungleLowerLeft;
        Vector2d jb = this.jungleUpperRight;
        while (isPositionInJungle(a)){
            a = getRandomPosition();
        }
        return a;
    }

    public boolean isPositionInJungle(Vector2d a){
        Vector2d ja = this.jungleLowerLeft;
        Vector2d jb = this.jungleUpperRight;
        return (a.x>=ja.x && a.x <=jb.x && a.y >= ja.y && a.y <= jb.y && jungleField!=0);
    }

    private void createJungleField(){
        double jungleRealField = mapHeight*mapWidth*jungleRatio;
        int jungleWidth = (int) Math.round((Math.sqrt(jungleRatio) * mapWidth));
        int jungleHeight = (int) Math.round(jungleRealField / jungleWidth);

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
        this.stepField = this.field - this.jungleField;
    }

    public String toString(){
        System.out.println("width: "+this.mapWidth+", height: "+this.mapHeight);
        System.out.println("jungle vectors: "+this.jungleLowerLeft +", "+this.jungleUpperRight);
        System.out.println("areas (jungle/step/all): "+this.jungleField + " / " + this.stepField +" /" + this.field);
        return "";
    }
}
