package lab5.src.main;

import lab5.src.commands.*;

/**
 * Главный класс приложения.
 * Содержит статическую точку входа (метод main), с которой начинается выполнение программы.
 */
public class Main {

    /**
     * Точка входа в программу.
     * Создает экземпляр приложения {@link App} и запускает его основной цикл.
     *
     * @param args аргументы командной строки (не используются в данной реализации)
     */
    public static void main(String[] args){
        App app = new App();
        app.start();
    }
}
