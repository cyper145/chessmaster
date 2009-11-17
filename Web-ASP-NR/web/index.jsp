<%--
    Document   : index
    Created on : 12/11/2009, 05:10:40 PM
    Author     : Usuario
--%>
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
                            <br>
                            <table>
                                <tbody>
                                    <tr class="odd">
                                        <td >Ingrese  la cadena <br> de entrada </td>
                                        <td align="center"><input type="text" name="entrada" size="60"></td>
                                    </tr>
                                    <tr class="odd">
                                        <td valign="middle">Ingrese la Gramática<br>a utilizar</td>
                                        <td>
                                            <textarea name="gramatica" cols="45" rows="15"></textarea>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <br>
                            <center><input type="submit" name="btnAnalizar" value="Analizar"></center>
                        </form>
                    </center>

                </div>

                <!-- B.3 SUBCONTENT -->

            </div>

            <!-- C. FOOTER AREA -->

            
        </div>

    </body>
</html>