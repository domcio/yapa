package pl.edu.agh.yapa.persistence;

/**
 * @author pawel
 */
//TODO why does this extend Throwable?
public class InvalidDatabaseStateException extends Throwable {
    public InvalidDatabaseStateException(String s) {
        super(s);
    }
}
