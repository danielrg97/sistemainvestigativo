package co.edu.funlam.sistemainvestigativo.utils;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    /**
     * Metodo para obtener todo el url de la peticion, incluido el querystring si está agregado
     * @param request
     * @return
     */
    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    /**
     * Obtiene el endpoint que se está llamando
     * @param request
     * @return
     */
    public static String getEndpoint(HttpServletRequest request){
        String[] requestURL = request.getRequestURL().toString().split("/");
        return "/"+requestURL[requestURL.length-1];
    }
}
