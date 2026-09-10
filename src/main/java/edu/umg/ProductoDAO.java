package edu.umg;

import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Filters.lt;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ProductoDAO implements AutoCloseable {

    private final MongoClient cliente;
    private final MongoCollection<Document> productos;

    public ProductoDAO() {
        cliente = ConexionMongo.conectar();
        MongoDatabase db = cliente.getDatabase("tienda");
        productos = db.getCollection("productos");
    }

    public void insertar(Producto producto) {
        Document documento = new Document("codigo", producto.getCodigo())
            .append("nombre", producto.getNombre())
            .append("categoria", producto.getCategoria())
            .append("precio", producto.getPrecio())
            .append("existencia", producto.getExistencia());

        productos.insertOne(documento);

        System.out.println("Producto insertado correctamente.");
    }

    public void listar() {
        for (Document documento : productos.find().sort(Sorts.descending("precio"))) {
            System.out.println(
                "Código: " + documento.getString("codigo") +
                " | Nombre: " + documento.getString("nombre") +
                " | Categoría: " + documento.getString("categoria") +
                " | Precio: " + documento.getDouble("precio") +
                " | Existencia: " + documento.getInteger("existencia")
            );
        }
    }

    public Document buscarPorCodigo(String codigo) {
        Document documento = productos.find(
            Filters.eq("codigo", codigo)
        ).first();

        if (documento == null) {
            System.out.println("Producto no encontrado.");
        }

        return documento;
    }

    public void actualizarExistencia(String codigo, int nuevaExistencia) {
        productos.updateOne(
            Filters.eq("codigo", codigo),
            Updates.set("existencia", nuevaExistencia)
        );

        System.out.println("Existencia actualizada correctamente.");
    }

    public void actualizarPrecio(String codigo, double nuevoPrecio) {
        productos.updateOne(
            Filters.eq("codigo", codigo),
            Updates.set("precio", nuevoPrecio)
        );

        System.out.println("Precio actualizado correctamente.");
    }

    public void eliminar(String codigo) {
        productos.deleteOne(
            Filters.eq("codigo", codigo)
        );

        System.out.println("Producto eliminado correctamente.");
    }

    public void listarPocoInventario(int limite) {
        for (Document documento : productos.find(
            lt("existencia", limite)
        )) {
            System.out.println(
                "Código: " + documento.getString("codigo") +
                " | Nombre: " + documento.getString("nombre") +
                " | Categoría: " + documento.getString("categoria") +
                " | Precio: " + documento.getDouble("precio") +
                " | Existencia: " + documento.getInteger("existencia")
                );
        }
    }

    @Override
    public void close() {
        cliente.close();
    }
}
