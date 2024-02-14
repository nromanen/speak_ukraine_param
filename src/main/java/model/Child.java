package model;

import java.time.LocalDate;
import java.util.List;

public record Child(Long id, String firstName, String lastName, LocalDate birthDate, List<Club> clubs) {
    public Child(String firstName, String lastName, LocalDate birthDate) {
        this(null, firstName, lastName,birthDate, null);
    }
    public Child(Long id, String firstName, String lastName, LocalDate birthDate) {
        this(id, firstName, lastName,birthDate, null);
    }
}

