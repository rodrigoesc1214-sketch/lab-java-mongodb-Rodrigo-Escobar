package edu.umg;

import org.bson.Document;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Java + MongoDB Atlas ===");

        if (!ConexionMongo.probarConexion()) {
            System.out.println("Revise MONGODB_URI y la configuración de red de Atlas.");
            return;
        }

        System.out.println("Conexión exitosa.");

        Scanner scanner = new Scanner(System.in);
        int opcion;

    try (ProductoDAO dao = new ProductoDAO()) {

        do {
            System.out.println("\n================================");
            System.out.println("       TIENDA - MONGODB ATLAS");
            System.out.println("================================");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Buscar producto");
            System.out.println("4. Actualizar precio");
            System.out.println("5. Actualizar existencia");
            System.out.println("6. Eliminar producto");
            System.out.println("7. Productos con poco inventario");
            System.out.println("0. Salir");
            System.out.print("\nSeleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Debe ingresar un número válido.");
                scanner.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Código: ");
                    String codigo = scanner.nextLine();

                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();

                    System.out.print("Categoría: ");
                    String categoria = scanner.nextLine();

                    System.out.print("Precio: ");
                    double precio = scanner.nextDouble();

                    System.out.print("Existencia: ");
                    int existencia = scanner.nextInt();
                    scanner.nextLine();

                    if (precio < 0 || existencia < 0) {
                        System.out.println("El precio y la existencia no pueden ser negativos.");
                        break;
                    }

                    Producto producto = new Producto(
                            codigo,
                            nombre,
                            categoria,
                            precio,
                            existencia
                    );

                    dao.insertar(producto);
                    break;

                case 2:
                    System.out.println("\n=== LISTA DE PRODUCTOS ===");
                    dao.listar();
                    break;

                case 3:
                    System.out.print("Ingrese el codigo del producto: ");
                    String codigoBuscar = scanner.nextLine();

                    Document encontrado = dao.buscarPorCodigo(codigoBuscar);

                    if (encontrado != null) {
                        System.out.println("\n=== PRODUCTO ENCONTRADO ===");
                        System.out.println(
                                "Código: " + encontrado.getString("codigo") +
                                " | Nombre: " + encontrado.getString("nombre") +
                                " | Categoría: " + encontrado.getString("categoria") +
                                " | Precio: " + encontrado.getDouble("precio") +
                                " | Existencia: " + encontrado.getInteger("existencia")
                        );
                    }
                    break;

                case 4:
                    System.out.print("Ingrese el código del producto: ");
                    String codigoPrecio = scanner.nextLine();

                    Document productoPrecio = dao.buscarPorCodigo(codigoPrecio);

                    if (productoPrecio == null) {
                        break;
                    }

                    System.out.print("Ingrese el nuevo precio: ");
                    double nuevoPrecio = scanner.nextDouble();
                    scanner.nextLine();

                    if (nuevoPrecio < 0) {
                        System.out.println("El precio no puede ser negativo.");
                        break;
                    }

                    dao.actualizarPrecio(codigoPrecio, nuevoPrecio);
                break;

                case 5:
                    System.out.print("Ingrese el código del producto: ");
                    String codigoExistencia = scanner.nextLine();

                    Document productoExistencia = dao.buscarPorCodigo(codigoExistencia);

                    if (productoExistencia == null) {
                        break;
                    }

                    System.out.print("Ingrese la nueva existencia: ");
                    int nuevaExistencia = scanner.nextInt();
                    scanner.nextLine();

                    if (nuevaExistencia < 0) {
                        System.out.println("La existencia no puede ser negativa.");
                        break;
                    }

                    dao.actualizarExistencia(codigoExistencia, nuevaExistencia);
                    break;
                case 6:
                    System.out.print("Ingrese el código del producto: ");
                    String codigoEliminar = scanner.nextLine();

                    Document productoEliminar = dao.buscarPorCodigo(codigoEliminar);

                    if (productoEliminar == null) {
                        break;
                    }

                    dao.eliminar(codigoEliminar);
                    break;

                case 7:
                    System.out.print("Ingrese el límite de existencia: ");
                    int limite = scanner.nextInt();
                    scanner.nextLine();

                    if (limite < 0) {
                        System.out.println("El límite no puede ser negativo.");
                        break;
                    }

                    System.out.println("\n=== PRODUCTOS CON POCO INVENTARIO ===");
                    dao.listarPocoInventario(limite);
                    break;

                case 0:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    scanner.close();
    }
}
