package akin.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("GARSON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Garson extends User {

    /** Garson müsait mi; garson kendisi günceller. */
    private Boolean available = false;

    /** Bugün çalışıyor mu (izin günü değil mi). Admin false yaparak "çalışmıyor" işaretleyebilir. */
    private Boolean workingToday = true;

    @Override
    public Role getRole() {
        return Role.GARSON;
    }

    @Override
    public Boolean getWorkingToday() {
        return workingToday;
    }

    @Override
    public Boolean getAvailable() {
        return available;
    }
}
