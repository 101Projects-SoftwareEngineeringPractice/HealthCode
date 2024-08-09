package org.software.code.dto;

public class NotificationMessage {
    private String name;
    private String phone;
    private String identity_card;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}