package lab2Main;
import ru.ifmo.se.pokemon.*;
//import move.*;
//import pokemon.*;
public final class TestBattleForMyG {
    public static void main(String[] args){
        Battle b = new Battle();
        Pokemon Swinub = new pokemon.Swinub("Пуфик", 5);
        Pokemon Piloswine = new pokemon.Piloswine("Винисиус", 30);
        Pokemon Chatot = new pokemon.Chatot("Чатот", 13);
        Pokemon Scyther = new pokemon.Scyther("Сайтер",24);
        Pokemon Scizor = new pokemon.Scizor("Эдвард руки-ножницы", 24);
        Pokemon Mamoswine = new pokemon.Mamoswine("Мамонт", 30);
        b.addAlly(Chatot);
        b.addAlly(Scyther);
        b.addAlly(Swinub);
        b.addFoe(Scizor);
        b.addFoe(Mamoswine);
        b.addFoe(Piloswine);
        b.go();

    }
}
