package xmlcleaner;

import logger.LOGTYPE;
import logger.Logger;
import xmlcleaner.ErrorHandler.ErrorContainer;
import xmlcleaner.ErrorHandler.Error;
import xmlcleaner.relations.Mapper;
import xmlcleaner.xml.XMLField;
import xmlcleaner.xml.XMLModel;
import xmlcleaner.xml.XMLRelation;

public class ModelHandler {

    private ModelContainer modelContainer;
    private Logger relationLogger;
    private ErrorContainer errorContainer = new ErrorContainer();
    private Mapper relationMapper = new Mapper();

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

        }

        for (Error error : errorContainer.getAllErrors()) {
            System.out.println(error.getType() + ": " + error.getRelation());
        }


    }

    /**
     * @param model
     * @return list of stringpairs, representing the pair of relations.
     * If no corresponding found, assign "NO_RELATION_FOUND"
     */
    public void searchModel(XMLModel model) {
        XMLRelation relation;
        for (String relationName : model.relationNameList) {
            relation = model.getRelation(relationName);
            if (checkOriginRelation(relation) && checkTargetRelation(relation)) {
                XMLModel modelA = modelContainer.getModel(relation.getModel());
                XMLModel modelB = modelContainer.getModel(relation.getForeignModel());
                XMLField fieldA = modelA.getField(relation.getField());
                XMLField fieldB = modelB.getField(relation.getForeignField());
                if(fieldA == null || fieldB == null){
                    String originRelationPath = String.format("%s.%s", model.getName(), relationName);
                    String targetRelationPath = relationMapper.getRelationPair(originRelationPath).getRelations()[1];
                    this.errorContainer.addError(originRelationPath, "FieldNotFound", "Relation points to a nonexistent field");
                    this.errorContainer.addError(targetRelationPath,"FieldNotFound", "Relation points to a nonexistent field" );
                }

                else if (fieldA.hasNull() || fieldB.hasNull()) {
                    if (fieldA.hasNull()) {
                        this.errorContainer.addError(String.format("%s.%s", modelA.getName(), fieldA.getName()), "BrokenField", "The field is missing one or more values");
                    }
                    if (fieldB.hasNull()) {
                        this.errorContainer.addError(String.format("%s.%s", modelB.getName(), fieldB.getName()), "BrokenField", "The field is missing one or more values");
                    }
                    String originRelationPath = String.format("%s.%s", model.getName(), relationName);
                    String targetRelationPath = relationMapper.getRelationPair(originRelationPath).getRelations()[1];
                    this.errorContainer.addError(originRelationPath, "PointsToBrokenField", "Relation points to one or more incorrect fields");
                    this.errorContainer.addError(targetRelationPath,"PointsToBrokenField", "Relation points to one or more incorrect fields" );
                }
            }
        }
    }

    public String formatXMLPathString(String[] paths) {
        String returnString = "";
        for (int i = 0; i < paths.length - 2; i++) {
            returnString += paths[i] + ".";
        }
        returnString += paths[-1];
        return returnString;
    }

    public boolean checkOriginRelation(XMLRelation relation) {
        String originModelName = relation.getParent().getName();
        String originRelationName = relation.getName();
        /************************************/
        //Check for null-vale in relation-fields
        if (relation.hasNull()) {
            this.relationLogger.addEntry(LOGTYPE.WARNING, String.format("%s.%s has missing field values.\n", originModelName, originRelationName));
            this.errorContainer.addError(originModelName + "." + originRelationName, "MissingValue", "Is missing one or more required values.");
            return false;
        }
        return true;
    }


    public boolean checkTargetRelation(XMLRelation originRelation) {
        boolean self_referencing;
        XMLModel targetModel;
        XMLRelation currentTargetRelation;

        String originModelName = originRelation.getParent().getName();
        String originRelationName = originRelation.getName();
        String targetModelName;


        /************************************/
        //Does targetmodel exist?
        self_referencing = this.modelContainer.models.get(originRelation.getModel()) == originRelation.getParent();
        targetModelName = self_referencing ? originRelation.getForeignModel() : originRelation.getModel();
        targetModel = this.modelContainer.models.get(targetModelName);

        if (targetModelName.equals(null)) {
            this.relationLogger.addEntry(LOGTYPE.WARNING, String.format("%s.%s points to no model", originRelation.getParent().getName(), originRelation.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName, "NoTargetModel", "The relation doesn't point to another model.");
            return false;
        }

        if (targetModel == null) {
            this.relationLogger.addEntry(LOGTYPE.WARNING, String.format("%s.%s is pointing to a nonexistent model.", originRelation.getParent().getName(), originRelation.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName + "." + targetModelName, "NullTargetModel", "The model does not exsist.");
            return false;
        }

        /*************************************/
        //Does targetmodel even have relations?
        if (!targetModel.hasRelations()) {
            this.relationLogger.addEntry(LOGTYPE.WARNING, String.format("%s has no relations", targetModel.getName()));
            this.errorContainer.addError(originModelName + "." + originRelationName + "." + targetModelName, "TargetModelNoRelation", "The supposed corresponding model has no relations.");
            return false;
        }

        /************************************/
        //Everything is correct, start finding target
        for (String targetRelationName : targetModel.relationNameList) {
            currentTargetRelation = targetModel.getRelation(targetRelationName);

            if (originRelation.correspondsTo(currentTargetRelation)) {
                this.relationLogger.addEntry(LOGTYPE.INFO, String.format("OR: %s.%s tied to TR: %s.%s", originModelName, originRelationName, targetModelName, targetRelationName));
                this.relationMapper.addRelation(originModelName, originRelationName, targetModelName, targetRelationName);
                return true;
            }
        }
        this.errorContainer.addError(originModelName + "." + originRelationName, "LoneRelation", "Has no corresponding");
        this.relationLogger.addEntry(LOGTYPE.WARNING, String.format("%s.%s has no corresponding in %s",
                originRelation.getParent().getName(), originRelation.getName(), targetModel.getName()));
        return false;


    }


}
