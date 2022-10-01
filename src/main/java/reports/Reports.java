package reports;

import com.aspose.cells.*;
import objects.DurationForPerson;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Reports {

    public void generateAllUsersChart(TreeMap<Date, List<DurationForPerson>> treeMap, Map<Integer, String> people, int numberOfMovingAverageDays) throws Exception {
        Workbook workbook = new Workbook();
        Worksheet worksheet = workbook.getWorksheets().get(0);

        int positionChange = 2;
        int i = 0;
        for (Map.Entry<Date, List<DurationForPerson>> entry : treeMap.entrySet()) {
            i++;
            float sum = 0;
            for (DurationForPerson dfp : entry.getValue()) {
                worksheet.getCells().get(i, getKeyFromValue(people, dfp.getPerson()) + positionChange).putValue(dfp.getDuration());
                sum += dfp.getDuration();
            }


            //Average
            worksheet.getCells().get(0, people.size() + positionChange + 1).putValue("Średnia");
            worksheet.getCells().get(i, people.size() + positionChange + 1).putValue(sum / entry.getValue().size());


            //Moving average
            if (i > numberOfMovingAverageDays) {
                worksheet.getCells().get(0, people.size() + positionChange + 2).putValue("Średnia krocząca");
                worksheet.getCells().get(i, people.size() + positionChange + 2).putValue(countMovingAverage(worksheet, numberOfMovingAverageDays, i, people.size() + positionChange + 1));
            }

            //Dates
            Style s = new Style();
            s.setNumber(22);
            worksheet.getCells().get(i, 1).setStyle(s);
            worksheet.getCells().get(i, 1).putValue(entry.getKey());

            //No
            worksheet.getCells().get(i, 0).putValue(i - 1);

        }
        //Nicks
        for (Map.Entry<Integer, String> entry : people.entrySet()) {
            worksheet.getCells().get(0, entry.getKey() + positionChange).putValue(entry.getValue());
        }

        workbook.save("Line-Chart.xls", SaveFormat.XLSX);


    }

    public float countMovingAverage(Worksheet worksheet, int numberOfMovingAverageDays, int currentRow, int kolumna) {
        float sum = 0;
        for (int i = currentRow - numberOfMovingAverageDays; i < currentRow; i++)
            sum += worksheet.getCells().get(i, kolumna).getFloatValue();
        return sum / numberOfMovingAverageDays;
    }

    public int getKeyFromValue(Map<Integer, String> map, String key) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }
        return 0;
    }


}