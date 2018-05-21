package xmlcleaner;

import xmlcleaner.xml.XMLRelation;

public class RelationError {
    private XMLRelation relation;
    private String type;
    private String description;


    public RelationError(XMLRelation relation, String type, String description){
        this.relation = relation;
        this.type = type;
        this.description = description;
    }

    public XMLRelation getRelation(){
        return this.relation;
    }

    public String getType(){
        return this.type;
    }

    public String getDescription(){
        return this.description;
    }


}
