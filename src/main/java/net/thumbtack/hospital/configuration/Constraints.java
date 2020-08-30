package net.thumbtack.hospital.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("Constraints")
@ConfigurationProperties(prefix = "hospital.constraints")
public class Constraints {
    private int maxNameLength;
    private int minPasswordLength;

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
        Constraints that = (Constraints) o;
        return maxNameLength == that.maxNameLength &&
                minPasswordLength == that.minPasswordLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxNameLength, minPasswordLength);
    }

    @Override
    public String toString() {
        return "Constraints{" +
                "maxNameLength=" + maxNameLength +
                ", minPasswordLength=" + minPasswordLength +
                '}';
    }
}