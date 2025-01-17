/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fptu.swp.controller;

import fptu.swp.entity.user.UserDAO;
import fptu.swp.entity.user.UserDTO;
import fptu.swp.entity.user.UserError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author triet
 */

public class UpdateProfileServlet extends HttpServlet {
    
    static final Logger LOGGER = Logger.getLogger(UpdateProfileServlet.class);
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
        LOGGER.info("Begin UpdateProfileServlet");
        //get roadmap
        ServletContext context = request.getServletContext();
        HashMap<String, String> roadmap = (HashMap<String, String>) context.getAttribute("ROADMAP");
        //default url
        String INVALID_PAGE_LABEL = context.getInitParameter("INVALID_PAGE_LABEL");
        String UPDATE_PAGE_LABEL = context.getInitParameter("UPDATE_PAGE_LABEL");

        String VIEW_NEWFEED_SERVLET = context.getInitParameter("VIEW_NEWFEED_SERVLET");
        
        String UPDATE_PAGE_PATH = roadmap.get(UPDATE_PAGE_LABEL);
        String VIEW_NEWFEED_SERVLET_PATH = roadmap.get(VIEW_NEWFEED_SERVLET);
        
        String INVALID_PAGE_PATH = roadmap.get(INVALID_PAGE_LABEL);
        String url = INVALID_PAGE_PATH;
        
        try {
            HttpSession session = request.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("USER");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phoneNum = request.getParameter("phoneNum");
            String description = request.getParameter("description");
            UserError userError = new UserError("", "", "","");
            if (description == null){
                description = "";
            }

            boolean check = true;
            if (name.length() > 50 || name.length() < 10) {
                userError.setNameError("Tên của bạn phải từ 10 đến 50 kí tự!");
                check = false;
            }
            if (phoneNum.length() != 10 || !phoneNum.matches("[0-9]{10}")) {
                if(!"".equals(phoneNum) && phoneNum != null ){
                    userError.setPhoneNumError("Số điện thoại phải bao gồm 10 kí tự số hoặc để trống!");
                    check = false;
                }
            }
            if (address.length() > 100 || address.length() < 20) {
                if(!"".equals(address) && address != null){
                    userError.setAddressError("Địa chỉ của bạn phải từ 20 đến 100 kí tự hoặc để trống!");
                    check = false;
                }
            }
            if (description.length() > 100 || description.length() < 10) {
                if(!"".equals(description) && description != null){
                    userError.setDescriptionError("Mô tả của bạn phải từ 10 đến 100 kí tự hoặc để trống!");
                    check = false;
                }
            }
            if (check) {
                UserDAO dao = new UserDAO();
                UserDTO user = loginUser;
                user.setAddress(address);
                user.setPhoneNum(phoneNum);
                user.setName(name);
                user.setDescription(description);
                boolean checkUpdate = dao.updateUser(user);
                if (checkUpdate) {
                    url = VIEW_NEWFEED_SERVLET_PATH;
                    session.setAttribute("USER", user);
                    LOGGER.info("UPDATE SUCCESFULLY. NEW INFO: "+ user +". FORWARD TO VIEW NEW FEED SERVLET");
                }
            } else {
                LOGGER.info("Invalidate input!!!" + userError);
                request.setAttribute("USER_ERROR", userError);
                url = UPDATE_PAGE_PATH;
            }

        } catch (Exception e) {
            LOGGER.error(e);
            request.getSession(true).setAttribute("errorMessage", "Something went wrong!!!");
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
