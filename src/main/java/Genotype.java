import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genotype {
    private final List<Integer> genotype;

    public Genotype(Animal parentA, Animal parentB){
        if(parentA==null || parentB==null) {
            this.genotype = createRandomGenotype();
        }
        else{
            this.genotype = createGenotype(parentA, parentB);
        }
    }

    //Returns array with this genotype
    public List<Integer> getGenotype(){
        return this.genotype;
    }

    // Returns the most likely move (0-7) depending on this genotype
    public int getMoveByGenotype(){
        int newRandomNumber = (int) (Math.random()*32);
        return this.genotype.get(newRandomNumber);
    }

    // Creates a genotype based on the parents' genotypes depending on their energy
    private List<Integer> createGenotype(Animal a, Animal b){
        int firstRangeEnergy = (a.getEnergy() * 32 / (a.getEnergy()+ b.getEnergy()));
        int secondRangeEnergy = 32 - firstRangeEnergy;
        List<Integer> newGenotype = new ArrayList<>();
        newGenotype.addAll(a.getGenotype().subList(0, firstRangeEnergy));
        newGenotype.addAll( b.getGenotype().subList(32-secondRangeEnergy, 32) );
        Collections.sort(newGenotype);
        return newGenotype;
    }

    // Creates a random genotype consisting of a 32-element array with numbers (0-7)
    private List<Integer> createRandomGenotype(){
        List<Integer> newGenotype = new ArrayList<>();
        for(int i = 0; i<32; i++){
            int newRandomNumber = (int) (Math.random()*8);
            newGenotype.add(newRandomNumber);
        }
        Collections.sort(newGenotype);
        return newGenotype;
    }
}
