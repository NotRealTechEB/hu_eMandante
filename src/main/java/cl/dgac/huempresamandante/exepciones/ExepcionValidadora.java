package cl.dgac.huempresamandante.exepciones;

public class ExepcionValidadora extends RuntimeException{
    public ExepcionValidadora(String mensaje){
        super(mensaje);
    }
}
