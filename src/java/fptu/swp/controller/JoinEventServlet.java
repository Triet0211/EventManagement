/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fptu.swp.controller;

import fptu.swp.entity.event.EventDAO;
import fptu.swp.entity.event.EventDetailDTO;
import fptu.swp.entity.user.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author triet
 */
public class JoinEventServlet extends HttpServlet {

    static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(JoinEventServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        LOGGER.info("Begin JoinEventServlet");

        // declare var
        HttpSession session;
        UserDTO loginUser;
        EventDAO eventDao = new EventDAO();

        // get roadmap
        ServletContext context = request.getServletContext();
        HashMap<String, String> roadmap = (HashMap<String, String>) context.getAttribute("ROADMAP");

        //default URL
        final String INVALID_PAGE_LABEL = context.getInitParameter("INVALID_PAGE_LABEL");
        final String VIEW_EVENTDETAIL_SERVLET = context.getInitParameter("VIEW_EVENTDETAIL_SERVLET");
        String url = INVALID_PAGE_LABEL;

        try {
            session = request.getSession();
            loginUser = (UserDTO) session.getAttribute("USER");
            int studentId = loginUser.getId();
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            boolean isJoining = Boolean.parseBoolean(request.getParameter("isJoining"));
            EventDetailDTO detail = eventDao.getEventDetail(eventId);
            if (detail != null) {
                if (detail.getStatusId() == 1) {
                    if (isJoining) {     //Joining (have info in DB) => Unjoin
                        if (eventDao.setJoiningStatus(eventId, studentId, false)) {
                            url = VIEW_EVENTDETAIL_SERVLET + "?eventId=" + eventId;
                        }
                    } else {
                        if (eventDao.checkExistenceAndOrInsertStudentInEvent(eventId, studentId)) {
                            if (eventDao.setJoiningStatus(eventId, studentId, true)) {
                                url = VIEW_EVENTDETAIL_SERVLET + "?eventId=" + eventId;
                            }
                        }
                    }
                }else{
                    request.getSession(true).setAttribute("errorMessage", "Sự kiện này không còn nhận đăng ký tham gia!");
                }
            }else{
                request.getSession(true).setAttribute("errorMessage", "Không tìm thấy sự kiện!");
            }

        } catch (Exception e) {
            LOGGER.error(e);
            request.getSession(true).setAttribute("errorMessage", "Something went wrong!");
        } finally {
            response.sendRedirect(url);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
