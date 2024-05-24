import app.App;
import app.AppClient;
import app.AppServer;

public class Main {
    public static void main(String[] args) {
        switch (System.getenv("MODE")) {
            case ("standalone"): {
                App.run();
                break;
            }
            case ("server"): {
                AppServer.run();
                break;
            }
            case ("client"): {
                AppClient.run();
                break;
            }
        }
    }
}

/*
Доработать программу из лабораторной работы №6 следующим образом:

/////////////////Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.
/////////////////Для генерации поля id использовать средства базы данных (sequence).
/////////////////Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
/////////////////Все команды получения данных должны работать с коллекцией в памяти, а не в БД
/////////////////Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.
/////////////////Пароли при хранении хэшировать алгоритмом SHA-512
/////////////////Запретить выполнение команд не авторизованным пользователям.
/////////////////При хранении объектов сохранять информацию о пользователе, который создал этот объект.
/////////////////Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие им.
/////////////////Для идентификации пользователя отправлять логин и пароль с каждым запросом.
/////////////////Необходимо реализовать многопоточную обработку запросов.

/////////////////Для многопоточного чтения запросов использовать Fixed thread pool
/////////////////Для многопотчной обработки полученного запроса использовать Fixed thread pool
/////////////////Для многопоточной отправки ответа использовать ForkJoinPool - разделяем на отправки каждого пакета
/////////////////Для синхронизации доступа к коллекции использовать потокобезопасные аналоги коллекции из java.util.concurrent

 */