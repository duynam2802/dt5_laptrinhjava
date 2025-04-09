package ut.edu.childgrowth.dtos;

import java.util.Map;

public class UpdateUserResponse {
    private String message;
    private Map<String, Object> changedFields;

    public UpdateUserResponse(String message, Map<String, Object> changedFields) {
        this.message = message;
        this.changedFields = changedFields;
    }

    // Getters & setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(Map<String, Object> changedFields) {
        this.changedFields = changedFields;
    }
}
