package eu.avalonya.api.models;

import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractModel {

    private boolean created = false;

    public abstract Pair<String, String> getId();

    public abstract Map<String, String> getRepositoryAttributes();

}
