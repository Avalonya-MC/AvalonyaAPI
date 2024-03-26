package eu.avalonya.api.models.dao;

import eu.avalonya.api.models.Ally;
import eu.avalonya.api.models.Town;

public class AllyDao {

    public void askAlly(Town sender, Town receiver)
    {
        Ally ally = new Ally(sender, receiver);
    }


}
