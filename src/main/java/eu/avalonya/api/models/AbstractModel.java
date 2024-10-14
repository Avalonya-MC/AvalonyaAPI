package eu.avalonya.api.models;

import eu.avalonya.api.models.serialization.ModelSerializable;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractModel implements ModelSerializable {

    private boolean created = false;

    public abstract Pair<String, String> getId();

    public abstract Map<String, String> getRepositoryAttributes();

}
