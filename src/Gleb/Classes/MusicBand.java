package Gleb.Classes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class MusicBand {
    public static int  ident=0;
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int numberOfParticipants; //Значение поля должно быть больше 0
    private Integer albumsCount; //Поле не может быть null, Значение поля должно быть больше 0
    private java.time.LocalDate establishmentDate; //Поле может быть null
    private MusicGenre genre; //Поле может быть null
    private Person frontMan; //Поле может быть null
    public MusicBand(String name, Coordinates coordinates, int numberOfParticipants, Integer albumsCount,
                     java.time.LocalDate establishmentDate, MusicGenre genre, Person frontMan) {
        this.id=ident+1;
        ident++;
        this.name=name;
        this.coordinates=coordinates;
        creationDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        this.numberOfParticipants=numberOfParticipants;
        this.albumsCount=albumsCount;
        this.establishmentDate=establishmentDate;
        this.genre=genre;
        this.frontMan=frontMan;
    }
    @Override
    public String toString(){
        String answer=
                "ID банды: "+id+"\n" +
                        "   Название банды: "+name+"\n" +
                        "   Адрес: "+coordinates.toString()+"\n" +
                        "   Дата добавления: "+creationDate+"\n" +
                        "   Количество участников: "+numberOfParticipants+"\n" +
                        "   Количество альбомов: "+albumsCount+"\n" +
                        "   Адрес: "+"\n"+coordinates+"\n";
        if(establishmentDate!=null)
            answer=answer+"   Дата основания: "+ establishmentDate.toString()+"\n";
        if(genre!=null)
            answer=answer+"   Жанр: "+genre+"\n";
        if(frontMan!=null)
            answer=answer+"   Солист: "+"\n"+frontMan.toString();
        return  answer;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public void setId(int newId){
        id=newId;
    }
    public int getNumberOfParticipants(){
        return numberOfParticipants;
    }
    public void setCreationTime(ZonedDateTime zdt){
        creationDate=zdt;
    }
}
