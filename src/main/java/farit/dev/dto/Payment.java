package farit.dev.dto;

import java.sql.Timestamp;

public record Payment(
        Long id,
        double income,
        double percent,
        double tax,
        Timestamp createdAt
) {
}
