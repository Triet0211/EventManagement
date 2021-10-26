<%-- 
    Document   : studentNewfeed
    Created on : Sep 30, 2021, 4:02:20 PM
    Author     : triet
--%>

<%@page import="fptu.swp.entity.user.UserDTO"%>
<%@page import="fptu.swp.entity.event.EventCardDTO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang Chủ</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta2/css/all.min.css"
              integrity="sha512-YWzhKL2whUzgiheMoBFwW8CKV4qpHQAEuvilg9FAn5VJUDwKZZxkJNuGM4XkWuk94WCrrwslk8yWNGmY1EduTA=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">

<!--        <script src='bootstrap/js/bootstrap.js'></script>-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script src="resources/sweetalert2.all.min.js"></script> 

        <link rel="stylesheet" type="text/css" href="resources/css/newfeed.css"/>

    </head>

    <body>

        <%@include file="nav_bar.jsp" %>
        
        <%  
            UserDTO logedInUser = (UserDTO) session.getAttribute("USER");
            String userRoleName = logedInUser.getRoleName();
        %>
        
        <div class="d-flex">
            <section id="filter_bar">
                <div id="decorating_text">
                    Discover the event world
                </div>
               
                <div id="filter_button_container">

                    <div class="filter_button">
                        <button class="chosen_button" onclick="showAllEvents(this)">Tất cả</button>
                    </div>

                <% 
                    if (userRoleName.equals("STUDENT")) {
                %>
                        <div class="filter_button">
                           <button onclick="showTopEvents(this)">Nổi bật</button>
                        </div>

                        <div class="filter_button">
                           <button onclick="showThisWeekEvents(this)">Tuần này</button>
                        </div>

                        <div class="filter_button">
                           <button onclick="showThisMonthEvents(this)">Tháng này</button>
                        </div>

                        <div class="filter_button">
                           <button onclick="showStudentCaredEvents(this)">Đã quan tâm</button>
                        </div>
                <%
                    } else if (userRoleName.equals("CLUB'S LEADER") || userRoleName.equals("DEPARTMENT'S MANAGER")) {
                %>
                        <div class="filter_button">
                           <button onclick="showOrganizerCreatedEvents(this)">Sự kiện của bạn</button>
                        </div>
                <%
                    } else if (userRoleName.equals("LECTURER")) {
                %>
                        <div class="filter_button">
                            <button onclick="showLecturerJoinedEvents(this)">Sự kiện có tham gia</button>
                        </div>
                <%
                    }
                %>
                </div>
            </section>
            
            <% 
                if (userRoleName.equals("CLUB'S LEADER") || userRoleName.equals("DEPARTMENT'S MANAGER")) {
            %>
                    <a id="btn_create_event" href="searchLocation?txtSearch=">
                        <img src="resources/icon/icon_create_new_event.svg" />
                        <button >Tạo sự kiện mới</button>
                    </a>
            <%
                }
            %>
                
        </div>

        <div id="card_container" class="container-fluid">
            <div id="card_container_row" class="row gx-1 gy-4">
                <%
                    List<EventCardDTO> listCard = (List<EventCardDTO>) request.getAttribute("LIST_CARD");
                    if (listCard != null) {
                        for (EventCardDTO card : listCard) {
                %>
                            <div class="col-12 col-md-6 col-lg-4 event_card">
                                <p class="event_infor event_date"><%= card.getDate() %></p>
                                <p class="event_infor event_follow"><%= card.getFollowing() %></p>
                                <p class="event_infor event_join"><%= card.getJoining() %></p>
                                <p class="event_infor special_tag"></p>
                                <div class="item">
                                    <div class="image_and_infor_container">
                                        <img class="event_image" src="data:image/jpg;base64,<%= card.getPoster() %>" alt="">
                                        <div class="infor_container">
                                            <p class="event_date_and_loc"><%= card.getDate()%> - <%= card.getLocation()%></p>
                                            <p class="event_name"><%= card.getName()%></p>
                                            <p class="event_organizer_name"><%= card.getOrganizerName()%></p>
                                        </div>
                                    </div>
                                    <div class="other_infor_container">
                                        <div class="care_infor_container">
                                            <div class="care_infor care_part">
                                                <img src="resources/icon/icon_care_blue.svg" alt="">
                                                <p><%= card.getFollowing()%> lượt quan tâm</p>
                                            </div>
                                            <div class="care_infor join_part">
                                                <img src="resources/icon/icon_join_orange.svg" alt="">
                                                <p><%= card.getJoining()%> bạn sẽ tham gia</p>
                                            </div>
                                        </div>
                                        <form action="viewEventDetail">
                                            <input class ="btnViewDetail" type="submit" value="Chi tiết">
                                            <input type="hidden" name="eventId" value="<%= card.getId()%>">
                                        </form>
                                    </div>
                                </div>
                            </div>
                <%      
                        }
                    }
                %>
            </div>
        </div>

        <script language="javascript">var js_version="1.0"</script>
        <script language="javascript1.1">var js_version="1.1"</script>
        <script language="javascript1.2">var js_version="1.2"</script>
        <script language="javascript1.3">var js_version="1.3"</script>
        <script language="javascript1.4">var js_version="1.4"</script>
        <script language="javascript1.5">var js_version="1.5"</script>
        <script language="javascript1.6">var js_version="1.6"</script>

        <footer class="end">
            <h4>Developed By Aladudu Group</h4>
            <p>COVID-19, 20/9/2021</p>
        </footer>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"
                integrity="sha384-W8fXfP3gkOKtndU4JGtKDvXbO53Wy8SZCQHczT5FMiiqmQfUpWbYdTil/SxwZgAN"
        crossorigin="anonymous"></script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.min.js"
                integrity="sha384-skAcpIdS7UcVUC05LJ9Dxay8AXcDYfBJqt1CJ85S/CFujBsIzCIv+l9liuYLaMQ/"
        crossorigin="anonymous"></script>

        <script type="text/javascript" src="resources/js/newfeed_function.js" >
        </script>
    </body>

</html>