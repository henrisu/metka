<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="tabs tab_file_management">
    <jsp:include page="../../../inc/datatableContainer.jsp">
        <jsp:param name="field" value="files" />
        <jsp:param name="generalHandler" value="false" />
    </jsp:include>
</div>