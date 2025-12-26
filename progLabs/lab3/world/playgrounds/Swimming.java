package lab3.world.playgrounds;

import java.util.Objects;

public class Swimming extends Playground{
    private double waterTemperature;
    public Swimming(double waterTemperature){
        super(Type.SWIMMING_POOL);
        if (waterTemperature<15 || waterTemperature>=40){throw new IllegalArgumentException("Температура воды должна быть меньше 40 градусов");}
        this.waterTemperature=waterTemperature;
    }
    public double getWaterTemperature() {
        return waterTemperature;
    }

    public boolean isReady(){
        return waterTemperature>=16 && waterTemperature<=40;
    }
    public void decreaseTemperature(double temperature){
        waterTemperature-=temperature;
        if (waterTemperature<0){waterTemperature=0;}
    }
    public void increaseTemperature(double temperature){
        waterTemperature+=temperature;
        if (waterTemperature>100){waterTemperature=99;}
    }
    public String describe(){
        return isReady()? "Плавательный бассейн, температура воды: " + waterTemperature : "Бассейн временно закрыт";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Swimming swimming = (Swimming) obj;
        return waterTemperature==swimming.waterTemperature;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), waterTemperature);
    }
    @Override
    public String toString() {
        return "Плавательный бассейн, температура воды: " + waterTemperature;
    }
}
