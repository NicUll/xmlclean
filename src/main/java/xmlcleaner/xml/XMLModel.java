package xmlcleaner.xml;

import logger.LOGTYPE;
import logger.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Used to represent the XML-files
 */

public class XMLModel {

    private final String name;
    private String parent; //possibly omittable
    private String primaryKey;
    private HashMap<String, XMLField> fieldMap = new HashMap<>();
    private HashMap<String, XMLRelation> relationMap = new HashMap<>();
    private Logger logger = new Logger("Model");

    public LinkedList<String> nameList = new LinkedList<>();

    public XMLModel(String name) {
        this.name = name;
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
    }

    public void insertField(XMLField inField) {
        this.insertXMLEntry(inField, fieldMap);
        this.fieldMap.put(inField.getName(), inField);

    }

    public void insertRelation(XMLRelation inRelation) {
        this.insertXMLEntry(inRelation, fieldMap);
        String relationName = inRelation.getName();
        this.relationMap.put(relationName, inRelation);
        this.nameList.add(relationName);
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
