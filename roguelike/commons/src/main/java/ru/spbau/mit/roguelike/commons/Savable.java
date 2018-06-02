package ru.spbau.mit.roguelike.commons;

import java.io.*;
import java.util.Base64;

/**
 * Savable object. Shows that this object can be saved by calling save method and loaded by calling load method.
 */
public interface Savable extends Serializable {
    default String save() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    static Savable load(String s) throws IOException, ClassNotFoundException {
        InputStream inputStream = new ByteArrayInputStream(s.getBytes());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Savable result = (Savable)objectInputStream.readObject();
        objectInputStream.close();
        return result;
    }
}
