package com.sdl.webapp.common.model.entity;

import com.sdl.webapp.common.mapping.SemanticEntity;
import com.sdl.webapp.common.mapping.SemanticProperty;

@SemanticEntity(entityName = "Place", vocab = "http://schema.org", prefix = "s", pub = true)
public class Place extends EntityBase {

    @SemanticProperty("s:name")
    private String name;

    @SemanticProperty("s:image")
    private Image image;

    @SemanticProperty("s:address")
    private String address;

    @SemanticProperty("s:telephone")
    private String telephone;

    @SemanticProperty("s:faxNumber")
    private String faxNumber;

    @SemanticProperty("s:email")
    private String email;

    @SemanticProperty("s:geo")
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}