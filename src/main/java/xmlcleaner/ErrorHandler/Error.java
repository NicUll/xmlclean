package xmlcleaner.ErrorHandler;

public class Error {
    private String relation;
    private String type;
    private String description;


    public Error(String relation, String type, String description){
        this.relation = relation;
        this.type = type;
        this.description = description;
    }

    public String getRelation(){
        return this.relation;
    }

    public String getType(){
        return this.type;
    }

    public String getDescription(){
        return this.description;
    }


}
