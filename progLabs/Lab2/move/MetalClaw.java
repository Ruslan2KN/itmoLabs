package move;
import ru.ifmo.se.pokemon.* ;

public final class MetalClaw extends PhysAdditionalDamage {
    public MetalClaw(){
        super(Type.STEEL, 50.0, 95.0 );

    }
/*    public Type typeOfPokemon(Pokemon p){
        return p.getTypes()[0];

    }
    public Type typeOfPokemon2(Pokemon p){
        return p.getTypes()[1];
    } */
    @Override
    public void applySelfEffects(Pokemon p){
        p.addEffect(new Effect().stat(Stat.ATTACK,1).chance(0.1));
    }

    @Override
    public String describe(){
        return "использует Metal Claw";
    }
/*    @Override
    public double calcTypeEffect(Pokemon var1, Pokemon var2){
        System.out.println( this.type.getEffect(var2.getTypes()));
        System.out.println( "Сработал calcTypeEffect ");
        return this.type.getEffect(var2.getTypes());
    }

@Override
    public double calcBaseDamage(Pokemon att, Pokemon def){
        System.out.println(typeOfPokemon(att));
        System.out.println(typeOfPokemon2(att));
        System.out.println("Сработал бэйз дамаг");
        System.out.println(((0.4 * (double)att.getLevel() + (double)2.0F) * this.power / (double)150.0F)*typeOfPokemon(att).getEffect(def.getTypes())*typeOfPokemon2(att).getEffect(def.getTypes()));
        return ((0.4 * (double)att.getLevel() + (double)2.0F) * this.power / (double)150.0F)*typeOfPokemon(att).getEffect(def.getTypes())*typeOfPokemon2(att).getEffect(def.getTypes());
    } */
}
