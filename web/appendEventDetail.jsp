<%-- 
    Document   : appendEventDetail
    Created on : Oct 3, 2021, 3:34:20 PM
    Author     : admin
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            history.forward();
        </script>
    </head>
    <body>
        <h1>Session Scope - ChosenLocationList</h1>
        <c:forEach var="locationDTO" items="${sessionScope.ChosenLocationList}" >
            <p>Location Id: ${locationDTO.id} - Location Name: ${locationDTO.name}</p>
        </c:forEach>
        --------    
        <h1>Session Scope - ChosenDate</h1>
        <p>ChosenDate : ${sessionScope.ChosenDate}</p>
        --------
        <h1>Session Scope - ChosenTimeRange</h1>
        <p>ChosenTimeRange : ${sessionScope.ChosenTimeRange}</p>
        --------

        ________________________________LIST GIANG VIEN DA CHON____________________________________________________<br>
        <c:forEach var="lec" items="${sessionScope.ChosenLecturerList}" >
            <form action ="removeChosenLecturer">
                <input type="hidden" name="lecturerId" value="${lec.id}">
                <p>Lecturer Id: ${lec.id} - Name : ${lec.name} - avatar : ${lec.avatar} -- <input type="submit" value="remove"/></p>
            </form>
        </c:forEach>
        <br>
        <br>

         ________________________________LIST GIANG VIEN DE SEARCH____________________________________________________<br>
         search lec's name <input type ="text" name ="search" value="${param.search}">
        <c:forEach var="lec" items="${sessionScope.LecturerList}" >
            <form action ="chooseLecturer">
                <input type="hidden" name="lecturerId" value="${lec.id}">
                <p>Lecturer Id: ${lec.id} - Name : ${lec.name} - avatar : ${lec.avatar} -- <input type="submit" value="add"/></p>
            </form>
        </c:forEach>

        <!--        FORM SEARCH TEN GIANG VIEN-->
        <!--        <form action="searchLecturer">
                    
                    <input type="submit" value="Submit" />
                </form>-->


        <h1>Session Scope - LecturerList</h1>
        <!--        FORM DUA DU LIEU VE CHO ReviewEventController-->
        <form action="reviewEvent" method="POST" enctype="multipart/form-data">
            <c:set var = "eventDetail" value = "${sessionScope.EVENT_DETAIL_REVIEW}"/>
            eventName <input type ="text" name ="eventName" value="${eventDetail.name}">
            description <input type ="text" name ="description" value="${eventDetail.description}">  
            File <input type="file" accept=".png .jpg .jpeg" name="fileUp" required="" value=""><br>
            <input type="submit" value="Submit" />
        </form>

    </body>
</html>
