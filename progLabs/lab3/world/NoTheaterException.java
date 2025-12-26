package lab3.world;

public class NoTheaterException extends RuntimeException {
    private final int courtyardNumber;
    public NoTheaterException(int courtyardNumber) {
        this.courtyardNumber=courtyardNumber;
    }

    @Override
    public String getMessage() {
        return "В этом дврое № " + courtyardNumber + "нет дома с театром";
    }
}
