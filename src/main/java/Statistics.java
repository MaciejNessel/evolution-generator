import javafx.application.Platform;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private final ArrayList<DayHistory> allDayHistoriess;
    private final Graph graph = new Graph(this);

    public Statistics(){
        this.allDayHistoriess = new ArrayList<>();
    }

    public void addDayHistory(DayHistory dayHistory){
        this.allDayHistoriess.add(dayHistory);
        Platform.runLater(() -> {this.graph.updateChart();});
    }

    public void saveStatisticsToFile() throws IOException {
        // Final data
        int animalsEnd = 0;
        int grassEnd = 0;
        int energyEnd = 0;
        int lifeEnd = 0;
        int childrenEnd = 0;
        int daysEnd = 0;

        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]{"Day", "Number of animals", "Number of grass", "AVG Energy", "AVG life length", "AVG owned children"});

        for(DayHistory dh : allDayHistoriess){
            dataLines.add(dh.getDayHistoryString());
            animalsEnd += dh.getNumOfAllAnimals();
            grassEnd += dh.getNumOfAllGrass();
            energyEnd += dh.getAvgEnergyOfLivingAnimals();
            lifeEnd += dh.getAvgLifeLength();
            childrenEnd += dh.getAvgOwnedChildren();
            daysEnd = dh.getNumOfDay();
        }
        if(daysEnd != 0){
            animalsEnd /= daysEnd;
            grassEnd /= daysEnd;
            energyEnd /= daysEnd;
            lifeEnd /= daysEnd;
            childrenEnd /= daysEnd;
        }


        dataLines.add(new String[]{"End: ", Integer.toString(animalsEnd), Integer.toString(grassEnd), Integer.toString(energyEnd), Integer.toString(lifeEnd), Integer.toString(childrenEnd)});
        File csvOutputFile = new File("StatisticsHistory.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
    public String convertToCSV(String[] data) {
        return String.join(",", data);
    }

    public VBox getGraph(){
        return graph.getGraph();
    };

    public void magic(int number){
        graph.magic(number);
    }

    public DayHistory lastDayHistory(){
        return allDayHistoriess.get(allDayHistoriess.size()-1);
    }
}
