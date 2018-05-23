package xmlcleaner;


import logger.LOGTYPE;
import logger.Logger;
import org.dom4j.*;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import xmlcleaner.xml.XMLField;
import xmlcleaner.xml.XMLModel;
import xmlcleaner.xml.XMLRelation;

import java.io.File;
import java.util.*;

//"C:\\Users\\null\\IdeaProjects\\XMLCleaner\\src\\test\\resources\\core\\MemberAccount.xml"

public class ModelContainer {


    public HashMap<String, Logger> loggers = new HashMap<>();
    public HashMap<String, XMLModel> models = new HashMap<>();

    public LinkedList<String> nameList = new LinkedList<>();

    private Map uris = new HashMap();



    public ModelContainer(){
        Logger.setLogLevel(Logger.LOGLEVEL.NONE);
    }

    public void setUris(String uriKey, String uriValue){
        this.uris.put(uriKey, uriValue);
    }

    public XMLModel getModel(String name){
        return models.get(name);
    }

    public Logger addLogger(String name){
        Logger newLogger = new Logger(name);
        loggers.put(name, newLogger);
        return newLogger;
    }



    public void setLogLevel(Logger.LOGLEVEL level){
        Logger.setLogLevel(level);
    }



    public String getAllLogs(){
        String logString = "";
        for(Logger logger: loggers.values()){
            logString += logger.name + ":\n" + logger.getErrorsAsString() + "\n\n";
        }
        return logString;
    }

    public void printAllLogs(){
        if (Logger.getLogLevel() == Logger.LOGLEVEL.NONE){
            return;
        }
        for(Logger logger: loggers.values()){
            System.out.println("\n\t\t" + logger.name + ":\n" + logger.getErrorsAsString() + "\n\n");
        }
    }

    public void loadXMLFolder(String folderPath){
        Logger folderLogger = this.addLogger("folderLogger");
        File dir = new File(folderPath);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null){


            folderLogger.addEntry(LOGTYPE.INFO, "Loaded directory with files");
            XMLModel tempModel;

            for(File child : directoryListing){
                tempModel = LoadXMLFile(folderPath+child.getName());
                models.put(tempModel.getName(), tempModel);
                this.nameList.add(tempModel.getName());
                folderLogger.addEntry(LOGTYPE.INFO, String.format("Loaded XML-file: %s", child.getName()));

            }
        }else{
            folderLogger.addEntry(LOGTYPE.INFO, "Folder empty");
        }
    }


    private XPath createAndHandleXPath(Document document, String xpathExpression){
        XPath returnXPath = document.createXPath(xpathExpression);
        returnXPath.setNamespaceURIs(this.uris);
        return returnXPath;
    }

    public XMLModel LoadXMLFile(String filename) {

        XMLModel model;
        File inputFile = new File(filename);
        SAXReader reader = new SAXReader();

        try {
            Document document = reader.read(inputFile);
            XPath xpath;
            List<Node> nodes;


            //Create model
            xpath = createAndHandleXPath(document, "/xmlns:modelDefinitions/xmlns:model");
            Node modelnode = xpath.selectSingleNode(document);
            model = new XMLModel(modelnode.valueOf("@name"));

            //Get fields
            xpath = createAndHandleXPath(document, "/xmlns:modelDefinitions/xmlns:model/xmlns:field");
            nodes = xpath.selectNodes(document);
            for (Node node : nodes) {
                model.insertXMLEntry(new XMLField(model, node.valueOf("@name"), node.valueOf("@column"),
                        node.valueOf("@required"), node.valueOf("@size"), node.valueOf("@fieldType")));
            }

            //Get relations
            xpath = createAndHandleXPath(document, "/xmlns:modelDefinitions/xmlns:model/xmlns:relation");
            nodes = xpath.selectNodes(document);
            for (Node node : nodes) {
                model.insertXMLEntry(new XMLRelation(model, node.valueOf("@name"), node.valueOf("@model"), node.valueOf("@foreignModel"),
                        node.valueOf("@cardinality"), node.valueOf("@field"), node.valueOf("@foreignField")));
            }

        } catch (DocumentException e) {
            e.printStackTrace();

            model = new XMLModel("");

        }
        return model;


    }







}
