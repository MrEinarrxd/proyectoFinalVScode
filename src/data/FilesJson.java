package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public class FilesJson<T> {

    private final Gson gson;
    private final Type listType;

    public FilesJson(Type listType) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.listType = listType;
    }

    // Escribe una lista completa en el archivo JSON (sobrescribe)
    public void writeList(String fileName, ArrayList<T> list) {
        String updatedJson = gson.toJson(list);
        Path path = Paths.get(fileName);

        try {
            Files.writeString(path, updatedJson);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Escribe un solo objeto en el archivo JSON (agrega)
    public void appendToFile(String fileName, T obj) {
        ArrayList<T> ALObj = readFile(fileName);
        ALObj.add(obj);
        writeList(fileName, ALObj);
    }

    // Lee una lista de objetos desde el archivo JSON
    public ArrayList<T> readFile(String fileName) {
        ArrayList<T> objs = new ArrayList<>();
        Path path = Paths.get(fileName);

        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                objs = gson.fromJson(json, listType);
                if (objs == null) {
                    objs = new ArrayList<>();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return objs;
    }
}