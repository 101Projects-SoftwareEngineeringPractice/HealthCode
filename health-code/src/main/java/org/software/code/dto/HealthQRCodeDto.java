package org.software.code.dto;


public class HealthQRCodeDto {
    private String qrcode_token;
    private int status ;

    public String getQrcode_token() {
        return qrcode_token;
    }

    public void setQrcode_token(String qrcode_token) {
        this.qrcode_token = qrcode_token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
