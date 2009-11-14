<%-- 
    Document   : resultado
    Created on : 12/11/2009, 05:10:40 PM
    Author     : Usuario
--%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="ASP_NR.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
    <head>
        <!--  Version: Multiflex-3.12 / Overview                    --><!--  Date:    January 20, 2008                             --><!--  Design:  www.1234.info                                --><!--  License: Fully open source without restrictions.      --><!--           Please keep footer credits with the words    --><!--           "Design by 1234.info" Thank you!             -->
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <%@page contentType="text/html" pageEncoding="UTF-8"%>
        <link rel="stylesheet" type="text/css" media="screen,projection,print" href="./css/layout4_setup.css">
        <link rel="stylesheet" type="text/css" media="screen,projection,print" href="./css/layout4_text.css">
        <link rel="icon" type="image/x-icon" href="./img/ajedrez.ico"><title>ASP-NR</title><!-- Global IE fix to avoid layout crash when single word size wider than column width --><!--[if IE]><style type="text/css"> body {word-wrap: break-word;}</style><![endif]--><link href="./css/highlighter.css" type="text/css" rel="stylesheet"><link href="./css/highlighter_002.css" type="text/css" rel="stylesheet">

    </head>
    <body>
        <!-- Main Page Container -->
        <div class="page-container">

            <!-- For alternative headers START PASTE here -->

    <!-- A. HEADER -->
            <div class="header">

                <!-- A.1 HEADER TOP -->
                <div class="header-top">

                    <!-- Sitelogo and sitename -->
                    <a class="sitelogo" href="#" title="Go to Start page"></a>
                    <div class="sitename">
                        <h1><a href="" title="Go to Start page">ASP-NR</a></h1>
                        <h2>Analizador Sintáctico Predictivo - No Recursivo</h2>
                    </div>

                    <!-- Navigation Level 0 -->


        <!-- Navigation Level 1 -->

                </div>

                <!-- A.2 HEADER MIDDLE -->
                <div class="header-middle">

                    <!-- Site message -->
                    <div class="sitemessage">
                        <h1>Facultad Politecnica (UNA)</h1>
                        <h2>Un Trabajo Practico de la materia<br>Diseño de Compiladores<br>Año 2009</h2>

                    </div>
                </div>

                <!-- A.3 HEADER BOTTOM -->
                <div class="header-bottom">

                    <!-- Navigation Level 2 (Drop-down menus) -->

                </div>

                <!-- A.4 HEADER BREADCRUMBS -->

      <!-- Breadcrumbs -->

            </div>

            <!-- For alternative headers END PASTE here -->

    <!-- B. MAIN -->
            <div class="main">

                <!-- B.1 MAIN NAVIGATION -->


      <!-- B.2 MAIN CONTENT -->
                <div>
                    <!-- Pagetitle -->
                    <center>
                        <form name="resolver" action="actionManager" method="post">
                            <center><h2>Resultados</h2></center>
                            <%
        boolean error = Boolean.parseBoolean((String) request.getAttribute("error"));
        if (!error) {
            ASP analizador = (ASP) request.getAttribute("analizador");
            analizador.derivacion.toString();
            StringTokenizer tokens = new StringTokenizer(analizador.derivacion.trim(), "\n");

            if(analizador.solucion){
                out.println("<center><h2>La cadena pertenece al Lenguaje!!!</h2></center>");
                out.println("<br>");
                //Impresion de la derivacion.
                out.println("<center><h3>La derivación obtenida es:</h3></center>");
                out.println("<br>");
                out.println("<table>");
                out.println("<tbody>");
                while (tokens.hasMoreElements()) {
                    out.println("<tr>");
                    out.println("<td>" + tokens.nextToken() + "</td>");
                    out.println("</tr>");
                }
                out.println("</tbody>");
                out.println("</table>");
            }else if(analizador.esAmbiguo){
                out.println("<center><h2>La Gramatica dada es ambigua!!!</h2></center>");
                out.println("<br>");
            }else{
                out.println("<center><h2>La cadena NO pertenece al Lenguaje!!!</h2></center>");
                out.println("<br>");
            }

            //Impresion de conjuntos primero y siguiente.
            Set claves = analizador.no_terminales.keySet();
            Iterator noTerminales = claves.iterator();
            out.println("<br>");
            out.println("<center><h3>Conjuntos Primero y Siguiente</h3></center>");
            out.println("<br>");
            out.println("<center>");
            out.println("<table>");
            out.println("<tbody>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th align=\"center\">No-Terminal<b></th>");
            out.println("<th align=\"center\">Primero<b></th>");
            out.println("<th align=\"center\">Siguiente<b></th>");
            out.println("</tr>");
            out.println("</thead>");
            while (noTerminales.hasNext()) {
                NoTerminal nT = (NoTerminal) analizador.no_terminales.get((String) noTerminales.next());
                out.println("<tr>");
                out.println("<td aling=\"center\"><b>" + nT.getNombre() + "</b></td>");
                out.println("<td>" + nT.getPrimero().toString() + "</td>");
                out.println("<td>" + nT.getSiguiente().toString() + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</center>");
            //Impresion de Tabla ASP.
            out.println("<br>");
            out.println("<center><h3>Tabla ASP</h3></center>");
            out.println("<br>");
            Iterator terminales = analizador.terminales.iterator();
            out.println("<center>");
            out.println("<table>");
            out.println("<tbody>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>&nbsp;</th>");
            while (terminales.hasNext()) {
                String terminal = (String) terminales.next();
                out.println("<th><b>" + terminal + "</b></th>");
            }
            out.println("</tr>");
            out.println("</thead>");
            noTerminales = claves.iterator();
            while (noTerminales.hasNext()) {
                NoTerminal nT = (NoTerminal) analizador.no_terminales.get((String) noTerminales.next());
                out.println("<tr>");
                out.println("<td aling=\"center\"><b>" + nT.getNombre() + "</b></td>");
                terminales = analizador.terminales.iterator();

                while(terminales.hasNext()){
                    Vector prod = (Vector)nT.getFilaTabla().get((String)terminales.next());
                    if(prod !=null && !prod.isEmpty()){
                        Iterator iter=prod.iterator();
                        out.println("<td>");
                        while(iter.hasNext())
                        {
                            String pd=(String)iter.next();
                            out.println(nT.getNombre()+"->"+pd + "<br>");
                        }
                        out.println("</td>");
                    }else{
                        out.println("<td>&nbsp;</td>");
                    }
                }
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</center>");
        }else{
            out.println("<center><h2>ERROR</h2></center>");
            out.println("<center><h3>" + error + "</h3></center>");
            //Impresion del error.
            Exception ex = (Exception) request.getAttribute("exception");
            out.println("<br>");
            out.println("<table>");
            out.println("<tbody>");
            out.println("<tr>");
            out.println("<td>" + ex.toString() + "</td>");
            out.println("</tr>");
            out.println("</tbody>");
            out.println("</table>");
        }
                            %>

                        </form>
                    </center>

                </div>

                <!-- B.3 SUBCONTENT -->

            </div>

            <!-- C. FOOTER AREA -->


        </div>

    </body>
</html>