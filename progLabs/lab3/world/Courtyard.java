package lab3.world;

import lab3.characters.Dwarf;
import lab3.world.playgrounds.Playground;
import lab3.world.playgrounds.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Courtyard {
    private final int number;
    private final List<Playground> playgrounds;
    private final List<House> houses;
    public Courtyard(int number){
        this.number=number;
        this.playgrounds=new ArrayList<>();
        this.houses=new ArrayList<>();
    }
    public void addPlayground(Playground playground){
        if (playground== null){ throw new IllegalArgumentException("playground не может быть равна null");}
        if (playgrounds.contains(playground)){ throw new IllegalArgumentException("этот playground уже был добавлен, в этот двор");}
        playgrounds.add(playground);
    }
    public List<Playground> getPlaygrounds(){ return Collections.unmodifiableList(playgrounds);}
    public boolean hasPlayground(Type type){
        if (type==null) return false;
        return playgrounds.stream().anyMatch(playground -> playground.getType()==type);
    }
    public void addHouse(House house){
        if (house== null){throw new IllegalArgumentException("house не может быть равен null");}
        if (houses.contains(house)){throw new IllegalArgumentException("этот house уже есть во дворе");}
        houses.add(house);
    }
    public List<House> getHouses(){
        return Collections.unmodifiableList(houses);
    }
    private House findHouseWithTheater() throws NoTheaterException{
        return houses.stream().filter(House::hasTheater).findFirst().orElseThrow(()-> new NoTheaterException(this.number));
    }
    public void moveDwarfToNewHouse(Dwarf... dwarfs){
        House houseWithTheater;
        try {
            houseWithTheater = findHouseWithTheater();
        } catch (NoTheaterException e) {
            System.out.println("В " + getNumber() + " дворе нет дома с театром");
            return;
        }
        for (Dwarf dwarf: dwarfs) {
            if (dwarf.getCurrentHouse() != null && dwarf.getCurrentHouse().hasTheater()) {
                System.out.println(dwarf.getName() + " уже находится в доме с театром");
            } else {
                System.out.println(dwarf.getName() +" переходит в дом с театрам, для отдыха ");dwarf.moveToHouse(houseWithTheater);
            }
        }
    }
    public int getNumber() {
        return number;
    }
    @Override
    public boolean equals(Object obj){
        if (this==obj)return true;
        if (obj==null || obj.getClass()!=getClass()) return false;
        Courtyard courtyard= (Courtyard) obj;
        return getNumber()==courtyard.number;
    }
    @Override
    public int hashCode(){
        return Objects.hash(number);
    }
    @Override
    public String toString(){
        return "Двор номер "+getNumber()+", содержит дома  "+ getHouses() + " и площадки "+getPlaygrounds();
    }
}
