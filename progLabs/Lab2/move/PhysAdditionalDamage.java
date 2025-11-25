package move;

import ru.ifmo.se.pokemon.*;

public class PhysAdditionalDamage extends PhysicalMove {
    public PhysAdditionalDamage(){
    }
    public PhysAdditionalDamage(Type type, double power, double acc){
        super(type, power, acc);
    }
    @Override
    public double calcBaseDamage(Pokemon att, Pokemon def){
        System.out.println(typeOfPokemon(att));
        System.out.println(typeOfPokemon2(att));
        System.out.println("Сработал бэйз дамаг");
        System.out.println(typeOfPokemon(att).getEffect(def.getTypes())*typeOfPokemon2(att).getEffect(def.getTypes()));
        System.out.println((0.4 * (double)att.getLevel() + (double)2.0F) * this.power / (double)150.0F);
        System.out.println(((0.4 * (double)att.getLevel() + (double)2.0F) * this.power / (double)150.0F)*typeOfPokemon(att).getEffect(def.getTypes())*typeOfPokemon2(att).getEffect(def.getTypes()));
        if (typeOfPokemon(att).getEffect(def.getTypes())*typeOfPokemon2(att).getEffect(def.getTypes())>=1.0d) {
            System.out.println("доп больше или равен 1");
            return ((0.4 * (double) att.getLevel() + (double) 2.0F) * this.power / (double) 150.0F) * typeOfPokemon(att).getEffect(def.getTypes()) * typeOfPokemon2(att).getEffect(def.getTypes());
        }
        else{
            System.out.println("доп меньше 1");
            return (0.4 * (double) att.getLevel() + (double) 2.0F) * this.power / (double) 150.0F;}
    }
    public Type typeOfPokemon(Pokemon p){
        return p.getTypes()[0];

    }
    public Type typeOfPokemon2(Pokemon p){
        return p.getTypes()[1];
    }
}
