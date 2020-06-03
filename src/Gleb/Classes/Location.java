package Gleb.Classes;

public class Location {
    private double x;
    private Integer y; //Поле не может быть null
    private float z;
    private String name; //Поле может быть null

    public Location(double x, Integer y, float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public String toString(){
        return  "       X: "+x+"\n" +
                "       Y: "+y+"\n"+
                "       Z: "+z+"\n"+
                "       Название: "+name+"\n";
    }
}
