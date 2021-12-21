

public class World {
    public static void main(String[] args) {

        InitialParameters parameters = new InitialParameters();

        IWorldMap map = new AbstractWorldMap(parameters, false, false);
        IEngine engine = new SimulationEngine(parameters, map);
        System.out.println(map);
        System.out.println("");
        for (int i = 0; i<10000;i++){
            engine.run();
        }
        System.out.println("");
        System.out.println(map);
        System.out.println();

        System.out.println(parameters);



    }
}
