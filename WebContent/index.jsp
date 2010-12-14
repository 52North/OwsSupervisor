<?xml version="1.0" encoding="utf-8"?>
<%@page import="org.n52.owsSupervisor.ICheckResult"%>
<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<jsp:useBean id="supervisor"
	class="org.n52.owsSupervisor.SupervisorBean" scope="application" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OWS Supervisor</title>

<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta http-equiv="Refresh" content="60" />

<link href="styles.css" rel="stylesheet" type="text/css" />

</head>

<body>

<div id="content">

<div id="header">
<div id="headline"><a href="<%=request.getContextPath()%>"
	title="Home"> <span class="title">OWS Supervisor</span><br />
<span class="infotext">OGC Web Service Supervisor Version <%=supervisor.getVersion()%></span></a></div>
<div id="logos"><a href="http://52north.org"
	title="52° North Open Source Initiative"> <img
	src="<%=request.getContextPath()%>/images/logo.gif" height="62"
	alt="52N logo" /></a></div>

</div>



<p>The following list shows all supervised services and their latest
status (maximum of <%=supervisor.getMaximumNumberOfResults()%> elements, please refresh page manually):</p>

<ul>
	<%
		Collection<ICheckResult> results = supervisor.getCheckResults();
		for (ICheckResult current : results) {
	%>
	<li><span class="checkTime"><%=current.getTimeOfCheck()%></span>:
	<span class="checkService"><%=current.getServiceIdentifier()%></span> -
	<span class="<%=current.getType().getStyle()%>"><%=current.getResult()%></span>
	</li>
	<%
		}
	%>
</ul>

<p class="infotext">Developer contact: Daniel N&uuml;st,
daniel.nuest@uni-muenster.de</p>

</div>

<!-- 
<div class="center"><a
	href="http://validator.w3.org/check?uri=referer"> <img
	src="http://www.w3.org/Icons/valid-xhtml11" alt="Valid XHTML 1.1" /> </a>

<a href="http://jigsaw.w3.org/css-validator/check/referer"> <img
	src="http://jigsaw.w3.org/css-validator/images/vcss"
	alt="CSS is valid!" /> </a></div>
 -->

</body>
</html>