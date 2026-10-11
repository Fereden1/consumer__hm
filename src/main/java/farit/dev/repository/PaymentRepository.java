package farit.dev.repository;

import farit.dev.db.Database;
import farit.dev.dto.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    public void save(double income, double percent, double tax) {
        String sql = "insert into payments (income, percent, tax) values (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, income);
            ps.setDouble(2, percent);
            ps.setDouble(3, tax);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("не получилось сохранить оплату", e);
        }
    }

    public List<Payment> findAll() {
        String sql = "select id, income, percent, tax, created_at from payments order by id desc";
        List<Payment> payments = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getLong("id"),
                        rs.getDouble("income"),
                        rs.getDouble("percent"),
                        rs.getDouble("tax"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не получилось прочитать историю оплат", e);
        }
        return payments;
    }
}
