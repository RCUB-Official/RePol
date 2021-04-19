package policygenerator.form;

public final class Trigger {

    public enum Operation {
        SET,
        CLEAR,
        REMOVE,
        RESET
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
            case RESET:
                expression = "reset(" + targetId + ")";
                break;
        }
        return expression;
    }
}
