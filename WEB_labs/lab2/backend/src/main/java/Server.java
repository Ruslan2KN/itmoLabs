import com.fastcgi.FCGIInterface;
import model.PointRequest;
import model.PointResult;
import model.ServerResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args){
        FCGIInterface fcgi = new FCGIInterface();
        JsonService jsonService = new JsonService();

        // Цикл ожидания запроса
        while (fcgi.FCGIaccept() >= 0){
            try {
                String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
                if (!"POST".equalsIgnoreCase(method)){
                    sendRawResponse("405 Method Not Allowed", "text/plain", "Разрешены только Post запросы");
                    continue;
                }

                String contentLengthStr = FCGIInterface.request.params.getProperty("CONTENT_LENGTH");
                int contentLength = 0;
                if (contentLengthStr != null && !contentLengthStr.isEmpty()){
                    contentLength= Integer.parseInt(contentLengthStr);
                }

                //byte[] buffer = System.in.readNBytes(contentLength);
                byte[] buffer = new byte[contentLength];
                int bytesRead = 0;
                while (bytesRead < contentLength){
                    int read =System.in.read(buffer, bytesRead, contentLength - bytesRead);
                    if (read == -1) break;
                    bytesRead += read;
                }

                String requestBody = new String(buffer, StandardCharsets.UTF_8);

                // Парс json
                PointRequest pointRequest;
                try {
                    pointRequest = jsonService.pointRequest(requestBody);
                } catch (Exception e){
                    sendJsonResponse(jsonService, ServerResponse.error("Неверный формат JSON: "+ e.getMessage()));
                    continue;
                }

                String validationError = Validator.validate(pointRequest);
                if (validationError != null){
                    sendJsonResponse(jsonService, ServerResponse.error(validationError));
                    continue;
                }

                List<PointResult> results = new ArrayList<>();
                String currentTime = LocalDateTime.now().format(TIME_FORMATTER);

                for (Double x : pointRequest.getX()){
                    for (Double r : pointRequest.getR()){
                        long startTime = System.nanoTime();
                        boolean isHit = AreaChecker.checkHit(x, pointRequest.getY(), r);
                        long executionTimeNs= System.nanoTime() - startTime;

                        results.add(new PointResult(x, pointRequest.getY(), r, isHit, currentTime, executionTimeNs));
                    }
                }

                sendJsonResponse(jsonService, ServerResponse.success(results));

            } catch (Exception e){
                sendJsonResponse(jsonService, ServerResponse.error("Внутренняя ошибка сервера: " + e.getMessage()));
            }
        }
    }

    private static void sendJsonResponse(JsonService jsonService, ServerResponse response){
        String json = jsonService.toJson(response);
        sendRawResponse("200 OK", "application/json; charset=utf-8", json);
    }

    private static void sendRawResponse(String status, String contentType, String body){
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        String httpResponse = "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + bodyBytes.length + "\r\n" +
                "\r\n" +
                body;
        System.out.print(httpResponse);
        System.out.flush();
    }

}
