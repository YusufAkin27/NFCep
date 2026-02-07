package akin.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter

public class Admin extends User {

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
