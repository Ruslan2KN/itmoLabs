package lab3.characters;

import lab3.world.House;
import lab3.world.playgrounds.Playground;

import java.util.Objects;

public class Dwarf {
    private final String name;
    private double strength;
    private double health;
    private House currentHouse;
    public Dwarf(String name, double strength, double health, House currentHouse){
        if (name== null) {throw new IllegalArgumentException("name не может быть null");}
        if (currentHouse== null) {throw new IllegalArgumentException(" currentHouse не может быть null");}
        if (strength <=0 && health<=0){ throw new IllegalArgumentException("strength и health не может быть меньше 1");}
        this.name=name;
        this.strength=strength;
        this.health=health;
        this.currentHouse=currentHouse;
    }
    private void improveStats(double strengthMore, double healthMore){
        this.health+=healthMore;
        this.strength+=strengthMore;
    }
    public void play(Playground playground){
        if (playground !=null && playground.isReady()){
            System.out.println(this.name+" хайпит на "+ playground.describe());
            switch (playground.getType()) {
                case TENNIS_COURT:
                    double x=(Math.random()+1)*10;
                    double y=(Math.random()+1)*10;
                    improveStats(x, y);
                    break;
                case BASKETBALL_COURT:
                    improveStats(8.9, 6.1);
                    break;
                case VOLLEYBALL_COURT:
                    improveStats(6.0, 4.4);
                    break;
                case SWIMMING_POOL:
                    improveStats(3.0, 6.6);
                    break;
                default:
                    System.out.println("Другой тип площадки.");
                    break;
            }
            System.out.printf("Сила и здоровье %s увеличилась и стала %.1f %.1f соответственно %n",this.name,this.strength,this.health);
        } else { if (playground.describe()==null) {throw new IllegalArgumentException("playground.describe не может быть null");}
            System.out.println(this.name+" не хайпит, так как "+ playground.describe());}
    }
    public void moveToHouse(House newHouse) {
        if (newHouse != null) {
            this.currentHouse = newHouse;
            System.out.println(this.name + " перемещен в дом: " + newHouse);
        } else {
            System.out.println("Дом "+newHouse +" не найден.");
        }
    }

    public String getName() {
        return name;
    }

    public House getCurrentHouse() {
        return currentHouse;
    }

    public double getHealth() {
        return health;
    }

    public double getStrength() {
        return strength;
    }
    @Override
    public boolean equals(Object obj){
        if (this==obj) return true;
        if (obj==null || obj.getClass()!=getClass()) return false;
        Dwarf dwarf= (Dwarf) obj;
        return dwarf.name.equals(name);
    }
    @Override
    public int hashCode(){
        return Objects.hashCode(name);
    }
    @Override
    public String toString(){
        return String.format("Карлик %s имеет %.1f силы и %.1f здоровья, находится в %s доме",getName(),getStrength(),getHealth(),getCurrentHouse());
    }
}
