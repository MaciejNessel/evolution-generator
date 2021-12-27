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
    private final LineChart<Number,Number> firstGraph = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());

    private final XYChart.Series seriesAnimals = new XYChart.Series();
    private final XYChart.Series seriesGrass = new XYChart.Series();
    private final XYChart.Series seriesAVGEnergy = new XYChart.Series();
    private final XYChart.Series seriesAVGLife = new XYChart.Series();
    private final XYChart.Series seriesAVGChild = new XYChart.Series();
    private final VBox graphBox = new VBox();
    private Label magicalUse = new Label();

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
        firstGraph.setTitle("Number of grasses and animals");

        seriesAnimals.setName("Number of animals");
        seriesGrass.setName("Number of grass");
        seriesAVGChild.setName("AVG children");
        seriesAVGLife.setName("AVG life length");
        seriesAVGEnergy.setName("AVG energy");

        firstGraph.getData().add(seriesAnimals);
        firstGraph.getData().add(seriesGrass);
        firstGraph.getData().add(seriesAVGLife);
        firstGraph.getData().add(seriesAVGChild);
        firstGraph.getData().add(seriesAVGEnergy);
        firstGraph.setCreateSymbols(false);

        graphBox.getChildren().addAll(firstGraph, magicalUse);

    }

    public VBox getGraph(){
        return graphBox;
    }

    public void updateGraph(){
        seriesAnimals.getData().add(new XYChart.Data(statistics.stats.get(statistics.stats.size()-1).getNumOfDay(), statistics.stats.get(statistics.stats.size()-1).getNumOfAllAnimals()));
        seriesGrass.getData().add(new XYChart.Data(statistics.stats.get(statistics.stats.size()-1).getNumOfDay(), statistics.stats.get(statistics.stats.size()-1).getNumOfAllGrass()));
        seriesAVGLife.getData().add(new XYChart.Data(statistics.stats.get(statistics.stats.size()-1).getNumOfDay(), statistics.stats.get(statistics.stats.size()-1).getAvgLifeLength()));
        seriesAVGChild.getData().add(new XYChart.Data(statistics.stats.get(statistics.stats.size()-1).getNumOfDay(), statistics.stats.get(statistics.stats.size()-1).getAvgOwnedChildren()));
        seriesAVGEnergy.getData().add(new XYChart.Data(statistics.stats.get(statistics.stats.size()-1).getNumOfDay(), statistics.stats.get(statistics.stats.size()-1).getAvgEnergyOfLivingAnimals()));
    }

    public void magic(int number){
        Platform.runLater(()->magicalUse.setText("MAGIC NUMBER:"+number));
    }

}
