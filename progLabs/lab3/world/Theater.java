package lab3.world;

import java.util.Objects;

public class Theater implements Describable{
    private final String name;
    private final int chairsCount;
    private ViewMode viewMode;
    public Theater(String name, int chairsCount){
        if (chairsCount <= 0) {
            throw new IllegalArgumentException("chairsCount не может быть меньше 1");
        }
        if (name == null) {
            throw new IllegalArgumentException("name не может быть null");
        }
        this.name = name;
        this.chairsCount = chairsCount;
        this.viewMode = (Math.random()*10>4.3f ? ViewMode.OUTDOOR: ViewMode.INDOOR);
    }

    public String getName() {
        return name;
    }
    public int getChairsCount(){ return chairsCount;}

    public ViewMode getViewMode() {
        return viewMode;
    }

    public void switchViewMode(){
        transferChairs();
        this.viewMode=(this.viewMode == ViewMode.INDOOR) ? ViewMode.OUTDOOR : ViewMode.INDOOR;

    }
    private void transferChairs(){
        System.out.print("Коротышки переносят стулья");
        if (getViewMode()==ViewMode.INDOOR){
            System.out.println("из зрительного зала на улицу");
        }
        else {
            System.out.println(" с улицы в зрительный зал");
        }
        System.out.println("Коротошыки успешно переместили "+getChairsCount()+" стульев");
    }
    @Override
    public String describe() {
        return String.format("Театр %s %s спектакль, количество стульев для гостей %d штук",getName(), (getViewMode()==ViewMode.INDOOR ? "показывает в зале": "показывает на улице"), getChairsCount());
    }
    @Override
    public boolean equals(Object obj){
        if (this==obj) return true;
        if (obj==null || obj.getClass()!=getClass()) return false;
        Theater theater= (Theater) obj;
        return theater.name.equals(name);
    }
    @Override
    public int hashCode(){
        return Objects.hash(name);
    }
    @Override
    public String toString(){
        return "Театр "+getName()+", со "+ getChairsCount() + " посадочными местами и со сценой " + (getViewMode()==ViewMode.INDOOR? "внутри зала":" на улице");
    }
}
