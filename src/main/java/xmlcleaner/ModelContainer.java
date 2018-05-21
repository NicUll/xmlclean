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



    public ModelContainer(){
        Logger.setLogLevel(Logger.LOGLEVEL.NONE);
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
        for(Logger logger: loggers.values()){
            System.out.println(logger.name + ":\n" + logger.getErrorsAsString() + "\n\n");
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


    public XMLModel LoadXMLFile(String filename) {

        XMLModel model;
        //Logger XMLLoaderLogger = this.addLogger("xmlLoader_"+filename);
        try {



            File inputFile = new File(filename);
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputFile);
            Element root = document.getRootElement();
            Map uris = new HashMap();
            XPath xpath;
            List<Node> nodes;
            String result;

            uris.put("xmlns", "http://abalon.se/modelDefinition/1.1");

            //Create model
            xpath = document.createXPath("/xmlns:modelDefinitions/xmlns:model");
            xpath.setNamespaceURIs(uris);
            Node modelnode = xpath.selectSingleNode(document);
            model = new XMLModel(modelnode.valueOf("@name"));



            //Get fields
            xpath = document.createXPath("/xmlns:modelDefinitions/xmlns:model/xmlns:field");

            xpath.setNamespaceURIs(uris);
            nodes = xpath.selectNodes(document);
            for (Node node : nodes) {

                result = String.format("Type: %s, Name: %s, Column: %s, Required: %s, Size: %s, Fieldtype: %s",
                        node.getName(),node.valueOf("@name"), node.valueOf("@column"), node.valueOf("@required"),
                        node.valueOf("@size"), node.valueOf("@fieldType"));
               // XMLLoaderLogger.addEntry(LOGTYPE.INFO, result);
                model.insertField(new XMLField(model, node.valueOf("@name"), node.valueOf("@column"),
                        node.valueOf("@required"), node.valueOf("@size"), node.valueOf("@fieldType")));



            }


            //Get relations
            xpath = document.createXPath("/xmlns:modelDefinitions/xmlns:model/xmlns:relation");
            xpath.setNamespaceURIs(uris);
            nodes = xpath.selectNodes(document);
            for (Node node : nodes) {
                result = String.format("Type: %s, Name: %s, Model: %s, ForeignModel: %s, Cardinality: %s, Field: %s, ForeignField: %s",
                        node.getName(),node.valueOf("@name"), node.valueOf("@model"), node.valueOf("@foreignModel"),
                        node.valueOf("@cardinality"), node.valueOf("@field"), node.valueOf("@foreignField"));
                //XMLLoaderLogger.addEntry(LOGTYPE.INFO, result);
                model.insertRelation(new XMLRelation(model, node.valueOf("@name"), node.valueOf("@model"), node.valueOf("@foreignModel"),
                        node.valueOf("@cardinality"), node.valueOf("@field"), node.valueOf("@foreignField")));



            }

            return model;
        } catch (DocumentException e) {
            e.printStackTrace();

            return new XMLModel("");
        }


    }





}
