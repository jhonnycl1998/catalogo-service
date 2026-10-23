package cibertec.grupo7.catalogoservice.repository;

import cibertec.grupo7.catalogoservice.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT p FROM Producto p WHERE p.activo = true " +
            "AND (:genero IS NULL OR :genero = '' OR LOWER(p.genero) = LOWER(:genero)) " +
            "AND (:talla IS NULL OR :talla = '' OR LOWER(p.talla) = LOWER(:talla)) " +
            "AND (:categoria IS NULL OR :categoria = '' OR LOWER(p.categoria) = LOWER(:categoria)) " +
            "AND (:marca IS NULL OR :marca = '' OR LOWER(p.marca) = LOWER(:marca))")
    List<Producto> buscarConFiltros(
            @Param("genero") String genero,
            @Param("talla") String talla,
            @Param("categoria") String categoria,
            @Param("marca") String marca
    );

    // UPDATE condicional requerido: stock = stock - cantidad WHERE stock >= cantidad
    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.idproducto = :idproducto AND p.stock >= :cantidad")
    int descontarStockCondicional(@Param("idproducto") Integer idproducto, @Param("cantidad") Integer cantidad);
}