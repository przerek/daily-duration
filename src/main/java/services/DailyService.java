package services;

import lombok.SneakyThrows;
import objects.DailyLog;
import objects.DurationForPerson;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyService {

    @SneakyThrows
    public static Date string2Data(String inputDate) {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return date;
    }

    public static int string2Duration(String inputDuration) {

        int minutes = 0;

        StringBuilder sb = new StringBuilder(inputDuration);

        Pattern minutesPattern = Pattern.compile("([\\d|\\.]*m)");
        Pattern hoursPattern = Pattern.compile("([\\d|\\.]*h)");
        Matcher minutesMatcher = minutesPattern.matcher(inputDuration);
        Matcher hoursMatcher = hoursPattern.matcher(inputDuration);

        if (minutesMatcher.find())
            minutes = Integer.parseInt(removeTimeUnit(minutesMatcher.group(1)));

        if (hoursMatcher.find())
            minutes = minutes + Math.round(Float.parseFloat(removeTimeUnit(hoursMatcher.group(1))) * 60);

        return minutes;
    }

    public Map<Date, List<DurationForPerson>> getLogsPerDate(List<DailyLog> dailyForPerson) {

        Map<Date, List<DurationForPerson>> resultMap = new HashMap<>();

        for (DailyLog dfp : dailyForPerson) {
            resultMap.put(dfp.getDate(), updateLogsForDate(resultMap.get(dfp.getDate()), new DurationForPerson(dfp.getPerson(), dfp.getDuration())));
        }

        return resultMap;
    }

    public List<DurationForPerson> updateLogsForDate(List<DurationForPerson> list, DurationForPerson dfp) {
        if (list == null)
            list = new ArrayList();

        list.add(dfp);
        return list;

    }

    public static String removeTimeUnit(String input) {
        StringBuilder sb = new StringBuilder(input);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static Map<Integer,String>  getPeopleNames(List<DailyLog> dailyForPeople ){
        Map<Integer,String> peopleNames = new HashMap<>() ;

        for(DailyLog dfp : dailyForPeople){
            if(!peopleNames.containsValue(dfp.getPerson())){
                peopleNames.put(peopleNames.size(), dfp.getPerson());
            }
        }
        return peopleNames;
    }
}
