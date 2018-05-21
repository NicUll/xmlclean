package logger;

import java.util.LinkedList;


public class Logger {


    private LinkedList<Logentry> entryList = new LinkedList<Logentry>();
    public String name;


    public enum LOGLEVEL {
        NONE, WARNING, ON
    }


    private static LOGLEVEL logLevel = LOGLEVEL.ON;


    public int[] entryTypeAmount = new int[LOGTYPE.values().length];

    public int entries = 0;
    public boolean hasEntry = false;


    public Logger(String name) {
        this.name = name;
    }


    public static void setLogLevel(LOGLEVEL level) {
        logLevel = level;
    }

    public static LOGLEVEL getLogLevel() {
        return logLevel;
    }

    public void addEntry(Logentry entry) {

        if (logLevel != LOGLEVEL.NONE) {
            if (logLevel == LOGLEVEL.ON || entry.getType().name() == logLevel.name()) {
                entryList.add(entry);
                this.entries += 1;
                this.hasEntry = true;

                this.entryTypeAmount[entry.getType().ordinal()] += 1;
            }
        }
    }

    public void addEntry(LOGTYPE type, String value) {

        this.addEntry(new Logentry(this.entries + 1, type, value));


    }

    public LinkedList getEntries() {
        return entryList;
    }

    public String getErrorsAsString() {
        String errorString = "";
        for (Logentry entry : entryList) {
            errorString += String.format("%s: %s \n", entry.getType(), entry.getValue());
        }
        return errorString;
    }


}
