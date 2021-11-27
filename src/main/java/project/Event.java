package project;

public class Event {
private EventType type;
    private String taskId;
    private String additionInfo;

    public Event(EventType type, String taskId, String additionInfo) {
        this.type = type;
        this.taskId = taskId;
        this.additionInfo = additionInfo;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(String additionInfo) {
        this.additionInfo = additionInfo;
    }

    @Override
    public String toString() {
        return "\nEvent{" +
                "type=" + type +
                ", taskId='" + taskId + '\'' +
                ", additionInfo:" + additionInfo +
                '}';
    }
}
