package xmlcleaner;

import logger.LOGTYPE;
import logger.Logger;
import xmlcleaner.ErrorHandler.ErrorContainer;
import xmlcleaner.ErrorHandler.Error;
import xmlcleaner.xml.XMLModel;
import xmlcleaner.xml.XMLRelation;

public class ModelHandler {

    private ModelContainer modelContainer;
    private Logger relationLogger;
    private ErrorContainer errorContainer = new ErrorContainer();

    public ModelHandler(ModelContainer modelContainer) {
        this.modelContainer = modelContainer;
    }

    public void iterateModels() {
        XMLModel currentModel;
        this.relationLogger = this.modelContainer.addLogger("RelationLogger");
        for (String currentModelName : this.modelContainer.nameList) {
            currentModel = this.modelContainer.getModel(currentModelName);
            if (currentModel.hasRelations()) {
                this.searchModel(currentModel);
            } else {
                this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s has no relations.", currentModelName));
            }
            System.out.printf("\n##############################\n"); //#DEBUG

        }

        for(Error error : errorContainer.getAllErrors()){
            System.out.println(error.getType() + ": " + error.getRelation());
        }



    }

    /**
     *
     * @param model
     * @return list of stringpairs, representing the pair of relations.
     * If no corresponding found, assign "NO_RELATION_FOUND"
     */
    public void searchModel(XMLModel model) {
        XMLRelation relation;
        for (String relationName : model.relationNameList) {
            relation = model.getRelation(relationName);
            if(checkOriginRelation(relation)) {
                checkTargetRelation(relation);
            }
        }
    }

    public boolean checkOriginRelation(XMLRelation relation){
        String originModelName = relation.getParent().getName();
        String originRelationName = relation.getName();
        this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("Origin-Relation: %s.%s",originModelName, originRelationName));
        /************************************/
        //Check for null-vale in relation-fields
        if (relation.hasNull()) {
            this.relationLogger.addEntry(LOGTYPE.WARNING, String.format("%s.%s has missing field values.\n", originModelName, originRelationName));
            this.errorContainer.addError(originModelName + "." + originRelationName, "MissingValue", "Is missing one or more required values.");
            return false;
        }
        return true;
    }


    public void checkTargetRelation(XMLRelation originRelation) {
        boolean self_referencing;
        XMLModel targetModel;
        XMLRelation currentTargetRelation;

        String originModelName = originRelation.getParent().getName();
        String originRelationName = originRelation.getName();
        String targetModelName;



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
        }

        /*************************************/
        //Does targetmodel even have relations?
        if (!targetModel.hasRelations()) {
            this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s has no relations", targetModel.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName + "." + targetModelName, "TargetModelNoRelation", "The supposed corresponding model has no relations.");
        }

        /************************************/
        //Everything is correct
        for(String targetRelationName : targetModel.relationNameList) {
            currentTargetRelation = targetModel.getRelation(targetRelationName);

            if (targetModel.getName().equals(originRelation.getParent().getName()) && originRelation.correspondsTo(currentTargetRelation)) {
                System.out.println(targetModelName + "." + currentTargetRelation.getName());
            }
        }
        this.errorContainer.addError(originModelName + "." + originRelationName, "LoneRelation", "Has no corresponding");
        this.relationLogger.addEntry(LOGTYPE.APPLICATION, String.format("%s.%s has no corresponding in %s",
                originRelation.getParent().getName(), originRelation.getName(), targetModel.getName()));


    }


}
