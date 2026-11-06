package domain;

import java.util.List;

public class Partido {
    public Partido(Modalidad modalidad, TipoPartido tipo, Personaje p1, Personaje p2){

    }

    public void actualizar(double dt){

    };       // física, tiempo, reglas y poderes
    public void mover(int jugador, double dx){
        
    };
    public void saltar(int jugador){
        
    };
    public void patear(int jugador){
        
    };
    public void usarPoder(int jugador){
        
    };
    public void pausar(){
        
    };
    public void reanudar(){
        
    };

    public Marcador getMarcador(){
        return null;
    };
    public EstadoPartido getEstado(){
        return null;
    };
    public double getTiempoRestante(){
        return 0.0;
    };
    public ResultadoPartido getResultado(){
        return null;
    };
    public Balon getBalon(){
        return null;
    };
    public List<Jugador> getJugadores(){
        return null;
    };

}
