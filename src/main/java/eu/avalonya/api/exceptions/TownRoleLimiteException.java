package eu.avalonya.api.exceptions;

public class TownRoleLimiteException extends Exception{
    public TownRoleLimiteException() {
        super("On ne peut pas créer plus de rôle dans une ville");
    }
}
