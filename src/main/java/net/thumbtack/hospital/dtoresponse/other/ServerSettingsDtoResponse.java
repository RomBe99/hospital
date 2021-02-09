package net.thumbtack.hospital.dtoresponse.other;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerSettingsDtoResponse that = (ServerSettingsDtoResponse) o;
        return maxNameLength == that.maxNameLength &&
                minPasswordLength == that.minPasswordLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxNameLength, minPasswordLength);
    }

    @Override
    public String toString() {
        return "ServerSettingsDtoResponse{" +
                "maxNameLength=" + maxNameLength +
                ", minPasswordLength=" + minPasswordLength +
                '}';
    }
}