package Gleb.Classes;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class MBandDeserializer implements JsonDeserializer<MusicBand> {
    @Override
    public MusicBand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    {
        MusicBand.ident++;
        String date = json.toString().substring(json.toString().indexOf("creationDate")-1,json.toString().indexOf("numberOfParticipants")-2);
        String jsonWithoutDate = json.toString().substring(0,json.toString().indexOf("creationDate")-2)+
                json.toString().substring(json.toString().indexOf("numberOfParticipants")-2,json.toString().length());
        Gson gson = new GsonBuilder().create();

        int year = Integer.parseInt(date.substring(date.indexOf("year")+6,date.indexOf("month")-2));
        int month = Integer.parseInt(date.substring(date.indexOf("month")+7,date.indexOf("day")-2));
        int day = Integer.parseInt(date.substring(date.indexOf("day")+5,date.indexOf("time")-3));
        int hour = Integer.parseInt(date.substring(date.indexOf("hour")+6,date.indexOf("minute")-2));
        int minute = Integer.parseInt(date.substring(date.indexOf("minute")+8,date.indexOf("second")-2));
        int second = Integer.parseInt(date.substring(date.indexOf("second")+8,date.indexOf("nano")-2));
        int nano = Integer.parseInt(date.substring(date.indexOf("nano")+6,date.indexOf("offset")-4));

        String zone = date.substring(date.indexOf("zone")+13, date.length()-3);

        LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second, nano);
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.of(zone));

        MusicBand mb = gson.fromJson(jsonWithoutDate,MusicBand.class);
        mb.setCreationTime(zdt);
        return mb;
    }
}