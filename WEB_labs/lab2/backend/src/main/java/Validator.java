import model.PointRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Validator {
    private static final Set<Double> ALLOWED_X = new HashSet<>(
            Arrays.asList(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0)
    );

    private static final Set<Double> ALLOWED_R = new HashSet<>(
            Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0)
    );

    public  static String validate(PointRequest request){
        if (request==null){
            return "Пустое тело запроса";
        }
        // Проверка X
        if (request.getX() == null || request.getX().isEmpty()){
            return "Не выбрано ни одно значение X";
        }
        for (Double x: request.getX()){
            if (x ==null || !ALLOWED_X.contains(x)){
                return "Недопустимное значение X: " + x;
            }
        }

        // Проверка Y
        if (request.getY() == null){
            return "Значение Y не передано";
        }

        if (request.getY() <= -3.0 || request.getY() >= 5.0){
            return "Значение Y должно быть в диапозоне от -3 до 5";
        }

        // Проверка R
        if (request.getR() == null || request.getR().isEmpty()){
            return "Не выбрано ни одно значение R";
        }
        for (Double r: request.getR()){
            if (r ==null || !ALLOWED_R.contains(r)){
                return "Недопустимное значение R: " + r;
            }
        }

        return null;
    }
}
