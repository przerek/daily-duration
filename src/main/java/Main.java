import lombok.SneakyThrows;
import objects.DailyLog;
import objects.DurationForPerson;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import reports.Reports;
import services.DailyService;

import static services.DailyService.*;


public class Main {

    @SneakyThrows
    public static void main(String args[]){

        final int NUMBER_OF_MOVING_AVERAGE_DAYS = 5;

        DailyService dailyService = new DailyService();


        File in = new File("C:\\Users\\user\\Desktop\\file.html");

        Document doc = Jsoup.parse(in);
        doc.firstChild();

        List<DailyLog> dailyLogs = new ArrayList<>();

        Elements elements = doc.select("div.actionContainer");
        for (Element el : elements) {
            dailyLogs.add(new DailyLog(el.select("div.action-details").get(0).child(0).attr("rel"),
                    string2Data(el.select("div.action-details").get(0).child(1).child(0).html()),
                    string2Duration(el.select("div.action-body").get(0).child(0).select("dd").get(0).select("dd").html())));
        }


        TreeMap<Date, List<DurationForPerson>> logsPerDate = new TreeMap<>(dailyService.getLogsPerDate(dailyLogs));

        Reports reports = new Reports();
        reports.generateAllUsersChart(logsPerDate, getPeopleNames(dailyLogs),NUMBER_OF_MOVING_AVERAGE_DAYS);


    }

}