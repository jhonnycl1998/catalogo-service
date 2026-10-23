package cibertec.grupo7.catalogoservice.service;

import cibertec.grupo7.catalogoservice.exception.StockInsuficienteException;
import cibertec.grupo7.catalogoservice.model.entity.Producto;
import cibertec.grupo7.catalogoservice.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarCatalogo(String genero, String talla, String categoria, String marca) {
        return productoRepository.buscarConFiltros(genero, talla, categoria, marca);
    }

    public List<Producto> listarTodosAdmin() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Integer idproducto) {
        return productoRepository.findById(idproducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Integer idproducto, Producto datos) {
        Producto existente = obtenerPorId(idproducto);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setGenero(datos.getGenero());
        existente.setTalla(datos.getTalla());
        existente.setCategoria(datos.getCategoria());
        existente.setMarca(datos.getMarca());
        existente.setImagenUrl(datos.getImagenUrl());
        if (datos.getActivo() != null) {
            existente.setActivo(datos.getActivo());
        }
        return productoRepository.save(existente);
    }

    @Transactional
    public void eliminarProducto(Integer idproducto) {
        Producto existente = obtenerPorId(idproducto);
        existente.setActivo(false);
        productoRepository.save(existente);
    }

    // Descuento condicional atómico invocado en el checkout
    @Transactional
    public void descontarStock(Integer idproducto, Integer cantidad) {
        int filasAfectadas = productoRepository.descontarStockCondicional(idproducto, cantidad);
        if (filasAfectadas == 0) {
            throw new StockInsuficienteException("Stock insuficiente para el producto ID: " + idproducto);
        }
    }
}