package pl.edu.agh.yapa.extraction;

/**
 * Created by Dominik on 18.06.2014.
 */
public class EngineFactory {
    public static Engine getEngineByName(String engineName) {
        XPathEngine engine = null;
        switch (engineName) {
            case "htmlCleaner" : engine = new XPathEngine();
        }
        return engine;
    }
}
