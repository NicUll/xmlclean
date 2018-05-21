package xmlcleaner.relations;

import xmlcleaner.xml.XMLModel;

import java.util.HashMap;

public class Mapper {

    private HashMap<String, Pair> relations = new HashMap<>();
    private HashMap<String, XMLModel> models = new HashMap<>();

    public Mapper(){

    }

    public void addRelation(String relationA, String relationB){

        //Assumes relationB is a good "guess" which relation relationA should be mapped to
        if(this.relations.get(relationA) == null) {
            Pair tempPair = new Pair();
            tempPair.addRelation(relationA);
            this.relations.put(relationA, tempPair);
            this.relations.put(relationB, tempPair);
        }else{
            this.relations.get(relationA).addRelation(relationA);
        }
    }
/*
    public void addRelation(Pair relationPair){
        if(this.relations.get(relationPair.getRelations()[0]) == null){
            this.relations.put(relationPair.getRelations()[0], relationPair);
            this.relations.put(relationPair.getRelations()[0], relationPair);
        }else{
            this.relations.get(relationPair.getRelations()[0]).addRelation(relationA);
        }
    }
*/
    public void addModel(XMLModel model){
        this.models.put(model.getName(), model);
    }

    public XMLModel getModel(String name){
        return this.models.get(name);
    }

}
