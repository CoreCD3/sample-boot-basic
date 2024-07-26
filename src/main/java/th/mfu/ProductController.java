package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import th.mfu.domain.Customer;
import th.mfu.domain.Product;
import th.mfu.dto.CustomerDTO;
import th.mfu.dto.ProductDTO;
import th.mfu.dto.mapper.ProductMapper;
import th.mfu.repository.ProductRepository;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepo;

    @Autowired
    ProductMapper productMapper;

    // GET for a product
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        if (!productRepo.existsById(id))
            return new ResponseEntity<ProductDTO>(HttpStatus.NOT_FOUND);
        Optional<Product> product = productRepo.findById(id);
        ProductDTO dto = new ProductDTO();
        productMapper.updateProductFromEntity(product.get(), dto);
        return new ResponseEntity<ProductDTO>(dto, HttpStatus.OK);
    }

    // Get all products
    @GetMapping("/products")
    public ResponseEntity<Collection<ProductDTO>> getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductDTO> dtos = new ArrayList<ProductDTO>();
        productMapper.updateProductFromEntity(products, dtos);
        return new ResponseEntity<Collection<ProductDTO>>(dtos, HttpStatus.OK);
    }

    // POST for creating a product
    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
        Product newProduct = new Product();
        productMapper.updateProductFromDto(productDTO, newProduct);
        productRepo.save(newProduct);
        return new ResponseEntity<String>("Product created", HttpStatus.CREATED);
    }
    
    @PatchMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
    if (!productRepo.existsById(id)) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    Optional<Product> productOpt = productRepo.findById(id);
    if (productOpt.isPresent()) {
        Product product = productOpt.get();
        productMapper.updateProductFromDto(productDTO, product);
        productRepo.save(product);
        return new ResponseEntity<>("Product updated", HttpStatus.OK);
        } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

    
    // DELETE for deleting a product by id
    @DeleteMapping("products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return new ResponseEntity<String>("Product deleted", HttpStatus.NO_CONTENT);
    }

}
