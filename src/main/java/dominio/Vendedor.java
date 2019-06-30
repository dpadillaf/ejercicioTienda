package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    
    private static final long COSTO_PRODUCTO_FRONTERA = 500000;
    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }

    public void generarGarantia( String codigo, String nombreCliente ) {
    	
    	Producto producto = repositorioProducto.obtenerPorCodigo( codigo );
    	
    	if ( producto != null ){ //verifica si existe producto
    		if ( !tieneGarantia( codigo ) ){
	        	
	        	if ( !codContiene3Vocales( codigo ) ){
	        		
	        		GarantiaExtendida garantiaExt;
	        		if ( producto.getPrecio() > COSTO_PRODUCTO_FRONTERA ){
	        			garantiaExt = new GarantiaExtendida ( producto, new Date(), fechaFinGarantia( new Date(), 199 ),
	        					producto.getPrecio() * 0.2, nombreCliente );
	        		}else{
	        			garantiaExt = new GarantiaExtendida ( producto, new Date(), fechaFinGarantia( new Date(), 99 ),
	        					producto.getPrecio() * 0.1, nombreCliente );
	        		}
	        		repositorioGarantia.agregar( garantiaExt );
	        		
	        	}else{
	        		throw new UnsupportedOperationException("Este producto no cuenta con garantía extendida");
	        	}
	        	
	        }else{
	        	throw new UnsupportedOperationException(EL_PRODUCTO_TIENE_GARANTIA);
	        }
    	}else{
    		throw new UnsupportedOperationException( "No existe un producto con el código ingresado" );
    	}
    }

    public boolean tieneGarantia( String codigo ) {
        
    	try{
    		if ( repositorioGarantia.obtener(codigo) != null){
    			return true;
    		}
    	}catch( Exception e ){
    		return false;
    	}
    	
    	return false;
    }
    
    private boolean codContiene3Vocales ( String cadena ){
    	
    	int contador = 0;
    	for( int x=0; x<cadena.length(); x++ ) {
    		  if ( ( cadena.charAt(x)=='A' ) || ( cadena.charAt(x)=='E' ) || ( cadena.charAt(x)=='I' ) 
    				  || ( cadena.charAt(x)=='O' ) || ( cadena.charAt(x)=='U' ) ){
    		    contador++;
    		  }
    		}
    	
    	if ( contador == 3 ){
    		return true;
    	}
    	
    	return false;
    }
    
    private Date fechaFinGarantia ( Date fechaI, int dias ){
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(fechaI);
    	while ( dias != 0 ){ 
    	    calendar.add(Calendar.DAY_OF_YEAR, 1);
    	    if ( calendar.get(Calendar.DAY_OF_WEEK) != 2 ){ //no cuenta los lunes
    	    	dias--;
    	    }
    	}
    	
    	if ( calendar.get(Calendar.DAY_OF_WEEK) == 1 ){ //verifica que no sea domingo
    		calendar.add(Calendar.DAY_OF_YEAR, 1);
    	}
    	return calendar.getTime();
    }

}
