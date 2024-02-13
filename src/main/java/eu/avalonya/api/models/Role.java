package eu.avalonya.api.models;

import org.bukkit.Color;

public class Role {

    private String role;
    private Citizen citizen;


    public Role(Citizen citizen){

        this.citizen = citizen;
        this.role = null;

    }

    public boolean setRole(String role) {

        if (citizen.getTown().getRoles().containsKey(role)){
            this.role = role;
            return true;
        }
        return false;
    }

    public String getColor(){
        String color = citizen.getTown().getRoles().get(role);
        return color;
    }

    public String getRole() {
        return role;
    }
}
