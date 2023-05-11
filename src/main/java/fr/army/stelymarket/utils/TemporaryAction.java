package fr.army.stelymarket.utils;

public class TemporaryAction {

    private String actorName = "";
    private TemporaryActionNames actionName = TemporaryActionNames.NULL;

    public TemporaryAction(String actorName, TemporaryActionNames actionName){
        this.actorName = actorName;
        this.actionName = actionName;
    }


    public String getActorName() {
        return this.actorName;
    }

    public TemporaryActionNames getActionName() {
        return this.actionName;
    }
}
