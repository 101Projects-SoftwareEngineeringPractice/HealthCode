package org.software.code.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthQRCodeDto {
    @JsonProperty("qrcode_token")
    private String qrcodeToken;
    private int status ;

    public String getQrcodeToken() {
        return qrcodeToken;
    }

    public void setQrcodeToken(String qrcodeToken) {
        this.qrcodeToken = qrcodeToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
