package akin.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Adds user_type discriminator column to users table if missing (migration from single User to User/Garson/Admin/Mutfak inheritance).
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class UserTypeMigration implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!columnExists("users", "user_type")) {
            log.info("Adding user_type column to users table.");
            jdbcTemplate.execute("ALTER TABLE users ADD COLUMN user_type VARCHAR(31)");
            backfillUserType();
        }
    }

    private boolean columnExists(String table, String column) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND column_name = ?",
                    table, column);
            return !rows.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private void backfillUserType() {
        try {
            if (tableExists("user_roles")) {
                try {
                    jdbcTemplate.execute("""
                        UPDATE users u SET user_type = CAST(ur.role AS VARCHAR)
                        FROM user_roles ur WHERE ur.user_id = u.id AND u.user_type IS NULL
                        """);
                } catch (Exception ignored) {
                    /* role may already be varchar */
                }
            }
            jdbcTemplate.execute("UPDATE users SET user_type = 'GARSON' WHERE user_type IS NULL");
            Integer nullCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE user_type IS NULL", Integer.class);
            if (nullCount != null && nullCount == 0) {
                jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN user_type SET NOT NULL");
            }
        } catch (Exception e) {
            log.warn("Could not backfill user_type (table may be empty): {}", e.getMessage());
        }
    }

    private boolean tableExists(String table) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT 1 FROM information_schema.tables WHERE table_name = ?", table);
            return !rows.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
