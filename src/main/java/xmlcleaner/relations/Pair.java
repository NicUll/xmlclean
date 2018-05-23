package xmlcleaner.relations;

public class Pair {

    private String[] relations = new String[2];

    private boolean error;


    public Pair(){

    }

    public void addRelation(String relation){
        int index = this.relations[0] == null ? 0 : 1;
        this.relations[index] = relation;
    }

    public String[] getRelations(){
        return relations;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean hasError(){
        return this.error;
    }




}
