package Gleb;

import Gleb.Classes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Programm {
    private LinkedHashSet<MusicBand> bandsList = new LinkedHashSet<MusicBand>();
    private Date initializeDate;
    private String filename="bands.json";
    private Scanner scriptScanner = new Scanner("");
    public void Programm() {
    }

    public void start()  {
        Scanner scanner = new Scanner(System.in);
        File file = new File(filename);
        if(file.exists()){
            try {
                Scanner scannerGson = new Scanner(file);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.registerTypeAdapter(MusicBand.class, new MBandDeserializer()).create();
                String script;
                while (scannerGson.hasNext()){
                    script=scannerGson.nextLine();
                    bandsList.add(gson.fromJson(script, MusicBand.class));
                }
                if (bandsList==null){
                    System.out.println("Список банд пуст! Можете создать новые...");
                    bandsList = new LinkedHashSet<MusicBand>();
                }
                else {
                    System.out.println("Список банд загружен.");
                    for(MusicBand mb: bandsList){
                        if (MusicBand.ident<mb.getId())MusicBand.ident=mb.getId();
                    }
                }
                scannerGson.close();
            }
            catch (Exception e){
                System.out.println("Загрузка из файла не произошла.");
            }
        }
        else         System.out.println("Список банд пуст! Можете создать новые...");

        //System.out.println("Введите имя файла:");
        while (true) {
            /**
             * Если была передана команда exit - завершение программы
             */
            System.out.print("Введите команду: ");
            if (!readCommand(scanner.nextLine(),false)){
                saveMethod();
                break;
            }
        }
    }

    /**
     * Метод сохранения коллекции банд в файл
     */
    private void saveMethod(){
        File file = new File(filename);
        //if(!file.exists())
        try {
            OutputStreamWriter writerGson = new OutputStreamWriter(new FileOutputStream(file));
            Gson gson = new Gson();
            String script = "";
            for (MusicBand mb:bandsList) {
                script+=gson.getAdapter(MusicBand.class).toJson(mb)+"\n";
            }
            script.substring(0,script.length()-2);
            writerGson.write(script);
            writerGson.close();
        }
        catch (Exception e){
            System.out.println("Что-то пошло не так, сохранение не удалось.");
        }

    }

    /**
     * Парсинг переданной команды
     * @param command
     * сама команда
     * @param isScript
     * флаг - источник команды
     * @return
     */
    private boolean readCommand(String command, boolean isScript) {
        switch (command) {
            case ("help"):
                System.out.println("" +
                        "help : вывести справку по доступным командам\n" +
                        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "add {element} : добавить новый элемент в коллекцию\n" +
                        "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                        "remove_by_id id : удалить элемент из коллекции по его id\n" +
                        "clear : очистить коллекцию\n" +
                        "save : сохранить коллекцию в файл\n" +
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                        "exit : завершить программу (без сохранения в файл)\n" +
                        "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                        "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                        "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                        "remove_any_by_number_of_participants numberOfParticipants : удалить из коллекции один элемент, значение поля numberOfParticipants которого эквивалентно заданному\n" +
                        "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                        "print_descending : вывести элементы коллекции в порядке убывания");
                break;
            case ("info"):
                infoMethod();
                break;
            case ("show"):
                showMethod();
                break;
            case ("add"):
                System.out.println("Добавление банды");
                addMethod(isScript);
                break;
            case ("clear"):
                bandsList.clear();
                System.out.println("Коллекция очищена!");
                break;
            case ("save"):
                saveMethod();
                System.out.println("Сохранение в файл произведено!");
                break;
            case ("exit"):
                System.out.println("Завершение работы...");
                return false;
            case ("print_descending"):
                descendingSort();
                break;
            default:
                if (command.startsWith("update ")) {
                    updateMethod(command.substring(7),isScript);
                } else if (command.startsWith("remove_by_id ")) {
                    remove_by_idMethod(command.substring(13));
                } else if (command.startsWith("execute_script ")) {
                    execute_scriptMethod(command.substring(15));
                } else if (command.startsWith("add_if_max")) {
                    add_if_maxMethod(command, isScript);
                } else if (command.startsWith("add_if_min")) {
                    add_if_minMethod(command, isScript);
                } else if (command.startsWith("remove_lower ")) {
                    remove_lowerMethod(command.substring(13));
                } else if (command.startsWith("remove_any_by_number_of_participants ")) {
                    remove_any_by_number_of_participantsMethod(command.substring(37));
                } else if (command.startsWith("filter_starts_with_name ")) {
                    filter_starts_with_nameMethod(command);
                } else {
                    System.out.println("Команда не найдена, повторите ввод:");
                    break;
                }
        }
        return true;
    }

    /**
     * Исполнение скрипта из файла
     * @param filename
     */
    private void execute_scriptMethod(String filename){
        File file = new File(filename);
        if(file.exists()){
            try {
                scriptScanner = new Scanner(file);
                String command;
                while (scriptScanner.hasNext()) {
                    command = scriptScanner.nextLine();
                    if (command!=""&&command!="\n"){
                        System.out.println(command);
                        readCommand(command, true);
                    }
                }
            }
            catch (Exception e){
                System.out.println("Невозможно прочитать скрипт. ");
            }
        }
        else System.out.println("Такого файла нет! ");
    }
    private void remove_lowerMethod(String command){
        try {
            int members = Integer.parseInt(command);
            for (MusicBand mb:bandsList) {
                if(mb.getNumberOfParticipants()<members) bandsList.remove(mb);
            }
            System.out.println("Все банды, в которых меньше "+members+" людей удалены!");
        }
        catch (Exception e){
            System.out.println("Неверно написано количество участников");
        }
    }
    private void add_if_maxMethod(String command, boolean isScript){
        addMethod(isScript);
        int members=0, membersmax=0, previewMembers=0;
        int i = bandsList.size();
        for(MusicBand mb : bandsList){
            i--;
            members=mb.getNumberOfParticipants();
            if(members>membersmax) membersmax=members;
            if (i==1)previewMembers=members;
        }
        if(members!=membersmax||previewMembers==members){
            System.out.println("При проверке оказалось, что условие не выполнено, добавление отменено");
            bandsList.remove(bandsList.toArray()[bandsList.toArray().length-1]);
        }
    }

    private void add_if_minMethod(String command, boolean isScript){
        addMethod(isScript);
        int members=Integer.MAX_VALUE, membersmin=Integer.MAX_VALUE, previewMembers=0;
        int i = bandsList.size();
        for(MusicBand mb : bandsList){
            i--;
            members=mb.getNumberOfParticipants();
            if(members<membersmin) membersmin=members;
            if (i==1)previewMembers=members;
        }
        if(members!=membersmin||previewMembers==members){
            System.out.println("При проверке оказалось, что условие не выполнено, добавление отменено");
            bandsList.remove(bandsList.toArray()[bandsList.toArray().length-1]);
        }
    }
    private void descendingSort() {
        LinkedHashSet<MusicBand> newBandList = new LinkedHashSet<MusicBand>();
        ArrayList<MusicBand> bandsArray = new ArrayList<>(bandsList);
        for (int i = bandsArray.size() - 1; i >= 0; i--)
            newBandList.add(bandsArray.get(i));
        bandsList = newBandList;
        showMethod();
        bandsList = new LinkedHashSet<MusicBand>();
        for (int i = bandsArray.size() - 1; i >= 0; i--)
            bandsList.add(bandsArray.get(i));
    }

    private void addMethod(boolean isScript) {
        Scanner scanner;
        if(isScript){
            scanner=scriptScanner;
        }
        else {
            scanner = new Scanner(System.in);
        }
        String name;
        System.out.print("Введите название: ");
        while (true) {
            name = scanner.nextLine();
            if (isScript) System.out.println(name);
            if (name != null){
                if (isScript) System.out.println(name);
                break;}
        }
        Double x, y;
        System.out.println("Введите координаты: ");
        System.out.print("  X: ");
        while (true) {
            try {
                x = Double.parseDouble(scanner.nextLine());
                if (isScript) System.out.println(x);
                if (x > -110) break;
                else System.out.print("Число должно быть >-110: ");
                if (x == null) System.out.print("Поле не может быть пустым. Введите двоичное число: ");
            } catch (Exception e) {
                System.out.print("Неверный формат. Введите двоичное число: ");
            }
        }
        System.out.print("  Y: ");
        while (true) {
            try {
                y = Double.parseDouble(scanner.nextLine());
                if (isScript) System.out.println(y);
                if (y != null) break;
                else System.out.print("Поле не может быть пустым. Введите двоичное число: ");
            } catch (Exception e) {
                System.out.print("Неверный формат. Введите двоичное число: ");
            }
        }
        Coordinates coordinates = new Coordinates(x, y);
        int numberOfParticipants;
        System.out.print("Введите количество участников банды: ");
        while (true) {
            try {
                numberOfParticipants = Integer.parseInt(scanner.nextLine());
                if (isScript) System.out.println(numberOfParticipants);
                if (numberOfParticipants > 0) break;
                else System.out.print("Число должно быть > 0 : ");
            } catch (Exception e) {
                System.out.print("Неверный формат. Введите целое число: ");
            }
        }

        Integer albumsCount;
        System.out.print("Введите количество альбомов банды: ");
        while (true) {
            try {
                albumsCount = Integer.parseInt(scanner.nextLine());
                if (isScript) System.out.println(albumsCount);
                if (albumsCount > 0) break;
                else System.out.print("Число должно быть > 0 : ");
            } catch (Exception e) {
                System.out.print("Неверный формат. Введите целое число: ");
            }
        }
        java.time.LocalDate establishmentDate;
        String date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Введите дату основания банды в формате yyyy-MM-dd(можно пропустить, нажав Enter): ");
        while (true) {
            date = scanner.nextLine();
            if (isScript) System.out.println(date);
            if (date.length() == 0) {
                establishmentDate = null;
                break;
            }
            try {
                establishmentDate = java.time.LocalDate.parse(date, formatter);
                break;
            } catch (Exception e) {
                System.out.print("Неверный формат. Введите дату (yyyy-MM-dd): ");
            }
        }

        MusicGenre genre = null;
        String genr;
        System.out.print("Введите один из следующих жанров: " + MusicGenre.BLUES + ", " +
                MusicGenre.POST_ROCK + ", " + MusicGenre.SOUL + " (можно пропустить, нажав Enter): ");
        while (true) {
            genr = scanner.nextLine();
            if (isScript) System.out.println(genr);

            if (genr.length() == 0) {
                genre = null;
                break;
            } else
                switch (genr) {
                    case ("BLUES"):
                        genre = MusicGenre.BLUES;
                        break;
                    case ("POST_ROCK"):
                        genre = MusicGenre.POST_ROCK;
                        break;
                    case ("SOUL"):
                        genre = MusicGenre.SOUL;
                        break;
                    default:
                        System.out.print("Такого жанра нет, введите один из предложенных(" + MusicGenre.BLUES + ", " +
                                MusicGenre.POST_ROCK + ", " + MusicGenre.SOUL + ")");
                        break;
                }
            if (genre != null)
                break;
        }

        Person frontMan = null;
        String frontManName;
        System.out.println("Введите информацию о главном солисте (можно пропустить, нажав Enter): ");
        System.out.print("  Введите имя: ");

        frontManName = scanner.nextLine();
        if (isScript) System.out.println(frontManName);
        /**
         * Добавляется ли информация о солисте?
         * Если да, то заполнение данных
         */
        if (frontManName.length() == 0) {
            frontMan = null;
        } else
            while (true) {
                System.out.print("  Введите паспорт (не менее 7 символов): ");
                String passportID;
                while (true) {
                    passportID = scanner.nextLine();
                    if (isScript) System.out.println(passportID);

                    if (passportID.length() >= 7) {
                        break;
                    } else System.out.print("   Длина паспорта должна быть не менее 7 символов. Введите снова: ");
                }

                Color color = null;
                String col;
                System.out.print("  Введите цвет волос (" + Color.BLACK + ", " +
                        Color.GREEN + ", " + Color.RED + ", " + Color.WHITE + ", " + Color.YELLOW + "): ");
                while (true) {
                    col = scanner.nextLine();
                    if (isScript) System.out.println(col);

                    if (col == "") {
                        System.out.println("    Пустое значение недопустимо. Попробуйте одно из этих (" + Color.BLACK + ", " + Color.GREEN + ", " + Color.RED + ", " + Color.WHITE + ", " + Color.YELLOW + "): ");
                    } else
                        switch (col) {
                            case ("BLACK"):
                                color = Color.BLACK;
                                break;
                            case ("GREEN"):
                                color = Color.GREEN;
                                break;
                            case ("RED"):
                                color = Color.RED;
                                break;
                            case ("WHITE"):
                                color = Color.WHITE;
                                break;
                            case ("YELLOW"):
                                color = Color.YELLOW;
                                break;
                            default:
                                System.out.print("  Такого цвета нет, введите один из предложенных(" + Color.BLACK + ", " + Color.GREEN + ", " + Color.RED + ", " + Color.WHITE + ", " + Color.YELLOW + ")");
                                break;
                        }
                    if (color != null)
                        break;
                }

                Location location;
                Double X;
                Integer Y;
                float z;
                String locationName;
                System.out.println("Введите координаты: ");
                System.out.print("  X: ");
                while (true) {
                    try {
                        X = Double.parseDouble(scanner.nextLine());
                        if (isScript) System.out.println(X);
                        break;
                    } catch (Exception e) {
                        System.out.print("  Неверный формат. Введите двоичное число: ");
                    }
                }

                System.out.print("  Y: ");
                while (true) {
                    try {
                        Y = Integer.parseInt(scanner.nextLine());
                        if (isScript) System.out.println(Y);
                        if (Y != null) break;
                        else System.out.print(" Поле не может быть пустым. Введите целое число: ");
                    } catch (Exception e) {
                        System.out.print("  Неверный формат. Введите целое число: ");
                    }
                }

                System.out.print("  Z: ");
                while (true) {
                    try {
                        z = Float.parseFloat(scanner.nextLine());
                        if (isScript) System.out.println(z);
                        break;
                    } catch (Exception e) {
                        System.out.print("  Неверный формат. Введите float число: ");
                    }
                }

                System.out.print("  Введите адрес: ");
                while (true) {
                    locationName=scanner.nextLine();
                    if (isScript) System.out.println(locationName);

                    if (locationName != "") break;
                    else System.out.print(" Поле не может быть пустым. Введите адрес еще раз: ");
                }

                location = new Location(X, Y, z, locationName);

                frontMan = new Person(name, passportID, color, location);


                break;
            }
        bandsList.add(new MusicBand(name, coordinates, numberOfParticipants, albumsCount, establishmentDate, genre, frontMan));
        System.out.println("Банда добавлена!");
    }

    private void showMethod() {
        for (MusicBand mb : bandsList) {
            System.out.println(mb.toString());
        }
    }

    private void infoMethod() {
        System.out.println("Информация о списке:");
        System.out.println("    Тип коллекции: " + bandsList.getClass());
        System.out.println("    Количество элементов: " + bandsList.size());
        System.out.println("    Количество элементов: " + bandsList.size());
    }

    private void remove_by_idMethod(String command){
        try {
            int id = Integer.parseInt(command);
            boolean isDeleted=false;
            for (MusicBand mb:bandsList) {
                if (mb.getId()==id) {
                    isDeleted=true;
                    bandsList.remove(mb);
                    System.out.println("Банда удалена!");
                }
            }
            if (!isDeleted)  System.out.println("Такой банды нет.");
        }
        catch (Exception e){
            System.out.println("Команда задана неверно. ");
        }

    }
    private void filter_starts_with_nameMethod(String command) {
        String name = command.substring(24);
        LinkedHashSet<MusicBand> filteredBands = new LinkedHashSet();
        LinkedHashSet<MusicBand> tempSet = new LinkedHashSet();
        for (MusicBand mb : bandsList) {
            tempSet.add(mb);
            if (mb.getName().startsWith(name))
                filteredBands.add(mb);
        }
        if (filteredBands.size() > 0) {
            bandsList = filteredBands;
            showMethod();
            bandsList = tempSet;
        }
    }

    /**
     * Метод обновления муз. банды по id
     * Принимает строку, содержащую id обновляемой банды.
     *
     * @param command
     */
    private void updateMethod(String command, boolean isScript) {
        try {
            //парсинг строки
            int id = Integer.parseInt(command);
            //создание временного сета, клон основного
            LinkedHashSet<MusicBand> tempSet = new LinkedHashSet();
            for (MusicBand mb : bandsList) {
                tempSet.add(mb);
            }
            //очистка основного сета
            bandsList.clear();
            //проверка на нахождение нужного элемента
            boolean trueId = false;
            //заполнение основного сета с заменой элемента
            for (MusicBand mb : tempSet) {
                if (mb.getId() != id)
                    bandsList.add(mb);
                else {
                    trueId = true;
                    //создание новой банды с использованием готового метода
                    addMethod(isScript);
                    //декремент общего числа элементов, т.к. удалился один из старых
                    MusicBand.ident--;
                }
            }
            //внесение в новый элемент id старого
            if (trueId)
                for (MusicBand mb : bandsList) {
                    if (mb.getId() == MusicBand.ident + 1) {
                        mb.setId(id);
                    }
                }
            else
                System.out.println("Банды с таким ID не существует");
        } catch (Exception e) {
            System.out.println("ID банды введен неверно. ");
        }
    }
    private void remove_any_by_number_of_participantsMethod(String command){

        try {
            //парсинг строки
            int numberOfParticipants = Integer.parseInt(command);
            //булевая переменная, проверка на нахождение
            boolean trueNumberOfParticipants = false;
            //поиск и удаление первого совпадения
            for (MusicBand mb : bandsList) {
                if (mb.getNumberOfParticipants()==numberOfParticipants){
                  bandsList.remove(mb);
                  trueNumberOfParticipants=true;
                  break;
                }
            }
            //если было удалено, то
            if (trueNumberOfParticipants)
                System.out.println("Банда удалена");
            //иначе
            else
                System.out.println("Банды с таким количеством участников не существует");
        } catch (Exception e) {
            System.out.println("Количество участников введено неверно. ");
        }
    }
}