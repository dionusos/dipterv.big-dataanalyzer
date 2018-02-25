package hu.denes.bme.dipterv.data.sql;

import java.util.ArrayList;
import java.util.List;

public class MSSqlQuery extends Query {
    private boolean limitSet = false;

    @Override
    public String toString() {
        if(!limitSet && limit != null && limit > 0) {
            limitSet = true;
            List<Select> s = new ArrayList<>();
            s.add(new Select("TOP " + limit, null));
            s.addAll(this.selects);
            this.selects = s;
        }
        return super.toString();
    }
}
