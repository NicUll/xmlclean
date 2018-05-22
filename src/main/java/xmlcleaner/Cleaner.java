package xmlcleaner;


import logger.Logger;
import logger.LOGTYPE;


public class Cleaner {



    public static void main(String[] args){
        ModelContainer modelContainer = new ModelContainer();
        modelContainer.setUris("xmlns", "http://abalon.se/modelDefinition/1.1");
        ModelHandler modelHandler = new ModelHandler(modelContainer);

        modelContainer.setLogLevel(Logger.LOGLEVEL.NONE);
        Logger mainLogger = modelContainer.addLogger("mainLogger");

        mainLogger.addEntry(LOGTYPE.INFO, "Created model");
        mainLogger.addEntry(LOGTYPE.INFO, "Created mainlogger");

        modelContainer.loadXMLFolder("C:\\Users\\null\\IdeaProjects\\xmlclean\\src\\main\\resources\\core\\");

        modelContainer.setLogLevel(Logger.LOGLEVEL.ON);
        try{
            modelHandler.iterateModels();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        modelContainer.printAllLogs();


    }
}
