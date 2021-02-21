package net.thumbtack.hospital.dtoresponse.other;

public class EmptyDtoResponse {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}