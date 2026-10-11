package farit.dev.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import farit.dev.dto.TaxRateResponse;
import farit.dev.service.TaxRateService;
import farit.dev.service.impl.TaxRateServiceRestImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TaxRateApiServlet extends HttpServlet {

    private TaxRateService taxRateService;
    private ObjectMapper objectMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        taxRateService = new TaxRateServiceRestImpl();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            TaxRateResponse rate = taxRateService.getTaxRate();
            resp.getWriter().write(objectMapper.writeValueAsString(rate));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
