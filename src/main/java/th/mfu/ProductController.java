package th.mfu;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;

@RestController
public class ProductController {

  @Autowired
  private ProductRepository productRepo; // Replace with your actual ProductRepository interface

  // GET for a product
  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Optional<Product> product = productRepo.findById(id);
    if (!product.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(product.get(), HttpStatus.OK);
  }

  // Get all products
  @GetMapping("/products")
  public ResponseEntity<Collection<Product>> getAllProducts() {
    return new ResponseEntity<>(productRepo.findAll(), HttpStatus.OK);
  }

  // POST for creating a product
  @PostMapping("/products")
  public ResponseEntity<String> createProduct(@RequestBody Product product) {
    productRepo.save(product);
    return new ResponseEntity<>("Product created", HttpStatus.CREATED);
  }

  // DELETE for deleting a product by id
  @DeleteMapping("/products/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    productRepo.deleteById(id);
    return new ResponseEntity<>("Product deleted", HttpStatus.NO_CONTENT);
  }
}
