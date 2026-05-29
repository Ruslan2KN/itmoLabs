package commands;

import network.Request;
import network.Response;

/**
 * Служебная команда для первичной проверки логина и пароля при подключении.
 */
public class Login extends Command {

    public Login() {
        super(Commands.login, "внутренняя команда для авторизации");
    }

    @Override
    public Response execute(Request request) {
        return new Response("Авторизация прошла успешно", true, null);
    }
}