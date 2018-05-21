package xmlcleaner.relations;

public class Pair {

    private String[] relations = new String[2];
    private boolean fed;
    private boolean error;


    public Pair(){
        this.fed = false;
    }

    public void addRelation(String relation){
        if(this.fed) {
            this.error = true;
            return;
        }
        int index = this.relations[0] == null ? 0 : 1;
        this.relations[index] = relation;
        this.fed = index==1;
    }

    public String[] getRelations(){
        return relations;
    }

    public boolean isFed(){
        return fed;
    }

    public boolean hasError(){
        return error;
    }

}
