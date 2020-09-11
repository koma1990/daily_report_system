package contorllers.likes;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class LikesCreateServlet
 */
@WebServlet("/likes/create")
public class LikesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikesCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){

            EntityManager em = DBUtil.createEntityManager();


            //Likeテーブルにデータを登録する処理
            Like l = new Like();
            Report r = em.find(Report.class, Integer.valueOf(request.getParameter("report_id")));

            l.setReport(r);
            l.setEmployee((Employee)request.getSession().getAttribute("login_employee"));

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            l.setCreated_at(currentTime);
            l.setUpdated_at(currentTime);

            //Reportテーブルのlike_countを＋1する処理
            int i = r.getLike_count();
            i++;
            r.setLike_count(i);

            em.getTransaction().begin();
            em.persist(l);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", r.getEmployee().getName()+"さんの日報にいいね!しました");
            response.sendRedirect(request.getContextPath() + "/reports/index");

        }
    }

}
