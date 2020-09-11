package controllers.follows;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowsDestroyServlet
 */
@WebServlet("/follows/destroy")
public class FollowsDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if( _token != null && _token.equals(request.getSession().getId())){

            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));

            Follow f = em.createNamedQuery("getAnExistingFollow", Follow.class)
                        .setParameter("follower", (Employee)request.getSession().getAttribute("login_employee"))
                        .setParameter("followee", r.getEmployee())
                        .getSingleResult();

            em.getTransaction().begin();
            em.remove(f);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", r.getEmployee().getName()+"さんのフォローを解除しました");
            response.sendRedirect(request.getContextPath() + "/reports/index");

        }
    }

}
