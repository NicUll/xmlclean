package xmlcleaner.xml;

public class GenXMLEntry implements XMLEntry {
    private XMLModel parent;
    private String name;
    private String nameSpace;
    private String owner;



    @Override
    public XMLModel getParent(){
        return this.parent;
    }


    @Override
    public String getName() {
        return null;
    }

    public String getNameSpace() {
        return null;
    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public String getEntryType() {
        return null;
    }

}
