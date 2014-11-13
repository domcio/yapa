package pl.edu.agh.yapa.persistence;

public class InvalidDatabaseStateException extends Exception {
    public InvalidDatabaseStateException(String s) {
        super(s);
    }
}
