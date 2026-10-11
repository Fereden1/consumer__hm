package farit.dev.servlet;

import farit.dev.dto.TaxRateResponse;
import farit.dev.repository.PaymentRepository;
import farit.dev.service.TaxRateService;
import farit.dev.service.impl.TaxRateServiceRestImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class TaxServlet extends HttpServlet {

    private TaxRateService taxRateService;
    private PaymentRepository paymentRepository;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taxRateService = new TaxRateServiceRestImpl();
        paymentRepository = new PaymentRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fillPage(req, null);
        req.getRequestDispatcher("/jsp/tax.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String incomeParam = req.getParameter("income");
        String error = validateIncome(incomeParam);
        if (Objects.nonNull(error)) {
            fillPage(req, error);
            req.getRequestDispatcher("/jsp/tax.jsp").forward(req, resp);
            return;
        }
        double income = Double.parseDouble(incomeParam.replace(",", "."));
        try {
            TaxRateResponse rate = taxRateService.getTaxRate();
            double percent = rate.percent();
            double tax = income * percent / 100.0;
            paymentRepository.save(income, percent, tax);
            resp.sendRedirect(req.getContextPath() + "/tax");
        } catch (Exception e) {
            fillPage(req, "Не получилось посчитать налог: " + e.getMessage());
            req.getRequestDispatcher("/jsp/tax.jsp").forward(req, resp);
        }
    }

    private void fillPage(HttpServletRequest req, String error) {
        req.setAttribute("error", error);
        try {
            req.setAttribute("payments", paymentRepository.findAll());
        } catch (Exception e) {
            req.setAttribute("payments", java.util.List.of());
            if (Objects.isNull(error)) {
                req.setAttribute("error", "Нет подключения к БД: " + e.getMessage());
            }
        }
        try {
            TaxRateResponse rate = taxRateService.getTaxRate();
            req.setAttribute("percent", rate.percent());
        } catch (Exception e) {
            req.setAttribute("percent", null);
            if (Objects.isNull(error)) {
                req.setAttribute("error", "Не получилось взять процент с админки: " + e.getMessage());
            }
        }
    }

    private String validateIncome(String incomeParam) {
        if (Objects.isNull(incomeParam) || incomeParam.isBlank()) {
            return "Введите сумму дохода";
        }
        try {
            double income = Double.parseDouble(incomeParam.replace(",", "."));
            if (income <= 0) {
                return "Сумма должна быть больше 0";
            }
            if (income > 1_000_000_000) {
                return "Слишком большая сумма";
            }
        } catch (NumberFormatException e) {
            return "Сумма должна быть числом";
        }
        return null;
    }
}
