import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Graph {

    private final Statistics statistics;
    private final LineChart<Number,Number> graph = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());

    private final XYChart.Series seriesAnimals = new XYChart.Series();
    private final XYChart.Series seriesGrass = new XYChart.Series();
    private final XYChart.Series seriesAVGEnergy = new XYChart.Series();
    private final XYChart.Series seriesAVGLife = new XYChart.Series();
    private final XYChart.Series seriesAVGChild = new XYChart.Series();
    private final VBox graphBox = new VBox();
    private final Label magicalUse = new Label();
    private final Label mostPopularGenotype = new Label();

    public Graph(Statistics stats){
        this.statistics = stats;
        createGraph();
        magicalUse.setFont(Font.font(18));
        magicalUse.setTextFill(Color.RED);
        magicalUse.setOnMouseClicked(e -> {
            magicalUse.setText("");
        });
    }

    private void createGraph(){
        graph.setTitle("Statistics");

        seriesAnimals.setName("Number of animals");
        seriesGrass.setName("Number of grass");
        seriesAVGChild.setName("AVG children");
        seriesAVGLife.setName("AVG life length");
        seriesAVGEnergy.setName("AVG energy");

        graph.getData().addAll(seriesAnimals,seriesGrass, seriesAVGLife, seriesAVGChild, seriesAVGEnergy);
        graph.setCreateSymbols(false);
        graph.setAnimated(false);
        graphBox.getChildren().addAll(graph, magicalUse, mostPopularGenotype);
    }

    public VBox getGraph(){
        return graphBox;
    }

    // Adds new points to the graph based on saved statistics from the simulation
    public void updateChart(){
        Integer numOfDay = statistics.lastDayHistory().getNumOfDay();
        seriesAnimals.getData().add(new XYChart.Data(numOfDay, statistics.lastDayHistory().getNumOfAllAnimals()));
        seriesGrass.getData().add(new XYChart.Data(numOfDay, statistics.lastDayHistory().getNumOfAllGrass()));
        seriesAVGLife.getData().add(new XYChart.Data(numOfDay, statistics.lastDayHistory().getAvgLifeLength()));
        seriesAVGChild.getData().add(new XYChart.Data(numOfDay, statistics.lastDayHistory().getAvgOwnedChildren()));
        seriesAVGEnergy.getData().add(new XYChart.Data(numOfDay, statistics.lastDayHistory().getAvgEnergyOfLivingAnimals()));
        mostPopularGenotype.setText("Actual dominant genotype: "+statistics.lastDayHistory().getMostPopularGenotype().toString());
    }

    // During magic mode it shows information in the interface about the next use
    public void magic(int number){
        Platform.runLater(()->magicalUse.setText("MAGIC NUMBER:"+number));
    }

}
