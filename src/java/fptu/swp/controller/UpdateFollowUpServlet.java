/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fptu.swp.controller;

import fptu.swp.entity.event.EventDAO;
import fptu.swp.entity.event.EventDetailDTO;
import fptu.swp.entity.schedule.ScheduleDTO;
import fptu.swp.entity.user.LecturerBriefInfoDTO;
import fptu.swp.entity.user.UserDAO;
import fptu.swp.entity.user.UserDTO;
import fptu.swp.utils.firebaseBinding.firebase4j.demo.FirebaseBindingSingleton;
import fptu.swp.utils.firebaseBinding.firebase4j.error.FirebaseException;
import fptu.swp.utils.firebaseBinding.firebase4j.error.JacksonUtilityException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author triet
 */
public class UpdateFollowUpServlet extends HttpServlet {

    static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(UpdateFollowUpServlet.class);

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        LOGGER.info("Begin FollowUpServlet");
        // declare var
        EventDAO eventDao = new EventDAO();
        UserDAO userDao = new UserDAO();

        // get roadmap
        ServletContext context = request.getServletContext();
        HashMap<String, String> roadmap = (HashMap<String, String>) context.getAttribute("ROADMAP");

        //default url
        final String INVALID_PAGE_LABEL = context.getInitParameter("INVALID_PAGE_LABEL");
        final String INVALID_PAGE_PATH = roadmap.get(INVALID_PAGE_LABEL);
        final String VIEW_EVENTDETAIL_SERVLET = context.getInitParameter("VIEW_EVENTDETAIL_SERVLET");
        final String VIEW_EVENTDETAIL_SERVLET_PATH = roadmap.get(VIEW_EVENTDETAIL_SERVLET);
        String url = INVALID_PAGE_PATH;

        try {
//            HttpSession session = request.getSession();
//            UserDTO loginUser = (UserDTO) session.getAttribute("USER");
            int eventId = Integer.parseInt(request.getParameter("eventId"));

            int loginUserId = ((UserDTO) request.getSession(false).getAttribute("USER")).getId();
            int organizerIdOfEvent = userDao.getOrganizerIdByEventId(eventId);

            if (loginUserId == organizerIdOfEvent) {
                String followUp = request.getParameter("followUp");

                EventDetailDTO detail = eventDao.getEventDetail(eventId);
                if (detail != null) {
                    if (detail.getStatusId() == 1 || detail.getStatusId() == 2) {
                        detail.setFollowUp(followUp);
                        if (eventDao.updateEventFollowUp(detail)) {
                            String linkToFirebase = "https://react-getting-started-30bc6-default-rtdb.firebaseio.com/";
                            FirebaseBindingSingleton firebase = FirebaseBindingSingleton.getInstance(linkToFirebase);
                            List<Integer> listFollowers = userDao.getFollowersIdByEventId(eventId);
                            for (int studentId : listFollowers) {
                                ScheduleDTO s = new ScheduleDTO();
                                s.setEventId(eventId);
                                s.setEventName(detail.getName());
                                s.setOrganizerAvatar(detail.getOrganizerAvatar());
                                s.setRunningTime(new Date());
                                String message = "Tiến trình sự kiện " + detail.getName() + " đã được cập nhật.";
                                s.setMessage(message);
                                s.setUserId(studentId);
                                System.out.println("Send noti of Update Event follow up with Id: " + eventId + "  is " + firebase.sendNotificationToUserID(s));
                            }
                            List<LecturerBriefInfoDTO> listLec = userDao.getListLecturerBriefInfo(eventId);
                            for (LecturerBriefInfoDTO lec : listLec) {
                                ScheduleDTO s = new ScheduleDTO();
                                s.setEventId(eventId);
                                s.setEventName(detail.getName());
                                s.setOrganizerAvatar(detail.getOrganizerAvatar());
                                s.setRunningTime(new Date());
                                String message = "Tiến trình sự kiện " + detail.getName() + " đã được cập nhật.";
                                s.setMessage(message);
                                s.setUserId(lec.getId());
                                System.out.println("Send noti of Update Event follow up with Id: " + eventId + "  is " + firebase.sendNotificationToUserID(s));
                            }

                            url = VIEW_EVENTDETAIL_SERVLET_PATH;
                        }

                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.error(ex);
        } catch (FirebaseException ex) {
            Logger.getLogger(UpdateFollowUpServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JacksonUtilityException ex) {
            Logger.getLogger(UpdateFollowUpServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            RequestDispatcher dis = request.getRequestDispatcher(url);
            dis.forward(request, response);
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
