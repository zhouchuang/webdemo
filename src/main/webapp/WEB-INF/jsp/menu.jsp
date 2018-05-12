<%--
  Created by IntelliJ IDEA.
  User: zhouchuang
  Date: 2018/1/20
  Time: 17:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"  %>
<html>
<head>
    <title>Title</title>
    <style>
        .panel-group{max-height:770px;overflow: auto;}
        .leftMenu{margin:10px;margin-top:5px;}
        .leftMenu .panel-heading{font-size:14px;padding-left:20px;height:36px;line-height:36px;color:white;position:relative;cursor:pointer;}/*转成手形图标*/
        .leftMenu .panel-heading span{position:absolute;right:10px;top:12px;}
        .leftMenu .menu-item-left{padding: 2px; background: transparent; border:1px solid transparent;border-radius: 6px;}
        .leftMenu .menu-item-left:hover{background:#C4E3F3;border:1px solid #1E90FF;}
    </style>
</head>
<body>
<div class="panel-group table-responsive" role="tablist" id="menuPanel">
    <div class="panel panel-primary leftMenu">
        <c:choose>
            <c:when test="${not empty sessionScope.get('menus')}">
                <c:forEach items="${sessionScope.get('menus')}" var="one">
                    <div class="panel-heading" id="collapseListGroupHeading1" data-toggle="collapse" data-target="#collapseListGroup1" role="tab" >
                        <h4 class="panel-title">
                                <c:if test="${not empty one.url}">
                                    <a href="javascript:void(0);" data-url="${one.url}">${one.name}</a>
                                </c:if>
                                <c:if test="${empty one.url}">
                                    ${one.name}
                                </c:if>
                                <span class="glyphicon glyphicon-chevron-up right"></span>
                        </h4>
                    </div>
                    <c:choose>
                        <c:when test="${not empty one.getMenuList()}">
                            <div id="collapseListGroup1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="collapseListGroupHeading1">
                                <div class="list-group">
                                    <c:forEach items="${one.getMenuList()}" var="two">
                                        <a href="javascript:void(0);" data-url="${two.url}" class="list-group-item">${two.name}</a>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:when>
                    </c:choose>
                </c:forEach>
            </c:when>
        </c:choose>
    </div>
</div>

<script>
    $(function(){
        $(".panel-heading").click(function(e){
            /*切换折叠指示图标*/
            $(this).find("span").toggleClass("glyphicon-chevron-down");
            $(this).find("span").toggleClass("glyphicon-chevron-up");
        });
    });
</script>
</body>
</html>
