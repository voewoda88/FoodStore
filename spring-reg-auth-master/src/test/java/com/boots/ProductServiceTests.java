package com.boots;

import com.boots.entity.Category;
import com.boots.entity.Product;
import com.boots.repository.ProductRepository;
import com.boots.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(),
                new Product()
        );

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.allProducts();

        assertEquals(2, result.size());

        Mockito.verify(productRepository).findAll();
    }

    @Test
    public void testSaveProduct_ProductDoesNotExist() {
        Product newProduct = new Product();

        when(productRepository.findByName(newProduct.getName())).thenReturn(null);

        boolean result = productService.saveProduct(newProduct);

        assertTrue(result);

        Mockito.verify(productRepository).findByName(newProduct.getName());
        Mockito.verify(productRepository).save(newProduct);
    }

    @Test
    public void testSaveProduct_ProductExists() {
        Product existingProduct = new Product();

        when(productRepository.findByName(existingProduct.getName())).thenReturn(existingProduct);

        boolean result = productService.saveProduct(existingProduct);

        assertFalse(result);

        Mockito.verify(productRepository).findByName(existingProduct.getName());
        Mockito.verify(productRepository, Mockito.never()).save(existingProduct);
    }

    @Test
    public void testGetLastProductId_ProductExists() {
        Product latestProduct = new Product();

        when(productRepository.findFirstByOrderByIdDesc()).thenReturn(latestProduct);

        Long result = productService.getLastProductId();

        assertEquals(null, result);
    }

    @Test
    public void testGetLastProductId_NoProducts() {
        when(productRepository.findFirstByOrderByIdDesc()).thenReturn(null);

        Long result = productService.getLastProductId();

        assertEquals(0L, (long)result);
    }

    @Test
    public void testDeleteProduct_ProductExists() {
        Long productIdToDelete = 1L;

        when(productRepository.findById(productIdToDelete)).thenReturn(Optional.of(new Product()));

        boolean result = productService.deleteProduct(productIdToDelete);

        assertTrue(result);

        Mockito.verify(productRepository).findById(productIdToDelete);
        Mockito.verify(productRepository).deleteById(productIdToDelete);
    }

    @Test
    public void testDeleteProduct_ProductDoesNotExist() {
        Long productIdToDelete = 1L;

        when(productRepository.findById(productIdToDelete)).thenReturn(Optional.empty());

        boolean result = productService.deleteProduct(productIdToDelete);

        assertFalse(result);

        Mockito.verify(productRepository).findById(productIdToDelete);
        Mockito.verify(productRepository, Mockito.never()).deleteById(productIdToDelete);
    }

    @Test
    public void testFindProductById_ProductExists() {
        Long productIdToFind = 1L;
        Product foundProduct = new Product();

        when(productRepository.findById(productIdToFind)).thenReturn(Optional.of(foundProduct));

        Product result = productService.findProductById(productIdToFind);

        assertEquals(foundProduct, result);

        Mockito.verify(productRepository).findById(productIdToFind);
    }

    @Test
    public void testFindProductById_ProductDoesNotExist() {
        Long productIdToFind = 1L;

        when(productRepository.findById(productIdToFind)).thenReturn(Optional.empty());

        Product result = productService.findProductById(productIdToFind);

        assertEquals(new Product(), result);

        Mockito.verify(productRepository).findById(productIdToFind);
    }

}
