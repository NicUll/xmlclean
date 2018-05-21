package xmlcleaner;

import logger.LOGTYPE;
import logger.Logger;
import xmlcleaner.ErrorHandler.ErrorContainer;
import xmlcleaner.ErrorHandler.Error;
import xmlcleaner.xml.XMLModel;
import xmlcleaner.xml.XMLRelation;

import java.util.LinkedList;

public class ModelHandler {

    private ModelContainer modelContainer;
    private LinkedList<String> loneRelations = new LinkedList<>();
    private Logger relationLogger;
    private ErrorContainer errorContainer = new ErrorContainer();

    public ModelHandler(ModelContainer modelContainer) {
        this.modelContainer = modelContainer;
    }

    public void iterateRelations() {
        XMLModel currentModel;
        LinkedList<String[]> currentModelStrings;
        this.relationLogger = this.modelContainer.addLogger("RelationLogger");
        for (String currentModelName : this.modelContainer.nameList) {
            currentModel = this.modelContainer.getModel(currentModelName);
            if (currentModel.hasRelations()) {
                currentModelStrings = this.searchModel(currentModel);
            } else {
                System.out.println(String.format("%s has no relations.", currentModelName)); //#DEBUG
            }
            System.out.printf("\n##############################\n"); //#DEBUG

        }
        for (String item : this.loneRelations) {
            System.out.printf("Look at: %s\n", item);
        }
        for(Error error : errorContainer.getAllErrors()){
            System.out.println(error.getType() + ": " + error.getRelation());

        }
        System.out.println(errorContainer.getAllErrors());

    }

    /**
     *
     * @param model
     * @return list of stringpairs, representing the pair of relations.
     * If no corresponding found, assign "NO_RELATION_FOUND"
     */
    public LinkedList<String[]> searchModel(XMLModel model) {
        LinkedList<String[]> relationStringList = new LinkedList<String[]>();
        XMLRelation relation;
        for (String relationName : model.nameList) {
            relation = model.getRelation(relationName);
            relationStringList.add(generateRelationStrings(relation));
        }
        return relationStringList;
    }

    public String[] generateRelationStrings(XMLRelation relation) {
        String relationA, relationB;
        relationA = findCurrentRelation(relation);
        relationB = findCorrespondingRelation(relation);
        return new String[]{relationA, relationB};
    }

    public String findCurrentRelation(XMLRelation relation){
        return relation.getParent().getName() + "." + relation.getName();
    }


    public String findCorrespondingRelation(XMLRelation originRelation) {
        boolean self_referencing; //Compares the objects, not the names (Strings)
        XMLModel targetModel;
        XMLRelation currentTargetRelation;
        String returnString = "NO_RELATION_FOUND";

        String originModelName = originRelation.getParent().getName();
        String originRelationName = originRelation.getName();
        String targetModelName;

        /************************************/
        //Check for null-vale in relation-fields
        if (originRelation.hasNull()) {
            this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s has missing field values.\n", originRelation.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName, "MissingValue", "Is missing one or more required values.");
            return returnString;
        }

        /************************************/
        //Does targetmodel exist?
        self_referencing = this.modelContainer.models.get(originRelation.getModel()) == originRelation.getParent();
        targetModel = self_referencing ?
                this.modelContainer.models.get(originRelation.getForeignModel()) :
                this.modelContainer.models.get(originRelation.getModel());
        targetModelName = targetModel.getName();
        if (targetModel == null) {
            this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s.%s is pointing to a nonexistent model.", originRelation.getParent().getName(), originRelation.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName + "." + targetModelName, "NoTargetModel", "The model does not exsist.");
            return returnString;
        }

        /*************************************/
        //Does targetmodel even have relations?
        if (!targetModel.hasRelations()) {
            this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s has no relations", targetModel.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName + "." + targetModelName, "TargetModelNoRelation", "The supposed corresponding model has no relations.");
            return returnString;
        }

        /************************************/
        //Everything is correct
        for(String targetRelationName : targetModel.nameList) {
            currentTargetRelation = targetModel.getRelation(targetRelationName);

            if (targetModel.getName().equals(originRelation.getParent().getName()) && originRelation.correspondsTo(currentTargetRelation)) {
                return targetModelName + "." + currentTargetRelation.getName();
            }
        }
        this.errorContainer.addError(originModelName + "." + originRelationName, "LoneRelation", "Has no corresponding");
        this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s.%s has no corresponding in %s",
                originRelation.getParent().getName(), originRelation.getName(), targetModel.getName()));

        return returnString;
    }


}
