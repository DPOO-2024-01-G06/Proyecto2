package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import galeria.Galeria;
import galeria.controller_galeria.ControladorComprador;
import galeria.structurer_inventario.Artista;
import galeria.structurer_inventario.Escultura;
import galeria.structurer_inventario.InventarioGaleria;
import galeria.structurer_inventario.Oferta;
import galeria.structurer_inventario.Pieza;
import galeria.structurer_inventario.Pintura;
import galeria.structurer_inventario.Subasta;
import galeria.structurer_inventario.Venta;
import galeria.structurer_inventario.Video;
import galeria.structurer_usuarios.Administrador;
import galeria.structurer_usuarios.Cajero;
import galeria.structurer_usuarios.Comprador;
import galeria.structurer_usuarios.Externo;
import galeria.structurer_usuarios.Operador;
import galeria.structurer_usuarios.UsuariosGaleria;


public class CompradorTest {
	private Galeria galeria;
	private ControladorComprador contComprador;
	
	@BeforeEach
	public void setUp() {
		galeria = SampleGaleria();
		List<Externo> listaExternos = galeria.getUsuariosGaleria().getExternos();
		Externo comprador1 = null;
		comprador1 = listaExternos.get(0);
		
		contComprador = new ControladorComprador(galeria, comprador1);
		assertNotNull(contComprador);
    }


    @Test
    void testActualizarContraseña() {
    	Comprador comprador = galeria.getUsuariosGaleria().getExternos().get(0).getComprador();
		
        contComprador.actualizarContraseña("nuevaContrasena");
        assertEquals("nuevaContrasena", comprador.getExterno().getContrasena(), "La contaraseña se actualizó correctamente." );
        
    }

    @Test
    void testActualizarCorreo() {
    	Comprador comprador = galeria.getUsuariosGaleria().getExternos().get(0).getComprador();
    	
        contComprador.actualizarCorreo("nuevoCorreo");
        assertEquals("nuevoCorreo", comprador.getExterno().getCorreo(), "El correo se actualizó correctamente." );
    }

    @Test
    void testActualizarCelular() {
    	Comprador comprador = galeria.getUsuariosGaleria().getExternos().get(0).getComprador();
    	
        contComprador.actualizarCelular("nuevoCelular");
        assertEquals("nuevoCelular", comprador.getExterno().getCelular(), "El celular se actualizó correctamente.");
    }


    @Test
    void testOfertar() {
    	int tamano = galeria.getUsuariosGaleria().getOperador().getOfertasPendientes().size();
    	contComprador.ofertar(0, 999.0, "metodoPago");
    	assertEquals(tamano, galeria.getUsuariosGaleria().getOperador().getOfertasPendientes().size(), "La oferta NO se agregó correctamente." );
    	assertEquals((tamano + 1), galeria.getUsuariosGaleria().getOperador().getOfertasPendientes().size(), "La oferta se agregó correctamente." );
    	
    }
    
    @Test
    void testIntentoComprar1() { 
    	Comprador comprador = galeria.getUsuariosGaleria().getExternos().get(0).getComprador();
    	int tamano = galeria.getUsuariosGaleria().getAdministrador().getSuperaronLimite().size();
    	contComprador.intentoComprar(0);
        assertEquals(999, comprador.getValorMaximo(), "No se realizó la compra porque se alcanzó el valor maximo");
        assertEquals(tamano, galeria.getUsuariosGaleria().getAdministrador().getSuperaronLimite().size(), "El tamañó de la lista de los usuarios que excedieron el precio no aumentó");
    }
    
    @Test
    void testIntentoComprar2() {
    	int tamano = galeria.getUsuariosGaleria().getAdministrador().getPendientesAceptar().size();
    	contComprador.intentoComprar(0);
        assertEquals(tamano, galeria.getUsuariosGaleria().getAdministrador().getPendientesAceptar().size(), "El tamañó de la lista de ventas pendientes no aumentó");
    }
    
    public Galeria SampleGaleria() {
		//Crear artistas y asociarles una obra
		Artista michelangelo = new Artista("Michelangelo", new ArrayList<Pieza>());
		Escultura escultura = new Escultura("David","1504-06-08", "Florencia", false, 
											null, (float)517, (float)199,(float)50,(float)5660,"marmol",null, null, michelangelo,
											new HashMap<String, List<String>>(), null);
		michelangelo.addPieza(escultura);
		Artista  munch= new Artista("Edvard Munch", new ArrayList<Pieza>());
		Pintura pintura = new Pintura("The scream", "1893", "Berlin", false, null, (float) 73.5,(float)91,"oleo",
									  null, munch, new HashMap<String, List<String>>(), null);
									munch.addPieza(pintura);
		Artista akerman = new Artista("Chantal Akerman", new ArrayList<Pieza>());
		Video video = new Video("Jeanne Dielman, 23 quai du Commerce, 1080 Bruxelles", "1975-01-01","Bruselas",false,
								null,(float) 201, "DVD","Ejempo",null, akerman, new HashMap<String, List<String>>(),null);
								akerman.addPieza(video);
		List<Artista> artistas= new ArrayList<Artista>();		
		artistas.add(akerman);artistas.add(munch);artistas.add(michelangelo);
		//Crear un externo y asociarle una obra
		Externo externo1 = new Externo("Juan123","0000","Juan Ramirez", "a","b",null,null);
		externo1.crearComprador((float)10000);
		externo1.getPropietario().addPiezaPropiedad(video);
		video.setPropietario(externo1.getPropietario());
		List<Externo> externos = new ArrayList<Externo>();externos.add(externo1);
		//Creacion Galeria
		InventarioGaleria inventarioGaleria = new InventarioGaleria(new HashMap<Integer, Subasta>(), new HashMap<Integer, Subasta>(), new  HashMap<Integer, Venta>(), new  HashMap<Integer, Venta>(), new HashMap<Integer, Pieza>(),artistas);
		Administrador administrador = new Administrador("ADMIN", "0000", "a","b","c",new ArrayList<Venta>(),new ArrayList<Pieza>(), new ArrayList<Comprador>(), new ArrayList<Comprador>());
		administrador.getPiezasPorAgregar().add(video);administrador.getPiezasPorAgregar().add(pintura);administrador.getPiezasPorAgregar().add(escultura); 
		Cajero cajero = new Cajero("CAJERO", "0000", "nombre", "correo@gmail.com", "0000000000", new ArrayList<Venta>());
		Operador operador = new Operador("OPERADOR", "0000", "nombre", "0000000000", "correo@gmail.com", new ArrayList<Oferta>());
		return new Galeria( new UsuariosGaleria(administrador, cajero, operador, externos), 
						inventarioGaleria);
	}
    
}