package net.omny.exceptions;

public class MalformedRequestException extends HttpException{
    
    public MalformedRequestException (String fullContentRequest){
        super("Error processing request : '"+fullContentRequest+"'  [MALFORMED]");
    }

}
