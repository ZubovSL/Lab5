package Gleb.Classes;

public class Coordinates {
    private Double x; //Значение поля должно быть больше -110, Поле не может быть null
    private Double y; //Поле не может быть null
    public Coordinates(double x, double y){
        this.x=x;
        this.y=y;
    }
    @Override
    public String toString(){
        return  "       X: "+x+"\n" +
                "       Y: "+y.toString()+"\n";
    }
}
