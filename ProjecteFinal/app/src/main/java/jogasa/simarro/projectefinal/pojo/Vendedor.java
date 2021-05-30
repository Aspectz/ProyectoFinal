package jogasa.simarro.projectefinal.pojo;

import java.io.Serializable;

public class Vendedor implements Serializable {
    private String id;
    private String companyName;
    private String email;


    public Vendedor(){}
    
    
    public Vendedor(String id, String companyName, String email) {
        this.id = id;
        this.companyName = companyName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
