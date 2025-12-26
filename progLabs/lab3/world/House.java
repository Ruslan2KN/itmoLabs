package lab3.world;

import java.util.Objects;

public class House {
    private final HouseData houseData;
    private final Theater theater;
    private final Cinema cinema;
    public House( String address, int houseNumber, Theater theater){
        if (theater == null) {
            throw new IllegalArgumentException("театр не может быть null");
        }
        checkHouseData(address, houseNumber);
        this.houseData = new HouseData(address,houseNumber);
        this.theater = theater;
        this.cinema = null;
    }
    public House( String address, int houseNumber, Cinema cinema){
        if (cinema == null) {
            throw new IllegalArgumentException("cinema не может быть null");
        }
        checkHouseData(address, houseNumber);
        this.houseData = new HouseData(address,houseNumber);
        this.theater = null;
        this.cinema = cinema;
    }
    private void checkHouseData(String address, int houseNumber) {
        if (address == null) {
            throw new IllegalArgumentException("address не может быть null");
        }
        if (houseNumber <= 0) {
            throw new IllegalArgumentException("houseNumber не может быть меньше 1");
        }
    }
    public boolean hasTheater() {
        return theater != null;
    }

    public boolean hasCinema() {
        return cinema != null;
    }

    public Theater getTheater() {
        return theater;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public HouseData getHouseData() {
        return houseData;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj==null || obj.getClass()!=getClass()) return false;
        House house= (House) obj;
        return house.houseData.equals(houseData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseData);
    }

    @Override
    public String toString() {
        if (hasTheater()){ return String.format("Адрес: %s, номер дома: %d , театр: %s", getHouseData().address(),getHouseData().houseNumber(),getTheater());}
        else {return String.format("Адрес: %s, номер дома: %d , кинотеатр: %s", getHouseData().address(),getHouseData().houseNumber(),getCinema()); }
    }
}
