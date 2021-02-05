/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policygenerator.forms;

/**
 *
 * @author vasilije
 */
public final class Trigger {

    public enum Operation {
        SET,
        CLEAR,
        REMOVE
    }

    private final String conditionId;
    private final String targetId;
    private final Operation operation;
    private final String value;

    public Trigger(String conditionId, String targetId, Operation operation, String value) {
        this.conditionId = conditionId;
        this.targetId = targetId;
        this.operation = operation;
        this.value = value;
    }

    public String getConditionId() {
        return conditionId;
    }

    public String getTargetId() {
        return targetId;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        String expression = null;
        switch (operation) {
            case SET:
                expression = targetId + " = \"" + value + "\"";
                break;
            case CLEAR:
                expression = "clear(" + targetId + ")";
                break;
            case REMOVE:
                expression = "remove(" + targetId + ", \"" + value + "\")";
                break;
        }
        return expression;
    }
}
