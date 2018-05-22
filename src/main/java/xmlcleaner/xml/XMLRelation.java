package xmlcleaner.xml;

public class XMLRelation implements XMLEntry{

    private String name;
    private String model;
    private String foreignModel;
    private String cardinality;
    private String field;
    private String foreignField;
    private boolean remove;
    private boolean reset;
    public static String entryType = "XMLRelation";

    private XMLModel parent;


    public XMLRelation(XMLModel parent, String model, String foreignModel, String cardinality, String field, String foreignField, boolean remove, boolean reset, String name) {
        this.parent = parent;
        this.name = name;
        this.model = model;
        this.foreignModel = foreignModel;
        this.cardinality = cardinality;
        this.field = field;
        this.foreignField = foreignField;
        this.remove = remove;
        this.reset = reset;
    }

    public XMLRelation(XMLModel parent, String name, String model, String foreignModel, String cardinality, String field, String foreignField){
        this(parent, model, foreignModel, cardinality, field, foreignField, false, false, name);
    }

    public XMLRelation(XMLModel parent, String name){
        this(parent, name, null, null, null, null, null);
    }

    @Override
    public XMLModel getParent(){
        return this.parent;
    }



    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public String getEntryType(){
        return entryType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getForeignModel() {
        return foreignModel;
    }

    public void setForeignModel(String foreignModel) {
        this.foreignModel = foreignModel;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getForeignField() {
        return foreignField;
    }

    public void setForeignField(String foreignField) {
        this.foreignField = foreignField;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }


    public String getValues(){
        return String.format("Model=%s, ForeignModel=%s, Field=%s, ForeignField=%s",this.model,this.foreignModel,this.field,this.foreignField);
    }

    public boolean correspondsTo(XMLRelation that){
        return
                (this.getModel().equals(that.getModel()))
                && (this.getField().equals(that.getField()))
                && (this.getForeignModel().equals(that.getForeignModel()))
                && (this.getForeignField().equals(that.getForeignField()));
    }

    public boolean hasNull(){
        return
                (this.getModel().equals(null))
                        || (this.getField().equals(null))
                        || (this.getForeignModel().equals(null))
                        || (this.getForeignField().equals(null));
    }
}
