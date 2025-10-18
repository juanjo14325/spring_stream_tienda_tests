-- 1. Lista los nombres y los precios de todos los productos de la tabla producto

SELECT nombre, precio FROM producto;

-- 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .

SELECT nombre, ROUND(precio*1.08, 2) FROM producto;

-- 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.

SELECT UPPER(nombre), precio FROM producto;

-- 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.

SELECT nombre, UPPER(SUBSTR(nombre,1,2)) FROM fabricante;

-- 5. Lista el código de los fabricantes que tienen productos.

SELECT DISTINCT F.codigo FROM fabricante F
INNER JOIN producto P ON
F.codigo = P.codigo_fabricante;

-- 6. Lista los nombres de los fabricantes ordenados de forma descendente.

SELECT F.nombre FROM fabricante F ORDER BY F.nombre desc;

-- 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.

SELECT nombre FROM producto ORDER BY nombre ASC  ;

-- 8. Devuelve una lista con los 5 primeros fabricantes.

SELECT nombre FROM fabricante LIMIT 5;

-- 9. Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.

SELECT nombre FROM fabricante LIMIT 2 OFFSET 3;

-- 10. Lista el nombre y el precio del producto más barato

SELECT nombre, precio FROM producto ORDER BY precio ASC LIMIT 1;

-- 11. Lista el nombre y el precio del producto más caro

SELECT nombre, precio FROM producto ORDER BY precio DESC LIMIT 1;


-- 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.

SELECT nombre FROM producto WHERE codigo_fabricante = 2;

-- 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.

SELECT nombre FROM producto WHERE precio <= 120;

-- 14. Lista los productos que tienen un precio mayor o igual a 400€.

SELECT nombre FROM producto WHERE precio >= 400;

-- 15. Lista todos los productos que tengan un precio entre 80€ y 300€.

SELECT  P.nombre, P.precio FROM producto P
INNER JOIN fabricante F ON P.codigo_fabricante = f.codigo
WHERE P.precio BETWEEN 80 AND 300;

-- 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.

SELECT P.nombre, F.codigo FROM producto P
JOIN fabricante F ON P.codigo_fabricante = F.codigo
WHERE P.precio >200 AND F.codigo = 6;

-- 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.

SELECT P.nombre, P.codigo_fabricante FROM producto P
JOIN fabricante F ON P.codigo_fabricante = F.codigo
WHERE P.codigo_fabricante = 1 OR P.codigo_fabricante = 3 OR P.codigo_fabricante = 5;

-- 18. Lista el nombre y el precio de los productos en céntimos.

SELECT nombre, precio*100 AS Precio_Centimos FROM producto;

-- 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S

SELECT nombre FROM fabricante
WHERE nombre LIKE 'S%';

-- 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.

SELECT nombre FROM producto
WHERE nombre LIKE 'Portátil%';

-- 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.

SELECT nombre, precio FROM producto
WHERE nombre LIKE 'Monitor%' AND precio < 215;

-- 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente)

SELECT nombre, precio FROM producto
WHERE precio >= 180
ORDER BY precio AND nombre;

-- 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos. Ordene el resultado por el nombre del fabricante, por orden alfabético.

SELECT P.nombre, P.precio, F.nombre FROM producto P
JOIN fabricante F ON P.codigo_fabricante = F.codigo
ORDER BY F.nombre;

-- 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.

SELECT P.nombre, P.precio, F.nombre FROM producto P
 JOIN fabricante F ON P.codigo_fabricante = F.codigo
ORDER BY P.precio DESC
LIMIT 1;

-- 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.

SELECT P.nombre, F.nombre, P.precio FROM producto P
JOIN fabricante F ON P.codigo_fabricante = F.codigo
WHERE F.nombre = 'Crucial' AND P.precio > 200 ;

-- 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate.

SELECT P.nombre, F.nombre FROM producto P
JOIN fabricante F ON P.codigo_fabricante = F.codigo
WHERE F.nombre = 'Asus' OR  F.nombre = 'Hewlett-Packard' OR F.nombre = 'Seagate';

-- 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€. Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.

SELECT P.nombre, P.precio, F.codigo FROM producto P
JOIN fabricante F ON  P.codigo_fabricante = F.codigo
WHERE P.precio >= 180
ORDER BY P.precio AND P.nombre;

-- 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.

SELECT F.nombre, P.nombre
FROM fabricante F
LEFT JOIN producto P ON F.codigo = P.codigo_fabricante
ORDER BY F.nombre, P.nombre;

-- 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.

SELECT F.nombre FROM fabricante F
LEFT JOIN producto P ON F.codigo = P.codigo_fabricante
WHERE P.nombre IS NULL;

-- 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.

SELECT COUNT(*) AS prductos_totales FROM producto;

-- 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.

SELECT COUNT(DISTINCT F.nombre) AS fabricantes_con_productos FROM fabricante F
INNER JOIN producto P ON F.codigo = P.codigo_fabricante;

-- 32. Calcula la media del precio de todos los productos

SELECT AVG(precio) AS media_precio FROM producto;

-- 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.

SELECT MIN(precio) AS producto_mas_barato FROM producto;

-- 34. Calcula la suma de los precios de todos los productos.

SELECT SUM(precio) AS precios_total FROM producto;

-- 35. Calcula el número de productos que tiene el fabricante Asus.

SELECT COUNT(F.nombre) AS num_asus FROM fabricante F
JOIN producto P ON F.codigo = P.codigo_fabricante
WHERE F.nombre = 'Asus';

-- 36. Calcula la media del precio de todos los productos del fabricante Asus.

SELECT AVG(P.precio) AS precio_medio_asus FROM fabricante F
JOIN producto P ON F.codigo = P.codigo_fabricante
WHERE F.nombre = 'Asus';

-- 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".

SELECT AVG(P.precio) AS precio_medio,
MAX(P.precio) AS precio_max,
MIN(P.precio) AS precio_min
FROM fabricante F
JOIN producto P ON F.codigo = P.codigo_fabricante
WHERE F.nombre = 'Crucial';

-- 38. Muestra el número total de productos que tiene cada uno de los fabricantes. El listado también debe incluir los fabricantes que no tienen ningún producto. El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. Ordene el resultado descendentemente por el número de productos.

SELECT F.nombre, COUNT(P.codigo) AS total_productos
FROM fabricante F
LEFT JOIN producto P ON F.codigo = P.codigo_fabricante
GROUP BY F.nombre
ORDER BY total_productos DESC;

-- 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".

SELECT F.nombre, MAX(p.precio) AS precio_maximo,
MIN(p.precio) AS precio_minimo,
AVG(p.precio) AS precio_medio
FROM fabricante F
INNER JOIN producto P ON F.codigo = P.codigo_fabricante
GROUP BY F.nombre
ORDER BY F.nombre;

-- 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.

SELECT F.nombre,
MAX(p.precio) AS precio_maximo,
MIN(p.precio) AS precio_minimo,
AVG(p.precio) AS precio_medio,
COUNT(p.codigo) AS total_productos
FROM fabricante F INNER JOIN producto P ON F.codigo = P.codigo_fabricante
GROUP BY F.nombre
HAVING AVG(p.precio) > 200
ORDER BY precio_medio DESC;


-- 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.

SELECT F.nombre FROM fabricante F
INNER JOIN producto P ON P.codigo_fabricante = F.codigo
GROUP BY F.nombre
HAVING COUNT(P.codigo_fabricante) >= 2;

-- 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. Ordenado de mayor a menor número de productos.

SELECT F.nombre, COUNT(P.codigo) AS total_p FROM fabricante F
JOIN producto P ON F.codigo = P.codigo_fabricante
WHERE P.precio >= 220
GROUP BY F.nombre
ORDER BY total_p DESC;

-- 43. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €

SELECT F.nombre FROM fabricante F
JOIN producto P ON F.codigo = P.codigo_fabricante
GROUP BY F.nombre
HAVING SUM(P.precio) > 1000;

-- 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €. Ordenado de menor a mayor por cuantía de precio de los productos.

SELECT F.nombre, SUM(P.precio) AS suma_precio FROM fabricante F
JOIN producto P ON F.codigo = P.codigo_fabricante
GROUP BY F.nombre
HAVING SUM(P.precio) > 1000
ORDER BY suma_precio;

-- 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.

SELECT F.nombre AS fabricante, MAX(P.precio) AS precio_maximo
FROM producto P INNER JOIN fabricante F ON P.codigo_fabricante = F.codigo
GROUP BY F.nombre
ORDER BY F.nombre ASC;

-- 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante. Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.


