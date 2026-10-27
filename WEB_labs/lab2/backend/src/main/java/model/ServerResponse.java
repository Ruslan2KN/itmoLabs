package model;

import java.util.List;

public record ServerResponse(String status, String message, List<PointResult> results) {

    public static ServerResponse success(List<PointResult> results) {
        return new ServerResponse("success", null, results);
    }

    public static ServerResponse error(String message) {
        return new ServerResponse("error", message, null);
    }
}
