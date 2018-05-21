package xmlcleaner;

import xmlcleaner.xml.XMLRelation;

import java.util.HashMap;
import java.util.LinkedList;

public class ErrorContainer {

    private HashMap<String, LinkedList<RelationError>> errors = new HashMap<>();

    public ErrorContainer(){

    }

    public LinkedList getErrorsOfType(String type){
        return errors.get(type);
    }

    public LinkedList<RelationError> getAllErrors(){
        LinkedList<RelationError> returnList = new LinkedList<RelationError>();
        for(LinkedList errorList : this.errors.values()){
            returnList.addAll(errorList);
        }
        return returnList;
    }

    public void addError(XMLRelation relation, String type, String description){
        if(!errors.containsKey(type)){
            errors.put(type, new LinkedList<RelationError>());
        }
        errors.get(type).add(new RelationError(relation, type, description));
    }


}
