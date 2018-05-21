package xmlcleaner.xml;

public interface XMLEntry {
    XMLModel getParent();
    String getName();
    String getOwner();
    String getType();

}
