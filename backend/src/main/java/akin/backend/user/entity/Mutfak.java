package akin.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("MUTFAK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mutfak extends User {

    /** Bugün çalışıyor mu (izin günü değil mi). Admin false yaparak "çalışmıyor" işaretleyebilir. */
    private Boolean workingToday = true;

    @Override
    public Role getRole() {
        return Role.MUTFAK;
    }

    @Override
    public Boolean getWorkingToday() {
        return workingToday;
    }
}
