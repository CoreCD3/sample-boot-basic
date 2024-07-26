package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import th.mfu.domain.Customer;
import th.mfu.domain.SaleOrder;
import th.mfu.dto.CustomerDTO;
import th.mfu.dto.SaleOrderDTO;
import th.mfu.dto.mapper.SaleOrderMapper;
import th.mfu.dto.mapper.CustomerMapper;
import th.mfu.repository.CustomerRepository;
import th.mfu.repository.ProductRepository;
import th.mfu.repository.SaleOrderRepository;


@RestController
public class SaleOrderController {
    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    SaleOrderRepository orderRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    SaleOrderMapper saleOrderMapper;

    // POST for creating an order
    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<String> createOrder(@PathVariable Long customerId, @RequestBody SaleOrderDTO orderDTO) {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if (customer.isPresent()) {
            SaleOrder newOrder = new SaleOrder();
            saleOrderMapper.updateSaleOrderFromDto(orderDTO, newOrder);
            newOrder.setCustomer(customer.get());
            orderRepo.save(newOrder);
            return new ResponseEntity<>("Order created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<List<SaleOrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        Optional<Customer> optCustomer = customerRepo.findById(customerId);
        if (!optCustomer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Customer customer = optCustomer.get();
        Collection<SaleOrder> orders = orderRepo.findByCustomerId(customerId);
        List<SaleOrderDTO> orderDTOs = new ArrayList<>();
        for (SaleOrder order : orders) {
            SaleOrderDTO orderDTO = new SaleOrderDTO();
            saleOrderMapper.updateSaleOrderFromEntity(order, orderDTO);
    
            CustomerDTO customerDTO = new CustomerDTO();
            customerMapper.updateCustomerFromEntity(customer, customerDTO);
            orderDTO.setCustomer(customerDTO);
    
            orderDTOs.add(orderDTO);
        }
    
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }
    


    @PatchMapping("/customers/{customerId}/orders")
    public ResponseEntity<String> updateSaleOrder(@PathVariable Long customerId, @RequestBody SaleOrderDTO saleOrderDTO) {
    if (!orderRepo.existsById(customerId)) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    Optional<SaleOrder> saleOrderOpt = orderRepo.findById(customerId);
    if (saleOrderOpt.isPresent()) {
        SaleOrder saleOrder = saleOrderOpt.get();
        saleOrderMapper.updateSaleOrderFromDto(saleOrderDTO, saleOrder);
        orderRepo.save(saleOrder);
        return new ResponseEntity<>("SaleOrder updated", HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


      // GET for getting all orders
    @GetMapping("/orders")
    public ResponseEntity<Collection<SaleOrderDTO>> getAllOrders() {
        System.out.println("getAllOrders called...");
        Collection<SaleOrder> orders = orderRepo.findAll();
        List<SaleOrderDTO> dtos = new ArrayList<>();
        saleOrderMapper.updateSaleOrderFromEntity(new ArrayList<>(orders), dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}