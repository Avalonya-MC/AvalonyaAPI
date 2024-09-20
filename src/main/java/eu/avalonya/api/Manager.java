package eu.avalonya.api;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Manager {
    private final AvalonyaAPI api;

    public AvalonyaAPI api() { return api; }
}
