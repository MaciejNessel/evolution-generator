import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {

    ArrayList<DayHistory> stats;

    public Statistics(){
        this.stats = new ArrayList<>();
    }

    public void addDayHistory(DayHistory dayHistory){
        this.stats.add(dayHistory);
    }

    public ArrayList<DayHistory> getStats(){
        return this.stats;
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

        for(DayHistory dh : stats){
            int[] actual = dh.getDayHistory();
            dataLines.add(dh.getDayHistoryString());
            animalsEnd += actual[1];
            grassEnd += actual[2];
            energyEnd += actual[3];
            lifeEnd += actual[4];
            childrenEnd += actual[5];
            daysEnd = actual[0];
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
        return Stream.of(data).collect(Collectors.joining(","));
    }
}
