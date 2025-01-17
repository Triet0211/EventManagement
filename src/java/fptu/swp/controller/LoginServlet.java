/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fptu.swp.controller;

import fptu.swp.controller.accessgoogle.GooglePojo;
import fptu.swp.controller.accessgoogle.GoogleUtils;
import fptu.swp.entity.user.UserDAO;
import fptu.swp.entity.user.UserDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
public class LoginServlet extends HttpServlet {
    
    static final Logger LOGGER = Logger.getLogger(LoginServlet.class);

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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        LOGGER.info("Begin LoginServlet");
        //declare var

        //get roadmap
        ServletContext context = request.getServletContext();
        HashMap<String, String> roadmap = (HashMap<String, String>) context.getAttribute("ROADMAP");

        //default url
        final String INVALID_PAGE_LABEL = context.getInitParameter("INVALID_PAGE_LABEL");
        final String VIEW_NEWFEED_SERVLET = context.getInitParameter("VIEW_NEWFEED_SERVLET");
        final String MANAGE_BY_ADMIN_SERVLET = context.getInitParameter("MANAGE_BY_ADMIN_SERVLET");
        final String INVALID_PAGE_PATH = roadmap.get(INVALID_PAGE_LABEL);
        final String VIEW_NEWFEED_SERVLET_PATH = roadmap.get(VIEW_NEWFEED_SERVLET);
        final String MANAGE_BY_ADMIN_SERVLET_PATH = roadmap.get(MANAGE_BY_ADMIN_SERVLET);
        String url = INVALID_PAGE_PATH;

        //parameter
        String code = request.getParameter("code");        
        try {
            if (code == null || code.isEmpty()) {
                LOGGER.error("Account not found!!!");
            } else {
                String accessToken = GoogleUtils.getToken(code);
                GooglePojo googlePojo = GoogleUtils.getUserInfo(accessToken);
                if (googlePojo != null) {
                    LOGGER.info("Login Success");
                }
                System.out.println("code: " + code);
                System.out.println("access Token: " + accessToken);
                request.setAttribute("id", googlePojo.getId());
                String email = googlePojo.getEmail();
                UserDAO dao = new UserDAO();
                UserDTO user = dao.login(googlePojo);
                if (user != null) {
                    HttpSession session = request.getSession();
                    LOGGER.info("USER: " + user);
                    session.setAttribute("USER", user);
                    if ("ADMIN".equals(user.getRoleName())) {
                        url = MANAGE_BY_ADMIN_SERVLET_PATH + "?management=organizer";
                        String authorizing = (String) session.getAttribute("AUTHORIZING_SENDING_EMAIL");
                        if(authorizing != null){
                            if("true".equals(authorizing)){
                                session.setAttribute("ACCESS_TOKEN", accessToken);
                                session.setAttribute("EMAIL_NAME", googlePojo.getName());
                            }
                        }
                    } else {
                        url = VIEW_NEWFEED_SERVLET_PATH;
                    }
                    
                }else{
                    request.getSession(true).setAttribute("errorMessage", "Tài khoản của bạn không có trong hệ thống hoặc đã bị hủy kích hoạt.");
                }
            }
        } catch (Exception ex) { 
            LOGGER.fatal("LOGIN FAILED", ex);
        } finally {
            RequestDispatcher dis = request.getRequestDispatcher(url);
            dis.forward(request, response);
            //response.sendRedirect(url);
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
