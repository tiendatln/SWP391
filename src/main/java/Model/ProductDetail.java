/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author Nguyễn Trường Vinh _ vinhntca181278
 */
public class ProductDetail {
     private int productDetailID;
    private Product productID;
    private String operatingSystem;
    private String cpuTechnology;
    private Integer coreCount;
    private Integer threadCount;
    private String cpuSpeed;
    private String gpu;
    private Integer ram;
    private String ramType;
    private String ramBusSpeed;
    private Integer maxRam;
    private String storage;
    private String memoryCard;
    private String screen;
    private String resolution;
    private String refreshRate;
    private String batteryCapacity;
    private String batteryType;
    private String maxChargingSupport;
    private String releaseDate;
    private String origin;

    public ProductDetail() {
    }

    public ProductDetail(int productDetailID, Product productID, String operatingSystem, String cpuTechnology, Integer coreCount, Integer threadCount, String cpuSpeed, String gpu, Integer ram, String ramType, String ramBusSpeed, Integer maxRam, String storage, String memoryCard, String screen, String resolution, String refreshRate, String batteryCapacity, String batteryType, String maxChargingSupport, String releaseDate, String origin) {
        this.productDetailID = productDetailID;
        this.productID = productID;
        this.operatingSystem = operatingSystem;
        this.cpuTechnology = cpuTechnology;
        this.coreCount = coreCount;
        this.threadCount = threadCount;
        this.cpuSpeed = cpuSpeed;
        this.gpu = gpu;
        this.ram = ram;
        this.ramType = ramType;
        this.ramBusSpeed = ramBusSpeed;
        this.maxRam = maxRam;
        this.storage = storage;
        this.memoryCard = memoryCard;
        this.screen = screen;
        this.resolution = resolution;
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.batteryType = batteryType;
        this.maxChargingSupport = maxChargingSupport;
        this.releaseDate = releaseDate;
        this.origin = origin;
    }

    public ProductDetail(Product productID, String operatingSystem, String cpuTechnology, Integer coreCount, Integer threadCount, String cpuSpeed, String gpu, Integer ram, String ramType, String ramBusSpeed, Integer maxRam, String storage, String memoryCard, String screen, String resolution, String refreshRate, String batteryCapacity, String batteryType, String maxChargingSupport, String releaseDate, String origin) {
        this.productID = productID;
        this.operatingSystem = operatingSystem;
        this.cpuTechnology = cpuTechnology;
        this.coreCount = coreCount;
        this.threadCount = threadCount;
        this.cpuSpeed = cpuSpeed;
        this.gpu = gpu;
        this.ram = ram;
        this.ramType = ramType;
        this.ramBusSpeed = ramBusSpeed;
        this.maxRam = maxRam;
        this.storage = storage;
        this.memoryCard = memoryCard;
        this.screen = screen;
        this.resolution = resolution;
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.batteryType = batteryType;
        this.maxChargingSupport = maxChargingSupport;
        this.releaseDate = releaseDate;
        this.origin = origin;
    }
    

    public ProductDetail(Product productID) {
        this.productID = productID;
    }

    public ProductDetail(String operatingSystem, String cpuTechnology, Integer coreCount, Integer threadCount, String cpuSpeed, String gpu, Integer ram, String ramType, String ramBusSpeed, Integer maxRam, String storage, String memoryCard, String screen, String resolution, String refreshRate, String batteryCapacity, String batteryType, String maxChargingSupport, String releaseDate, String origin) {
        this.operatingSystem = operatingSystem;
        this.cpuTechnology = cpuTechnology;
        this.coreCount = coreCount;
        this.threadCount = threadCount;
        this.cpuSpeed = cpuSpeed;
        this.gpu = gpu;
        this.ram = ram;
        this.ramType = ramType;
        this.ramBusSpeed = ramBusSpeed;
        this.maxRam = maxRam;
        this.storage = storage;
        this.memoryCard = memoryCard;
        this.screen = screen;
        this.resolution = resolution;
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.batteryType = batteryType;
        this.maxChargingSupport = maxChargingSupport;
        this.releaseDate = releaseDate;
        this.origin = origin;
    }

    public ProductDetail(int aInt, String string, String string0, int aInt0, int aInt1, int aInt2, double aDouble, String string1, int aInt3, String string2, String string3) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

    

    public int getProductDetailID() {
        return productDetailID;
    }

    public void setProductDetailID(int productDetailID) {
        this.productDetailID = productDetailID;
    }

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

   
    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getCpuTechnology() {
        return cpuTechnology;
    }

    public void setCpuTechnology(String cpuTechnology) {
        this.cpuTechnology = cpuTechnology;
    }

    public Integer getCoreCount() {
        return coreCount;
    }

    public void setCoreCount(Integer coreCount) {
        this.coreCount = coreCount;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public String getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(String cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    public String getRamBusSpeed() {
        return ramBusSpeed;
    }

    public void setRamBusSpeed(String ramBusSpeed) {
        this.ramBusSpeed = ramBusSpeed;
    }

    public Integer getMaxRam() {
        return maxRam;
    }

    public void setMaxRam(Integer maxRam) {
        this.maxRam = maxRam;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getMemoryCard() {
        return memoryCard;
    }

    public void setMemoryCard(String memoryCard) {
        this.memoryCard = memoryCard;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public String getMaxChargingSupport() {
        return maxChargingSupport;
    }

    public void setMaxChargingSupport(String maxChargingSupport) {
        this.maxChargingSupport = maxChargingSupport;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "productDetailID=" + productDetailID +
                ", productID=" + productID +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", cpuTechnology='" + cpuTechnology + '\'' +
                ", coreCount=" + coreCount +
                ", threadCount=" + threadCount +
                ", cpuSpeed='" + cpuSpeed + '\'' +
                ", gpu='" + gpu + '\'' +
                ", ram=" + ram +
                ", ramType='" + ramType + '\'' +
                ", ramBusSpeed='" + ramBusSpeed + '\'' +
                ", maxRam=" + maxRam +
                ", storage='" + storage + '\'' +
                ", memoryCard='" + memoryCard + '\'' +
                ", screen='" + screen + '\'' +
                ", resolution='" + resolution + '\'' +
                ", refreshRate='" + refreshRate + '\'' +
                ", batteryCapacity='" + batteryCapacity + '\'' +
                ", batteryType='" + batteryType + '\'' +
                ", maxChargingSupport='" + maxChargingSupport + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }

    
    
}
