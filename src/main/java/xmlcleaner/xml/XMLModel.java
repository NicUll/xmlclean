package xmlcleaner.xml;

import logger.LOGTYPE;
import logger.Logger;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Used to represent the XML-files
 */

public class XMLModel {

    private final String name;

    private String primaryKey;
    private Logger logger = new Logger("Model");

    private HashMap<String, XMLField> fieldMap = new HashMap<>();
    private HashMap<String, XMLRelation> relationMap = new HashMap<>();
    private HashMap<String, HashMap> entryMaps = new HashMap<>();


    public LinkedList<String> relationNameList = new LinkedList<>();
    public LinkedList<String> fieldNameList = new LinkedList<>();
    private HashMap<String, LinkedList> nameLists = new HashMap<>();

    public XMLModel(String name) {
        this.name = name;

        this.entryMaps.put(XMLField.entryType, fieldMap);
        this.entryMaps.put(XMLRelation.entryType, relationMap);

        this.nameLists.put(XMLField.entryType, fieldNameList);
        this.nameLists.put(XMLRelation.entryType, relationNameList);

    }



    public String getName() {
        return name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void insertXMLEntry(XMLEntry entry) {
        String currentEntryType = entry.getEntryType();
        String entryName = entry.getName();
        if (entryMaps.get(currentEntryType).containsValue(entry)) {
            logger.addEntry(LOGTYPE.WARNING, String.format("Duplicate field: %s", entryName));

        }
        entryMaps.get(currentEntryType).put(entryName, entry); //Store entry in appropriate map depending on if field or relation
        nameLists.get(currentEntryType).add(entryName);

    }


    public XMLEntry getEntry(String entryType, String key){
        return (XMLEntry) this.entryMaps.get(entryType).get(key);
    }



    public XMLField getField(String key) {
        return this.fieldMap.get(key);
    }



    public XMLRelation getRelation(String key) {
        return this.relationMap.get(key);
    }

    public boolean hasRelations(){
        return this.relationMap != null;
    }



}
