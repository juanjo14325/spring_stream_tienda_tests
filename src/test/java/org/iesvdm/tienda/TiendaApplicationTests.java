package org.iesvdm.tienda;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Arrays;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.DynamicTest.stream;


@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;
	
	@Autowired
	ProductoRepository prodRepo;

	@Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();
		
		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}
	
	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});
				
	}

	
	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();
		
		var listNomPrec = listProds.stream()
		.map(s -> "Nombre: " + s.getNombre() + " Precio: " + s.getPrecio())
		.toList();
		listNomPrec.forEach( x-> System.out.println(x));

		Assertions.assertEquals(11, listNomPrec.size());
		Assertions.assertTrue(listNomPrec.contains("Nombre: Disco duro SATA3 1TB Precio: 86.99"));


	}
	
	
	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
	 */
	@Test
	void test2() {
		var listProds = prodRepo.findAll();

		var listPrecio= listProds.stream()
			// .map(p -> p.getPrecio()*1.08)
			// .map(prec -> BigDecimal.valueOf(prec).setScale(2, RoundingMode.HALF_UP))
			// .map(prec -> prec + "$")
			// .toList();
			.map(prod -> prod.getNombre()
					 + " con precio: "
					  + BigDecimal.valueOf(prod.getPrecio()*1.08)
						.setScale(2,RoundingMode.HALF_UP)+ "$")
						.toList();

		listPrecio.forEach( s -> System.out.println(s));
		
		Assertions.assertEquals(11, listPrecio.size());

		Assertions.assertTrue(listPrecio.contains("Impresora HP Laserjet Pro M26nw con precio: 194.40$"));
	}
	
	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();
		
		List <String> listResultado = listProds.stream()
					.map( p -> p.getNombre().toUpperCase() + "precio: " + p.getPrecio()) 
					.toList();

		listResultado.forEach(str -> System.out.println(str));
		
		Assertions.assertEquals(11, listResultado.size());
	}
	
	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();
		
		listFabs.stream()
				.map( n -> n.getNombre() + " " + n.getNombre().substring(0, 2))
				.toList();

		listFabs.forEach(s -> System.out.println(s));
		
	}
	
	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();
        List<Integer> listCod = listFabs.stream()
                .filter(fabricante -> fabricante.getProductos() != null
                && !fabricante.getProductos().isEmpty())
                .map(c -> c.getCodigo())
                .toList();
        listCod.forEach(c -> System.out.println(c));

	}
	
	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();

        var listOrd = listFabs.stream()
                .sorted(comparing((Fabricante f) -> f.getNombre(),reverseOrder()))
                .map(f -> f.getNombre())
                .toList();
        listOrd.forEach(f -> System.out.println(f));
	}
	
	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de
     * forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();

        var listAscDesc = listProds.stream()
                .sorted(comparing((Producto p) -> p.getNombre())
                .thenComparing((Producto p) -> p.getPrecio(),reverseOrder()))
                .map(p-> p.getNombre()+ " " + p.getPrecio() )
                .toList();
        listAscDesc.forEach(System.out::println);
        Assertions.assertEquals(11,listAscDesc.size());

	}
	
	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();
        var list5Fabs = listFabs.stream()
                .limit(5)
                .map(Fabricante::getNombre)
                .toList();

        list5Fabs.forEach(System.out::println);

	}
	
	/**
	 * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();
        var list2Fabs = listFabs.stream()
                .limit(5)
                .skip(3)
                .map(f -> f.getNombre())
                .toList();
        list2Fabs.forEach(System.out::println);




	}
	
	/**
	 * 10. Lista el nombre y el precio del producto más barato
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();
        var listMasBarato = listProds.stream()
                .sorted(comparing((Producto p) -> p.getPrecio() ))
                .map(p -> p.getNombre() +" "+ p.getPrecio())
                .limit(1)
                .toList();
        listMasBarato.forEach(System.out::println);
	}
	
	/**
	 * 11. Lista el nombre y el precio del producto más caro
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();
        var listMasCaro = listProds.stream()
                .sorted(comparing((Producto p) -> p.getPrecio(),reverseOrder()))
                .map(p -> p.getNombre() +" "+ p.getPrecio())
                .limit(1)
                .toList();
        listMasCaro.forEach(System.out::println);
		/* Optional <Producto> prodOpt = listProds.stream()
		 *			.sorted(
		 *				comparing(producto -> producto.getPrecio(), reverseOrder())	 								
		 *			).findFirst();
			if(prodOpt.isPresent()){
			Producto prod = prodOpt.get();
			System.out.println(prod.getNombre() + " " + prod.getPrecio());

			}
			prodOpt.ifPresent(x -> System.out.println(x.getNombre() + " " + x.getPrecio()))
			prodOpt.ifPresentOrElse(x -> System.out.println(x.getNombre() + " " + x.getPrecio())
			,() -> System.out.println("Sin resultado"))

		 */

	}
	
	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 * 
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();
		var listNomP = listProds.stream()
                .filter(p-> p.getFabricante().getCodigo()==2)
                .map(p -> p.getNombre())
                .toList();
        listNomP.forEach(System.out::println);
	}
	
	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();
        var listMenor = listProds.stream()
                .filter(p -> p.getPrecio() <= 120)
                .map(p -> p.getNombre())
                .toList();
        listMenor.forEach(System.out::println);

	}
	
	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();
        var listMayor = listProds.stream()
                .filter(p -> p.getPrecio() >= 400)
                .map(p -> p.getNombre())
                .toList();
        listMayor.forEach(System.out::println);
	}
	
	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€. 
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();

		var listRango = listProds.stream()
						.filter(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300)
						.map(p -> p.getNombre())
						.toList();
		listRango.forEach(System.out::println);

	}
	
	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();
		var listMayor200 = listProds.stream()
							.filter(p -> p.getPrecio() > 200 && p.getFabricante().getCodigo() == 6)
							.map(p -> p.getNombre())
							.toList();
		listMayor200.forEach(System.out::println);
	}
	
	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();
		var listProdsCod = listProds.stream()
							.filter( p -> p.getFabricante().getCodigo() == 1 || 
							p.getFabricante().getCodigo() == 3 || 
							p.getFabricante().getCodigo() == 5 )
							.map(Producto::getNombre)
							.toList();
		listProdsCod.forEach(System.out::println);


	}
	
	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();
		
		var listProdsCent = listProds.stream()
							.map(prod -> prod.getNombre()
					 	+ " Precio: "
					  	+ BigDecimal.valueOf((int) (prod.getPrecio()*100 )) + " céntimos")
						.toList();

		listProdsCent.forEach( s -> System.out.println(s));
	}
	
	
	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();

		var listNomS = listFabs.stream()
						.filter( f -> f.getNombre().contains("S"))
						.map(Fabricante::getNombre)
						.toList();
		listNomS.forEach(System.out::println);

	}
	
	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();
		
		var listPortatil = listProds.stream()
							.filter(p -> p.getNombre().contains("Portátil") )
							.map(Producto::getNombre)
							.toList();

		listPortatil.forEach(System.out::println);

	}
	
	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre 
	 * y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();
		var listMonitor = listProds.stream()
							.filter(p -> p.getNombre().contains("Monitor") && p.getPrecio() < 215)
							.map(Producto::getNombre)
							.toList();

		listMonitor.forEach(System.out::println);


	}
	
	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre
	 * (en orden ascendente).
	 */
	@Test
	void test22() {
		var listProds = prodRepo.findAll();
		
		var listProdAscDesc = listProds.stream()
							.filter(p -> p.getPrecio() >= 180)
							.sorted(comparing((Producto p) -> p.getPrecio())
							.thenComparing((Producto p)  -> p.getNombre()))
							.map(p -> p.getNombre() + " " + p.getPrecio())
							.toList();

		listProdAscDesc.forEach(System.out::println);
	}
	
	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante
	 * de todos los productos de la base de datos. 
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();

		var listaCompletaOrd = listProds.stream()
							.sorted(comparing((Producto p) -> p.getNombre()))
							.map( p -> p.getNombre() + " Precio: " + p.getPrecio() +
							" Frabricante: " + p.getFabricante().getNombre())
							.toList();
		
		listaCompletaOrd.forEach(System.out::println);

	}
	
	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();

		var prodMasCaro = listProds.stream()
						  .sorted( comparing(((Producto p)-> p.getPrecio()),reverseOrder()))
						  .limit(1)
						  .map( p -> p.getNombre() + " -> " + p.getPrecio() + " -> " + p.getFabricante().getNombre())
						  .toList();
		prodMasCaro.forEach(System.out::println);

	}
	
	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();

		var listCrucial = listProds.stream()
						.filter( p -> p.getFabricante().getNombre().equals("Crucial") && p.getPrecio() > 200)
						.map( p -> p.getNombre())
						.toList();

		listCrucial.forEach(System.out::println);

	}
	
	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();
		
		var listFabricantes = listProds.stream()
							.filter( p -> p.getFabricante().getNombre().equals("Asus") || 
							p.getFabricante().getNombre().equals("Hewlett-Packard") ||
							p.getFabricante().getNombre().equals("Seagate"))
							.map( p -> p.getNombre())
							.toList();

		listFabricantes.forEach(System.out::println);					
	}
	
	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que 
	 * tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes 
	 * campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

		Producto                Precio             Fabricante
		-----------------------------------------------------
		GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
		Portátil Yoga 520      |452.79            |Lenovo
		Portátil Ideapd 320    |359.64000000000004|Lenovo
		Monitor 27 LED Full HD |199.25190000000003|Asus

	 */		
	@Test
	void test27() {
		var listProds = prodRepo.findAll();

		long longNom = listProds.stream()
					.mapToLong(p -> p.getNombre().length()).max().orElse(0);

		long longPrecio = listProds.stream()
					.mapToLong(p -> BigDecimal.valueOf(p.getPrecio())
					.setScale(2,RoundingMode.HALF_UP).toString().length()).max().orElse(0);

		var listMayor180 = listProds.stream()
						.filter( p -> p.getPrecio() >= 180)
						.sorted(comparing((Producto p)-> p.getPrecio(),reverseOrder())
						.thenComparing((Producto p) -> p.getNombre()))

						.map (p -> p.getNombre()
						+ " ".repeat((int)longNom - p.getNombre().length()));
						//+"|"+ BigDecimal.valueOf(p.getPrecio().setScale(2,RoundingMode.HALF_UP)).collect(joining("\n"));

        long maxLongNombre = listProds.stream().mapToLong(p -> p.getNombre().length()).max().orElse(0);

        String cuerpoTabla = listProds.stream().filter(p -> p.getPrecio() >= 180)
                .sorted(comparing((Producto p) -> p.getPrecio() , reverseOrder())
                        .thenComparing((Producto p) -> p.getNombre()))
                .map( p -> p.getNombre()
                + " ".repeat((int)maxLongNombre - p.getNombre().length())
                + "|"
                + BigDecimal.valueOf(p.getPrecio()).setScale(2,RoundingMode.HALF_UP)
                + "|"
                + p.getFabricante().getNombre()
                ).collect(joining("\n"));



        System.out.println(cuerpoTabla);
	}
	
	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. 
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados. 
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
Fabricante: Asus

            	Productos:
            	Monitor 27 LED Full HD
            	Monitor 24 LED Full HD

Fabricante: Lenovo

            	Productos:
            	Portátil Ideapd 320
            	Portátil Yoga 520

Fabricante: Hewlett-Packard

            	Productos:
            	Impresora HP Deskjet 3720
            	Impresora HP Laserjet Pro M26nw

Fabricante: Samsung

            	Productos:
            	Disco SSD 1 TB

Fabricante: Seagate

            	Productos:
            	Disco duro SATA3 1TB

Fabricante: Crucial

            	Productos:
            	GeForce GTX 1080 Xtreme
            	Memoria RAM DDR4 8GB

Fabricante: Gigabyte

            	Productos:
            	GeForce GTX 1050Ti

Fabricante: Huawei

            	Productos:


Fabricante: Xiaomi

            	Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();
		
		var listFabsConP = listFabs.stream()
                .map(f -> "Fabricante: " + f.getNombre() + "\n\tProductos: \n\t" + f.getProductos()
                        .stream()
                        .map(p -> p.getNombre())
                        .collect(joining("\n\t")))
                        .toList();
        listFabsConP.forEach(System.out::println);
	}
	
	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();

        var listFabsSinP = listFabs.stream()
                .filter( p -> p.getProductos().isEmpty())
                .map(p -> p.getNombre())
                         .toList();
		listFabsSinP.forEach(System.out::println);



	}
	
	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();
		
		var listNumTotal = listProds.stream()
						.map(p -> p.getNombre())
						.count();
		System.out.println(listNumTotal);
		Assertions.assertEquals(11, listNumTotal);

	}

	
	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();

		var numFabsCon = listProds.stream()
					.map(p -> p.getFabricante().getCodigo())
					.distinct()
					.count();

		System.out.println(numFabsCon);
		Assertions.assertEquals(7, numFabsCon);

	}
	
	/**
	 * 32. Calcula la media del precio de todos los productos
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();

		double mediaProductos = listProds.stream().mapToDouble(p -> p.getPrecio())
                .average()
                .orElse(0.0);

        System.out.println("Media precio de todos los productos: " + mediaProductos);
        Assertions.assertEquals(271.7236363636364, mediaProductos);

    }
	
	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();

        double prodMasBarato = listProds.stream()
                            .mapToDouble(p -> p.getPrecio())
                            .min()
                            .orElse(0.0);
        System.out.println("Producto mas barato: " + prodMasBarato +"€");
        Assertions.assertEquals(59.99, prodMasBarato);
	}
	
	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();

        double suma = listProds.stream()
                    .mapToDouble(p -> p.getPrecio())
                    .sum();

        System.out.println("Suma de todos los precios: " + suma + "€");
        Assertions.assertEquals(2988.96, suma);

    }
	
	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();

        var prodAsus = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Asus"))
                .count();
         System.out.println("Productos totales: " + prodAsus);
        Assertions.assertEquals(2, prodAsus);

    }
	
	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();

        var mediaAsus = listProds.stream()
                .filter(p -> p.getFabricante().getNombre().equals("Asus"))
                .mapToDouble(p -> p.getPrecio())
                        .average()
                        .orElse(0.0);
        System.out.println("Media de todos los productos de Asus: " + mediaAsus +"€");
        Assertions.assertEquals(223.995, mediaAsus);
	}
	
	
	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. 
	 *  Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();


	}
	
	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes. 
	 * El listado también debe incluir los fabricantes que no tienen ningún producto. 
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. 
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:
	 
     Fabricante     #Productos
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
           Asus              2
         Lenovo              2
Hewlett-Packard              2
        Samsung              1
        Seagate              1
        Crucial              2
       Gigabyte              1
         Huawei              0
         Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. 
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. 
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();

        var listF2oMas = listFabs.stream()
                        .filter(f -> f.getProductos().size() >= 2)
                        .map(f -> f.getNombre())
                        .toList();
        listF2oMas.forEach(System.out::println);
	}
	
	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. 
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();
        record NomFabConteoProds(String nomFab, long contProds){}
        var listadoNombre3 = listFabs.stream()
                .map(f->new NomFabConteoProds(
                        f.getNombre(),
                        f.getProductos()
                                .stream()
                                .filter(p->p.getPrecio()>200)
                                .count())
                )
                .sorted(comparing((a)->a.contProds(),reverseOrder()))
                .toList();

        listadoNombre3.forEach(s-> System.out.println(s));
	}
	
	/**
	 * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
        var mayor1000 = listFabs.stream()
                .filter(fab -> {
                    double suma = fab.getProductos().stream()
                            .mapToDouble(p -> p.getPrecio())
                            .sum();
                    return suma > 1000;
                })
                .map(fab -> fab.getNombre())
                .collect(Collectors.joining("\n"));

        System.out.println(mayor1000);

	}
	
	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		var listFabs = fabRepo.findAll();
        var mayor1000 = listFabs.stream()
                .filter(fab -> {
                    double suma = fab.getProductos().stream()
                            .mapToDouble(p -> p.getPrecio())
                            .sum();
                    return suma > 1000;
                })
                .map(fab -> fab.getNombre())
                .collect(Collectors.joining("\n"));

        System.out.println(mayor1000);



	}
	
	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. 
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. 
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
        record productoMasCaro(String producto, double Precio, String fabricante){}

        var salida = listFabs.stream()
                .map(fab -> {
                    var optionalProdMax = fab.getProductos().stream()
                            .sorted(comparing((Producto x) -> x.getPrecio(), reverseOrder()))
                            .findFirst();
                    if (optionalProdMax.isPresent()) {
                        return optionalProdMax.get().getNombre() + " " +
                                optionalProdMax.get().getPrecio() + " " +
                                fab.getNombre();
                    } else {
                        return fab.getNombre() + " sin productos";
                    }
                })
                .collect(joining("\n"));

        System.out.println(salida);

    }
	
	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */

    @Test
    void test46() {
        var listFabs = fabRepo.findAll();

        var salida = listFabs.stream()
                .sorted(comparing(Fabricante::getNombre))
                .flatMap(fabricante -> {
                    double mediaFab = fabricante.getProductos().stream()
                            .mapToDouble(Producto::getPrecio)
                            .average()
                            .orElse(0.0);

                    return fabricante.getProductos().stream()
                            .filter(p -> p.getPrecio() >= mediaFab)
                            .sorted(comparing(Producto::getPrecio).reversed());
                })
                .map(p -> p.getFabricante().getNombre() + " | " + p.getNombre() + " | " + p.getPrecio())
                .collect(Collectors.joining("\n"));

        System.out.println(salida);
    }

}
