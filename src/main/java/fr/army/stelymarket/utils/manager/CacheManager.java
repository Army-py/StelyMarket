package fr.army.stelymarket.utils.manager;

import java.util.ArrayList;

import fr.army.stelymarket.utils.TemporaryAction;
import fr.army.stelymarket.utils.TemporaryActionNames;


public class CacheManager {
    // {senderName, receiverName, actionName, Team}
    private ArrayList<TemporaryAction> cachedTempAction = new ArrayList<TemporaryAction>();


    public void addTempAction(String actorName, TemporaryActionNames actionName){
        cachedTempAction.add(new TemporaryAction(actorName, actionName));
    }

    public TemporaryAction getTempAction(String playerName){
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActorName().equals(playerName)){
                return tempAction;
            }
        }
        return null;
    }

    public TemporaryActionNames getPlayerActionName(String playerName){
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActorName().equals(playerName)){
                return tempAction.getActionName();
            }
        }
        return null;
    }

    public void removePlayerAction(String playerName){
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActorName().equals(playerName)){
                cachedTempAction.remove(tempAction);
                return;
            }
        }
    }

    public void removePlayerActionName(String playerName, TemporaryActionNames actionName){
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActorName().equals(playerName)){
                if(tempAction.getActionName().equals(actionName)){
                    cachedTempAction.remove(tempAction);
                    return;
                }
            }
        }
    }

    public boolean playerHasAction(String playerName){
        if (cachedTempAction.isEmpty()) return false;
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActorName().equals(playerName)){
                return true;
            }
        }
        return false;
    }

    public boolean playerHasActionName(String playerName, TemporaryActionNames actionName){
        if (cachedTempAction.isEmpty()) return false;
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActorName().equals(playerName) && tempAction.getActionName().equals(actionName)){
                return true;
            }
        }
        return false;
    }

    public boolean containsActionName(TemporaryActionNames actionName){
        if (cachedTempAction.isEmpty()) return false;
        for(TemporaryAction tempAction : cachedTempAction){
            if(tempAction.getActionName().equals(actionName)){
                return true;
            }
        }
        return false;
    }
}
