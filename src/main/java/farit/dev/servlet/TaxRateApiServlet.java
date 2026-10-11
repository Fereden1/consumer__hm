package farit.dev.servlet;

import farit.dev.dto.TaxRateResponse;
import farit.dev.service.TaxRateService;
import farit.dev.service.impl.TaxRateServiceRestImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/tax-rate")
public class TaxRateApiServlet extends HttpServlet {

    private TaxRateService taxRateService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        taxRateService = new TaxRateServiceRestImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            TaxRateResponse rate = taxRateService.getTaxRate();
            resp.getWriter().write("{\"percent\":" + rate.percent() + "}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            String message = e.getMessage() == null ? "error" : e.getMessage().replace("\"", "'");
            resp.getWriter().write("{\"error\":\"" + message + "\"}");
        }
    }
}
