package th.mfu.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import th.mfu.domain.SaleOrder;

public class SaleOrderDTO {

    private Long id;
    private String notes;
    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    
}
