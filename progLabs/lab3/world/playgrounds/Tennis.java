package lab3.world.playgrounds;

import java.util.Objects;

public class Tennis extends Playground{
    private int countRacket;
    public Tennis(int countRacket){
        super(Type.TENNIS_COURT);
        if (countRacket<0){throw new IllegalArgumentException("Количество ракеток не должно быть отрицательным");}
        this.countRacket=countRacket;
    }
    public int getCountRacket() {
        return countRacket;
    }
    public boolean isReady(){
        return (countRacket>=2);
    }
    public String describe(){
        return isReady()? "Теннисный корт с "+getCountRacket()+" ракетками": "Теннисный корт временно закрыт";
    }
    public void findRacket(int racket){
        if (racket<=0){throw new IllegalArgumentException("Количество найденых ракеток должно быть больше 0");}
        countRacket+=racket;
        System.out.printf("%d ракеток было найдено, теперь их %d %n",racket,countRacket);
    }
    public void breakRacket(){
        countRacket-=(int)(Math.random()*10);
        if (countRacket<0){countRacket=0;}
        System.out.println("Несколько ракеток сломалось");
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Tennis tennis = (Tennis) obj;
        return countRacket==tennis.countRacket;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countRacket);
    }
    @Override
    public String toString() {
        return "Теннисный корт, количество ракеток: " + countRacket;
    }
}
