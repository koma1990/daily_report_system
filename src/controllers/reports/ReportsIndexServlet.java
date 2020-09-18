package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int follow_check;
        try {
        follow_check = Integer.parseInt(request.getParameter("follow_check"));
        } catch(Exception e){
            follow_check = 0;
        }

        if (follow_check == 1){

            int page;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch(Exception e){
                page = 1;
            }

            List<Report> reports = em.createNamedQuery("getMyFollowReports", Report.class)
                                     .setParameter("employee", (Employee)request.getSession().getAttribute("login_employee"))
                                     .setFirstResult(15 * (page - 1))
                                     .setMaxResults(15)
                                     .getResultList();

            long reports_count = (long)em.createNamedQuery("getMyFollowReportsCount", Long.class)
                                    .setParameter("employee", (Employee)request.getSession().getAttribute("login_employee"))
                                    .getSingleResult();

            request.setAttribute("reports", reports);
            request.setAttribute("reports_count", reports_count);
            request.setAttribute("follow_check", follow_check);
            request.setAttribute("page", page);

        } else {

            int page;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch(Exception e){
                page = 1;
            }

            follow_check = 0;

            List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
                                 .setFirstResult(15 * (page - 1))
                                 .setMaxResults(15)
                                 .getResultList();

            long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class)
                                     .getSingleResult();

            request.setAttribute("reports", reports);
            request.setAttribute("reports_count", reports_count);
            request.setAttribute("follow_check", follow_check);
            request.setAttribute("page", page);
        }

        em.close();

        if(request.getSession().getAttribute("flush") != null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

}
