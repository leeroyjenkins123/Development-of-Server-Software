package io.swagger.entity;

import javax.persistence.*;

@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "name", nullable = false)
    private String name;

    public ServiceEntity(){

    }

    public ServiceEntity(Long serviceId, String name){
        this.serviceId = serviceId;
        this.name = name;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
