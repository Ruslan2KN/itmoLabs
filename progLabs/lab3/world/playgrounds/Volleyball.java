package lab3.world.playgrounds;

import java.util.Objects;

public class Volleyball extends Playground{
    private boolean volleyballNet;
    public Volleyball(boolean volleyballNet){
        super(Type.VOLLEYBALL_COURT);
        this.volleyballNet=volleyballNet;
    }
    public String describe(){
        return isReady()? "Волебольная площадка с сеткой": "Волейбольная площадка временно закрыта";
    }
    public boolean isReady(){
        return volleyballNet;
    }
    public void findVollevballNet(){
        if (!volleyballNet){
        volleyballNet=true;
        System.out.println("Волебольная сетка найдена, теперь ее можно устанавливать");}
        else {
            System.out.println("Сетка уже установлена");
        }
    }
    public void breakVolleyballNet(){
        if (volleyballNet){
        volleyballNet=false;
        System.out.println("Волейбольная сетка порвалась, нужно найти новую");}
        else {
            System.out.println("Сетка уже порвана");
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Volleyball volleball = (Volleyball) obj;
        return volleyballNet==volleball.volleyballNet;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), volleyballNet);
    }
    @Override
    public String toString() {
        return "Волейбольная площадка " + (volleyballNet? "с сеткой":"без сетки") ;
    }
}
