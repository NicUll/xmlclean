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

        this.nameLists.put(XMLField.entryType, relationNameList);
        this.nameLists.put(XMLRelation.entryType, fieldNameList);
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

    public void insertXMLEntry(XMLEntry entry, HashMap entryMap) {
        if (entryMap.containsValue(entry)) {
            logger.addEntry(LOGTYPE.WARNING, String.format("Duplicate field: %s", entry.getName()));
        }
        String currentEntryType = entry.getEntryType();
        entryMaps.get(currentEntryType).put(entry.getName(), entry); //Store entry in appropriate map depending on if field or relation
        nameLists.get(currentEntryType).add(entry.getName());
    }

    public void insertField(XMLField inField) {
        this.insertXMLEntry(inField, fieldMap);
        String fieldName = inField.getName();
        this.fieldMap.put(fieldName, inField);
        this.fieldNameList.add(fieldName);

    }

    public void insertRelation(XMLRelation inRelation) {
        this.insertXMLEntry(inRelation, relationMap);
        String relationName = inRelation.getName();
        this.relationMap.put(relationName, inRelation);
        this.relationNameList.add(relationName);
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
