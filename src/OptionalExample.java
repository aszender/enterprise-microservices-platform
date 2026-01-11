package src;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OptionalExample {
    String name;
    void setName(String name) {
        this.name = name;
    }
    Optional<String> getName() {
        System.out.println("Name: " + name);
        return Optional.ofNullable(name);
    }

    public void printName() {
        String name1 = getName().orElse("Default Name");
        System.out.println("Name: " + name1);
    }

    public void demonstrateOptional() {
        setName("Andres");
        printName(); // Should print "Andres"

        setName(null);
        printName(); // Should print "Default Name"
    }

    /* === JAVA TIME API === */
    public void demonstrateTimeAPIs() {
        // LocalDate
        LocalDate today = LocalDate.now();
        System.out.println("LocalDate: " + today);
        
        // LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        System.out.println("LocalDateTime: " + now);
        
        // Instant
        Instant instant = Instant.now();
        System.out.println("Instant: " + instant);
        
        // Timestamp (milliseconds)
        long timestamp = System.currentTimeMillis(); // Current timestamp in milliseconds
        System.out.println("Timestamp: " + timestamp);
    }

}
