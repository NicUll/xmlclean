package xmlcleaner;


import logger.Logger;
import logger.LOGTYPE;

import static logger.LOGTYPE.ERROR;
import static logger.LOGTYPE.INFO;
import static logger.LOGTYPE.WARNING;


public class Cleaner {



    public static void main(String[] args){
        ModelContainer modelContainer = new ModelContainer();
        modelContainer.setUris("xmlns", "http://abalon.se/modelDefinition/1.1");
        ModelHandler modelHandler = new ModelHandler(modelContainer);

        //Logger.setLogLevelByType(new LOGTYPE[]{WARNING, ERROR});
        Logger.setLogLevel(Logger.LOGLEVEL.NONE);

        Logger mainLogger = modelContainer.addLogger("mainLogger");

        mainLogger.addEntry(INFO, "Created model");
        mainLogger.addEntry(INFO, "Created mainlogger");

        modelContainer.loadXMLFolder("C:\\Users\\null\\IdeaProjects\\xmlclean\\src\\main\\resources\\core\\");



        try{
            modelHandler.iterateModels();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        modelContainer.printAllLogs();


    }
}
