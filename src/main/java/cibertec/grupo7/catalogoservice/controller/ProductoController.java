package cibertec.grupo7.catalogoservice.controller;

import cibertec.grupo7.catalogoservice.exception.StockInsuficienteException;
import cibertec.grupo7.catalogoservice.model.entity.Producto;
import cibertec.grupo7.catalogoservice.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Público / CLIENTE: Catálogo con filtros
    @GetMapping
    public ResponseEntity<List<Producto>> verCatalogo(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String talla,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca) {
        return ResponseEntity.ok(productoService.listarCatalogo(genero, talla, categoria, marca));
    }

    @GetMapping("/{idproducto}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Integer idproducto) {
        return ResponseEntity.ok(productoService.obtenerPorId(idproducto));
    }

    // ADMIN: CRUD de productos
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<Producto>> listarTodosAdmin() {
        return ResponseEntity.ok(productoService.listarTodosAdmin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(producto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idproducto}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer idproducto, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizarProducto(idproducto, producto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idproducto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idproducto) {
        productoService.eliminarProducto(idproducto);
        return ResponseEntity.noContent().build();
    }
}