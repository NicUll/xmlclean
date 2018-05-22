package xmlcleaner.xml;

public class XMLField implements XMLEntry{

    private String name;
    private String column;
    private String required;
    private String size;
    private String fieldType;
    private XMLModel parent;
    private String nameSpace;
    private String owner;
    public static String entryType = "XMLField";



    public XMLField(XMLModel parent, String name, String column, String required, String size, String fieldType) {
        this.parent = parent;
        this.name = name;
        this.column = column;
        this.required = required;
        this.size = size;
        this.fieldType = fieldType;
    }

    public XMLField(XMLModel parent, String name) {
        this(parent, name, null, null, null, null);
    }

    @Override
    public XMLModel getParent(){
        return this.parent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getNameSpace() {
        return this.nameSpace;
    }

    @Override
    public String getOwner() {
        return this.owner;
    }

    @Override
    public String getEntryType(){
        return entryType;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public boolean hasNull(){
        return
                this.name.equals(null)
                || this.column.equals(null)
                || this.required.equals(null)
                || this.size.equals(null)
                || this.fieldType.equals(null);
    }


}

