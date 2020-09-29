package co.edu.funlam.sistemainvestigativo.exceptions.access;

public class ApplicationException extends Exception{
    private boolean supressStackTrace = false;

    public ApplicationException(String message, boolean supressStackTrace){
        super(message, null, supressStackTrace, !supressStackTrace);
        this.supressStackTrace = supressStackTrace;
    }

    @Override
    public String toString(){
        if(supressStackTrace){
            return getLocalizedMessage();
        }else{
            return super.toString();
        }
    }
}
