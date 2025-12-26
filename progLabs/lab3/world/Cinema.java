package lab3.world;

import java.util.Objects;

public class Cinema implements Describable{
    private final String name;
    private final int seats;
    public Cinema(String name, int seats){
        if (seats<= 0){throw new IllegalArgumentException("chairsCount не может быть меньше 1");}
        if (name==null){throw new IllegalArgumentException("name не может быть null");}
        this.name=name;
        this.seats=seats;
    }

    public String getName() {
        return name;
    }
    public int getSeats(){ return seats;}

    @Override
    public String describe() {
        return "Кинотеатр "+getName()+" имеет "+getSeats()+" мест и сейчас там проходит показ фильма 'Остров проклятых'";
    }
    @Override
    public boolean equals(Object obj){
        if (this==obj)return true;
        if (obj==null || obj.getClass()!=getClass()) return false;
        Cinema cinema= (Cinema) obj;
        return cinema.name.equals(name);
    }
    @Override
    public int hashCode(){
        return Objects.hash(name);
    }
    @Override
    public String toString(){
        return "Кинотеатр "+getName()+", со "+ getSeats() + " посадочными местами";
    }
}
