package net.thumbtack.hospital.dtoresponse.other;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;

public class ServerSettingsDtoResponse extends SettingsDtoResponse {
    private int maxNameLength;
    private int minPasswordLength;

    public ServerSettingsDtoResponse() {
    }

    public ServerSettingsDtoResponse(int maxNameLength, int minPasswordLength) {
        setMaxNameLength(maxNameLength);
        setMinPasswordLength(minPasswordLength);
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }
}