package xmlcleaner.relations;


import java.util.HashMap;

public class Mapper {

    private final HashMap<String, Pair> relations = new HashMap<>();


    public Mapper() {

    }

    private void addRelation(String relationA, String relationB) {
        Pair tempPair = new Pair();
        tempPair.addRelation(relationA);
        tempPair.addRelation(relationB);
        this.relations.put(relationA, tempPair);
        this.relations.put(relationB, tempPair);
    }

    public void addRelation(String modelA, String relationA, String modelB, String relationB){
        this.addRelation(String.format("%s.%s", modelA, relationA), String.format("%s.%s",modelB,relationB));
    }

    public boolean hasRelation(String model, String relation){
        return hasRelation(String.format("%s.%s", model, relation));
    }

    private boolean hasRelation(String relation) {
        return relations.containsKey(relation);
    }

    public Pair getRelationPair(String relation){
        return relations.get(relation);
    }

    public Pair getRelationPair(String model, String relation){
        return getRelationPair(String.format("%s.%s", model, relation));
    }



}
