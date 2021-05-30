package jogasa.simarro.projectefinal.pojo;

import java.io.Serializable;

public class    Direccion implements Serializable {
    private String nombre,direccionEnvio,cPostal,poblacion,provincia,pais,idUser,idAddress;


    public Direccion(String nombre, String direccionEnvio, String cPostal, String poblacion, String provincia, String pais) {
        this.nombre = nombre;
        this.direccionEnvio = direccionEnvio;
        this.cPostal = cPostal;
        this.poblacion = poblacion;
        this.provincia = provincia;
        this.pais = pais;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public Direccion() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getcPostal() {
        return cPostal;
    }

    public void setcPostal(String cPostal) {
        this.cPostal = cPostal;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return  direccionEnvio + ',' + cPostal + ',' + poblacion + ',' + pais;
    }
}
