/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ASP_NR;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Es el servlet utilizado para gestionar las solicitudes hechas desde
 * el navegador web.
 * @author Manuel Avalos.
 * @author Arturo Ferreira.
 */
public class ActionManager extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ActionManager</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ActionManager at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        this.doPost(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String entrada = (String)request.getParameter("entrada");
        String gramatica = (String)request.getParameter("gramatica");
        ASP analizador = new ASP();
        StringTokenizer st = new StringTokenizer( gramatica,"\r\n" );
        Vector grammar =  new Vector();
        while(st.hasMoreElements()){
            grammar.add(st.nextToken());
        }
        try {
            entrada = entrada.trim() + " $";
            analizador = this.analizar(entrada.trim(), grammar);
        } catch (Exception e) {
            request.setAttribute("error", "true");
            request.setAttribute("exception", e);
        }
        request.setAttribute("analizador", analizador);

        RequestDispatcher dispatcher = request.getRequestDispatcher("resultado.jsp");
        dispatcher.forward(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public ASP analizar(String entrada, Vector gramatica) throws IOException, Exception {
        // TODO code application logic here

            ASP analizador = new ASP();
            analizador.setTablaHash(gramatica);
            analizador.CalcularPrimeros();
            analizador.CalcularSiguientes();            
            analizador.hacerTablaASP();
            if(!analizador.esAmbiguo){
                analizador.analizar(entrada);
            }
            return analizador;

    }
}
