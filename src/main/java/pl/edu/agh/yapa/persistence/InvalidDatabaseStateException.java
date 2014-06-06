package pl.edu.agh.yapa.persistence;

/**
 * @author pawel
 */
public class InvalidDatabaseStateException extends Throwable {
    public InvalidDatabaseStateException(String s) {
        super(s);
    }
}
