package ahorcado;
  
import static ahorcado.Estados.*;
import ahorcado.Servidor.ServidorHilo;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
 

enum Estados
{
    INICIAL, LETRA, SALIR
}

public class Protocolo {
 
    private static final int erroresMaximos = 6;
    private Estados estado = INICIAL;
    
    private String[] palabras = {"patata","escalera","puerta","cancion","ordenador","movil"};
    
    private ServidorHilo hilo; 

    public Estados getEstado() {
        return estado;
    }

    Protocolo( ServidorHilo hilo) {
        this.hilo = hilo; 
    }
    
    

    public String procesarEntrada(String entrada) {
        
        String salida = "";
        String[] entradaProcesada = entrada.split(":"); 
        
        
        if (estado == INICIAL) {
            if(entradaProcesada[0].equals("J")){
                
                Random r = new Random();
                hilo.setPalabra(palabras[r.nextInt(palabras.length)]); 
                
                salida = "Longitud:"+hilo.getPalabra().length();
                
                hilo.getLetrasUsadas().clear();
                hilo.setNumAciertos(0);
                hilo.setNumErrores(0);
                estado = LETRA;
            
            }else if (entradaProcesada[0].equals("S")){ 
                salida = "S:";
                hilo.setSeguir(false);
                estado = SALIR;
            }
        
        }else if(estado == LETRA) {
            
            if(entradaProcesada[0].equals("L")){ 
                Character letra = entradaProcesada[1].charAt(0); 
                char[] letras = hilo.getPalabra().toCharArray();  
                String letraString = letra.toString(); 
                ArrayList<Integer> numeros = new ArrayList(); 
                
                if(!hilo.comprobarLetra(letra)){
                    
                    hilo.getLetrasUsadas().add(letra);
                    
                    if(hilo.getPalabra().contains(letraString)){
                        System.out.println("Esta");

                        for(int i = 0;i<hilo.getPalabra().length();i++){
                            if(hilo.getPalabra().charAt(i) == letra){
                                numeros.add(i); 
                                hilo.setNumAciertos(hilo.getNumAciertos()+1);
                            }
                        } 
                        if(hilo.getNumAciertos() >= hilo.getPalabra().length()){
                            salida+="G:";
                            salida+=hilo.getNumAciertos();
                        
                        }else{
                            salida+="LC:";

                            for(int numero: numeros){
                                salida+=numero;
                                salida+=":";
                            }
                        
                        } 
                    }else{
                        hilo.setNumErrores(hilo.getNumErrores()+1); 
                        if(hilo.getNumErrores() >= Protocolo.erroresMaximos){
                            salida+="P:";
                            estado = INICIAL;
                        }else{
                            salida+="LI:";
                            salida+=hilo.getNumErrores();
                        }
                    }
                }else{
                    salida="LU:";
                    estado = LETRA;
                }
                
                
                
            }else if (entradaProcesada[0].equals("S")){ 
                salida = "S:";  
                estado = INICIAL;
            }
        } 
        
        return salida;
    }
}
