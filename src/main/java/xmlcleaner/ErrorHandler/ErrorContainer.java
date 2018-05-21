package xmlcleaner.ErrorHandler;

import java.util.HashMap;
import java.util.LinkedList;

public class ErrorContainer {

    private HashMap<String, LinkedList<Error>> errors = new HashMap<>();

    public ErrorContainer(){

    }

    public LinkedList getErrorsOfType(String type){
        return errors.get(type);
    }

    public LinkedList<Error> getAllErrors(){
        LinkedList<Error> returnList = new LinkedList<Error>();
        for(LinkedList errorList : this.errors.values()){
            returnList.addAll(errorList);
        }
        return returnList;
    }

    public void addError(String relation, String type, String description){
        if(!errors.containsKey(type)){
            errors.put(type, new LinkedList<Error>());
        }
        errors.get(type).add(new Error(relation, type, description));
    }


}
