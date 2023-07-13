package ir.bam.consumer.enumeration;

public enum MessageTypeEnum {
    METHOD_CALL("auditMethodCall"),
    REQUEST_URL("auditRequest"),
    RESPONSE_URL("auditResponse");

    private String messageType;

    MessageTypeEnum(String messageType) {
        this.messageType = messageType;
    }


    public static MessageTypeEnum from(final String value) {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            if (messageTypeEnum.messageType.equalsIgnoreCase(value)) {
                return messageTypeEnum;
            }
        }
        return null;
    }

    public String getMessageType() {
        return messageType;
    }
}
