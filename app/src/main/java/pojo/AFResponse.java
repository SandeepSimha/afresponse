package pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chsan_000 on 8/2/2016.
 */
public class AFResponse implements Serializable {
    private List<Promotions> promotions;

    public List<Promotions> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotions> promotions) {
        this.promotions = promotions;
    }
}
